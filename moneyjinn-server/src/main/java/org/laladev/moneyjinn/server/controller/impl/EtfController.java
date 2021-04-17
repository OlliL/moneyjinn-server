package org.laladev.moneyjinn.server.controller.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.etf.CalcEtfSaleRequest;
import org.laladev.moneyjinn.core.rest.model.etf.CalcEtfSaleResponse;
import org.laladev.moneyjinn.core.rest.model.etf.ListEtfFlowsResponse;
import org.laladev.moneyjinn.core.rest.model.etf.ListEtfOverviewResponse;
import org.laladev.moneyjinn.core.rest.model.etf.transport.EtfFlowTransport;
import org.laladev.moneyjinn.core.rest.model.etf.transport.EtfSummaryTransport;
import org.laladev.moneyjinn.core.rest.model.etf.transport.EtfTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.model.etf.Etf;
import org.laladev.moneyjinn.model.etf.EtfFlow;
import org.laladev.moneyjinn.model.etf.EtfIsin;
import org.laladev.moneyjinn.model.etf.EtfValue;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSaleAskPrice;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSaleBidPrice;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSaleIsin;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSalePieces;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSaleTransactionCosts;
import org.laladev.moneyjinn.server.annotation.RequiresAuthorization;
import org.laladev.moneyjinn.server.controller.mapper.EtfFlowTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.EtfTransportMapper;
import org.laladev.moneyjinn.service.api.IEtfService;
import org.laladev.moneyjinn.service.api.ISettingService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequestMapping("/moneyflow/server/etf/")
// TODO: Multi-User
// TODO: Multi-ETF
// TODO: Enter ETF Buys/Sels
// TODO: Think about how to display the portfolio when sales happened (FIFO)
// TODO: Unit-Testing
public class EtfController extends AbstractController {

	private static final BigDecimal TAX_RELEVANT_PERCENTAGE = new BigDecimal(70).scaleByPowerOfTen(-2);
	@Inject
	private IEtfService etfService;
	@Inject
	private ISettingService settingService;

	@RequestMapping(value = "listEtfOverview/{year}/{month}", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ListEtfOverviewResponse listEtfOverview(@PathVariable(value = "year") final Short requestYear,
			@PathVariable(value = "month") final Short requestMonth) {
		final Month month = Month.of(requestMonth.intValue());
		final LocalDate beginOfMonth = LocalDate.of(requestYear.intValue(), month, 1);
		final LocalDate endOfMonth = beginOfMonth.with(TemporalAdjusters.lastDayOfMonth());

		final ListEtfOverviewResponse response = new ListEtfOverviewResponse();
		final List<EtfSummaryTransport> transports = new ArrayList<>();

		final List<Etf> etfs = this.etfService.getAllEtf();
		for (final Etf etf : etfs) {
			final EtfValue etfValue = this.etfService.getEtfValueEndOfMonth(etf.getId(), requestYear, month);
			final List<EtfFlow> etfFlows = this.etfService.getAllEtfFlowsUntil(etf.getId(), endOfMonth);

			final EtfSummaryTransport transport = new EtfSummaryTransport();
			transport.setIsin(etf.getId().getId());
			transport.setName(etf.getName());
			transport.setChartUrl(etf.getChartUrl());

			transport.setBuyPrice(etfValue.getBuyPrice());
			transport.setSellPrice(etfValue.getSellPrice());
			transport.setPricesTimestamp(Timestamp.valueOf(etfValue.getChangeDate()));

			BigDecimal amount = BigDecimal.ZERO;
			BigDecimal spentValue = BigDecimal.ZERO;
			for (final EtfFlow flow : etfFlows) {
				amount = amount.add(flow.getAmount());
				spentValue = spentValue.add(flow.getAmount().multiply(flow.getPrice()));
			}
			transport.setAmount(amount);
			transport.setSpentValue(spentValue);

			transports.add(transport);
		}

		response.setEtfSummaryTransports(transports);
		return response;
	}

	@RequestMapping(value = "listEtfFlows", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ListEtfFlowsResponse listEtfFlows() {
		final ListEtfFlowsResponse response = new ListEtfFlowsResponse();

		final List<Etf> etfs = this.etfService.getAllEtf();
		response.setEtfTransports(super.mapList(etfs, EtfTransport.class));

		final List<EtfFlowTransport> transports = new ArrayList<>();
		for (final Etf etf : etfs) {
			final List<EtfFlow> etfFlows = this.etfService.getAllEtfFlowsUntil(etf.getId(), LocalDate.now());
			transports.addAll(super.mapList(etfFlows, EtfFlowTransport.class));
		}
		response.setEtfFlowTransports(transports);

		this.settingService.getClientCalcEtfSaleAskPrice(this.getUserId())
				.ifPresent(s -> response.setCalcEtfAskPrice(s.getSetting()));
		this.settingService.getClientCalcEtfSaleBidPrice(this.getUserId())
				.ifPresent(s -> response.setCalcEtfBidPrice(s.getSetting()));
		this.settingService.getClientCalcEtfSaleIsin(this.getUserId())
				.ifPresent(s -> response.setCalcEtfSaleIsin(s.getSetting()));
		this.settingService.getClientCalcEtfSalePieces(this.getUserId())
				.ifPresent(s -> response.setCalcEtfSalePieces(s.getSetting()));
		this.settingService.getClientCalcEtfSaleTransactionCosts(this.getUserId())
				.ifPresent(s -> response.setCalcEtfTransactionCosts(s.getSetting()));

		return response;
	}

	@RequestMapping(value = "calcEtfSale", method = { RequestMethod.PUT })
	@RequiresAuthorization
	public CalcEtfSaleResponse calcEtfSale(@RequestBody final CalcEtfSaleRequest request) {
		final CalcEtfSaleResponse response = new CalcEtfSaleResponse();

		this.settingService.setClientCalcEtfSaleAskPrice(this.getUserId(),
				new ClientCalcEtfSaleAskPrice(request.getAskPrice()));
		this.settingService.setClientCalcEtfSaleBidPrice(this.getUserId(),
				new ClientCalcEtfSaleBidPrice(request.getBidPrice()));
		this.settingService.setClientCalcEtfSaleIsin(this.getUserId(), new ClientCalcEtfSaleIsin(request.getIsin()));
		this.settingService.setClientCalcEtfSalePieces(this.getUserId(),
				new ClientCalcEtfSalePieces(request.getPieces()));
		this.settingService.setClientCalcEtfSaleTransactionCosts(this.getUserId(),
				new ClientCalcEtfSaleTransactionCosts(request.getTransactionCosts()));

		final EtfIsin etfIsin = new EtfIsin(request.getIsin());

		final BigDecimal pieces = request.getPieces();
		BigDecimal openPieces = pieces;
		BigDecimal originalBuyPrice = BigDecimal.ZERO;

		final List<EtfFlow> etfFlows = this.etfService.getAllEtfFlowsUntil(etfIsin, LocalDate.now());
		Collections.reverse(etfFlows); // reverse order - FIFO!

		for (final EtfFlow etfFlow : etfFlows) {
			BigDecimal useablePieces = etfFlow.getAmount();
			if (useablePieces.compareTo(openPieces) == 1) {
				useablePieces = openPieces;
			}
			openPieces = openPieces.subtract(useablePieces);
			originalBuyPrice = originalBuyPrice.add(useablePieces.multiply(etfFlow.getPrice()));
		}

		if (BigDecimal.ZERO.compareTo(openPieces) != 0) {
			final ValidationItemTransport valItem = new ValidationItemTransport();
			valItem.setError(ErrorCode.AMOUNT_TO_HIGH.getErrorCode());
			response.setResult(Boolean.FALSE);
			response.setValidationItemTransports(Arrays.asList(valItem));
		} else {

			final BigDecimal newBuyPrice = request.getAskPrice().multiply(pieces);
			final BigDecimal sellPrice = request.getBidPrice().multiply(pieces);
			final BigDecimal transactionCosts = request.getTransactionCosts().multiply(BigDecimal.valueOf(2));
			final BigDecimal profit = sellPrice.subtract(originalBuyPrice);
			final BigDecimal chargeable = profit.multiply(TAX_RELEVANT_PERCENTAGE).setScale(2, RoundingMode.UP);
			final BigDecimal rebuyLosses = newBuyPrice.subtract(sellPrice);
			final BigDecimal overallCosts = rebuyLosses.add(transactionCosts);

			response.setNewBuyPrice(newBuyPrice);
			response.setSellPrice(sellPrice);
			response.setTransactionCosts(transactionCosts);
			response.setIsin(etfIsin.getId());
			response.setPieces(pieces);
			response.setOriginalBuyPrice(originalBuyPrice);
			response.setProfit(profit);
			response.setChargeable(chargeable);
			response.setRebuyLosses(rebuyLosses);
			response.setOverallCosts(overallCosts);
		}
		return response;

	}

	@Override
	protected void addBeanMapper() {
		super.registerBeanMapper(new EtfFlowTransportMapper());
		super.registerBeanMapper(new EtfTransportMapper());

	}

}
