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

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.businesslogic.model.monthlysettlement.MonthlySettlement;
import org.laladev.moneyjinn.businesslogic.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.businesslogic.service.api.IMonthlySettlementService;
import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import org.laladev.moneyjinn.core.rest.model.monthlysettlement.ShowMonthlySettlementCreateResponse;
import org.laladev.moneyjinn.core.rest.model.monthlysettlement.ShowMonthlySettlementDeleteResponse;
import org.laladev.moneyjinn.core.rest.model.monthlysettlement.ShowMonthlySettlementListResponse;
import org.laladev.moneyjinn.core.rest.model.monthlysettlement.UpsertMonthlySettlementRequest;
import org.laladev.moneyjinn.core.rest.model.transport.MonthlySettlementTransport;
import org.laladev.moneyjinn.server.annotation.RequiresAuthorization;
import org.laladev.moneyjinn.server.controller.mapper.ImportedMonthlySettlementTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.MonthlySettlementTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.ValidationItemTransportMapper;
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
	IMonthlySettlementService monthlySettlementService;
	@Inject
	ICapitalsourceService capitalsourceService;

	@Override
	protected void addBeanMapper() {
		this.registerBeanMapper(new MonthlySettlementTransportMapper());
		this.registerBeanMapper(new ImportedMonthlySettlementTransportMapper());
		this.registerBeanMapper(new ValidationItemTransportMapper());
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
		Month month = Month.of(requestMonth.intValue());

		List<MonthlySettlementTransport> monthlySettlementTransports = null;
		int numberOfEditableSettlements = 0;

		// only continue if settlements where made at all
		if (allYears != null && !allYears.isEmpty()) {

			// validate if settlements are recorded for the given year, if not fall back to the last
			// recorded one
			if (!allYears.contains(year)) {
				year = allYears.get(allYears.size() - 1);
				month = null;
			}

			allMonth = this.monthlySettlementService.getAllMonth(userId, year);

			if (allMonth != null && allMonth.contains(month)) {

				final List<MonthlySettlement> monthlySettlements = this.monthlySettlementService
						.getAllMonthlySettlementsByYearMonth(userId, year, month);

				for (final MonthlySettlement monthlySettlement : monthlySettlements) {
					if (userId.equals(monthlySettlement.getUser().getId())
							|| monthlySettlement.getCapitalsource().isGroupUse()) {
						numberOfEditableSettlements++;
					}

				}
				monthlySettlementTransports = super.mapList(monthlySettlements, MonthlySettlementTransport.class);
			}
		}

		final LocalDate today = LocalDate.now();
		final List<Capitalsource> capitalSources = this.capitalsourceService.getGroupCapitalsourcesByDateRange(userId,
				today, today);

		int numberOfAddableSettlements = 0;
		if (capitalSources != null) {
			numberOfAddableSettlements = capitalSources.size();
		}

		response.setAllYears(allYears);
		response.setYear(year);
		response.setMonth((short) month.getValue());

		if (allMonth != null && !allMonth.isEmpty()) {
			response.setAllMonth(
					allMonth.stream().map(m -> (short) m.getValue()).collect(Collectors.toCollection(ArrayList::new)));
		}

		if (monthlySettlementTransports != null && !monthlySettlementTransports.isEmpty()) {
			response.setMonthlySettlementTransports(monthlySettlementTransports);
			response.setNumberOfEditableSettlements(numberOfEditableSettlements);
		}
		response.setNumberOfAddableSettlements(numberOfAddableSettlements);

		return response;
	}

	@RequestMapping(value = "showMonthlySettlementCreate", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowMonthlySettlementCreateResponse showMonthlySettlementCreate() {
		return this.showMonthlySettlementCreate(null, null);
	}

	@RequestMapping(value = "showMonthlySettlementCreate/{year}/{month}", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowMonthlySettlementCreateResponse showMonthlySettlementCreate(
			@PathVariable(value = "year") final Short year, @PathVariable(value = "month") final Short month) {
		return null;
	}

	@RequestMapping(value = "showMonthlySettlementDelete/{year}/{month}", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowMonthlySettlementDeleteResponse showMonthlySettlementDelete(
			@PathVariable(value = "year") final Short year, @PathVariable(value = "month") final Short month) {
		return null;
	}

	@RequestMapping(value = "upsertMonthlySettlement", method = { RequestMethod.POST })
	@RequiresAuthorization
	public ValidationResponse upsertMonthlySettlement(@RequestBody final UpsertMonthlySettlementRequest request) {
		return null;
	}

	@RequestMapping(value = "deleteMonthlySettlement/{year}/{month}", method = { RequestMethod.DELETE })
	@RequiresAuthorization
	public void deleteMonthlySettlement(@PathVariable(value = "year") final Short year,
			@PathVariable(value = "month") final Short month) {
	}
}
