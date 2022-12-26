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

package org.laladev.moneyjinn.service.dao;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;
import org.laladev.moneyjinn.service.dao.data.BankAccountData;
import org.laladev.moneyjinn.service.dao.data.ContractpartnerAccountData;
import org.laladev.moneyjinn.service.dao.mapper.IContractpartnerAccountDaoMapper;

@Named
public class ContractpartnerAccountDao {
  @Inject
  private IContractpartnerAccountDaoMapper mapper;

  public ContractpartnerAccountData getContractpartnerAccountByBankAccount(final Long userId,
      final String bankCode, final String accountNumber) {
    return this.mapper.getContractpartnerAccountByBankAccount(bankCode, accountNumber);
  }

  public ContractpartnerAccountData getContractpartnerAccountById(final Long userId,
      final Long contractpartnerAccountId) {
    return this.mapper.getContractpartnerAccountById(contractpartnerAccountId);
  }

  public List<ContractpartnerAccountData> getContractpartnerAccounts(final Long userId,
      final Long contractpartnerId) {
    return this.mapper.getContractpartnerAccounts(contractpartnerId);
  }

  public Long createContractpartnerAccount(
      final ContractpartnerAccountData contractpartnerAccountData) {
    this.mapper.createContractpartnerAccount(contractpartnerAccountData);
    return contractpartnerAccountData.getId();
  }

  public void updateContractpartnerAccount(
      final ContractpartnerAccountData contractpartnerAccountData) {
    this.mapper.updateContractpartnerAccount(contractpartnerAccountData);
  }

  public void deleteContractpartnerAccount(final Long userId, final Long contractpartnerAccountId) {
    this.mapper.deleteContractpartnerAccount(contractpartnerAccountId);
  }

  public void deleteContractpartnerAccounts(final Long userId, final Long contractpartnerId) {
    this.mapper.deleteContractpartnerAccounts(contractpartnerId);
  }

  public List<ContractpartnerAccountData> getAllContractpartnerByAccounts(final Long userId,
      final List<BankAccountData> bankAccountDatas) {
    return this.mapper.getAllContractpartnerByAccounts(bankAccountDatas);
  }
}
