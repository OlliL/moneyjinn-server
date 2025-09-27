//
// Copyright (c) 2021-2025 Oliver Lehmann <lehmann@ans-netz.de>
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

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static org.springframework.util.Assert.notNull;

@Named
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ImportedMoneyflowReceiptService extends AbstractService implements IImportedMoneyflowReceiptService {
    private static final String MEDIA_TYPE_IMAGE_JPEG = "image/jpeg";
    private static final String MEDIA_TYPE_APPLICATION_PDF = "application/pdf";
    private static final List<String> SUPPORTED_MEDIA_TYPES = Arrays.asList(MEDIA_TYPE_APPLICATION_PDF,
            MEDIA_TYPE_IMAGE_JPEG);

    private final Tika tika = new Tika();

    private final ImportedMoneyflowReceiptDao importedMoneyflowReceiptDao;
    private final ImportedMoneyflowReceiptDataMapper importedMoneyflowReceiptDataMapper;

    @Override
    public ValidationResult validateImportedMoneyflowReceipt(
            @NonNull final ImportedMoneyflowReceipt importedMoneyflowReceipt) {
        notNull(importedMoneyflowReceipt.getUser(), "Imported Moneyflow Receipt.user must not be null!");
        notNull(importedMoneyflowReceipt.getUser().getId(),
                "Imported Moneyflow Receipt.user.id must not be null!");
        notNull(importedMoneyflowReceipt.getGroup(), "Imported Moneyflow Receipt.access must not be null!");
        notNull(importedMoneyflowReceipt.getGroup().getId(),
                "Imported Moneyflow Receipt.access.id must not be null!");
        notNull(importedMoneyflowReceipt.getReceipt(), "Imported Moneyflow Receipt.receipt must not be null!");
        notNull(importedMoneyflowReceipt.getFilename(), "Imported Moneyflow Receipt.filename must not be null!");

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
    public List<ImportedMoneyflowReceipt> getAllImportedMoneyflowReceipts(@NonNull final UserID userId,
                                                                          @NonNull final GroupID groupId) {
        final List<ImportedMoneyflowReceiptData> importedMoneyflowReceipts = this.importedMoneyflowReceiptDao
                .getAllImportedMoneyflowReceipts(groupId.getId());
        return this.importedMoneyflowReceiptDataMapper.mapBToA(importedMoneyflowReceipts);
    }

    @Override
    public ImportedMoneyflowReceiptID createImportedMoneyflowReceipt(
            @NonNull final ImportedMoneyflowReceipt importedMoneyflowReceipt) {
        importedMoneyflowReceipt.setId(null);
        final ValidationResult validationResult = this.validateImportedMoneyflowReceipt(importedMoneyflowReceipt);
        if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
            final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().getFirst();
            throw new BusinessException("Imported Moneyflow Receipt creation failed!", validationResultItem.getError());
        }
        final ImportedMoneyflowReceiptData importedMoneyflowReceiptData = this.importedMoneyflowReceiptDataMapper
                .mapAToB(importedMoneyflowReceipt);
        this.importedMoneyflowReceiptDao.createImportedMoneyflowReceipt(importedMoneyflowReceiptData);
        return new ImportedMoneyflowReceiptID(importedMoneyflowReceiptData.getId());
    }

    @Override
    public void deleteImportedMoneyflowReceipt(@NonNull final UserID userId, @NonNull final GroupID groupId,
                                               @NonNull final ImportedMoneyflowReceiptID importedMoneyflowReceiptId) {
        this.importedMoneyflowReceiptDao.deleteImportedMoneyflowReceipt(groupId.getId(),
                importedMoneyflowReceiptId.getId());
    }

    @Override
    public ImportedMoneyflowReceipt getImportedMoneyflowReceiptById(@NonNull final UserID userId,
                                                                    @NonNull final GroupID groupId,
                                                                    @NonNull final ImportedMoneyflowReceiptID importedMoneyflowReceiptId) {
        final ImportedMoneyflowReceiptData importedMoneyflowReceipt = this.importedMoneyflowReceiptDao
                .getImportedMoneyflowReceiptById(groupId.getId(), importedMoneyflowReceiptId.getId());
        return this.importedMoneyflowReceiptDataMapper.mapBToA(importedMoneyflowReceipt);
    }
}
