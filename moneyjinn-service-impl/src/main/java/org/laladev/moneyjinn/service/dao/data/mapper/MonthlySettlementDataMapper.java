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

package org.laladev.moneyjinn.service.dao.data.mapper;

import org.laladev.moneyjinn.converter.CapitalsourceIdMapper;
import org.laladev.moneyjinn.converter.GroupIdMapper;
import org.laladev.moneyjinn.converter.IMapstructMapper;
import org.laladev.moneyjinn.converter.MonthlySettlementIdMapper;
import org.laladev.moneyjinn.converter.UserIdMapper;
import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.laladev.moneyjinn.converter.javatypes.MonthToIntegerMapper;
import org.laladev.moneyjinn.model.monthlysettlement.MonthlySettlement;
import org.laladev.moneyjinn.service.dao.data.MonthlySettlementData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class, uses = { MonthlySettlementIdMapper.class, MonthToIntegerMapper.class,
		CapitalsourceIdMapper.class, UserIdMapper.class, GroupIdMapper.class })
public interface MonthlySettlementDataMapper extends IMapstructMapper<MonthlySettlement, MonthlySettlementData> {
	@Override
	@Mapping(target = "user.id", source = "mauUserId")
	@Mapping(target = "group.id", source = "magGroupId")
	@Mapping(target = "capitalsource.id", source = "mcsCapitalsourceId")
	MonthlySettlement mapBToA(MonthlySettlementData monthlySettlementData);

	@Override
	@Mapping(target = "mauUserId", source = "user.id")
	@Mapping(target = "magGroupId", source = "group.id")
	@Mapping(target = "mcsCapitalsourceId", source = "capitalsource.id")
	MonthlySettlementData mapAToB(MonthlySettlement monthlySettlement);
}
