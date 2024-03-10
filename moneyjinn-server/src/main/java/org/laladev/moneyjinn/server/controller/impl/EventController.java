//Copyright (c) 2015-2024 Oliver Lehmann <lehmann@ans-netz.de>
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
import java.util.List;

import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowStatus;
import org.laladev.moneyjinn.server.controller.api.EventControllerApi;
import org.laladev.moneyjinn.server.model.ShowEventListResponse;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.IImportedMoneyflowService;
import org.laladev.moneyjinn.service.api.IMonthlySettlementService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class EventController extends AbstractController implements EventControllerApi {
	private final IMonthlySettlementService monthlySettlementService;
	private final ICapitalsourceService capitalsourceService;
	private final IImportedMoneyflowService importedMoneyflowService;

	@Override
	@PostConstruct
	protected void addBeanMapper() {
		// No Mapping needed.
	}

	@Override
	public ResponseEntity<ShowEventListResponse> showEventList() {
		final UserID userId = super.getUserId();
		final ShowEventListResponse response = new ShowEventListResponse();
		// missing monthly settlements from last month?
		final LocalDate beginOfPreviousMonth = LocalDate.now().minusMonths(1L).withDayOfMonth(1);
		final Month month = beginOfPreviousMonth.getMonth();
		final Integer year = beginOfPreviousMonth.getYear();
		final boolean monthlySettlementExists = this.monthlySettlementService.checkMonthlySettlementsExists(userId,
				year, month);

		final LocalDate today = LocalDate.now();
		final List<Capitalsource> capitalsources = this.capitalsourceService.getGroupCapitalsourcesByDateRange(userId,
				today, today);
		if (capitalsources != null && !capitalsources.isEmpty()) {
			final List<CapitalsourceID> capitalsourceIds = capitalsources.stream().map(Capitalsource::getId).toList();
			final Integer numberOfImportedMoneyflows = this.importedMoneyflowService.countImportedMoneyflows(userId,
					capitalsourceIds, ImportedMoneyflowStatus.CREATED);
			response.setNumberOfImportedMoneyflows(numberOfImportedMoneyflows);
		}
		response.setMonthlySettlementMissing(!monthlySettlementExists);
		response.setMonthlySettlementMonth(month.getValue());
		response.setMonthlySettlementYear(year);
		return ResponseEntity.ok(response);
	}
}
