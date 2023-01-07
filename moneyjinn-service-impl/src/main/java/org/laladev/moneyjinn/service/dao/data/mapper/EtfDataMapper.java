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

package org.laladev.moneyjinn.service.dao.data.mapper;

import org.laladev.moneyjinn.core.mapper.IMapper;
import org.laladev.moneyjinn.model.etf.Etf;
import org.laladev.moneyjinn.model.etf.EtfIsin;
import org.laladev.moneyjinn.service.dao.data.EtfData;

public class EtfDataMapper implements IMapper<Etf, EtfData> {
  @Override
  public Etf mapBToA(final EtfData b) {
    final Etf a = new Etf();
    a.setId(new EtfIsin(b.getIsin()));
    a.setName(b.getName());
    a.setWkn(b.getWkn());
    a.setTicker(b.getTicker());
    a.setChartUrl(b.getChartUrl());
    return a;
  }

  @Override
  public EtfData mapAToB(final Etf a) {
    return null;
  }
}
