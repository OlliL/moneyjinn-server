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

import org.laladev.moneyjinn.converter.ContractpartnerAccountIdMapper;
import org.laladev.moneyjinn.core.mapper.IMapper;
import org.laladev.moneyjinn.model.ContractpartnerAccount;
import org.laladev.moneyjinn.service.dao.data.ContractpartnerAccountData;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR, uses = ContractpartnerAccountIdMapper.class)
public interface ContractpartnerAccountDataMapper
    extends IMapper<ContractpartnerAccount, ContractpartnerAccountData> {

  @Override
  @Mapping(target = "contractpartner.id.id", source = "mcpContractpartnerId")
  @Mapping(target = "bankAccount.accountNumber", source = "accountNumber")
  @Mapping(target = "bankAccount.bankCode", source = "bankCode")
  ContractpartnerAccount mapBToA(final ContractpartnerAccountData b);

  @Override
  @Mapping(target = "mcpContractpartnerId", source = "contractpartner.id.id")
  @Mapping(target = ".", source = "bankAccount")
  ContractpartnerAccountData mapAToB(final ContractpartnerAccount a);

  // work around https://github.com/mapstruct/mapstruct/issues/1166
  @AfterMapping
  default ContractpartnerAccount doAfterMapping(
      @MappingTarget final ContractpartnerAccount entity) {
    if (entity != null && entity.getBankAccount() != null
        && entity.getBankAccount().getAccountNumber() == null
        && entity.getBankAccount().getBankCode() == null) {
      entity.setBankAccount(null);
    }
    return entity;
  }
}
