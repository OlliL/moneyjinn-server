//Copyright (c) 2015-2018 Oliver Lehmann <lehmann@ans-netz.de>
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
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.laladev.moneyjinn.core.rest.model.report.ListReportsResponse;
import org.laladev.moneyjinn.core.rest.model.report.ShowMonthlyReportGraphRequest;
import org.laladev.moneyjinn.core.rest.model.report.ShowMonthlyReportGraphResponse;
import org.laladev.moneyjinn.core.rest.model.report.ShowReportingFormResponse;
import org.laladev.moneyjinn.core.rest.model.report.ShowTrendsFormResponse;
import org.laladev.moneyjinn.core.rest.model.report.ShowTrendsGraphRequest;
import org.laladev.moneyjinn.core.rest.model.report.ShowTrendsGraphResponse;
import org.laladev.moneyjinn.core.rest.model.report.ShowYearlyReportGraphRequest;
import org.laladev.moneyjinn.core.rest.model.report.ShowYearlyReportGraphResponse;
import org.laladev.moneyjinn.core.rest.model.report.transport.PostingAccountAmountTransport;
import org.laladev.moneyjinn.core.rest.model.report.transport.ReportTurnoverCapitalsourceTransport;
import org.laladev.moneyjinn.core.rest.model.report.transport.TrendsCalculatedTransport;
import org.laladev.moneyjinn.core.rest.model.report.transport.TrendsSettledTransport;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;
import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowSplitEntryTransport;
import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowTransport;
import org.laladev.moneyjinn.core.rest.model.transport.PostingAccountTransport;
import org.laladev.moneyjinn.model.ImportedBalance;
import org.laladev.moneyjinn.model.PostingAccount;
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
import org.laladev.moneyjinn.server.annotation.RequiresAuthorization;
import org.laladev.moneyjinn.server.controller.mapper.CapitalsourceStateMapper;
import org.laladev.moneyjinn.server.controller.mapper.CapitalsourceTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.CapitalsourceTypeMapper;
import org.laladev.moneyjinn.server.controller.mapper.MoneyflowSplitEntryTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.MoneyflowTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.PostingAccountAmountTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.PostingAccountTransportMapper;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.IImportedBalanceService;
import org.laladev.moneyjinn.service.api.IMoneyflowReceiptService;
import org.laladev.moneyjinn.service.api.IMoneyflowService;
import org.laladev.moneyjinn.service.api.IMoneyflowSplitEntryService;
import org.laladev.moneyjinn.service.api.IMonthlySettlementService;
import org.laladev.moneyjinn.service.api.IPostingAccountService;
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
@RequestMapping("/moneyflow/server/report/")
public class ReportController extends AbstractController {

	@Inject
	private IMoneyflowService moneyflowService;
	@Inject
	private IMoneyflowSplitEntryService moneyflowSplitEntryService;
	@Inject
	private IMoneyflowReceiptService moneyflowReceiptService;
	@Inject
	private ICapitalsourceService capitalsourceService;
	@Inject
	private IMonthlySettlementService monthlySettlementService;
	@Inject
	private IImportedBalanceService importedBalanceService;
	@Inject
	private IPostingAccountService postingAccountService;
	@Inject
	private ISettingService settingService;

	@Override
	protected void addBeanMapper() {
		super.registerBeanMapper(new MoneyflowTransportMapper());
		super.registerBeanMapper(new MoneyflowSplitEntryTransportMapper());
		super.registerBeanMapper(new CapitalsourceTransportMapper());
		super.registerBeanMapper(new PostingAccountTransportMapper());
		super.registerBeanMapper(new PostingAccountAmountTransportMapper());
	}

	@RequestMapping(value = "showReportingForm", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowReportingFormResponse showReportingForm() {
		final UserID userId = super.getUserId();
		final ShowReportingFormResponse response = new ShowReportingFormResponse();

		final List<Short> allYears = this.moneyflowService.getAllYears(userId);
		final List<PostingAccount> postingAccounts = this.postingAccountService.getAllPostingAccounts();
		final ClientReportingUnselectedPostingAccountIdsSetting setting = this.settingService
				.getClientReportingUnselectedPostingAccountIdsSetting(userId);

		response.setAllYears(allYears);
		response.setPostingAccountTransports(super.mapList(postingAccounts, PostingAccountTransport.class));
		if (setting != null && setting.getSetting() != null && !setting.getSetting().isEmpty()) {
			final List<Long> postingAccountIds = setting.getSetting().stream().map(PostingAccountID::getId)
					.collect(Collectors.toCollection(ArrayList::new));
			response.setPostingAccountIds(postingAccountIds);
		}

		return response;
	}

	@RequestMapping(value = "showMonthlyReportGraph", method = { RequestMethod.PUT })
	@RequiresAuthorization
	public ShowMonthlyReportGraphResponse showMonthlyReportGraph(
			@RequestBody final ShowMonthlyReportGraphRequest request) {
		final UserID userId = super.getUserId();
		final ShowMonthlyReportGraphResponse response = new ShowMonthlyReportGraphResponse();

		if (request.getStartDate() != null && request.getEndDate() != null && request.getPostingAccountIdsYes() != null
				&& !request.getPostingAccountIdsYes().isEmpty()) {
			final LocalDate startDate = request.getStartDate().toLocalDate();
			final LocalDate endDate = request.getEndDate().toLocalDate();

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
			final List<PostingAccount> postingAccounts = this.postingAccountService.getAllPostingAccounts();

			final List<PostingAccountAmountTransport> responsePostingAccountAmount = super.mapList(
					postingAccountAmounts, PostingAccountAmountTransport.class);
			final List<PostingAccountTransport> responsePostingAccounts = super.mapList(postingAccounts,
					PostingAccountTransport.class);

			response.setPostingAccountAmountTransports(responsePostingAccountAmount);
			response.setPostingAccountTransports(responsePostingAccounts);
		}
		return response;
	}

	@RequestMapping(value = "showYearlyReportGraph", method = { RequestMethod.PUT })
	@RequiresAuthorization
	public ShowYearlyReportGraphResponse showYearlyReportGraph(
			@RequestBody final ShowYearlyReportGraphRequest request) {
		final UserID userId = super.getUserId();
		final ShowYearlyReportGraphResponse response = new ShowYearlyReportGraphResponse();

		if (request.getStartDate() != null && request.getEndDate() != null && request.getPostingAccountIdsYes() != null
				&& !request.getPostingAccountIdsYes().isEmpty()) {
			final LocalDate startDate = request.getStartDate().toLocalDate();
			final LocalDate endDate = request.getEndDate().toLocalDate();

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
					.getAllMoneyflowsByDateRangeGroupedByYearPostingAccount(userId, postingAccountIdsYes, startDate,
							endDate);
			final List<PostingAccount> postingAccounts = this.postingAccountService.getAllPostingAccounts();

			final List<PostingAccountAmountTransport> responsePostingAccountAmount = super.mapList(
					postingAccountAmounts, PostingAccountAmountTransport.class);
			final List<PostingAccountTransport> responsePostingAccounts = super.mapList(postingAccounts,
					PostingAccountTransport.class);

			response.setPostingAccountAmountTransports(responsePostingAccountAmount);
			response.setPostingAccountTransports(responsePostingAccounts);
		}

		return response;
	}

	@RequestMapping(value = "showTrendsForm", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowTrendsFormResponse showTrendsForm() {
		final UserID userId = super.getUserId();
		final ShowTrendsFormResponse response = new ShowTrendsFormResponse();

		final LocalDate maxMoneyflowDate = this.moneyflowService.getMaxMoneyflowDate(userId);
		final LocalDate minMonthlysettlementwDate = this.monthlySettlementService.getMinSettlementDate(userId);

		if (maxMoneyflowDate != null) {
			response.setMaxDate(Date.valueOf(maxMoneyflowDate));
		}
		if (minMonthlysettlementwDate != null) {
			response.setMinDate(Date.valueOf(minMonthlysettlementwDate));
		}

		final List<Capitalsource> capitalsources = this.capitalsourceService.getAllCapitalsources(userId);
		response.setCapitalsourceTransports(super.mapList(capitalsources, CapitalsourceTransport.class));

		final ClientTrendCapitalsourceIDsSetting clientTrendCapitalsourceIDsSetting = this.settingService
				.getClientTrendCapitalsourceIDsSetting(userId);
		if (clientTrendCapitalsourceIDsSetting != null && clientTrendCapitalsourceIDsSetting.getSetting() != null
				&& !clientTrendCapitalsourceIDsSetting.getSetting().isEmpty()) {
			final List<Long> capitalsourceIds = clientTrendCapitalsourceIDsSetting.getSetting().stream()
					.map(CapitalsourceID::getId).collect(Collectors.toCollection(ArrayList::new));
			response.setSettingTrendCapitalsourceIds(capitalsourceIds);
		}

		return response;
	}

	@RequestMapping(value = "showTrendsGraph", method = { RequestMethod.PUT })
	@RequiresAuthorization
	public ShowTrendsGraphResponse showTrendsGraph(@RequestBody final ShowTrendsGraphRequest request) {
		final UserID userId = super.getUserId();
		final ShowTrendsGraphResponse response = new ShowTrendsGraphResponse();

		if (request.getStartDate() != null && request.getEndDate() != null && request.getCapitalSourceIds() != null
				&& !request.getCapitalSourceIds().isEmpty()) {
			final LocalDate startDate = request.getStartDate().toLocalDate();
			final LocalDate endDate = request.getEndDate().toLocalDate().with(TemporalAdjusters.lastDayOfMonth());

			final List<CapitalsourceID> capitalsourceIds = request.getCapitalSourceIds().stream()
					.map(CapitalsourceID::new).collect(Collectors.toCollection(ArrayList::new));
			final ClientTrendCapitalsourceIDsSetting setting = new ClientTrendCapitalsourceIDsSetting(capitalsourceIds);

			// Save the selection for the next time the form is shown
			this.settingService.setClientTrendCapitalsourceIDsSetting(userId, setting);

			final List<MonthlySettlement> monthlySettlements = this.monthlySettlementService
					.getAllMonthlySettlementsByRangeAndCapitalsource(userId, startDate, endDate, capitalsourceIds);

			final List<Capitalsource> capitalsources = this.capitalsourceService.getAllCapitalsources(userId);
			final Map<CapitalsourceID, LocalDate> validTilCapitalsourceIdMap = new HashMap<>();
			for (final Capitalsource capitalsource : capitalsources) {
				validTilCapitalsourceIdMap.put(capitalsource.getId(), capitalsource.getValidTil());
			}

			final SortedMap<Short, SortedMap<Month, BigDecimal>> settlementAmounts = new TreeMap<>();
			final SortedMap<Short, SortedMap<Month, BigDecimal>> moneyflowAmounts = new TreeMap<>();

			Month lastMonth = null;
			Short lastYear = null;
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
						final BigDecimal amount = this.moneyflowService.getSumAmountByDateRangeForCapitalsourceIds(
								userId, beginOfMonth, endOfMonth, capitalsourceIds);
						lastAmount = lastAmount.add(amount);

						final Month month = endOfMonth.getMonth();
						final Short year = (short) endOfMonth.getYear();

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
			for (final Entry<Short, SortedMap<Month, BigDecimal>> yearEntry : settlementAmounts.entrySet()) {
				final Short year = yearEntry.getKey();
				for (final Entry<Month, BigDecimal> monthEntry : yearEntry.getValue().entrySet()) {
					final TrendsSettledTransport trendsSettledTransport = new TrendsSettledTransport();
					trendsSettledTransport.setYear(year);
					trendsSettledTransport.setMonth((short) monthEntry.getKey().getValue());
					trendsSettledTransport.setAmount(monthEntry.getValue());
					trendsSettledTransports.add(trendsSettledTransport);
				}
			}

			final List<TrendsCalculatedTransport> trendsCalculatedTransports = new ArrayList<>();
			for (final Entry<Short, SortedMap<Month, BigDecimal>> yearEntry : moneyflowAmounts.entrySet()) {
				final Short year = yearEntry.getKey();
				for (final Entry<Month, BigDecimal> monthEntry : yearEntry.getValue().entrySet()) {
					final TrendsCalculatedTransport trendsCalculatedTransport = new TrendsCalculatedTransport();
					trendsCalculatedTransport.setYear(year);
					trendsCalculatedTransport.setMonth((short) monthEntry.getKey().getValue());
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
		return response;
	}

	private void filterByValidity(final List<CapitalsourceID> capitalsourceIds,
			final Map<CapitalsourceID, LocalDate> validTilCapitalsourceIdMap, final LocalDate beginOfMonth) {
		capitalsourceIds.removeIf(csi -> validTilCapitalsourceIdMap.get(csi).isBefore(beginOfMonth));
	}

	@RequestMapping(value = "listReports", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ListReportsResponse listReports() {
		return this.listReports(null, null);
	}

	@RequestMapping(value = "listReports/{year}", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ListReportsResponse listReports(@PathVariable(value = "year") final Short requestYear) {
		return this.listReports(requestYear, null);
	}

	@RequestMapping(value = "listReports/{year}/{month}", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ListReportsResponse listReports(@PathVariable(value = "year") final Short requestYear,
			@PathVariable(value = "month") final Short requestMonth) {
		final UserID userId = super.getUserId();
		final ListReportsResponse response = new ListReportsResponse();

		final List<Short> allYears = this.moneyflowService.getAllYears(userId);
		final LocalDate today = LocalDate.now();

		final List<ReportTurnoverCapitalsourceTransport> turnoverCapitalsources = new ArrayList<>();
		List<Moneyflow> moneyflows = null;
		BigDecimal turnoverEndOfYearCalculated = null;
		BigDecimal amountBeginOfYear = null;
		boolean nextMonthHasMoneyflows = false;
		boolean previousMonthHasMoneyflows = false;
		Month prevMonth = null;
		Short prevYear = null;
		Month nextMonth = null;
		Short nextYear = null;
		final Map<CapitalsourceID, MonthlySettlement> newCapitalsourcesSettled = new HashMap<>();
		Map<MoneyflowID, List<MoneyflowSplitEntry>> moneyflowSplitEntries = new HashMap<>();
		List<MoneyflowID> moneyflowIdsWithReceipts = new ArrayList<>();
		List<Month> allMonth = null;

		Short year = requestYear;
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
				final LocalDate beginOfPrevMonth = beginOfMonth.minusMonths(1L);

				// TODO: Do not hardcode "previous" month for determine the last recorded
				// settlement.
				final Month prevMonthSettlement = beginOfPrevMonth.getMonth();
				final Short prevYearSettlement = (short) beginOfPrevMonth.getYear();

				moneyflows = this.moneyflowService.getAllMoneyflowsByDateRangeIncludingPrivate(userId, beginOfMonth,
						endOfMonth);

				final List<MoneyflowID> relevantMoneyflowIds = moneyflows.stream()
						.filter(mf -> !mf.isPrivat() || mf.getUser().getId().equals(userId)).map(Moneyflow::getId)
						.collect(Collectors.toCollection(ArrayList::new));

				moneyflowSplitEntries = this.moneyflowSplitEntryService.getMoneyflowSplitEntries(userId,
						relevantMoneyflowIds);
				moneyflowIdsWithReceipts = this.moneyflowReceiptService.getMoneyflowIdsWithReceipt(userId,
						relevantMoneyflowIds);

				final List<MonthlySettlement> settlementsPrevMonth = this.monthlySettlementService
						.getAllMonthlySettlementsByYearMonth(userId, prevYearSettlement, prevMonthSettlement);
				final List<MonthlySettlement> settlementsThisMonth = this.monthlySettlementService
						.getAllMonthlySettlementsByYearMonth(userId, year, month);
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
					prevYear = (short) previousDate.getYear();
				}
				if (nextDate != null) {
					nextMonthHasMoneyflows = true;
					nextMonth = nextDate.getMonth();
					nextYear = (short) nextDate.getYear();
				}

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
					// this will hold all capitalsources which will be removed later if they where
					// processed it will then only contain new capitalsources which have no
					// settlement in the previous nor in this month yet
					final List<Capitalsource> newCapitalsourcesUnsettled = new ArrayList<>();
					newCapitalsourcesUnsettled.addAll(validCapitalsources);

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
							this.addCurrentAmount(userId, lastSettlementCapitalsource, beginOfMonth,
									lastSettlement.getAmount(), turnoverCapitalsource, moneyflows);
						}
						final BigDecimal movementCalculated = this.getMovementForCapitalsourceAndDateRange(moneyflows,
								capitalsourceId, beginOfMonth, endOfMonth);
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
								this.addCurrentAmount(userId, capitalsource, beginOfMonth, BigDecimal.ZERO,
										turnoverCapitalsource, moneyflows);
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

					final List<CapitalsourceID> yearlyAssetCapitalsourceIds = yearlyValidCapitalsources.stream()
							.filter(Capitalsource::isAsset).map(Capitalsource::getId)
							.collect(Collectors.toCollection(ArrayList::new));

					if (!yearlyAssetCapitalsourceIds.isEmpty()) {
						turnoverEndOfYearCalculated = this.moneyflowService.getSumAmountByDateRangeForCapitalsourceIds(
								userId, beginOfYear, endOfMonth, yearlyAssetCapitalsourceIds);
						amountBeginOfYear = this.getAssetAmountFromMonthlySettlements(userId,
								(short) (year.intValue() - 1), Month.DECEMBER, yearlyAssetCapitalsourceIds);
						// Special case: The very first year of moneyflows in the system will most
						// likely have no final settlement of the last year (December settlement).
						// In this case, use the the earliest settlement of the current year for the
						// annual turnover.
						if (amountBeginOfYear == null) {
							final List<Month> allSettledMonth = this.monthlySettlementService.getAllMonth(userId, year);
							amountBeginOfYear = this.getAssetAmountFromMonthlySettlements(userId, year,
									allSettledMonth.get(0), yearlyAssetCapitalsourceIds);
						}
					}
				}
			} else {
				month = null;
			}
		}

		response.setYear(year);
		if (month != null) {
			response.setMonth((short) month.getValue());
		}

		if (allYears != null && !allYears.isEmpty()) {
			response.setAllYears(allYears);
		}

		if (allMonth != null && !allMonth.isEmpty()) {
			response.setAllMonth(
					allMonth.stream().map(m -> (short) m.getValue()).collect(Collectors.toCollection(ArrayList::new)));
		}

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
				final ArrayList<MoneyflowSplitEntry> moneyflowSplitEntryList = moneyflowSplitEntries.values().stream()
						.flatMap(List::stream).collect(Collectors.toCollection(ArrayList::new));

				response.setMoneyflowSplitEntryTransports(
						super.mapList(moneyflowSplitEntryList, MoneyflowSplitEntryTransport.class));
			}

			if (!moneyflowIdsWithReceipts.isEmpty()) {
				final List<Long> moneyflowIdLongs = moneyflowIdsWithReceipts.stream().map(MoneyflowID::getId)
						.collect(Collectors.toCollection(ArrayList::new));
				response.setMoneyflowsWithReceipt(moneyflowIdLongs);
			}
		}

		response.setTurnoverEndOfYearCalculated(turnoverEndOfYearCalculated);
		response.setAmountBeginOfYear(amountBeginOfYear);
		if (nextMonthHasMoneyflows) {
			response.setNextMonthHasMoneyflows((short) 1);
		}
		if (previousMonthHasMoneyflows) {
			response.setPreviousMonthHasMoneyflows((short) 1);
		}
		if (prevMonth != null) {
			response.setPreviousMonth((short) prevMonth.getValue());
		}
		response.setPreviousYear(prevYear);
		if (nextMonth != null) {
			response.setNextMonth((short) nextMonth.getValue());
		}
		response.setNextYear(nextYear);

		return response;
	}

	private BigDecimal getMovementForCapitalsourceAndDateRange(final List<Moneyflow> moneyflows,
			final CapitalsourceID capitalsourceId, final LocalDate dateFrom, final LocalDate dateTil) {
		return moneyflows.stream().filter(mf -> mf.getCapitalsource().getId().equals(capitalsourceId))
				.filter(mf -> mf.getBookingDate().compareTo(dateFrom) >= 0)
				.filter(mf -> mf.getBookingDate().compareTo(dateTil) <= 0).map(Moneyflow::getAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	private BigDecimal getAssetAmountFromMonthlySettlements(final UserID userId, final Short year, final Month month,
			final List<CapitalsourceID> yearlyAssetCapitalsourceIds) {
		final List<MonthlySettlement> monthlySettlements = this.monthlySettlementService
				.getAllMonthlySettlementsByYearMonth(userId, year, month);

		if (monthlySettlements != null && !monthlySettlements.isEmpty()) {
			return monthlySettlements.stream()
					.filter(ms -> yearlyAssetCapitalsourceIds.contains(ms.getCapitalsource().getId()))
					.map(MonthlySettlement::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
		}
		return null;
	}

	private Month getMonth(final Short requestMonth) {
		return requestMonth != null && requestMonth >= 1 && requestMonth <= 12 ? Month.of(requestMonth.intValue())
				: null;
	}

	private void addCurrentAmount(final UserID userId, final Capitalsource capitalsource, final LocalDate startOfMonth,
			final BigDecimal startAmount, final ReportTurnoverCapitalsourceTransport turnoverCapitalsource,
			final List<Moneyflow> moneyflows) {
		final LocalDate today = LocalDate.now();

		final CapitalsourceID capitalsourceId = capitalsource.getId();

		List<ImportedBalance> importedBalances = null;
		if (capitalsource.getImportAllowed() != CapitalsourceImport.NOT_ALLOWED) {
			importedBalances = this.importedBalanceService.getAllImportedBalancesByCapitalsourceIds(userId,
					Collections.singletonList(capitalsourceId));
		}

		if (importedBalances != null && !importedBalances.isEmpty()) {
			final ImportedBalance importedBalance = importedBalances.get(0);
			turnoverCapitalsource.setAmountCurrent(importedBalance.getBalance());
			turnoverCapitalsource.setAmountCurrentState(Timestamp.valueOf(importedBalance.getDate()));
		} else {
			final BigDecimal movement = this.getMovementForCapitalsourceAndDateRange(moneyflows, capitalsourceId,
					startOfMonth, today);
			final BigDecimal amount = startAmount.add(movement);
			turnoverCapitalsource.setAmountCurrent(amount);
		}
	}

}
