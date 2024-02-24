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
import org.mapstruct.ValueMapping;

@Mapper(config = MapStructConfig.class, uses = UserIdMapper.class)
public interface UserDataMapper extends IMapstructMapper<User, UserData> {
	@Override
	@Mapping(target = "attributes", source = "changePassword", qualifiedByName = "mapUserAttributesToEntity")
	@Mapping(target = "role", source = "role")
	@Mapping(target = "id", source = "userid")
	User mapBToA(UserData b);

	@Named("mapUserAttributesToEntity")
	default Collection<UserAttribute> mapUserAttributesToEntity(final boolean changePassword) {
		final Collection<UserAttribute> attributes = new ArrayList<>();
		if (changePassword) {
			attributes.add(UserAttribute.IS_NEW);
		}
		if (attributes.isEmpty()) {
			attributes.add(UserAttribute.NONE);
		}
		return attributes;
	}

	@ValueMapping(target = "ADMIN", source = "ADMIN")
	@ValueMapping(target = "STANDARD", source = "STANDARD")
	@ValueMapping(target = "IMPORT", source = "IMPORT")
	@ValueMapping(target = "INACTIVE", source = "INACTIVE")
	UserRole mapUserRoleToEntity(String role);

	@ValueMapping(target = "ADMIN", source = "ADMIN")
	@ValueMapping(target = "STANDARD", source = "STANDARD")
	@ValueMapping(target = "IMPORT", source = "IMPORT")
	@ValueMapping(target = "INACTIVE", source = "INACTIVE")
	String mapEntityToUserRole(UserRole role);

	@Override
	@Mapping(target = "changePassword", source = "attributes", qualifiedByName = "mapUserAttributeIsNewToData")
	@Mapping(target = "role", source = "role")
	@Mapping(target = "userid", source = "id")
	UserData mapAToB(User a);

	@Named("mapUserAttributeIsNewToData")
	default boolean mapUserAttributeIsNewToData(final Collection<UserAttribute> a) {
		return (a != null && a.contains(UserAttribute.IS_NEW));
	}
}
