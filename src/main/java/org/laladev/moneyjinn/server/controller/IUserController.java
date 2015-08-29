package org.laladev.moneyjinn.server.controller;

import org.laladev.moneyjinn.core.rest.model.user.CreateUserRequest;
import org.laladev.moneyjinn.core.rest.model.user.CreateUserResponse;
import org.laladev.moneyjinn.core.rest.model.user.GetUserSettingsForStartupResponse;
import org.laladev.moneyjinn.core.rest.model.user.ShowCreateUserResponse;
import org.laladev.moneyjinn.core.rest.model.user.ShowDeleteUserResponse;
import org.laladev.moneyjinn.core.rest.model.user.ShowEditUserResponse;
import org.laladev.moneyjinn.core.rest.model.user.ShowUserListResponse;
import org.laladev.moneyjinn.core.rest.model.user.UpdateUserRequest;
import org.laladev.moneyjinn.core.rest.model.user.UpdateUserResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/moneyflow/user/")
public interface IUserController {

	@RequestMapping(value = "showEditUser/{id}", method = { RequestMethod.GET })
	public ShowEditUserResponse showEditUser(final Long userId);

	@RequestMapping(value = "updateUser", method = { RequestMethod.PUT })
	public UpdateUserResponse updateUser(final UpdateUserRequest request);

	@RequestMapping(value = "showUserList", method = { RequestMethod.GET })
	public ShowUserListResponse showUserList();

	@RequestMapping(value = "showUserList/{restriction}", method = { RequestMethod.GET })
	public ShowUserListResponse showUserList(final String restriction);

	@RequestMapping(value = "showCreateUser", method = { RequestMethod.GET })
	public ShowCreateUserResponse showCreateUser();

	@RequestMapping(value = "createUser", method = { RequestMethod.POST })
	public CreateUserResponse createUser(final CreateUserRequest request);

	@RequestMapping(value = "deleteUserById/{id}", method = { RequestMethod.DELETE })
	public void deleteUserById(final Long userId);

	@RequestMapping(value = "getUserSettingsForStartup/{name}", method = { RequestMethod.GET })
	public GetUserSettingsForStartupResponse getUserSettingsForStartup(final String name);

	@RequestMapping(value = "showDeleteUser/{id}", method = { RequestMethod.GET })
	public ShowDeleteUserResponse showDeleteUser(final Long id);

}