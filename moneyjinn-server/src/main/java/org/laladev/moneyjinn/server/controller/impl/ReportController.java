//Copyright (c) 2015-2023 Oliver Lehmann <lehmann@ans-netz.de>
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

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.laladev.moneyjinn.model.ImportedBalance;
import org.laladev.moneyjinn.model.PostingAccountAmount;
import org.laladev.moneyjinn.model.PostingAccountID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceImport;
import org.laladev.moneyjinn.model.moneyflow.Moneyflow;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowID;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowSplitEntry;
import org.laladev.moneyjinn.model.monthlysettlement.MonthlySettlement;
import org.laladev.moneyjinn.model.setting.ClientReportingUnselectedPostingAccountIdsSetting;
import org.laladev.moneyjinn.model.setting.ClientTrendCapitalsourceIDsSetting;
import org.laladev.moneyjinn.server.controller.api.ReportControllerApi;
import org.laladev.moneyjinn.server.controller.mapper.CapitalsourceStateMapper;
import org.laladev.moneyjinn.server.controller.mapper.CapitalsourceTypeMapper;
import org.laladev.moneyjinn.server.controller.mapper.MoneyflowSplitEntryTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.MoneyflowTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.PostingAccountAmountTransportMapper;
import org.laladev.moneyjinn.server.model.GetAvailableReportMonthResponse;
import org.laladev.moneyjinn.server.model.ListReportsResponse;
import org.laladev.moneyjinn.server.model.MoneyflowSplitEntryTransport;
import org.laladev.moneyjinn.server.model.MoneyflowTransport;
import org.laladev.moneyjinn.server.model.PostingAccountAmountTransport;
import org.laladev.moneyjinn.server.model.ReportTurnoverCapitalsourceTransport;
import org.laladev.moneyjinn.server.model.ShowMonthlyReportGraphRequest;
import org.laladev.moneyjinn.server.model.ShowMonthlyReportGraphResponse;
import org.laladev.moneyjinn.server.model.ShowReportingFormResponse;
import org.laladev.moneyjinn.server.model.ShowTrendsFormResponse;
import org.laladev.moneyjinn.server.model.ShowTrendsGraphRequest;
import org.laladev.moneyjinn.server.model.ShowTrendsGraphResponse;
import org.laladev.moneyjinn.server.model.ShowYearlyReportGraphRequest;
import org.laladev.moneyjinn.server.model.ShowYearlyReportGraphResponse;
import org.laladev.moneyjinn.server.model.TrendsCalculatedTransport;
import org.laladev.moneyjinn.server.model.TrendsSettledTransport;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.IImportedBalanceService;
import org.laladev.moneyjinn.service.api.IMoneyflowReceiptService;
import org.laladev.moneyjinn.service.api.IMoneyflowService;
import org.laladev.moneyjinn.service.api.IMoneyflowSplitEntryService;
import org.laladev.moneyjinn.service.api.IMonthlySettlementService;
import org.laladev.moneyjinn.service.api.ISettingService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ReportController extends AbstractController implements ReportControllerApi {
  private final IMoneyflowService moneyflowService;
  private final IMoneyflowSplitEntryService moneyflowSplitEntryService;
  private final IMoneyflowReceiptService moneyflowReceiptService;
  private final ICapitalsourceService capitalsourceService;
  private final IMonthlySettlementService monthlySettlementService;
  private final IImportedBalanceService importedBalanceService;
  private final ISettingService settingService;
  private final MoneyflowSplitEntryTransportMapper moneyflowSplitEntryTransportMapper;
  private final MoneyflowTransportMapper moneyflowTransportMapper;
  private final PostingAccountAmountTransportMapper postingAccountAmountTransportMapper;

  @Override
  @PostConstruct
  protected void addBeanMapper() {
    super.registerBeanMapper(this.moneyflowTransportMapper);
    super.registerBeanMapper(this.moneyflowSplitEntryTransportMapper);
    super.registerBeanMapper(this.postingAccountAmountTransportMapper);
  }

  @Override
  public ResponseEntity<ShowReportingFormResponse> showReportingForm() {
    final UserID userId = super.getUserId();
    final ShowReportingFormResponse response = new ShowReportingFormResponse();
    final LocalDate maxMoneyflowDate = this.moneyflowService.getMaxMoneyflowDate(userId);
    final LocalDate minMoneyflowDate = this.moneyflowService.getMinMoneyflowDate(userId);
    if (maxMoneyflowDate != null) {
      response.setMaxDate(maxMoneyflowDate);
    }
    if (minMoneyflowDate != null) {
      response.setMinDate(minMoneyflowDate);
    }

    this.settingService.getClientReportingUnselectedPostingAccountIdsSetting(userId)
        .ifPresent(s -> response
            .setPostingAccountIds(s.getSetting().stream().map(PostingAccountID::getId).toList()));

    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<ShowMonthlyReportGraphResponse> showMonthlyReportGraph(
      @RequestBody final ShowMonthlyReportGraphRequest request) {
    final UserID userId = super.getUserId();
    final ShowMonthlyReportGraphResponse response = new ShowMonthlyReportGraphResponse();
    if (request.getStartDate() != null && request.getEndDate() != null
        && request.getPostingAccountIdsYes() != null
        && !request.getPostingAccountIdsYes().isEmpty()) {
      final LocalDate startDate = request.getStartDate();
      final LocalDate endDate = request.getEndDate();
      final List<PostingAccountID> postingAccountIdsYes = request.getPostingAccountIdsYes().stream()
          .map(PostingAccountID::new).collect(Collectors.toCollection(ArrayList::new));
      if (request.getPostingAccountIdsNo() != null) {
        final List<PostingAccountID> postingAccountIdsNo = request.getPostingAccountIdsNo().stream()
            .map(PostingAccountID::new).collect(Collectors.toCollection(ArrayList::new));
        final ClientReportingUnselectedPostingAccountIdsSetting setting = new ClientReportingUnselectedPostingAccountIdsSetting(
            postingAccountIdsNo);
        this.settingService.setClientReportingUnselectedPostingAccountIdsSetting(userId, setting);
      }
      final List<PostingAccountAmount> postingAccountAmounts = this.moneyflowService
          .getAllMoneyflowsByDateRangeGroupedByYearMonthPostingAccount(userId, postingAccountIdsYes,
              startDate, endDate);
      final List<PostingAccountAmountTransport> responsePostingAccountAmount = super.mapList(
          postingAccountAmounts, PostingAccountAmountTransport.class);
      response.setPostingAccountAmountTransports(responsePostingAccountAmount);
    }
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<ShowYearlyReportGraphResponse> showYearlyReportGraph(
      @RequestBody final ShowYearlyReportGraphRequest request) {
    final UserID userId = super.getUserId();
    final ShowYearlyReportGraphResponse response = new ShowYearlyReportGraphResponse();
    if (request.getStartDate() != null && request.getEndDate() != null
        && request.getPostingAccountIdsYes() != null
        && !request.getPostingAccountIdsYes().isEmpty()) {
      final LocalDate startDate = request.getStartDate();
      final LocalDate endDate = request.getEndDate();
      final List<PostingAccountID> postingAccountIdsYes = request.getPostingAccountIdsYes().stream()
          .map(PostingAccountID::new).collect(Collectors.toCollection(ArrayList::new));
      if (request.getPostingAccountIdsNo() != null) {
        final List<PostingAccountID> postingAccountIdsNo = request.getPostingAccountIdsNo().stream()
            .map(PostingAccountID::new).collect(Collectors.toCollection(ArrayList::new));
        final ClientReportingUnselectedPostingAccountIdsSetting setting = new ClientReportingUnselectedPostingAccountIdsSetting(
            postingAccountIdsNo);
        this.settingService.setClientReportingUnselectedPostingAccountIdsSetting(userId, setting);
      }
      final List<PostingAccountAmount> postingAccountAmounts = this.moneyflowService
          .getAllMoneyflowsByDateRangeGroupedByYearPostingAccount(userId, postingAccountIdsYes,
              startDate, endDate);
      final List<PostingAccountAmountTransport> responsePostingAccountAmount = super.mapList(
          postingAccountAmounts, PostingAccountAmountTransport.class);
      response.setPostingAccountAmountTransports(responsePostingAccountAmount);
    }
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<ShowTrendsFormResponse> showTrendsForm() {
    final UserID userId = super.getUserId();
    final ShowTrendsFormResponse response = new ShowTrendsFormResponse();
    final LocalDate maxMoneyflowDate = this.moneyflowService.getMaxMoneyflowDate(userId);
    final LocalDate minMonthlysettlementwDate = this.monthlySettlementService
        .getMinSettlementDate(userId);
    if (maxMoneyflowDate != null) {
      response.setMaxDate(maxMoneyflowDate);
      response.setMinDate(maxMoneyflowDate);
    }
    if (minMonthlysettlementwDate != null) {
      response.setMinDate(minMonthlysettlementwDate);
    }

    this.settingService.getClientTrendCapitalsourceIDsSetting(userId)
        .ifPresent(s -> response.setSettingTrendCapitalsourceIds(
            s.getSetting().stream().map(CapitalsourceID::getId).toList()));

    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<ShowTrendsGraphResponse> showTrendsGraph(
      @RequestBody final ShowTrendsGraphRequest request) {
    final UserID userId = super.getUserId();
    final ShowTrendsGraphResponse response = new ShowTrendsGraphResponse();
    if (request.getStartDate() != null && request.getEndDate() != null
        && request.getCapitalSourceIds() != null && !request.getCapitalSourceIds().isEmpty()) {
      final LocalDate startDate = request.getStartDate();
      final LocalDate endDate = request.getEndDate().with(TemporalAdjusters.lastDayOfMonth());
      final List<CapitalsourceID> capitalsourceIds = request.getCapitalSourceIds().stream()
          .map(CapitalsourceID::new).collect(Collectors.toCollection(ArrayList::new));
      final ClientTrendCapitalsourceIDsSetting setting = new ClientTrendCapitalsourceIDsSetting(
          capitalsourceIds);
      // Save the selection for the next time the form is shown
      this.settingService.setClientTrendCapitalsourceIDsSetting(userId, setting);
      final List<MonthlySettlement> monthlySettlements = this.monthlySettlementService
          .getAllMonthlySettlementsByRangeAndCapitalsource(userId, startDate, endDate,
              capitalsourceIds);
      final List<Capitalsource> capitalsources = this.capitalsourceService
          .getAllCapitalsources(userId);
      final Map<CapitalsourceID, LocalDate> validTilCapitalsourceIdMap = new HashMap<>();
      for (final Capitalsource capitalsource : capitalsources) {
        validTilCapitalsourceIdMap.put(capitalsource.getId(), capitalsource.getValidTil());
      }
      final SortedMap<Integer, SortedMap<Month, BigDecimal>> settlementAmounts = new TreeMap<>();
      final SortedMap<Integer, SortedMap<Month, BigDecimal>> moneyflowAmounts = new TreeMap<>();
      Month lastMonth = null;
      Integer lastYear = null;
      BigDecimal lastAmount = BigDecimal.ZERO;
      LocalDate lastSettledDay;
      if (monthlySettlements != null && !monthlySettlements.isEmpty()) {
        for (final MonthlySettlement monthlySettlement : monthlySettlements) {
          lastMonth = monthlySettlement.getMonth();
          lastYear = monthlySettlement.getYear();
          SortedMap<Month, BigDecimal> settlementAmountMap = settlementAmounts.get(lastYear);
          if (settlementAmountMap == null) {
            settlementAmountMap = new TreeMap<>();
          }
          lastAmount = settlementAmountMap.get(lastMonth);
          if (lastAmount == null) {
            lastAmount = BigDecimal.ZERO;
          }
          lastAmount = lastAmount.add(monthlySettlement.getAmount());
          settlementAmountMap.put(lastMonth, lastAmount);
          settlementAmounts.put(lastYear, settlementAmountMap);
        }
        lastSettledDay = LocalDate.of(lastYear.intValue(), lastMonth, 1)
            .with(TemporalAdjusters.lastDayOfMonth());
      } else {
        lastSettledDay = startDate.minusMonths(1L).with(TemporalAdjusters.lastDayOfMonth());
      }
      if (endDate.isAfter(lastSettledDay)) {
        LocalDate beginOfMonth = lastSettledDay.plusDays(1L);
        LocalDate endOfMonth = beginOfMonth.with(TemporalAdjusters.lastDayOfMonth());
        LocalDate maxMoneyflowDate = this.moneyflowService.getMaxMoneyflowDate(userId);
        if (maxMoneyflowDate != null) {
          maxMoneyflowDate = maxMoneyflowDate.with(TemporalAdjusters.lastDayOfMonth());
          while (!endOfMonth.isAfter(maxMoneyflowDate)) {
            this.filterByValidity(capitalsourceIds, validTilCapitalsourceIdMap, beginOfMonth);
            if (capitalsourceIds.isEmpty()) {
              break;
            }
            final BigDecimal amount = this.moneyflowService
                .getSumAmountByDateRangeForCapitalsourceIds(userId, beginOfMonth, endOfMonth,
                    capitalsourceIds);
            lastAmount = lastAmount.add(amount);
            final Month month = endOfMonth.getMonth();
            final Integer year = endOfMonth.getYear();
            SortedMap<Month, BigDecimal> moneyflowAmountMap = moneyflowAmounts.get(year);
            if (moneyflowAmountMap == null) {
              moneyflowAmountMap = new TreeMap<>();
            }
            moneyflowAmountMap.put(month, lastAmount);
            moneyflowAmounts.put(year, moneyflowAmountMap);
            beginOfMonth = beginOfMonth.with(TemporalAdjusters.lastDayOfMonth()).plusDays(1L);
            endOfMonth = endOfMonth.plusDays(1L).with(TemporalAdjusters.lastDayOfMonth());
          }
        }
      }
      final List<TrendsSettledTransport> trendsSettledTransports = new ArrayList<>();
      for (final Entry<Integer, SortedMap<Month, BigDecimal>> yearEntry : settlementAmounts
          .entrySet()) {
        final Integer year = yearEntry.getKey();
        for (final Entry<Month, BigDecimal> monthEntry : yearEntry.getValue().entrySet()) {
          final TrendsSettledTransport trendsSettledTransport = new TrendsSettledTransport();
          trendsSettledTransport.setYear(year);
          trendsSettledTransport.setMonth(monthEntry.getKey().getValue());
          trendsSettledTransport.setAmount(monthEntry.getValue());
          trendsSettledTransports.add(trendsSettledTransport);
        }
      }
      final List<TrendsCalculatedTransport> trendsCalculatedTransports = new ArrayList<>();
      for (final Entry<Integer, SortedMap<Month, BigDecimal>> yearEntry : moneyflowAmounts
          .entrySet()) {
        final Integer year = yearEntry.getKey();
        for (final Entry<Month, BigDecimal> monthEntry : yearEntry.getValue().entrySet()) {
          final TrendsCalculatedTransport trendsCalculatedTransport = new TrendsCalculatedTransport();
          trendsCalculatedTransport.setYear(year);
          trendsCalculatedTransport.setMonth(monthEntry.getKey().getValue());
          trendsCalculatedTransport.setAmount(monthEntry.getValue());
          trendsCalculatedTransports.add(trendsCalculatedTransport);
        }
      }
      if (!trendsSettledTransports.isEmpty()) {
        response.setTrendsSettledTransports(trendsSettledTransports);
      }
      if (!trendsCalculatedTransports.isEmpty()) {
        response.setTrendsCalculatedTransports(trendsCalculatedTransports);
      }
    }
    return ResponseEntity.ok(response);
  }

  private void filterByValidity(final List<CapitalsourceID> capitalsourceIds,
      final Map<CapitalsourceID, LocalDate> validTilCapitalsourceIdMap,
      final LocalDate beginOfMonth) {
    capitalsourceIds.removeIf(csi -> validTilCapitalsourceIdMap.get(csi).isBefore(beginOfMonth));
  }

  @Override
  public ResponseEntity<GetAvailableReportMonthResponse> getAvailableMonth() {
    return this.getAvailableMonthYearMonth(null, null);
  }

  @Override
  public ResponseEntity<GetAvailableReportMonthResponse> getAvailableMonthYear(
      @PathVariable(value = "year") final Integer requestYear) {
    return this.getAvailableMonthYearMonth(requestYear, null);
  }

  @Override
  public ResponseEntity<GetAvailableReportMonthResponse> getAvailableMonthYearMonth(
      @PathVariable(value = "year") final Integer requestYear,
      @PathVariable(value = "month") final Integer requestMonth) {
    final UserID userId = super.getUserId();
    final GetAvailableReportMonthResponse response = new GetAvailableReportMonthResponse();
    final List<Integer> allYears = this.moneyflowService.getAllYears(userId);
    boolean nextMonthHasMoneyflows = false;
    boolean previousMonthHasMoneyflows = false;
    Month prevMonth = null;
    Integer prevYear = null;
    Month nextMonth = null;
    Integer nextYear = null;
    List<Month> allMonth = null;
    Integer year = requestYear;
    Month month = this.getMonth(requestMonth);
    // only continue if settlements where made at all
    if (allYears != null && !allYears.isEmpty()) {
      // validate if settlements are recorded for the given year, if not fall back to the
      // last recorded one
      if (year == null || !allYears.contains(year)) {
        year = allYears.get(allYears.size() - 1);
        month = null;
      }
      allMonth = this.moneyflowService.getAllMonth(userId, year);
      if (month != null && allMonth != null && allMonth.contains(month)) {
        final LocalDate beginOfMonth = LocalDate.of(year, month, 1);
        final LocalDate endOfMonth = beginOfMonth.with(TemporalAdjusters.lastDayOfMonth());
        final int indexInAllMonthList = allMonth.indexOf(month);
        LocalDate previousDate = null;
        LocalDate nextDate = null;
        if (month != Month.JANUARY) {
          if (indexInAllMonthList > 0) {
            previousDate = LocalDate.of(year, allMonth.get(indexInAllMonthList - 1), 1);
          }
        } else {
          previousDate = this.moneyflowService.getPreviousMoneyflowDate(userId, beginOfMonth);
        }
        if (month != Month.DECEMBER) {
          if (indexInAllMonthList < allMonth.size() - 1) {
            nextDate = LocalDate.of(year, allMonth.get(indexInAllMonthList + 1), 1);
          }
        } else {
          nextDate = this.moneyflowService.getNextMoneyflowDate(userId, endOfMonth);
        }
        if (previousDate != null) {
          previousMonthHasMoneyflows = true;
          prevMonth = previousDate.getMonth();
          prevYear = previousDate.getYear();
        }
        if (nextDate != null) {
          nextMonthHasMoneyflows = true;
          nextMonth = nextDate.getMonth();
          nextYear = nextDate.getYear();
        }
      } else {
        month = null;
      }
    }
    response.setYear(year);
    if (month != null) {
      response.setMonth(month.getValue());
    }
    if (allYears != null && !allYears.isEmpty()) {
      response.setAllYears(allYears);
    }
    if (allMonth != null && !allMonth.isEmpty()) {
      response.setAllMonth(
          allMonth.stream().map(Month::getValue).collect(Collectors.toCollection(ArrayList::new)));
    }
    if (nextMonthHasMoneyflows) {
      response.setNextMonthHasMoneyflows(1);
    }
    if (previousMonthHasMoneyflows) {
      response.setPreviousMonthHasMoneyflows(1);
    }
    if (prevMonth != null) {
      response.setPreviousMonth(prevMonth.getValue());
    }
    response.setPreviousYear(prevYear);
    if (nextMonth != null) {
      response.setNextMonth(nextMonth.getValue());
    }
    response.setNextYear(nextYear);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<ListReportsResponse> listReportsV2(
      @PathVariable(value = "year") final Integer requestYear,
      @PathVariable(value = "month") final Integer requestMonth) {
    final UserID userId = super.getUserId();
    final ListReportsResponse response = new ListReportsResponse();
    final List<ReportTurnoverCapitalsourceTransport> turnoverCapitalsources = new ArrayList<>();
    List<Moneyflow> moneyflows = null;
    BigDecimal turnoverEndOfYearCalculated = null;
    BigDecimal amountBeginOfYear = null;
    final Map<CapitalsourceID, MonthlySettlement> newCapitalsourcesSettled = new HashMap<>();
    Map<MoneyflowID, List<MoneyflowSplitEntry>> moneyflowSplitEntries = new HashMap<>();
    List<MoneyflowID> moneyflowIdsWithReceipts = new ArrayList<>();
    final Integer year = requestYear;
    final Month month = this.getMonth(requestMonth);
    if (month != null) {
      final LocalDate beginOfMonth = LocalDate.of(year, month, 1);
      final LocalDate endOfMonth = beginOfMonth.with(TemporalAdjusters.lastDayOfMonth());
      final LocalDate beginOfPrevMonth = beginOfMonth.minusMonths(1L);
      // TODO: Do not hardcode "previous" month for determine the last recorded
      // settlement.
      final Month prevMonthSettlement = beginOfPrevMonth.getMonth();
      final Integer prevYearSettlement = beginOfPrevMonth.getYear();
      moneyflows = this.moneyflowService.getAllMoneyflowsByDateRangeIncludingPrivate(userId,
          beginOfMonth, endOfMonth);
      if (moneyflows != null && !moneyflows.isEmpty()) {
        final List<MoneyflowID> relevantMoneyflowIds = moneyflows.stream()
            .filter(mf -> !mf.isPrivat() || mf.getUser().getId().equals(userId))
            .map(Moneyflow::getId).collect(Collectors.toCollection(ArrayList::new));
        moneyflowSplitEntries = this.moneyflowSplitEntryService.getMoneyflowSplitEntries(userId,
            relevantMoneyflowIds);
        moneyflowIdsWithReceipts = this.moneyflowReceiptService.getMoneyflowIdsWithReceipt(userId,
            relevantMoneyflowIds);
        final List<MonthlySettlement> settlementsPrevMonth = this.monthlySettlementService
            .getAllMonthlySettlementsByYearMonth(userId, prevYearSettlement, prevMonthSettlement);
        final List<MonthlySettlement> settlementsThisMonth = this.monthlySettlementService
            .getAllMonthlySettlementsByYearMonth(userId, year, month);
        final List<Capitalsource> validCapitalsources = this.capitalsourceService
            .getAllCapitalsourcesByDateRange(userId, beginOfMonth, endOfMonth);
        // statistics are only generated, if for the previous month a settlement was
        // accomplished
        if (settlementsPrevMonth != null && !settlementsPrevMonth.isEmpty()) {
          // rearange the settlements of the selected month to access them later by their
          // capitalsourceId directly
          if (settlementsThisMonth != null && !settlementsThisMonth.isEmpty()) {
            for (final MonthlySettlement monthlySettlement : settlementsThisMonth) {
              newCapitalsourcesSettled.put(monthlySettlement.getCapitalsource().getId(),
                  monthlySettlement);
            }
          }
          final List<CapitalsourceID> capitalsourceIds = validCapitalsources.stream()
              .filter(mcs -> mcs.getImportAllowed() != CapitalsourceImport.NOT_ALLOWED)
              .map(Capitalsource::getId).collect(Collectors.toCollection(ArrayList::new));
          final List<ImportedBalance> importedBalances = this.importedBalanceService
              .getAllImportedBalancesByCapitalsourceIds(userId, capitalsourceIds);
          // this will hold all capitalsources which will be removed later if they where
          // processed it will then only contain new capitalsources which have no
          // settlement in the previous nor in this month yet
          final List<Capitalsource> newCapitalsourcesUnsettled = new ArrayList<>();
          newCapitalsourcesUnsettled.addAll(validCapitalsources);
          final LocalDate today = LocalDate.now();
          for (final MonthlySettlement lastSettlement : settlementsPrevMonth) {
            final Capitalsource lastSettlementCapitalsource = lastSettlement.getCapitalsource();
            // Capitalsource has a settlement for the previous month, but is no longer
            // valid this month -> ignore it
            if (beginOfMonth.isAfter(lastSettlementCapitalsource.getValidTil())) {
              continue;
            }
            final ReportTurnoverCapitalsourceTransport turnoverCapitalsource = new ReportTurnoverCapitalsourceTransport();
            turnoverCapitalsource.setCapitalsourceComment(lastSettlementCapitalsource.getComment());
            turnoverCapitalsource.setCapitalsourceType(
                CapitalsourceTypeMapper.map(lastSettlementCapitalsource.getType()));
            turnoverCapitalsource.setCapitalsourceState(
                CapitalsourceStateMapper.map(lastSettlementCapitalsource.getState()));
            turnoverCapitalsource.setAmountBeginOfMonthFixed(lastSettlement.getAmount());
            final CapitalsourceID capitalsourceId = lastSettlementCapitalsource.getId();
            // if a settlement for the selected month was already done, use its data,
            // otherwise calculate the movement on the fly
            if (settlementsThisMonth != null && !settlementsThisMonth.isEmpty()) {
              if (newCapitalsourcesSettled.containsKey(capitalsourceId)) {
                turnoverCapitalsource.setAmountEndOfMonthFixed(
                    newCapitalsourcesSettled.get(capitalsourceId).getAmount());
                newCapitalsourcesSettled.remove(capitalsourceId);
              }
            } else if (today.compareTo(beginOfMonth) >= 0 && today.compareTo(endOfMonth) <= 0) {
              // show imported balances only for the current month
              this.addCurrentAmount(lastSettlementCapitalsource, beginOfMonth,
                  lastSettlement.getAmount(), turnoverCapitalsource, moneyflows, importedBalances);
            }
            final BigDecimal movementCalculated = this.getMovementForCapitalsourceAndDateRange(
                moneyflows, capitalsourceId, beginOfMonth, endOfMonth);
            turnoverCapitalsource
                .setAmountEndOfMonthCalculated(movementCalculated.add(lastSettlement.getAmount()));
            turnoverCapitalsources.add(turnoverCapitalsource);
            newCapitalsourcesUnsettled.remove(lastSettlementCapitalsource);
          }
          if (newCapitalsourcesSettled != null && !newCapitalsourcesSettled.isEmpty()) {
            // new capitalsource with no settlement in the previous month - assume a 0
            // settlement
            for (final MonthlySettlement monthlySettlement : newCapitalsourcesSettled.values()) {
              final Capitalsource settlementsThisMonthCapitalsource = monthlySettlement
                  .getCapitalsource();
              final ReportTurnoverCapitalsourceTransport turnoverCapitalsource = new ReportTurnoverCapitalsourceTransport();
              turnoverCapitalsource
                  .setCapitalsourceComment(settlementsThisMonthCapitalsource.getComment());
              turnoverCapitalsource.setCapitalsourceType(
                  CapitalsourceTypeMapper.map(settlementsThisMonthCapitalsource.getType()));
              turnoverCapitalsource.setCapitalsourceState(
                  CapitalsourceStateMapper.map(settlementsThisMonthCapitalsource.getState()));
              turnoverCapitalsource.setAmountBeginOfMonthFixed(BigDecimal.ZERO);
              turnoverCapitalsource.setAmountEndOfMonthFixed(monthlySettlement.getAmount());
              turnoverCapitalsource.setAmountEndOfMonthCalculated(
                  this.getMovementForCapitalsourceAndDateRange(moneyflows,
                      settlementsThisMonthCapitalsource.getId(), beginOfMonth, endOfMonth));
              turnoverCapitalsources.add(turnoverCapitalsource);
              newCapitalsourcesUnsettled.remove(settlementsThisMonthCapitalsource);
            }
          }
          if (newCapitalsourcesUnsettled != null && !newCapitalsourcesUnsettled.isEmpty()) {
            final boolean nextMonthHasMoneyflows = this.moneyflowService
                .getNextMoneyflowDate(userId, endOfMonth) != null;
            // new capitalsource with neither a settlement in the current, nor in the
            // previous month - assume a 0 settlement last month
            for (final Capitalsource capitalsource : newCapitalsourcesUnsettled) {
              final ReportTurnoverCapitalsourceTransport turnoverCapitalsource = new ReportTurnoverCapitalsourceTransport();
              turnoverCapitalsource.setCapitalsourceComment(capitalsource.getComment());
              turnoverCapitalsource
                  .setCapitalsourceType(CapitalsourceTypeMapper.map(capitalsource.getType()));
              turnoverCapitalsource
                  .setCapitalsourceState(CapitalsourceStateMapper.map(capitalsource.getState()));
              turnoverCapitalsource.setAmountBeginOfMonthFixed(BigDecimal.ZERO);
              turnoverCapitalsource.setAmountEndOfMonthCalculated(
                  this.getMovementForCapitalsourceAndDateRange(moneyflows, capitalsource.getId(),
                      beginOfMonth, endOfMonth));
              if (!nextMonthHasMoneyflows) {
                this.addCurrentAmount(capitalsource, beginOfMonth, BigDecimal.ZERO,
                    turnoverCapitalsource, moneyflows, importedBalances);
              }
              turnoverCapitalsources.add(turnoverCapitalsource);
            }
          }
          // Sort turnover Capitalsources in the same way all valid Capitalsources where
          // sorted.
          final List<String> validCapitalsourceComments = validCapitalsources.stream()
              .map(Capitalsource::getComment).collect(Collectors.toCollection(ArrayList::new));
          final Comparator<String> orderingComparator = Comparator
              .comparingInt(validCapitalsourceComments::indexOf);
          turnoverCapitalsources.sort((final ReportTurnoverCapitalsourceTransport left,
              final ReportTurnoverCapitalsourceTransport right) -> orderingComparator
                  .compare(left.getCapitalsourceComment(), right.getCapitalsourceComment()));
          final LocalDate beginOfYear = LocalDate.of(year, Month.JANUARY, 1);
          final List<Capitalsource> yearlyValidCapitalsources = this.capitalsourceService
              .getAllCapitalsourcesByDateRange(userId, beginOfYear, endOfMonth);
          final List<CapitalsourceID> yearlyAssetCapitalsourceIds = yearlyValidCapitalsources
              .stream().filter(Capitalsource::isAsset).map(Capitalsource::getId)
              .collect(Collectors.toCollection(ArrayList::new));
          if (!yearlyAssetCapitalsourceIds.isEmpty()) {
            turnoverEndOfYearCalculated = this.moneyflowService
                .getSumAmountByDateRangeForCapitalsourceIds(userId, beginOfYear, endOfMonth,
                    yearlyAssetCapitalsourceIds);
            amountBeginOfYear = this.getAssetAmountFromMonthlySettlements(userId,
                (year.intValue() - 1), Month.DECEMBER, yearlyAssetCapitalsourceIds);
            // Special case: The very first year of moneyflows in the system will most
            // likely have no final settlement of the last year (December settlement).
            // In this case, use the the earliest settlement of the current year for the
            // annual turnover.
            if (amountBeginOfYear == null) {
              final List<Month> allSettledMonth = this.monthlySettlementService.getAllMonth(userId,
                  year);
              amountBeginOfYear = this.getAssetAmountFromMonthlySettlements(userId, year,
                  allSettledMonth.get(0), yearlyAssetCapitalsourceIds);
            }
          }
        }
      }
      response.setMonth(month.getValue());
    }

    response.setYear(year);
    if (turnoverCapitalsources != null && !turnoverCapitalsources.isEmpty()) {
      response.setReportTurnoverCapitalsourceTransports(turnoverCapitalsources);
    }
    if (moneyflows != null && !moneyflows.isEmpty()) {
      final List<MoneyflowTransport> moneyflowTransports = moneyflows.stream()
          .filter(mf -> !mf.isPrivat() || mf.getUser().getId().equals(userId))
          .map(mf -> super.map(mf, MoneyflowTransport.class))
          .collect(Collectors.toCollection(ArrayList::new));
      response.setMoneyflowTransports(moneyflowTransports);
      if (!moneyflowSplitEntries.isEmpty()) {
        final ArrayList<MoneyflowSplitEntry> moneyflowSplitEntryList = moneyflowSplitEntries
            .values().stream().flatMap(List::stream)
            .collect(Collectors.toCollection(ArrayList::new));
        response.setMoneyflowSplitEntryTransports(
            super.mapList(moneyflowSplitEntryList, MoneyflowSplitEntryTransport.class));
      }
      if (!moneyflowIdsWithReceipts.isEmpty()) {
        final List<Long> moneyflowIdLongs = moneyflowIdsWithReceipts.stream()
            .map(MoneyflowID::getId).collect(Collectors.toCollection(ArrayList::new));
        response.setMoneyflowsWithReceipt(moneyflowIdLongs);
      }
      response.setTurnoverEndOfYearCalculated(turnoverEndOfYearCalculated);
      response.setAmountBeginOfYear(amountBeginOfYear);
    }
    return ResponseEntity.ok(response);
  }

  private BigDecimal getMovementForCapitalsourceAndDateRange(final List<Moneyflow> moneyflows,
      final CapitalsourceID capitalsourceId, final LocalDate dateFrom, final LocalDate dateTil) {
    return moneyflows.parallelStream()
        .filter(mf -> mf.getCapitalsource().getId().equals(capitalsourceId))
        .filter(mf -> mf.getBookingDate().compareTo(dateFrom) >= 0)
        .filter(mf -> mf.getBookingDate().compareTo(dateTil) <= 0).map(Moneyflow::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private BigDecimal getAssetAmountFromMonthlySettlements(final UserID userId, final Integer year,
      final Month month, final List<CapitalsourceID> yearlyAssetCapitalsourceIds) {
    final List<MonthlySettlement> monthlySettlements = this.monthlySettlementService
        .getAllMonthlySettlementsByYearMonth(userId, year, month);
    if (monthlySettlements != null && !monthlySettlements.isEmpty()) {
      return monthlySettlements.parallelStream()
          .filter(ms -> yearlyAssetCapitalsourceIds.contains(ms.getCapitalsource().getId()))
          .map(MonthlySettlement::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    return null;
  }

  private Month getMonth(final Integer requestMonth) {
    return requestMonth != null && requestMonth >= 1 && requestMonth <= 12
        ? Month.of(requestMonth.intValue())
        : null;
  }

  private void addCurrentAmount(final Capitalsource capitalsource, final LocalDate startOfMonth,
      final BigDecimal startAmount,
      final ReportTurnoverCapitalsourceTransport turnoverCapitalsource,
      final List<Moneyflow> moneyflows, final List<ImportedBalance> importedBalances) {
    final LocalDate today = LocalDate.now();
    final CapitalsourceID capitalsourceId = capitalsource.getId();
    ImportedBalance importedBalance = null;
    if (capitalsource.getImportAllowed() != CapitalsourceImport.NOT_ALLOWED) {
      for (final ImportedBalance impBalance : importedBalances) {
        if (impBalance.getCapitalsource().getId().equals(capitalsource.getId())) {
          importedBalance = impBalance;
        }
      }
    }
    if (importedBalance != null) {
      turnoverCapitalsource.setAmountCurrent(importedBalance.getBalance());
      turnoverCapitalsource.setAmountCurrentState(
          importedBalance.getDate().atZone(ZoneId.systemDefault()).toOffsetDateTime());
    } else {
      final BigDecimal movement = this.getMovementForCapitalsourceAndDateRange(moneyflows,
          capitalsourceId, startOfMonth, today);
      final BigDecimal amount = startAmount.add(movement);
      turnoverCapitalsource.setAmountCurrent(amount);
    }
  }
}
