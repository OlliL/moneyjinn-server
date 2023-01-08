//
// Copyright (c) 2021-2023 Oliver Lehmann <lehmann@ans-netz.de>
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

import org.laladev.moneyjinn.converter.EtfFlowIdMapper;
import org.laladev.moneyjinn.converter.EtfIsinMapper;
import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.laladev.moneyjinn.core.mapper.IMapper;
import org.laladev.moneyjinn.core.rest.model.etf.transport.EtfEffectiveFlowTransport;
import org.laladev.moneyjinn.model.etf.EtfFlow;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapStructConfig.class, uses = { EtfIsinMapper.class, EtfFlowIdMapper.class })
public interface EtfEffectiveFlowTransportMapper
    extends IMapper<EtfFlow, EtfEffectiveFlowTransport> {
  @Override
  @Mapping(target = "id", source = "etfflowid")
  @Mapping(target = "time", source = "timestamp")
  EtfFlow mapBToA(EtfEffectiveFlowTransport etfEffectiveFlowTransport);

  @Override
  @Mapping(target = "etfflowid", source = "id")
  @Mapping(target = "nanoseconds", source = "time.nano")
  @Mapping(target = "timestamp", source = "time")
  EtfEffectiveFlowTransport mapAToB(EtfFlow etfFlow);

  // work around https://github.com/mapstruct/mapstruct/issues/1166
  @AfterMapping
  default EtfFlow doAfterMapping(final EtfEffectiveFlowTransport source,
      @MappingTarget final EtfFlow entity) {
    if (entity != null && source != null && entity.getTime() != null) {
      final int nanos = source.getNanoseconds() != null ? source.getNanoseconds() : 0;
      entity.setTime(entity.getTime().withNano(nanos));
    }
    return entity;
  }
}
