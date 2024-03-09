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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.service.CacheNames;
import org.laladev.moneyjinn.service.api.IGroupService;
import org.laladev.moneyjinn.service.dao.GroupDao;
import org.laladev.moneyjinn.service.dao.data.GroupData;
import org.laladev.moneyjinn.service.dao.data.mapper.GroupDataMapper;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.util.Assert;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;

@Named
@EnableCaching
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class GroupService extends AbstractService implements IGroupService {
	private static final String GROUP_MUST_NOT_BE_NULL = "group must not be null!";
	private static final Log LOG = LogFactory.getLog(GroupService.class);
	private final GroupDao groupDao;
	private final GroupDataMapper croupDataMapper;

	@Override
	@PostConstruct
	protected void addBeanMapper() {
		super.registerBeanMapper(this.croupDataMapper);
	}

	@Override
	public ValidationResult validateGroup(final Group group) {
		Assert.notNull(group, GROUP_MUST_NOT_BE_NULL);

		final ValidationResult validationResult = new ValidationResult();
		final Consumer<ErrorCode> addResult = (final ErrorCode errorCode) -> validationResult.addValidationResultItem(
				new ValidationResultItem(group.getId(), errorCode));

		if (group.getName() == null || group.getName().isBlank()) {
			addResult.accept(ErrorCode.NAME_MUST_NOT_BE_EMPTY);
		} else {
			final Group checkGroup = this.getGroupByName(group.getName());
			// Update OR Create
			if (checkGroup != null && (group.getId() == null || !checkGroup.getId().equals(group.getId()))) {
				addResult.accept(ErrorCode.GROUP_WITH_SAME_NAME_ALREADY_EXISTS);
			}
		}

		return validationResult;
	}

	@Override
	@Cacheable(CacheNames.GROUP_BY_ID)
	public Group getGroupById(final GroupID groupId) {
		Assert.notNull(groupId, "groupId must not be null!");
		final GroupData groupData = this.groupDao.getGroupById(groupId.getId());
		return super.map(groupData, Group.class);
	}

	@Override
	@Cacheable(CacheNames.ALL_GROUPS)
	public List<Group> getAllGroups() {
		final List<GroupData> groupDataList = this.groupDao.getAllGroups();
		return super.mapList(groupDataList, Group.class);
	}

	@Override
	public Group getGroupByName(final String name) {
		Assert.notNull(name, "name must not be null!");
		final GroupData groupData = this.groupDao.getGroupByName(name);
		return super.map(groupData, Group.class);
	}

	@Override
	public void updateGroup(final Group group) {
		Assert.notNull(group, GROUP_MUST_NOT_BE_NULL);
		final ValidationResult validationResult = this.validateGroup(group);
		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("Group update failed!", validationResultItem.getError());
		}
		final GroupData groupData = super.map(group, GroupData.class);
		this.groupDao.updateGroup(groupData);
		this.evictGroupCache(group.getId());
	}

	@Override
	public GroupID createGroup(final Group group) {
		Assert.notNull(group, GROUP_MUST_NOT_BE_NULL);
		group.setId(null);
		final ValidationResult validationResult = this.validateGroup(group);
		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("Group creation failed!", validationResultItem.getError());
		}
		final GroupData groupData = super.map(group, GroupData.class);
		final Long groupId = this.groupDao.createGroup(groupData);
		this.evictGroupCache(new GroupID(groupId));
		return new GroupID(groupId);
	}

	@Override
	public void deleteGroup(final GroupID groupId) {
		Assert.notNull(groupId, "groupId must not be null!");
		try {
			this.groupDao.deleteGroup(groupId.getId());
			this.evictGroupCache(groupId);
		} catch (final Exception e) {
			LOG.info(e);
			throw new BusinessException("You may not delete a group while there where/are users assigned to it!",
					ErrorCode.GROUP_IN_USE);
		}
	}

	private void evictGroupCache(final GroupID groupId) {
		if (groupId != null) {
			final Cache allGroupsCache = super.getCache(CacheNames.ALL_GROUPS);
			final Cache groupByIdCache = super.getCache(CacheNames.GROUP_BY_ID);
			if (allGroupsCache != null) {
				allGroupsCache.clear();
			}
			if (groupByIdCache != null) {
				groupByIdCache.evict(groupId);
			}
		}
	}
}
