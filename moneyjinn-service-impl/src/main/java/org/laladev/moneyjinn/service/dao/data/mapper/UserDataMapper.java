//
// Copyright (c) 2015-2023 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.service.dao.data.mapper;

import java.util.ArrayList;
import java.util.Collection;

import org.laladev.moneyjinn.converter.IMapstructMapper;
import org.laladev.moneyjinn.converter.UserIdMapper;
import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserAttribute;
import org.laladev.moneyjinn.model.access.UserRole;
import org.laladev.moneyjinn.service.dao.data.UserData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapStructConfig.class, uses = UserIdMapper.class)
public interface UserDataMapper extends IMapstructMapper<User, UserData> {
	@Override
	@Mapping(target = "attributes", source = ".", qualifiedByName = "mapUserAttributesToEntity")
	@Mapping(target = "role", source = ".", qualifiedByName = "mapUserRoleToEntity")
	User mapBToA(UserData b);

	@Named("mapUserAttributesToEntity")
	default Collection<UserAttribute> mapUserAttributesToEntity(final UserData b) {
		final Collection<UserAttribute> attributes = new ArrayList<>();
		if (b.isAttChangePassword()) {
			attributes.add(UserAttribute.IS_NEW);
		}
		if (attributes.isEmpty()) {
			attributes.add(UserAttribute.NONE);
		}
		return attributes;
	}

	@Named("mapUserRoleToEntity")
	default UserRole mapUserRoleToEntity(final UserData b) {
		if (b.isPermAdmin()) {
			return UserRole.ADMIN;
		}
		if (b.isPermLogin()) {
			return UserRole.STANDARD;
		}
		if (b.isPermImport()) {
			return UserRole.IMPORT;
		}
		return UserRole.INACTIVE;
	}

	@Override
	@Mapping(target = "attChangePassword", source = "attributes", qualifiedByName = "mapUserAttributeIsNewToData")
	@Mapping(target = "permLogin", source = "role", qualifiedByName = "mapUserRoleLoginToData")
	@Mapping(target = "permAdmin", source = "role", qualifiedByName = "mapUserRoleAdminToData")
	@Mapping(target = "permImport", source = "role", qualifiedByName = "mapUserRoleImportToData")
	UserData mapAToB(User a);

	@Named("mapUserAttributeIsNewToData")
	default boolean mapUserAttributeIsNewToData(final Collection<UserAttribute> a) {
		return (a != null && a.contains(UserAttribute.IS_NEW));
	}

	@Named("mapUserRoleAdminToData")
	default boolean mapUserRoleAdminToData(final UserRole a) {
		return UserRole.ADMIN == a;
	}

	@Named("mapUserRoleLoginToData")
	default boolean mapUserRoleLoginToData(final UserRole a) {
		return UserRole.STANDARD == a;
	}

	@Named("mapUserRoleImportToData")
	default boolean mapUserRoleImportToData(final UserRole a) {
		return UserRole.IMPORT == a;
	}
}
