//
// Copyright (c) 2021-2024 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.service.dao.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.laladev.moneyjinn.service.dao.data.EtfData;
import org.laladev.moneyjinn.service.dao.data.EtfFlowData;
import org.laladev.moneyjinn.service.dao.data.EtfValueData;

public interface IEtfDaoMapper {
	public List<EtfData> getAllEtf();

	public EtfData getEtfById(@Param("isin") String isin);

	public List<EtfFlowData> getAllFlowsUntil(@Param("isin") String isin, @Param("dateUntil") LocalDateTime timeUntil);

	public EtfValueData getEtfValueForMonth(@Param("isin") String isin, @Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);

	public EtfFlowData getEtfFlowById(@Param("id") Long id);

	public Long createEtfFlow(EtfFlowData data);

	public void updateEtfFlow(EtfFlowData data);

	public void deleteEtfFlow(@Param("id") Long id);
}
