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

import java.sql.Date;
import java.time.LocalDate;

import org.laladev.moneyjinn.businesslogic.model.BankAccount;
import org.laladev.moneyjinn.businesslogic.model.Contractpartner;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerID;
import org.laladev.moneyjinn.businesslogic.model.PostingAccount;
import org.laladev.moneyjinn.businesslogic.model.PostingAccountID;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.businesslogic.model.moneyflow.ImportedMoneyflow;
import org.laladev.moneyjinn.businesslogic.model.moneyflow.ImportedMoneyflowID;
import org.laladev.moneyjinn.core.mapper.IMapper;
import org.laladev.moneyjinn.core.rest.model.transport.ImportedMoneyflowTransport;

public class ImportedMoneyflowTransportMapper implements IMapper<ImportedMoneyflow, ImportedMoneyflowTransport> {
	private static final Short IS_PRIVATE_SHORT = Short.valueOf((short) 1);

	@Override
	public ImportedMoneyflow mapBToA(final ImportedMoneyflowTransport importedMoneyflowTransport) {
		final ImportedMoneyflow importedMoneyflow = new ImportedMoneyflow();
		if (importedMoneyflowTransport.getId() != null) {
			importedMoneyflow.setId(new ImportedMoneyflowID(importedMoneyflowTransport.getId()));
		}
		importedMoneyflow.setAmount(importedMoneyflowTransport.getAmount());

		if (importedMoneyflowTransport.getBookingdate() != null) {
			final LocalDate bookingDate = importedMoneyflowTransport.getBookingdate().toLocalDate();
			importedMoneyflow.setBookingDate(bookingDate);
		}
		if (importedMoneyflowTransport.getInvoicedate() != null) {
			final LocalDate invoiceDate = importedMoneyflowTransport.getInvoicedate().toLocalDate();
			importedMoneyflow.setInvoiceDate(invoiceDate);
		}
		if (importedMoneyflowTransport.getCapitalsourceid() != null) {
			final Capitalsource capitalsource = new Capitalsource(
					new CapitalsourceID(importedMoneyflowTransport.getCapitalsourceid()));
			importedMoneyflow.setCapitalsource(capitalsource);
		}
		if (importedMoneyflowTransport.getContractpartnerid() != null) {
			final Contractpartner contractpartner = new Contractpartner(
					new ContractpartnerID(importedMoneyflowTransport.getContractpartnerid()));
			importedMoneyflow.setContractpartner(contractpartner);
		}
		importedMoneyflow.setComment(importedMoneyflowTransport.getComment());
		if (importedMoneyflowTransport.getPrivat() != null
				&& IS_PRIVATE_SHORT.equals(importedMoneyflowTransport.getPrivat())) {
			importedMoneyflow.setPrivat(true);
		}
		if (importedMoneyflowTransport.getPostingaccountid() != null) {
			final PostingAccount postingAccount = new PostingAccount(
					new PostingAccountID(importedMoneyflowTransport.getPostingaccountid()));
			importedMoneyflow.setPostingAccount(postingAccount);
		}

		importedMoneyflow.setUsage(importedMoneyflowTransport.getUsage());

		if (importedMoneyflowTransport.getAccountNumber() != null
				&& !importedMoneyflowTransport.getAccountNumber().trim().isEmpty()) {
			importedMoneyflow.setBankAccount(new BankAccount(importedMoneyflowTransport.getAccountNumber(),
					importedMoneyflowTransport.getBankCode()));
		}
		importedMoneyflow.setExternalId(importedMoneyflowTransport.getExternalid());
		importedMoneyflow.setName(importedMoneyflowTransport.getName());
		importedMoneyflow.setUsage(importedMoneyflowTransport.getUsage());

		return importedMoneyflow;
	}

	@Override
	public ImportedMoneyflowTransport mapAToB(final ImportedMoneyflow importedMoneyflow) {
		final ImportedMoneyflowTransport importedMoneyflowTransport = new ImportedMoneyflowTransport();
		importedMoneyflowTransport.setId(importedMoneyflow.getId().getId());
		importedMoneyflowTransport.setAmount(importedMoneyflow.getAmount());
		if (importedMoneyflow.getBookingDate() != null) {
			final Date bookingDate = Date.valueOf(importedMoneyflow.getBookingDate());
			importedMoneyflowTransport.setBookingdate(bookingDate);
		}
		if (importedMoneyflow.getInvoiceDate() != null) {
			final Date invoiceDate = Date.valueOf(importedMoneyflow.getInvoiceDate());
			importedMoneyflowTransport.setInvoicedate(invoiceDate);
		}
		final Capitalsource capitalsource = importedMoneyflow.getCapitalsource();
		importedMoneyflowTransport.setCapitalsourceid(capitalsource.getId().getId());
		importedMoneyflowTransport.setCapitalsourcecomment(capitalsource.getComment());
		final Contractpartner contractpartner = importedMoneyflow.getContractpartner();
		if (contractpartner != null) {
			importedMoneyflowTransport.setContractpartnerid(contractpartner.getId().getId());
			importedMoneyflowTransport.setContractpartnername(contractpartner.getName());
		}
		importedMoneyflowTransport.setComment(importedMoneyflow.getComment());
		if (importedMoneyflow.isPrivat()) {
			importedMoneyflowTransport.setPrivat(IS_PRIVATE_SHORT);
		}
		final User user = importedMoneyflow.getUser();
		if (user != null) {
			importedMoneyflowTransport.setUserid(user.getId().getId());
		}

		final PostingAccount postingAccount = importedMoneyflow.getPostingAccount();
		if (postingAccount != null) {
			importedMoneyflowTransport.setPostingaccountid(postingAccount.getId().getId());
			importedMoneyflowTransport.setPostingaccountname(postingAccount.getName());
		}
		importedMoneyflowTransport.setUsage(importedMoneyflow.getUsage());

		final BankAccount bankAccount = importedMoneyflow.getBankAccount();
		if (bankAccount != null) {
			importedMoneyflowTransport.setAccountNumber(bankAccount.getAccountNumber());
			importedMoneyflowTransport.setBankCode(bankAccount.getBankCode());
		}

		importedMoneyflowTransport.setExternalid(importedMoneyflow.getExternalId());
		importedMoneyflowTransport.setName(importedMoneyflow.getName());
		importedMoneyflowTransport.setUsage(importedMoneyflow.getUsage());

		return importedMoneyflowTransport;
	}
}
