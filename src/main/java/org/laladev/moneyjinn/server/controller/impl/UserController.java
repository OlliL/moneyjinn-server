package org.laladev.moneyjinn.server.controller.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.laladev.moneyjinn.businesslogic.model.access.AccessRelation;
import org.laladev.moneyjinn.businesslogic.model.access.Group;
import org.laladev.moneyjinn.businesslogic.model.access.GroupID;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserAttribute;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.access.UserPermission;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientDateFormatSetting;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientDisplayedLanguageSetting;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientMaxRowsSetting;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResult;
import org.laladev.moneyjinn.businesslogic.service.api.IAccessRelationService;
import org.laladev.moneyjinn.businesslogic.service.api.IGroupService;
import org.laladev.moneyjinn.businesslogic.service.api.ISettingService;
import org.laladev.moneyjinn.businesslogic.service.api.IUserService;
import org.laladev.moneyjinn.core.rest.model.transport.AccessRelationTransport;
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
import org.laladev.moneyjinn.server.controller.IUserController;
import org.laladev.moneyjinn.server.controller.mapper.AccessRelationTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.GroupTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.UserTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.ValidationItemTransportMapper;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController extends AbstractController implements IUserController {
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

	@Override
	public ShowEditUserResponse showEditUser(@PathVariable(value = "id") final Long userId) {
		final ShowEditUserResponse response = new ShowEditUserResponse();
		this.fillAbstractShowUserResponse(new UserID(userId), response);
		return response;
	}

	@Override
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

	@Override
	public ShowUserListResponse showUserList() {
		return this.showUserList(null);
	}

	@Override
	public ShowUserListResponse showUserList(@PathVariable(value = "restriction") final String restriction) {
		final UserID userId = super.getUserId();
		final ClientMaxRowsSetting clientMaxRowsSetting = this.settingService.getClientMaxRowsSetting(userId);
		final List<Character> initials = this.userService.getAllUserInitials();
		final Integer count = this.userService.countAllUsers();

		List<User> users = null;

		if ((restriction != null && restriction.equals(String.valueOf("all")))
				|| (restriction == null && clientMaxRowsSetting.getSetting().compareTo(count) >= 0)) {
			users = this.userService.getAllUsers();
		} else if (restriction != null && restriction.length() == 1) {
			users = this.userService.getAllUsersByInitial(restriction.toCharArray()[0]);
		}

		final ShowUserListResponse response = new ShowUserListResponse();
		final Set<Group> groupSet = new HashSet<Group>();
		final List<AccessRelation> accessRelationList = new ArrayList<AccessRelation>();
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
			response.setGroupTransports(super.mapList(new ArrayList<Group>(groupSet), GroupTransport.class));
		}
		response.setInitials(initials);

		return response;
	}

	@Override
	public ShowCreateUserResponse showCreateUser() {
		final ShowCreateUserResponse response = new ShowCreateUserResponse();
		this.fillAbstractCreateUserResponse(response);
		return response;
	}

	@Override
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
		} else {
			final UserID newUserID = this.userService.createUser(user);
			if (newUserID != null) {
				this.settingService.initSettings(newUserID);
				if (accessRelation != null) {
					accessRelation.setId(newUserID);
					this.accessRelationService.setAccessRelationForNewUser(accessRelation);
				}
			}
		}
		return null;
	}

	@Override
	public void deleteUserById(@PathVariable(value = "id") final Long id) {
		final UserID userId = new UserID(id);
		this.accessRelationService.deleteAllAccessRelation(userId);
		this.settingService.deleteSettings(userId);
		this.userService.deleteUser(userId);
	}

	@Override
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

	@Override
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
