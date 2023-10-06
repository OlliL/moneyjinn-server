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

import org.laladev.moneyjinn.converter.CapitalsourceIdMapper;
import org.laladev.moneyjinn.converter.ContractpartnerIdMapper;
import org.laladev.moneyjinn.converter.GroupIdMapper;
import org.laladev.moneyjinn.converter.IMapstructMapper;
import org.laladev.moneyjinn.converter.MoneyflowIdMapper;
import org.laladev.moneyjinn.converter.PostingAccountIdMapper;
import org.laladev.moneyjinn.converter.UserIdMapper;
import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.laladev.moneyjinn.model.moneyflow.Moneyflow;
import org.laladev.moneyjinn.service.dao.data.MoneyflowData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class, uses = { MoneyflowIdMapper.class, CapitalsourceIdMapper.class,
		ContractpartnerIdMapper.class, PostingAccountIdMapper.class, UserIdMapper.class, GroupIdMapper.class })
public interface MoneyflowDataMapper extends IMapstructMapper<Moneyflow, MoneyflowData> {
	@Override
	@Mapping(target = "bookingDate", source = "bookingdate")
	@Mapping(target = "invoiceDate", source = "invoicedate")
	@Mapping(target = "capitalsource.id", source = "mcsCapitalsourceId")
	@Mapping(target = "contractpartner.id", source = "mcpContractpartnerId")
	@Mapping(target = "postingAccount.id", source = "mpaPostingAccountId")
	@Mapping(target = "user.id", source = "macIdCreator")
	@Mapping(target = "group.id", source = "macIdAccessor")
	@Mapping(target = "moneyflowSplitEntries", ignore = true)
	Moneyflow mapBToA(MoneyflowData moneyflowData);

	@Override
	@Mapping(target = "bookingdate", source = "bookingDate")
	@Mapping(target = "invoicedate", source = "invoiceDate")
	@Mapping(target = "mcsCapitalsourceId", source = "capitalsource.id")
	@Mapping(target = "mcpContractpartnerId", source = "contractpartner.id")
	@Mapping(target = "mpaPostingAccountId", source = "postingAccount.id")
	@Mapping(target = "macIdCreator", source = "user.id")
	@Mapping(target = "macIdAccessor", source = "group.id")
	public MoneyflowData mapAToB(Moneyflow moneyflow);
}
