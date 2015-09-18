package org.laladev.moneyjinn.server.controller.impl;

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

import javax.inject.Inject;

import org.laladev.moneyjinn.businesslogic.model.access.Group;
import org.laladev.moneyjinn.businesslogic.model.access.GroupID;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientMaxRowsSetting;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResult;
import org.laladev.moneyjinn.businesslogic.service.api.IGroupService;
import org.laladev.moneyjinn.businesslogic.service.api.ISettingService;
import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import org.laladev.moneyjinn.core.rest.model.group.AbstractGroupResponse;
import org.laladev.moneyjinn.core.rest.model.group.CreateGroupRequest;
import org.laladev.moneyjinn.core.rest.model.group.ShowDeleteGroupResponse;
import org.laladev.moneyjinn.core.rest.model.group.ShowEditGroupResponse;
import org.laladev.moneyjinn.core.rest.model.group.ShowGroupListResponse;
import org.laladev.moneyjinn.core.rest.model.group.UpdateGroupRequest;
import org.laladev.moneyjinn.core.rest.model.transport.GroupTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.server.annotation.RequiresAuthorization;
import org.laladev.moneyjinn.server.annotation.RequiresPermissionAdmin;
import org.laladev.moneyjinn.server.controller.mapper.GroupTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.ValidationItemTransportMapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequestMapping("/moneyflow/server/group/")
public class GroupController extends AbstractController {
	@Inject
	private IGroupService groupService;
	@Inject
	private ISettingService settingService;

	@Override
	protected void addBeanMapper() {
		this.registerBeanMapper(new GroupTransportMapper());
		this.registerBeanMapper(new ValidationItemTransportMapper());
	}

	@RequestMapping(value = "showGroupList", method = { RequestMethod.GET })
	@RequiresAuthorization
	@RequiresPermissionAdmin
	public ShowGroupListResponse showGroupList() {
		return this.showGroupList(null);
	}

	@RequestMapping(value = "showGroupList/{restriction}", method = { RequestMethod.GET })
	@RequiresAuthorization
	@RequiresPermissionAdmin
	public ShowGroupListResponse showGroupList(@PathVariable(value = "restriction") final String restriction) {
		final UserID userID = super.getUserId();
		final ClientMaxRowsSetting clientMaxRowsSetting = this.settingService.getClientMaxRowsSetting(userID);
		final Set<Character> initials = this.groupService.getAllGroupInitials();
		final Integer count = this.groupService.countAllGroups();

		List<Group> groups = null;

		if ((restriction != null && restriction.equals(String.valueOf("all")))
				|| (restriction == null && clientMaxRowsSetting.getSetting().compareTo(count) >= 0)) {
			groups = this.groupService.getAllGroups();
		} else if (restriction != null && restriction.length() == 1) {
			groups = this.groupService.getAllGroupsByInitial(restriction.toCharArray()[0]);
		}

		final ShowGroupListResponse response = new ShowGroupListResponse();
		if (groups != null && !groups.isEmpty()) {
			response.setGroupTransports(super.mapList(groups, GroupTransport.class));
		}
		response.setInitials(initials);

		return response;
	}

	@RequestMapping(value = "createGroup", method = { RequestMethod.POST })
	@RequiresAuthorization
	@RequiresPermissionAdmin
	public ValidationResponse createGroup(@RequestBody final CreateGroupRequest request) {
		final Group group = super.map(request.getGroupTransport(), Group.class);
		group.setId(null);

		final ValidationResult validationResult = this.groupService.validateGroup(group);

		if (!validationResult.isValid()) {
			final ValidationResponse response = new ValidationResponse();
			response.setResult(validationResult.isValid());
			response.setValidationItemTransports(super.mapList(validationResult.getValidationResultItems(), ValidationItemTransport.class));
			return response;
		}
		this.groupService.createGroup(group);
		return null;
	}

	@RequestMapping(value = "updateGroup", method = { RequestMethod.PUT })
	@RequiresAuthorization
	@RequiresPermissionAdmin
	public ValidationResponse updateGroup(@RequestBody final UpdateGroupRequest request) {
		final Group group = super.map(request.getGroupTransport(), Group.class);

		final ValidationResult validationResult = this.groupService.validateGroup(group);

		if (validationResult.isValid()) {
			this.groupService.updateGroup(group);
		} else {
			final ValidationResponse response = new ValidationResponse();
			response.setResult(validationResult.isValid());
			response.setValidationItemTransports(super.mapList(validationResult.getValidationResultItems(), ValidationItemTransport.class));
			return response;
		}

		return null;
	}

	@RequestMapping(value = "deleteGroupById/{id}", method = { RequestMethod.DELETE })
	@RequiresAuthorization
	@RequiresPermissionAdmin
	public void deleteGroupById(@PathVariable(value = "id") final Long id) {
		final GroupID groupId = new GroupID(id);
		this.groupService.deleteGroup(groupId);
	}

	@RequestMapping(value = "showEditGroup/{id}", method = { RequestMethod.GET })
	@RequiresAuthorization
	@RequiresPermissionAdmin
	public ShowEditGroupResponse showEditGroup(@PathVariable(value = "id") final Long groupId) {
		final ShowEditGroupResponse response = new ShowEditGroupResponse();
		this.fillAbstractGroupResponse(groupId, response);
		return response;
	}

	@RequestMapping(value = "showDeleteGroup/{id}", method = { RequestMethod.GET })
	@RequiresAuthorization
	@RequiresPermissionAdmin
	public ShowDeleteGroupResponse showDeleteGroup(@PathVariable(value = "id") final Long groupId) {
		final ShowDeleteGroupResponse response = new ShowDeleteGroupResponse();
		this.fillAbstractGroupResponse(groupId, response);
		return response;
	}

	private void fillAbstractGroupResponse(final Long id, final AbstractGroupResponse response) {
		final GroupID groupId = new GroupID(id);
		final Group group = this.groupService.getGroupById(groupId);
		response.setGroupTransport(super.map(group, GroupTransport.class));
	}
}
