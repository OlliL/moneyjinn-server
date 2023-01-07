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

package org.laladev.moneyjinn.server.controller.mapper;

import java.time.Month;
import org.laladev.moneyjinn.core.mapper.IMapper;
import org.laladev.moneyjinn.core.rest.model.transport.MonthlySettlementTransport;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.monthlysettlement.MonthlySettlement;
import org.laladev.moneyjinn.model.monthlysettlement.MonthlySettlementID;

public class MonthlySettlementTransportMapper
    implements IMapper<MonthlySettlement, MonthlySettlementTransport> {
  @Override
  public MonthlySettlement mapBToA(final MonthlySettlementTransport monthlySettlementTransport) {
    final MonthlySettlement monthlySettlement = new MonthlySettlement();
    if (monthlySettlementTransport.getId() != null) {
      monthlySettlement.setId(new MonthlySettlementID(monthlySettlementTransport.getId()));
    }
    monthlySettlement.setAmount(monthlySettlementTransport.getAmount());
    monthlySettlement.setYear(monthlySettlementTransport.getYear());
    final Short month = monthlySettlementTransport.getMonth();
    if (monthlySettlementTransport.getMonth() != null) {
      monthlySettlement.setMonth(Month.of(month));
    }
    if (monthlySettlementTransport.getCapitalsourceid() != null) {
      final Capitalsource capitalsource = new Capitalsource(
          new CapitalsourceID(monthlySettlementTransport.getCapitalsourceid()));
      monthlySettlement.setCapitalsource(capitalsource);
    }
    return monthlySettlement;
  }

  @Override
  public MonthlySettlementTransport mapAToB(final MonthlySettlement monthlySettlement) {
    final MonthlySettlementTransport monthlySettlementTransport = new MonthlySettlementTransport();
    if (monthlySettlement.getId() != null) {
      monthlySettlementTransport.setId(monthlySettlement.getId().getId());
    }
    monthlySettlementTransport.setAmount(monthlySettlement.getAmount());
    monthlySettlementTransport.setYear(monthlySettlement.getYear());
    monthlySettlementTransport.setMonth((short) monthlySettlement.getMonth().getValue());
    final Capitalsource capitalsource = monthlySettlement.getCapitalsource();
    monthlySettlementTransport.setCapitalsourceid(capitalsource.getId().getId());
    monthlySettlementTransport.setCapitalsourcecomment(capitalsource.getComment());
    monthlySettlementTransport
        .setCapitalsourcetype(CapitalsourceTypeMapper.map(capitalsource.getType()));
    monthlySettlementTransport
        .setCapitalsourcegroupuse(capitalsource.isGroupUse() ? (short) 1 : null);
    final User user = monthlySettlement.getUser();
    monthlySettlementTransport.setUserid(user.getId().getId());
    return monthlySettlementTransport;
  }
}
