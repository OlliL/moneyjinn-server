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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.IHasGroup;
import org.laladev.moneyjinn.model.IHasUser;
import org.laladev.moneyjinn.model.access.AccessID;
import org.laladev.moneyjinn.model.access.AccessRelation;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.service.CacheNames;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.IGroupService;
import org.laladev.moneyjinn.service.dao.AccessRelationDao;
import org.laladev.moneyjinn.service.dao.data.AccessRelationData;
import org.laladev.moneyjinn.service.dao.data.mapper.AccessRelationDataMapper;
import org.springframework.util.Assert;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;

@Named
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class AccessRelationService extends AbstractService implements IAccessRelationService {
	private static final String USER_ID_MUST_NOT_BE_NULL = "UserId must not be null!";
	private static final String ACCESS_RELATION_MUST_NOT_BE_NULL = "AccessRelation must not be null!";
	private final AccessRelationDao accessRelationDao;
	private final IGroupService groupService;
	private final AccessRelationDataMapper accessRelationDataMapper;

	public LocalDate now() {
		return LocalDate.now();
	}

	@Override
	public ValidationResult validateAccessRelation(final AccessRelation accessRelation) {
		Assert.notNull(accessRelation, ACCESS_RELATION_MUST_NOT_BE_NULL);

		final ValidationResult validationResult = new ValidationResult();
		final Consumer<ErrorCode> addResult = (final ErrorCode errorCode) -> validationResult.addValidationResultItem(
				new ValidationResultItem(accessRelation.getId(), errorCode));

		if (accessRelation.getGroupID() == null) {
			addResult.accept(ErrorCode.GROUP_MUST_BE_SPECIFIED);
		}

		if (accessRelation.getValidFrom() == null) {
			addResult.accept(ErrorCode.VALIDFROM_NOT_DEFINED);
		}

		return validationResult;
	}

	@Override
	public Group getCurrentGroup(final UserID userId) {
		return this.getGroup(userId, this.now());
	}

	@Override
	public Group getGroup(final UserID userId, final LocalDate date) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(date, "Date must not be null!");
		final List<AccessRelation> accessRelations = this.getAllAccessRelationsById(userId);
		for (final AccessRelation accessRelation : accessRelations) {
			if (!(date.isBefore(accessRelation.getValidFrom()) || date.isAfter(accessRelation.getValidTil()))) {
				return this.groupService.getGroupById(accessRelation.getGroupID());
			}
		}
		return null;
	}

	@Override
	public List<AccessRelation> getAllAccessRelationsById(final UserID userId) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);

		final Supplier<List<AccessRelation>> supplier = () -> this.accessRelationDataMapper.mapBToA(
				this.accessRelationDao.getAllAccessRelationsById(userId.getId()));

		return super.getListFromCacheOrExecute(CacheNames.ALL_ACCESS_RELATIONS_BY_USER_ID, userId, supplier);
	}

	@Override
	public ValidationResult setAccessRelationForExistingUser(final AccessRelation accessRelation) {
		Assert.notNull(accessRelation, ACCESS_RELATION_MUST_NOT_BE_NULL);
		final ValidationResult validationResult = this.validateAccessRelation(accessRelation);
		if (accessRelation.getValidFrom().isBefore(this.now())) {
			validationResult.addValidationResultItem(
					new ValidationResultItem(accessRelation.getId(), ErrorCode.VALIDFROM_EARLIER_THAN_TOMORROW));
		}
		if (validationResult.isValid()) {
			this.setAccessRelation(accessRelation);
		}
		return validationResult;
	}

	@Override
	public ValidationResult setAccessRelationForNewUser(final AccessRelation accessRelation) {
		Assert.notNull(accessRelation, ACCESS_RELATION_MUST_NOT_BE_NULL);
		final ValidationResult validationResult = this.validateAccessRelation(accessRelation);
		if (validationResult.isValid()) {
			this.setAccessRelation(accessRelation);
		}
		return validationResult;
	}

	@Override
	public AccessRelation getCurrentAccessRelationById(final UserID userId) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		final AccessRelationData accessRelationData = this.accessRelationDao.getAccessRelationById(userId.getId(),
				this.now());
		return this.accessRelationDataMapper.mapBToA(accessRelationData);
	}

	@Override
	public void deleteAllAccessRelation(final UserID useriId) {
		Assert.notNull(useriId, USER_ID_MUST_NOT_BE_NULL);
		this.evictAccessRelationCache(useriId);
		this.accessRelationDao.deleteAllAccessRelation(useriId.getId());
	}

	@Override
	public Set<UserID> getAllUserWithSameGroup(final UserID userId) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		final Set<Long> accessIdList = this.accessRelationDao.getAllUserWithSameGroup(userId.getId());
		return accessIdList.stream().map(UserID::new).collect(Collectors.toSet());
	}

	private List<AccessRelation> getAllAccessRelationsByIdDate(final AccessID accessRelationId, final LocalDate date) {
		final List<AccessRelationData> accessRelationDataList = this.accessRelationDao
				.getAllAccessRelationsByIdDate(accessRelationId.getId(), date);
		return this.accessRelationDataMapper.mapBToA(accessRelationDataList);
	}

	private void setAccessRelation(final AccessRelation accessRelation) {
		this.evictAccessRelationCache(accessRelation.getId());
		if (accessRelation.getValidTil() == null) {
			accessRelation.setValidTil(MAX_DATE);
		}
		LocalDate previousValidTil = accessRelation.getValidFrom().minusDays(1);
		final List<AccessRelation> currentAccessRelations = this.getAllAccessRelationsByIdDate(accessRelation.getId(),
				previousValidTil);
		final List<AccessRelation> updateAccessRelationItems = new ArrayList<>();
		final List<AccessRelation> deleteAccessRelationItems = new ArrayList<>();
		AccessRelation previousCurrentAccessRelation = null;
		final AccessRelation insertAccessRelation = new AccessRelation(accessRelation);

		// new user, no existing relations
		boolean addAccessRelation = true;
		if (!currentAccessRelations.isEmpty()) {
			// existing user
			addAccessRelation = false;

			// @formatter:off
			// @non-java-start
      /**
       *============================================================================================
       * Time Frame changes which have to be supported
       *============================================================================================
       *   current:   | 1                           |
       *   change:                | 2               |
       *   result:    | 1        || 2               | (a) end 1, add 2 after 1
       *============================================================================================
       *   current:   | 1        || 2               |
       *   change:                             | 1  |
       *   result:    | 1        || 2         || 1  | (b) end 2, add another 1 after 2
       *============================================================================================
       *   current:   | 1        || 2         || 1  |
       *   change:                       | 2        |
       *   result:    | 1        || 2         || 1  | (c) ignore 2 as it is within another 2 entry
       *============================================================================================
       *   current:   | 1        || 2         || 1  |
       *   change:                             | 2  |
       *   result:    | 1        || 2               | (d) remove the second 1 and prolong 2
       *============================================================================================
       *   current:   | 1        || 2         || 1  |
       *   change:                | 2               |
       *   result:    | 1        || 2         || 1  | (e) ignore 2 as it starts at the same than
       *                                            |     existing 2
       *============================================================================================
       *   current:   | 1        || 2         || 1  |
       *   change:                       | 1        |
       *   result:    | 1        || 2   || 1        | (f) shorten 2, add another 1 after the
       *                                            |     shortened 2
       *============================================================================================
       *   current:   | 1        || 2   || 1        |
       *   change:           | 2                    |
       *   result:    | 1   || 2        || 1        | (g) shorten the first 1 and move the beginning
       *                                            |     of 2 backward
       *============================================================================================
       *   current:   | 1   || 2        || 1        |
       *   change:           | 1                    |
       *   result:    | 1                           | (h) remove 2 and the second 1
       *============================================================================================
       *   current:   | 1        || 2         || 1  |
       *   change:                | 3               |
       *   result:    | 1        || 3         || 1  | (i) replace 2 by 3
       * 
       */
			// @non-java-end
			// @formatter:on

			for (final AccessRelation currentAccessRelation : currentAccessRelations) {
				final LocalDate nextValidFrom = insertAccessRelation.getValidTil().plusDays(1);
				previousValidTil = insertAccessRelation.getValidFrom().minusDays(1);
				if (currentAccessRelation.getValidFrom().isEqual(accessRelation.getValidFrom())) {
					// if there is already a relation which starts at the same day, our new relation
					// gets the same Valid-Til as the original relation has
					if (previousCurrentAccessRelation != null
							&& previousCurrentAccessRelation.getGroupID().equals(accessRelation.getGroupID())) {
						// (d, h) if the access relation which was valid straight before the new
						// access relation is for the same RefId, remove the previous and current
						// relation and modify the new relation to start at the previous and end at
						// the current relation
						insertAccessRelation.setValidTil(currentAccessRelation.getValidTil());
						insertAccessRelation.setValidFrom(previousCurrentAccessRelation.getValidFrom());
						deleteAccessRelationItems.add(previousCurrentAccessRelation);
						deleteAccessRelationItems.add(currentAccessRelation);
						addAccessRelation = true;
					} else if (currentAccessRelation.getGroupID().equals(accessRelation.getGroupID())) {
						// (e) if the new relation starts at the same day than the currently checked
						// one and is also for the same RefId - just ignore it
						addAccessRelation = false;
						break;
					} else {
						insertAccessRelation.setValidTil(currentAccessRelation.getValidTil());
						// (i) if the groups of the previous relation is different and the current
						// relation is also different from the new one, insert a new relation and
						// delete the current relation
						addAccessRelation = true;
						deleteAccessRelationItems.add(currentAccessRelation);
					}
				} else if (currentAccessRelation.getValidFrom().isBefore(accessRelation.getValidFrom())) {
					// (a, b, f, g) if the checked relation starts before the new relation, modify
					// its validtil to just straight before the new one starts if the RefId is
					// different
					if (!currentAccessRelation.getGroupID().equals(accessRelation.getGroupID())) {

						AccessRelation updateAccessRelationItem;
						updateAccessRelationItem = new AccessRelation(currentAccessRelation);
						updateAccessRelationItem.setValidTil(previousValidTil);
						updateAccessRelationItems.add(updateAccessRelationItem);

						insertAccessRelation.setValidTil(currentAccessRelation.getValidTil());
						addAccessRelation = true;
					}
					// (c) nothing to do
				} else if (currentAccessRelation.getValidFrom().isEqual(nextValidFrom) &&
				// (f, i) if the next item after the new access relation has the same refId,
				// delete it and prolong our new item
						currentAccessRelation.getGroupID().equals(accessRelation.getGroupID())) {
					insertAccessRelation.setValidTil(currentAccessRelation.getValidTil());
					deleteAccessRelationItems.add(currentAccessRelation);
				}
				previousCurrentAccessRelation = currentAccessRelation;
			}
		}
		for (final AccessRelation accessRelationItem : deleteAccessRelationItems) {
			this.accessRelationDao.deleteAccessRelationByDate(accessRelationItem.getId().getId(),
					accessRelationItem.getValidFrom());
		}
		for (final AccessRelation accessRelationItem : updateAccessRelationItems) {
			final AccessRelationData accessRelationData = this.accessRelationDataMapper.mapAToB(accessRelationItem);
			this.accessRelationDao.updateAccessRelation(accessRelationItem.getId().getId(),
					accessRelationItem.getValidFrom(), accessRelationData);
		}
		if (addAccessRelation) {
			final AccessRelationData accessRelationData = this.accessRelationDataMapper.mapAToB(insertAccessRelation);
			this.accessRelationDao.createAccessRelation(accessRelationData);
		}
	}

	private void evictAccessRelationCache(final UserID userId) {
		super.evictFromCache(CacheNames.ALL_ACCESS_RELATIONS_BY_USER_ID, userId);
	}

	@Override
	public <T extends IHasGroup & IHasUser> void enrichEntity(final T entity, final LocalDate date) {
		final User user = entity.getUser();
		if (user != null && date != null) {
			final var fullMag = this.getGroup(user.getId(), date);
			entity.setGroup(fullMag);
		}
	}
}
