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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

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
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.IContractpartnerService;
import org.laladev.moneyjinn.service.api.IPostingAccountService;
import org.laladev.moneyjinn.service.api.IPreDefMoneyflowService;
import org.laladev.moneyjinn.service.api.IUserService;
import org.laladev.moneyjinn.service.dao.PreDefMoneyflowDao;
import org.laladev.moneyjinn.service.dao.data.PreDefMoneyflowData;
import org.laladev.moneyjinn.service.dao.data.mapper.PreDefMoneyflowDataMapper;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.util.Assert;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;

@Named
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class PreDefMoneyflowService extends AbstractService implements IPreDefMoneyflowService {
	private static final String PRE_DEF_MONEYFLOW_ID_MUST_NOT_BE_NULL = "preDefMoneyflowId must not be null!";
	private static final String PRE_DEF_MONEYFLOW_MUST_NOT_BE_NULL = "preDefMoneyflow must not be null!";
	private static final String USER_ID_MUST_NOT_BE_NULL = "UserId must not be null!";
	private final IUserService userService;
	private final ICapitalsourceService capitalsourceService;
	private final IContractpartnerService contractpartnerService;
	private final IAccessRelationService accessRelationService;
	private final IPostingAccountService postingAccountService;
	private final PreDefMoneyflowDao preDefMoneyflowDao;
	private final PreDefMoneyflowDataMapper preDefMoneyflowDataMapper;

	@Override
	@PostConstruct
	protected void addBeanMapper() {
		super.registerBeanMapper(this.preDefMoneyflowDataMapper);
	}

	private final PreDefMoneyflow mapPreDefMoneyflowData(final PreDefMoneyflowData preDefMoneyflowData) {
		if (preDefMoneyflowData != null) {
			final PreDefMoneyflow preDefMoneyflow = super.map(preDefMoneyflowData, PreDefMoneyflow.class);

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
		Assert.notNull(preDefMoneyflow, PRE_DEF_MONEYFLOW_MUST_NOT_BE_NULL);
		Assert.notNull(preDefMoneyflow.getUser(), "preDefMoneyflow.user must not be null!");
		Assert.notNull(preDefMoneyflow.getUser().getId(), "preDefMoneyflow.user.id must not be null!");

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
	@Cacheable(CacheNames.POSTINGACCOUNT_BY_ID)
	public PreDefMoneyflow getPreDefMoneyflowById(final UserID userId, final PreDefMoneyflowID preDefMoneyflowId) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(preDefMoneyflowId, PRE_DEF_MONEYFLOW_ID_MUST_NOT_BE_NULL);
		final PreDefMoneyflowData preDefMoneyflowData = this.preDefMoneyflowDao.getPreDefMoneyflowById(userId.getId(),
				preDefMoneyflowId.getId());
		return this.mapPreDefMoneyflowData(preDefMoneyflowData);
	}

	@Override
	@Cacheable(CacheNames.ALL_PRE_DEF_MONEYFLOWS)
	public List<PreDefMoneyflow> getAllPreDefMoneyflows(final UserID userId) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		final List<PreDefMoneyflowData> preDefMoneyflowDataList = this.preDefMoneyflowDao
				.getAllPreDefMoneyflows(userId.getId());
		return this.mapPreDefMoneyflowDataList(preDefMoneyflowDataList);
	}

	@Override
	public void updatePreDefMoneyflow(final PreDefMoneyflow preDefMoneyflow) {
		Assert.notNull(preDefMoneyflow, PRE_DEF_MONEYFLOW_MUST_NOT_BE_NULL);
		final ValidationResult validationResult = this.validatePreDefMoneyflow(preDefMoneyflow);
		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("PreDefMoneyflow update failed!", validationResultItem.getError());
		}
		final PreDefMoneyflowData preDefMoneyflowData = super.map(preDefMoneyflow, PreDefMoneyflowData.class);
		this.preDefMoneyflowDao.updatePreDefMoneyflow(preDefMoneyflowData);
		this.evictPreDefMoneyflowCache(preDefMoneyflow.getUser().getId(), preDefMoneyflow.getId());
	}

	@Override
	public PreDefMoneyflowID createPreDefMoneyflow(final PreDefMoneyflow preDefMoneyflow) {
		Assert.notNull(preDefMoneyflow, PRE_DEF_MONEYFLOW_MUST_NOT_BE_NULL);
		preDefMoneyflow.setId(null);
		final ValidationResult validationResult = this.validatePreDefMoneyflow(preDefMoneyflow);
		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("PreDefMoneyflow creation failed!", validationResultItem.getError());
		}
		final PreDefMoneyflowData preDefMoneyflowData = super.map(preDefMoneyflow, PreDefMoneyflowData.class);
		final Long preDefMoneyflowId = this.preDefMoneyflowDao.createPreDefMoneyflow(preDefMoneyflowData);
		this.evictPreDefMoneyflowCache(preDefMoneyflow.getUser().getId(), new PreDefMoneyflowID(preDefMoneyflowId));
		return new PreDefMoneyflowID(preDefMoneyflowId);
	}

	@Override
	public void deletePreDefMoneyflow(final UserID userId, final PreDefMoneyflowID preDefMoneyflowId) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(preDefMoneyflowId, PRE_DEF_MONEYFLOW_ID_MUST_NOT_BE_NULL);
		this.preDefMoneyflowDao.deletePreDefMoneyflow(userId.getId(), preDefMoneyflowId.getId());
		this.evictPreDefMoneyflowCache(userId, preDefMoneyflowId);
	}

	@Override
	public void setLastUsedDate(final UserID userId, final PreDefMoneyflowID preDefMoneyflowId) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(preDefMoneyflowId, PRE_DEF_MONEYFLOW_ID_MUST_NOT_BE_NULL);
		this.preDefMoneyflowDao.setLastUsed(userId.getId(), preDefMoneyflowId.getId());
		this.evictPreDefMoneyflowCache(userId, preDefMoneyflowId);
	}

	private void evictPreDefMoneyflowCache(final UserID userId, final PreDefMoneyflowID preDefMoneyflowId) {
		if (preDefMoneyflowId != null) {
			final Cache allPreDefMoneyflowsCache = super.getCache(CacheNames.ALL_PRE_DEF_MONEYFLOWS);
			final Cache preDefMoneyflowByIdCache = super.getCache(CacheNames.POSTINGACCOUNT_BY_ID);
			if (allPreDefMoneyflowsCache != null) {
				allPreDefMoneyflowsCache.evict(userId);
			}
			if (preDefMoneyflowByIdCache != null) {
				preDefMoneyflowByIdCache.evict(new SimpleKey(userId, preDefMoneyflowId));
			}
		}
	}
}
