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

package org.laladev.moneyjinn.businesslogic.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.laladev.moneyjinn.businesslogic.dao.ImportedMoneyflowDao;
import org.laladev.moneyjinn.businesslogic.dao.data.ImportedMoneyflowData;
import org.laladev.moneyjinn.businesslogic.dao.data.mapper.ImportedMoneyflowDataMapper;
import org.laladev.moneyjinn.businesslogic.dao.data.mapper.ImportedMoneyflowStatusMapper;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflow;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowID;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowStatus;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.IImportedMoneyflowService;
import org.springframework.util.Assert;

@Named
public class ImportedMoneyflowService extends AbstractService implements IImportedMoneyflowService {
	@Inject
	private ImportedMoneyflowDao importedMoneyflowDao;
	@Inject
	private ICapitalsourceService capitalsourceService;
	@Inject
	private IAccessRelationService accessRelationService;

	@Override
	protected void addBeanMapper() {
		super.registerBeanMapper(new ImportedMoneyflowDataMapper());
	}

	private final ImportedMoneyflow mapImportedMoneyflowData(final UserID userId,
			final ImportedMoneyflowData importedMoneyflowData) {

		if (importedMoneyflowData != null) {
			final ImportedMoneyflow importedMoneyflow = super.map(importedMoneyflowData, ImportedMoneyflow.class);

			final Group group = this.accessRelationService.getAccessor(userId, LocalDate.now());

			Capitalsource capitalsource = importedMoneyflow.getCapitalsource();
			if (capitalsource != null) {
				final CapitalsourceID capitalsourceId = capitalsource.getId();
				capitalsource = this.capitalsourceService.getCapitalsourceById(userId, group.getId(), capitalsourceId);
				importedMoneyflow.setCapitalsource(capitalsource);
			}

			return importedMoneyflow;
		}
		return null;
	}

	private final List<ImportedMoneyflow> mapImportedMoneyflowDataList(final UserID userId,
			final List<ImportedMoneyflowData> importedMoneyflowDataList) {
		return importedMoneyflowDataList.stream().map(element -> this.mapImportedMoneyflowData(userId, element))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public ValidationResult validateImportedMoneyflow(final ImportedMoneyflow importedMoneyflow) {
		return new ValidationResult();
	}

	@Override
	public Integer countImportedMoneyflows(final UserID userId, final List<CapitalsourceID> capitalsourceIds,
			final ImportedMoneyflowStatus status) {
		Assert.notNull(userId);
		Assert.notNull(capitalsourceIds);

		final List<Long> capitalsourceIdLongs = capitalsourceIds.stream().map(CapitalsourceID::getId)
				.collect(Collectors.toCollection(ArrayList::new));

		return this.importedMoneyflowDao.countImportedMoneyflows(userId.getId(), capitalsourceIdLongs,
				ImportedMoneyflowStatusMapper.map(status));
	}

	@Override
	public List<ImportedMoneyflow> getAllImportedMoneyflowsByCapitalsourceIds(final UserID userId,
			final List<CapitalsourceID> capitalsourceIds, final LocalDate dateFrom, final LocalDate dateTil) {
		return this.getAllImportedMoneyflowsByCapitalsourceIds(userId, capitalsourceIds, null, dateFrom, dateTil);
	}

	@Override
	public List<ImportedMoneyflow> getAllImportedMoneyflowsByCapitalsourceIds(final UserID userId,
			final List<CapitalsourceID> capitalsourceIds, final ImportedMoneyflowStatus status) {
		return this.getAllImportedMoneyflowsByCapitalsourceIds(userId, capitalsourceIds, status, null, null);
	}

	private List<ImportedMoneyflow> getAllImportedMoneyflowsByCapitalsourceIds(final UserID userId,
			final List<CapitalsourceID> capitalsourceIds, final ImportedMoneyflowStatus status,
			final LocalDate dateFrom, final LocalDate dateTil) {
		Assert.notNull(userId);
		Assert.notNull(capitalsourceIds);

		final List<Long> capitalsourceIdLongs = capitalsourceIds.stream().map(CapitalsourceID::getId)
				.collect(Collectors.toCollection(ArrayList::new));

		final List<ImportedMoneyflowData> importedMoneyflowDatas = this.importedMoneyflowDao
				.getAllImportedMoneyflowsByCapitalsourceIds(userId.getId(), capitalsourceIdLongs,
						ImportedMoneyflowStatusMapper.map(status), dateFrom, dateTil);
		return this.mapImportedMoneyflowDataList(userId, importedMoneyflowDatas);
	}

	@Override
	public void createImportedMoneyflow(final ImportedMoneyflow importedMoneyflow) {
		Assert.notNull(importedMoneyflow);

		final ImportedMoneyflowData importedMoneyflowData = super.map(importedMoneyflow, ImportedMoneyflowData.class);
		this.importedMoneyflowDao.createImportedMoneyflow(importedMoneyflowData);
	}

	@Override
	public void updateImportedMoneyflowStatus(final UserID userId, final ImportedMoneyflowID importedMoneyflowId,
			final ImportedMoneyflowStatus status) {
		Assert.notNull(userId);
		Assert.notNull(importedMoneyflowId);
		Assert.notNull(status);

		this.importedMoneyflowDao.updateImportedMoneyflowStatus(userId.getId(), importedMoneyflowId.getId(),
				ImportedMoneyflowStatusMapper.map(status));
	}

	@Override
	public void deleteImportedMoneyflowById(final UserID userId, final ImportedMoneyflowID importedMoneyflowId) {
		Assert.notNull(userId);
		Assert.notNull(importedMoneyflowId);

		this.importedMoneyflowDao.deleteImportedMoneyflowById(userId.getId(), importedMoneyflowId.getId());
	}

}
