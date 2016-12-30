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

package org.laladev.moneyjinn.service.dao.data.mapper;

import org.laladev.moneyjinn.core.mapper.IMapper;
import org.laladev.moneyjinn.model.Contractpartner;
import org.laladev.moneyjinn.model.ContractpartnerID;
import org.laladev.moneyjinn.model.PostingAccount;
import org.laladev.moneyjinn.model.PostingAccountID;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.service.dao.data.ContractpartnerData;

public class ContractpartnerDataMapper implements IMapper<Contractpartner, ContractpartnerData> {

	@Override
	public Contractpartner mapBToA(final ContractpartnerData contractpartnerData) {
		final Contractpartner contractpartner = new Contractpartner(new ContractpartnerID(contractpartnerData.getId()));

		contractpartner.setUser(new User(new UserID(contractpartnerData.getMacIdCreator())));
		contractpartner.setAccess(new Group(new GroupID(contractpartnerData.getMacIdAccessor())));

		contractpartner.setValidFrom(contractpartnerData.getValidFrom());
		contractpartner.setValidTil(contractpartnerData.getValidTil());

		contractpartner.setName(contractpartnerData.getName());
		contractpartner.setStreet(contractpartnerData.getStreet());
		contractpartner.setPostcode(contractpartnerData.getPostcode());
		contractpartner.setTown(contractpartnerData.getTown());
		contractpartner.setCountry(contractpartnerData.getCountry());

		contractpartner.setMoneyflowComment(contractpartnerData.getMmfComment());

		final Long postingAccountId = contractpartnerData.getMpaPostingAccountId();
		if (postingAccountId != null) {
			contractpartner.setPostingAccount(new PostingAccount(new PostingAccountID(postingAccountId)));
		}

		return contractpartner;
	}

	@Override
	public ContractpartnerData mapAToB(final Contractpartner contractpartner) {
		final ContractpartnerData contractpartnerData = new ContractpartnerData();

		if (contractpartner.getId() != null) {
			contractpartnerData.setId(contractpartner.getId().getId());
		}

		if (contractpartner.getUser() != null) {
			contractpartnerData.setMacIdCreator(contractpartner.getUser().getId().getId());
		}
		if (contractpartner.getAccess() != null) {
			contractpartnerData.setMacIdAccessor(contractpartner.getAccess().getId().getId());
		}
		contractpartnerData.setValidFrom(contractpartner.getValidFrom());
		contractpartnerData.setValidTil(contractpartner.getValidTil());

		contractpartnerData.setName(contractpartner.getName());
		contractpartnerData.setStreet(contractpartner.getStreet());
		contractpartnerData.setPostcode(contractpartner.getPostcode());
		contractpartnerData.setTown(contractpartner.getTown());
		contractpartnerData.setCountry(contractpartner.getCountry());

		contractpartnerData.setMmfComment(contractpartner.getMoneyflowComment());

		final PostingAccount postingAccount = contractpartner.getPostingAccount();
		if (postingAccount != null) {
			contractpartnerData.setMpaPostingAccountId(postingAccount.getId().getId());
		}

		return contractpartnerData;
	}
}
