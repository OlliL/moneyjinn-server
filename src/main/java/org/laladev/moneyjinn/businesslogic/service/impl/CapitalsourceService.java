//
// Copyright (c) 2015-2016 Oliver Lehmann <oliver@laladev.org>
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

package org.laladev.moneyjinn.businesslogic.service.impl;

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
import org.laladev.moneyjinn.businesslogic.dao.CapitalsourceDao;
import org.laladev.moneyjinn.businesslogic.dao.data.CapitalsourceData;
import org.laladev.moneyjinn.businesslogic.dao.data.mapper.CapitalsourceDataMapper;
import org.laladev.moneyjinn.businesslogic.model.BankAccount;
import org.laladev.moneyjinn.businesslogic.model.access.Group;
import org.laladev.moneyjinn.businesslogic.model.access.GroupID;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceState;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceType;
import org.laladev.moneyjinn.businesslogic.model.exception.BusinessException;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResult;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.businesslogic.service.CacheNames;
import org.laladev.moneyjinn.businesslogic.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.businesslogic.service.api.IGroupService;
import org.laladev.moneyjinn.businesslogic.service.api.IUserService;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.util.Assert;

@Named
@EnableCaching
public class CapitalsourceService extends AbstractService implements ICapitalsourceService {
	private static final Log LOG = LogFactory.getLog(CapitalsourceService.class);

	@Inject
	private CapitalsourceDao capitalsourceDao;
	@Inject
	private IUserService userService;
	@Inject
	private IGroupService groupService;

	@Override
	protected void addBeanMapper() {
		super.registerBeanMapper(new CapitalsourceDataMapper());

	}

	private final Capitalsource mapCapitalsourceData(final CapitalsourceData capitalsourceData) {
		if (capitalsourceData != null) {
			final Capitalsource capitalsource = super.map(capitalsourceData, Capitalsource.class);
			final UserID userId = capitalsource.getUser().getId();
			final User user = this.userService.getUserById(userId);
			final Group group = this.groupService.getGroupById(capitalsource.getAccess().getId());
			capitalsource.setUser(user);
			capitalsource.setAccess(group);
			return capitalsource;
		}
		return null;
	}

	private final List<Capitalsource> mapCapitalsourceDataList(final List<CapitalsourceData> capitalsourceDataList) {
		return capitalsourceDataList.stream().map(element -> this.mapCapitalsourceData(element))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	/**
	 * This method takes a Capitalsource as argument and sets the properties validFrom and validTil
	 * if they are NULL to default values as well as type and state.
	 *
	 * @param capitalsource
	 *            {@link Capitalsource}
	 *
	 * @return Capitalsource
	 */
	private final void prepareCapitalsource(final Capitalsource capitalsource) {
		if (capitalsource.getValidFrom() == null) {
			capitalsource.setValidFrom(LocalDate.now());
		}
		if (capitalsource.getValidTil() == null) {
			capitalsource.setValidTil(MAX_DATE);
		}
		if (capitalsource.getType() == null) {
			capitalsource.setType(CapitalsourceType.CURRENT_ASSET);
		}
		if (capitalsource.getState() == null) {
			capitalsource.setState(CapitalsourceState.CACHE);
		}
	}

	@Override
	public ValidationResult validateCapitalsource(final Capitalsource capitalsource) {
		Assert.notNull(capitalsource);
		Assert.notNull(capitalsource.getUser());
		Assert.notNull(capitalsource.getUser().getId());

		this.prepareCapitalsource(capitalsource);
		final ValidationResult validationResult = new ValidationResult();

		if (capitalsource.getValidTil().isBefore(capitalsource.getValidFrom())) {
			validationResult.addValidationResultItem(
					new ValidationResultItem(capitalsource.getId(), ErrorCode.VALIDFROM_AFTER_VALIDTIL));
		} else if (capitalsource.getId() != null) {
			// update existing Capitalsource
			final boolean checkCapitalsourceInUseOutOfDate = this.capitalsourceDao.checkCapitalsourceInUseOutOfDate(
					capitalsource.getUser().getId().getId(), capitalsource.getId().getId(),
					capitalsource.getValidFrom(), capitalsource.getValidTil());
			if (checkCapitalsourceInUseOutOfDate) {
				validationResult.addValidationResultItem(
						new ValidationResultItem(capitalsource.getId(), ErrorCode.CAPITALSOURCE_IN_USE_PERIOD));
			}
		}

		if (capitalsource.getComment() == null || capitalsource.getComment().trim().isEmpty()) {
			validationResult.addValidationResultItem(
					new ValidationResultItem(capitalsource.getId(), ErrorCode.COMMENT_IS_NOT_SET));
		} else {
			final Capitalsource checkCapitalsource = this.getCapitalsourceByComment(capitalsource.getUser().getId(),
					capitalsource.getComment(), capitalsource.getValidFrom());
			if (checkCapitalsource != null
					&& (capitalsource.getId() == null || !checkCapitalsource.getId().equals(capitalsource.getId()))) {
				// new Capitalsource || update existing Capitalsource
				validationResult.addValidationResultItem(
						new ValidationResultItem(capitalsource.getId(), ErrorCode.NAME_ALREADY_EXISTS));
			}
		}

		if (capitalsource.getBankAccount() != null) {
			for (final ErrorCode errorCode : capitalsource.getBankAccount().checkValidity()) {
				validationResult.addValidationResultItem(new ValidationResultItem(capitalsource.getId(), errorCode));
			}
		}

		return validationResult;
	}

	@Override
	@Cacheable(CacheNames.CAPITALSOURCE_BY_ID)
	public Capitalsource getCapitalsourceById(final UserID userId, final GroupID groupId,
			final CapitalsourceID capitalsourceId) {
		Assert.notNull(userId);
		Assert.notNull(groupId);
		Assert.notNull(capitalsourceId);
		final CapitalsourceData capitalsourceData = this.capitalsourceDao.getCapitalsourceById(userId.getId(),
				groupId.getId(), capitalsourceId.getId());
		return this.mapCapitalsourceData(capitalsourceData);
	}

	@Override
	public Set<Character> getAllCapitalsourceInitials(final UserID userId) {
		Assert.notNull(userId);
		return this.capitalsourceDao.getAllCapitalsourceInitials(userId.getId());
	}

	@Override
	public Set<Character> getAllCapitalsourceInitialsByDateRange(final UserID userId, final LocalDate validFrom,
			final LocalDate validTil) {
		Assert.notNull(userId);
		Assert.notNull(validFrom);
		Assert.notNull(validTil);
		return this.capitalsourceDao.getAllCapitalsourceInitialsByDateRange(userId.getId(), validFrom, validTil);
	}

	@Override
	public Integer countAllCapitalsources(final UserID userId) {
		Assert.notNull(userId);
		return this.capitalsourceDao.countAllCapitalsources(userId.getId());
	}

	@Override
	public Integer countAllCapitalsourcesByDateRange(final UserID userId, final LocalDate validFrom,
			final LocalDate validTil) {
		Assert.notNull(userId);
		Assert.notNull(validFrom);
		Assert.notNull(validTil);
		return this.capitalsourceDao.countAllCapitalsourcesByDateRange(userId.getId(), validFrom, validTil);
	}

	@Override
	@Cacheable(CacheNames.ALL_CAPITALSOURCES)
	public List<Capitalsource> getAllCapitalsources(final UserID userId) {
		Assert.notNull(userId);
		final List<CapitalsourceData> capitalsourceDataList = this.capitalsourceDao
				.getAllCapitalsources(userId.getId());
		return this.mapCapitalsourceDataList(capitalsourceDataList);
	}

	@Override
	public List<Capitalsource> getAllCapitalsourcesByDateRange(final UserID userId, final LocalDate validFrom,
			final LocalDate validTil) {
		Assert.notNull(userId);
		Assert.notNull(validFrom);
		Assert.notNull(validTil);
		final List<CapitalsourceData> capitalsourceDataList = this.capitalsourceDao
				.getAllCapitalsourcesByDateRange(userId.getId(), validFrom, validTil);
		return this.mapCapitalsourceDataList(capitalsourceDataList);
	}

	@Override
	public List<Capitalsource> getAllCapitalsourcesByInitial(final UserID userId, final Character initial) {
		Assert.notNull(userId);
		Assert.notNull(initial);
		final List<CapitalsourceData> capitalsourceDataList = this.capitalsourceDao
				.getAllCapitalsourcesByInitial(userId.getId(), initial);
		return this.mapCapitalsourceDataList(capitalsourceDataList);
	}

	@Override
	public List<Capitalsource> getAllCapitalsourcesByInitialAndDateRange(final UserID userId, final Character initial,
			final LocalDate validFrom, final LocalDate validTil) {
		Assert.notNull(userId);
		Assert.notNull(validFrom);
		Assert.notNull(validTil);
		Assert.notNull(initial);
		final List<CapitalsourceData> capitalsourceDataList = this.capitalsourceDao
				.getAllCapitalsourcesByInitialAndDateRange(userId.getId(), initial, validFrom, validTil);
		return this.mapCapitalsourceDataList(capitalsourceDataList);
	}

	@Override
	public Capitalsource getCapitalsourceByComment(final UserID userId, final String name, final LocalDate date) {
		Assert.notNull(userId);
		Assert.notNull(date);
		Assert.notNull(name);
		final CapitalsourceData capitalsourceData = this.capitalsourceDao.getCapitalsourceByComment(userId.getId(),
				name, date);
		return this.mapCapitalsourceData(capitalsourceData);
	}

	@Override
	public void updateCapitalsource(final Capitalsource capitalsource) {
		Assert.notNull(capitalsource);
		final ValidationResult validationResult = this.validateCapitalsource(capitalsource);

		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("Capitalsource update failed!", validationResultItem.getError());
		}

		final CapitalsourceData capitalsourceData = super.map(capitalsource, CapitalsourceData.class);
		this.capitalsourceDao.updateCapitalsource(capitalsourceData);
		this.evictCapitalsourceCache(capitalsource.getUser().getId(), capitalsource.getAccess().getId(),
				capitalsource.getId());
	}

	@Override
	public void createCapitalsource(final Capitalsource capitalsource) {
		Assert.notNull(capitalsource);
		capitalsource.setId(null);
		final ValidationResult validationResult = this.validateCapitalsource(capitalsource);

		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("Capitalsource creation failed!", validationResultItem.getError());
		}

		final CapitalsourceData capitalsourceData = super.map(capitalsource, CapitalsourceData.class);
		final Long capitalsourceId = this.capitalsourceDao.createCapitalsource(capitalsourceData);
		this.evictCapitalsourceCache(capitalsource.getUser().getId(), capitalsource.getAccess().getId(),
				new CapitalsourceID(capitalsourceId));
	}

	@Override
	public void deleteCapitalsource(final UserID userId, final GroupID groupId, final CapitalsourceID capitalsourceId) {
		Assert.notNull(userId);
		Assert.notNull(groupId);
		Assert.notNull(capitalsourceId);
		try {
			this.capitalsourceDao.deleteCapitalsource(userId.getId(), groupId.getId(), capitalsourceId.getId());
			this.evictCapitalsourceCache(userId, groupId, capitalsourceId);
		} catch (final Exception e) {
			LOG.info(e);
			throw new BusinessException(
					"You may not delete a source of capital while it is referenced by a flow of money!",
					ErrorCode.CAPITALSOURCE_STILL_REFERENCED);
		}

	}

	@Override
	public List<Capitalsource> getGroupBookableCapitalsources(final UserID userId) {
		Assert.notNull(userId);
		final List<CapitalsourceData> capitalsourceDataList = this.capitalsourceDao
				.getGroupCapitalsources(userId.getId());
		return this.mapCapitalsourceDataList(capitalsourceDataList).stream()
				.filter(cs -> !cs.getType().equals(CapitalsourceType.CREDIT))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public List<Capitalsource> getGroupBookableCapitalsourcesByDateRange(final UserID userId, final LocalDate validFrom,
			final LocalDate validTil) {
		final List<Capitalsource> capitalsources = this.getGroupCapitalsourcesByDateRange(userId, validFrom, validTil);

		return capitalsources.stream().filter(cs -> !cs.getType().equals(CapitalsourceType.CREDIT))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Capitalsource> getGroupCapitalsourcesByDateRange(final UserID userId, final LocalDate validFrom,
			final LocalDate validTil) {
		Assert.notNull(userId);
		Assert.notNull(validFrom);
		Assert.notNull(validTil);

		final Cache cache = super.getCache(CacheNames.GROUP_CAPITALSOURCES_BY_DATE, userId.getId().toString());
		final SimpleKey key = new SimpleKey(validFrom, validTil);
		List<Capitalsource> capitalsources = cache.get(key, List.class);

		if (capitalsources == null) {
			final List<CapitalsourceData> capitalsourceDataList = this.capitalsourceDao
					.getGroupCapitalsourcesByDateRange(userId.getId(), validFrom, validTil);
			capitalsources = this.mapCapitalsourceDataList(capitalsourceDataList);
			cache.put(key, capitalsources);
		}

		return capitalsources;
	}

	@Override
	public Capitalsource getCapitalsourceByAccount(final UserID userId, final BankAccount bankAccount,
			final LocalDate endOfMonth) {
		final CapitalsourceData capitalsourceData = this.capitalsourceDao
				.getCapitalsourceByAccount(bankAccount.getBankCode(), bankAccount.getAccountNumber(), endOfMonth);

		return this.mapCapitalsourceData(capitalsourceData);
	}

	private void evictCapitalsourceCache(final UserID userId, final GroupID groupId,
			final CapitalsourceID capitalsourceId) {
		if (capitalsourceId != null) {
			final Cache allCapitalsourcesCache = super.getCache(CacheNames.ALL_CAPITALSOURCES);
			final Cache capitalsourceByIdCache = super.getCache(CacheNames.CAPITALSOURCE_BY_ID);
			final Cache groupCapitalsourcesByDateCache = super.getCache(CacheNames.GROUP_CAPITALSOURCES_BY_DATE,
					userId.getId().toString());
			if (allCapitalsourcesCache != null) {
				allCapitalsourcesCache.evict(userId);
			}
			if (capitalsourceByIdCache != null) {
				capitalsourceByIdCache.evict(new SimpleKey(userId, groupId, capitalsourceId));
			}
			if (groupCapitalsourcesByDateCache != null) {
				groupCapitalsourcesByDateCache.clear();
			}
		}
	}
}
