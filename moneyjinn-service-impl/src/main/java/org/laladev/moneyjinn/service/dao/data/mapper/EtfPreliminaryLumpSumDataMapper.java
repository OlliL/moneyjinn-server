//
// Copyright (c) 2015-2024 Oliver Lehmann <lehmann@ans-netz.de>
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

import java.time.YearMonth;

import org.laladev.moneyjinn.converter.EtfIsinMapper;
import org.laladev.moneyjinn.converter.IMapstructMapper;
import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.laladev.moneyjinn.converter.javatypes.MonthToIntegerMapper;
import org.laladev.moneyjinn.model.etf.EtfPreliminaryLumpSum;
import org.laladev.moneyjinn.service.dao.data.EtfPreliminaryLumpSumData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapStructConfig.class, uses = { EtfIsinMapper.class, MonthToIntegerMapper.class })
public interface EtfPreliminaryLumpSumDataMapper
		extends IMapstructMapper<EtfPreliminaryLumpSum, EtfPreliminaryLumpSumData> {
	@Override
	@Mapping(target = "id", source = "isin")
	@Mapping(target = "yearMonth", source = ".", qualifiedByName = "mapToYearMonth")
	EtfPreliminaryLumpSum mapBToA(EtfPreliminaryLumpSumData etfPreliminaryLumpSumData);

	@Override
	@Mapping(target = "isin", source = "id")
	@Mapping(target = "year", source = "yearMonth", qualifiedByName = "mapToYear")
	@Mapping(target = "month", source = "yearMonth", qualifiedByName = "mapToMonth")
	EtfPreliminaryLumpSumData mapAToB(EtfPreliminaryLumpSum etfPreliminaryLumpSum);

	@Named("mapToYearMonth")
	default YearMonth mapToYearMonth(final EtfPreliminaryLumpSumData etfPreliminaryLumpSumData) {
		return YearMonth.of(etfPreliminaryLumpSumData.getYear(), etfPreliminaryLumpSumData.getMonth());
	}

	@Named("mapToYear")
	default Integer mapToYear(final YearMonth yearMonth) {
		return yearMonth.getYear();
	}

	@Named("mapToMonth")
	default Integer mapToMonth(final YearMonth yearMonth) {
		return yearMonth.getMonthValue();
	}
}