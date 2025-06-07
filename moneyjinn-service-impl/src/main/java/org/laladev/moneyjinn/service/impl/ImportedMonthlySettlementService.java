//
// Copyright (c) 2015-2025 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.service.impl;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.monthlysettlement.ImportedMonthlySettlement;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.IImportedMonthlySettlementService;
import org.laladev.moneyjinn.service.api.IUserService;
import org.laladev.moneyjinn.service.dao.ImportedMonthlySettlementDao;
import org.laladev.moneyjinn.service.dao.data.ImportedMonthlySettlementData;
import org.laladev.moneyjinn.service.dao.data.mapper.ImportedMonthlySettlementDataMapper;
import org.springframework.util.Assert;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;

@Named
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ImportedMonthlySettlementService extends AbstractService implements IImportedMonthlySettlementService {
	private final ImportedMonthlySettlementDao importedMonthlySettlementDao;
	private final IUserService userService;
	private final ICapitalsourceService capitalsourceService;
	private final IAccessRelationService accessRelationService;
	private final ImportedMonthlySettlementDataMapper importedMonthlySettlementDataMapper;

	private ImportedMonthlySettlement mapImportedMonthlySettlementData(final UserID userId,
			final ImportedMonthlySettlementData importedMonthlySettlementData) {
		if (importedMonthlySettlementData != null) {
			final ImportedMonthlySettlement importedMonthlySettlement = this.importedMonthlySettlementDataMapper
					.mapBToA(importedMonthlySettlementData);
			importedMonthlySettlement.setUser(new User(userId));

			final LocalDate endOfMonth = LocalDate
					.of(importedMonthlySettlement.getYear(), importedMonthlySettlement.getMonth(), 1)
					.with(TemporalAdjusters.lastDayOfMonth());

			this.userService.enrichEntity(importedMonthlySettlement);
			this.accessRelationService.enrichEntity(importedMonthlySettlement, endOfMonth);
			this.capitalsourceService.enrichEntity(importedMonthlySettlement);

			return importedMonthlySettlement;
		}
		return null;
	}

	private List<ImportedMonthlySettlement> mapImportedMonthlySettlementDataList(final UserID userId,
			final List<ImportedMonthlySettlementData> importedMonthlySettlementDataList) {
		return importedMonthlySettlementDataList.stream()
				.map(element -> this.mapImportedMonthlySettlementData(userId, element)).toList();
	}

	@Override
	public ValidationResult validateImportedMonthlySettlement(
			final ImportedMonthlySettlement importedMonthlySettlement) {
		return new ValidationResult();
	}

	@Override
	public List<ImportedMonthlySettlement> getImportedMonthlySettlementsByMonth(final UserID userId, final Integer year,
			final Month month) {
		Assert.notNull(userId, "UserId must not be null!");
		Assert.notNull(year, "year must not be null!");
		Assert.notNull(month, "month must not be null!");
		final List<ImportedMonthlySettlementData> importedMonthlySettlementDataList = this.importedMonthlySettlementDao
				.getImportedMonthlySettlementsByMonth(year, month.getValue());
		return this.mapImportedMonthlySettlementDataList(userId, importedMonthlySettlementDataList);
	}

	@Override
	public void upsertImportedMonthlySettlement(final ImportedMonthlySettlement importedMonthlySettlement) {
		Assert.notNull(importedMonthlySettlement, "importedMonthlySettlement must not be null!");
		final ImportedMonthlySettlementData importedMonthlySettlementData = this.importedMonthlySettlementDataMapper
				.mapAToB(importedMonthlySettlement);
		this.importedMonthlySettlementDao.upsertImportedMonthlySettlement(importedMonthlySettlementData);
	}
}
