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

package org.laladev.moneyjinn.server.controller.mapper;

import org.laladev.moneyjinn.converter.*;
import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.laladev.moneyjinn.converter.javatypes.BooleanToIntegerMapper;
import org.laladev.moneyjinn.model.moneyflow.Moneyflow;
import org.laladev.moneyjinn.server.model.MoneyflowTransport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapStructConfig.class, uses = {CapitalsourceIdMapper.class, ContractpartnerIdMapper.class,
        PostingAccountIdMapper.class, MoneyflowIdMapper.class, BooleanToIntegerMapper.class, UserIdMapper.class})
public interface MoneyflowTransportMapper extends IMapstructMapper<Moneyflow, MoneyflowTransport> {
    @Override
    @Mapping(target = "bookingDate", source = "bookingdate")
    @Mapping(target = "invoiceDate", source = "invoicedate")
    @Mapping(target = "capitalsource.id", source = "capitalsourceid")
    @Mapping(target = "contractpartner.id", source = "contractpartnerid")
    @Mapping(target = "postingAccount.id", source = "postingaccountid")
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "group", ignore = true)
    @Mapping(target = "moneyflowSplitEntries", ignore = true)
    Moneyflow mapBToA(MoneyflowTransport moneyflowTransport);

    @Override
    @Mapping(target = "bookingdate", source = "bookingDate")
    @Mapping(target = "invoicedate", source = "invoiceDate")
    @Mapping(target = "capitalsourceid", source = "capitalsource.id")
    @Mapping(target = "capitalsourcecomment", source = "capitalsource.comment")
    @Mapping(target = "contractpartnerid", source = "contractpartner.id")
    @Mapping(target = "contractpartnername", source = "contractpartner.name")
    @Mapping(target = "postingaccountid", source = "postingAccount.id")
    @Mapping(target = "postingaccountname", source = "postingAccount.name")
    @Mapping(target = "userid", source = "user.id")
    MoneyflowTransport mapAToB(Moneyflow importedMoneyflow);
}