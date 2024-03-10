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

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.IHasPostingAccount;
import org.laladev.moneyjinn.model.PostingAccount;
import org.laladev.moneyjinn.model.PostingAccountID;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.service.CacheNames;
import org.laladev.moneyjinn.service.api.IPostingAccountService;
import org.laladev.moneyjinn.service.dao.PostingAccountDao;
import org.laladev.moneyjinn.service.dao.data.PostingAccountData;
import org.laladev.moneyjinn.service.dao.data.mapper.PostingAccountDataMapper;
import org.laladev.moneyjinn.service.event.EventType;
import org.laladev.moneyjinn.service.event.PostingAccountChangedEvent;
import org.springframework.util.Assert;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;

@Named
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class PostingAccountService extends AbstractService implements IPostingAccountService {
	private static final String POSTING_ACCOUNT_MUST_NOT_BE_NULL = "postingAccount must not be null!";
	private static final Log LOG = LogFactory.getLog(PostingAccountService.class);
	private final PostingAccountDao postingAccountDao;
	private final PostingAccountDataMapper postingAccountDataMapper;

	@Override
	@PostConstruct
	protected void addBeanMapper() {
		super.registerBeanMapper(this.postingAccountDataMapper);
	}

	@Override
	public ValidationResult validatePostingAccount(final PostingAccount postingAccount) {
		Assert.notNull(postingAccount, POSTING_ACCOUNT_MUST_NOT_BE_NULL);

		final ValidationResult validationResult = new ValidationResult();
		final Consumer<ErrorCode> addResult = (final ErrorCode errorCode) -> validationResult.addValidationResultItem(
				new ValidationResultItem(postingAccount.getId(), errorCode));

		if (postingAccount.getName() == null || postingAccount.getName().isBlank()) {
			addResult.accept(ErrorCode.NAME_MUST_NOT_BE_EMPTY);
		} else {
			final PostingAccount checkPostingAccount = this.getPostingAccountByName(postingAccount.getName());
			// Update OR Create
			if (checkPostingAccount != null && (postingAccount.getId() == null
					|| !checkPostingAccount.getId().equals(postingAccount.getId()))) {
				addResult.accept(ErrorCode.POSTINGACCOUNT_WITH_SAME_NAME_ALREADY_EXISTS);
			}
		}

		return validationResult;
	}

	@Override
	public PostingAccount getPostingAccountById(final PostingAccountID postingAccountId) {
		Assert.notNull(postingAccountId, "postingAccountId must not be null!");

		final Supplier<PostingAccount> supplier = () -> super.map(
				this.postingAccountDao.getPostingAccountById(postingAccountId.getId()), PostingAccount.class);

		return super.getFromCacheOrExecute(CacheNames.POSTINGACCOUNT_BY_ID, postingAccountId, supplier,
				PostingAccount.class);
	}

	@Override
	public List<PostingAccount> getAllPostingAccounts() {
		final Supplier<List<PostingAccount>> supplier = () -> super.mapList(
				this.postingAccountDao.getAllPostingAccounts(), PostingAccount.class);

		return super.getListFromCacheOrExecute(CacheNames.ALL_POSTINGACCOUNTS, CacheNames.ALL_POSTINGACCOUNTS, supplier,
				PostingAccount.class);
	}

	@Override
	public PostingAccount getPostingAccountByName(final String name) {
		Assert.notNull(name, "name must not be null!");
		final PostingAccountData postingAccountData = this.postingAccountDao.getPostingAccountByName(name);
		return super.map(postingAccountData, PostingAccount.class);
	}

	@Override
	public void updatePostingAccount(final PostingAccount postingAccount) {
		Assert.notNull(postingAccount, POSTING_ACCOUNT_MUST_NOT_BE_NULL);
		final ValidationResult validationResult = this.validatePostingAccount(postingAccount);
		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("PostingAccount update failed!", validationResultItem.getError());
		}
		final PostingAccountData postingAccountData = super.map(postingAccount, PostingAccountData.class);
		this.postingAccountDao.updatePostingAccount(postingAccountData);
		this.evictPostingAccountCache(postingAccount.getId());

		final PostingAccountChangedEvent event = new PostingAccountChangedEvent(this, EventType.UPDATE, postingAccount);
		super.publishEvent(event);
	}

	@Override
	public PostingAccountID createPostingAccount(final PostingAccount postingAccount) {
		Assert.notNull(postingAccount, POSTING_ACCOUNT_MUST_NOT_BE_NULL);
		postingAccount.setId(null);
		final ValidationResult validationResult = this.validatePostingAccount(postingAccount);
		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("PostingAccount creation failed!", validationResultItem.getError());
		}
		final PostingAccountData postingAccountData = super.map(postingAccount, PostingAccountData.class);
		final Long postingAccountIdLong = this.postingAccountDao.createPostingAccount(postingAccountData);
		final PostingAccountID postingAccountId = new PostingAccountID(postingAccountIdLong);
		postingAccount.setId(postingAccountId);

		this.evictPostingAccountCache(postingAccountId);

		final PostingAccountChangedEvent event = new PostingAccountChangedEvent(this, EventType.CREATE, postingAccount);
		super.publishEvent(event);

		return postingAccountId;
	}

	@Override
	public void deletePostingAccount(final PostingAccountID postingAccountId) {
		Assert.notNull(postingAccountId, "postingAccountId must not be null!");
		final PostingAccount postingAccount = this.getPostingAccountById(postingAccountId);
		if (postingAccount != null) {
			try {
				this.postingAccountDao.deletePostingAccount(postingAccountId.getId());

				this.evictPostingAccountCache(postingAccountId);

				final PostingAccountChangedEvent event = new PostingAccountChangedEvent(this, EventType.DELETE,
						postingAccount);
				super.publishEvent(event);
			} catch (final Exception e) {
				LOG.info(e);
				throw new BusinessException(
						"The posting account cannot be deleted because it is still referenced by a flow of money or a predefined flow of money!",
						ErrorCode.POSTINGACCOUNT_STILL_REFERENCED);
			}
		}
	}

	private void evictPostingAccountCache(final PostingAccountID postingAccountId) {
		if (postingAccountId != null) {
			super.clearCache(CacheNames.ALL_POSTINGACCOUNTS);
			super.evictFromCache(CacheNames.POSTINGACCOUNT_BY_ID, postingAccountId);
		}
	}

	@Override
	public <T extends IHasPostingAccount> void enrichEntity(final T entity) {
		if (entity.getPostingAccount() != null) {
			final var fullMca = this.getPostingAccountById(entity.getPostingAccount().getId());
			entity.setPostingAccount(fullMca);
		}
	}
}
