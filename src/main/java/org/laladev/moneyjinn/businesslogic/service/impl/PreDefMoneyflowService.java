package org.laladev.moneyjinn.businesslogic.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

//Copyright (c) 2015 Oliver Lehmann <oliver@laladev.org>
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without
//modification, are permitted provided that the following conditions
//are met:
//1. Redistributions of source code must retain the above copyright
//notice, this list of conditions and the following disclaimer
//2. Redistributions in binary form must reproduce the above copyright
//notice, this list of conditions and the following disclaimer in the
//documentation and/or other materials provided with the distribution.
//
//THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
//ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
//IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
//ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
//FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
//DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
//OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
//HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
//LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
//OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
//SUCH DAMAGE.

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.laladev.moneyjinn.businesslogic.dao.PreDefMoneyflowDao;
import org.laladev.moneyjinn.businesslogic.dao.data.PreDefMoneyflowData;
import org.laladev.moneyjinn.businesslogic.dao.data.mapper.PreDefMoneyflowDataMapper;
import org.laladev.moneyjinn.businesslogic.model.Contractpartner;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerID;
import org.laladev.moneyjinn.businesslogic.model.ErrorCode;
import org.laladev.moneyjinn.businesslogic.model.PostingAccount;
import org.laladev.moneyjinn.businesslogic.model.PostingAccountID;
import org.laladev.moneyjinn.businesslogic.model.PreDefMoneyflow;
import org.laladev.moneyjinn.businesslogic.model.PreDefMoneyflowID;
import org.laladev.moneyjinn.businesslogic.model.access.Group;
import org.laladev.moneyjinn.businesslogic.model.access.GroupID;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.businesslogic.model.exception.BusinessException;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResult;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.businesslogic.service.CacheNames;
import org.laladev.moneyjinn.businesslogic.service.api.IAccessRelationService;
import org.laladev.moneyjinn.businesslogic.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.businesslogic.service.api.IContractpartnerService;
import org.laladev.moneyjinn.businesslogic.service.api.IPostingAccountService;
import org.laladev.moneyjinn.businesslogic.service.api.IPreDefMoneyflowService;
import org.laladev.moneyjinn.businesslogic.service.api.IUserService;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.util.Assert;

@Named
@EnableCaching
public class PreDefMoneyflowService extends AbstractService implements IPreDefMoneyflowService {

	@Inject
	private IUserService userService;
	@Inject
	private ICapitalsourceService capitalsourceService;
	@Inject
	private IContractpartnerService contractpartnerService;
	@Inject
	private IAccessRelationService accessRelationService;
	@Inject
	private IPostingAccountService postingAccountService;

	@Inject
	private PreDefMoneyflowDao preDefMoneyflowDao;
	@Inject
	private CacheManager cacheManager;

	@Override
	protected void addBeanMapper() {
		super.registerBeanMapper(new PreDefMoneyflowDataMapper());

	}

	private final PreDefMoneyflow mapPreDefMoneyflowData(final PreDefMoneyflowData preDefMoneyflowData) {
		if (preDefMoneyflowData != null) {
			final PreDefMoneyflow preDefMoneyflow = super.map(preDefMoneyflowData, PreDefMoneyflow.class);
			final UserID userId = preDefMoneyflow.getUser().getId();
			final User user = this.userService.getUserById(userId);
			final Group accessor = this.accessRelationService.getAccessor(userId);
			final GroupID groupId = accessor.getId();
			preDefMoneyflow.setUser(user);

			PostingAccount postingAccount = preDefMoneyflow.getPostingAccount();
			if (postingAccount != null) {
				final PostingAccountID postingAccountId = postingAccount.getId();
				postingAccount = this.postingAccountService.getPostingAccountById(postingAccountId);
				preDefMoneyflow.setPostingAccount(postingAccount);
			}

			Capitalsource capitalsource = preDefMoneyflow.getCapitalsource();
			if (capitalsource != null) {
				final CapitalsourceID capitalsourceId = capitalsource.getId();
				capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId, capitalsourceId);
				preDefMoneyflow.setCapitalsource(capitalsource);
			}

			Contractpartner contractpartner = preDefMoneyflow.getContractpartner();
			if (contractpartner != null) {
				final ContractpartnerID contractpartnerId = contractpartner.getId();
				contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);
				preDefMoneyflow.setContractpartner(contractpartner);
			}

			return preDefMoneyflow;
		}
		return null;
	}

	private final List<PreDefMoneyflow> mapPreDefMoneyflowDataList(
			final List<PreDefMoneyflowData> preDefMoneyflowDataList) {
		return preDefMoneyflowDataList.stream().map(element -> this.mapPreDefMoneyflowData(element))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public ValidationResult validatePreDefMoneyflow(final PreDefMoneyflow preDefMoneyflow) {
		Assert.notNull(preDefMoneyflow);
		Assert.notNull(preDefMoneyflow.getUser());
		Assert.notNull(preDefMoneyflow.getUser().getId());
		final ValidationResult validationResult = new ValidationResult();

		final LocalDate today = LocalDate.now();
		final UserID userId = preDefMoneyflow.getUser().getId();

		if (preDefMoneyflow.getCapitalsource() == null) {
			validationResult.addValidationResultItem(
					new ValidationResultItem(preDefMoneyflow.getId(), ErrorCode.CAPITALSOURCE_IS_NOT_SET));
		} else {
			final Group accessor = this.accessRelationService.getAccessor(userId);
			final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, accessor.getId(),
					preDefMoneyflow.getCapitalsource().getId());
			if (capitalsource == null) {
				validationResult.addValidationResultItem(
						new ValidationResultItem(preDefMoneyflow.getId(), ErrorCode.CAPITALSOURCE_DOES_NOT_EXIST));
			} else if (today.isBefore(capitalsource.getValidFrom()) || today.isAfter(capitalsource.getValidTil())) {
				validationResult.addValidationResultItem(
						new ValidationResultItem(preDefMoneyflow.getId(), ErrorCode.CAPITALSOURCE_USE_OUT_OF_VALIDITY));
			}
		}

		if (preDefMoneyflow.getContractpartner() == null) {
			validationResult.addValidationResultItem(
					new ValidationResultItem(preDefMoneyflow.getId(), ErrorCode.CONTRACTPARTNER_IS_NOT_SET));
		} else {
			final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
					preDefMoneyflow.getContractpartner().getId());
			if (contractpartner == null) {
				validationResult.addValidationResultItem(
						new ValidationResultItem(preDefMoneyflow.getId(), ErrorCode.CONTRACTPARTNER_DOES_NOT_EXIST));
			} else if (today.isBefore(contractpartner.getValidFrom()) || today.isAfter(contractpartner.getValidTil())) {
				validationResult.addValidationResultItem(
						new ValidationResultItem(preDefMoneyflow.getId(), ErrorCode.CONTRACTPARTNER_NO_LONGER_VALID));
			}
		}

		if (preDefMoneyflow.getComment() == null || preDefMoneyflow.getComment().trim().isEmpty()) {
			validationResult.addValidationResultItem(
					new ValidationResultItem(preDefMoneyflow.getId(), ErrorCode.COMMENT_IS_NOT_SET));
		}

		if (preDefMoneyflow.getAmount() == null || preDefMoneyflow.getAmount().compareTo(BigDecimal.ZERO) == 0) {
			validationResult.addValidationResultItem(
					new ValidationResultItem(preDefMoneyflow.getId(), ErrorCode.AMOUNT_IS_ZERO));
		}

		if (preDefMoneyflow.getPostingAccount() == null) {
			validationResult.addValidationResultItem(
					new ValidationResultItem(preDefMoneyflow.getId(), ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED));
		} else {
			final PostingAccount postingAccount = this.postingAccountService
					.getPostingAccountById(preDefMoneyflow.getPostingAccount().getId());
			if (postingAccount == null) {
				validationResult.addValidationResultItem(
						new ValidationResultItem(preDefMoneyflow.getId(), ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED));
			}

		}

		return validationResult;
	}

	@Override
	@Cacheable(CacheNames.POSTINGACCOUNT_BY_ID)
	public PreDefMoneyflow getPreDefMoneyflowById(final UserID userId, final PreDefMoneyflowID preDefMoneyflowId) {
		Assert.notNull(userId);
		Assert.notNull(preDefMoneyflowId);
		final PreDefMoneyflowData preDefMoneyflowData = this.preDefMoneyflowDao.getPreDefMoneyflowById(userId.getId(),
				preDefMoneyflowId.getId());
		return this.mapPreDefMoneyflowData(preDefMoneyflowData);
	}

	@Override
	public Set<Character> getAllPreDefMoneyflowInitials(final UserID userId) {
		Assert.notNull(userId);
		final List<Long> contractpartnerIds = this.preDefMoneyflowDao.getAllContractpartnerIds(userId.getId());
		if (contractpartnerIds != null && !contractpartnerIds.isEmpty()) {
			final List<Contractpartner> contractpartners = contractpartnerIds.stream()
					.map(cpi -> this.contractpartnerService.getContractpartnerById(userId, new ContractpartnerID(cpi)))
					.collect(Collectors.toCollection(ArrayList::new));
			final HashSet<Character> initials = contractpartners.stream()
					.map(cp -> cp.getName().toUpperCase().charAt(0)).collect(Collectors.toCollection(HashSet::new));
			return initials;
		}

		return null;
	}

	@Override
	public Integer countAllPreDefMoneyflows(final UserID userId) {
		Assert.notNull(userId);
		return this.preDefMoneyflowDao.countAllPreDefMoneyflows(userId.getId());
	}

	@Override
	@Cacheable(CacheNames.ALL_POSTINGACCOUNTS)
	public List<PreDefMoneyflow> getAllPreDefMoneyflows(final UserID userId) {
		Assert.notNull(userId);
		final List<PreDefMoneyflowData> preDefMoneyflowDataList = this.preDefMoneyflowDao
				.getAllPreDefMoneyflows(userId.getId());
		return this.mapPreDefMoneyflowDataList(preDefMoneyflowDataList);
	}

	@Override
	public List<PreDefMoneyflow> getAllPreDefMoneyflowsByInitial(final UserID userId, final Character initial) {
		Assert.notNull(userId);
		Assert.notNull(initial);
		final List<PreDefMoneyflow> preDefMoneyflows = this.getAllPreDefMoneyflows(userId);
		if (preDefMoneyflows != null && !preDefMoneyflows.isEmpty()) {
			return preDefMoneyflows.stream()
					.filter(pdm -> pdm.getContractpartner().getName().toUpperCase()
							.startsWith(initial.toString().toUpperCase()))
					.collect(Collectors.toCollection(ArrayList::new));
		}
		return null;
	}

	@Override
	public void updatePreDefMoneyflow(final PreDefMoneyflow preDefMoneyflow) {
		Assert.notNull(preDefMoneyflow);
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
	public void createPreDefMoneyflow(final PreDefMoneyflow preDefMoneyflow) {
		Assert.notNull(preDefMoneyflow);
		preDefMoneyflow.setId(null);
		final ValidationResult validationResult = this.validatePreDefMoneyflow(preDefMoneyflow);

		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("PreDefMoneyflow creation failed!", validationResultItem.getError());
		}

		final PreDefMoneyflowData preDefMoneyflowData = super.map(preDefMoneyflow, PreDefMoneyflowData.class);
		final Long preDefMoneyflowId = this.preDefMoneyflowDao.createPreDefMoneyflow(preDefMoneyflowData);
		this.evictPreDefMoneyflowCache(preDefMoneyflow.getUser().getId(), new PreDefMoneyflowID(preDefMoneyflowId));
	}

	@Override
	public void deletePreDefMoneyflow(final UserID userId, final PreDefMoneyflowID preDefMoneyflowId) {
		Assert.notNull(userId);
		Assert.notNull(preDefMoneyflowId);
		this.preDefMoneyflowDao.deletePreDefMoneyflow(userId.getId(), preDefMoneyflowId.getId());
		this.evictPreDefMoneyflowCache(userId, preDefMoneyflowId);

	}

	@Override
	public void setLastUsedDate(final UserID userId, final PreDefMoneyflowID preDefMoneyflowId) {
		Assert.notNull(userId);
		Assert.notNull(preDefMoneyflowId);
		this.preDefMoneyflowDao.setLastUsed(userId.getId(), preDefMoneyflowId.getId());
	}

	private void evictPreDefMoneyflowCache(final UserID userId, final PreDefMoneyflowID preDefMoneyflowId) {
		if (preDefMoneyflowId != null) {
			final Cache allPreDefMoneyflowsCache = this.cacheManager.getCache(CacheNames.ALL_POSTINGACCOUNTS);
			final Cache preDefMoneyflowByIdCache = this.cacheManager.getCache(CacheNames.POSTINGACCOUNT_BY_ID);
			if (allPreDefMoneyflowsCache != null) {
				allPreDefMoneyflowsCache.evict(userId);
			}
			if (preDefMoneyflowByIdCache != null) {
				preDefMoneyflowByIdCache.evict(new SimpleKey(userId, preDefMoneyflowId));
			}
		}
	}

}
