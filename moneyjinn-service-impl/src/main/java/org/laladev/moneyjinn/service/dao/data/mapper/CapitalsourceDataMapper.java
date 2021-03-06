//
// Copyright (c) 2015 Oliver Lehmann <lehmann@ans-netz.de>
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
import org.laladev.moneyjinn.model.BankAccount;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.service.dao.data.CapitalsourceData;

public class CapitalsourceDataMapper implements IMapper<Capitalsource, CapitalsourceData> {

	@Override
	public Capitalsource mapBToA(final CapitalsourceData capitalsourceData) {
		final Capitalsource capitalsource = new Capitalsource(new CapitalsourceID(capitalsourceData.getId()));
		if (capitalsourceData.getAccountNumber() != null) {
			capitalsource.setBankAccount(
					new BankAccount(capitalsourceData.getAccountNumber(), capitalsourceData.getBankCode()));
		}
		capitalsource.setComment(capitalsourceData.getComment());
		capitalsource.setGroupUse(capitalsourceData.isAttGroupUse());
		capitalsource.setImportAllowed(CapitalsourceImportMapper.map(capitalsourceData.getImportAllowed()));

		capitalsource.setState(CapitalsourceStateMapper.map(capitalsourceData.getState()));
		capitalsource.setType(CapitalsourceTypeMapper.map(capitalsourceData.getType()));

		capitalsource.setUser(new User(new UserID(capitalsourceData.getMacIdCreator())));
		capitalsource.setAccess(new Group(new GroupID(capitalsourceData.getMacIdAccessor())));

		capitalsource.setValidFrom(capitalsourceData.getValidFrom());
		capitalsource.setValidTil(capitalsourceData.getValidTil());

		return capitalsource;
	}

	@Override
	public CapitalsourceData mapAToB(final Capitalsource capitalsource) {
		final CapitalsourceData capitalsourceData = new CapitalsourceData();

		if (capitalsource.getId() != null) {
			capitalsourceData.setId(capitalsource.getId().getId());
		}

		final BankAccount bankAccount = capitalsource.getBankAccount();
		if (bankAccount != null) {
			capitalsourceData.setAccountNumber(bankAccount.getAccountNumber());
			capitalsourceData.setBankCode(bankAccount.getBankCode());
		}
		capitalsourceData.setComment(capitalsource.getComment());
		capitalsourceData.setAttGroupUse(capitalsource.isGroupUse());

		capitalsourceData.setImportAllowed(CapitalsourceImportMapper.map(capitalsource.getImportAllowed()));
		capitalsourceData.setState(CapitalsourceStateMapper.map(capitalsource.getState()));
		capitalsourceData.setType(CapitalsourceTypeMapper.map(capitalsource.getType()));

		if (capitalsource.getUser() != null) {
			capitalsourceData.setMacIdCreator(capitalsource.getUser().getId().getId());
		}
		if (capitalsource.getAccess() != null) {
			capitalsourceData.setMacIdAccessor(capitalsource.getAccess().getId().getId());
		}

		capitalsourceData.setValidFrom(capitalsource.getValidFrom());
		capitalsourceData.setValidTil(capitalsource.getValidTil());

		return capitalsourceData;
	}
}
