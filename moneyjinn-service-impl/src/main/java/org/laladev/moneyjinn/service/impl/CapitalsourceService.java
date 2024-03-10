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
import org.laladev.moneyjinn.model.BankAccount;
import org.laladev.moneyjinn.model.IHasCapitalsource;
import org.laladev.moneyjinn.model.IHasGroup;
import org.laladev.moneyjinn.model.IHasUser;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceState;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceType;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.service.CacheNames;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.IGroupService;
import org.laladev.moneyjinn.service.api.IUserService;
import org.laladev.moneyjinn.service.dao.CapitalsourceDao;
import org.laladev.moneyjinn.service.dao.data.CapitalsourceData;
import org.laladev.moneyjinn.service.dao.data.mapper.CapitalsourceDataMapper;
import org.laladev.moneyjinn.service.event.CapitalsourceChangedEvent;
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
public class CapitalsourceService extends AbstractService implements ICapitalsourceService {
	private static final String STILL_REFERENCED = "You may not delete a source of capital while it is referenced by a flow of money!";
	private static final String USER_ID_MUST_NOT_BE_NULL = "UserId must not be null!";
	private static final String CAPITALSOURCE_MUST_NOT_BE_NULL = "Capitalsource must not be null!";
	private final CapitalsourceDao capitalsourceDao;
	private final IUserService userService;
	private final IGroupService groupService;
	private final IAccessRelationService accessRelationService;
	private final CapitalsourceDataMapper capitalsourceDataMapper;

	@Override
	@PostConstruct
	protected void addBeanMapper() {
		super.registerBeanMapper(this.capitalsourceDataMapper);
	}

	private Capitalsource mapCapitalsourceData(final CapitalsourceData capitalsourceData) {
		if (capitalsourceData != null) {
			final Capitalsource capitalsource = super.map(capitalsourceData, Capitalsource.class);

			this.userService.enrichEntity(capitalsource);
			this.groupService.enrichEntity(capitalsource);

			return capitalsource;
		}
		return null;
	}

	private List<Capitalsource> mapCapitalsourceDataList(final List<CapitalsourceData> capitalsourceDataList) {
		return capitalsourceDataList.stream().map(this::mapCapitalsourceData).toList();
	}

	/**
	 * This method takes a Capitalsource as argument and sets the properties
	 * validFrom and validTil if they are NULL to default values as well as type and
	 * state.
	 *
	 * @param capitalsource {@link Capitalsource}
	 */
	private void prepareCapitalsource(final Capitalsource capitalsource) {
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
		Assert.notNull(capitalsource, CAPITALSOURCE_MUST_NOT_BE_NULL);
		Assert.notNull(capitalsource.getUser(), "Capitalsource.user must not be null!");
		Assert.notNull(capitalsource.getUser().getId(), "Capitalsource.user.id must not be null!");
		Assert.notNull(capitalsource.getGroup(), "Capitalsource.group must not be null!");
		Assert.notNull(capitalsource.getGroup().getId(), "Capitalsource.group.id must not be null!");

		this.prepareCapitalsource(capitalsource);

		final ValidationResult validationResult = new ValidationResult();
		final Consumer<ErrorCode> addResult = (final ErrorCode errorCode) -> validationResult.addValidationResultItem(
				new ValidationResultItem(capitalsource.getId(), errorCode));

		if (capitalsource.getValidTil().isBefore(capitalsource.getValidFrom())) {
			addResult.accept(ErrorCode.VALIDFROM_AFTER_VALIDTIL);
		} else if (capitalsource.getId() != null) {
			// update existing Capitalsource
			final boolean checkCapitalsourceInUseOutOfDate = this.capitalsourceDao.checkCapitalsourceInUseOutOfDate(
					capitalsource.getUser().getId().getId(), capitalsource.getId().getId(),
					capitalsource.getValidFrom(), capitalsource.getValidTil());
			if (checkCapitalsourceInUseOutOfDate) {
				addResult.accept(ErrorCode.CAPITALSOURCE_IN_USE_PERIOD);
			}
		}

		if (capitalsource.getComment() == null || capitalsource.getComment().isBlank()) {
			addResult.accept(ErrorCode.COMMENT_IS_NOT_SET);
		} else {
			final Capitalsource checkCapitalsource = this.getCapitalsourceByComment(capitalsource.getUser().getId(),
					capitalsource.getComment(), capitalsource.getValidFrom());
			if (checkCapitalsource != null
					&& (capitalsource.getId() == null || !checkCapitalsource.getId().equals(capitalsource.getId()))) {
				// new Capitalsource || update existing Capitalsource
				addResult.accept(ErrorCode.NAME_ALREADY_EXISTS);
			}
		}

		if (capitalsource.getBankAccount() != null) {
			capitalsource.getBankAccount().checkValidity().forEach(addResult);
		}

		return validationResult;
	}

	@Override
	public Capitalsource getCapitalsourceById(final UserID userId, final GroupID groupId,
			final CapitalsourceID capitalsourceId) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(groupId, "GroupId must not be null!");
		Assert.notNull(capitalsourceId, "CapitalsourceId must not be null!");

		final Supplier<Capitalsource> supplier = () -> this.mapCapitalsourceData(
				this.capitalsourceDao.getCapitalsourceById(userId.getId(), groupId.getId(), capitalsourceId.getId()));

		return super.getFromCacheOrExecute(CacheNames.CAPITALSOURCE_BY_ID,
				new SimpleKey(userId, groupId, capitalsourceId), supplier, Capitalsource.class);
	}

	@Override
	public List<Capitalsource> getAllCapitalsources(final UserID userId) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);

		final Supplier<List<Capitalsource>> supplier = () -> this
				.mapCapitalsourceDataList(this.capitalsourceDao.getAllCapitalsources(userId.getId()));

		return super.getListFromCacheOrExecute(CacheNames.ALL_CAPITALSOURCES, userId, supplier, Capitalsource.class);
	}

	@Override
	public List<Capitalsource> getAllCapitalsourcesByDateRange(final UserID userId, final LocalDate validFrom,
			final LocalDate validTil) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(validFrom, "ValidFrom must not be null!");
		Assert.notNull(validTil, "ValidTil must not be null!");
		final List<CapitalsourceData> capitalsourceDataList = this.capitalsourceDao
				.getAllCapitalsourcesByDateRange(userId.getId(), validFrom, validTil);
		return this.mapCapitalsourceDataList(capitalsourceDataList);
	}

	@Override
	public Capitalsource getCapitalsourceByComment(final UserID userId, final String name, final LocalDate date) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(date, "Date must not be null!");
		Assert.notNull(name, "Name must not be null!");
		final CapitalsourceData capitalsourceData = this.capitalsourceDao.getCapitalsourceByComment(userId.getId(),
				name, date);
		return this.mapCapitalsourceData(capitalsourceData);
	}

	@Override
	public void updateCapitalsource(final Capitalsource capitalsource) {
		Assert.notNull(capitalsource, CAPITALSOURCE_MUST_NOT_BE_NULL);
		final ValidationResult validationResult = this.validateCapitalsource(capitalsource);
		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("Capitalsource update failed!", validationResultItem.getError());
		}
		final CapitalsourceData capitalsourceData = super.map(capitalsource, CapitalsourceData.class);
		this.capitalsourceDao.updateCapitalsource(capitalsourceData);
		this.evictCapitalsourceCache(capitalsource.getUser().getId(), capitalsource.getGroup().getId(),
				capitalsource.getId());
		final CapitalsourceChangedEvent event = new CapitalsourceChangedEvent(this, EventType.UPDATE, capitalsource);
		super.publishEvent(event);
	}

	@Override
	public CapitalsourceID createCapitalsource(final Capitalsource capitalsource) {
		Assert.notNull(capitalsource, CAPITALSOURCE_MUST_NOT_BE_NULL);
		capitalsource.setId(null);
		final ValidationResult validationResult = this.validateCapitalsource(capitalsource);
		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("Capitalsource creation failed!", validationResultItem.getError());
		}
		final CapitalsourceData capitalsourceData = super.map(capitalsource, CapitalsourceData.class);
		final Long capitalsourceIdLong = this.capitalsourceDao.createCapitalsource(capitalsourceData);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(capitalsourceIdLong);
		capitalsource.setId(capitalsourceId);

		this.evictCapitalsourceCache(capitalsource.getUser().getId(), capitalsource.getGroup().getId(),
				capitalsourceId);

		final CapitalsourceChangedEvent event = new CapitalsourceChangedEvent(this, EventType.UPDATE, capitalsource);
		super.publishEvent(event);

		return capitalsourceId;
	}

	@Override
	public void deleteCapitalsource(final UserID userId, final GroupID groupId, final CapitalsourceID capitalsourceId) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(groupId, "GroupId must not be null!");
		Assert.notNull(capitalsourceId, "CapitalsourceId must not be null!");

		final Capitalsource capitalsource = this.getCapitalsourceById(userId, groupId, capitalsourceId);
		if (capitalsource != null) {
			try {
				this.capitalsourceDao.deleteCapitalsource(userId.getId(), groupId.getId(), capitalsourceId.getId());

				this.evictCapitalsourceCache(userId, groupId, capitalsourceId);

				final CapitalsourceChangedEvent event = new CapitalsourceChangedEvent(this, EventType.DELETE,
						capitalsource);
				super.publishEvent(event);

			} catch (final Exception e) {
				log.log(Level.INFO, STILL_REFERENCED, e);
				throw new BusinessException(STILL_REFERENCED, ErrorCode.CAPITALSOURCE_STILL_REFERENCED);
			}
		}
	}

	@Override
	public List<Capitalsource> getGroupBookableCapitalsourcesByDateRange(final UserID userId, final LocalDate validFrom,
			final LocalDate validTil) {
		final List<Capitalsource> capitalsources = this.getGroupCapitalsourcesByDateRange(userId, validFrom, validTil);
		return capitalsources.stream().filter(cs -> !cs.getType().equals(CapitalsourceType.CREDIT)).toList();
	}

	@Override
	public List<Capitalsource> getGroupCapitalsourcesByDateRange(final UserID userId, final LocalDate validFrom,
			final LocalDate validTil) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(validFrom, "ValidFrom must not be null!");
		Assert.notNull(validTil, "ValidTil must not be null!");

		final Supplier<List<Capitalsource>> supplier = () -> this.mapCapitalsourceDataList(
				this.capitalsourceDao.getGroupCapitalsourcesByDateRange(userId.getId(), validFrom, validTil));

		return super.getListFromCacheOrExecute(
				super.getCombinedCacheName(CacheNames.GROUP_CAPITALSOURCES_BY_DATE, userId.getId()),
				new SimpleKey(validFrom, validTil), supplier, Capitalsource.class);
	}

	@Override
	public Capitalsource getCapitalsourceByAccount(final UserID userId, final BankAccount bankAccount,
			final LocalDate date) {
		final CapitalsourceData capitalsourceData = this.capitalsourceDao
				.getCapitalsourceByAccount(bankAccount.getBankCode(), bankAccount.getAccountNumber(), date);
		return this.mapCapitalsourceData(capitalsourceData);
	}

	private void evictCapitalsourceCache(final UserID userId, final GroupID groupId,
			final CapitalsourceID capitalsourceId) {
		if (capitalsourceId != null) {
			this.accessRelationService.getAllUserWithSameGroup(userId).forEach(evictingUserId -> {
				super.evictFromCache(CacheNames.CAPITALSOURCE_BY_ID,
						new SimpleKey(evictingUserId, groupId, capitalsourceId));
				super.evictFromCache(CacheNames.ALL_CAPITALSOURCES, evictingUserId);
				super.clearCache(
						super.getCombinedCacheName(CacheNames.GROUP_CAPITALSOURCES_BY_DATE, evictingUserId.getId()));
			});
		}
	}

	@Override
	public <T extends IHasCapitalsource & IHasUser> void enrichEntity(final T entity) {
		final User user = entity.getUser();
		final Capitalsource capitalsource = entity.getCapitalsource();

		if (capitalsource != null && user != null) {
			final GroupID groupId;
			if (entity instanceof final IHasGroup hasGroup && hasGroup.getGroup() != null) {
				groupId = hasGroup.getGroup().getId();
			} else {
				groupId = this.accessRelationService.getCurrentGroup(user.getId()).getId();
			}
			final var fullMcs = this.getCapitalsourceById(user.getId(), groupId, capitalsource.getId());
			entity.setCapitalsource(fullMcs);
		}
	}
}
