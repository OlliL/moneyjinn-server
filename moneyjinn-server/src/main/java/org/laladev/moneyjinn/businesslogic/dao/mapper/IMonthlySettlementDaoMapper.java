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

package org.laladev.moneyjinn.businesslogic.dao.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.laladev.moneyjinn.businesslogic.dao.data.MonthlySettlementData;

public interface IMonthlySettlementDaoMapper {
	public List<Short> getAllYears(Long userId);

	public List<Short> getAllMonth(@Param("userId") Long userId, @Param("year") Short year);

	public List<MonthlySettlementData> getAllMonthlySettlementsByYearMonth(@Param("userId") Long userId,
			@Param("year") Short year, @Param("month") Short month);

	public LocalDate getMaxSettlementDate(Long userId);

	public Short checkMonthlySettlementsExists(@Param("userId") Long userId, @Param("year") Short year,
			@Param("month") Short month);

	public void upsertMonthlySettlement(MonthlySettlementData monthlySettlementData);

	public void deleteMonthlySettlement(@Param("userId") Long userId, @Param("year") Short year,
			@Param("month") Short month);

	public List<MonthlySettlementData> getAllMonthlySettlementsByRangeAndCapitalsource(@Param("userId") Long user,
			@Param("startYear") int startYear, @Param("startMonth") int startMonth, @Param("endYear") int endYear,
			@Param("endMonth") int endMonth, @Param("mcsCapitalsourceIds") List<Long> capitalsourceIdLongs);

}