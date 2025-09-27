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
import org.laladev.moneyjinn.model.Contractpartner;
import org.laladev.moneyjinn.model.PostingAccount;
import org.laladev.moneyjinn.model.PreDefMoneyflow;
import org.laladev.moneyjinn.model.PreDefMoneyflowID;
import org.laladev.moneyjinn.model.access.AccessRelation;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceType;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.service.CacheNames;
import org.laladev.moneyjinn.service.api.*;
import org.laladev.moneyjinn.service.dao.PreDefMoneyflowDao;
import org.laladev.moneyjinn.service.dao.data.PreDefMoneyflowData;
import org.laladev.moneyjinn.service.dao.data.mapper.PreDefMoneyflowDataMapper;
import org.springframework.cache.interceptor.SimpleKey;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.springframework.util.Assert.notNull;

@Named
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class PreDefMoneyflowService extends AbstractService implements IPreDefMoneyflowService {
    private final IUserService userService;
    private final ICapitalsourceService capitalsourceService;
    private final IContractpartnerService contractpartnerService;
    private final IAccessRelationService accessRelationService;
    private final IPostingAccountService postingAccountService;
    private final PreDefMoneyflowDao preDefMoneyflowDao;
    private final PreDefMoneyflowDataMapper preDefMoneyflowDataMapper;

    private final PreDefMoneyflow mapPreDefMoneyflowData(final PreDefMoneyflowData preDefMoneyflowData) {
        if (preDefMoneyflowData != null) {
            final PreDefMoneyflow preDefMoneyflow = this.preDefMoneyflowDataMapper.mapBToA(preDefMoneyflowData);

            this.userService.enrichEntity(preDefMoneyflow);
            this.postingAccountService.enrichEntity(preDefMoneyflow);
            this.capitalsourceService.enrichEntity(preDefMoneyflow);
            this.contractpartnerService.enrichEntity(preDefMoneyflow);

            return preDefMoneyflow;
        }
        return null;
    }

    private List<PreDefMoneyflow> mapPreDefMoneyflowDataList(final List<PreDefMoneyflowData> preDefMoneyflowDataList) {
        return preDefMoneyflowDataList.stream().map(this::mapPreDefMoneyflowData).toList();
    }

    @Override
    public ValidationResult validatePreDefMoneyflow(final PreDefMoneyflow preDefMoneyflow) {
        notNull(preDefMoneyflow.getUser(), "preDefMoneyflow.user must not be null!");
        notNull(preDefMoneyflow.getUser().getId(), "preDefMoneyflow.user.id must not be null!");

        final LocalDate today = LocalDate.now();
        final UserID userId = preDefMoneyflow.getUser().getId();
        final ValidationResult validationResult = new ValidationResult();
        final Consumer<ErrorCode> addResult = (final ErrorCode errorCode) -> validationResult.addValidationResultItem(
                new ValidationResultItem(preDefMoneyflow.getId(), errorCode));

        if (preDefMoneyflow.getCapitalsource() == null) {
            addResult.accept(ErrorCode.CAPITALSOURCE_IS_NOT_SET);
        } else {
            final AccessRelation accessRelation = this.accessRelationService.getCurrentAccessRelationById(userId);
            final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId,
                    accessRelation.getGroupID(), preDefMoneyflow.getCapitalsource().getId());
            if (capitalsource == null) {
                addResult.accept(ErrorCode.CAPITALSOURCE_DOES_NOT_EXIST);
            } else if (!capitalsource.groupUseAllowed(userId)) {
                addResult.accept(ErrorCode.CAPITALSOURCE_DOES_NOT_EXIST);
            } else if (!capitalsource.dateIsInValidPeriod(today)) {
                addResult.accept(ErrorCode.CAPITALSOURCE_USE_OUT_OF_VALIDITY);
            } else if (capitalsource.getType() == CapitalsourceType.CREDIT) {
                addResult.accept(ErrorCode.CAPITALSOURCE_INVALID);
            }
        }

        if (preDefMoneyflow.getContractpartner() == null) {
            addResult.accept(ErrorCode.CONTRACTPARTNER_IS_NOT_SET);
        } else {
            final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
                    preDefMoneyflow.getContractpartner().getId());
            if (contractpartner == null) {
                addResult.accept(ErrorCode.CONTRACTPARTNER_DOES_NOT_EXIST);
            } else if (!contractpartner.dateIsInValidPeriod(today)) {
                addResult.accept(ErrorCode.CONTRACTPARTNER_NO_LONGER_VALID);
            }
        }

        if (preDefMoneyflow.getComment() == null || preDefMoneyflow.getComment().isBlank()) {
            addResult.accept(ErrorCode.COMMENT_IS_NOT_SET);
        }

        final BigDecimal amount = preDefMoneyflow.getAmount();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
            addResult.accept(ErrorCode.AMOUNT_IS_ZERO);
        } else if (amount.precision() - amount.scale() > 6) {
            addResult.accept(ErrorCode.AMOUNT_TO_BIG);
        }

        if (preDefMoneyflow.getPostingAccount() == null) {
            addResult.accept(ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED);
        } else {
            final PostingAccount postingAccount = this.postingAccountService
                    .getPostingAccountById(preDefMoneyflow.getPostingAccount().getId());
            if (postingAccount == null) {
                addResult.accept(ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED);
            }
        }

        return validationResult;
    }

    @Override
    public PreDefMoneyflow getPreDefMoneyflowById(@NonNull final UserID userId,
                                                  @NonNull final PreDefMoneyflowID preDefMoneyflowId) {
        final Supplier<PreDefMoneyflow> supplier = () -> this.mapPreDefMoneyflowData(
                this.preDefMoneyflowDao.getPreDefMoneyflowById(userId.getId(), preDefMoneyflowId.getId()));

        return super.getFromCacheOrExecute(CacheNames.PRE_DEF_MONEYFLOW_BY_ID, new SimpleKey(userId, preDefMoneyflowId),
                supplier, PreDefMoneyflow.class);
    }

    @Override
    public List<PreDefMoneyflow> getAllPreDefMoneyflows(@NonNull final UserID userId) {
        final Supplier<List<PreDefMoneyflow>> supplier = () -> this.mapPreDefMoneyflowDataList(
                this.preDefMoneyflowDao.getAllPreDefMoneyflows(userId.getId()));

        return super.getListFromCacheOrExecute(CacheNames.ALL_PRE_DEF_MONEYFLOWS, userId, supplier);

    }

    @Override
    public void updatePreDefMoneyflow(@NonNull final PreDefMoneyflow preDefMoneyflow) {
        final ValidationResult validationResult = this.validatePreDefMoneyflow(preDefMoneyflow);
        if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
            final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().getFirst();
            throw new BusinessException("PreDefMoneyflow update failed!", validationResultItem.getError());
        }
        final PreDefMoneyflowData preDefMoneyflowData = this.preDefMoneyflowDataMapper.mapAToB(preDefMoneyflow);
        this.preDefMoneyflowDao.updatePreDefMoneyflow(preDefMoneyflowData);
        this.evictPreDefMoneyflowCache(preDefMoneyflow.getUser().getId(), preDefMoneyflow.getId());
    }

    @Override
    public PreDefMoneyflowID createPreDefMoneyflow(@NonNull final PreDefMoneyflow preDefMoneyflow) {
        preDefMoneyflow.setId(null);
        final ValidationResult validationResult = this.validatePreDefMoneyflow(preDefMoneyflow);
        if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
            final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().getFirst();
            throw new BusinessException("PreDefMoneyflow creation failed!", validationResultItem.getError());
        }
        final PreDefMoneyflowData preDefMoneyflowData = this.preDefMoneyflowDataMapper.mapAToB(preDefMoneyflow);
        final Long preDefMoneyflowId = this.preDefMoneyflowDao.createPreDefMoneyflow(preDefMoneyflowData);
        this.evictPreDefMoneyflowCache(preDefMoneyflow.getUser().getId(), new PreDefMoneyflowID(preDefMoneyflowId));
        return new PreDefMoneyflowID(preDefMoneyflowId);
    }

    @Override
    public void deletePreDefMoneyflow(@NonNull final UserID userId,
                                      @NonNull final PreDefMoneyflowID preDefMoneyflowId) {
        this.preDefMoneyflowDao.deletePreDefMoneyflow(userId.getId(), preDefMoneyflowId.getId());
        this.evictPreDefMoneyflowCache(userId, preDefMoneyflowId);
    }

    @Override
    public void setLastUsedDate(@NonNull final UserID userId, @NonNull final PreDefMoneyflowID preDefMoneyflowId) {
        this.preDefMoneyflowDao.setLastUsed(userId.getId(), preDefMoneyflowId.getId());
        this.evictPreDefMoneyflowCache(userId, preDefMoneyflowId);
    }

    private void evictPreDefMoneyflowCache(final UserID userId, final PreDefMoneyflowID preDefMoneyflowId) {
        if (preDefMoneyflowId != null) {
            super.evictFromCache(CacheNames.ALL_PRE_DEF_MONEYFLOWS, userId);
            super.evictFromCache(CacheNames.PRE_DEF_MONEYFLOW_BY_ID, new SimpleKey(userId, preDefMoneyflowId));
        }
    }
}
