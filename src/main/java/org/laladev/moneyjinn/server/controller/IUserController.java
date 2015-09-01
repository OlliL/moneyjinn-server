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

public interface IUserController {

	public ShowEditUserResponse showEditUser(final Long userId);

	public UpdateUserResponse updateUser(final UpdateUserRequest request);

	public ShowUserListResponse showUserList();

	public ShowUserListResponse showUserList(final String restriction);

	public ShowCreateUserResponse showCreateUser();

	public CreateUserResponse createUser(final CreateUserRequest request);

	public void deleteUserById(final Long userId);

	public GetUserSettingsForStartupResponse getUserSettingsForStartup(final String name);

	public ShowDeleteUserResponse showDeleteUser(final Long id);

}