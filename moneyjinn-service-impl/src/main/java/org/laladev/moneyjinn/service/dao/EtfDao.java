//
// Copyright (c) 2021-2025 Oliver Lehmann <lehmann@ans-netz.de>
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
import java.time.Year;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.laladev.moneyjinn.service.dao.data.EtfData;
import org.laladev.moneyjinn.service.dao.data.EtfFlowData;
import org.laladev.moneyjinn.service.dao.data.EtfPreliminaryLumpSumData;
import org.laladev.moneyjinn.service.dao.data.EtfValueData;
import org.laladev.moneyjinn.service.dao.mapper.IEtfDaoMapper;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;

@Named
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class EtfDao {
	private final IEtfDaoMapper mapper;

	public List<EtfData> getAllEtf(final Long userId) {
		return this.mapper.getAllEtf(userId);
	}

	public EtfData getEtfById(final Long userId, final Long etfId) {
		return this.mapper.getEtfById(userId, etfId);
	}

	public Long createEtf(final EtfData etfData) {
		this.mapper.createEtf(etfData);
		return etfData.getEtfid();
	}

	public void updateEtf(final EtfData etfData) {
		this.mapper.updateEtf(etfData);
	}

	public void deleteEtf(final Long groupId, final Long etfId) {
		this.mapper.deleteEtf(groupId, etfId);
	}

	public EtfValueData getEtfValueForMonth(final String etfIsin, final Year year, final Month month) {
		final LocalDate startDate = year.atMonth(month).atDay(1);
		final LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
		return this.mapper.getEtfValueForMonth(etfIsin, startDate, endDate);
	}

	public EtfValueData getLatestEtfValue(final String etfIsin) {
		return this.mapper.getLatestEtfValue(etfIsin);
	}

	public List<EtfFlowData> getAllFlowsUntil(final Long etfId, final LocalDateTime timeUntil) {
		return this.mapper.getAllFlowsUntil(etfId, timeUntil);
	}

	public EtfFlowData getEtfFowById(final Long etfFlowId) {
		return this.mapper.getEtfFlowById(etfFlowId);
	}

	public Long createEtfFlow(final EtfFlowData etfFlowData) {
		this.mapper.createEtfFlow(etfFlowData);
		return etfFlowData.getEtfflowid();
	}

	public void updateEtfFlow(final EtfFlowData etfFlowData) {
		this.mapper.updateEtfFlow(etfFlowData);
	}

	public void deleteEtfFlow(final Long etfFlowId) {
		this.mapper.deleteEtfFlow(etfFlowId);
	}

	public List<EtfPreliminaryLumpSumData> getAllPreliminaryLumpSum(final Long etfId) {
		return this.mapper.getAllPreliminaryLumpSum(etfId);
	}

	public EtfPreliminaryLumpSumData getPreliminaryLumpSum(final Long etfPreliminaryLumpSumId) {
		return this.mapper.getPreliminaryLumpSum(etfPreliminaryLumpSumId);
	}

	public Long getPreliminaryLumpSumId(final Long etfId, final Integer year) {
		return this.mapper.getPreliminaryLumpSumId(etfId, year);
	}

	public List<EtfPreliminaryLumpSumData> getAllEtfPreliminaryLumpSum(final Long etfId) {
		return this.mapper.getAllEtfPreliminaryLumpSum(etfId);
	}

	public void createPreliminaryLumpSum(final EtfPreliminaryLumpSumData data) {
		this.mapper.createPreliminaryLumpSum(data);
	}

	public void updatePreliminaryLumpSum(final EtfPreliminaryLumpSumData data) {
		this.mapper.updatePreliminaryLumpSum(data);
	}

	public void deletePreliminaryLumpSum(final Long etfPreliminaryLumpSumId) {
		this.mapper.deletePreliminaryLumpSum(etfPreliminaryLumpSumId);
	}
}
