//
// Copyright (c) 2015-2023 Oliver Lehmann <lehmann@ans-netz.de>
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
// ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
// OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
// OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE.
//

package org.laladev.moneyjinn.service.dao.data.mapper;

import java.time.Month;
import org.laladev.moneyjinn.core.mapper.IMapper;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.monthlysettlement.MonthlySettlement;
import org.laladev.moneyjinn.model.monthlysettlement.MonthlySettlementID;
import org.laladev.moneyjinn.service.dao.data.MonthlySettlementData;

public class MonthlySettlementDataMapper
    implements IMapper<MonthlySettlement, MonthlySettlementData> {
  @Override
  public MonthlySettlement mapBToA(final MonthlySettlementData monthlySettlementData) {
    final MonthlySettlement monthlySettlement = new MonthlySettlement();
    monthlySettlement.setId(new MonthlySettlementID(monthlySettlementData.getId()));
    monthlySettlement.setAmount(monthlySettlementData.getAmount());
    monthlySettlement.setMonth(Month.of(monthlySettlementData.getMonth().intValue()));
    monthlySettlement.setYear(monthlySettlementData.getYear());
    monthlySettlement.setCapitalsource(
        new Capitalsource(new CapitalsourceID(monthlySettlementData.getMcsCapitalsourceId())));
    monthlySettlement.setUser(new User(new UserID(monthlySettlementData.getMacIdCreator())));
    return monthlySettlement;
  }

  @Override
  public MonthlySettlementData mapAToB(final MonthlySettlement monthlySettlement) {
    final MonthlySettlementData monthlySettlementData = new MonthlySettlementData();
    // might be null for new MonthlySettlements
    if (monthlySettlement.getId() != null) {
      monthlySettlementData.setId(monthlySettlement.getId().getId());
    }
    monthlySettlementData.setAmount(monthlySettlement.getAmount());
    monthlySettlementData.setMonth((short) monthlySettlement.getMonth().getValue());
    monthlySettlementData.setYear(monthlySettlement.getYear());
    monthlySettlementData
        .setMcsCapitalsourceId(monthlySettlement.getCapitalsource().getId().getId());
    if (monthlySettlement.getUser() != null) {
      monthlySettlementData.setMacIdCreator(monthlySettlement.getUser().getId().getId());
    }
    if (monthlySettlement.getGroup() != null) {
      monthlySettlementData.setMacIdAccessor(monthlySettlement.getGroup().getId().getId());
    }
    return monthlySettlementData;
  }
}
