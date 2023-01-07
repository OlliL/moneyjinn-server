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

import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.access.AccessID;
import org.laladev.moneyjinn.model.access.AccessRelation;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.exception.TechnicalException;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.service.CacheNames;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.IGroupService;
import org.laladev.moneyjinn.service.dao.AccessRelationDao;
import org.laladev.moneyjinn.service.dao.data.AccessFlattenedData;
import org.laladev.moneyjinn.service.dao.data.AccessRelationData;
import org.laladev.moneyjinn.service.dao.data.mapper.AccessFlattenedDataMapper;
import org.laladev.moneyjinn.service.dao.data.mapper.AccessRelationDataMapper;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.util.Assert;

@Named
@EnableCaching
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class AccessRelationService extends AbstractService implements IAccessRelationService {
  private static final Log LOG = LogFactory.getLog(AccessRelationService.class);
  private final AccessRelationDao accessRelationDao;
  private final IGroupService groupService;

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
    Assert.notNull(accessRelation, "AccessRelation must not be null!");
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
    Assert.notNull(accessId, "AccessId must not be null!");
    Assert.notNull(date, "Date must not be null!");
    final List<AccessRelation> accessRelations = this.getAllAccessRelationsById(accessId);
    for (final AccessRelation accessRelation : accessRelations) {
      if (!(date.isBefore(accessRelation.getValidFrom())
          || date.isAfter(accessRelation.getValidTil()))) {
        final Group groupById = this.groupService
            .getGroupById(new GroupID(accessRelation.getParentAccessRelation().getId().getId()));
        return groupById;
      }
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<AccessRelation> getAllAccessRelationsById(final AccessID accessId) {
    Assert.notNull(accessId, "AccessId must not be null!");
    final Cache cache = super.getCache(CacheNames.ALL_ACCESS_RELATIONS_BY_USER_ID);
    List<AccessRelation> result = cache.get(accessId.getId(), List.class);
    if (result == null) {
      final List<AccessRelationData> accessRelationDataList = this.accessRelationDao
          .getAllAccessRelationsById(accessId.getId());
      result = super.mapList(accessRelationDataList, AccessRelation.class);
      cache.put(accessId.getId(), result);
    }
    return result;
  }

  @Override
  public ValidationResult setAccessRelationForExistingUser(final AccessRelation accessRelation) {
    Assert.notNull(accessRelation, "AccessRelation must not be null!");
    final ValidationResult validationResult = this.validateAccessRelation(accessRelation);
    if (accessRelation.getValidFrom().isBefore(this.now())) {
      validationResult.addValidationResultItem(new ValidationResultItem(accessRelation.getId(),
          ErrorCode.VALIDFROM_EARLIER_THAN_TOMORROW));
    }
    if (validationResult.isValid()) {
      this.setAccessRelation(accessRelation);
    }
    return validationResult;
  }

  @Override
  public ValidationResult setAccessRelationForNewUser(final AccessRelation accessRelation) {
    Assert.notNull(accessRelation, "AccessRelation must not be null!");
    final ValidationResult validationResult = this.validateAccessRelation(accessRelation);
    if (validationResult.isValid()) {
      this.setAccessRelation(accessRelation);
    }
    return validationResult;
  }

  @Override
  public AccessRelation getAccessRelationById(final AccessID accessRelationId) {
    Assert.notNull(accessRelationId, "AccessRelationId must not be null!");
    return this.getAccessRelationById(accessRelationId, this.now());
  }

  @Override
  public AccessRelation getAccessRelationById(final AccessID accessRelationId,
      final LocalDate date) {
    Assert.notNull(accessRelationId, "AccessRelationId must not be null!");
    Assert.notNull(date, "Date must not be null!");
    final AccessRelationData accessRelationData = this.accessRelationDao
        .getAccessRelationById(accessRelationId.getId(), date);
    return super.map(accessRelationData, AccessRelation.class);
  }

  @Override
  public void deleteAllAccessRelation(final AccessID accessRelationId) {
    Assert.notNull(accessRelationId, "AccessRelationId must not be null!");
    this.evictAccessRelationCache(accessRelationId);
    this.accessRelationDao.deleteAllAccessFlattened(accessRelationId.getId());
    this.accessRelationDao.deleteAllAccessRelation(accessRelationId.getId());
  }

  @Override
  public Set<UserID> getAllUserWithSameGroup(final AccessID userId) {
    Assert.notNull(userId, "UserId must not be null!");
    final Set<Long> accessIdList = this.accessRelationDao.getAllUserWithSameGroup(userId.getId());
    return accessIdList.stream().map(UserID::new).collect(Collectors.toSet());
  }

  private List<AccessRelation> getAllAccessRelationsByIdDate(final AccessID accessRelationId,
      final LocalDate date) {
    final List<AccessRelationData> accessRelationDataList = this.accessRelationDao
        .getAllAccessRelationsByIdDate(accessRelationId.getId(), date);
    return super.mapList(accessRelationDataList, AccessRelation.class);
  }

  private void setAccessRelation(final AccessRelation accessRelation) {
    this.evictAccessRelationCache(accessRelation.getId());
    if (accessRelation.getValidTil() == null) {
      accessRelation.setValidTil(MAX_DATE);
    }
    LocalDate previousValidTil = accessRelation.getValidFrom().minusDays(1);
    final List<AccessRelation> currentAccessRelations = this
        .getAllAccessRelationsByIdDate(accessRelation.getId(), previousValidTil);
    final List<AccessRelation> updateAccessRelationItems = new ArrayList<>();
    final List<AccessRelation> deleteAccessRelationItems = new ArrayList<>();
    AccessRelation previousCurrentAccessRelation = null;
    AccessRelation insertAccessRelation;
    try {
      insertAccessRelation = accessRelation.clone();
    } catch (final CloneNotSupportedException e) {
      LOG.error(e);
      throw new TechnicalException("Clone Not Supported", ErrorCode.UNKNOWN);
    }
    // new user, no existing relations
    boolean addAccessRelation = true;
    if (!currentAccessRelations.isEmpty()) {
      // existing user
      addAccessRelation = false;
      // @formatter:off
      // @non-java-start
      //============================================================================================
      // Time Frame changes which have to be supported
      //============================================================================================
      //   current:   | 1                           |
      //   change:                | 2               |
      //   new:       | 1        || 2               | (a) end 1, add 2 after 1
      //============================================================================================
      //   current:   | 1        || 2               |
      //   change:                             | 1  |
      //   new:       | 1        || 2         || 1  | (b) end 2, add another 1 after 2
      //============================================================================================
      //   current:   | 1        || 2         || 1  |
      //   change:                       | 2        |
      //   new:       | 1        || 2         || 1  | (c) ignore 2 as it is within another 2 entry
      //============================================================================================
      //   current:   | 1        || 2         || 1  |
      //   change:                             | 2  |
      //   new:       | 1        || 2               | (d) remove the second 1 and prolong 2
      //============================================================================================
      //   current:   | 1        || 2         || 1  |
      //   change:                | 2               |
      //   new:       | 1        || 2         || 1  | (e) ignore 2 as it starts at the same than
      //                                            |     existing 2
      //============================================================================================
      //   current:   | 1        || 2         || 1  |
      //   change:                       | 1        |
      //   new:       | 1        || 2   || 1        | (f) shorten 2, add another 1 after the
      //                                            |     shortened 2
      //============================================================================================
      //   current:   | 1        || 2   || 1        |
      //   change:           | 2                    |
      //   new:       | 1   || 2        || 1        | (g) shorten the first 1 and move the beginning
      //                                            |     of 2 backward
      //============================================================================================
      //   current:   | 1   || 2        || 1        |
      //   change:           | 1                    |
      //   new:       | 1                           | (h) remove 2 and the second 1
      //============================================================================================
      //   current:   | 1        || 2         || 1  |
      //   change:                | 3               |
      //   new:       | 1        || 3         || 1  | (i) replace 2 by 3
      // @non-java-end
      // @formatter:on
      for (final AccessRelation currentAccessRelation : currentAccessRelations) {
        final LocalDate nextValidFrom = insertAccessRelation.getValidTil().plusDays(1);
        previousValidTil = insertAccessRelation.getValidFrom().minusDays(1);
        if (currentAccessRelation.getValidFrom().isEqual(accessRelation.getValidFrom())) {
          // if there is already a relation which starts at the same day, our new relation
          // gets the same Valid-Til as the original relation has
          if (previousCurrentAccessRelation != null
              && previousCurrentAccessRelation.getParentAccessRelation().getId()
                  .equals(accessRelation.getParentAccessRelation().getId())) {
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
            AccessRelation updateAccessRelationItem;
            try {
              updateAccessRelationItem = currentAccessRelation.clone();
              updateAccessRelationItem.setValidTil(previousValidTil);
              updateAccessRelationItems.add(updateAccessRelationItem);
            } catch (final CloneNotSupportedException e) {
              LOG.error(e);
              throw new TechnicalException("Clone Not Supported", ErrorCode.UNKNOWN);
            }
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
      if (flattenRelationSince == null
          || accessRelationItem.getValidFrom().isBefore(flattenRelationSince)) {
        flattenRelationSince = accessRelationItem.getValidFrom();
      }
      this.accessRelationDao.deleteAccessRelationByDate(accessRelationItem.getId().getId(),
          accessRelationItem.getValidFrom());
    }
    for (final AccessRelation accessRelationItem : updateAccessRelationItems) {
      if (flattenRelationSince == null
          || accessRelationItem.getValidFrom().isBefore(flattenRelationSince)) {
        flattenRelationSince = accessRelationItem.getValidFrom();
      }
      final AccessRelationData accessRelationData = super.map(accessRelationItem,
          AccessRelationData.class);
      this.accessRelationDao.updateAccessRelation(accessRelationItem.getId().getId(),
          accessRelationItem.getValidFrom(), accessRelationData);
    }
    if (addAccessRelation) {
      if (flattenRelationSince == null
          || insertAccessRelation.getValidFrom().isBefore(flattenRelationSince)) {
        flattenRelationSince = insertAccessRelation.getValidFrom();
      }
      final AccessRelationData accessRelationData = super.map(insertAccessRelation,
          AccessRelationData.class);
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
    this.accessRelationDao.deleteAccessFlattenedAfter(userId.getId(), date);
    for (final AccessRelation accessRelation : accessRelations) {
      final AccessFlattenedData accessFlattened = super.map(accessRelation,
          AccessFlattenedData.class);
      this.accessRelationDao.createAccessFlattened(accessFlattened);
    }
  }

  private void evictAccessRelationCache(final AccessID accessId) {
    final Cache cache = super.getCache(CacheNames.ALL_ACCESS_RELATIONS_BY_USER_ID);
    cache.evict(accessId.getId());
  }
}
