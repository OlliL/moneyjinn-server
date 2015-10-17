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

package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import java.sql.Date;
import java.time.LocalDate;

import org.laladev.moneyjinn.businesslogic.dao.data.MoneyflowData;
import org.laladev.moneyjinn.businesslogic.model.Contractpartner;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerID;
import org.laladev.moneyjinn.businesslogic.model.PostingAccount;
import org.laladev.moneyjinn.businesslogic.model.PostingAccountID;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.businesslogic.model.moneyflow.Moneyflow;
import org.laladev.moneyjinn.businesslogic.model.moneyflow.MoneyflowID;
import org.laladev.moneyjinn.core.mapper.IMapper;

public class MoneyflowDataMapper implements IMapper<Moneyflow, MoneyflowData> {

	@Override
	public Moneyflow mapBToA(final MoneyflowData moneyflowData) {
		final Moneyflow moneyflow = new Moneyflow();
		moneyflow.setId(new MoneyflowID(moneyflowData.getId()));
		moneyflow.setAmount(moneyflowData.getAmount());

		final LocalDate bookingDate = moneyflowData.getBookingdate().toLocalDate();
		final LocalDate invoiceDate = moneyflowData.getInvoicedate().toLocalDate();

		moneyflow.setBookingDate(bookingDate);
		moneyflow.setInvoiceDate(invoiceDate);
		moneyflow.setCapitalsource(new Capitalsource(new CapitalsourceID(moneyflowData.getMcsCapitalsourceId())));
		moneyflow.setContractpartner(
				new Contractpartner(new ContractpartnerID(moneyflowData.getMcpContractpartnerId())));
		moneyflow.setComment(moneyflowData.getComment());
		moneyflow.setUser(new User(new UserID(moneyflowData.getMacIdCreator())));
		moneyflow.setPrivat(moneyflowData.isPrivat());
		moneyflow.setPostingAccount(new PostingAccount(new PostingAccountID(moneyflowData.getMpaPostingAccountId())));

		return moneyflow;
	}

	@Override
	public MoneyflowData mapAToB(final Moneyflow moneyflow) {
		final MoneyflowData moneyflowData = new MoneyflowData();
		// might be null for new Moneyflows
		if (moneyflow.getId() != null) {
			moneyflowData.setId(moneyflow.getId().getId());
		}
		moneyflowData.setAmount(moneyflow.getAmount());

		final Date bookingDate = Date.valueOf(moneyflow.getBookingDate());
		final Date invoiceDate = Date.valueOf(moneyflow.getInvoiceDate());

		moneyflowData.setBookingdate(bookingDate);
		moneyflowData.setInvoicedate(invoiceDate);

		moneyflowData.setMcsCapitalsourceId(moneyflow.getCapitalsource().getId().getId());
		moneyflowData.setMcpContractpartnerId(moneyflow.getContractpartner().getId().getId());
		moneyflowData.setComment(moneyflow.getComment());
		if (moneyflow.getUser() != null) {
			moneyflowData.setMacIdCreator(moneyflow.getUser().getId().getId());
		}
		if (moneyflow.getGroup() != null) {
			moneyflowData.setMacIdAccessor(moneyflow.getGroup().getId().getId());
		}
		moneyflowData.setPrivat(moneyflow.isPrivat());
		moneyflowData.setMpaPostingAccountId(moneyflow.getPostingAccount().getId().getId());

		return moneyflowData;
	}
}
