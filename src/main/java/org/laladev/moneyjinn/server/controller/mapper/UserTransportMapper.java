//
// Copyright (c) 2015 Oliver Lehmann <oliver@laladev.org>
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

package org.laladev.moneyjinn.server.controller.mapper;

import java.util.ArrayList;
import java.util.Collection;

import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserAttribute;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.access.UserPermission;
import org.laladev.moneyjinn.core.mapper.IMapper;
import org.laladev.moneyjinn.core.rest.model.transport.UserTransport;

public class UserTransportMapper implements IMapper<User, UserTransport> {
	@Override
	public User mapBToA(final UserTransport userTransport) {
		final Collection<UserAttribute> attributes = new ArrayList<UserAttribute>();
		final Collection<UserPermission> permissions = new ArrayList<UserPermission>();
		if (this.isTrue(userTransport.getUserIsAdmin())) {
			permissions.add(UserPermission.ADMIN);
		}
		if (this.isTrue(userTransport.getUserCanLogin())) {
			permissions.add(UserPermission.LOGIN);
		}
		if (permissions.isEmpty()) {
			permissions.add(UserPermission.NONE);
		}

		if (this.isTrue(userTransport.getUserIsNew())) {
			attributes.add(UserAttribute.IS_NEW);
		}
		if (attributes.isEmpty()) {
			attributes.add(UserAttribute.NONE);
		}
		UserID userId = null;
		if (userTransport.getId() != null) {
			userId = new UserID(userTransport.getId());
		}

		return new User(userId, userTransport.getUserName(), userTransport.getUserPassword(), attributes, permissions);
	}

	private boolean isTrue(final Short property) {
		return property != null && property.equals(Short.valueOf((short) 1));
	}

	@Override
	public UserTransport mapAToB(final User user) {
		final UserTransport userTransport = new UserTransport();
		userTransport.setId(user.getId().getId());
		userTransport.setUserName(user.getName());

		if (user.getAttributes().contains(UserAttribute.IS_NEW)) {
			userTransport.setUserIsNew(Short.valueOf((short) 1));
		}

		if (user.getPermissions().contains(UserPermission.ADMIN)) {
			userTransport.setUserIsAdmin(Short.valueOf((short) 1));
		}

		if (user.getPermissions().contains(UserPermission.LOGIN)) {
			userTransport.setUserCanLogin(Short.valueOf((short) 1));
		}

		return userTransport;
	}

}
