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

package org.laladev.moneyjinn.businesslogic.service.impl;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.laladev.moneyjinn.businesslogic.dao.ImportedMonthlySettlementDao;
import org.laladev.moneyjinn.businesslogic.dao.data.ImportedMonthlySettlementData;
import org.laladev.moneyjinn.businesslogic.dao.data.mapper.ImportedMonthlySettlementDataMapper;
import org.laladev.moneyjinn.businesslogic.model.access.Group;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.businesslogic.model.monthlysettlement.ImportedMonthlySettlement;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResult;
import org.laladev.moneyjinn.businesslogic.service.api.IAccessRelationService;
import org.laladev.moneyjinn.businesslogic.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.businesslogic.service.api.IImportedMonthlySettlementService;
import org.springframework.util.Assert;

@Named
public class ImportedMonthlySettlementService extends AbstractService implements IImportedMonthlySettlementService {
	@Inject
	ImportedMonthlySettlementDao importedMonthlySettlementDao;

	@Inject
	ICapitalsourceService capitalsourceService;
	@Inject
	IAccessRelationService accessRelationService;

	@Override
	protected void addBeanMapper() {
		super.registerBeanMapper(new ImportedMonthlySettlementDataMapper());
	}

	private final ImportedMonthlySettlement mapImportedMonthlySettlementData(final UserID userId,
			final ImportedMonthlySettlementData importedMonthlySettlementData) {

		if (importedMonthlySettlementData != null) {
			final ImportedMonthlySettlement importedMonthlySettlement = super.map(importedMonthlySettlementData,
					ImportedMonthlySettlement.class);

			final LocalDate beginOfMonth = LocalDate.of(importedMonthlySettlement.getYear(),
					importedMonthlySettlement.getMonth(), 1);
			final LocalDate endOfMonth = beginOfMonth.with(TemporalAdjusters.lastDayOfMonth());

			final Group group = this.accessRelationService.getAccessor(userId, endOfMonth);

			Capitalsource capitalsource = importedMonthlySettlement.getCapitalsource();
			if (capitalsource != null) {
				final CapitalsourceID capitalsourceId = capitalsource.getId();
				capitalsource = this.capitalsourceService.getCapitalsourceById(userId, group.getId(), capitalsourceId);
				importedMonthlySettlement.setCapitalsource(capitalsource);
			}

			return importedMonthlySettlement;
		}
		return null;
	}

	private final List<ImportedMonthlySettlement> mapImportedMonthlySettlementDataList(final UserID userId,
			final List<ImportedMonthlySettlementData> importedMonthlySettlementDataList) {
		return importedMonthlySettlementDataList.stream()
				.map(element -> this.mapImportedMonthlySettlementData(userId, element))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public ValidationResult validateImportedMonthlySettlement(
			final ImportedMonthlySettlement importedMonthlySettlement) {
		final ValidationResult validationResult = new ValidationResult();
		return validationResult;
	}

	@Override
	public List<ImportedMonthlySettlement> getImportedMonthlySettlementsByMonth(final UserID userId, final Short year,
			final Month month) {
		Assert.notNull(userId);
		Assert.notNull(year);
		Assert.notNull(month);

		final List<ImportedMonthlySettlementData> importedMonthlySettlementDataList = this.importedMonthlySettlementDao
				.getImportedMonthlySettlementsByMonth(year, (short) month.getValue());
		return this.mapImportedMonthlySettlementDataList(userId, importedMonthlySettlementDataList);
	}

	@Override
	public void createImportedMonthlySettlement(final ImportedMonthlySettlement importedMonthlySettlement) {
		Assert.notNull(importedMonthlySettlement);

		final ImportedMonthlySettlementData importedMonthlySettlementData = super.map(importedMonthlySettlement,
				ImportedMonthlySettlementData.class);
		this.importedMonthlySettlementDao.createImportedMonthlySettlement(importedMonthlySettlementData);

	}

}
