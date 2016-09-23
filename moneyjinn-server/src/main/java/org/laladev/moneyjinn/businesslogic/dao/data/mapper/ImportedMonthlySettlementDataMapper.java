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

package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import java.time.Month;

import org.laladev.moneyjinn.businesslogic.dao.data.ImportedMonthlySettlementData;
import org.laladev.moneyjinn.core.mapper.IMapper;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.monthlysettlement.ImportedMonthlySettlement;
import org.laladev.moneyjinn.model.monthlysettlement.ImportedMonthlySettlementID;

public class ImportedMonthlySettlementDataMapper
		implements IMapper<ImportedMonthlySettlement, ImportedMonthlySettlementData> {

	@Override
	public ImportedMonthlySettlement mapBToA(final ImportedMonthlySettlementData importedMonthlySettlementData) {
		final ImportedMonthlySettlement importedMonthlySettlement = new ImportedMonthlySettlement();
		importedMonthlySettlement.setId(new ImportedMonthlySettlementID(importedMonthlySettlementData.getId()));
		importedMonthlySettlement.setAmount(importedMonthlySettlementData.getAmount());
		importedMonthlySettlement.setMonth(Month.of(importedMonthlySettlementData.getMonth().intValue()));
		importedMonthlySettlement.setYear(importedMonthlySettlementData.getYear());

		importedMonthlySettlement.setCapitalsource(
				new Capitalsource(new CapitalsourceID(importedMonthlySettlementData.getMcsCapitalsourceId())));

		importedMonthlySettlement.setExternalId(importedMonthlySettlementData.getExternalId());

		return importedMonthlySettlement;
	}

	@Override
	public ImportedMonthlySettlementData mapAToB(final ImportedMonthlySettlement importedMonthlySettlement) {
		final ImportedMonthlySettlementData importedMonthlySettlementData = new ImportedMonthlySettlementData();
		// might be null for new ImportedMonthlySettlements
		if (importedMonthlySettlement.getId() != null) {
			importedMonthlySettlementData.setId(importedMonthlySettlement.getId().getId());
		}
		importedMonthlySettlementData.setAmount(importedMonthlySettlement.getAmount());
		importedMonthlySettlementData.setMonth((short) importedMonthlySettlement.getMonth().getValue());
		importedMonthlySettlementData.setYear(importedMonthlySettlement.getYear());

		importedMonthlySettlementData
				.setMcsCapitalsourceId(importedMonthlySettlement.getCapitalsource().getId().getId());

		importedMonthlySettlementData.setExternalId(importedMonthlySettlement.getExternalId());

		return importedMonthlySettlementData;
	}
}
