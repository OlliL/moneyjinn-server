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

import org.laladev.moneyjinn.businesslogic.dao.data.ImportedBalanceData;
import org.laladev.moneyjinn.core.mapper.IMapper;
import org.laladev.moneyjinn.model.ImportedBalance;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;

public class ImportedBalanceDataMapper implements IMapper<ImportedBalance, ImportedBalanceData> {

	@Override
	public ImportedBalance mapBToA(final ImportedBalanceData importedBalanceData) {
		final ImportedBalance importedBalance = new ImportedBalance();
		importedBalance.setBalance(importedBalanceData.getBalance());
		importedBalance.setDate(importedBalanceData.getChangedate());
		importedBalance
				.setCapitalsource(new Capitalsource(new CapitalsourceID(importedBalanceData.getMcsCapitalsourceId())));

		return importedBalance;
	}

	@Override
	public ImportedBalanceData mapAToB(final ImportedBalance importedBalance) {
		final ImportedBalanceData importedBalanceData = new ImportedBalanceData();
		importedBalanceData.setBalance(importedBalance.getBalance());
		importedBalanceData.setMcsCapitalsourceId(importedBalance.getCapitalsource().getId().getId());
		importedBalanceData.setChangedate(importedBalance.getDate());

		return importedBalanceData;
	}
}
