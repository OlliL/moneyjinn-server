//Copyright (c) 2015 Oliver Lehmann <lehmann@ans-netz.de>
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.laladev.moneyjinn.core.rest.model.transport.GroupTransport;
import org.laladev.moneyjinn.core.rest.model.transport.UserTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.core.rest.model.user.AbstractCreateUserResponse;
import org.laladev.moneyjinn.core.rest.model.user.AbstractShowUserResponse;
import org.laladev.moneyjinn.core.rest.model.user.AbstractUpdateUserResponse;
import org.laladev.moneyjinn.core.rest.model.user.CreateUserRequest;
import org.laladev.moneyjinn.core.rest.model.user.CreateUserResponse;
import org.laladev.moneyjinn.core.rest.model.user.GetUserSettingsForStartupResponse;
import org.laladev.moneyjinn.core.rest.model.user.ShowCreateUserResponse;
import org.laladev.moneyjinn.core.rest.model.user.ShowDeleteUserResponse;
import org.laladev.moneyjinn.core.rest.model.user.ShowEditUserResponse;
import org.laladev.moneyjinn.core.rest.model.user.ShowUserListResponse;
import org.laladev.moneyjinn.core.rest.model.user.UpdateUserRequest;
import org.laladev.moneyjinn.core.rest.model.user.UpdateUserResponse;
import org.laladev.moneyjinn.core.rest.model.user.transport.AccessRelationTransport;
import org.laladev.moneyjinn.model.access.AccessRelation;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserAttribute;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.access.UserPermission;
import org.laladev.moneyjinn.model.setting.ClientDateFormatSetting;
import org.laladev.moneyjinn.model.setting.ClientDisplayedLanguageSetting;
import org.laladev.moneyjinn.model.setting.ClientMaxRowsSetting;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.server.annotation.RequiresAuthorization;
import org.laladev.moneyjinn.server.annotation.RequiresPermissionAdmin;
import org.laladev.moneyjinn.server.controller.mapper.AccessRelationTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.GroupTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.UserTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.ValidationItemTransportMapper;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.IGroupService;
import org.laladev.moneyjinn.service.api.ISettingService;
import org.laladev.moneyjinn.service.api.IUserService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequestMapping("/moneyflow/server/user/")
public class UserController extends AbstractController {
	private static final String RESTRICTION_ALL = "all";
	@Inject
	private IUserService userService;
	@Inject
	private IAccessRelationService accessRelationService;
	@Inject
	private IGroupService groupService;
	@Inject
	private ISettingService settingService;

	@Override
	protected void addBeanMapper() {
		this.registerBeanMapper(new UserTransportMapper());
		this.registerBeanMapper(new GroupTransportMapper());
		this.registerBeanMapper(new AccessRelationTransportMapper());
		this.registerBeanMapper(new ValidationItemTransportMapper());

	}

	@RequestMapping(value = "showEditUser/{id}", method = { RequestMethod.GET })
	@RequiresAuthorization
	@RequiresPermissionAdmin
	public ShowEditUserResponse showEditUser(@PathVariable(value = "id") final Long userId) {
		final ShowEditUserResponse response = new ShowEditUserResponse();
		this.fillAbstractShowUserResponse(new UserID(userId), response);
		return response;
	}

	@RequestMapping(value = "updateUser", method = { RequestMethod.PUT })
	@RequiresAuthorization
	@RequiresPermissionAdmin
	public UpdateUserResponse updateUser(@RequestBody final UpdateUserRequest request) {
		final User user = super.map(request.getUserTransport(), User.class);

		final ValidationResult validationResultUser = this.userService.validateUser(user);
		final ValidationResult validationResult = new ValidationResult();
		validationResult.mergeValidationResult(validationResultUser);

		final AccessRelation accessRelation = super.map(request.getAccessRelationTransport(), AccessRelation.class);
		if (accessRelation != null) {
			final ValidationResult validationResultAccess = this.accessRelationService
					.validateAccessRelation(accessRelation);
			validationResult.mergeValidationResult(validationResultAccess);
		}

		if (validationResult.isValid()) {
			this.userService.updateUser(user);
			if (user.getPassword() != null) {
				this.userService.resetPassword(user.getId(), user.getPassword());
			}
			if (accessRelation != null) {
				validationResult.mergeValidationResult(
						this.accessRelationService.setAccessRelationForExistingUser(accessRelation));
			}
		}

		if (!validationResult.isValid()) {
			// TODO Rollback
			final UpdateUserResponse response = new UpdateUserResponse();
			this.fillAbstractUpdateUserResponse(user.getId(), response);
			response.setResult(validationResult.isValid());
			response.setValidationItemTransports(
					super.mapList(validationResult.getValidationResultItems(), ValidationItemTransport.class));
			return response;
		}

		return null;
	}

	@RequestMapping(value = "showUserList", method = { RequestMethod.GET })
	@RequiresAuthorization
	@RequiresPermissionAdmin
	public ShowUserListResponse showUserList() {
		return this.showUserList(null);
	}

	@RequestMapping(value = "showUserList/{restriction}", method = { RequestMethod.GET })
	@RequiresAuthorization
	@RequiresPermissionAdmin
	public ShowUserListResponse showUserList(@PathVariable(value = "restriction") final String restriction) {
		final UserID userId = super.getUserId();

		List<User> users = null;
		if (restriction != null) {
			if (restriction.equals(String.valueOf(RESTRICTION_ALL))) {
				users = this.userService.getAllUsers();
			} else if (restriction.length() == 1) {
				users = this.userService.getAllUsersByInitial(restriction.toCharArray()[0]);
			}
		} else {
			final ClientMaxRowsSetting clientMaxRowsSetting = this.settingService.getClientMaxRowsSetting(userId);
			final Integer count = this.userService.countAllUsers();

			if (clientMaxRowsSetting.getSetting().compareTo(count) >= 0) {
				users = this.userService.getAllUsers();
			}
		}

		final ShowUserListResponse response = new ShowUserListResponse();
		final Set<Group> groupSet = new HashSet<>();

		final List<AccessRelation> accessRelationList = new ArrayList<>();
		if (users != null && !users.isEmpty()) {
			for (final User user : users) {
				final AccessRelation accessRelation = this.accessRelationService.getAccessRelationById(user.getId());
				if (accessRelation != null) {
					accessRelationList.add(accessRelation);
					if (accessRelation.getParentAccessRelation() != null) {
						final Group group = this.groupService
								.getGroupById(new GroupID(accessRelation.getParentAccessRelation().getId().getId()));
						groupSet.add(group);
					}
				}
			}

			response.setUserTransports(super.mapList(users, UserTransport.class));
			response.setAccessRelationTransports(super.mapList(accessRelationList, AccessRelationTransport.class));
			response.setGroupTransports(super.mapList(new ArrayList<>(groupSet), GroupTransport.class));
		}

		final Set<Character> initials = this.userService.getAllUserInitials();
		response.setInitials(initials);

		return response;
	}

	@RequestMapping(value = "showCreateUser", method = { RequestMethod.GET })
	@RequiresAuthorization
	@RequiresPermissionAdmin
	public ShowCreateUserResponse showCreateUser() {
		final ShowCreateUserResponse response = new ShowCreateUserResponse();
		this.fillAbstractCreateUserResponse(response);
		return response;
	}

	@RequestMapping(value = "createUser", method = { RequestMethod.POST })
	@RequiresAuthorization
	@RequiresPermissionAdmin
	public CreateUserResponse createUser(@RequestBody final CreateUserRequest request) {
		final User user = super.map(request.getUserTransport(), User.class);
		user.setId(null);

		final ValidationResult validationResultUser = this.userService.validateUser(user);
		final ValidationResult validationResult = new ValidationResult();
		validationResult.mergeValidationResult(validationResultUser);

		final AccessRelation accessRelation = super.map(request.getAccessRelationTransport(), AccessRelation.class);
		if (accessRelation != null) {
			accessRelation.setId(null);

			final ValidationResult validationResultAccess = this.accessRelationService
					.validateAccessRelation(accessRelation);
			validationResult.mergeValidationResult(validationResultAccess);
		}

		if (!validationResult.isValid()) {
			final CreateUserResponse response = new CreateUserResponse();
			this.fillAbstractCreateUserResponse(response);
			response.setResult(validationResult.isValid());
			response.setValidationItemTransports(
					super.mapList(validationResult.getValidationResultItems(), ValidationItemTransport.class));
			return response;
		}

		final UserID newUserId = this.userService.createUser(user);
		if (newUserId != null) {
			this.settingService.initSettings(newUserId);
			if (accessRelation != null) {
				accessRelation.setId(newUserId);
				this.accessRelationService.setAccessRelationForNewUser(accessRelation);
			}
		}
		return null;
	}

	@RequestMapping(value = "deleteUserById/{id}", method = { RequestMethod.DELETE })
	@RequiresAuthorization
	@RequiresPermissionAdmin
	public void deleteUserById(@PathVariable(value = "id") final Long id) {
		final UserID userId = new UserID(id);
		this.accessRelationService.deleteAllAccessRelation(userId);
		this.settingService.deleteSettings(userId);
		this.userService.deleteUser(userId);
	}

	@RequestMapping(value = "getUserSettingsForStartup/{name}", method = { RequestMethod.GET })
	@RequiresAuthorization
	public GetUserSettingsForStartupResponse getUserSettingsForStartup(
			@PathVariable(value = "name") final String name) {
		final User user = this.userService.getUserByName(name);
		final GetUserSettingsForStartupResponse response = new GetUserSettingsForStartupResponse();

		if (user != null) {
			final UserID userId = user.getId();

			response.setUserId(userId.getId());

			final ClientDateFormatSetting clientDateFormatSetting = this.settingService
					.getClientDateFormatSetting(userId);
			final ClientDisplayedLanguageSetting clientDisplayedLanguageSetting = this.settingService
					.getClientDisplayedLanguageSetting(userId);
			response.setSettingDateFormat(clientDateFormatSetting.getSetting());
			response.setSettingDisplayedLanguage(clientDisplayedLanguageSetting.getSetting());
			response.setAttributeNew(user.getAttributes().contains(UserAttribute.IS_NEW));
			response.setPermissionAdmin(user.getPermissions().contains(UserPermission.ADMIN));
		}

		return response;
	}

	@RequestMapping(value = "showDeleteUser/{id}", method = { RequestMethod.GET })
	@RequiresAuthorization
	@RequiresPermissionAdmin
	public ShowDeleteUserResponse showDeleteUser(@PathVariable(value = "id") final Long userId) {
		final ShowDeleteUserResponse response = new ShowDeleteUserResponse();
		this.fillAbstractShowUserResponse(new UserID(userId), response);
		return response;
	}

	private void fillAbstractCreateUserResponse(final AbstractCreateUserResponse response) {
		final List<Group> groups = this.groupService.getAllGroups();
		final List<GroupTransport> groupTransports = super.mapList(groups, GroupTransport.class);

		response.setGroupTransports(groupTransports);
	}

	private void fillAbstractUpdateUserResponse(final UserID userId, final AbstractUpdateUserResponse response) {
		final List<AccessRelation> accessRelations = this.accessRelationService.getAllAccessRelationsById(userId);
		final List<AccessRelationTransport> accessRelationTransports = super.mapList(accessRelations,
				AccessRelationTransport.class);

		response.setAccessRelationTransports(accessRelationTransports);
		this.fillAbstractCreateUserResponse(response);
	}

	private void fillAbstractShowUserResponse(final UserID userId, final AbstractShowUserResponse response) {
		final User user = this.userService.getUserById(userId);
		if (user != null) {
			final UserTransport userTransport = super.map(user, UserTransport.class);
			response.setUserTransport(userTransport);

			this.fillAbstractUpdateUserResponse(user.getId(), response);
		}
	}

}
