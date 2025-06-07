//Copyright (c) 2015-2025 Oliver Lehmann <lehmann@ans-netz.de>
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
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.monthlysettlement.ImportedMonthlySettlement;
import org.laladev.moneyjinn.model.monthlysettlement.MonthlySettlement;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.server.controller.api.MonthlySettlementControllerApi;
import org.laladev.moneyjinn.server.controller.mapper.MonthlySettlementTransportMapper;
import org.laladev.moneyjinn.server.model.GetAvailableMonthlySettlementMonthResponse;
import org.laladev.moneyjinn.server.model.MonthlySettlementTransport;
import org.laladev.moneyjinn.server.model.ShowMonthlySettlementCreateResponse;
import org.laladev.moneyjinn.server.model.ShowMonthlySettlementListResponse;
import org.laladev.moneyjinn.server.model.UpsertMonthlySettlementRequest;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.IImportedMonthlySettlementService;
import org.laladev.moneyjinn.service.api.IMoneyflowService;
import org.laladev.moneyjinn.service.api.IMonthlySettlementService;
import org.laladev.moneyjinn.service.api.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class MonthlySettlementController extends AbstractController implements MonthlySettlementControllerApi {
	private final IMonthlySettlementService monthlySettlementService;
	private final ICapitalsourceService capitalsourceService;
	private final IImportedMonthlySettlementService importedMonthlySettlementService;
	private final IMoneyflowService moneyflowService;
	private final IUserService userService;
	private final IAccessRelationService accessRelationService;
	private final MonthlySettlementTransportMapper monthlySettlementTransportMapper;

	@Override
	public ResponseEntity<GetAvailableMonthlySettlementMonthResponse> getAvailableMonth() {
		return this.getAvailableMonthYearMonth(null, null);
	}

	@Override
	public ResponseEntity<GetAvailableMonthlySettlementMonthResponse> getAvailableMonthYear(
			@PathVariable(value = "year") final Integer year) {
		return this.getAvailableMonthYearMonth(year, null);
	}

	@Override
	public ResponseEntity<GetAvailableMonthlySettlementMonthResponse> getAvailableMonthYearMonth(
			@PathVariable(value = "year") final Integer requestYear,
			@PathVariable(value = "month") final Integer requestMonth) {
		final UserID userId = super.getUserId();
		final GetAvailableMonthlySettlementMonthResponse response = new GetAvailableMonthlySettlementMonthResponse();
		final List<Integer> allYears = this.monthlySettlementService.getAllYears(userId);
		Integer year = requestYear;
		Month month = this.getMonth(requestMonth);

		// only continue if settlements where made at all
		if (allYears != null && !allYears.isEmpty()) {
			// validate if settlements are recorded for the given year, if not fall back to
			// the last recorded one
			if (year == null || !allYears.contains(year)) {
				year = allYears.get(allYears.size() - 1);
				month = null;
			}
			final List<Month> allMonth = this.monthlySettlementService.getAllMonth(userId, year);
			if (allMonth != null && !allMonth.isEmpty()) {
				response.setAllMonth(allMonth.stream().map(Month::getValue).toList());
				if (month != null && allMonth.contains(month)) {
					response.setMonth(month.getValue());
				}
			}
			response.setYear(year);
			response.setAllYears(allYears);
		}
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<ShowMonthlySettlementListResponse> showMonthlySettlementListV2(
			@PathVariable(value = "year") final Integer requestYear,
			@PathVariable(value = "month") final Integer requestMonth) {
		final UserID userId = super.getUserId();
		final ShowMonthlySettlementListResponse response = new ShowMonthlySettlementListResponse();
		final Integer year = requestYear;
		final Month month = this.getMonth(requestMonth);

		// only continue if settlements where made at all
		if (month != null && year != null) {
			final List<MonthlySettlement> monthlySettlements = this.monthlySettlementService
					.getAllMonthlySettlementsByYearMonth(userId, year, month);
			final List<MonthlySettlementTransport> monthlySettlementTransports = this.monthlySettlementTransportMapper
					.mapAToB(monthlySettlements);
			response.setMonthlySettlementTransports(monthlySettlementTransports);
		}
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<ShowMonthlySettlementCreateResponse> showMonthlySettlementCreate() {
		return this.showMonthlySettlementCreateYearMonth(null, null);
	}

	@Override
	public ResponseEntity<ShowMonthlySettlementCreateResponse> showMonthlySettlementCreateYear(
			@PathVariable(value = "year") final Integer requestYear) {
		return this.showMonthlySettlementCreateYearMonth(requestYear, null);
	}

	@Override
	public ResponseEntity<ShowMonthlySettlementCreateResponse> showMonthlySettlementCreateYearMonth(
			@PathVariable(value = "year") final Integer requestYear,
			@PathVariable(value = "month") final Integer requestMonth) {
		final UserID userId = super.getUserId();
		final ShowMonthlySettlementCreateResponse response = new ShowMonthlySettlementCreateResponse();
		LocalDate lastDate = this.monthlySettlementService.getMaxSettlementDate(userId);
		boolean previousSettlementExists = false;
		Integer nextYear;
		Month nextMonth;
		if (lastDate != null) {
			final LocalDate nextDateBegin = lastDate.plusMonths(1).withDayOfMonth(1);
			nextYear = nextDateBegin.getYear();
			nextMonth = nextDateBegin.getMonth();
			previousSettlementExists = true;
		} else {
			lastDate = LocalDate.now();
			nextYear = lastDate.getYear();
			nextMonth = lastDate.getMonth();
		}
		Integer year;
		Month month;
		if (requestYear == null && requestMonth == null) {
			year = nextYear;
			month = nextMonth;
		} else {
			if (requestYear != null) {
				year = requestYear;
			} else {
				year = nextYear;
			}
			month = this.getMonth(requestMonth);
			if (month == null) {
				if (year.equals(nextYear)) {
					month = nextMonth;
				} else {
					month = Month.DECEMBER;
				}
			}
		}
		boolean selectedMonthIsNextSettlementMonth = false;
		boolean selectedMonthDoesExist = false;
		boolean editMode = false;
		if (year.equals(nextYear) && month.equals(nextMonth)) {
			// if the specified month is the month after the last settled month, it goes
			// like the
			// default selection
			selectedMonthIsNextSettlementMonth = true;
		} else {
			final boolean settlementExists = this.monthlySettlementService.checkMonthlySettlementsExists(userId, year,
					month);
			if (settlementExists) {
				// if the selected month exists, we switch to the edit-mode
				selectedMonthDoesExist = true;
			}
		}
		final LocalDate beginOfMonth = LocalDate.of(year, month, 1);
		final LocalDate endOfMonth = beginOfMonth.with(TemporalAdjusters.lastDayOfMonth());
		final List<Capitalsource> capitalsources = this.capitalsourceService.getGroupCapitalsourcesByDateRange(userId,
				beginOfMonth, endOfMonth);
		List<MonthlySettlementTransport> importedMonthlySettlementTransports = new ArrayList<>();
		List<MonthlySettlementTransport> monthlySettlementTransports = new ArrayList<>();
		if (capitalsources != null && !capitalsources.isEmpty()) {
			final List<MonthlySettlement> monthlySettlements = new ArrayList<>();
			if (selectedMonthDoesExist) {
				monthlySettlements.addAll(this.getMyEditableMonthlySettlements(userId, year, month));
				/*
				 * It is possible that a capitalsource gets valid for a month which has already
				 * been settled. To make it possible to create a settlement for this new source
				 * it gets added here.
				 */
				final List<CapitalsourceID> capitalsourceIds = monthlySettlements.stream()
						.map(ms -> ms.getCapitalsource().getId()).toList();
				for (final Capitalsource capitalsource : capitalsources) {
					if (capitalsource.getUser().getId().equals(userId)
							&& !capitalsourceIds.contains(capitalsource.getId())) {
						final MonthlySettlement monthlySettlement = new MonthlySettlement();
						monthlySettlement.setYear(year);
						monthlySettlement.setMonth(month);
						monthlySettlement.setAmount(BigDecimal.ZERO);
						monthlySettlement.setCapitalsource(capitalsource);
						monthlySettlement.setUser(capitalsource.getUser());
						monthlySettlements.add(monthlySettlement);
					}
				}
				editMode = true;
			} else {
				// selected month does not exist
				for (final Capitalsource capitalsource : capitalsources) {
					if (capitalsource.getUser().getId().equals(userId)) {
						final MonthlySettlement monthlySettlement = new MonthlySettlement();
						monthlySettlement.setYear(year);
						monthlySettlement.setMonth(month);
						monthlySettlement.setAmount(BigDecimal.ZERO);
						monthlySettlement.setCapitalsource(capitalsource);
						monthlySettlement.setUser(capitalsource.getUser());
						monthlySettlements.add(monthlySettlement);
					}
				}
				if (selectedMonthIsNextSettlementMonth && previousSettlementExists) {
					final List<ImportedMonthlySettlement> importedMonthlySettlements = new ArrayList<>(
							this.importedMonthlySettlementService.getImportedMonthlySettlementsByMonth(userId, year,
									month));
					final List<MonthlySettlement> relevantImportedMonthlySettlements = new ArrayList<>();
					// prefill Amount if the selected month is the next one
					final Integer lastYear = lastDate.getYear();
					final Month lastMonth = lastDate.getMonth();
					final List<MonthlySettlement> lastMonthlySettlements = new ArrayList<>(this.monthlySettlementService
							.getAllMonthlySettlementsByYearMonth(userId, lastYear, lastMonth));
					final Iterator<MonthlySettlement> iteratorMonthlySettlements = monthlySettlements.iterator();
					while (iteratorMonthlySettlements.hasNext()) {
						final MonthlySettlement monthlySettlement = iteratorMonthlySettlements.next();
						final CapitalsourceID capitalsourceId = monthlySettlement.getCapitalsource().getId();
						// try to find the amount to settle at first in the array of imported
						// end-of-month amounts.
						boolean imported = false;
						if (importedMonthlySettlements != null) {
							final Iterator<ImportedMonthlySettlement> iteratorImportedMonthlySettlements = importedMonthlySettlements
									.iterator();
							while (iteratorImportedMonthlySettlements.hasNext()) {
								final ImportedMonthlySettlement importedMonthlySettlement = iteratorImportedMonthlySettlements
										.next();
								if (importedMonthlySettlement.getCapitalsource().getId().equals(capitalsourceId)) {
									imported = true;
									monthlySettlement.setAmount(importedMonthlySettlement.getAmount());
									relevantImportedMonthlySettlements.add(monthlySettlement);
									iteratorMonthlySettlements.remove();
									iteratorImportedMonthlySettlements.remove();
									break;
								}
							}
						}
						if (!imported) {
							// if no settlement was imported, try to calculate it
							BigDecimal baseAmount = BigDecimal.ZERO;
							final Iterator<MonthlySettlement> iteratorLastMonthlySettlements = lastMonthlySettlements
									.iterator();
							while (iteratorLastMonthlySettlements.hasNext()) {
								final MonthlySettlement lastMonthlySettlement = iteratorLastMonthlySettlements.next();
								if (lastMonthlySettlement.getCapitalsource().getId().equals(capitalsourceId)) {
									baseAmount = lastMonthlySettlement.getAmount();
									iteratorLastMonthlySettlements.remove();
									break;
								}
							}
							final BigDecimal movedAmount = this.moneyflowService
									.getSumAmountByDateRangeForCapitalsourceId(userId, beginOfMonth, endOfMonth,
											capitalsourceId);
							monthlySettlement.setAmount(baseAmount.add(movedAmount));
						}
					}
					importedMonthlySettlementTransports = this.monthlySettlementTransportMapper
							.mapAToB(relevantImportedMonthlySettlements);
				}
			}
			monthlySettlementTransports = this.monthlySettlementTransportMapper.mapAToB(monthlySettlements);
		}
		response.setYear(year);
		response.setMonth(month.getValue());
		response.setEditMode((editMode ? 1 : 0));
		if (monthlySettlementTransports != null && !monthlySettlementTransports.isEmpty()) {
			response.setMonthlySettlementTransports(monthlySettlementTransports);
		}
		if (importedMonthlySettlementTransports != null && !importedMonthlySettlementTransports.isEmpty()) {
			response.setImportedMonthlySettlementTransports(importedMonthlySettlementTransports);
		}
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<Void> upsertMonthlySettlement(@RequestBody final UpsertMonthlySettlementRequest request) {
		final UserID userId = super.getUserId();
		final List<MonthlySettlement> monthlySettlements = this.monthlySettlementTransportMapper
				.mapBToA(request.getMonthlySettlementTransports());
		final User user = this.userService.getUserById(userId);
		final Group group = this.accessRelationService.getCurrentGroup(userId);
		for (final MonthlySettlement monthlySettlement : monthlySettlements) {
			monthlySettlement.setUser(user);
			monthlySettlement.setGroup(group);
		}
		final ValidationResult validationResult = this.monthlySettlementService
				.upsertMonthlySettlements(monthlySettlements);

		this.throwValidationExceptionIfInvalid(validationResult);

		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<Void> deleteMonthlySettlement(@PathVariable(value = "year") final Integer requestYear,
			@PathVariable(value = "month") final Integer requestMonth) {
		final UserID userId = super.getUserId();
		this.monthlySettlementService.deleteMonthlySettlement(userId, requestYear, this.getMonth(requestMonth));

		return ResponseEntity.noContent().build();
	}

	private Month getMonth(final Integer requestMonth) {
		return requestMonth != null && requestMonth >= 1 && requestMonth <= 12 ? Month.of(requestMonth.intValue())
				: null;
	}

	private List<MonthlySettlement> getMyEditableMonthlySettlements(final UserID userId, final Integer year,
			final Month month) {
		final List<MonthlySettlement> monthlySettlements = this.monthlySettlementService
				.getAllMonthlySettlementsByYearMonth(userId, year, month);
		if (monthlySettlements != null && !monthlySettlements.isEmpty()) {
			return monthlySettlements.stream().filter(ms -> ms.getUser().getId().equals(userId)).toList();
		}
		return Collections.emptyList();
	}
}
