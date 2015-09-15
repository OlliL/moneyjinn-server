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

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.laladev.moneyjinn.businesslogic.dao.AccessRelationDao;
import org.laladev.moneyjinn.businesslogic.dao.data.AccessFlattenedData;
import org.laladev.moneyjinn.businesslogic.dao.data.AccessRelationData;
import org.laladev.moneyjinn.businesslogic.dao.data.mapper.AccessFlattenedDataMapper;
import org.laladev.moneyjinn.businesslogic.dao.data.mapper.AccessRelationDataMapper;
import org.laladev.moneyjinn.businesslogic.model.ErrorCode;
import org.laladev.moneyjinn.businesslogic.model.access.AccessID;
import org.laladev.moneyjinn.businesslogic.model.access.AccessRelation;
import org.laladev.moneyjinn.businesslogic.model.access.Group;
import org.laladev.moneyjinn.businesslogic.model.access.GroupID;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResult;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.businesslogic.service.CacheNames;
import org.laladev.moneyjinn.businesslogic.service.api.IAccessRelationService;
import org.laladev.moneyjinn.businesslogic.service.api.IGroupService;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.util.Assert;

@Named
@EnableCaching
public class AccessRelationService extends AbstractService implements IAccessRelationService {

	private final Log LOG = LogFactory.getLog(this.getClass());

	@Inject
	private AccessRelationDao accessRelationDao;
	@Inject
	private IGroupService groupService;

	@Override
	protected void addBeanMapper() {
		super.registerBeanMapper(new AccessRelationDataMapper());
		super.registerBeanMapper(new AccessFlattenedDataMapper());

	}

	public LocalDate now() {
		return LocalDate.now();
	}

	@Override
	public ValidationResult validateAccessRelation(final AccessRelation accessRelation) {
		Assert.notNull(accessRelation);
		final ValidationResult validationResult = new ValidationResult();

		if (accessRelation.getParentAccessRelation() == null) {
			validationResult.addValidationResultItem(
					new ValidationResultItem(accessRelation.getId(), ErrorCode.GROUP_MUST_BE_SPECIFIED));
		}
		if (accessRelation.getValidFrom() == null) {
			validationResult.addValidationResultItem(
					new ValidationResultItem(accessRelation.getId(), ErrorCode.VALIDFROM_NOT_DEFINED));

		}

		return validationResult;
	}

	@Override
	public Group getAccessor(final AccessID accessId) {
		return this.getAccessor(accessId, this.now());
	}

	@Override
	public Group getAccessor(final AccessID accessId, final LocalDate date) {
		Assert.notNull(accessId);
		Assert.notNull(date);

		final Cache cache = super.getCache(CacheNames.ACCESS_RELATION_BY_USER_ID_AND_DATE, accessId.getId().toString());
		Group group = cache.get(date, Group.class);

		if (group == null) {
			final List<Group> groups = this.getAllUserGroupsByUserIdDate(accessId, date);

			if (!groups.isEmpty()) {
				group = groups.get(0);
				cache.put(date, group);
			}
		}

		return group;
	}

	@Override
	@Cacheable(value = CacheNames.ALL_ACCESS_RELATIONS_BY_USER_ID)
	public List<AccessRelation> getAllAccessRelationsById(final AccessID accessId) {
		Assert.notNull(accessId);
		final List<AccessRelationData> accessRelationDataList = this.accessRelationDao
				.getAllAccessRelationsById(accessId.getId());
		return super.mapList(accessRelationDataList, AccessRelation.class);
	}

	@Override
	public ValidationResult setAccessRelationForExistingUser(final AccessRelation accessRelation) {
		Assert.notNull(accessRelation);
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
		Assert.notNull(accessRelation);
		final ValidationResult validationResult = this.validateAccessRelation(accessRelation);
		if (validationResult.isValid()) {
			this.setAccessRelation(accessRelation);
		}
		return validationResult;
	}

	@Override
	public AccessRelation getAccessRelationById(final AccessID accessRelationId) {
		Assert.notNull(accessRelationId);
		return this.getAccessRelationById(accessRelationId, this.now());
	}

	@Override
	public AccessRelation getAccessRelationById(final AccessID accessRelationId, final LocalDate date) {
		Assert.notNull(accessRelationId);
		Assert.notNull(date);
		final AccessRelationData accessRelationData = this.accessRelationDao
				.getAccessRelationById(accessRelationId.getId(), Date.valueOf(date));
		return super.map(accessRelationData, AccessRelation.class);
	}

	@Override
	public void deleteAllAccessRelation(final AccessID accessRelationId) {
		Assert.notNull(accessRelationId);
		this.evictAccessRelationCache(accessRelationId);
		this.accessRelationDao.deleteAllAccessFlattened(accessRelationId.getId());
		this.accessRelationDao.deleteAllAccessRelation(accessRelationId.getId());
	}

	private List<Group> getAllUserGroupsByUserIdDate(final AccessID accessId, final LocalDate date) {
		final List<Group> groupList = new ArrayList<Group>();
		final Date dateSQL = Date.valueOf(date);
		AccessRelationData accessRelationData = this.accessRelationDao.getAccessRelationById(accessId.getId(), dateSQL);
		AccessRelation accessRelation = super.map(accessRelationData, AccessRelation.class);
		while (accessRelation.getParentAccessRelation() != null
				&& accessRelation.getParentAccessRelation().getId().getId() != 0l) {
			accessRelationData = this.accessRelationDao
					.getAccessRelationById(accessRelation.getParentAccessRelation().getId().getId(), dateSQL);
			accessRelation = super.map(accessRelationData, AccessRelation.class);
			final Group groupById = this.groupService.getGroupById(new GroupID(accessRelation.getId().getId()));
			if (groupById != null) {
				groupList.add(groupById);
			}
		}
		return groupList;

	}

	private List<AccessRelation> getAllAccessRelationsByIdDate(final AccessID accessRelationId, final LocalDate date) {
		final List<AccessRelationData> accessRelationDataList = this.accessRelationDao
				.getAllAccessRelationsByIdDate(accessRelationId.getId(), Date.valueOf(date));
		return super.mapList(accessRelationDataList, AccessRelation.class);
	}

	private void setAccessRelation(final AccessRelation accessRelation) {

		this.evictAccessRelationCache(accessRelation.getId());

		if (accessRelation.getValidTil() == null) {
			// TODO Enviornment for "max year"
			accessRelation.setValidTil(LocalDate.parse("2999-12-31"));
		}

		LocalDate previousValidTil = accessRelation.getValidFrom().minusDays(1);
		final List<AccessRelation> currentAccessRelations = this.getAllAccessRelationsByIdDate(accessRelation.getId(),
				previousValidTil);

		final List<AccessRelation> updateAccessRelationItems = new ArrayList<>();
		final List<AccessRelation> deleteAccessRelationItems = new ArrayList<>();
		AccessRelation previousCurrentAccessRelation = null;

		AccessRelation insertAccessRelation = null;
		try {
			insertAccessRelation = accessRelation.clone();
		} catch (final CloneNotSupportedException e) {
			this.LOG.error(e);
		}

		// new user, no existing relations
		boolean addAccessRelation = true;

		if (!currentAccessRelations.isEmpty()) {
			// existing user
			addAccessRelation = false;

			// @formatter:off
			//=================================================================================================================================
			// Time Frame changes which have to be supported
			//=================================================================================================================================
			//   current:   | 1                                          |
			//   change:                     | 2                         |
			//   new:       | 1             || 2                         | (a) end 1, add 2 after 1
			//=================================================================================================================================
			//   current:   | 1             || 2                         |
			//   change:                                    | 1          |
			//   new:       | 1             || 2           || 1          | (b) end 2, add another 1 after 2
			//=================================================================================================================================
			//   current:   | 1             || 2           || 1          |
			//   change:                              | 2                |
			//   new:       | 1             || 2           || 1          | (c) ignore 2 as it is within another 2 entry
			//=================================================================================================================================
			//   current:   | 1             || 2           || 1          |
			//   change:                                    | 2          |
			//   new:       | 1             || 2                         | (d) remove the second 1 and prolong 2
			//=================================================================================================================================
			//   current:   | 1             || 2           || 1          |
			//   change:                     | 2                         |
			//   new:       | 1             || 2           || 1          | (e) ignore 2 as it starts at the same than existing 2
			//=================================================================================================================================
			//   current:   | 1             || 2           || 1          |
			//   change:                              | 1                |
			//   new:       | 1             || 2     || 1                | (f) shorten 2, add another 1 after the shortened 2
			//=================================================================================================================================
			//   current:   | 1             || 2     || 1                |
			//   change:                | 2                              |
			//   new:       | 1        || 2          || 1                | (g) shorten the first 1 and move the beginning of 2 backward
			//=================================================================================================================================
			//   current:   | 1        || 2          || 1                |
			//   change:                | 1                              |
			//   new:       | 1                                          | (h) remove 2 and the second 1
			//=================================================================================================================================
			//   current:   | 1             || 2           || 1          |
			//   change:                     | 3                         |
			//   new:       | 1             || 4           || 1          | (i) replace 2 by 3
			// @formatter:on

			for (final AccessRelation currentAccessRelation : currentAccessRelations) {
				final LocalDate nextValidFrom = insertAccessRelation.getValidTil().plusDays(1);
				previousValidTil = insertAccessRelation.getValidFrom().minusDays(1);

				if (currentAccessRelation.getValidFrom().isEqual(accessRelation.getValidFrom())) {
					// if there is already a relation which starts at the same day, our new relation
					// gets the same Valid-Til as the original relation has
					if (previousCurrentAccessRelation != null && previousCurrentAccessRelation.getParentAccessRelation()
							.getId().equals(accessRelation.getParentAccessRelation().getId())) {
						// (d, h) if the access relation which was valid straight before the new
						// access relation is for the same RefId, remove the previous and current
						// relation and modify the new relation to start at the previous and end at
						// the current relation
						insertAccessRelation.setValidTil(currentAccessRelation.getValidTil());
						insertAccessRelation.setValidFrom(previousCurrentAccessRelation.getValidFrom());
						deleteAccessRelationItems.add(previousCurrentAccessRelation);
						deleteAccessRelationItems.add(currentAccessRelation);
						addAccessRelation = true;
					} else if (currentAccessRelation.getParentAccessRelation().getId()
							.equals(accessRelation.getParentAccessRelation().getId())) {
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
					if (!currentAccessRelation.getParentAccessRelation().getId()
							.equals(accessRelation.getParentAccessRelation().getId())) {
						AccessRelation updateAccessRelationItem = null;
						try {
							updateAccessRelationItem = currentAccessRelation.clone();
						} catch (final CloneNotSupportedException e) {
							this.LOG.error(e);
						}
						updateAccessRelationItem.setValidTil(previousValidTil);
						updateAccessRelationItems.add(updateAccessRelationItem);
						// accessRelationItem ['id'] = currentAccessRelation.getId();
						// accessRelationItem ['validfrom'] = currentAccessRelation.getValidFrom();
						// accessRelationItem ['item'] = clone currentAccessRelation;
						// accessRelationItem ['item'].setValidTil( previousValidTil );
						// updateAccessRelationItems [] = accessRelationItem;
						insertAccessRelation.setValidTil(currentAccessRelation.getValidTil());
						addAccessRelation = true;
					}
					// (c) nothing to do
				} else if (currentAccessRelation.getValidFrom().isEqual(nextValidFrom)) {
					// (f, i) if the next item after the new access relation has the same refId,
					// delete it and prolong our new item
					if (currentAccessRelation.getParentAccessRelation().getId()
							.equals(accessRelation.getParentAccessRelation().getId())) {
						insertAccessRelation.setValidTil(currentAccessRelation.getValidTil());
						deleteAccessRelationItems.add(currentAccessRelation);
					}
				}
				previousCurrentAccessRelation = currentAccessRelation;
			}
		}

		LocalDate flattenRelationSince = null;
		for (final AccessRelation accessRelationItem : deleteAccessRelationItems) {
			if (flattenRelationSince == null || accessRelationItem.getValidFrom().isBefore(flattenRelationSince)) {
				flattenRelationSince = accessRelationItem.getValidFrom();
			}
			this.accessRelationDao.deleteAccessRelationByDate(accessRelationItem.getId().getId(),
					Date.valueOf(accessRelationItem.getValidFrom()));
		}
		for (final AccessRelation accessRelationItem : updateAccessRelationItems) {
			if (flattenRelationSince == null || accessRelationItem.getValidFrom().isBefore(flattenRelationSince)) {
				flattenRelationSince = accessRelationItem.getValidFrom();
			}
			final AccessRelationData accessRelationData = super.map(accessRelationItem, AccessRelationData.class);
			this.accessRelationDao.updateAccessRelation(accessRelationItem.getId().getId(),
					Date.valueOf(accessRelationItem.getValidFrom()), accessRelationData);
		}
		if (addAccessRelation) {
			if (flattenRelationSince == null || insertAccessRelation.getValidFrom().isBefore(flattenRelationSince)) {
				flattenRelationSince = insertAccessRelation.getValidFrom();
			}
			final AccessRelationData accessRelationData = super.map(insertAccessRelation, AccessRelationData.class);
			this.accessRelationDao.createAccessRelation(accessRelationData);
		}

		// we have to recreate the access_flattened table beginning with the earlies entry we
		// modified
		if (flattenRelationSince != null) {
			this.redoAccessFlattened(accessRelation.getId(), flattenRelationSince);
		}
	}

	// does currently only support 1 user . 1 group . "group-0"
	private void redoAccessFlattened(final AccessID userId, final LocalDate date) {
		final List<AccessRelation> accessRelations = this.getAllAccessRelationsByIdDate(userId, date);
		this.accessRelationDao.deleteAccessFlattenedAfter(userId.getId(), Date.valueOf(date));
		for (final AccessRelation accessRelation : accessRelations) {
			final AccessFlattenedData accessFlattened = super.map(accessRelation, AccessFlattenedData.class);
			this.accessRelationDao.createAccessFlattened(accessFlattened);
		}
	}

	private void evictAccessRelationCache(final AccessID accessID) {
		final Cache cache = super.getCache(CacheNames.ALL_ACCESS_RELATIONS_BY_USER_ID);
		cache.evict(accessID);
	}

}
