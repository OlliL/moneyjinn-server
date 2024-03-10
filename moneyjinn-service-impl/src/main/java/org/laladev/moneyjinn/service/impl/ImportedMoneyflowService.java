//
// Copyright (c) 2015-2023 Oliver Lehmann <lehmann@ans-netz.de>
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
import java.util.List;

import org.laladev.moneyjinn.model.access.AccessRelation;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflow;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowID;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowStatus;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.IImportedMoneyflowService;
import org.laladev.moneyjinn.service.api.IUserService;
import org.laladev.moneyjinn.service.dao.ImportedMoneyflowDao;
import org.laladev.moneyjinn.service.dao.data.ImportedMoneyflowData;
import org.laladev.moneyjinn.service.dao.data.mapper.ImportedMoneyflowDataMapper;
import org.laladev.moneyjinn.service.dao.data.mapper.ImportedMoneyflowStatusMapper;
import org.springframework.util.Assert;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;

@Named
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ImportedMoneyflowService extends AbstractService implements IImportedMoneyflowService {
	private static final String IMPORTED_MONEYFLOW_ID_MUST_NOT_BE_NULL = "importedMoneyflowId must not be null!";
	private static final String USER_ID_MUST_NOT_BE_NULL = "UserId must not be null!";
	private final ImportedMoneyflowDao importedMoneyflowDao;
	private final IUserService userService;
	private final ICapitalsourceService capitalsourceService;
	private final IAccessRelationService accessRelationService;
	private final ImportedMoneyflowDataMapper importedMoneyflowDataMapper;

	@Override
	@PostConstruct
	protected void addBeanMapper() {
		super.registerBeanMapper(this.importedMoneyflowDataMapper);
	}

	private ImportedMoneyflow mapImportedMoneyflowData(final UserID userId,
			final ImportedMoneyflowData importedMoneyflowData) {
		if (importedMoneyflowData != null) {
			final ImportedMoneyflow importedMoneyflow = super.map(importedMoneyflowData, ImportedMoneyflow.class);
			importedMoneyflow.setUser(new User(userId));

			this.userService.enrichEntity(importedMoneyflow);
			this.accessRelationService.enrichEntity(importedMoneyflow, importedMoneyflow.getBookingDate());
			this.capitalsourceService.enrichEntity(importedMoneyflow);

			return importedMoneyflow;
		}
		return null;
	}

	private final List<ImportedMoneyflow> mapImportedMoneyflowDataList(final UserID userId,
			final List<ImportedMoneyflowData> importedMoneyflowDataList) {
		return importedMoneyflowDataList.stream().map(element -> this.mapImportedMoneyflowData(userId, element))
				.toList();
	}

	@Override
	public ValidationResult validateImportedMoneyflow(final ImportedMoneyflow importedMoneyflow) {
		return new ValidationResult();
	}

	@Override
	public ImportedMoneyflow getImportedMoneyflowById(final UserID userId,
			final ImportedMoneyflowID importedMoneyflowId) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(importedMoneyflowId, IMPORTED_MONEYFLOW_ID_MUST_NOT_BE_NULL);
		final ImportedMoneyflowData importedMoneyflowData = this.importedMoneyflowDao
				.getImportedMoneyflowById(importedMoneyflowId.getId());
		return this.mapImportedMoneyflowData(userId, importedMoneyflowData);
	}

	@Override
	public Integer countImportedMoneyflows(final UserID userId, final List<CapitalsourceID> capitalsourceIds,
			final ImportedMoneyflowStatus status) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(capitalsourceIds, "capitalsourceIds must not be null!");
		final List<Long> capitalsourceIdLongs = capitalsourceIds.stream().map(CapitalsourceID::getId).toList();
		return this.importedMoneyflowDao.countImportedMoneyflows(capitalsourceIdLongs,
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
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(capitalsourceIds, "capitalsourceIds must not be null!");

		final AccessRelation accessRelation = this.accessRelationService.getCurrentAccessRelationById(userId);

		LocalDate firewalledDateFrom;
		LocalDate firewalledDateTil;
		if (dateFrom == null) {
			firewalledDateFrom = accessRelation.getValidFrom();
		} else {
			firewalledDateFrom = accessRelation.getValidFrom().isAfter(dateFrom) ? accessRelation.getValidFrom()
					: dateFrom;
		}
		if (dateTil == null) {
			firewalledDateTil = accessRelation.getValidTil();
		} else {
			firewalledDateTil = dateTil.isAfter(accessRelation.getValidTil()) ? accessRelation.getValidTil() : dateTil;
		}

		final List<Long> capitalsourceIdLongs = capitalsourceIds.stream().map(CapitalsourceID::getId).toList();
		final List<ImportedMoneyflowData> importedMoneyflowDatas = this.importedMoneyflowDao
				.getAllImportedMoneyflowsByCapitalsourceIds(capitalsourceIdLongs,
						ImportedMoneyflowStatusMapper.map(status), firewalledDateFrom, firewalledDateTil);
		return this.mapImportedMoneyflowDataList(userId, importedMoneyflowDatas);
	}

	@Override
	public void createImportedMoneyflow(final ImportedMoneyflow importedMoneyflow) {
		Assert.notNull(importedMoneyflow, "importedMoneyflow must not be null!");
		if (!this.checkIfExternalIdAlreadyExists(importedMoneyflow.getExternalId())) {
			final ImportedMoneyflowData importedMoneyflowData = super.map(importedMoneyflow,
					ImportedMoneyflowData.class);
			this.importedMoneyflowDao.createImportedMoneyflow(importedMoneyflowData);
		}
	}

	@Override
	public boolean checkIfExternalIdAlreadyExists(final String externalId) {
		Assert.notNull(externalId, "externalId must not be null!");
		return this.importedMoneyflowDao.checkIfExternalIdAlreadyExists(externalId);
	}

	@Override
	public void updateImportedMoneyflowStatus(final UserID userId, final ImportedMoneyflowID importedMoneyflowId,
			final ImportedMoneyflowStatus status) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(importedMoneyflowId, IMPORTED_MONEYFLOW_ID_MUST_NOT_BE_NULL);
		Assert.notNull(status, "status must not be null!");
		this.importedMoneyflowDao.updateImportedMoneyflowStatus(importedMoneyflowId.getId(),
				ImportedMoneyflowStatusMapper.map(status));
	}

	@Override
	public void deleteImportedMoneyflowById(final UserID userId, final ImportedMoneyflowID importedMoneyflowId) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(importedMoneyflowId, IMPORTED_MONEYFLOW_ID_MUST_NOT_BE_NULL);
		this.importedMoneyflowDao.deleteImportedMoneyflowById(importedMoneyflowId.getId());
	}
}
