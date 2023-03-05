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

package org.laladev.moneyjinn.service.impl;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.BankAccount;
import org.laladev.moneyjinn.model.Contractpartner;
import org.laladev.moneyjinn.model.ContractpartnerAccount;
import org.laladev.moneyjinn.model.ContractpartnerAccountID;
import org.laladev.moneyjinn.model.ContractpartnerID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.service.CacheNames;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.IContractpartnerAccountService;
import org.laladev.moneyjinn.service.api.IContractpartnerService;
import org.laladev.moneyjinn.service.dao.ContractpartnerAccountDao;
import org.laladev.moneyjinn.service.dao.data.BankAccountData;
import org.laladev.moneyjinn.service.dao.data.ContractpartnerAccountData;
import org.laladev.moneyjinn.service.dao.data.mapper.BankAccountDataMapper;
import org.laladev.moneyjinn.service.dao.data.mapper.ContractpartnerAccountDataMapper;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.util.Assert;

@Named
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ContractpartnerAccountService extends AbstractService
    implements IContractpartnerAccountService {
  private static final String USER_ID_MUST_NOT_BE_NULL = "UserId must not be null!";
  private final IContractpartnerService contractpartnerService;
  private final ContractpartnerAccountDao contractpartnerAccountDao;
  private final IAccessRelationService accessRelationService;
  private final BankAccountDataMapper bankAccountDataMapper;
  private final ContractpartnerAccountDataMapper contractpartnerAccountDataMapper;

  @Override
  @PostConstruct
  protected void addBeanMapper() {
    super.registerBeanMapper(this.contractpartnerAccountDataMapper);
    super.registerBeanMapper(this.bankAccountDataMapper);
  }

  @Override
  public ValidationResult validateContractpartnerAccount(final UserID userId,
      final ContractpartnerAccount contractpartnerAccount) {
    final ValidationResult validationResult = new ValidationResult();
    if (contractpartnerAccount.getBankAccount() == null) {
      validationResult.addValidationResultItem(new ValidationResultItem(
          contractpartnerAccount.getId(), ErrorCode.BANK_CODE_CONTAINS_ILLEGAL_CHARS_OR_IS_EMPTY));
      validationResult
          .addValidationResultItem(new ValidationResultItem(contractpartnerAccount.getId(),
              ErrorCode.ACCOUNT_NUMBER_CONTAINS_ILLEGAL_CHARS_OR_IS_EMPTY));
    } else {
      for (final ErrorCode errorCode : contractpartnerAccount.getBankAccount().checkValidity()) {
        validationResult.addValidationResultItem(
            new ValidationResultItem(contractpartnerAccount.getId(), errorCode));
      }
      final ContractpartnerAccount contractpartnerAccountChecked = this
          .getContractpartnerAccountByBankAccount(userId, contractpartnerAccount.getBankAccount());
      if (contractpartnerAccountChecked != null && (contractpartnerAccount.getId() == null
          || !contractpartnerAccountChecked.getId().equals(contractpartnerAccount.getId()))) {
        validationResult
            .addValidationResultItem(new ValidationResultItem(contractpartnerAccount.getId(),
                ErrorCode.ACCOUNT_ALREADY_ASSIGNED_TO_OTHER_PARTNER, Collections
                    .singletonList(contractpartnerAccountChecked.getContractpartner().getName())));
      }
    }
    if (contractpartnerAccount.getContractpartner() == null) {
      validationResult.addValidationResultItem(new ValidationResultItem(
          contractpartnerAccount.getId(), ErrorCode.CONTRACTPARTNER_IS_NOT_SET));
    } else {
      final Contractpartner contractpartner = this.contractpartnerService
          .getContractpartnerById(userId, contractpartnerAccount.getContractpartner().getId());
      if (contractpartner == null) {
        validationResult.addValidationResultItem(new ValidationResultItem(
            contractpartnerAccount.getId(), ErrorCode.CONTRACTPARTNER_DOES_NOT_EXIST));
      }
    }
    return validationResult;
  }

  private ContractpartnerAccount mapContractpartnerAccountData(final UserID userId,
      final ContractpartnerAccountData contractpartnerAccountData) {
    if (contractpartnerAccountData != null) {
      final ContractpartnerAccount contractpartnerAccount = super.map(contractpartnerAccountData,
          ContractpartnerAccount.class);
      final Contractpartner contractpartner = this.contractpartnerService
          .getContractpartnerById(userId, contractpartnerAccount.getContractpartner().getId());
      // this secures the Account - a user which has no access to the partner may not
      // modify
      // its accounts
      if (contractpartner != null) {
        contractpartnerAccount.setContractpartner(contractpartner);
        return contractpartnerAccount;
      }
    }
    return null;
  }

  private List<ContractpartnerAccount> mapContractpartnerAccountDataList(final UserID userId,
      final List<ContractpartnerAccountData> contractpartnerAccountDataList) {
    return contractpartnerAccountDataList.stream()
        .map(element -> this.mapContractpartnerAccountData(userId, element))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  private ContractpartnerAccount getContractpartnerAccountByBankAccount(final UserID userId,
      final BankAccount bankAccount) {
    Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
    Assert.notNull(bankAccount, "BankAccount must not be null!");
    final ContractpartnerAccountData contractpartnerAccountData = this.contractpartnerAccountDao
        .getContractpartnerAccountByBankAccount(bankAccount.getBankCode(),
            bankAccount.getAccountNumber());
    return this.mapContractpartnerAccountData(userId, contractpartnerAccountData);
  }

  @Override
  @Cacheable(CacheNames.CONTRACTPARTNER_ACCOUNT_BY_ID)
  public ContractpartnerAccount getContractpartnerAccountById(final UserID userId,
      final ContractpartnerAccountID contractpartnerAccountId) {
    Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
    Assert.notNull(contractpartnerAccountId, "ContractpartnerAccountId must not be null!");
    final ContractpartnerAccountData contractpartnerAccountData = this.contractpartnerAccountDao
        .getContractpartnerAccountById(contractpartnerAccountId.getId());
    return this.mapContractpartnerAccountData(userId, contractpartnerAccountData);
  }

  @Override
  @Cacheable(CacheNames.CONTRACTPARTNER_ACCOUNTS_BY_PARTNER)
  public List<ContractpartnerAccount> getContractpartnerAccounts(final UserID userId,
      final ContractpartnerID contractpartnerId) {
    Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
    Assert.notNull(contractpartnerId, "ContractpartnerId must not be null!");
    final List<ContractpartnerAccountData> contractpartnerAccountData = this.contractpartnerAccountDao
        .getContractpartnerAccounts(contractpartnerId.getId());
    return this.mapContractpartnerAccountDataList(userId, contractpartnerAccountData);
  }

  @Override
  public ContractpartnerAccountID createContractpartnerAccount(final UserID userId,
      final ContractpartnerAccount contractpartnerAccount) {
    Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
    Assert.notNull(contractpartnerAccount, "ContractpartnerAccount must not be null!");
    contractpartnerAccount.setId(null);
    final ValidationResult validationResult = this.validateContractpartnerAccount(userId,
        contractpartnerAccount);
    if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
      final ValidationResultItem validationResultItem = validationResult.getValidationResultItems()
          .get(0);
      throw new BusinessException("ContractpartnerAccount creation failed!",
          validationResultItem.getError());
    }
    final ContractpartnerAccountData contractpartnerAccountData = super.map(contractpartnerAccount,
        ContractpartnerAccountData.class);
    final Long contractpartnerAccountId = this.contractpartnerAccountDao
        .createContractpartnerAccount(contractpartnerAccountData);
    this.evictContractpartnerAccountCache(userId,
        new ContractpartnerAccountID(contractpartnerAccountId),
        contractpartnerAccount.getContractpartner().getId());
    return new ContractpartnerAccountID(contractpartnerAccountId);
  }

  @Override
  public void updateContractpartnerAccount(final UserID userId,
      final ContractpartnerAccount contractpartnerAccount) {
    Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
    Assert.notNull(contractpartnerAccount, "ContractpartnerAccount must not be null!");
    final ValidationResult validationResult = this.validateContractpartnerAccount(userId,
        contractpartnerAccount);
    if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
      final ValidationResultItem validationResultItem = validationResult.getValidationResultItems()
          .get(0);
      throw new BusinessException("ContractpartnerAccount update failed!",
          validationResultItem.getError());
    }
    final ContractpartnerAccount contractpartnerAccountOld = this
        .getContractpartnerAccountById(userId, contractpartnerAccount.getId());
    if (contractpartnerAccountOld != null) {
      final ContractpartnerAccountData contractpartnerAccountData = super.map(
          contractpartnerAccount, ContractpartnerAccountData.class);
      this.contractpartnerAccountDao.updateContractpartnerAccount(contractpartnerAccountData);
      this.evictContractpartnerAccountCache(userId, contractpartnerAccount.getId(),
          contractpartnerAccount.getContractpartner().getId());
      if (!contractpartnerAccountOld.getContractpartner().getId()
          .equals(contractpartnerAccount.getContractpartner().getId())) {
        this.evictContractpartnerAccountCache(userId, contractpartnerAccount.getId(),
            contractpartnerAccountOld.getContractpartner().getId());
      }
    }
  }

  @Override
  public void deleteContractpartnerAccountById(final UserID userId,
      final ContractpartnerAccountID contractpartnerAccountId) {
    Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
    Assert.notNull(contractpartnerAccountId, "ContractpartnerAccountId must not be null!");
    final ContractpartnerAccount contractpartnerAccount = this.getContractpartnerAccountById(userId,
        contractpartnerAccountId);
    if (contractpartnerAccount != null) {
      this.contractpartnerAccountDao.deleteContractpartnerAccount(contractpartnerAccountId.getId());
      this.evictContractpartnerAccountCache(userId, contractpartnerAccountId,
          contractpartnerAccount.getContractpartner().getId());
    }
  }

  @Override
  public void deleteContractpartnerAccounts(final UserID userId,
      final ContractpartnerID contractpartnerId) {
    Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
    Assert.notNull(contractpartnerId, "ContractpartnerId must not be null!");
    final List<ContractpartnerAccount> contractpartnerAccounts = this
        .getContractpartnerAccounts(userId, contractpartnerId);
    if (contractpartnerAccounts != null && !contractpartnerAccounts.isEmpty()) {
      this.contractpartnerAccountDao.deleteContractpartnerAccounts(contractpartnerId.getId());
      contractpartnerAccounts.forEach(ca -> this.evictContractpartnerAccountCache(userId,
          ca.getId(), ca.getContractpartner().getId()));
    }
  }

  @Override
  public List<ContractpartnerAccount> getAllContractpartnerByAccounts(final UserID userId,
      final List<BankAccount> bankAccounts) {
    Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
    Assert.notNull(bankAccounts, "BankAccounts must not be null!");
    final List<BankAccountData> bankAccountDatas = super.mapList(bankAccounts,
        BankAccountData.class);
    final List<ContractpartnerAccountData> contractpartnerAccountData = this.contractpartnerAccountDao
        .getAllContractpartnerByAccounts(bankAccountDatas);
    return this.mapContractpartnerAccountDataList(userId, contractpartnerAccountData);
  }

  private void evictContractpartnerAccountCache(final UserID userId,
      final ContractpartnerAccountID contractpartnerAccountIDd,
      final ContractpartnerID contractpartnerId) {
    if (contractpartnerAccountIDd != null) {
      final Cache contractpartnerAccountsByPartnerCache = super.getCache(
          CacheNames.CONTRACTPARTNER_ACCOUNTS_BY_PARTNER);
      final Cache contractpartnerAccountByIdCache = super.getCache(
          CacheNames.CONTRACTPARTNER_ACCOUNT_BY_ID);
      final Set<UserID> userIds = this.accessRelationService.getAllUserWithSameGroup(userId);
      for (final UserID evictingUserId : userIds) {
        if (contractpartnerAccountsByPartnerCache != null) {
          contractpartnerAccountsByPartnerCache
              .evict(new SimpleKey(evictingUserId, contractpartnerId));
        }
        if (contractpartnerAccountByIdCache != null) {
          contractpartnerAccountByIdCache
              .evict(new SimpleKey(evictingUserId, contractpartnerAccountIDd));
        }
      }
    }
  }
}
