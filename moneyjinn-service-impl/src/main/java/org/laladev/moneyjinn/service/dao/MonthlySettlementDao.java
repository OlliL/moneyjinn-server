//
// Copyright (c) 2015-2025 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.service.dao;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import org.laladev.moneyjinn.service.dao.data.MonthlySettlementData;
import org.laladev.moneyjinn.service.dao.mapper.IMonthlySettlementDaoMapper;

import java.time.LocalDate;
import java.util.List;

@Named
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class MonthlySettlementDao {
    private final IMonthlySettlementDaoMapper mapper;

    public List<Integer> getAllYears(final Long userId) {
        return this.mapper.getAllYears(userId);
    }

    public List<Integer> getAllMonth(final Long userId, final Integer year) {
        return this.mapper.getAllMonth(userId, year);
    }

    public List<MonthlySettlementData> getAllMonthlySettlementsByYearMonth(final Long userId, final Integer year,
                                                                           final Integer month) {
        return this.mapper.getAllMonthlySettlementsByYearMonth(userId, year, month);
    }

    public LocalDate getMaxSettlementDate(final Long userId) {
        return this.mapper.getMaxSettlementDate(userId);
    }

    public LocalDate getMinSettlementDate(final Long userId) {
        return this.mapper.getMinSettlementDate(userId);
    }

    public boolean checkMonthlySettlementsExists(final Long userId, final Integer year, final Integer month) {
        return Boolean.TRUE.equals(this.mapper.checkMonthlySettlementsExists(userId, year, month));
    }

    public void upsertMonthlySettlement(final MonthlySettlementData monthlySettlementData) {
        this.mapper.upsertMonthlySettlement(monthlySettlementData);
    }

    public void deleteMonthlySettlement(final Long userId, final Integer year, final Integer month) {
        this.mapper.deleteMonthlySettlement(userId, year, month);
    }

    public List<MonthlySettlementData> getAllMonthlySettlementsByRangeAndCapitalsource(final Long user,
                                                                                       final int startYear, final int startMonth, final int endYear, final int endMonth,
                                                                                       final List<Long> capitalsourceIdLongs) {
        return this.mapper.getAllMonthlySettlementsByRangeAndCapitalsource(user, startYear, startMonth, endYear,
                endMonth, capitalsourceIdLongs);
    }
}
