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

import org.laladev.moneyjinn.converter.CapitalsourceIdMapper;
import org.laladev.moneyjinn.converter.ImportedMonthlySettlementIdMapper;
import org.laladev.moneyjinn.converter.UserIdMapper;
import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.laladev.moneyjinn.converter.javatypes.MonthToIntegerMapper;
import org.laladev.moneyjinn.core.mapper.IMapper;
import org.laladev.moneyjinn.model.monthlysettlement.ImportedMonthlySettlement;
import org.laladev.moneyjinn.server.model.ImportedMonthlySettlementTransport;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapStructConfig.class, uses = { ImportedMonthlySettlementIdMapper.class,
    MonthToIntegerMapper.class, CapitalsourceIdMapper.class, UserIdMapper.class })
public interface ImportedMonthlySettlementTransportMapper
    extends IMapper<ImportedMonthlySettlement, ImportedMonthlySettlementTransport> {
  @Override
  @Mapping(target = "capitalsource.id", source = "capitalsourceid")
  @Mapping(target = "externalId", source = "externalid")
  @Mapping(target = "user", ignore = true)
  @Mapping(target = "group", ignore = true)
  ImportedMonthlySettlement mapBToA(
      ImportedMonthlySettlementTransport importedMonthlySettlementTransport);

  @Override
  @Mapping(target = "accountNumberCapitalsource", source = "capitalsource.bankAccount.accountNumber")
  @Mapping(target = "bankCodeCapitalsource", source = "capitalsource.bankAccount.bankCode")
  @Mapping(target = "capitalsourceid", source = "capitalsource.id")
  @Mapping(target = "capitalsourcecomment", source = "capitalsource.comment")
  @Mapping(target = "capitalsourcegroupuse", ignore = true)
  @Mapping(target = "capitalsourcetype", ignore = true)
  @Mapping(target = "externalid", source = "externalId")
  @Mapping(target = "userid", source = "user.id")
  ImportedMonthlySettlementTransport mapAToB(ImportedMonthlySettlement importedMonthlySettlement);

  // work around https://github.com/mapstruct/mapstruct/issues/1166
  @AfterMapping
  default ImportedMonthlySettlement doAfterMapping(
      @MappingTarget final ImportedMonthlySettlement entity) {
    if (entity != null && entity.getCapitalsource() != null
        && entity.getCapitalsource().getId() == null) {
      entity.setCapitalsource(null);
    }
    return entity;
  }
}
