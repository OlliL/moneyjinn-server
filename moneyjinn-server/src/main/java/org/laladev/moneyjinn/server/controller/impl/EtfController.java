//Copyright (c) 2021-2024 Oliver Lehmann <lehmann@ans-netz.de>
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without
//modification, are permitted provided that the following conditions
//are met:
//1. Redistributions of source code must retain the above copyright
//notice, this list of conditions and the following disclaimer
//2. Redistributions in binary form must reproduce the above copyright
//notice, this list of conditions and the following disclaimer in the
//documentation and/or other materials provided with the distribution.
//
//THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
//ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
//IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
//ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
//FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
//DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
//OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
//HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
//LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
//OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
//SUCH DAMAGE.

package org.laladev.moneyjinn.server.controller.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.etf.Etf;
import org.laladev.moneyjinn.model.etf.EtfFlow;
import org.laladev.moneyjinn.model.etf.EtfFlowComparator;
import org.laladev.moneyjinn.model.etf.EtfFlowWithTaxInfo;
import org.laladev.moneyjinn.model.etf.EtfID;
import org.laladev.moneyjinn.model.etf.EtfValue;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSaleAskPrice;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSaleBidPrice;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSalePieces;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSaleTransactionCostsAbsolute;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSaleTransactionCostsRelative;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.server.controller.api.EtfControllerApi;
import org.laladev.moneyjinn.server.controller.mapper.EtfEffectiveFlowTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.EtfFlowTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.EtfTransportMapper;
import org.laladev.moneyjinn.server.model.CalcEtfSaleRequest;
import org.laladev.moneyjinn.server.model.CalcEtfSaleResponse;
import org.laladev.moneyjinn.server.model.EtfEffectiveFlowTransport;
import org.laladev.moneyjinn.server.model.EtfFlowTransport;
import org.laladev.moneyjinn.server.model.EtfSummaryTransport;
import org.laladev.moneyjinn.server.model.ListEtfFlowsResponse;
import org.laladev.moneyjinn.server.model.ListEtfOverviewResponse;
import org.laladev.moneyjinn.service.api.IEtfService;
import org.laladev.moneyjinn.service.api.ISettingService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class EtfController extends AbstractController implements EtfControllerApi {
	private static final BigDecimal TAX_RELEVANT_PERCENTAGE = new BigDecimal(70).scaleByPowerOfTen(-2);
	private final IEtfService etfService;
	private final ISettingService settingService;
	private final EtfTransportMapper etfTransportMapper;
	private final EtfFlowTransportMapper etfFlowTransportMapper;
	private final EtfEffectiveFlowTransportMapper etfEffectiveFlowTransportMapper;

	@Override
	@PostConstruct
	protected void addBeanMapper() {
		super.registerBeanMapper(this.etfFlowTransportMapper);
		super.registerBeanMapper(this.etfEffectiveFlowTransportMapper);
		super.registerBeanMapper(this.etfTransportMapper);
	}

	@Override
	public ResponseEntity<ListEtfOverviewResponse> listEtfOverview(
			@PathVariable(value = "year") final Integer requestYear,
			@PathVariable(value = "month") final Integer requestMonth) {
		final Month month = Month.of(requestMonth);
		final Year year = Year.of(requestYear);
		final LocalDateTime endOfMonth = LocalDateTime.of(requestYear.intValue(), month, 1, 23, 59, 59, 999999999)
				.with(TemporalAdjusters.lastDayOfMonth());
		final UserID userId = super.getUserId();
		final ListEtfOverviewResponse response = new ListEtfOverviewResponse();
		final List<EtfSummaryTransport> transports = new ArrayList<>();

		final List<Etf> etfs = this.etfService.getAllEtf(super.getUserId());
		for (final Etf etf : etfs) {
			final EtfValue etfValue = this.etfService.getEtfValueEndOfMonth(etf.getIsin(), year, month);
			final List<EtfFlow> allEtfFlows = this.etfService.getAllEtfFlowsUntil(userId, etf.getId(), endOfMonth);
			final List<EtfFlowWithTaxInfo> etfFlows = this.etfService.calculateEffectiveEtfFlows(userId, allEtfFlows);
			if (etfFlows != null && !etfFlows.isEmpty()) {
				final EtfSummaryTransport transport = this.getEtfSummaryTransportForEtf(etf, etfValue, etfFlows);
				transports.add(transport);
			}
		}
		if (!transports.isEmpty()) {
			response.setEtfSummaryTransports(transports);
		}
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<ListEtfFlowsResponse> listEtfFlowsById(@PathVariable("id") final Long id) {
		final UserID userId = this.getUserId();
		final EtfID etfId = new EtfID(id);

		final Etf etf = this.etfService.getEtfById(userId, etfId);
		if (etf == null) {
			return ResponseEntity.notFound().build();
		}

		final ListEtfFlowsResponse response = new ListEtfFlowsResponse();

		final List<EtfFlow> etfFlows = this.etfService.getAllEtfFlowsUntil(userId, etfId, LocalDateTime.now());
		final List<EtfFlowTransport> etfFlowTransports = super.mapList(etfFlows, EtfFlowTransport.class);
		response.setEtfFlowTransports(etfFlowTransports);

		final List<EtfFlowWithTaxInfo> etfEffectiveFlows = new ArrayList<>(
				this.etfService.calculateEffectiveEtfFlows(userId, etfFlows));
		Collections.sort(etfEffectiveFlows, Collections.reverseOrder(new EtfFlowComparator()));
		final List<EtfEffectiveFlowTransport> etfEffectiveFlowTransports = super.mapList(etfEffectiveFlows,
				EtfEffectiveFlowTransport.class);
		response.setEtfEffectiveFlowTransports(etfEffectiveFlowTransports);

		final EtfValue etfValue = this.etfService.getLatestEtfValue(etf.getIsin());
		final EtfSummaryTransport transport = this.getEtfSummaryTransportForEtf(etf, etfValue, etfEffectiveFlows);
		response.setEtfSummaryTransport(transport);

		this.settingService.getClientCalcEtfSaleAskPrice(userId)
				.ifPresent(s -> response.setCalcEtfAskPrice(s.getSetting()));
		this.settingService.getClientCalcEtfSaleBidPrice(userId)
				.ifPresent(s -> response.setCalcEtfBidPrice(s.getSetting()));
		this.settingService.getClientCalcEtfSalePieces(userId)
				.ifPresent(s -> response.setCalcEtfSalePieces(s.getSetting()));
		this.settingService.getClientCalcEtfSaleTransactionCostsAbsolute(userId)
				.ifPresent(s -> response.setCalcEtfTransactionCostsAbsolute(s.getSetting()));
		this.settingService.getClientCalcEtfSaleTransactionCostsRelative(userId)
				.ifPresent(s -> response.setCalcEtfTransactionCostsRelative(s.getSetting()));

		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<CalcEtfSaleResponse> calcEtfSale(@RequestBody final CalcEtfSaleRequest request) {
		final CalcEtfSaleResponse response = new CalcEtfSaleResponse();

		if (request.getAskPrice() == null || request.getBidPrice() == null || request.getEtfId() == null
				|| request.getPieces() == null || request.getTransactionCostsAbsolute() == null
				|| request.getTransactionCostsRelative() == null) {
			return ResponseEntity.ok(response);
		}

		final UserID userId = this.getUserId();
		final EtfID etfId = new EtfID(request.getEtfId());

		if (this.etfService.getEtfById(userId, etfId) == null) {
			return ResponseEntity.ok(response);
		}

		final BigDecimal pieces = request.getPieces().abs();
		final BigDecimal askPrice = request.getAskPrice().abs();
		final BigDecimal bidPrice = request.getBidPrice().abs();
		this.settingService.setClientCalcEtfSaleAskPrice(this.getUserId(), new ClientCalcEtfSaleAskPrice(askPrice));
		this.settingService.setClientCalcEtfSaleBidPrice(this.getUserId(), new ClientCalcEtfSaleBidPrice(bidPrice));
		this.settingService.setClientCalcEtfSalePieces(this.getUserId(), new ClientCalcEtfSalePieces(pieces));
		this.settingService.setClientCalcEtfSaleTransactionCostsAbsolute(this.getUserId(),
				new ClientCalcEtfSaleTransactionCostsAbsolute(request.getTransactionCostsAbsolute()));
		this.settingService.setClientCalcEtfSaleTransactionCostsRelative(this.getUserId(),
				new ClientCalcEtfSaleTransactionCostsRelative(request.getTransactionCostsRelative()));

		BigDecimal openPieces = pieces;
		BigDecimal originalBuyPrice = BigDecimal.ZERO;
		BigDecimal overallPreliminaryLumpSum = BigDecimal.ZERO;

		final List<EtfFlow> etfFlows = this.etfService.getAllEtfFlowsUntil(userId, etfId, LocalDateTime.now());
		final List<EtfFlowWithTaxInfo> effectiveEtfFlows = new ArrayList<>(
				this.etfService.calculateEffectiveEtfFlows(userId, etfFlows));

		for (final EtfFlowWithTaxInfo etfFlow : effectiveEtfFlows) {
			BigDecimal useablePieces = etfFlow.getAmount();
			if (useablePieces.compareTo(openPieces) > 0) {
				useablePieces = openPieces;
				overallPreliminaryLumpSum = overallPreliminaryLumpSum.add(etfFlow.getAccumulatedPreliminaryLumpSum()
						.divide(etfFlow.getAmount(), 10, RoundingMode.HALF_UP).multiply(openPieces));
			} else {
				overallPreliminaryLumpSum = overallPreliminaryLumpSum.add(etfFlow.getAccumulatedPreliminaryLumpSum());
			}
			openPieces = openPieces.subtract(useablePieces);
			originalBuyPrice = originalBuyPrice
					.add(useablePieces.multiply(etfFlow.getPrice()).setScale(2, RoundingMode.HALF_UP));
		}
		overallPreliminaryLumpSum = overallPreliminaryLumpSum.setScale(2, RoundingMode.HALF_UP);
		if (BigDecimal.ZERO.compareTo(openPieces) != 0) {
			final ValidationResult validationResult = new ValidationResult();
			validationResult.addValidationResultItem(new ValidationResultItem(null, ErrorCode.AMOUNT_TO_HIGH));

			this.throwValidationExceptionIfInvalid(validationResult);
		} else {
			final BigDecimal newBuyPrice = askPrice.multiply(pieces).setScale(2, RoundingMode.HALF_UP);
			final BigDecimal sellPrice = bidPrice.multiply(pieces).setScale(2, RoundingMode.HALF_UP);

			final BigDecimal transactionCostsAbsoluteSum = request.getTransactionCostsAbsolute()
					.multiply(BigDecimal.valueOf(2));
			final BigDecimal transactionCostsRelative = request.getTransactionCostsRelative() != null
					? request.getTransactionCostsRelative()
					: BigDecimal.ZERO;
			final BigDecimal transactionCostsRelativeSell = sellPrice.multiply(transactionCostsRelative)
					.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
			final BigDecimal transactionCostsRelativeBuy = newBuyPrice.multiply(transactionCostsRelative)
					.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

			final BigDecimal profit = sellPrice.subtract(originalBuyPrice);
			final BigDecimal chargeable = profit.multiply(TAX_RELEVANT_PERCENTAGE).setScale(2, RoundingMode.UP)
					.subtract(overallPreliminaryLumpSum.multiply(TAX_RELEVANT_PERCENTAGE).setScale(2, RoundingMode.UP));
			final BigDecimal rebuyLosses = newBuyPrice.subtract(sellPrice);

			final BigDecimal overallCosts = rebuyLosses.add(transactionCostsAbsoluteSum)
					.add(transactionCostsRelativeSell).add(transactionCostsRelativeBuy);

			response.setNewBuyPrice(newBuyPrice);
			response.setSellPrice(sellPrice);
			response.setTransactionCostsAbsoluteBuy(request.getTransactionCostsAbsolute());
			response.setTransactionCostsRelativeBuy(transactionCostsRelativeBuy);
			response.setTransactionCostsAbsoluteSell(request.getTransactionCostsAbsolute());
			response.setTransactionCostsRelativeSell(transactionCostsRelativeSell);
			response.setEtfId(etfId.getId());
			response.setPieces(pieces);
			response.setOriginalBuyPrice(originalBuyPrice);
			response.setProfit(profit);
			response.setAccumulatedPreliminaryLumpSum(overallPreliminaryLumpSum);
			response.setChargeable(chargeable);
			response.setRebuyLosses(rebuyLosses);
			response.setOverallCosts(overallCosts);
		}

		return ResponseEntity.ok(response);
	}

	private EtfSummaryTransport getEtfSummaryTransportForEtf(final Etf etf, final EtfValue etfValue,
			final List<EtfFlowWithTaxInfo> etfFlows) {
		final EtfSummaryTransport transport = new EtfSummaryTransport();
		transport.setEtfId(etf.getId().getId());
		transport.setName(etf.getName());
		transport.setChartUrl(etf.getChartUrl());
		if (etfValue != null) {
			transport.setBuyPrice(etfValue.getBuyPrice());
			transport.setSellPrice(etfValue.getSellPrice());
			transport.setPricesTimestamp(
					etfValue.getChangeDate().atZone(ZoneId.systemDefault()).toOffsetDateTime());
		}
		BigDecimal amount = BigDecimal.ZERO;
		BigDecimal spentValue = BigDecimal.ZERO;
		for (final EtfFlow flow : etfFlows) {
			amount = amount.add(flow.getAmount());
			spentValue = spentValue.add(flow.getAmount().multiply(flow.getPrice()));
		}
		transport.setAmount(amount);
		transport.setSpentValue(spentValue);
		return transport;
	}
}
