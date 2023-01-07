//
// Copyright (c) 2016-2018 Oliver Lehmann <lehmann@ans-netz.de>
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

import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.PostingAccount;
import org.laladev.moneyjinn.model.PostingAccountID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowID;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowSplitEntry;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowSplitEntryID;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.service.api.IMoneyflowSplitEntryService;
import org.laladev.moneyjinn.service.api.IPostingAccountService;
import org.laladev.moneyjinn.service.dao.MoneyflowSplitEntryDao;
import org.laladev.moneyjinn.service.dao.data.MoneyflowSplitEntryData;
import org.laladev.moneyjinn.service.dao.data.mapper.MoneyflowSplitEntryDataMapper;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.util.Assert;

@Named
@EnableCaching
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class MoneyflowSplitEntryService extends AbstractService
    implements IMoneyflowSplitEntryService {
  private final IPostingAccountService postingAccountService;
  private final MoneyflowSplitEntryDao moneyflowSplitEntryDao;

  @Override
  protected void addBeanMapper() {
    super.registerBeanMapper(new MoneyflowSplitEntryDataMapper());
  }

  private MoneyflowSplitEntry mapMoneyflowSplitEntryData(
      final MoneyflowSplitEntryData moneyflowSplitEntryData) {
    if (moneyflowSplitEntryData != null) {
      final MoneyflowSplitEntry moneyflowSplitEntry = super.map(moneyflowSplitEntryData,
          MoneyflowSplitEntry.class);
      PostingAccount postingAccount = moneyflowSplitEntry.getPostingAccount();
      if (postingAccount != null) {
        final PostingAccountID postingAccountId = postingAccount.getId();
        postingAccount = this.postingAccountService.getPostingAccountById(postingAccountId);
        moneyflowSplitEntry.setPostingAccount(postingAccount);
      }
      return moneyflowSplitEntry;
    }
    return null;
  }

  private List<MoneyflowSplitEntry> mapMoneyflowSplitEntryDataList(
      final List<MoneyflowSplitEntryData> moneyflowSplitEntryDataList) {
    return moneyflowSplitEntryDataList.stream().map(this::mapMoneyflowSplitEntryData)
        .collect(Collectors.toCollection(ArrayList::new));
  }

  @Override
  public ValidationResult validateMoneyflowSplitEntry(
      final MoneyflowSplitEntry moneyflowSplitEntry) {
    Assert.notNull(moneyflowSplitEntry, "moneyflowSplitEntry must not be null!");
    final ValidationResult validationResult = new ValidationResult();
    if (moneyflowSplitEntry.getComment() == null
        || moneyflowSplitEntry.getComment().trim().isEmpty()) {
      validationResult.addValidationResultItem(
          new ValidationResultItem(moneyflowSplitEntry.getId(), ErrorCode.COMMENT_IS_NOT_SET));
    }
    if (moneyflowSplitEntry.getAmount() == null
        || moneyflowSplitEntry.getAmount().compareTo(BigDecimal.ZERO) == 0) {
      validationResult.addValidationResultItem(
          new ValidationResultItem(moneyflowSplitEntry.getId(), ErrorCode.AMOUNT_IS_ZERO));
    }
    if (moneyflowSplitEntry.getPostingAccount() == null) {
      validationResult.addValidationResultItem(new ValidationResultItem(moneyflowSplitEntry.getId(),
          ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED));
    } else {
      final PostingAccount postingAccount = this.postingAccountService
          .getPostingAccountById(moneyflowSplitEntry.getPostingAccount().getId());
      if (postingAccount == null) {
        validationResult.addValidationResultItem(new ValidationResultItem(
            moneyflowSplitEntry.getId(), ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED));
      }
    }
    return validationResult;
  }

  @Override
  public List<MoneyflowSplitEntry> getMoneyflowSplitEntries(final UserID userId,
      final MoneyflowID moneyflowId) {
    List<MoneyflowSplitEntry> list = this
        .getMoneyflowSplitEntries(userId, Collections.singletonList(moneyflowId)).get(moneyflowId);
    if (list == null) {
      list = new ArrayList<>();
    }
    return list;
  }

  @Override
  public Map<MoneyflowID, List<MoneyflowSplitEntry>> getMoneyflowSplitEntries(final UserID userId,
      final List<MoneyflowID> moneyflowIds) {
    Assert.notNull(userId, "UserId must not be null!");
    Assert.notNull(moneyflowIds, "moneyflowIds must not be null!");
    final List<Long> moneyflowIdLongs = moneyflowIds.stream().map(MoneyflowID::getId)
        .collect(Collectors.toCollection(ArrayList::new));
    final List<MoneyflowSplitEntryData> moneyflowSplitEntriesData = this.moneyflowSplitEntryDao
        .getMoneyflowSplitEntries(moneyflowIdLongs);
    final List<MoneyflowSplitEntry> mapMoneyflowSplitEntries = this
        .mapMoneyflowSplitEntryDataList(moneyflowSplitEntriesData);
    final Map<MoneyflowID, List<MoneyflowSplitEntry>> moneyflowSplitEntryMap = new HashMap<>();
    for (final MoneyflowSplitEntry moneyflowSplitEntry : mapMoneyflowSplitEntries) {
      final MoneyflowID moneyflowId = moneyflowSplitEntry.getMoneyflowId();
      List<MoneyflowSplitEntry> mapList = moneyflowSplitEntryMap.get(moneyflowId);
      if (mapList == null) {
        mapList = new ArrayList<>();
      }
      mapList.add(moneyflowSplitEntry);
      moneyflowSplitEntryMap.put(moneyflowId, mapList);
    }
    return moneyflowSplitEntryMap;
  }

  @Override
  public void createMoneyflowSplitEntries(final UserID userId,
      final List<MoneyflowSplitEntry> moneyflowSplitEntries) {
    Assert.notNull(moneyflowSplitEntries, "moneyflowSplitEntries must not be null!");
    final ValidationResult validationResult = new ValidationResult();
    moneyflowSplitEntries.forEach(
        mf -> validationResult.mergeValidationResult(this.validateMoneyflowSplitEntry(mf)));
    if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
      final ValidationResultItem validationResultItem = validationResult.getValidationResultItems()
          .get(0);
      throw new BusinessException("MoneyflowsSplitEntry creation failed!",
          validationResultItem.getError());
    }
    for (final MoneyflowSplitEntry moneyflowSplitEntry : moneyflowSplitEntries) {
      final MoneyflowSplitEntryData moneyflowSplitEntryData = super.map(moneyflowSplitEntry,
          MoneyflowSplitEntryData.class);
      this.moneyflowSplitEntryDao.createMoneyflowSplitEntry(moneyflowSplitEntryData);
    }
  }

  @Override
  public void updateMoneyflowSplitEntry(final UserID userId,
      final MoneyflowSplitEntry moneyflowSplitEntry) {
    Assert.notNull(moneyflowSplitEntry, "moneyflowSplitEntry must not be null!");
    final ValidationResult validationResult = this.validateMoneyflowSplitEntry(moneyflowSplitEntry);
    if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
      final ValidationResultItem validationResultItem = validationResult.getValidationResultItems()
          .get(0);
      throw new BusinessException("MoneyflowSplitEntry update failed!",
          validationResultItem.getError());
    }
    final MoneyflowSplitEntryData moneyflowSplitEntryData = super.map(moneyflowSplitEntry,
        MoneyflowSplitEntryData.class);
    this.moneyflowSplitEntryDao.updateMoneyflowSplitEntry(moneyflowSplitEntryData);
  }

  @Override
  public void deleteMoneyflowSplitEntry(final UserID userId, final MoneyflowID moneyflowId,
      final MoneyflowSplitEntryID moneyflowSplitEntryId) {
    Assert.notNull(userId, "UserId must not be null!");
    Assert.notNull(moneyflowSplitEntryId, "moneyflowSplitEntryId must not be null!");
    this.moneyflowSplitEntryDao.deleteMoneyflowSplitEntry(moneyflowId.getId(),
        moneyflowSplitEntryId.getId());
  }

  @Override
  public void deleteMoneyflowSplitEntries(final UserID userId, final MoneyflowID moneyflowId) {
    Assert.notNull(userId, "UserId must not be null!");
    Assert.notNull(moneyflowId, "moneyflowId must not be null!");
    this.moneyflowSplitEntryDao.deleteMoneyflowSplitEntries(moneyflowId.getId());
  }
}
