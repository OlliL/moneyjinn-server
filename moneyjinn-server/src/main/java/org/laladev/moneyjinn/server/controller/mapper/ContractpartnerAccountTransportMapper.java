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

package org.laladev.moneyjinn.server.controller.mapper;

import org.laladev.moneyjinn.businesslogic.model.BankAccount;
import org.laladev.moneyjinn.businesslogic.model.Contractpartner;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerAccount;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerAccountID;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerID;
import org.laladev.moneyjinn.core.mapper.IMapper;
import org.laladev.moneyjinn.core.rest.model.transport.ContractpartnerAccountTransport;

public class ContractpartnerAccountTransportMapper
		implements IMapper<ContractpartnerAccount, ContractpartnerAccountTransport> {

	@Override
	public ContractpartnerAccount mapBToA(final ContractpartnerAccountTransport contractpartnerAccountTransport) {

		BankAccount bankAccount = null;
		if (contractpartnerAccountTransport.getAccountNumber() != null
				&& contractpartnerAccountTransport.getBankCode() != null) {
			bankAccount = new BankAccount(contractpartnerAccountTransport.getAccountNumber(),
					contractpartnerAccountTransport.getBankCode());
		}

		final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(
				contractpartnerAccountTransport.getId());

		Contractpartner contractpartner = null;
		if (contractpartnerAccountTransport.getContractpartnerid() != null) {
			contractpartner = new Contractpartner(
					new ContractpartnerID(contractpartnerAccountTransport.getContractpartnerid()));
		}

		return new ContractpartnerAccount(contractpartnerAccountId, contractpartner, bankAccount);
	}

	@Override
	public ContractpartnerAccountTransport mapAToB(final ContractpartnerAccount contractpartnerAccount) {
		final ContractpartnerAccountTransport contractpartnerAccountTransport = new ContractpartnerAccountTransport();
		contractpartnerAccountTransport.setId(contractpartnerAccount.getId().getId());
		contractpartnerAccountTransport.setAccountNumber(contractpartnerAccount.getBankAccount().getAccountNumber());
		contractpartnerAccountTransport.setBankCode(contractpartnerAccount.getBankAccount().getBankCode());
		contractpartnerAccountTransport
				.setContractpartnerid(contractpartnerAccount.getContractpartner().getId().getId());

		return contractpartnerAccountTransport;
	}
}