//
// Copyright (c) 2015-2025 Oliver Lehmann <lehmann@ans-netz.de>
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

import org.laladev.moneyjinn.converter.IMapstructMapper;
import org.laladev.moneyjinn.converter.UserIdMapper;
import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserAttribute;
import org.laladev.moneyjinn.model.access.UserRole;
import org.laladev.moneyjinn.server.model.UserTransport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapStructConfig.class, uses = UserIdMapper.class)
public interface UserTransportMapper extends IMapstructMapper<User, UserTransport> {
	@Override
	@Mapping(target = "attributes", source = ".", qualifiedByName = "mapUserAttributesToEntity")
	@Mapping(target = "role", source = "role", qualifiedByName = "mapUserRoleToEntity")
	@Mapping(target = "name", source = "userName")
	@Mapping(target = "password", source = "userPassword")
	User mapBToA(UserTransport b);

	default boolean isTrue(final Integer property) {
		return property != null && property.equals(1);
	}

	@Named("mapUserAttributesToEntity")
	default Collection<UserAttribute> mapUserAttributesToEntity(final UserTransport b) {
		final Collection<UserAttribute> attributes = new ArrayList<>();
		if (this.isTrue(b.getUserIsNew())) {
			attributes.add(UserAttribute.IS_NEW);
		}
		if (attributes.isEmpty()) {
			attributes.add(UserAttribute.NONE);
		}
		return attributes;
	}

	@Named("mapUserRoleToEntity")
	default UserRole mapUserRoleToEntity(final UserTransport.RoleEnum b) {
		return switch (b) {
		case ADMIN -> UserRole.ADMIN;
		case IMPORT -> UserRole.IMPORT;
		case INACTIVE -> UserRole.INACTIVE;
		case STANDARD -> UserRole.STANDARD;
		};
	}

	@Override
	@Mapping(target = "userIsNew", source = "attributes", qualifiedByName = "mapUserAttributeIsNewToTransport")
	@Mapping(target = "role", source = "role", qualifiedByName = "mapUserRolesToTransport")
	@Mapping(target = "userName", source = "name")
	@Mapping(target = "userPassword", ignore = true)
	UserTransport mapAToB(User a);

	@Named("mapUserAttributeIsNewToTransport")
	default Integer mapUserAttributeIsNewToTransport(final Collection<UserAttribute> a) {
		if (a != null && a.contains(UserAttribute.IS_NEW)) {
			return 1;
		}
		return null;
	}

	@Named("mapUserRolesToTransport")
	default UserTransport.RoleEnum mapUserRolesToEntity(final UserRole b) {
		return switch (b) {
		case ADMIN -> UserTransport.RoleEnum.ADMIN;
		case IMPORT -> UserTransport.RoleEnum.IMPORT;
		case INACTIVE -> UserTransport.RoleEnum.INACTIVE;
		case STANDARD -> UserTransport.RoleEnum.STANDARD;
		};
	}

}