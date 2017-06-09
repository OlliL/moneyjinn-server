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

package org.laladev.moneyjinn.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.laladev.moneyjinn.model.ImportedBalance;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.IImportedBalanceService;
import org.laladev.moneyjinn.service.dao.ImportedBalanceDao;
import org.laladev.moneyjinn.service.dao.data.ImportedBalanceData;
import org.laladev.moneyjinn.service.dao.data.mapper.ImportedBalanceDataMapper;
import org.springframework.util.Assert;

@Named
public class ImportedBalanceService extends AbstractService implements IImportedBalanceService {
	@Inject
	private ImportedBalanceDao importedBalanceDao;
	@Inject
	private ICapitalsourceService capitalsourceService;
	@Inject
	private IAccessRelationService accessRelationService;

	@Override
	protected void addBeanMapper() {
		super.registerBeanMapper(new ImportedBalanceDataMapper());
	}

	private final ImportedBalance mapImportedBalanceData(final UserID userId,
			final ImportedBalanceData importedBalanceData) {

		if (importedBalanceData != null) {
			final ImportedBalance importedBalance = super.map(importedBalanceData, ImportedBalance.class);

			final Group group = this.accessRelationService.getAccessor(userId, importedBalance.getDate().toLocalDate());

			Capitalsource capitalsource = importedBalance.getCapitalsource();
			if (capitalsource != null) {
				final CapitalsourceID capitalsourceId = capitalsource.getId();
				capitalsource = this.capitalsourceService.getCapitalsourceById(userId, group.getId(), capitalsourceId);
				importedBalance.setCapitalsource(capitalsource);
			}

			return importedBalance;
		}
		return null;
	}

	private final List<ImportedBalance> mapImportedBalanceDataList(final UserID userId,
			final List<ImportedBalanceData> importedBalanceDataList) {
		return importedBalanceDataList.stream().map(element -> this.mapImportedBalanceData(userId, element))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public ValidationResult validateImportedBalance(final ImportedBalance importedBalance) {
		return new ValidationResult();
	}

	@Override
	public List<ImportedBalance> getAllImportedBalancesByCapitalsourceIds(final UserID userId,
			final List<CapitalsourceID> capitalsourceIds) {
		Assert.notNull(userId, "UserId must not be null!");
		Assert.notNull(capitalsourceIds, "capitalsourceIds must not be null!");

		final List<Long> capitalsourceIdLongs = capitalsourceIds.stream().map(CapitalsourceID::getId)
				.collect(Collectors.toCollection(ArrayList::new));

		final List<ImportedBalanceData> importedBalanceDataList = this.importedBalanceDao
				.getAllImportedBalancesByCapitalsourceIds(userId.getId(), capitalsourceIdLongs);
		return this.mapImportedBalanceDataList(userId, importedBalanceDataList);
	}

	@Override
	public void upsertImportedBalance(final ImportedBalance importedBalance) {
		Assert.notNull(importedBalance, "importedBalance must not be null!");

		final ImportedBalanceData importedBalanceData = super.map(importedBalance, ImportedBalanceData.class);
		this.importedBalanceDao.upsertImportedBalance(importedBalanceData);

	}

}
