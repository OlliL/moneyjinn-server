package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import java.util.ArrayList;
import java.util.Collection;

import org.laladev.moneyjinn.api.IMapper;
import org.laladev.moneyjinn.businesslogic.dao.data.UserData;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserAttribute;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.access.UserPermission;

public class UserDataMapper implements IMapper<User, UserData> {
	@Override
	public User mapBToA(final UserData b) {
		final Collection<UserAttribute> attributes = new ArrayList<UserAttribute>();
		final Collection<UserPermission> permissions = new ArrayList<UserPermission>();
		if (b.isPermAdmin()) {
			permissions.add(UserPermission.ADMIN);
		}
		if (b.isPermLogin()) {
			permissions.add(UserPermission.LOGIN);
		}
		if (permissions.isEmpty()) {
			permissions.add(UserPermission.NONE);
		}

		if (b.isAttChangePassword()) {
			attributes.add(UserAttribute.IS_NEW);
		}
		if (attributes.isEmpty()) {
			attributes.add(UserAttribute.NONE);
		}

		final User user = new User(new UserID(b.getId()), b.getName(), b.getPassword(), attributes, permissions);
		return user;
	}

	@Override
	public UserData mapAToB(final User a) {
		final UserData userData = new UserData();
		// might be null for new users
		if (a.getId() != null) {
			userData.setId(a.getId().getId());
		}
		userData.setName(a.getName());
		userData.setPassword(a.getPassword());

		if (a.getAttributes().contains(UserAttribute.IS_NEW)) {
			userData.setAttChangePassword(true);
		} else {
			userData.setAttChangePassword(false);
		}

		if (a.getPermissions().contains(UserPermission.ADMIN)) {
			userData.setPermAdmin(true);
		} else {
			userData.setPermAdmin(false);
		}

		if (a.getPermissions().contains(UserPermission.LOGIN)) {
			userData.setPermLogin(true);
		} else {
			userData.setPermLogin(false);
		}

		return userData;
	}

}
