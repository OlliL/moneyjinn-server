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

import java.time.LocalDateTime;
import org.laladev.moneyjinn.core.mapper.IMapper;
import org.laladev.moneyjinn.core.rest.model.etf.transport.EtfFlowTransport;
import org.laladev.moneyjinn.model.etf.EtfFlow;
import org.laladev.moneyjinn.model.etf.EtfFlowID;
import org.laladev.moneyjinn.model.etf.EtfIsin;

public class EtfFlowTransportMapper implements IMapper<EtfFlow, EtfFlowTransport> {
  @Override
  public EtfFlow mapBToA(final EtfFlowTransport etfFlowTransport) {
    final EtfFlow etfFlow = new EtfFlow();
    if (etfFlowTransport.getEtfflowid() != null) {
      etfFlow.setId(new EtfFlowID(etfFlowTransport.getEtfflowid()));
    }
    etfFlow.setAmount(etfFlowTransport.getAmount());
    if (etfFlowTransport.getIsin() != null) {
      etfFlow.setIsin(new EtfIsin(etfFlowTransport.getIsin()));
    }
    etfFlow.setPrice(etfFlowTransport.getPrice());
    if (etfFlowTransport.getTimestamp() != null) {
      final LocalDateTime time = etfFlowTransport.getTimestamp();
      final int nanos = etfFlowTransport.getNanoseconds() != null
          ? etfFlowTransport.getNanoseconds()
          : 0;
      etfFlow.setTime(time.withNano(nanos));
    }
    return etfFlow;
  }

  @Override
  public EtfFlowTransport mapAToB(final EtfFlow etfFlow) {
    final EtfFlowTransport transport = new EtfFlowTransport();
    transport.setEtfflowid(etfFlow.getId().getId());
    transport.setIsin(etfFlow.getIsin().getId());
    if (etfFlow.getTime() != null) {
      transport.setTimestamp(etfFlow.getTime());
      transport.setNanoseconds(etfFlow.getTime().getNano());
    }
    transport.setAmount(etfFlow.getAmount());
    transport.setPrice(etfFlow.getPrice());
    return transport;
  }
}
