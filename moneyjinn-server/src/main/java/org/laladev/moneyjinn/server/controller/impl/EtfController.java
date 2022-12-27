//Copyright (c) 2021 Oliver Lehmann <lehmann@ans-netz.de>
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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import org.laladev.moneyjinn.core.rest.model.etf.CalcEtfSaleRequest;
import org.laladev.moneyjinn.core.rest.model.etf.CalcEtfSaleResponse;
import org.laladev.moneyjinn.core.rest.model.etf.CreateEtfFlowRequest;
import org.laladev.moneyjinn.core.rest.model.etf.CreateEtfFlowResponse;
import org.laladev.moneyjinn.core.rest.model.etf.ListEtfFlowsResponse;
import org.laladev.moneyjinn.core.rest.model.etf.ListEtfOverviewResponse;
import org.laladev.moneyjinn.core.rest.model.etf.ShowCreateEtfFlowResponse;
import org.laladev.moneyjinn.core.rest.model.etf.ShowDeleteEtfFlowResponse;
import org.laladev.moneyjinn.core.rest.model.etf.ShowEditEtfFlowResponse;
import org.laladev.moneyjinn.core.rest.model.etf.UpdateEtfFlowRequest;
import org.laladev.moneyjinn.core.rest.model.etf.transport.EtfEffectiveFlowTransport;
import org.laladev.moneyjinn.core.rest.model.etf.transport.EtfFlowTransport;
import org.laladev.moneyjinn.core.rest.model.etf.transport.EtfSummaryTransport;
import org.laladev.moneyjinn.core.rest.model.etf.transport.EtfTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
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
import org.laladev.moneyjinn.server.annotation.RequiresAuthorization;
import org.laladev.moneyjinn.server.controller.mapper.EtfEffectiveFlowTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.EtfFlowTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.EtfTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.ValidationItemTransportMapper;
import org.laladev.moneyjinn.service.api.IEtfService;
import org.laladev.moneyjinn.service.api.ISettingService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import jakarta.inject.Inject;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequestMapping("/moneyflow/server/etf/")
// TODO: Multi-User
// TODO: Multi-ETF
// TODO: Unit-Testing
public class EtfController extends AbstractController {
  private static final BigDecimal TAX_RELEVANT_PERCENTAGE = new BigDecimal(70)
      .scaleByPowerOfTen(-2);
  @Inject
  private IEtfService etfService;
  @Inject
  private ISettingService settingService;

  @RequestMapping(value = "listEtfOverview/{year}/{month}", method = { RequestMethod.GET })
  @RequiresAuthorization
  public ListEtfOverviewResponse listEtfOverview(
      @PathVariable(value = "year") final Short requestYear,
      @PathVariable(value = "month") final Short requestMonth) {
    final Month month = Month.of(requestMonth.intValue());
    final LocalDateTime beginOfMonth = LocalDateTime.of(requestYear.intValue(), month, 1, 23, 59,
        59, 999999999);
    final LocalDateTime endOfMonth = beginOfMonth.with(TemporalAdjusters.lastDayOfMonth());
    final ListEtfOverviewResponse response = new ListEtfOverviewResponse();
    final List<EtfSummaryTransport> transports = new ArrayList<>();
    final List<Etf> etfs = this.etfService.getAllEtf();
    for (final Etf etf : etfs) {
      final EtfValue etfValue = this.etfService.getEtfValueEndOfMonth(etf.getId(), requestYear,
          month);
      final List<EtfFlow> allEtfFlows = this.etfService.getAllEtfFlowsUntil(etf.getId(),
          endOfMonth);
      final List<EtfFlow> etfFlows = this.etfService.calculateEffectiveEtfFlows(allEtfFlows);
      if (etfFlows != null && !etfFlows.isEmpty()) {
        final EtfSummaryTransport transport = new EtfSummaryTransport();
        transport.setIsin(etf.getId().getId());
        transport.setName(etf.getName());
        transport.setChartUrl(etf.getChartUrl());
        if (etfValue != null) {
          transport.setBuyPrice(etfValue.getBuyPrice());
          transport.setSellPrice(etfValue.getSellPrice());
          transport.setPricesTimestamp(Timestamp.valueOf(etfValue.getChangeDate()));
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
    return response;
  }

  @RequestMapping(value = "listEtfFlows", method = { RequestMethod.GET })
  @RequiresAuthorization
  public ListEtfFlowsResponse listEtfFlows() {
    final ListEtfFlowsResponse response = new ListEtfFlowsResponse();
    final List<Etf> etfs = this.etfService.getAllEtf();
    response.setEtfTransports(super.mapList(etfs, EtfTransport.class));
    final List<EtfFlowTransport> transports = new ArrayList<>();
    final List<EtfEffectiveFlowTransport> effectiveTransports = new ArrayList<>();
    for (final Etf etf : etfs) {
      final List<EtfFlow> etfFlows = this.etfService.getAllEtfFlowsUntil(etf.getId(),
          LocalDateTime.now());
      transports.addAll(super.mapList(etfFlows, EtfFlowTransport.class));
      final List<EtfFlow> etfEffectiveFlows = this.etfService.calculateEffectiveEtfFlows(etfFlows);
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
    this.settingService.setClientCalcEtfSaleIsin(this.getUserId(),
        new ClientCalcEtfSaleIsin(request.getIsin()));
    this.settingService.setClientCalcEtfSalePieces(this.getUserId(),
        new ClientCalcEtfSalePieces(request.getPieces()));
    this.settingService.setClientCalcEtfSaleTransactionCosts(this.getUserId(),
        new ClientCalcEtfSaleTransactionCosts(request.getTransactionCosts()));
    final EtfIsin etfIsin = new EtfIsin(request.getIsin());
    final BigDecimal pieces = request.getPieces();
    BigDecimal openPieces = pieces;
    BigDecimal originalBuyPrice = BigDecimal.ZERO;
    final List<EtfFlow> etfFlows = this.etfService.getAllEtfFlowsUntil(etfIsin,
        LocalDateTime.now());
    final List<EtfFlow> effectiveEtfFlows = this.etfService.calculateEffectiveEtfFlows(etfFlows);
    for (final EtfFlow etfFlow : effectiveEtfFlows) {
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
      final BigDecimal transactionCosts = request.getTransactionCosts()
          .multiply(BigDecimal.valueOf(2));
      final BigDecimal profit = sellPrice.subtract(originalBuyPrice);
      final BigDecimal chargeable = profit.multiply(TAX_RELEVANT_PERCENTAGE).setScale(2,
          RoundingMode.UP);
      final BigDecimal rebuyLosses = newBuyPrice.subtract(sellPrice);
      final BigDecimal overallCosts = rebuyLosses.add(transactionCosts);
      response.setResult(Boolean.TRUE);
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

  @RequestMapping(value = "showCreateEtfFlow", method = { RequestMethod.GET })
  @RequiresAuthorization
  public ShowCreateEtfFlowResponse showCreateEtfFlow() {
    final ShowCreateEtfFlowResponse response = new ShowCreateEtfFlowResponse();
    final List<Etf> etfs = this.etfService.getAllEtf();
    response.setEtfTransports(super.mapList(etfs, EtfTransport.class));
    return response;
  }

  @RequestMapping(value = "showEditEtfFlow/{id}", method = { RequestMethod.GET })
  @RequiresAuthorization
  public ShowEditEtfFlowResponse showEditEtfFlow(@PathVariable(value = "id") final Long id) {
    final ShowEditEtfFlowResponse response = new ShowEditEtfFlowResponse();
    final List<Etf> etfs = this.etfService.getAllEtf();
    response.setEtfTransports(super.mapList(etfs, EtfTransport.class));
    final EtfFlow etfFlow = this.etfService.getEtfFlowById(new EtfFlowID(id));
    response.setEtfFlowTransport(super.map(etfFlow, EtfFlowTransport.class));
    return response;
  }

  @RequestMapping(value = "showDeleteEtfFlow/{id}", method = { RequestMethod.GET })
  @RequiresAuthorization
  public ShowDeleteEtfFlowResponse showDeleteEtfFlow(@PathVariable(value = "id") final Long id) {
    final ShowDeleteEtfFlowResponse response = new ShowDeleteEtfFlowResponse();
    final List<Etf> etfs = this.etfService.getAllEtf();
    response.setEtfTransports(super.mapList(etfs, EtfTransport.class));
    final EtfFlow etfFlow = this.etfService.getEtfFlowById(new EtfFlowID(id));
    response.setEtfFlowTransport(super.map(etfFlow, EtfFlowTransport.class));
    return response;
  }

  @RequestMapping(value = "createEtfFlow", method = { RequestMethod.POST })
  @RequiresAuthorization
  public CreateEtfFlowResponse createEtfFlow(@RequestBody final CreateEtfFlowRequest request) {
    final EtfFlow etfFlow = super.map(request.getEtfFlowTransport(), EtfFlow.class);
    etfFlow.setId(null);
    final ValidationResult validationResult = this.etfService.validateEtfFlow(etfFlow);
    final CreateEtfFlowResponse response = new CreateEtfFlowResponse();
    response.setResult(validationResult.isValid());
    if (!validationResult.isValid()) {
      response.setValidationItemTransports(super.mapList(
          validationResult.getValidationResultItems(), ValidationItemTransport.class));
      return response;
    }
    final EtfFlowID etfFlowId = this.etfService.createEtfFlow(etfFlow);
    response.setEtfFlowId(etfFlowId.getId());
    return response;
  }

  @RequestMapping(value = "updateEtfFlow", method = { RequestMethod.PUT })
  @RequiresAuthorization
  public ValidationResponse updateEtfFlow(@RequestBody final UpdateEtfFlowRequest request) {
    final EtfFlow etfFlow = super.map(request.getEtfFlowTransport(), EtfFlow.class);
    final ValidationResult validationResult = this.etfService.validateEtfFlow(etfFlow);
    final ValidationResponse response = new ValidationResponse();
    response.setResult(validationResult.isValid());
    if (!validationResult.isValid()) {
      response.setValidationItemTransports(super.mapList(
          validationResult.getValidationResultItems(), ValidationItemTransport.class));
      return response;
    }
    this.etfService.updateEtfFlow(etfFlow);
    return response;
  }

  @RequestMapping(value = "deleteEtfFlow/{id}", method = { RequestMethod.DELETE })
  @RequiresAuthorization
  public void deleteEtfFlow(@PathVariable(value = "id") final Long id) {
    this.etfService.deleteEtfFlow(new EtfFlowID(id));
  }

  @Override
  protected void addBeanMapper() {
    super.registerBeanMapper(new EtfFlowTransportMapper());
    super.registerBeanMapper(new EtfEffectiveFlowTransportMapper());
    super.registerBeanMapper(new EtfTransportMapper());
    super.registerBeanMapper(new ValidationItemTransportMapper());
  }
}
