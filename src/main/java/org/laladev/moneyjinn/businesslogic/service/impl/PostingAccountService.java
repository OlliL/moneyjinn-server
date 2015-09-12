package org.laladev.moneyjinn.businesslogic.service.impl;

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

import javax.inject.Inject;
import javax.inject.Named;

import org.laladev.moneyjinn.businesslogic.dao.PostingAccountDao;
import org.laladev.moneyjinn.businesslogic.dao.data.PostingAccountData;
import org.laladev.moneyjinn.businesslogic.dao.data.mapper.PostingAccountDataMapper;
import org.laladev.moneyjinn.businesslogic.model.ErrorCode;
import org.laladev.moneyjinn.businesslogic.model.PostingAccount;
import org.laladev.moneyjinn.businesslogic.model.PostingAccountID;
import org.laladev.moneyjinn.businesslogic.model.exception.BusinessException;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResult;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.businesslogic.service.CacheNames;
import org.laladev.moneyjinn.businesslogic.service.api.IPostingAccountService;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.util.Assert;

@Named
@EnableCaching
public class PostingAccountService extends AbstractService implements IPostingAccountService {

	@Inject
	private PostingAccountDao postingAccountDao;

	@Override
	protected void addBeanMapper() {
		super.registerBeanMapper(new PostingAccountDataMapper());

	}

	@Override
	public ValidationResult validatePostingAccount(final PostingAccount postingAccount) {
		Assert.notNull(postingAccount);
		final ValidationResult validationResult = new ValidationResult();

		if (postingAccount.getName() == null || postingAccount.getName().trim().isEmpty()) {
			validationResult.addValidationResultItem(
					new ValidationResultItem(postingAccount.getId(), ErrorCode.NAME_MUST_NOT_BE_EMPTY));
		} else {
			final PostingAccount checkPostingAccount = this.getPostingAccountByName(postingAccount.getName());
			// Update OR Create
			if (checkPostingAccount != null) {
				if (postingAccount.getId() == null || !checkPostingAccount.getId().equals(postingAccount.getId())) {
					validationResult.addValidationResultItem(new ValidationResultItem(postingAccount.getId(),
							ErrorCode.POSTINGACCOUNT_WITH_SAME_NAME_ALREADY_EXISTS));
				}
			}
		}

		return validationResult;
	}

	@Override
	@Cacheable(CacheNames.POSTINGACCOUNT_BY_ID)
	public PostingAccount getPostingAccountById(final PostingAccountID postingAccountId) {
		Assert.notNull(postingAccountId);
		final PostingAccountData postingAccountData = this.postingAccountDao
				.getPostingAccountById(postingAccountId.getId());
		return super.map(postingAccountData, PostingAccount.class);
	}

	@Override
	public Set<Character> getAllPostingAccountInitials() {
		return this.postingAccountDao.getAllPostingAccountInitials();
	}

	@Override
	public Integer countAllPostingAccounts() {
		return this.postingAccountDao.countAllPostingAccounts();
	}

	@Override
	@Cacheable(CacheNames.ALL_POSTINGACCOUNTS)
	public List<PostingAccount> getAllPostingAccounts() {
		final List<PostingAccountData> postingAccountDataList = this.postingAccountDao.getAllPostingAccounts();
		return super.mapList(postingAccountDataList, PostingAccount.class);
	}

	@Override
	public List<PostingAccount> getAllPostingAccountsByInitial(final Character initial) {
		Assert.notNull(initial);
		final List<PostingAccountData> postingAccountDataList = this.postingAccountDao
				.getAllPostingAccountsByInitial(initial);
		return super.mapList(postingAccountDataList, PostingAccount.class);
	}

	@Override
	public PostingAccount getPostingAccountByName(final String name) {
		Assert.notNull(name);
		final PostingAccountData postingAccountData = this.postingAccountDao.getPostingAccountByName(name);
		final PostingAccount postingAccount = super.map(postingAccountData, PostingAccount.class);
		return postingAccount;
	}

	@Override
	public void updatePostingAccount(final PostingAccount postingAccount) {
		Assert.notNull(postingAccount);
		final ValidationResult validationResult = this.validatePostingAccount(postingAccount);

		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("PostingAccount update failed!", validationResultItem.getError());
		}

		final PostingAccountData postingAccountData = super.map(postingAccount, PostingAccountData.class);
		this.postingAccountDao.updatePostingAccount(postingAccountData);
		this.evictPostingAccountCache(postingAccount.getId());
	}

	@Override
	public void createPostingAccount(final PostingAccount postingAccount) {
		Assert.notNull(postingAccount);
		postingAccount.setId(null);
		final ValidationResult validationResult = this.validatePostingAccount(postingAccount);

		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("PostingAccount creation failed!", validationResultItem.getError());
		}

		final PostingAccountData postingAccountData = super.map(postingAccount, PostingAccountData.class);
		final Long postingAccountId = this.postingAccountDao.createPostingAccount(postingAccountData);
		this.evictPostingAccountCache(new PostingAccountID(postingAccountId));
	}

	@Override
	public void deletePostingAccount(final PostingAccountID postingAccountId) {
		Assert.notNull(postingAccountId);
		try {
			this.postingAccountDao.deletePostingAccount(postingAccountId.getId());
			this.evictPostingAccountCache(postingAccountId);
		} catch (final Exception e) {
			throw new BusinessException(
					"The posting account cannot be deleted because it is still referenced by a flow of money or a predefined flow of money!",
					ErrorCode.POSTINGACCOUNT_STILL_REFERENCED);
		}

	}

	private void evictPostingAccountCache(final PostingAccountID postingAccountId) {
		if (postingAccountId != null) {
			final Cache allPostingAccountsCache = super.getCache(CacheNames.ALL_POSTINGACCOUNTS);
			final Cache postingAccountByIdCache = super.getCache(CacheNames.POSTINGACCOUNT_BY_ID);
			if (allPostingAccountsCache != null) {
				allPostingAccountsCache.clear();
			}
			if (postingAccountByIdCache != null) {
				postingAccountByIdCache.evict(postingAccountId);
			}
		}
	}
}
