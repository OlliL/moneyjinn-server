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
import org.laladev.moneyjinn.converter.ContractpartnerIdMapper;
import org.laladev.moneyjinn.converter.ImportedMoneyflowIdMapper;
import org.laladev.moneyjinn.converter.PostingAccountIdMapper;
import org.laladev.moneyjinn.converter.UserIdMapper;
import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.laladev.moneyjinn.converter.javatypes.BooleanToShortMapper;
import org.laladev.moneyjinn.core.mapper.IMapper;
import org.laladev.moneyjinn.core.rest.model.transport.ImportedMoneyflowTransport;
import org.laladev.moneyjinn.model.BankAccount;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflow;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapStructConfig.class, uses = { CapitalsourceIdMapper.class,
    ContractpartnerIdMapper.class, PostingAccountIdMapper.class, ImportedMoneyflowIdMapper.class,
    BooleanToShortMapper.class, UserIdMapper.class })
public interface ImportedMoneyflowTransportMapper
    extends IMapper<ImportedMoneyflow, ImportedMoneyflowTransport> {
  @Override
  @Mapping(target = "bookingDate", source = "bookingdate")
  @Mapping(target = "invoiceDate", source = "invoicedate")
  @Mapping(target = "capitalsource.id", source = "capitalsourceid")
  @Mapping(target = "contractpartner.id", source = "contractpartnerid")
  @Mapping(target = "postingAccount.id", source = "postingaccountid")
  @Mapping(target = "externalId", source = "externalid")
  @Mapping(target = "bankAccount.accountNumber", source = "accountNumber")
  @Mapping(target = "bankAccount.bankCode", source = "bankCode")
  @Mapping(target = "user", ignore = true)
  @Mapping(target = "group", ignore = true)
  @Mapping(target = "status", ignore = true)
  ImportedMoneyflow mapBToA(ImportedMoneyflowTransport ImportedMoneyflowTransport);

  @Override
  @Mapping(target = "accountNumber", source = "bankAccount.accountNumber", defaultValue = "")
  @Mapping(target = "bankCode", source = "bankAccount.bankCode", defaultValue = "")
  @Mapping(target = "bookingdate", source = "bookingDate")
  @Mapping(target = "invoicedate", source = "invoiceDate")
  @Mapping(target = "capitalsourceid", source = "capitalsource.id")
  @Mapping(target = "capitalsourcecomment", source = "capitalsource.comment")
  @Mapping(target = "contractpartnerid", source = "contractpartner.id")
  @Mapping(target = "contractpartnername", source = "contractpartner.name")
  @Mapping(target = "postingaccountid", source = "postingAccount.id")
  @Mapping(target = "postingaccountname", source = "postingAccount.name")
  @Mapping(target = "userid", source = "user.id")
  @Mapping(target = "accountNumberCapitalsource", ignore = true)
  @Mapping(target = "bankCodeCapitalsource", ignore = true)
  @Mapping(target = "externalid", source = "externalId")
  ImportedMoneyflowTransport mapAToB(ImportedMoneyflow importedMoneyflow);

  // work around https://github.com/mapstruct/mapstruct/issues/1166
  @AfterMapping
  default ImportedMoneyflow doAfterMapping(@MappingTarget final ImportedMoneyflow entity) {
    if (entity != null) {
      final BankAccount bankAccount = entity.getBankAccount();
      if (bankAccount != null && (bankAccount.getAccountNumber() == null
          || bankAccount.getAccountNumber().trim().isEmpty()
              && (entity.getBankAccount().getBankCode() == null
                  || bankAccount.getBankCode().trim().isEmpty()))) {
        entity.setBankAccount(null);
      }
      if (entity.getCapitalsource() != null && entity.getCapitalsource().getId() == null) {
        entity.setCapitalsource(null);
      }
      if (entity.getContractpartner() != null && entity.getContractpartner().getId() == null) {
        entity.setContractpartner(null);
      }
      if (entity.getPostingAccount() != null && entity.getPostingAccount().getId() == null) {
        entity.setPostingAccount(null);
      }
    }
    return entity;
  }
}