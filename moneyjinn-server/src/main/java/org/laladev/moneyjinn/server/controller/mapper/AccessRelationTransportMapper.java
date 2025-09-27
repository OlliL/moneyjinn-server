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

import org.laladev.moneyjinn.converter.GroupIdMapper;
import org.laladev.moneyjinn.converter.IMapstructMapper;
import org.laladev.moneyjinn.converter.UserIdMapper;
import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.laladev.moneyjinn.model.access.AccessRelation;
import org.laladev.moneyjinn.server.model.AccessRelationTransport;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapStructConfig.class, uses = {UserIdMapper.class, GroupIdMapper.class})
public interface AccessRelationTransportMapper extends IMapstructMapper<AccessRelation, AccessRelationTransport> {
    @Override
    @Mapping(target = "validFrom", source = "validfrom")
    @Mapping(target = "validTil", source = "validtil")
    @Mapping(target = "groupID", source = "refId")
    AccessRelation mapBToA(AccessRelationTransport accessRelationTransport);

    @Override
    @Mapping(target = "validfrom", source = "validFrom")
    @Mapping(target = "validtil", source = "validTil")
    @Mapping(target = "refId", source = "groupID")
    AccessRelationTransport mapAToB(AccessRelation accessRelation);

    // work around https://github.com/mapstruct/mapstruct/issues/1166
    @AfterMapping
    default void doAfterMapping(@MappingTarget final AccessRelation entity) {
        if (entity != null && entity.getGroupID() != null && entity.getGroupID().getId() == null) {
            entity.setGroupID(null);
        }
    }
}
