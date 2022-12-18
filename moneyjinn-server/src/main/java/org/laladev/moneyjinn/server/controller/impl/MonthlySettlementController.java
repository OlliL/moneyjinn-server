//Copyright (c) 2015-2017 Oliver Lehmann <lehmann@ans-netz.de>
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
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.inject.Inject;

import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import org.laladev.moneyjinn.core.rest.model.monthlysettlement.GetAvailableMonthResponse;
import org.laladev.moneyjinn.core.rest.model.monthlysettlement.ShowMonthlySettlementCreateResponse;
import org.laladev.moneyjinn.core.rest.model.monthlysettlement.ShowMonthlySettlementDeleteResponse;
import org.laladev.moneyjinn.core.rest.model.monthlysettlement.ShowMonthlySettlementListResponse;
import org.laladev.moneyjinn.core.rest.model.monthlysettlement.UpsertMonthlySettlementRequest;
import org.laladev.moneyjinn.core.rest.model.transport.MonthlySettlementTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.monthlysettlement.ImportedMonthlySettlement;
import org.laladev.moneyjinn.model.monthlysettlement.MonthlySettlement;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.server.annotation.RequiresAuthorization;
import org.laladev.moneyjinn.server.controller.mapper.ImportedMonthlySettlementTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.MonthlySettlementTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.ValidationItemTransportMapper;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.IImportedMonthlySettlementService;
import org.laladev.moneyjinn.service.api.IMoneyflowService;
import org.laladev.moneyjinn.service.api.IMonthlySettlementService;
import org.laladev.moneyjinn.service.api.IUserService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequestMapping("/moneyflow/server/monthlysettlement/")
public class MonthlySettlementController extends AbstractController {
	@Inject
	private IMonthlySettlementService monthlySettlementService;
	@Inject
	private ICapitalsourceService capitalsourceService;
	@Inject
	private IImportedMonthlySettlementService importedMonthlySettlementService;
	@Inject
	private IMoneyflowService moneyflowService;
	@Inject
	private IUserService userService;
	@Inject
	private IAccessRelationService accessRelationService;

	@Override
	protected void addBeanMapper() {
		this.registerBeanMapper(new MonthlySettlementTransportMapper());
		this.registerBeanMapper(new ImportedMonthlySettlementTransportMapper());
		this.registerBeanMapper(new ValidationItemTransportMapper());
	}

	@RequestMapping(value = "getAvailableMonth", method = { RequestMethod.GET })
	@RequiresAuthorization
	public GetAvailableMonthResponse getAvailableMonth() {
		return this.getAvailableMonth(null, null);
	}

	@RequestMapping(value = "getAvailableMonth/{year}", method = { RequestMethod.GET })
	@RequiresAuthorization
	public GetAvailableMonthResponse getAvailableMonth(@PathVariable(value = "year") final Short year) {
		return this.getAvailableMonth(year, null);
	}

	@RequestMapping(value = "getAvailableMonth/{year}/{month}", method = { RequestMethod.GET })
	@RequiresAuthorization
	public GetAvailableMonthResponse getAvailableMonth(@PathVariable(value = "year") final Short requestYear,
			@PathVariable(value = "month") final Short requestMonth) {
		final UserID userId = super.getUserId();
		final GetAvailableMonthResponse response = new GetAvailableMonthResponse();

		final List<Short> allYears = this.monthlySettlementService.getAllYears(userId);
		List<Month> allMonth = null;

		Short year = requestYear;
		Month month = this.getMonth(requestMonth);

		// only continue if settlements where made at all
		if (allYears != null && !allYears.isEmpty()) {

			// validate if settlements are recorded for the given year, if not fall back to
			// the last recorded one
			if (year == null || !allYears.contains(year)) {
				year = allYears.get(allYears.size() - 1);
				month = null;
			}

			allMonth = this.monthlySettlementService.getAllMonth(userId, year);

			if (allMonth != null && !allMonth.isEmpty()) {
				response.setAllMonth(allMonth.stream().map(m -> (short) m.getValue())
						.collect(Collectors.toCollection(ArrayList::new)));
				if (month != null && allMonth.contains(month)) {
					response.setMonth((short) month.getValue());
				}
			}
			response.setYear(year);
			response.setAllYears(allYears);
		}

		return response;
	}

	@RequestMapping(value = "showMonthlySettlementListV2/{year}/{month}", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowMonthlySettlementListResponse showMonthlySettlementListV2(
			@PathVariable(value = "year") final Short requestYear,
			@PathVariable(value = "month") final Short requestMonth) {
		final UserID userId = super.getUserId();
		final ShowMonthlySettlementListResponse response = new ShowMonthlySettlementListResponse();

		final Short year = requestYear;
		final Month month = this.getMonth(requestMonth);

		List<MonthlySettlementTransport> monthlySettlementTransports = null;

		// only continue if settlements where made at all
		if (month != null && year != null) {

			final List<MonthlySettlement> monthlySettlements = this.monthlySettlementService
					.getAllMonthlySettlementsByYearMonth(userId, year, month);

			monthlySettlementTransports = super.mapList(monthlySettlements, MonthlySettlementTransport.class);
			response.setMonthlySettlementTransports(monthlySettlementTransports);
		}

		return response;
	}

	@RequestMapping(value = "showMonthlySettlementList", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowMonthlySettlementListResponse showMonthlySettlementList() {
		return this.showMonthlySettlementList(null, null);
	}

	@RequestMapping(value = "showMonthlySettlementList/{year}", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowMonthlySettlementListResponse showMonthlySettlementList(@PathVariable(value = "year") final Short year) {
		return this.showMonthlySettlementList(year, null);
	}

	@RequestMapping(value = "showMonthlySettlementList/{year}/{month}", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowMonthlySettlementListResponse showMonthlySettlementList(
			@PathVariable(value = "year") final Short requestYear,
			@PathVariable(value = "month") final Short requestMonth) {
		final UserID userId = super.getUserId();
		final ShowMonthlySettlementListResponse response = new ShowMonthlySettlementListResponse();

		final List<Short> allYears = this.monthlySettlementService.getAllYears(userId);
		List<Month> allMonth = null;

		Short year = requestYear;
		Month month = this.getMonth(requestMonth);

		List<MonthlySettlementTransport> monthlySettlementTransports = null;
		int numberOfEditableSettlements = 0;

		// only continue if settlements where made at all
		if (allYears != null && !allYears.isEmpty()) {

			// validate if settlements are recorded for the given year, if not fall back to
			// the
			// last recorded one
			if (year == null || !allYears.contains(year)) {
				year = allYears.get(allYears.size() - 1);
				month = null;
			}

			allMonth = this.monthlySettlementService.getAllMonth(userId, year);

			if (month != null && allMonth != null && allMonth.contains(month)) {

				final List<MonthlySettlement> monthlySettlements = this.monthlySettlementService
						.getAllMonthlySettlementsByYearMonth(userId, year, month);

				for (final MonthlySettlement monthlySettlement : monthlySettlements) {
					if (userId.equals(monthlySettlement.getUser().getId())) {
						numberOfEditableSettlements++;
					}

				}
				monthlySettlementTransports = super.mapList(monthlySettlements, MonthlySettlementTransport.class);
				response.setMonth((short) month.getValue());
			}
			response.setYear(year);
		}

		final LocalDate today = LocalDate.now();
		final List<Capitalsource> capitalSources = this.capitalsourceService.getGroupCapitalsourcesByDateRange(userId,
				today, today);

		if (capitalSources != null && !capitalSources.isEmpty()) {
			response.setNumberOfAddableSettlements(
					Long.valueOf(capitalSources.stream().filter(cs -> cs.getUser().getId().equals(userId)).count())
							.intValue());
		}

		if (allYears != null && !allYears.isEmpty()) {
			response.setAllYears(allYears);
		}

		if (allMonth != null && !allMonth.isEmpty()) {
			response.setAllMonth(
					allMonth.stream().map(m -> (short) m.getValue()).collect(Collectors.toCollection(ArrayList::new)));
		}

		if (monthlySettlementTransports != null && !monthlySettlementTransports.isEmpty()) {
			response.setMonthlySettlementTransports(monthlySettlementTransports);
			response.setNumberOfEditableSettlements(numberOfEditableSettlements);
		}

		return response;
	}

	@RequestMapping(value = "showMonthlySettlementCreate", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowMonthlySettlementCreateResponse showMonthlySettlementCreate() {
		return this.showMonthlySettlementCreate(null, null);
	}

	@RequestMapping(value = "showMonthlySettlementCreate/{year}", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowMonthlySettlementCreateResponse showMonthlySettlementCreate(
			@PathVariable(value = "year") final Short requestYear) {
		return this.showMonthlySettlementCreate(requestYear, null);
	}

	@RequestMapping(value = "showMonthlySettlementCreate/{year}/{month}", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowMonthlySettlementCreateResponse showMonthlySettlementCreate(
			@PathVariable(value = "year") final Short requestYear,
			@PathVariable(value = "month") final Short requestMonth) {

		final UserID userId = super.getUserId();
		final ShowMonthlySettlementCreateResponse response = new ShowMonthlySettlementCreateResponse();

		LocalDate lastDate = this.monthlySettlementService.getMaxSettlementDate(userId);

		boolean previousSettlementExists = false;
		Short nextYear;
		Month nextMonth;

		if (lastDate != null) {
			final LocalDate nextDateBegin = lastDate.plusMonths(1).withDayOfMonth(1);
			nextYear = (short) nextDateBegin.getYear();
			nextMonth = nextDateBegin.getMonth();
			previousSettlementExists = true;
		} else {
			lastDate = LocalDate.now();
			nextYear = (short) lastDate.getYear();
			nextMonth = lastDate.getMonth();
		}

		Short year;
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
			List<MonthlySettlement> monthlySettlements = new ArrayList<>();

			if (selectedMonthDoesExist) {
				monthlySettlements = this.getMyEditableMonthlySettlements(userId, year, month);
				/*
				 * I could be, that for an already fixed month, a "new" capitalsource gets valid
				 * afterwards. To make it possible to create a settlement for this new source,
				 * add it here.
				 */
				final List<CapitalsourceID> capitalsourceIds = monthlySettlements.stream()
						.map(ms -> ms.getCapitalsource().getId()).collect(Collectors.toCollection(ArrayList::new));

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
					final List<ImportedMonthlySettlement> importedMonthlySettlements = this.importedMonthlySettlementService
							.getImportedMonthlySettlementsByMonth(userId, year, month);
					final List<MonthlySettlement> relevantImportedMonthlySettlements = new ArrayList<>();

					// prefill Amount if the selected month is the next one
					final Short lastYear = (short) lastDate.getYear();
					final Month lastMonth = lastDate.getMonth();
					final List<MonthlySettlement> lastMonthlySettlements = this.monthlySettlementService
							.getAllMonthlySettlementsByYearMonth(userId, lastYear, lastMonth);

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
					importedMonthlySettlementTransports = super.mapList(relevantImportedMonthlySettlements,
							MonthlySettlementTransport.class);
				}
			}
			monthlySettlementTransports = super.mapList(monthlySettlements, MonthlySettlementTransport.class);
		}

		response.setYear(year);
		response.setMonth((short) month.getValue());
		response.setEditMode((short) (editMode ? 1 : 0));
		if (monthlySettlementTransports != null && !monthlySettlementTransports.isEmpty()) {
			response.setMonthlySettlementTransports(monthlySettlementTransports);
		}
		if (importedMonthlySettlementTransports != null && !importedMonthlySettlementTransports.isEmpty()) {
			response.setImportedMonthlySettlementTransports(importedMonthlySettlementTransports);
		}

		return response;
	}

	@RequestMapping(value = "showMonthlySettlementDelete/{year}/{month}", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowMonthlySettlementDeleteResponse showMonthlySettlementDelete(
			@PathVariable(value = "year") final Short requestYear,
			@PathVariable(value = "month") final Short requestMonth) {

		final UserID userId = super.getUserId();
		final ShowMonthlySettlementDeleteResponse response = new ShowMonthlySettlementDeleteResponse();

		final Month month = this.getMonth(requestMonth);

		final List<MonthlySettlement> monthlySettlements = this.getMyEditableMonthlySettlements(userId, requestYear,
				month);

		if (!monthlySettlements.isEmpty()) {
			response.setMonthlySettlementTransports(
					super.mapList(monthlySettlements, MonthlySettlementTransport.class));
		}

		return response;
	}

	@RequestMapping(value = "upsertMonthlySettlement", method = { RequestMethod.POST })
	@RequiresAuthorization
	public ValidationResponse upsertMonthlySettlement(@RequestBody final UpsertMonthlySettlementRequest request) {
		final UserID userId = super.getUserId();
		final List<MonthlySettlement> monthlySettlements = super.mapList(request.getMonthlySettlementTransports(),
				MonthlySettlement.class);

		final User user = this.userService.getUserById(userId);
		final Group group = this.accessRelationService.getAccessor(userId);

		for (final MonthlySettlement monthlySettlement : monthlySettlements) {
			monthlySettlement.setUser(user);
			monthlySettlement.setGroup(group);
		}

		final ValidationResult validationResult = this.monthlySettlementService
				.upsertMonthlySettlements(monthlySettlements);

		final ValidationResponse response = new ValidationResponse();
		response.setResult(validationResult.isValid());

		if (!validationResult.isValid()) {
			response.setValidationItemTransports(
					super.mapList(validationResult.getValidationResultItems(), ValidationItemTransport.class));
		}
		return response;
	}

	@RequestMapping(value = "deleteMonthlySettlement/{year}/{month}", method = { RequestMethod.DELETE })
	@RequiresAuthorization
	public void deleteMonthlySettlement(@PathVariable(value = "year") final Short requestYear,
			@PathVariable(value = "month") final Short requestMonth) {
		final UserID userId = super.getUserId();
		this.monthlySettlementService.deleteMonthlySettlement(userId, requestYear, this.getMonth(requestMonth));
	}

	private Month getMonth(final Short requestMonth) {
		return requestMonth != null && requestMonth >= 1 && requestMonth <= 12 ? Month.of(requestMonth.intValue())
				: null;
	}

	private List<MonthlySettlement> getMyEditableMonthlySettlements(final UserID userId, final Short year,
			final Month month) {

		final List<MonthlySettlement> monthlySettlements = this.monthlySettlementService
				.getAllMonthlySettlementsByYearMonth(userId, year, month);

		if (monthlySettlements != null && !monthlySettlements.isEmpty()) {
			return monthlySettlements.stream().filter(ms -> ms.getUser().getId().equals(userId))
					.collect(Collectors.toCollection(ArrayList::new));
		}

		return new ArrayList<>();
	}
}
