package org.laladev.moneyjinn.businesslogic.service.impl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

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
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;

@Named
@EnableCaching
public class AccessRelationService extends AbstractService implements IAccessRelationService {

	@Inject
	private AccessRelationDao accessRelationDao;
	@Inject
	private CacheManager cacheManager;
	@Inject
	private IGroupService groupService;

	@Override
	protected void addBeanMapper() {
		super.registerBeanMapper(new AccessRelationDataMapper());
		super.registerBeanMapper(new AccessFlattenedDataMapper());

	}

	@Override
	public ValidationResult validateAccessRelation(final AccessRelation accessRelation) {
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
	@Cacheable(value = CacheNames.ALL_ACCESS_RELATIONS_BY_USER_ID, key = "#accessId.id.concat('-').concat(LocalDate.now().toString())")
	public Group getAccessor(final AccessID accessId) {
		return this.getAccessor(accessId, LocalDate.now());
	}

	@Override
	@Cacheable(value = CacheNames.ALL_ACCESS_RELATIONS_BY_USER_ID, key = "#accessId.id.concat('-').concat(#date.toString())")
	public Group getAccessor(final AccessID accessId, final LocalDate date) {
		final List<Group> groups = this.getAllUserGroupsByUserIdDate(accessId, date);
		Group accessor = null;

		if (!groups.isEmpty()) {
			accessor = groups.get(0);
		}

		return accessor;
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
			final Group groupById = this.groupService.getGroupById((GroupID) accessRelation.getId());
			if (groupById != null) {
				groupList.add(groupById);
			}
		}
		return groupList;

	}

	@Override
	@Cacheable(value = CacheNames.ALL_ACCESS_RELATIONS_BY_USER_ID, key = "#accessId.id")
	public List<AccessRelation> getAllAccessRelationsById(final AccessID accessId) {
		final List<AccessRelationData> accessRelationDataList = this.accessRelationDao
				.getAllAccessRelationsById(accessId.getId());
		return super.mapList(accessRelationDataList, AccessRelation.class);
	}

	@Override
	public ValidationResult setAccessRelationForExistingUser(final AccessRelation accessRelation) {
		final ValidationResult validationResult = this.validateAccessRelation(accessRelation);
		if (accessRelation.getValidFrom().isBefore(LocalDate.now())) {
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
		final ValidationResult validationResult = this.validateAccessRelation(accessRelation);
		if (validationResult.isValid()) {
			this.setAccessRelation(accessRelation);
		}
		return validationResult;
	}

	@Override
	public AccessRelation getAccessRelationById(final AccessID accessRelationID) {
		return this.getAccessRelationById(accessRelationID, LocalDate.now());
	}

	@Override
	public AccessRelation getAccessRelationById(final AccessID accessRelationID, final LocalDate date) {
		final AccessRelationData accessRelationData = this.accessRelationDao
				.getAccessRelationById(accessRelationID.getId(), Date.valueOf(date));
		return super.map(accessRelationData, AccessRelation.class);
	}

	public List<AccessRelation> getAllAccessRelationsByIdDate(final AccessID userId, final LocalDate date) {
		final List<AccessRelationData> accessRelationDataList = this.accessRelationDao
				.getAllAccessRelationsByIdDate(userId.getId(), Date.valueOf(date));
		return super.mapList(accessRelationDataList, AccessRelation.class);
	}

	@Override
	public void deleteAllAccessRelation(final AccessID accessRelationID) {
		this.evictAccessRelationCache(accessRelationID);
		this.accessRelationDao.deleteAllAccessFlattened(accessRelationID.getId());
		this.accessRelationDao.deleteAllAccessRelation(accessRelationID.getId());
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
		} catch (final CloneNotSupportedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
							e.printStackTrace();
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
		final Cache cache = this.cacheManager.getCache(CacheNames.ALL_ACCESS_RELATIONS_BY_USER_ID);
		cache.evict(accessID.getId());
	}

}
