//
// Copyright (c) 2021 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.service.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.laladev.moneyjinn.service.dao.data.EtfData;
import org.laladev.moneyjinn.service.dao.data.EtfFlowData;
import org.laladev.moneyjinn.service.dao.data.EtfValueData;
import org.laladev.moneyjinn.service.dao.mapper.IEtfDaoMapper;

@Named
public class EtfDao {

	@Inject
	IEtfDaoMapper mapper;

	public List<EtfData> getAllEtf() {
		return this.mapper.getAllEtf();
	}

	public EtfData getEtfById(final String isin) {
		return this.mapper.getEtfById(isin);
	}

	public EtfValueData getEtfValueForMonth(final String isin, final Short year, final Month month) {
		final LocalDate date = LocalDate.of(year.intValue(), month, 1);

		return this.mapper.getEtfValueForMonth(isin, date);
	}

	public List<EtfFlowData> getAllFlowsUntil(final String isin, final LocalDateTime timeUntil) {
		return this.mapper.getAllFlowsUntil(isin, timeUntil);
	}

	public EtfFlowData getEtfFowById(final Long etfFlowId) {
		return this.mapper.getEtfFlowById(etfFlowId);
	}

	public Long createEtfFlow(final EtfFlowData etfFlowData) {
		return this.mapper.createEtfFlow(etfFlowData);
	}

	public void updateEtfFlow(final EtfFlowData etfFlowData) {
		this.mapper.updateEtfFlow(etfFlowData);
	}

	public void deleteEtfFlow(final Long etfFlowId) {
		this.mapper.deleteEtfFlow(etfFlowId);
	}

}
