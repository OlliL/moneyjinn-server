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

package org.laladev.moneyjinn.service.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.laladev.moneyjinn.service.dao.data.MoneyflowData;
import org.laladev.moneyjinn.service.dao.data.MoneyflowSearchParamsData;
import org.laladev.moneyjinn.service.dao.data.PostingAccountAmountData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IMoneyflowDaoMapper {
    void createMoneyflow(MoneyflowData moneyflowData);

    MoneyflowData getMoneyflowById(@Param("userId") Long userId, @Param("id") Long id);

    void updateMoneyflow(MoneyflowData moneyflowData);

    void deleteMoneyflow(@Param("userId") Long userId, @Param("id") Long id);

    BigDecimal getSumAmountByDateRangeForCapitalsourceIds(@Param("userId") Long userId,
                                                          @Param("validFrom") LocalDate validFrom, @Param("validTil") LocalDate validTil,
                                                          @Param("mcsCapitalsourceIds") List<Long> capitalsourceIds);

    List<Integer> getAllYears(Long userId);

    List<Integer> getAllMonth(@Param("userId") Long userId, @Param("beginOfYear") LocalDate beginOfYear,
                              @Param("endOfYear") LocalDate endOfYear);

    List<MoneyflowData> getAllMoneyflowsByDateRangeIncludingPrivate(@Param("userId") Long userId,
                                                                    @Param("dateFrom") LocalDate dateFrom, @Param("dateTil") LocalDate dateTil);

    Boolean monthHasMoneyflows(@Param("userId") Long userId, @Param("dateFrom") LocalDate dateFrom,
                               @Param("dateTil") LocalDate dateTil);

    LocalDate getMinMoneyflowDate(Long userId);

    LocalDate getMaxMoneyflowDate(Long userId);

    LocalDate getPreviousMoneyflowDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    LocalDate getNextMoneyflowDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    List<PostingAccountAmountData> getAllMoneyflowsByDateRangeGroupedByYearMonthPostingAccount(
            @Param("userId") Long userId, @Param("postingAccountIds") List<Long> postingAccountIdLongs,
            @Param("dateFrom") LocalDate dateFrom, @Param("dateTil") LocalDate dateTil);

    List<PostingAccountAmountData> getAllMoneyflowsByDateRangeGroupedByYearPostingAccount(
            @Param("userId") Long userId, @Param("postingAccountIds") List<Long> postingAccountIdLongs,
            @Param("dateFrom") LocalDate dateFrom, @Param("dateTil") LocalDate dateTil);

    List<MoneyflowData> searchMoneyflowsByAmountDate(@Param("userId") Long userId,
                                                     @Param("dateFrom") LocalDate dateFrom, @Param("dateTil") LocalDate dateTil,
                                                     @Param("amount") BigDecimal amount);

    List<MoneyflowData> searchMoneyflowsByAbsoluteAmountDate(@Param("userId") Long userId,
                                                             @Param("dateFrom") LocalDate dateFrom, @Param("dateTil") LocalDate dateTil,
                                                             @Param("amount") BigDecimal amount);

    List<MoneyflowData> getAllMoneyflowsByDateRangeCapitalsourceId(@Param("userId") Long userId,
                                                                   @Param("dateFrom") LocalDate dateFrom, @Param("dateTil") LocalDate dateTil,
                                                                   @Param("capitalsourceId") Long capitalsourceId);

    List<MoneyflowData> searchMoneyflows(MoneyflowSearchParamsData moneyflowSearchParamsData);
}
