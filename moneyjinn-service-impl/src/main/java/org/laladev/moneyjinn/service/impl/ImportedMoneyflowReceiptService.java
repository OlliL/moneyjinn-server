//
// Copyright (c) 2021-2023 Oliver Lehmann <lehmann@ans-netz.de>
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

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.apache.tika.Tika;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowReceipt;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowReceiptID;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.service.api.IImportedMoneyflowReceiptService;
import org.laladev.moneyjinn.service.dao.ImportedMoneyflowReceiptDao;
import org.laladev.moneyjinn.service.dao.data.ImportedMoneyflowReceiptData;
import org.laladev.moneyjinn.service.dao.data.mapper.ImportedMoneyflowReceiptDataMapper;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.util.Assert;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;

@Named
@EnableCaching
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ImportedMoneyflowReceiptService extends AbstractService implements IImportedMoneyflowReceiptService {
	private static final String USER_ID_MUST_NOT_BE_NULL = "UserId must not be null!";
	private static final String GROUP_ID_MUST_NOT_BE_NULL = "GroupId must not be null!";
	private static final String MEDIA_TYPE_IMAGE_JPEG = "image/jpeg";
	private static final String MEDIA_TYPE_APPLICATION_PDF = "application/pdf";
	private static final List<String> SUPPORTED_MEDIA_TYPES = Arrays.asList(MEDIA_TYPE_APPLICATION_PDF,
			MEDIA_TYPE_IMAGE_JPEG);

	private final Tika tika = new Tika();

	private final ImportedMoneyflowReceiptDao importedMoneyflowReceiptDao;
	private final ImportedMoneyflowReceiptDataMapper importedMoneyflowReceiptDataMapper;

	@Override
	@PostConstruct
	protected void addBeanMapper() {
		super.registerBeanMapper(this.importedMoneyflowReceiptDataMapper);
	}

	@Override
	public ValidationResult validateImportedMoneyflowReceipt(final ImportedMoneyflowReceipt importedMoneyflowReceipt) {
		Assert.notNull(importedMoneyflowReceipt, "Imported Moneyflow Receipt must not be null!");
		Assert.notNull(importedMoneyflowReceipt.getUser(), "Imported Moneyflow Receipt.user must not be null!");
		Assert.notNull(importedMoneyflowReceipt.getUser().getId(),
				"Imported Moneyflow Receipt.user.id must not be null!");
		Assert.notNull(importedMoneyflowReceipt.getGroup(), "Imported Moneyflow Receipt.access must not be null!");
		Assert.notNull(importedMoneyflowReceipt.getGroup().getId(),
				"Imported Moneyflow Receipt.access.id must not be null!");
		Assert.notNull(importedMoneyflowReceipt.getReceipt(), "Imported Moneyflow Receipt.receipt must not be null!");
		Assert.notNull(importedMoneyflowReceipt.getFilename(), "Imported Moneyflow Receipt.filename must not be null!");

		final ValidationResult validationResult = new ValidationResult();
		final Consumer<ErrorCode> addResult = (final ErrorCode errorCode) -> validationResult.addValidationResultItem(
				new ValidationResultItem(importedMoneyflowReceipt.getId(), errorCode));

		this.prepareImportedMoneyflowReceipt(importedMoneyflowReceipt);

		if (!SUPPORTED_MEDIA_TYPES.contains(importedMoneyflowReceipt.getMediaType())) {
			addResult.accept(ErrorCode.UNSUPPORTED_MEDIA_TYPE);
		}

		return validationResult;
	}

	private void prepareImportedMoneyflowReceipt(final ImportedMoneyflowReceipt importedMoneyflowReceipt) {
		final String mediaType = this.tika.detect(importedMoneyflowReceipt.getReceipt());
		importedMoneyflowReceipt.setMediaType(mediaType);
	}

	@Override
	public List<ImportedMoneyflowReceipt> getAllImportedMoneyflowReceipts(final UserID userId, final GroupID groupId) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(groupId, GROUP_ID_MUST_NOT_BE_NULL);
		final List<ImportedMoneyflowReceiptData> importedMoneyflowReceipts = this.importedMoneyflowReceiptDao
				.getAllImportedMoneyflowReceipts(groupId.getId());
		return super.mapList(importedMoneyflowReceipts, ImportedMoneyflowReceipt.class);
	}

	@Override
	public ImportedMoneyflowReceiptID createImportedMoneyflowReceipt(
			final ImportedMoneyflowReceipt importedMoneyflowReceipt) {
		Assert.notNull(importedMoneyflowReceipt, "Imported Moneyflow Receipt must not be null!");
		importedMoneyflowReceipt.setId(null);
		final ValidationResult validationResult = this.validateImportedMoneyflowReceipt(importedMoneyflowReceipt);
		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("Imported Moneyflow Receipt creation failed!", validationResultItem.getError());
		}
		final ImportedMoneyflowReceiptData importedMoneyflowReceiptData = super.map(importedMoneyflowReceipt,
				ImportedMoneyflowReceiptData.class);
		this.importedMoneyflowReceiptDao.createImportedMoneyflowReceipt(importedMoneyflowReceiptData);
		return new ImportedMoneyflowReceiptID(importedMoneyflowReceiptData.getId());
	}

	@Override
	public void deleteImportedMoneyflowReceipt(final UserID userId, final GroupID groupId,
			final ImportedMoneyflowReceiptID importedMoneyflowReceiptId) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(groupId, GROUP_ID_MUST_NOT_BE_NULL);
		Assert.notNull(importedMoneyflowReceiptId, "ImportedMoneyflowReceiptID must not be null!");
		this.importedMoneyflowReceiptDao.deleteImportedMoneyflowReceipt(groupId.getId(),
				importedMoneyflowReceiptId.getId());
	}

	@Override
	public ImportedMoneyflowReceipt getImportedMoneyflowReceiptById(final UserID userId, final GroupID groupId,
			final ImportedMoneyflowReceiptID importedMoneyflowReceiptId) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(groupId, GROUP_ID_MUST_NOT_BE_NULL);
		Assert.notNull(importedMoneyflowReceiptId, "ImportedMoneyflowReceiptID must not be null!");
		final ImportedMoneyflowReceiptData importedMoneyflowReceipt = this.importedMoneyflowReceiptDao
				.getImportedMoneyflowReceiptById(groupId.getId(), importedMoneyflowReceiptId.getId());
		return super.map(importedMoneyflowReceipt, ImportedMoneyflowReceipt.class);
	}
}
