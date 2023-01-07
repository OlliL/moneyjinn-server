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
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.laladev.moneyjinn.core.rest.model.event.ShowEventListResponse;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowStatus;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.IImportedMoneyflowService;
import org.laladev.moneyjinn.service.api.IMonthlySettlementService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequestMapping("/moneyflow/server/event/")
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class EventController extends AbstractController {
  private final IMonthlySettlementService monthlySettlementService;
  private final ICapitalsourceService capitalsourceService;
  private final IImportedMoneyflowService importedMoneyflowService;

  @Override
  @PostConstruct
  protected void addBeanMapper() {
    // No Mapping needed.
  }

  @RequestMapping(value = "showEventList", method = { RequestMethod.GET })
  public ShowEventListResponse showEventList() {
    final UserID userId = super.getUserId();
    final ShowEventListResponse response = new ShowEventListResponse();
    // missing monthly settlements from last month?
    final LocalDate beginOfPreviousMonth = LocalDate.now().minusMonths(1L).withDayOfMonth(1);
    final Month month = beginOfPreviousMonth.getMonth();
    final Short year = (short) beginOfPreviousMonth.getYear();
    final boolean monthlySettlementExists = this.monthlySettlementService
        .checkMonthlySettlementsExists(userId, year, month);

    final LocalDate today = LocalDate.now();
    final List<Capitalsource> capitalsources = this.capitalsourceService
        .getGroupCapitalsourcesByDateRange(userId, today, today);
    if (capitalsources != null && !capitalsources.isEmpty()) {
      final List<CapitalsourceID> capitalsourceIds = capitalsources.stream()
          .map(Capitalsource::getId).collect(Collectors.toCollection(ArrayList::new));
      final Integer numberOfImportedMoneyflows = this.importedMoneyflowService
          .countImportedMoneyflows(userId, capitalsourceIds, ImportedMoneyflowStatus.CREATED);
      response.setNumberOfImportedMoneyflows(numberOfImportedMoneyflows);
    }
    response.setMonthlySettlementMissing(!monthlySettlementExists);
    response.setMonthlySettlementMonth((short) month.getValue());
    response.setMonthlySettlementYear(year);
    return response;
  }
}
