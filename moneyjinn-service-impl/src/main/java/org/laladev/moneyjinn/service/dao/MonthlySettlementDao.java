//
// Copyright (c) 2015 Oliver Lehmann <oliver@laladev.org>
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

import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.laladev.moneyjinn.service.dao.data.MonthlySettlementData;
import org.laladev.moneyjinn.service.dao.mapper.IMonthlySettlementDaoMapper;

@Named
public class MonthlySettlementDao {
	@Inject
	IMonthlySettlementDaoMapper mapper;

	public List<Short> getAllYears(final Long userId) {
		return this.mapper.getAllYears(userId);
	}

	public List<Short> getAllMonth(final Long userId, final Short year) {
		return this.mapper.getAllMonth(userId, year);
	}

	public List<MonthlySettlementData> getAllMonthlySettlementsByYearMonth(final Long userId, final Short year,
			final Short month) {
		return this.mapper.getAllMonthlySettlementsByYearMonth(userId, year, month);
	}

	public LocalDate getMaxSettlementDate(final Long userId) {
		return this.mapper.getMaxSettlementDate(userId);
	}

	public LocalDate getMinSettlementDate(final Long userId) {
		return this.mapper.getMinSettlementDate(userId);
	}

	public boolean checkMonthlySettlementsExists(final Long userId, final Short year, final Short month) {
		final Short exists = this.mapper.checkMonthlySettlementsExists(userId, year, month);
		return exists != null;
	}

	public void upsertMonthlySettlement(final MonthlySettlementData monthlySettlementData) {
		this.mapper.upsertMonthlySettlement(monthlySettlementData);
	}

	public void deleteMonthlySettlement(final Long userId, final Short year, final Short month) {
		this.mapper.deleteMonthlySettlement(userId, year, month);
	}

	public List<MonthlySettlementData> getAllMonthlySettlementsByRangeAndCapitalsource(final Long user,
			final int startYear, final int startMonth, final int endYear, final int endMonth,
			final List<Long> capitalsourceIdLongs) {
		return this.mapper.getAllMonthlySettlementsByRangeAndCapitalsource(user, startYear, startMonth, endYear,
				endMonth, capitalsourceIdLongs);
	}

}
