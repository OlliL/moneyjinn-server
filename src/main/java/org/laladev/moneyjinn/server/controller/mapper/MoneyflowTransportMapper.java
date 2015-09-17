package org.laladev.moneyjinn.server.controller.mapper;

import java.sql.Date;
import java.time.LocalDate;

import org.laladev.moneyjinn.businesslogic.model.Contractpartner;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerID;
import org.laladev.moneyjinn.businesslogic.model.Moneyflow;
import org.laladev.moneyjinn.businesslogic.model.MoneyflowID;
import org.laladev.moneyjinn.businesslogic.model.PostingAccount;
import org.laladev.moneyjinn.businesslogic.model.PostingAccountID;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.core.mapper.IMapper;
import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowTransport;

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

public class MoneyflowTransportMapper implements IMapper<Moneyflow, MoneyflowTransport> {
	private static final Short IS_PRIVATE_SHORT = Short.valueOf((short) 1);

	@Override
	public Moneyflow mapBToA(final MoneyflowTransport moneyflowTransport) {
		final Moneyflow moneyflow = new Moneyflow();
		if (moneyflowTransport.getId() != null) {
			moneyflow.setId(new MoneyflowID(moneyflowTransport.getId()));
		}
		moneyflow.setAmount(moneyflowTransport.getAmount());

		if (moneyflowTransport.getBookingdate() != null) {
			final LocalDate bookingDate = moneyflowTransport.getBookingdate().toLocalDate();
			moneyflow.setBookingDate(bookingDate);
		}
		if (moneyflowTransport.getInvoicedate() != null) {
			final LocalDate invoiceDate = moneyflowTransport.getInvoicedate().toLocalDate();
			moneyflow.setInvoiceDate(invoiceDate);
		}
		if (moneyflowTransport.getCapitalsourceid() != null) {
			final Capitalsource capitalsource = new Capitalsource(
					new CapitalsourceID(moneyflowTransport.getCapitalsourceid()));
			moneyflow.setCapitalsource(capitalsource);
		}
		if (moneyflowTransport.getContractpartnerid() != null) {
			final Contractpartner contractpartner = new Contractpartner(
					new ContractpartnerID(moneyflowTransport.getContractpartnerid()));
			moneyflow.setContractpartner(contractpartner);
		}
		moneyflow.setComment(moneyflowTransport.getComment());
		if (moneyflowTransport.getPrivat() != null && IS_PRIVATE_SHORT.equals(moneyflowTransport.getPrivat())) {
			moneyflow.setPrivat(true);
		}
		if (moneyflowTransport.getPostingaccountid() != null) {
			final PostingAccount postingAccount = new PostingAccount(
					new PostingAccountID(moneyflowTransport.getPostingaccountid()));
			moneyflow.setPostingAccount(postingAccount);
		}

		return moneyflow;
	}

	@Override
	public MoneyflowTransport mapAToB(final Moneyflow moneyflow) {
		final MoneyflowTransport moneyflowTransport = new MoneyflowTransport();
		moneyflowTransport.setId(moneyflow.getId().getId());
		moneyflowTransport.setAmount(moneyflow.getAmount());
		if (moneyflow.getBookingDate() != null) {
			final Date bookingDate = Date.valueOf(moneyflow.getBookingDate());
			moneyflowTransport.setBookingdate(bookingDate);
		}
		if (moneyflow.getInvoiceDate() != null) {
			final Date invoiceDate = Date.valueOf(moneyflow.getInvoiceDate());
			moneyflowTransport.setInvoicedate(invoiceDate);
		}
		final Capitalsource capitalsource = moneyflow.getCapitalsource();
		moneyflowTransport.setCapitalsourceid(capitalsource.getId().getId());
		moneyflowTransport.setCapitalsourcecomment(capitalsource.getComment());
		final Contractpartner contractpartner = moneyflow.getContractpartner();
		moneyflowTransport.setContractpartnerid(contractpartner.getId().getId());
		moneyflowTransport.setContractpartnername(contractpartner.getName());
		moneyflowTransport.setComment(moneyflow.getComment());
		if (moneyflow.isPrivat()) {
			moneyflowTransport.setPrivat(IS_PRIVATE_SHORT);
		}
		final User user = moneyflow.getUser();
		moneyflowTransport.setUserid(user.getId().getId());
		final PostingAccount postingAccount = moneyflow.getPostingAccount();
		moneyflowTransport.setPostingaccountid(postingAccount.getId().getId());
		moneyflowTransport.setPostingaccountname(postingAccount.getName());

		return moneyflowTransport;
	}
}
