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

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;

import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.IHasGroup;
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

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Named
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Log
public class GroupService extends AbstractService implements IGroupService {
	private static final String STILL_REFERENCED = "You may not delete a group while there where/are users assigned to it!";
	private final GroupDao groupDao;
	private final GroupDataMapper groupDataMapper;

	@Override
	public ValidationResult validateGroup(@NonNull final Group group) {
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
	public Group getGroupById(@NonNull final GroupID groupId) {
		final Supplier<Group> supplier = () -> this.groupDataMapper
				.mapBToA(this.groupDao.getGroupById(groupId.getId()));

		return super.getFromCacheOrExecute(CacheNames.GROUP_BY_ID, groupId, supplier, Group.class);
	}

	@Override
	public List<Group> getAllGroups() {
		final Supplier<List<Group>> supplier = () -> this.groupDataMapper.mapBToA(this.groupDao.getAllGroups());

		return super.getListFromCacheOrExecute(CacheNames.ALL_GROUPS, CacheNames.ALL_GROUPS, supplier);
	}

	@Override
	public Group getGroupByName(@NonNull final String name) {
		final GroupData groupData = this.groupDao.getGroupByName(name);
		return this.groupDataMapper.mapBToA(groupData);
	}

	@Override
	public void updateGroup(@NonNull final Group group) {
		final ValidationResult validationResult = this.validateGroup(group);
		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().getFirst();
			throw new BusinessException("Group update failed!", validationResultItem.getError());
		}
		final GroupData groupData = this.groupDataMapper.mapAToB(group);
		this.groupDao.updateGroup(groupData);
		this.evictGroupCache(group.getId());
	}

	@Override
	public GroupID createGroup(@NonNull final Group group) {
		group.setId(null);
		final ValidationResult validationResult = this.validateGroup(group);
		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().getFirst();
			throw new BusinessException("Group creation failed!", validationResultItem.getError());
		}
		final GroupData groupData = this.groupDataMapper.mapAToB(group);
		final Long groupId = this.groupDao.createGroup(groupData);
		this.evictGroupCache(new GroupID(groupId));
		return new GroupID(groupId);
	}

	@Override
	public void deleteGroup(@NonNull final GroupID groupId) {
		try {
			this.groupDao.deleteGroup(groupId.getId());
			this.evictGroupCache(groupId);
		} catch (final Exception e) {
			log.log(Level.INFO, STILL_REFERENCED, e);
			throw new BusinessException(STILL_REFERENCED, ErrorCode.GROUP_IN_USE);
		}
	}

	private void evictGroupCache(final GroupID groupId) {
		if (groupId != null) {
			super.evictFromCache(CacheNames.GROUP_BY_ID, groupId);
			super.clearCache(CacheNames.ALL_GROUPS);
		}
	}

	@Override
	public <T extends IHasGroup> void enrichEntity(final T entity) {
		if (entity.getGroup() != null) {
			final var fullMag = this.getGroupById(entity.getGroup().getId());
			entity.setGroup(fullMag);
		}
	}
}
