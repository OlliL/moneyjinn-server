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

import java.io.Serializable;
import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.laladev.moneyjinn.core.mapper.IMapper;
import org.laladev.moneyjinn.server.model.ValidationItemTransport;
import org.laladev.moneyjinn.model.AbstractEntityID;
import org.laladev.moneyjinn.model.validation.ValidationResultItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapStructConfig.class)
public interface ValidationItemTransportMapper
    extends IMapper<ValidationItemTransport, ValidationResultItem> {
  @Override
  default ValidationResultItem mapAToB(final ValidationItemTransport a) {
    throw new UnsupportedOperationException("Mapping not supported!");
  }

  @Override
  @Mapping(target = "error", source = "error.errorCode")
  @Mapping(target = "key", source = "key", qualifiedByName = "mapKeyToTransport")
  ValidationItemTransport mapBToA(ValidationResultItem b);

  @Named("mapKeyToTransport")
  default String mapKeyToTransport(final AbstractEntityID<? extends Serializable> idModel) {
    if (idModel != null) {
      return idModel.getId().toString();
    }
    return null;
  }

}
