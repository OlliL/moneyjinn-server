//Copyright (c) 2015 Oliver Lehmann <oliver@laladev.org>
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
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.laladev.moneyjinn.businesslogic.model.ImportedBalance;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.businesslogic.model.moneyflow.Moneyflow;
import org.laladev.moneyjinn.businesslogic.model.monthlysettlement.MonthlySettlement;
import org.laladev.moneyjinn.businesslogic.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.businesslogic.service.api.IImportedBalanceService;
import org.laladev.moneyjinn.businesslogic.service.api.IMoneyflowService;
import org.laladev.moneyjinn.businesslogic.service.api.IMonthlySettlementService;
import org.laladev.moneyjinn.core.rest.model.report.ListReportsResponse;
import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ReportTurnoverCapitalsourceTransport;
import org.laladev.moneyjinn.server.annotation.RequiresAuthorization;
import org.laladev.moneyjinn.server.controller.mapper.CapitalsourceStateMapper;
import org.laladev.moneyjinn.server.controller.mapper.CapitalsourceTypeMapper;
import org.laladev.moneyjinn.server.controller.mapper.MoneyflowTransportMapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
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
	private ICapitalsourceService capitalsourceService;
	@Inject
	private IMonthlySettlementService monthlySettlementService;
	@Inject
	private IImportedBalanceService importedBalanceService;
	// @Inject
	// private ISettingService settingService;

	@Override
	protected void addBeanMapper() {
		super.registerBeanMapper(new MoneyflowTransportMapper());
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
				final LocalDate beginOfNextMonth = beginOfMonth.plusMonths(1L);

				prevMonth = beginOfPrevMonth.getMonth();
				prevYear = (short) beginOfPrevMonth.getYear();
				nextMonth = beginOfNextMonth.getMonth();
				nextYear = (short) beginOfNextMonth.getYear();

				moneyflows = this.moneyflowService.getAllMoneyflowsByDateRange(userId, beginOfMonth, endOfMonth);
				final List<MonthlySettlement> settlementsPrevMonth = this.monthlySettlementService
						.getAllMonthlySettlementsByYearMonth(userId, prevYear, prevMonth);
				final List<MonthlySettlement> settlementsThisMonth = this.monthlySettlementService
						.getAllMonthlySettlementsByYearMonth(userId, year, month);
				final int indexInAllMonthList = allMonth.indexOf(month);

				if (month != Month.JANUARY) {
					if (indexInAllMonthList > 0) {
						previousMonthHasMoneyflows = true;
					}
				} else {
					previousMonthHasMoneyflows = this.moneyflowService.monthHasMoneyflows(userId, prevYear, prevMonth);
				}

				if (month != Month.DECEMBER) {
					if (indexInAllMonthList < allMonth.size() - 1) {
						nextMonthHasMoneyflows = true;
					}
				} else {
					nextMonthHasMoneyflows = this.moneyflowService.monthHasMoneyflows(userId, nextYear, nextMonth);
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
						// valid this month . ignore it
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
						if (settlementsThisMonth != null && !settlementsThisMonth.isEmpty()
								&& newCapitalsourcesSettled.containsKey(capitalsourceId)) {
							turnoverCapitalsource.setAmountEndOfMonthFixed(
									newCapitalsourcesSettled.get(capitalsourceId).getAmount());
							newCapitalsourcesSettled.remove(capitalsourceId);
						} else {
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
							this.addCurrentAmount(userId, capitalsource, beginOfMonth, BigDecimal.ZERO,
									turnoverCapitalsource, moneyflows);
							turnoverCapitalsources.add(turnoverCapitalsource);
						}
					}

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
			response.setMoneyflowTransports(super.mapList(moneyflows, MoneyflowTransport.class));
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
		if (capitalsource.isImportAllowed()) {
			importedBalances = this.importedBalanceService.getAllImportedBalancesByCapitalsourceIds(userId,
					Arrays.asList(capitalsourceId));
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
