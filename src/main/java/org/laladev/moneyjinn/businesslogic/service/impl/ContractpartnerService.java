package org.laladev.moneyjinn.businesslogic.service.impl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.laladev.moneyjinn.businesslogic.dao.ContractpartnerDao;
import org.laladev.moneyjinn.businesslogic.dao.data.ContractpartnerData;
import org.laladev.moneyjinn.businesslogic.dao.data.mapper.ContractpartnerDataMapper;
import org.laladev.moneyjinn.businesslogic.model.Contractpartner;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerID;
import org.laladev.moneyjinn.businesslogic.model.ErrorCode;
import org.laladev.moneyjinn.businesslogic.model.PostingAccount;
import org.laladev.moneyjinn.businesslogic.model.PostingAccountID;
import org.laladev.moneyjinn.businesslogic.model.access.GroupID;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.exception.BusinessException;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResult;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.businesslogic.service.CacheNames;
import org.laladev.moneyjinn.businesslogic.service.api.IContractpartnerService;
import org.laladev.moneyjinn.businesslogic.service.api.IPostingAccountService;
import org.laladev.moneyjinn.businesslogic.service.api.IUserService;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.util.Assert;

@Named
@EnableCaching
public class ContractpartnerService extends AbstractService implements IContractpartnerService {
	private static final Log LOG = LogFactory.getLog(ContractpartnerService.class);

	@Inject
	private ContractpartnerDao contractpartnerDao;
	@Inject
	private IUserService userService;
	@Inject
	private IPostingAccountService postingAccountService;

	@Override
	protected void addBeanMapper() {
		super.registerBeanMapper(new ContractpartnerDataMapper());

	}

	private final Contractpartner mapContractpartnerData(final ContractpartnerData contractpartnerData) {
		if (contractpartnerData != null) {
			final Contractpartner contractpartner = super.map(contractpartnerData, Contractpartner.class);
			final UserID userId = contractpartner.getUser().getId();
			final User user = this.userService.getUserById(userId);
			contractpartner.setUser(user);

			PostingAccount postingAccount = contractpartner.getPostingAccount();
			if (postingAccount != null) {
				final PostingAccountID postingAccountId = postingAccount.getId();
				postingAccount = this.postingAccountService.getPostingAccountById(postingAccountId);
				contractpartner.setPostingAccount(postingAccount);
			}

			return contractpartner;
		}
		return null;
	}

	private final List<Contractpartner> mapContractpartnerDataList(
			final List<ContractpartnerData> contractpartnerDataList) {
		return contractpartnerDataList.stream().map(element -> this.mapContractpartnerData(element))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	/**
	 * This method takes a Contractpartner as argument and sets the properties validFrom and
	 * validTil if they are NULL to default values
	 *
	 * @param contractpartner
	 *            {@link Contractpartner}
	 *
	 * @return Contractpartner
	 */
	private final void prepareContractpartner(final Contractpartner contractpartner) {
		if (contractpartner.getValidFrom() == null) {
			contractpartner.setValidFrom(LocalDate.now());
		}
		if (contractpartner.getValidTil() == null) {
			// TODO Enviornment for "max year"
			contractpartner.setValidTil(LocalDate.parse("2999-12-31"));
		}
	}

	@Override
	public ValidationResult validateContractpartner(final Contractpartner contractpartner) {
		Assert.notNull(contractpartner);
		this.prepareContractpartner(contractpartner);
		final ValidationResult validationResult = new ValidationResult();

		if (contractpartner.getValidTil().isBefore(contractpartner.getValidFrom())) {
			validationResult.addValidationResultItem(
					new ValidationResultItem(contractpartner.getId(), ErrorCode.VALIDFROM_AFTER_VALIDTIL));
		} else if (contractpartner.getId() != null) {
			// update existing Contractpartner
			if (this.contractpartnerDao.checkContractpartnerInUseOutOfDate(contractpartner.getUser().getId().getId(),
					contractpartner.getId().getId(), Date.valueOf(contractpartner.getValidFrom()),
					Date.valueOf(contractpartner.getValidTil()))) {
				validationResult.addValidationResultItem(new ValidationResultItem(contractpartner.getId(),
						ErrorCode.MONEYFLOWS_OUTSIDE_VALIDITY_PERIOD));
			}
		}

		if (contractpartner.getName() == null || contractpartner.getName().trim().isEmpty()) {
			validationResult.addValidationResultItem(
					new ValidationResultItem(contractpartner.getId(), ErrorCode.NAME_MUST_NOT_BE_EMPTY));
		} else {
			final Contractpartner checkContractpartner = this
					.getContractpartnerByName(contractpartner.getUser().getId(), contractpartner.getName());
			if (checkContractpartner != null) {
				// new Contractpartner || update existing Contractpartner
				if (contractpartner.getId() == null || !checkContractpartner.getId().equals(contractpartner.getId())) {
					validationResult.addValidationResultItem(
							new ValidationResultItem(contractpartner.getId(), ErrorCode.NAME_ALREADY_EXISTS));
				}
			}
		}

		return validationResult;
	}

	@Override
	@Cacheable(CacheNames.CONTRACTPARTNER_BY_ID)
	public Contractpartner getContractpartnerById(final UserID userId, final ContractpartnerID contractpartnerId) {
		Assert.notNull(userId);
		Assert.notNull(contractpartnerId);
		final ContractpartnerData contractpartnerData = this.contractpartnerDao.getContractpartnerById(userId.getId(),
				contractpartnerId.getId());
		return this.mapContractpartnerData(contractpartnerData);
	}

	@Override
	public Set<Character> getAllContractpartnerInitials(final UserID userId) {
		Assert.notNull(userId);
		return this.contractpartnerDao.getAllContractpartnerInitials(userId.getId());
	}

	@Override
	public Set<Character> getAllContractpartnerInitialsByDateRange(final UserID userId, final LocalDate validFrom,
			final LocalDate validTil) {
		Assert.notNull(userId);
		Assert.notNull(validFrom);
		Assert.notNull(validTil);
		return this.contractpartnerDao.getAllContractpartnerInitialsByDateRange(userId.getId(), Date.valueOf(validFrom),
				Date.valueOf(validTil));
	}

	@Override
	public Integer countAllContractpartners(final UserID userId) {
		Assert.notNull(userId);
		return this.contractpartnerDao.countAllContractpartners(userId.getId());
	}

	@Override
	public Integer countAllContractpartnersByDateRange(final UserID userId, final LocalDate validFrom,
			final LocalDate validTil) {
		Assert.notNull(userId);
		Assert.notNull(validFrom);
		Assert.notNull(validTil);
		return this.contractpartnerDao.countAllContractpartnersByDateRange(userId.getId(), Date.valueOf(validFrom),
				Date.valueOf(validTil));
	}

	@Override
	@Cacheable(CacheNames.ALL_CONTRACTPARTNER)
	public List<Contractpartner> getAllContractpartners(final UserID userId) {
		Assert.notNull(userId);
		final List<ContractpartnerData> contractpartnerDataList = this.contractpartnerDao
				.getAllContractpartners(userId.getId());
		return this.mapContractpartnerDataList(contractpartnerDataList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Contractpartner> getAllContractpartnersByDateRange(final UserID userId, final LocalDate validFrom,
			final LocalDate validTil) {
		Assert.notNull(userId);
		Assert.notNull(validFrom);
		Assert.notNull(validTil);

		final Cache cache = super.getCache(CacheNames.ALL_CONTRACTPARTNER_BY_DATE, userId.getId().toString());
		List<Contractpartner> contractpartners = null;
		final SimpleKey key = new SimpleKey(validFrom, validTil);
		contractpartners = cache.get(key, List.class);

		if (contractpartners == null) {
			final List<ContractpartnerData> contractpartnerDataList = this.contractpartnerDao
					.getAllContractpartnersByDateRange(userId.getId(), Date.valueOf(validFrom), Date.valueOf(validTil));
			contractpartners = this.mapContractpartnerDataList(contractpartnerDataList);
			cache.put(key, contractpartners);
		}

		return contractpartners;
	}

	@Override
	public List<Contractpartner> getAllContractpartnersByInitial(final UserID userId, final Character initial) {
		Assert.notNull(userId);
		Assert.notNull(initial);
		final List<ContractpartnerData> contractpartnerDataList = this.contractpartnerDao
				.getAllContractpartnersByInitial(userId.getId(), initial);
		return this.mapContractpartnerDataList(contractpartnerDataList);
	}

	@Override
	public List<Contractpartner> getAllContractpartnersByInitialAndDateRange(final UserID userId,
			final Character initial, final LocalDate validFrom, final LocalDate validTil) {
		Assert.notNull(userId);
		Assert.notNull(validFrom);
		Assert.notNull(validTil);
		Assert.notNull(initial);
		final List<ContractpartnerData> contractpartnerDataList = this.contractpartnerDao
				.getAllContractpartnersByInitialAndDateRange(userId.getId(), initial, Date.valueOf(validFrom),
						Date.valueOf(validTil));
		return this.mapContractpartnerDataList(contractpartnerDataList);
	}

	@Override
	public Contractpartner getContractpartnerByName(final UserID userId, final String name) {
		Assert.notNull(userId);
		Assert.notNull(name);
		final ContractpartnerData contractpartnerData = this.contractpartnerDao.getContractpartnerByName(userId.getId(),
				name);
		return this.mapContractpartnerData(contractpartnerData);
	}

	@Override
	public void updateContractpartner(final Contractpartner contractpartner) {
		Assert.notNull(contractpartner);
		final ValidationResult validationResult = this.validateContractpartner(contractpartner);

		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("Contractpartner update failed!", validationResultItem.getError());
		}

		final ContractpartnerData contractpartnerData = super.map(contractpartner, ContractpartnerData.class);
		this.contractpartnerDao.updateContractpartner(contractpartnerData);
		this.evictContractpartnerCache(contractpartner.getUser().getId(), contractpartner.getId());
	}

	@Override
	public void createContractpartner(final Contractpartner contractpartner) {
		Assert.notNull(contractpartner);
		contractpartner.setId(null);
		final ValidationResult validationResult = this.validateContractpartner(contractpartner);

		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("Contractpartner creation failed!", validationResultItem.getError());
		}

		final ContractpartnerData contractpartnerData = super.map(contractpartner, ContractpartnerData.class);
		final Long contractpartnerId = this.contractpartnerDao.createContractpartner(contractpartnerData);
		this.evictContractpartnerCache(contractpartner.getUser().getId(), new ContractpartnerID(contractpartnerId));
	}

	@Override
	public void deleteContractpartner(final UserID userId, final GroupID groupId,
			final ContractpartnerID contractpartnerId) {
		Assert.notNull(userId);
		Assert.notNull(groupId);
		Assert.notNull(contractpartnerId);
		try {
			this.contractpartnerDao.deleteContractpartner(groupId.getId(), contractpartnerId.getId());
			this.evictContractpartnerCache(userId, contractpartnerId);
		} catch (final Exception e) {
			LOG.info(e);
			throw new BusinessException(
					"You may not delete a contractual partner who is still referenced by a flow of money!",
					ErrorCode.CONTRACTPARTNER_IN_USE);
		}

	}

	private void evictContractpartnerCache(final UserID userId, final ContractpartnerID contractpartnerId) {
		if (contractpartnerId != null) {
			final Cache allContractpartnersCache = super.getCache(CacheNames.ALL_CONTRACTPARTNER);
			final Cache contractpartnerByIdCache = super.getCache(CacheNames.CONTRACTPARTNER_BY_ID);
			final Cache allContractpartnerByCDateCache = super.getCache(CacheNames.ALL_CONTRACTPARTNER_BY_DATE,
					userId.getId().toString());
			if (allContractpartnersCache != null) {
				allContractpartnersCache.evict(userId);
			}
			if (contractpartnerByIdCache != null) {
				contractpartnerByIdCache.evict(new SimpleKey(userId, contractpartnerId));
			}
			if (allContractpartnerByCDateCache != null) {
				allContractpartnerByCDateCache.clear();
			}
		}
	}

}
