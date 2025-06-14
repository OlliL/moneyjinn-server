//Copyright (c) 2015-2025 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.server.controller.impl;

import java.util.List;

import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.server.controller.api.GroupControllerApi;
import org.laladev.moneyjinn.server.controller.mapper.GroupTransportMapper;
import org.laladev.moneyjinn.server.model.CreateGroupRequest;
import org.laladev.moneyjinn.server.model.CreateGroupResponse;
import org.laladev.moneyjinn.server.model.ShowGroupListResponse;
import org.laladev.moneyjinn.server.model.UpdateGroupRequest;
import org.laladev.moneyjinn.service.api.IGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class GroupController extends AbstractController implements GroupControllerApi {
	private final IGroupService groupService;
	private final GroupTransportMapper groupTransportMapper;

	@Override
	@PreAuthorize(HAS_AUTHORITY_ADMIN)
	public ResponseEntity<ShowGroupListResponse> showGroupList() {
		final List<Group> groups = this.groupService.getAllGroups();
		final ShowGroupListResponse response = new ShowGroupListResponse();
		if (groups != null && !groups.isEmpty()) {
			response.setGroupTransports(this.groupTransportMapper.mapAToB(groups));
		}
		return ResponseEntity.ok(response);
	}

	@Override
	@PreAuthorize(HAS_AUTHORITY_ADMIN)
	public ResponseEntity<CreateGroupResponse> createGroup(@RequestBody final CreateGroupRequest request) {
		final Group group = this.groupTransportMapper.mapBToA(request.getGroupTransport());
		group.setId(null);
		final ValidationResult validationResult = this.groupService.validateGroup(group);

		this.throwValidationExceptionIfInvalid(validationResult);

		final GroupID groupId = this.groupService.createGroup(group);

		final CreateGroupResponse response = new CreateGroupResponse();
		response.setGroupId(groupId.getId());
		return ResponseEntity.ok(response);
	}

	@Override
	@PreAuthorize(HAS_AUTHORITY_ADMIN)
	public ResponseEntity<Void> updateGroup(@RequestBody final UpdateGroupRequest request) {
		final Group group = this.groupTransportMapper.mapBToA(request.getGroupTransport());
		final ValidationResult validationResult = this.groupService.validateGroup(group);

		this.throwValidationExceptionIfInvalid(validationResult);

		this.groupService.updateGroup(group);

		return ResponseEntity.noContent().build();
	}

	@Override
	@PreAuthorize(HAS_AUTHORITY_ADMIN)
	public ResponseEntity<Void> deleteGroupById(final Long id) {
		final GroupID groupId = new GroupID(id);

		this.groupService.deleteGroup(groupId);

		return ResponseEntity.noContent().build();
	}

}
