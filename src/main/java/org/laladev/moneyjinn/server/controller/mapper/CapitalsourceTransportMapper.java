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
import org.laladev.moneyjinn.businesslogic.model.BankAccount;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceState;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceType;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;

public class CapitalsourceTransportMapper implements IMapper<Capitalsource, CapitalsourceTransport> {
	private static final Short GROUP_USE_SHORT = Short.valueOf((short) 1);
	private static final Short IMPORT_ALLOWED_SHORT = Short.valueOf((short) 1);
	private static final Short NON_CACHE_SHORT = Short.valueOf((short) 1);
	private static final Short CACHE_SHORT = Short.valueOf((short) 2);
	private static final Short CURRENT_ASSET_SHORT = Short.valueOf((short) 1);
	private static final Short LONG_TERM_ASSET_SHORT = Short.valueOf((short) 2);
	private static final Short RESERVE_ASSET_SHORT = Short.valueOf((short) 3);
	private static final Short PROVISION_ASSET_SHORT = Short.valueOf((short) 4);

	@Override
	public Capitalsource mapBToA(final CapitalsourceTransport capitalsourceTransport) {
		final Capitalsource capitalsource = new Capitalsource(new CapitalsourceID(capitalsourceTransport.getId()));
		if (capitalsourceTransport.getAccountNumber() != null) {
			capitalsource.setBankAccount(
					new BankAccount(capitalsourceTransport.getAccountNumber(), capitalsourceTransport.getBankCode()));
		}
		capitalsource.setComment(capitalsourceTransport.getComment());
		if (capitalsourceTransport.getGroupUse() != null
				&& GROUP_USE_SHORT.equals(capitalsourceTransport.getGroupUse())) {
			capitalsource.setGroupUse(true);
		}
		if (capitalsourceTransport.getImportAllowed() != null
				&& IMPORT_ALLOWED_SHORT.equals(capitalsourceTransport.getImportAllowed())) {
			capitalsource.setImportAllowed(true);
		}

		if (capitalsourceTransport.getState() != null) {
			switch (capitalsourceTransport.getState()) {
			case 1:
				capitalsource.setState(CapitalsourceState.NON_CACHE);
				break;
			case 2:
				capitalsource.setState(CapitalsourceState.CACHE);
				break;
			}
		}

		if (capitalsourceTransport.getType() != null) {
			switch (capitalsourceTransport.getType()) {
			case 1:
				capitalsource.setType(CapitalsourceType.CURRENT_ASSET);
				break;
			case 2:
				capitalsource.setType(CapitalsourceType.LONG_TERM_ASSET);
				break;
			case 3:
				capitalsource.setType(CapitalsourceType.RESERVE_ASSET);
				break;
			case 4:
				capitalsource.setType(CapitalsourceType.PROVISION_ASSET);
				break;
			}
		}

		capitalsource.setUser(new User(new UserID(capitalsourceTransport.getUserid())));

		if (capitalsourceTransport.getValidFrom() != null) {
			final LocalDate validFrom = capitalsourceTransport.getValidFrom().toLocalDate();
			capitalsource.setValidFrom(validFrom);
		}
		if (capitalsourceTransport.getValidTil() != null) {
			final LocalDate validTil = capitalsourceTransport.getValidTil().toLocalDate();
			capitalsource.setValidTil(validTil);
		}
		return capitalsource;
	}

	@Override
	public CapitalsourceTransport mapAToB(final Capitalsource capitalsource) {
		final CapitalsourceTransport capitalsourceTransport = new CapitalsourceTransport();

		capitalsourceTransport.setId(capitalsource.getId().getId());

		final BankAccount bankAccount = capitalsource.getBankAccount();
		if (bankAccount != null) {
			capitalsourceTransport.setAccountNumber(bankAccount.getAccountNumber());
			capitalsourceTransport.setBankCode(bankAccount.getBankCode());
		}
		capitalsourceTransport.setComment(capitalsource.getComment());
		if (capitalsource.isGroupUse()) {
			capitalsourceTransport.setGroupUse(GROUP_USE_SHORT);
		}
		if (capitalsource.isImportAllowed()) {
			capitalsourceTransport.setImportAllowed(IMPORT_ALLOWED_SHORT);
		}

		switch (capitalsource.getState()) {
		case NON_CACHE:
			capitalsourceTransport.setState(NON_CACHE_SHORT);
			break;
		case CACHE:
			capitalsourceTransport.setState(CACHE_SHORT);
			break;
		}
		switch (capitalsource.getType()) {
		case CURRENT_ASSET:
			capitalsourceTransport.setType(CURRENT_ASSET_SHORT);
			break;
		case LONG_TERM_ASSET:
			capitalsourceTransport.setType(LONG_TERM_ASSET_SHORT);
			break;
		case RESERVE_ASSET:
			capitalsourceTransport.setType(RESERVE_ASSET_SHORT);
			break;
		case PROVISION_ASSET:
			capitalsourceTransport.setType(PROVISION_ASSET_SHORT);
			break;
		}

		final User user = capitalsource.getUser();
		if (user != null) {
			capitalsourceTransport.setUserid(user.getId().getId());
		}
		final Date validFrom = Date.valueOf(capitalsource.getValidFrom());
		final Date validTil = Date.valueOf(capitalsource.getValidTil());

		capitalsourceTransport.setValidFrom(validFrom);
		capitalsourceTransport.setValidTil(validTil);

		return capitalsourceTransport;
	}
}
