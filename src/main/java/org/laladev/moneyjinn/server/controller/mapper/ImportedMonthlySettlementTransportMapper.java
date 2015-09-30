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

package org.laladev.moneyjinn.server.controller.mapper;

import java.time.Month;

import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.businesslogic.model.monthlysettlement.ImportedMonthlySettlement;
import org.laladev.moneyjinn.businesslogic.model.monthlysettlement.ImportedMonthlySettlementID;
import org.laladev.moneyjinn.core.mapper.IMapper;
import org.laladev.moneyjinn.core.rest.model.transport.ImportedMonthlySettlementTransport;

public class ImportedMonthlySettlementTransportMapper
		implements IMapper<ImportedMonthlySettlement, ImportedMonthlySettlementTransport> {

	@Override
	public ImportedMonthlySettlement mapBToA(
			final ImportedMonthlySettlementTransport importedMonthlySettlementTransport) {
		final ImportedMonthlySettlement importedMonthlySettlement = new ImportedMonthlySettlement();
		if (importedMonthlySettlementTransport.getId() != null) {
			importedMonthlySettlement
					.setId(new ImportedMonthlySettlementID(importedMonthlySettlementTransport.getId()));
		}
		importedMonthlySettlement.setAmount(importedMonthlySettlementTransport.getAmount());
		importedMonthlySettlement.setYear(importedMonthlySettlementTransport.getYear());
		final Short month = importedMonthlySettlementTransport.getMonth();
		if (importedMonthlySettlementTransport.getMonth() != null) {
			importedMonthlySettlement.setMonth(Month.of(month));
		}

		if (importedMonthlySettlementTransport.getCapitalsourceid() != null) {
			final Capitalsource capitalsource = new Capitalsource(
					new CapitalsourceID(importedMonthlySettlementTransport.getCapitalsourceid()));
			importedMonthlySettlement.setCapitalsource(capitalsource);
		}

		importedMonthlySettlement.setExternalId(importedMonthlySettlementTransport.getExternalid());

		return importedMonthlySettlement;
	}

	@Override
	public ImportedMonthlySettlementTransport mapAToB(final ImportedMonthlySettlement importedMonthlySettlement) {
		final ImportedMonthlySettlementTransport importedMonthlySettlementTransport = new ImportedMonthlySettlementTransport();
		importedMonthlySettlementTransport.setId(importedMonthlySettlement.getId().getId());
		importedMonthlySettlementTransport.setAmount(importedMonthlySettlement.getAmount());
		importedMonthlySettlementTransport.setYear(importedMonthlySettlement.getYear());
		importedMonthlySettlementTransport.setMonth((short) importedMonthlySettlement.getMonth().getValue());
		final Capitalsource capitalsource = importedMonthlySettlement.getCapitalsource();
		importedMonthlySettlementTransport.setCapitalsourceid(capitalsource.getId().getId());
		importedMonthlySettlementTransport.setCapitalsourcecomment(capitalsource.getComment());
		final User user = importedMonthlySettlement.getUser();
		importedMonthlySettlementTransport.setUserid(user.getId().getId());

		importedMonthlySettlementTransport.setExternalid(importedMonthlySettlement.getExternalId());

		return importedMonthlySettlementTransport;
	}
}
