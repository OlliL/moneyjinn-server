//
// Copyright (c) 2015-2024 Oliver Lehmann <lehmann@ans-netz.de>
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

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;

import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.Contractpartner;
import org.laladev.moneyjinn.model.ContractpartnerID;
import org.laladev.moneyjinn.model.IHasContractpartner;
import org.laladev.moneyjinn.model.IHasUser;
import org.laladev.moneyjinn.model.PostingAccount;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.service.CacheNames;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.IContractpartnerService;
import org.laladev.moneyjinn.service.api.IGroupService;
import org.laladev.moneyjinn.service.api.IPostingAccountService;
import org.laladev.moneyjinn.service.api.IUserService;
import org.laladev.moneyjinn.service.dao.ContractpartnerDao;
import org.laladev.moneyjinn.service.dao.data.ContractpartnerData;
import org.laladev.moneyjinn.service.dao.data.mapper.ContractpartnerDataMapper;
import org.laladev.moneyjinn.service.event.ContractpartnerChangedEvent;
import org.laladev.moneyjinn.service.event.EventType;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.util.Assert;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Named
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Log
public class ContractpartnerService extends AbstractService implements IContractpartnerService {
	private static final String STILL_REFERENCED = "You may not delete a contractual partner who is still referenced by a flow of money!";
	private static final String CONTRACTPARTNER_MUST_NOT_BE_NULL = "contractpartner must not be null!";
	private static final String USER_ID_MUST_NOT_BE_NULL = "UserId must not be null!";
	private final ContractpartnerDao contractpartnerDao;
	private final IUserService userService;
	private final IGroupService groupService;
	private final IPostingAccountService postingAccountService;
	private final IAccessRelationService accessRelationService;
	private final ContractpartnerDataMapper contractpartnerDataMapper;

	@Override
	@PostConstruct
	protected void addBeanMapper() {
		super.registerBeanMapper(this.contractpartnerDataMapper);
	}

	private Contractpartner mapContractpartnerData(final ContractpartnerData contractpartnerData) {
		if (contractpartnerData != null) {
			final Contractpartner contractpartner = super.map(contractpartnerData, Contractpartner.class);

			this.userService.enrichEntity(contractpartner);
			this.groupService.enrichEntity(contractpartner);
			this.postingAccountService.enrichEntity(contractpartner);

			return contractpartner;
		}
		return null;
	}

	private List<Contractpartner> mapContractpartnerDataList(final List<ContractpartnerData> contractpartnerDataList) {
		return contractpartnerDataList.stream().map(this::mapContractpartnerData).toList();
	}

	/**
	 * This method takes a Contractpartner as argument and sets the properties
	 * validFrom and validTil if they are NULL to default values.
	 *
	 * @param contractpartner {@link Contractpartner}
	 */
	private void prepareContractpartner(final Contractpartner contractpartner) {
		if (contractpartner.getValidFrom() == null) {
			contractpartner.setValidFrom(LocalDate.now());
		}
		if (contractpartner.getValidTil() == null) {
			contractpartner.setValidTil(MAX_DATE);
		}
	}

	@Override
	public ValidationResult validateContractpartner(final Contractpartner contractpartner) {
		Assert.notNull(contractpartner, CONTRACTPARTNER_MUST_NOT_BE_NULL);
		Assert.notNull(contractpartner.getUser(), "contractpartner.user must not be null!");
		Assert.notNull(contractpartner.getUser().getId(), "contractpartner.user.id must not be null!");
		Assert.notNull(contractpartner.getGroup(), "contractpartner.group must not be null!");
		Assert.notNull(contractpartner.getGroup().getId(), "contractpartner.group.id must not be null!");

		this.prepareContractpartner(contractpartner);

		final ValidationResult validationResult = new ValidationResult();
		final Consumer<ErrorCode> addResult = (final ErrorCode errorCode) -> validationResult.addValidationResultItem(
				new ValidationResultItem(contractpartner.getId(), errorCode));

		if (contractpartner.getValidTil().isBefore(contractpartner.getValidFrom())) {
			addResult.accept(ErrorCode.VALIDFROM_AFTER_VALIDTIL);
		} else if (contractpartner.getId() != null && this.contractpartnerDao.checkContractpartnerInUseOutOfDate(
				contractpartner.getUser().getId().getId(), contractpartner.getId().getId(),
				contractpartner.getValidFrom(), contractpartner.getValidTil())) {
			// update existing Contractpartner
			addResult.accept(ErrorCode.MONEYFLOWS_OUTSIDE_VALIDITY_PERIOD);
		}

		if (contractpartner.getName() == null || contractpartner.getName().isBlank()) {
			addResult.accept(ErrorCode.NAME_MUST_NOT_BE_EMPTY);
		} else {
			final Contractpartner checkContractpartner = this
					.getContractpartnerByName(contractpartner.getUser().getId(), contractpartner.getName());
			if (checkContractpartner != null && (contractpartner.getId() == null
					|| !checkContractpartner.getId().equals(contractpartner.getId()))) {
				// new Contractpartner || update existing Contractpartner
				addResult.accept(ErrorCode.NAME_ALREADY_EXISTS);
			}
		}

		if (contractpartner.getPostingAccount() != null) {
			final PostingAccount postingAccount = this.postingAccountService
					.getPostingAccountById(contractpartner.getPostingAccount().getId());
			if (postingAccount == null) {
				addResult.accept(ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED);
			} else {
				// Replace by full object to send postingAccountComment via the event later.
				contractpartner.setPostingAccount(postingAccount);
			}
		}

		return validationResult;
	}

	@Override
	public Contractpartner getContractpartnerById(final UserID userId, final ContractpartnerID contractpartnerId) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(contractpartnerId, "contractpartnerId must not be null!");

		final Supplier<Contractpartner> supplier = () -> this.mapContractpartnerData(
				this.contractpartnerDao.getContractpartnerById(userId.getId(), contractpartnerId.getId()));

		return super.getFromCacheOrExecute(CacheNames.CONTRACTPARTNER_BY_ID, new SimpleKey(userId, contractpartnerId),
				supplier, Contractpartner.class);
	}

	@Override
	public List<Contractpartner> getAllContractpartners(final UserID userId) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);

		final Supplier<List<Contractpartner>> supplier = () -> this.mapContractpartnerDataList(
				this.contractpartnerDao.getAllContractpartners(userId.getId()));

		return super.getListFromCacheOrExecute(CacheNames.ALL_CONTRACTPARTNER, userId, supplier, Contractpartner.class);
	}

	@Override
	public Contractpartner getContractpartnerByName(final UserID userId, final String name) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(name, "name must not be null!");
		final ContractpartnerData contractpartnerData = this.contractpartnerDao.getContractpartnerByName(userId.getId(),
				name);
		return this.mapContractpartnerData(contractpartnerData);
	}

	@Override
	public void updateContractpartner(final Contractpartner contractpartner) {
		Assert.notNull(contractpartner, CONTRACTPARTNER_MUST_NOT_BE_NULL);
		final ValidationResult validationResult = this.validateContractpartner(contractpartner);
		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("Contractpartner update failed!", validationResultItem.getError());
		}
		final ContractpartnerData contractpartnerData = super.map(contractpartner, ContractpartnerData.class);
		this.contractpartnerDao.updateContractpartner(contractpartnerData);
		this.evictContractpartnerCache(contractpartner.getUser().getId(), contractpartner.getId());
		final ContractpartnerChangedEvent event = new ContractpartnerChangedEvent(this, EventType.UPDATE,
				contractpartner);
		super.publishEvent(event);
	}

	@Override
	public ContractpartnerID createContractpartner(final Contractpartner contractpartner) {
		Assert.notNull(contractpartner, CONTRACTPARTNER_MUST_NOT_BE_NULL);
		contractpartner.setId(null);
		final ValidationResult validationResult = this.validateContractpartner(contractpartner);
		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("Contractpartner creation failed!", validationResultItem.getError());
		}
		final ContractpartnerData contractpartnerData = super.map(contractpartner, ContractpartnerData.class);

		final Long contractpartnerIdLong = this.contractpartnerDao.createContractpartner(contractpartnerData);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(contractpartnerIdLong);
		contractpartner.setId(contractpartnerId);

		this.evictContractpartnerCache(contractpartner.getUser().getId(), contractpartnerId);

		final ContractpartnerChangedEvent event = new ContractpartnerChangedEvent(this, EventType.CREATE,
				contractpartner);
		super.publishEvent(event);

		return contractpartnerId;
	}

	@Override
	public void deleteContractpartner(final UserID userId, final GroupID groupId,
			final ContractpartnerID contractpartnerId) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(groupId, "groupId must not be null!");
		Assert.notNull(contractpartnerId, "contractpartnerId must not be null!");

		final Contractpartner contractpartner = this.getContractpartnerById(userId, contractpartnerId);
		if (contractpartner != null) {
			try {
				this.contractpartnerDao.deleteContractpartner(groupId.getId(), contractpartnerId.getId());

				this.evictContractpartnerCache(userId, contractpartnerId);

				final ContractpartnerChangedEvent event = new ContractpartnerChangedEvent(this, EventType.DELETE,
						contractpartner);
				super.publishEvent(event);
			} catch (final Exception e) {
				log.log(Level.INFO, STILL_REFERENCED, e);
				throw new BusinessException(STILL_REFERENCED, ErrorCode.CONTRACTPARTNER_IN_USE);
			}
		}
	}

	private void evictContractpartnerCache(final UserID userId, final ContractpartnerID contractpartnerId) {
		if (contractpartnerId != null) {
			this.accessRelationService.getAllUserWithSameGroup(userId).forEach(evictingUserId -> {
				super.evictFromCache(CacheNames.CONTRACTPARTNER_BY_ID,
						new SimpleKey(evictingUserId, contractpartnerId));
				super.evictFromCache(CacheNames.ALL_CONTRACTPARTNER, evictingUserId);
			});
		}
	}

	@Override
	public <T extends IHasContractpartner & IHasUser> void enrichEntity(final T entity) {
		final User user = entity.getUser();
		final Contractpartner contractpartner = entity.getContractpartner();

		if (contractpartner != null && user != null) {
			final var fullMcp = this.getContractpartnerById(user.getId(), contractpartner.getId());
			entity.setContractpartner(fullMcp);
		}
	}
}
