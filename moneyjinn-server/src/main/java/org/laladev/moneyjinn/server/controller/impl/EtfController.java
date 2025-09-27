//Copyright (c) 2021-2025 Oliver Lehmann <lehmann@ans-netz.de>
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

import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.etf.*;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSalePieces;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.server.controller.api.EtfControllerApi;
import org.laladev.moneyjinn.server.controller.mapper.EtfEffectiveFlowTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.EtfFlowTransportMapper;
import org.laladev.moneyjinn.server.model.*;
import org.laladev.moneyjinn.service.api.IEtfService;
import org.laladev.moneyjinn.service.api.ISettingService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class EtfController extends AbstractController implements EtfControllerApi {
    private static final BigDecimal BIG_DECIMAL_100 = BigDecimal.valueOf(100);
    private final IEtfService etfService;
    private final ISettingService settingService;
    private final EtfFlowTransportMapper etfFlowTransportMapper;
    private final EtfEffectiveFlowTransportMapper etfEffectiveFlowTransportMapper;

    @Override
    public ResponseEntity<ListEtfOverviewResponse> listEtfOverview(
            final Integer requestYear,
            final Integer requestMonth) {
        final Month month = Month.of(requestMonth);
        final Year year = Year.of(requestYear);
        final LocalDateTime endOfMonth = LocalDateTime.of(requestYear, month, 1, 23, 59, 59, 999999999)
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
    public ResponseEntity<ListEtfFlowsResponse> listEtfFlowsById(final Long id) {
        final UserID userId = this.getUserId();
        final EtfID etfId = new EtfID(id);

        final Etf etf = this.etfService.getEtfById(userId, etfId);
        if (etf == null) {
            return ResponseEntity.notFound().build();
        }

        final ListEtfFlowsResponse response = new ListEtfFlowsResponse();

        final List<EtfFlow> etfFlows = this.etfService.getAllEtfFlowsUntil(userId, etfId, LocalDateTime.now());
        final List<EtfFlowTransport> etfFlowTransports = this.etfFlowTransportMapper.mapAToB(etfFlows);
        response.setEtfFlowTransports(etfFlowTransports);

        final List<EtfFlowWithTaxInfo> etfEffectiveFlows = new ArrayList<>(
                this.etfService.calculateEffectiveEtfFlows(userId, etfFlows));
        etfEffectiveFlows.sort(Collections.reverseOrder(new EtfFlowComparator()));
        final List<EtfEffectiveFlowTransport> etfEffectiveFlowTransports = this.etfEffectiveFlowTransportMapper
                .mapAToB(etfEffectiveFlows);
        response.setEtfEffectiveFlowTransports(etfEffectiveFlowTransports);

        final EtfValue etfValue = this.etfService.getLatestEtfValue(etf.getIsin());
        final EtfSummaryTransport transport = this.getEtfSummaryTransportForEtf(etf, etfValue, etfEffectiveFlows);
        response.setEtfSummaryTransport(transport);

        this.settingService.getClientCalcEtfSalePieces(userId)
                .ifPresent(s -> response.setCalcEtfSalePieces(s.getSetting()));

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CalcEtfSaleResponse> calcEtfSale(@RequestBody final CalcEtfSaleRequest request) {
        final CalcEtfSaleResponse response = new CalcEtfSaleResponse();

        final BigDecimal transactionCostsAbsolute = request.getTransactionCostsAbsolute() == null ? BigDecimal.ZERO
                : request.getTransactionCostsAbsolute();
        final BigDecimal transactionCostsRelative = request.getTransactionCostsRelative() == null ? BigDecimal.ZERO
                : request.getTransactionCostsRelative();
        final BigDecimal transactionCostsMaximum = request.getTransactionCostsMaximum();

        if (request.getAskPrice() == null || request.getBidPrice() == null || request.getEtfId() == null
                || request.getPieces() == null) {
            return ResponseEntity.ok(response);
        }

        final UserID userId = this.getUserId();
        final EtfID etfId = new EtfID(request.getEtfId());

        final Etf etf = this.etfService.getEtfById(userId, etfId);
        if (etf == null) {
            return ResponseEntity.ok(response);
        }

        final BigDecimal taxRelevantPercentage = etf.getPartialTaxExemption() == null ? BigDecimal.ONE
                : BIG_DECIMAL_100.subtract(etf.getPartialTaxExemption()).scaleByPowerOfTen(-2); // transform 30% --> 0.7

        final BigDecimal pieces = request.getPieces().abs();
        final BigDecimal askPrice = request.getAskPrice().abs();
        final BigDecimal bidPrice = request.getBidPrice().abs();
        this.settingService.setClientCalcEtfSalePieces(this.getUserId(), new ClientCalcEtfSalePieces(pieces));

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

            BigDecimal transactionCostsRelativeSell = sellPrice.multiply(transactionCostsRelative)
                    .divide(BIG_DECIMAL_100, 2, RoundingMode.HALF_UP);
            if (transactionCostsMaximum != null && transactionCostsMaximum
                    .compareTo(transactionCostsRelativeSell.add(transactionCostsAbsolute)) < 0) {
                transactionCostsRelativeSell = transactionCostsMaximum.subtract(transactionCostsAbsolute);
            }
            BigDecimal transactionCostsRelativeBuy = newBuyPrice.multiply(transactionCostsRelative)
                    .divide(BIG_DECIMAL_100, 2, RoundingMode.HALF_UP);

            if (transactionCostsMaximum != null && transactionCostsMaximum
                    .compareTo(transactionCostsRelativeBuy.add(transactionCostsAbsolute)) < 0) {
                transactionCostsRelativeBuy = transactionCostsMaximum.subtract(transactionCostsAbsolute);
            }

            final BigDecimal profit = sellPrice.subtract(originalBuyPrice);
            final BigDecimal chargeable = profit.multiply(taxRelevantPercentage).setScale(2, RoundingMode.UP)
                    .subtract(overallPreliminaryLumpSum.multiply(taxRelevantPercentage).setScale(2, RoundingMode.UP));
            final BigDecimal rebuyLosses = newBuyPrice.subtract(sellPrice);

            final BigDecimal overallCosts = rebuyLosses.add(transactionCostsAbsolute).add(transactionCostsRelativeSell)
                    .add(transactionCostsAbsolute).add(transactionCostsRelativeBuy);

            response.setNewBuyPrice(newBuyPrice);
            response.setSellPrice(sellPrice);
            response.setTransactionCostsAbsoluteBuy(transactionCostsAbsolute);
            response.setTransactionCostsRelativeBuy(transactionCostsRelativeBuy);
            response.setTransactionCostsAbsoluteSell(transactionCostsAbsolute);
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
