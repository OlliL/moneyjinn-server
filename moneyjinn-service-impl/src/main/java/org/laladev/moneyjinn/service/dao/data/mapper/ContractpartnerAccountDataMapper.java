//
// Copyright (c) 2015 Oliver Lehmann <lehmann@ans-netz.de>
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

import org.laladev.moneyjinn.core.mapper.IMapper;
import org.laladev.moneyjinn.model.BankAccount;
import org.laladev.moneyjinn.model.Contractpartner;
import org.laladev.moneyjinn.model.ContractpartnerAccount;
import org.laladev.moneyjinn.model.ContractpartnerAccountID;
import org.laladev.moneyjinn.model.ContractpartnerID;
import org.laladev.moneyjinn.service.dao.data.ContractpartnerAccountData;

public class ContractpartnerAccountDataMapper
    implements IMapper<ContractpartnerAccount, ContractpartnerAccountData> {
  @Override
  public ContractpartnerAccount mapBToA(
      final ContractpartnerAccountData contractpartnerAccountData) {
    final BankAccount bankAccount = new BankAccount(contractpartnerAccountData.getAccountNumber(),
        contractpartnerAccountData.getBankCode());
    final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(
        contractpartnerAccountData.getId());
    final Contractpartner contractpartner = new Contractpartner(
        new ContractpartnerID(contractpartnerAccountData.getMcpContractpartnerId()));
    return new ContractpartnerAccount(contractpartnerAccountId, contractpartner, bankAccount);
  }

  @Override
  public ContractpartnerAccountData mapAToB(final ContractpartnerAccount contractpartnerAccount) {
    final ContractpartnerAccountData contractpartnerAccountData = new ContractpartnerAccountData();
    if (contractpartnerAccount.getId() != null) {
      contractpartnerAccountData.setId(contractpartnerAccount.getId().getId());
    }
    if (contractpartnerAccount.getBankAccount() != null) {
      contractpartnerAccountData
          .setAccountNumber(contractpartnerAccount.getBankAccount().getAccountNumber());
      contractpartnerAccountData.setBankCode(contractpartnerAccount.getBankAccount().getBankCode());
    }
    if (contractpartnerAccount.getContractpartner() != null
        && contractpartnerAccount.getContractpartner().getId() != null) {
      contractpartnerAccountData
          .setMcpContractpartnerId(contractpartnerAccount.getContractpartner().getId().getId());
    }
    return contractpartnerAccountData;
  }
}
