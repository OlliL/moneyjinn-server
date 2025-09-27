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

package org.laladev.moneyjinn.service.impl;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.monthlysettlement.MonthlySettlement;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.IGroupService;
import org.laladev.moneyjinn.service.api.IMonthlySettlementService;
import org.laladev.moneyjinn.service.api.IUserService;
import org.laladev.moneyjinn.service.dao.MonthlySettlementDao;
import org.laladev.moneyjinn.service.dao.data.MonthlySettlementData;
import org.laladev.moneyjinn.service.dao.data.mapper.MonthlySettlementDataMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static org.springframework.util.Assert.notEmpty;
import static org.springframework.util.Assert.notNull;

@Named
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class MonthlySettlementService extends AbstractService implements IMonthlySettlementService {
    private final MonthlySettlementDao monthlySettlementDao;
    private final IUserService userService;
    private final IGroupService groupService;
    private final ICapitalsourceService capitalsourceService;
    private final MonthlySettlementDataMapper monthlySettlementDataMapper;

    private ValidationResult validateMonthlySettlement(@NonNull final MonthlySettlement monthlySettlement) {
        notNull(monthlySettlement.getCapitalsource(), "monthlySettlement.capitalsource must not be null!");
        notNull(monthlySettlement.getCapitalsource().getId(), "monthlySettlement.capitalsource.id must not be null!");
        notNull(monthlySettlement.getUser(), "monthlySettlement.user must not be null!");
        notNull(monthlySettlement.getUser().getId(), "monthlySettlement.user.id must not be null!");
        notNull(monthlySettlement.getGroup(), "monthlySettlement.group must not be null!");
        notNull(monthlySettlement.getGroup().getId(), "monthlySettlement.group.id must not be null!");

        final ValidationResult validationResult = new ValidationResult();
        final Consumer<ErrorCode> addResult = (final ErrorCode errorCode) -> validationResult.addValidationResultItem(
                new ValidationResultItem(monthlySettlement.getId(), errorCode));

        final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(
                monthlySettlement.getUser().getId(), monthlySettlement.getGroup().getId(),
                monthlySettlement.getCapitalsource().getId());
        if (capitalsource == null || !capitalsource.getUser().getId().equals(monthlySettlement.getUser().getId())) {
            addResult.accept(ErrorCode.CAPITALSOURCE_DOES_NOT_EXIST);
        }

        final BigDecimal amount = monthlySettlement.getAmount();
        if (amount == null) {
            addResult.accept(ErrorCode.AMOUNT_HAS_TO_BE_SPECIFIED);
        } else if ((amount.signum() == 0 ? 1 : amount.precision() - amount.scale()) > 6) {
            addResult.accept(ErrorCode.AMOUNT_TO_BIG);
        }

        return validationResult;
    }

    private MonthlySettlement mapMonthlySettlementData(final MonthlySettlementData monthlySettlementData) {
        if (monthlySettlementData != null) {
            final MonthlySettlement monthlySettlement = this.monthlySettlementDataMapper.mapBToA(monthlySettlementData);

            this.userService.enrichEntity(monthlySettlement);
            this.groupService.enrichEntity(monthlySettlement);
            this.capitalsourceService.enrichEntity(monthlySettlement);

            return monthlySettlement;
        }
        return null;
    }

    private List<MonthlySettlement> mapMonthlySettlementDataList(
            final List<MonthlySettlementData> monthlySettlementDataList) {
        return monthlySettlementDataList.stream().map(this::mapMonthlySettlementData).toList();
    }

    @Override
    public List<Integer> getAllYears(@NonNull final UserID userId) {
        return this.monthlySettlementDao.getAllYears(userId.getId());
    }

    @Override
    public List<Month> getAllMonth(@NonNull final UserID userId, @NonNull final Integer year) {
        final List<Integer> allMonths = this.monthlySettlementDao.getAllMonth(userId.getId(), year);
        if (allMonths != null) {
            return allMonths.stream().map(m -> Month.of(m.intValue())).toList();
        }
        return Collections.emptyList();
    }

    @Override
    public List<MonthlySettlement> getAllMonthlySettlementsByYearMonth(@NonNull final UserID userId,
                                                                       @NonNull final Integer year, @NonNull final Month month) {
        final List<MonthlySettlementData> monthlySettlementDatas = this.monthlySettlementDao
                .getAllMonthlySettlementsByYearMonth(userId.getId(), year, month.getValue());
        return this.mapMonthlySettlementDataList(monthlySettlementDatas);
    }

    @Override
    public LocalDate getMaxSettlementDate(@NonNull final UserID userId) {
        return this.monthlySettlementDao.getMaxSettlementDate(userId.getId());
    }

    @Override
    public LocalDate getMinSettlementDate(@NonNull final UserID userId) {
        return this.monthlySettlementDao.getMinSettlementDate(userId.getId());
    }

    @Override
    public boolean checkMonthlySettlementsExists(@NonNull final UserID userId, @NonNull final Integer year,
                                                 @NonNull final Month month) {
        return this.monthlySettlementDao.checkMonthlySettlementsExists(userId.getId(), year, month.getValue());
    }

    @Override
    public ValidationResult upsertMonthlySettlements(@NonNull final List<MonthlySettlement> monthlySettlements) {
        final ValidationResult validationResult = new ValidationResult();
        monthlySettlements.forEach(ms -> validationResult.mergeValidationResult(this.validateMonthlySettlement(ms)));
        if (validationResult.isValid()) {
            final List<MonthlySettlementData> monthlySettlementDatas = this.monthlySettlementDataMapper
                    .mapAToB(monthlySettlements);
            monthlySettlementDatas.forEach(this.monthlySettlementDao::upsertMonthlySettlement);
        }

        return validationResult;
    }

    @Override
    public void deleteMonthlySettlement(@NonNull final UserID userId, @NonNull final Integer year,
                                        @NonNull final Month month) {
        this.monthlySettlementDao.deleteMonthlySettlement(userId.getId(), year, month.getValue());
    }

    @Override
    public List<MonthlySettlement> getAllMonthlySettlementsByRangeAndCapitalsource(@NonNull final UserID userId,
                                                                                   @NonNull final LocalDate begin, @NonNull final LocalDate end,
                                                                                   @NonNull final List<CapitalsourceID> capitalsourceIds) {
        notEmpty(capitalsourceIds, "CapitalsourceIds must not be empty!");
        final List<Long> capitalsourceIdLongs = capitalsourceIds.stream().map(CapitalsourceID::getId).toList();
        final List<MonthlySettlementData> monthlySettlementDatas = this.monthlySettlementDao
                .getAllMonthlySettlementsByRangeAndCapitalsource(userId.getId(), begin.getYear(), begin.getMonthValue(),
                        end.getYear(), end.getMonthValue(), capitalsourceIdLongs);
        return this.mapMonthlySettlementDataList(monthlySettlementDatas);
    }
}
