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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.laladev.moneyjinn.service.dao.data.MoneyflowData;
import org.laladev.moneyjinn.service.dao.data.MoneyflowSearchParamsData;
import org.laladev.moneyjinn.service.dao.data.PostingAccountAmountData;
import org.laladev.moneyjinn.service.dao.mapper.IMoneyflowDaoMapper;

@Named
public class MoneyflowDao {
  @Inject
  private IMoneyflowDaoMapper mapper;

  public Long createMoneyflow(final MoneyflowData moneyflowData) {
    this.mapper.createMoneyflow(moneyflowData);
    return moneyflowData.getId();
  }

  public MoneyflowData getMoneyflowById(final Long userId, final Long id) {
    return this.mapper.getMoneyflowById(userId, id);
  }

  public void updateMoneyflow(final MoneyflowData moneyflowData) {
    this.mapper.updateMoneyflow(moneyflowData);
  }

  public void deleteMoneyflow(final Long groupId, final Long id) {
    this.mapper.deleteMoneyflow(groupId, id);
  }

  public List<Short> getAllYears(final Long userId) {
    return this.mapper.getAllYears(userId);
  }

  public List<Short> getAllMonth(final Long userId, final LocalDate beginOfYear,
      final LocalDate endOfYear) {
    return this.mapper.getAllMonth(userId, beginOfYear, endOfYear);
  }

  public BigDecimal getSumAmountByDateRangeForCapitalsourceIds(final Long userId,
      final LocalDate validFrom, final LocalDate validTil, final List<Long> capitalsourceIds) {
    return this.mapper.getSumAmountByDateRangeForCapitalsourceIds(userId, validFrom, validTil,
        capitalsourceIds);
  }

  public List<MoneyflowData> getAllMoneyflowsByDateRange(final Long userId,
      final LocalDate dateFrom, final LocalDate dateTil) {
    return this.mapper.getAllMoneyflowsByDateRange(userId, dateFrom, dateTil);
  }

  public List<MoneyflowData> getAllMoneyflowsByDateRangeIncludingPrivate(final Long userId,
      final LocalDate dateFrom, final LocalDate dateTil) {
    return this.mapper.getAllMoneyflowsByDateRangeIncludingPrivate(userId, dateFrom, dateTil);
  }

  public boolean monthHasMoneyflows(final Long userId, final LocalDate dateFrom,
      final LocalDate dateTil) {
    final Boolean result = this.mapper.monthHasMoneyflows(userId, dateFrom, dateTil);
    if (result == null) {
      return false;
    }
    return result;
  }

  public LocalDate getMaxMoneyflowDate(final Long userId) {
    return this.mapper.getMaxMoneyflowDate(userId);
  }

  public LocalDate getPreviousMoneyflowDate(final Long userId, final LocalDate date) {
    return this.mapper.getPreviousMoneyflowDate(userId, date);
  }

  public LocalDate getNextMoneyflowDate(final Long userId, final LocalDate date) {
    return this.mapper.getNextMoneyflowDate(userId, date);
  }

  public List<PostingAccountAmountData> getAllMoneyflowsByDateRangeGroupedByYearMonthPostingAccount(
      final Long userId, final List<Long> postingAccountIdLongs, final LocalDate dateFrom,
      final LocalDate dateTil) {
    return this.mapper.getAllMoneyflowsByDateRangeGroupedByYearMonthPostingAccount(userId,
        postingAccountIdLongs, dateFrom, dateTil);
  }

  public List<PostingAccountAmountData> getAllMoneyflowsByDateRangeGroupedByYearPostingAccount(
      final Long userId, final List<Long> postingAccountIdLongs, final LocalDate dateFrom,
      final LocalDate dateTil) {
    return this.mapper.getAllMoneyflowsByDateRangeGroupedByYearPostingAccount(userId,
        postingAccountIdLongs, dateFrom, dateTil);
  }

  public List<MoneyflowData> searchMoneyflowsByAmountDate(final Long userId,
      final LocalDate dateFrom, final LocalDate dateTil, final BigDecimal amount) {
    return this.mapper.searchMoneyflowsByAmountDate(userId, dateFrom, dateTil, amount);
  }

  public List<MoneyflowData> searchMoneyflowsByAbsoluteAmountDate(final Long userId,
      final LocalDate dateFrom, final LocalDate dateTil, final BigDecimal amount) {
    return this.mapper.searchMoneyflowsByAbsoluteAmountDate(userId, dateFrom, dateTil, amount);
  }

  public List<MoneyflowData> getAllMoneyflowsByDateRangeCapitalsourceId(final Long userId,
      final LocalDate dateFrom, final LocalDate dateTil, final Long capitalsourceId) {
    return this.mapper.getAllMoneyflowsByDateRangeCapitalsourceId(userId, dateFrom, dateTil,
        capitalsourceId);
  }

  public List<MoneyflowData> searchMoneyflows(final Long userId,
      final MoneyflowSearchParamsData moneyflowSearchParamsData) {
    if (moneyflowSearchParamsData.getSearchString() != null
        && !moneyflowSearchParamsData.isFeatureEqual()
        && !moneyflowSearchParamsData.isFeatureRegexp()) {
      // LIKE-mode
      moneyflowSearchParamsData
          .setSearchString('%' + moneyflowSearchParamsData.getSearchString() + '%');
    }
    moneyflowSearchParamsData.setUserId(userId);
    return this.mapper.searchMoneyflows(moneyflowSearchParamsData);
  }
}
