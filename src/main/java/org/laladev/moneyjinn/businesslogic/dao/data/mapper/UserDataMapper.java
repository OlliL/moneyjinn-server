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

package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import java.util.ArrayList;
import java.util.Collection;

import org.laladev.moneyjinn.businesslogic.dao.data.UserData;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserAttribute;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.access.UserPermission;
import org.laladev.moneyjinn.core.mapper.IMapper;

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

		return new User(new UserID(b.getId()), b.getName(), b.getPassword(), attributes, permissions);
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
