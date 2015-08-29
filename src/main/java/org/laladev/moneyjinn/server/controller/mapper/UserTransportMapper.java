package org.laladev.moneyjinn.server.controller.mapper;

import java.util.ArrayList;
import java.util.Collection;

import org.laladev.moneyjinn.api.IMapper;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserAttribute;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.access.UserPermission;
import org.laladev.moneyjinn.core.rest.model.transport.UserTransport;

public class UserTransportMapper implements IMapper<User, UserTransport> {
	@Override
	public User mapBToA(final UserTransport userTransport) {
		if (userTransport == null) {
			return null;
		}

		final Collection<UserAttribute> attributes = new ArrayList<UserAttribute>();
		final Collection<UserPermission> permissions = new ArrayList<UserPermission>();
		if (isTrue(userTransport.getUserIsAdmin())) {
			permissions.add(UserPermission.ADMIN);
		}
		if (isTrue(userTransport.getUserCanLogin())) {
			permissions.add(UserPermission.LOGIN);
		}
		if (permissions.isEmpty()) {
			permissions.add(UserPermission.NONE);
		}

		if (isTrue(userTransport.getUserIsNew())) {
			attributes.add(UserAttribute.IS_NEW);
		}
		if (attributes.isEmpty()) {
			attributes.add(UserAttribute.NONE);
		}

		final User user = new User(new UserID(userTransport.getId()), userTransport.getUserName(),
				userTransport.getUserPassword(), attributes, permissions);
		return user;
	}

	private boolean isTrue(final Short property) {
		return (property != null && property.equals(Short.valueOf((short) 1)));
	}

	@Override
	public UserTransport mapAToB(final User user) {
		if (user == null) {
			return null;
		}

		final UserTransport userTransport = new UserTransport();
		userTransport.setId(user.getId().getId());
		userTransport.setUserName(user.getName());
		userTransport.setUserPassword(user.getPassword());

		if (user.getAttributes().contains(UserAttribute.IS_NEW)) {
			userTransport.setUserIsNew(Short.valueOf(((short) 1)));
		}

		if (user.getPermissions().contains(UserPermission.ADMIN)) {
			userTransport.setUserIsAdmin(Short.valueOf(((short) 1)));
		}

		if (user.getPermissions().contains(UserPermission.LOGIN)) {
			userTransport.setUserCanLogin(Short.valueOf(((short) 1)));
		}

		return userTransport;
	}

}
