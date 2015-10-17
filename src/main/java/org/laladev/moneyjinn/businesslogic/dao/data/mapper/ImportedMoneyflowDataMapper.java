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

import org.laladev.moneyjinn.businesslogic.dao.data.ImportedMoneyflowData;
import org.laladev.moneyjinn.businesslogic.model.BankAccount;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.businesslogic.model.moneyflow.ImportedMoneyflow;
import org.laladev.moneyjinn.businesslogic.model.moneyflow.ImportedMoneyflowID;
import org.laladev.moneyjinn.core.mapper.IMapper;

public class ImportedMoneyflowDataMapper implements IMapper<ImportedMoneyflow, ImportedMoneyflowData> {

	@Override
	public ImportedMoneyflow mapBToA(final ImportedMoneyflowData importedMoneyflowData) {
		final ImportedMoneyflow importedMoneyflow = new ImportedMoneyflow();
		importedMoneyflow.setId(new ImportedMoneyflowID(importedMoneyflowData.getId()));
		importedMoneyflow.setAmount(importedMoneyflowData.getAmount());

		final LocalDate bookingDate = importedMoneyflowData.getBookingdate().toLocalDate();
		final LocalDate invoiceDate = importedMoneyflowData.getInvoicedate().toLocalDate();

		importedMoneyflow.setBookingDate(bookingDate);
		importedMoneyflow.setInvoiceDate(invoiceDate);
		importedMoneyflow.setCapitalsource(
				new Capitalsource(new CapitalsourceID(importedMoneyflowData.getMcsCapitalsourceId())));

		if (importedMoneyflowData.getAccountNumber() != null) {
			importedMoneyflow.setBankAccount(
					new BankAccount(importedMoneyflowData.getAccountNumber(), importedMoneyflowData.getBankCode()));
		}
		importedMoneyflow.setExternalId(importedMoneyflowData.getExternalId());
		importedMoneyflow.setUsage(importedMoneyflowData.getComment());
		importedMoneyflow.setName(importedMoneyflowData.getName());

		return importedMoneyflow;
	}

	@Override
	public ImportedMoneyflowData mapAToB(final ImportedMoneyflow importedMoneyflow) {
		final ImportedMoneyflowData importedMoneyflowData = new ImportedMoneyflowData();
		// might be null for new ImportedMoneyflows
		if (importedMoneyflow.getId() != null) {
			importedMoneyflowData.setId(importedMoneyflow.getId().getId());
		}
		importedMoneyflowData.setAmount(importedMoneyflow.getAmount());

		final Date bookingDate = Date.valueOf(importedMoneyflow.getBookingDate());
		final Date invoiceDate = Date.valueOf(importedMoneyflow.getInvoiceDate());

		importedMoneyflowData.setBookingdate(bookingDate);
		importedMoneyflowData.setInvoicedate(invoiceDate);
		importedMoneyflowData.setMcsCapitalsourceId(importedMoneyflow.getCapitalsource().getId().getId());

		final BankAccount bankAccount = importedMoneyflow.getBankAccount();
		if (bankAccount != null) {
			importedMoneyflowData.setAccountNumber(bankAccount.getAccountNumber());
			importedMoneyflowData.setBankCode(bankAccount.getBankCode());
		}

		importedMoneyflowData.setExternalId(importedMoneyflow.getExternalId());
		importedMoneyflowData.setComment(importedMoneyflow.getUsage());
		importedMoneyflowData.setName(importedMoneyflow.getName());

		return importedMoneyflowData;
	}
}
