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

package org.laladev.moneyjinn.server.controller.mapper;

import java.util.ArrayList;
import java.util.Collection;
import org.laladev.moneyjinn.converter.IMapstructMapper;
import org.laladev.moneyjinn.converter.UserIdMapper;
import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserAttribute;
import org.laladev.moneyjinn.model.access.UserPermission;
import org.laladev.moneyjinn.server.model.UserTransport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapStructConfig.class, uses = UserIdMapper.class)
public interface UserTransportMapper extends IMapstructMapper<User, UserTransport> {
  @Override
  @Mapping(target = "attributes", source = ".", qualifiedByName = "mapUserAttributesToEntity")
  @Mapping(target = "permissions", source = ".", qualifiedByName = "mapUserPermissionsToEntity")
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

  @Named("mapUserPermissionsToEntity")
  default Collection<UserPermission> mapUserPermissionsToEntity(final UserTransport b) {
    final Collection<UserPermission> permissions = new ArrayList<>();
    if (this.isTrue(b.getUserIsAdmin())) {
      permissions.add(UserPermission.ADMIN);
    }
    if (this.isTrue(b.getUserCanLogin())) {
      permissions.add(UserPermission.LOGIN);
    }
    if (permissions.isEmpty()) {
      permissions.add(UserPermission.NONE);
    }
    return permissions;
  }

  @Override
  @Mapping(target = "userIsNew", source = "attributes", qualifiedByName = "mapUserAttributeIsNewToTransport")
  @Mapping(target = "userCanLogin", source = "permissions", qualifiedByName = "mapUserPermissionLoginToTransport")
  @Mapping(target = "userIsAdmin", source = "permissions", qualifiedByName = "mapUserPermissionAdminToTransport")
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

  @Named("mapUserPermissionAdminToTransport")
  default Integer mapUserPermissionAdminToTransport(final Collection<UserPermission> a) {
    if (a != null && a.contains(UserPermission.ADMIN)) {
      return 1;
    }
    return null;
  }

  @Named("mapUserPermissionLoginToTransport")
  default Integer mapUserPermissionLoginToTransport(final Collection<UserPermission> a) {
    if (a != null && a.contains(UserPermission.LOGIN)) {
      return 1;
    }
    return null;
  }
}