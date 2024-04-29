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

package org.laladev.moneyjinn.service.api;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.List;

import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.etf.Etf;
import org.laladev.moneyjinn.model.etf.EtfFlow;
import org.laladev.moneyjinn.model.etf.EtfFlowID;
import org.laladev.moneyjinn.model.etf.EtfFlowWithTaxInfo;
import org.laladev.moneyjinn.model.etf.EtfID;
import org.laladev.moneyjinn.model.etf.EtfIsin;
import org.laladev.moneyjinn.model.etf.EtfPreliminaryLumpSum;
import org.laladev.moneyjinn.model.etf.EtfPreliminaryLumpSumID;
import org.laladev.moneyjinn.model.etf.EtfValue;
import org.laladev.moneyjinn.model.validation.ValidationResult;

public interface IEtfService {

	//
	// ETF
	//

	List<Etf> getAllEtf(UserID userId);

	Etf getEtfById(UserID userId, EtfID etfId);

	ValidationResult validateEtf(Etf etf);

	EtfID createEtf(Etf etf);

	void updateEtf(Etf etf);

	void deleteEtf(UserID userId, EtfID etfId);

	//
	// ETF Flows
	//

	List<EtfFlow> getAllEtfFlowsUntil(EtfID etfId, LocalDateTime timeUntil);

	EtfValue getEtfValueEndOfMonth(EtfIsin etfIsin, Year year, Month month);

	EtfFlow getEtfFlowById(EtfFlowID etfFlowId);

	ValidationResult validateEtfFlow(EtfFlow etfFlow);

	EtfFlowID createEtfFlow(EtfFlow etfFlow);

	void updateEtfFlow(EtfFlow etfFlow);

	void deleteEtfFlow(EtfFlowID etfFlowId);

	List<EtfFlowWithTaxInfo> calculateEffectiveEtfFlows(List<EtfFlow> etfFlows);

	//
	// ETF Preliminary Lump Sum
	//

	EtfPreliminaryLumpSum getEtfPreliminaryLumpSum(EtfPreliminaryLumpSumID id);

	List<Year> getAllEtfPreliminaryLumpSumYears(EtfID etfId);

	ValidationResult validateEtfPreliminaryLumpSum(EtfPreliminaryLumpSum etfPreliminaryLumpSum);

	void createEtfPreliminaryLumpSum(EtfPreliminaryLumpSum etfPreliminaryLumpSum);

	void updateEtfPreliminaryLumpSum(EtfPreliminaryLumpSum etfPreliminaryLumpSum);

	void deleteEtfPreliminaryLumpSum(EtfPreliminaryLumpSumID id);

}
