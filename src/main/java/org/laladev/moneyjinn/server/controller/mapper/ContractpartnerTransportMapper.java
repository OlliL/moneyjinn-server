package org.laladev.moneyjinn.server.controller.mapper;

import java.sql.Date;
import java.time.LocalDate;

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

import org.laladev.moneyjinn.api.IMapper;
import org.laladev.moneyjinn.businesslogic.model.Contractpartner;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerID;
import org.laladev.moneyjinn.businesslogic.model.PostingAccount;
import org.laladev.moneyjinn.businesslogic.model.PostingAccountID;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.core.rest.model.transport.ContractpartnerTransport;

public class ContractpartnerTransportMapper implements IMapper<Contractpartner, ContractpartnerTransport> {

	@Override
	public Contractpartner mapBToA(final ContractpartnerTransport contractpartnerTransport) {
		final Contractpartner contractpartner = new Contractpartner(
				new ContractpartnerID(contractpartnerTransport.getId()));

		contractpartner.setUser(new User(new UserID(contractpartnerTransport.getUserid())));

		if (contractpartnerTransport.getValidFrom() != null) {
			final LocalDate validFrom = contractpartnerTransport.getValidFrom().toLocalDate();
			contractpartner.setValidFrom(validFrom);
		}
		if (contractpartnerTransport.getValidTil() != null) {
			final LocalDate validTil = contractpartnerTransport.getValidTil().toLocalDate();
			contractpartner.setValidTil(validTil);
		}

		contractpartner.setName(contractpartnerTransport.getName());
		contractpartner.setStreet(contractpartnerTransport.getStreet());
		contractpartner.setPostcode(contractpartnerTransport.getPostcode());
		contractpartner.setTown(contractpartnerTransport.getTown());
		contractpartner.setCountry(contractpartnerTransport.getCountry());

		contractpartner.setMoneyflowComment(contractpartnerTransport.getMoneyflowComment());

		final Long postingAccountId = contractpartnerTransport.getPostingAccountId();
		if (postingAccountId != null) {
			contractpartner.setPostingAccount(new PostingAccount(new PostingAccountID(postingAccountId)));
		}

		return contractpartner;
	}

	@Override
	public ContractpartnerTransport mapAToB(final Contractpartner contractpartner) {
		final ContractpartnerID contractpartnerId = contractpartner.getId();
		final User user = contractpartner.getUser();
		final Long id = contractpartnerId == null ? null : contractpartnerId.getId();
		final Long userId = user == null ? null : user.getId().getId();
		final Date validFrom = Date.valueOf(contractpartner.getValidFrom());
		final Date validTil = Date.valueOf(contractpartner.getValidTil());

		Long postingAccountId = null;
		String postingAccountName = null;
		final PostingAccount postingAccount = contractpartner.getPostingAccount();
		if (postingAccount != null) {
			postingAccountId = postingAccount.getId().getId();
			postingAccountName = postingAccount.getName();
		}

		final ContractpartnerTransport contractpartnerTransport = new ContractpartnerTransport(id, userId,
				contractpartner.getName(), contractpartner.getStreet(), contractpartner.getPostcode(),
				contractpartner.getTown(), validTil, validFrom, contractpartner.getCountry(),
				contractpartner.getMoneyflowComment(), postingAccountName, postingAccountId);

		return contractpartnerTransport;
	}
}
