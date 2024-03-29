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
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.etf.Etf;
import org.laladev.moneyjinn.model.etf.EtfFlow;
import org.laladev.moneyjinn.model.etf.EtfFlowComparator;
import org.laladev.moneyjinn.model.etf.EtfFlowID;
import org.laladev.moneyjinn.model.etf.EtfIsin;
import org.laladev.moneyjinn.model.etf.EtfValue;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSaleAskPrice;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSaleBidPrice;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSaleIsin;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSalePieces;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSaleTransactionCosts;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.server.controller.api.EtfControllerApi;
import org.laladev.moneyjinn.server.controller.mapper.EtfEffectiveFlowTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.EtfFlowTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.EtfTransportMapper;
import org.laladev.moneyjinn.server.model.CalcEtfSaleRequest;
import org.laladev.moneyjinn.server.model.CalcEtfSaleResponse;
import org.laladev.moneyjinn.server.model.CreateEtfFlowRequest;
import org.laladev.moneyjinn.server.model.CreateEtfFlowResponse;
import org.laladev.moneyjinn.server.model.EtfEffectiveFlowTransport;
import org.laladev.moneyjinn.server.model.EtfFlowTransport;
import org.laladev.moneyjinn.server.model.EtfSummaryTransport;
import org.laladev.moneyjinn.server.model.EtfTransport;
import org.laladev.moneyjinn.server.model.ListEtfFlowsResponse;
import org.laladev.moneyjinn.server.model.ListEtfOverviewResponse;
import org.laladev.moneyjinn.server.model.UpdateEtfFlowRequest;
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
// TODO: Multi-User
// TODO: Multi-ETF
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class EtfController extends AbstractController implements EtfControllerApi {
	private static final BigDecimal TAX_RELEVANT_PERCENTAGE = new BigDecimal(70).scaleByPowerOfTen(-2);
	private final IEtfService etfService;
	private final ISettingService settingService;
	private final EtfTransportMapper etfTransportMapper;
	private final EtfFlowTransportMapper etfFlowTransportMapper;
	private final EtfEffectiveFlowTransportMapper etfEffectiveFlowTransportMapper;

	@Override
	public ResponseEntity<ListEtfOverviewResponse> listEtfOverview(
			@PathVariable(value = "year") final Integer requestYear,
			@PathVariable(value = "month") final Integer requestMonth) {
		final Month month = Month.of(requestMonth);
		final LocalDateTime endOfMonth = LocalDateTime.of(requestYear.intValue(), month, 1, 23, 59, 59, 999999999)
				.with(TemporalAdjusters.lastDayOfMonth());
		final ListEtfOverviewResponse response = new ListEtfOverviewResponse();
		final List<EtfSummaryTransport> transports = new ArrayList<>();

		final List<Etf> etfs = this.etfService.getAllEtf();
		for (final Etf etf : etfs) {
			final EtfValue etfValue = this.etfService.getEtfValueEndOfMonth(etf.getId(), requestYear, month);
			final List<EtfFlow> allEtfFlows = this.etfService.getAllEtfFlowsUntil(etf.getId(), endOfMonth);
			final List<EtfFlow> etfFlows = this.etfService.calculateEffectiveEtfFlows(allEtfFlows);
			if (etfFlows != null && !etfFlows.isEmpty()) {
				final EtfSummaryTransport transport = new EtfSummaryTransport();
				transport.setIsin(etf.getId().getId());
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
				transports.add(transport);
			}
		}
		if (!transports.isEmpty()) {
			response.setEtfSummaryTransports(transports);
		}
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<ListEtfFlowsResponse> listEtfFlows() {
		final ListEtfFlowsResponse response = new ListEtfFlowsResponse();

		final List<Etf> etfs = this.etfService.getAllEtf();

		if (!etfs.isEmpty()) {
			response.setEtfTransports(super.mapList(etfs, EtfTransport.class));
			final List<EtfFlowTransport> transports = new ArrayList<>();
			final List<EtfEffectiveFlowTransport> effectiveTransports = new ArrayList<>();
			for (final Etf etf : etfs) {
				final List<EtfFlow> etfFlows = this.etfService.getAllEtfFlowsUntil(etf.getId(), LocalDateTime.now());
				transports.addAll(super.mapList(etfFlows, EtfFlowTransport.class));
				final List<EtfFlow> etfEffectiveFlows = new ArrayList<>(
						this.etfService.calculateEffectiveEtfFlows(etfFlows));
				Collections.sort(etfEffectiveFlows, Collections.reverseOrder(new EtfFlowComparator()));
				effectiveTransports.addAll(super.mapList(etfEffectiveFlows, EtfEffectiveFlowTransport.class));
			}
			response.setEtfFlowTransports(transports);
			response.setEtfEffectiveFlowTransports(effectiveTransports);
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
		}

		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<CalcEtfSaleResponse> calcEtfSale(@RequestBody final CalcEtfSaleRequest request) {
		final CalcEtfSaleResponse response = new CalcEtfSaleResponse();

		if (request.getAskPrice() == null || request.getBidPrice() == null || request.getIsin() == null
				|| request.getPieces() == null || request.getTransactionCosts() == null) {
			return ResponseEntity.ok(response);
		}

		final BigDecimal pieces = request.getPieces().abs();
		final BigDecimal askPrice = request.getAskPrice().abs();
		final BigDecimal bidPrice = request.getBidPrice().abs();
		this.settingService.setClientCalcEtfSaleAskPrice(this.getUserId(), new ClientCalcEtfSaleAskPrice(askPrice));
		this.settingService.setClientCalcEtfSaleBidPrice(this.getUserId(), new ClientCalcEtfSaleBidPrice(bidPrice));
		this.settingService.setClientCalcEtfSaleIsin(this.getUserId(), new ClientCalcEtfSaleIsin(request.getIsin()));
		this.settingService.setClientCalcEtfSalePieces(this.getUserId(), new ClientCalcEtfSalePieces(pieces));
		this.settingService.setClientCalcEtfSaleTransactionCosts(this.getUserId(),
				new ClientCalcEtfSaleTransactionCosts(request.getTransactionCosts()));
		final EtfIsin etfIsin = new EtfIsin(request.getIsin());
		BigDecimal openPieces = pieces;
		BigDecimal originalBuyPrice = BigDecimal.ZERO;

		final List<EtfFlow> etfFlows = this.etfService.getAllEtfFlowsUntil(etfIsin, LocalDateTime.now());
		final List<EtfFlow> effectiveEtfFlows = this.etfService.calculateEffectiveEtfFlows(etfFlows);

		if (effectiveEtfFlows != null && !effectiveEtfFlows.isEmpty()) {
			for (final EtfFlow etfFlow : effectiveEtfFlows) {
				BigDecimal useablePieces = etfFlow.getAmount();
				if (useablePieces.compareTo(openPieces) > 0) {
					useablePieces = openPieces;
				}
				openPieces = openPieces.subtract(useablePieces);
				originalBuyPrice = originalBuyPrice.add(useablePieces.multiply(etfFlow.getPrice()));
			}
			if (BigDecimal.ZERO.compareTo(openPieces) != 0) {
				final ValidationResult validationResult = new ValidationResult();
				validationResult.addValidationResultItem(new ValidationResultItem(null, ErrorCode.AMOUNT_TO_HIGH));

				this.throwValidationExceptionIfInvalid(validationResult);
			} else {
				final BigDecimal newBuyPrice = askPrice.multiply(pieces);
				final BigDecimal sellPrice = bidPrice.multiply(pieces);
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
		}
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<CreateEtfFlowResponse> createEtfFlow(@RequestBody final CreateEtfFlowRequest request) {
		final CreateEtfFlowResponse response = new CreateEtfFlowResponse();
		final EtfFlow etfFlow = super.map(request.getEtfFlowTransport(), EtfFlow.class);

		if (etfFlow != null) {
			etfFlow.setId(null);
			final ValidationResult validationResult = this.etfService.validateEtfFlow(etfFlow);

			this.throwValidationExceptionIfInvalid(validationResult);

			final EtfFlowID etfFlowId = this.etfService.createEtfFlow(etfFlow);

			response.setEtfFlowId(etfFlowId.getId());
		}

		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<Void> updateEtfFlow(@RequestBody final UpdateEtfFlowRequest request) {
		final EtfFlow etfFlow = super.map(request.getEtfFlowTransport(), EtfFlow.class);

		if (etfFlow != null) {
			final ValidationResult validationResult = this.etfService.validateEtfFlow(etfFlow);

			this.throwValidationExceptionIfInvalid(validationResult);

			this.etfService.updateEtfFlow(etfFlow);
		}

		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<Void> deleteEtfFlow(@PathVariable(value = "id") final Long id) {
		this.etfService.deleteEtfFlow(new EtfFlowID(id));

		return ResponseEntity.noContent().build();
	}

	@Override
	@PostConstruct
	protected void addBeanMapper() {
		super.registerBeanMapper(this.etfFlowTransportMapper);
		super.registerBeanMapper(this.etfEffectiveFlowTransportMapper);
		super.registerBeanMapper(this.etfTransportMapper);
	}
}
