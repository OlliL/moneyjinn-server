package org.laladev.moneyjinn.server.controller.mapper;

import java.sql.Date;
import java.time.LocalDate;

import org.laladev.moneyjinn.api.IMapper;
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
import org.laladev.moneyjinn.core.rest.model.transport.PreDefMoneyflowTransport;

//Copyright (c) 2015 Oliver Lehmann <oliver@laladev.org>
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without
//modification, are permitted provided that the following conditions
//are met:
//1. Redistributions of source code must retain the above copyright
//notice, this list of conditions and the following disclaimer
//2. Redistributions in binary form must reproduce the above copyright
//notice, this list of conditions and the following disclaimer in the
//documentation and/or other materials provided with the distribution.
//
//THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
//ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
//IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
//ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
//FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
//DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
//OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
//HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
//LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
//OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
//SUCH DAMAGE.

public class PreDefMoneyflowTransportMapper implements IMapper<PreDefMoneyflow, PreDefMoneyflowTransport> {
	private static final Short ONCE_A_MONTH_SHORT = Short.valueOf((short) 1);

	@Override
	public PreDefMoneyflow mapBToA(final PreDefMoneyflowTransport preDefMoneyflowTransport) {
		final PreDefMoneyflow preDefMoneyflow = new PreDefMoneyflow();
		if (preDefMoneyflowTransport.getId() != null) {
			preDefMoneyflow.setId(new PreDefMoneyflowID(preDefMoneyflowTransport.getId()));
		}
		preDefMoneyflow.setAmount(preDefMoneyflowTransport.getAmount());
		if (preDefMoneyflowTransport.getCreatedate() != null) {
			final LocalDate creationDate = preDefMoneyflowTransport.getCreatedate().toLocalDate();
			preDefMoneyflow.setCreationDate(creationDate);
		}
		if (preDefMoneyflowTransport.getCapitalsourceid() != null) {
			final Capitalsource capitalsource = new Capitalsource(
					new CapitalsourceID(preDefMoneyflowTransport.getCapitalsourceid()));
			preDefMoneyflow.setCapitalsource(capitalsource);
		}
		if (preDefMoneyflowTransport.getContractpartnerid() != null) {
			final Contractpartner contractpartner = new Contractpartner(
					new ContractpartnerID(preDefMoneyflowTransport.getContractpartnerid()));
			preDefMoneyflow.setContractpartner(contractpartner);
		}
		preDefMoneyflow.setComment(preDefMoneyflowTransport.getComment());
		if (preDefMoneyflowTransport.getLastUsed() != null) {
			final LocalDate lastUsedDate = preDefMoneyflowTransport.getLastUsed().toLocalDate();
			preDefMoneyflow.setLastUsedDate(lastUsedDate);
		}
		if (preDefMoneyflowTransport.getOnceAMonth() != null
				&& ONCE_A_MONTH_SHORT.equals(preDefMoneyflowTransport.getOnceAMonth())) {
			preDefMoneyflow.setOnceAMonth(true);
		}
		if (preDefMoneyflowTransport.getUserid() != null) {
			preDefMoneyflow.setUser(new User(new UserID(preDefMoneyflowTransport.getUserid())));
		}
		if (preDefMoneyflowTransport.getPostingaccountid() != null) {
			final PostingAccount postingAccount = new PostingAccount(
					new PostingAccountID(preDefMoneyflowTransport.getPostingaccountid()));
			preDefMoneyflow.setPostingAccount(postingAccount);
		}

		return preDefMoneyflow;
	}

	@Override
	public PreDefMoneyflowTransport mapAToB(final PreDefMoneyflow preDefMoneyflow) {
		final PreDefMoneyflowTransport preDefMoneyflowTransport = new PreDefMoneyflowTransport();
		preDefMoneyflowTransport.setId(preDefMoneyflow.getId().getId());
		preDefMoneyflowTransport.setAmount(preDefMoneyflow.getAmount());
		if (preDefMoneyflow.getCreationDate() != null) {
			final Date creationDate = Date.valueOf(preDefMoneyflow.getCreationDate());
			preDefMoneyflowTransport.setCreatedate(creationDate);
		}
		final Capitalsource capitalsource = preDefMoneyflow.getCapitalsource();
		preDefMoneyflowTransport.setCapitalsourceid(capitalsource.getId().getId());
		preDefMoneyflowTransport.setCapitalsourcecomment(capitalsource.getComment());
		final Contractpartner contractpartner = preDefMoneyflow.getContractpartner();
		preDefMoneyflowTransport.setContractpartnerid(contractpartner.getId().getId());
		preDefMoneyflowTransport.setContractpartnername(contractpartner.getName());
		preDefMoneyflowTransport.setComment(preDefMoneyflow.getComment());
		if (preDefMoneyflow.getLastUsedDate() != null) {
			final Date lastUserDate = Date.valueOf(preDefMoneyflow.getLastUsedDate());
			preDefMoneyflowTransport.setLastUsed(lastUserDate);
		}
		if (preDefMoneyflow.isOnceAMonth()) {
			preDefMoneyflowTransport.setOnceAMonth(ONCE_A_MONTH_SHORT);
		}
		final User user = preDefMoneyflow.getUser();
		preDefMoneyflowTransport.setUserid(user.getId().getId());
		final PostingAccount postingAccount = preDefMoneyflow.getPostingAccount();
		preDefMoneyflowTransport.setPostingaccountid(postingAccount.getId().getId());
		preDefMoneyflowTransport.setPostingaccountname(postingAccount.getName());

		return preDefMoneyflowTransport;
	}
}
