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

package org.laladev.moneyjinn.service.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.laladev.moneyjinn.service.dao.data.EtfData;
import org.laladev.moneyjinn.service.dao.data.EtfFlowData;
import org.laladev.moneyjinn.service.dao.data.EtfPreliminaryLumpSumData;
import org.laladev.moneyjinn.service.dao.data.EtfValueData;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface IEtfDaoMapper {
    List<EtfData> getAllEtf(@Param("mauUserId") Long userId);

    EtfData getEtfById(@Param("mauUserId") Long userId, @Param("etfid") Long etfId);

    Long createEtf(EtfData data);

    void updateEtf(EtfData data);

    void deleteEtf(@Param("magGroupId") Long magGroupId, @Param("etfid") Long etfid);

    List<EtfFlowData> getAllFlowsUntil(@Param("metEtfid") Long etfId,
                                       @Param("dateUntil") LocalDateTime timeUntil);

    EtfValueData getEtfValueForMonth(@Param("isin") String isin, @Param("startDate") LocalDate startDate,
                                     @Param("endDate") LocalDate endDate);

    EtfValueData getLatestEtfValue(@Param("isin") String isin);

    EtfFlowData getEtfFlowById(@Param("id") Long id);

    Long createEtfFlow(EtfFlowData data);

    void updateEtfFlow(EtfFlowData data);

    void deleteEtfFlow(@Param("id") Long id);

    List<EtfPreliminaryLumpSumData> getAllPreliminaryLumpSum(@Param("metEtfid") Long etfId);

    EtfPreliminaryLumpSumData getPreliminaryLumpSum(
            @Param("etfPreliminaryLumpSumId") Long etfPreliminaryLumpSumId);

    Long getPreliminaryLumpSumId(@Param("metEtfid") Long etfId, @Param("year") Integer year);

    List<EtfPreliminaryLumpSumData> getAllEtfPreliminaryLumpSum(@Param("metEtfid") Long etfId);

    void createPreliminaryLumpSum(EtfPreliminaryLumpSumData data);

    void updatePreliminaryLumpSum(EtfPreliminaryLumpSumData data);

    void deletePreliminaryLumpSum(@Param("etfPreliminaryLumpSumId") Long etfPreliminaryLumpSumId);
}
