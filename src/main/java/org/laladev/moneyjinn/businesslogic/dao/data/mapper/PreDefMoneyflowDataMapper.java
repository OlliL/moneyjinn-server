//
// Copyright (c) 2015 Oliver Lehmann <oliver@laladev.org>
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

package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import java.sql.Date;
import java.time.LocalDate;

import org.laladev.moneyjinn.businesslogic.dao.data.PreDefMoneyflowData;
import org.laladev.moneyjinn.businesslogic.model.Contractpartner;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerID;
import org.laladev.moneyjinn.businesslogic.model.PostingAccount;
import org.laladev.moneyjinn.businesslogic.model.PostingAccountID;
import org.laladev.moneyjinn.businesslogic.model.PreDefMoneyflow;
import org.laladev.moneyjinn.businesslogic.model.PreDefMoneyflowID;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.core.mapper.IMapper;

public class PreDefMoneyflowDataMapper implements IMapper<PreDefMoneyflow, PreDefMoneyflowData> {

	@Override
	public PreDefMoneyflow mapBToA(final PreDefMoneyflowData preDefMoneyflowData) {
		final PreDefMoneyflow preDefMoneyflow = new PreDefMoneyflow();
		preDefMoneyflow.setId(new PreDefMoneyflowID(preDefMoneyflowData.getId()));
		preDefMoneyflow.setAmount(preDefMoneyflowData.getAmount());

		final LocalDate creationDate = preDefMoneyflowData.getCreatedate().toLocalDate();

		preDefMoneyflow.setCreationDate(creationDate);
		preDefMoneyflow
				.setCapitalsource(new Capitalsource(new CapitalsourceID(preDefMoneyflowData.getMcsCapitalsourceId())));
		preDefMoneyflow.setContractpartner(
				new Contractpartner(new ContractpartnerID(preDefMoneyflowData.getMcpContractpartnerId())));
		preDefMoneyflow.setComment(preDefMoneyflowData.getComment());
		if (preDefMoneyflowData.getLastUsed() != null) {
			final LocalDate lastUsed = preDefMoneyflowData.getLastUsed().toLocalDate();
			preDefMoneyflow.setLastUsedDate(lastUsed);
		}
		preDefMoneyflow.setUser(new User(new UserID(preDefMoneyflowData.getMacId())));
		preDefMoneyflow.setOnceAMonth(preDefMoneyflowData.isOnceAMonth());
		preDefMoneyflow.setPostingAccount(
				new PostingAccount(new PostingAccountID(preDefMoneyflowData.getMpaPostingAccountId())));

		return preDefMoneyflow;
	}

	@Override
	public PreDefMoneyflowData mapAToB(final PreDefMoneyflow preDefMoneyflow) {
		final PreDefMoneyflowData preDefMoneyflowData = new PreDefMoneyflowData();
		// might be null for new PreDefMoneyflows
		if (preDefMoneyflow.getId() != null) {
			preDefMoneyflowData.setId(preDefMoneyflow.getId().getId());
		}
		preDefMoneyflowData.setAmount(preDefMoneyflow.getAmount());
		if (preDefMoneyflow.getCreationDate() != null) {
			final Date creationDate = Date.valueOf(preDefMoneyflow.getCreationDate());
			preDefMoneyflowData.setCreatedate(creationDate);
		}
		preDefMoneyflowData.setMcsCapitalsourceId(preDefMoneyflow.getCapitalsource().getId().getId());
		preDefMoneyflowData.setMcpContractpartnerId(preDefMoneyflow.getContractpartner().getId().getId());
		preDefMoneyflowData.setComment(preDefMoneyflow.getComment());
		if (preDefMoneyflow.getLastUsedDate() != null) {
			final Date lastUserDate = Date.valueOf(preDefMoneyflow.getLastUsedDate());
			preDefMoneyflowData.setLastUsed(lastUserDate);
		}
		if (preDefMoneyflow.getUser() != null) {
			preDefMoneyflowData.setMacId(preDefMoneyflow.getUser().getId().getId());
		}
		preDefMoneyflowData.setOnceAMonth(preDefMoneyflow.isOnceAMonth());
		preDefMoneyflowData.setMpaPostingAccountId(preDefMoneyflow.getPostingAccount().getId().getId());

		return preDefMoneyflowData;
	}
}
