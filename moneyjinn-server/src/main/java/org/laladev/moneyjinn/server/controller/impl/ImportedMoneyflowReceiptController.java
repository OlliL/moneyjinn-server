//Copyright (c) 2021-2025 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.server.controller.impl;

import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.model.moneyflow.*;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.server.controller.api.ImportedMoneyflowReceiptControllerApi;
import org.laladev.moneyjinn.server.controller.mapper.ImportedMoneyflowReceiptTransportMapper;
import org.laladev.moneyjinn.server.model.CreateImportedMoneyflowReceiptsRequest;
import org.laladev.moneyjinn.server.model.ImportedMoneyflowReceiptTransport;
import org.laladev.moneyjinn.server.model.ShowImportImportedMoneyflowReceiptsResponse;
import org.laladev.moneyjinn.service.api.*;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ImportedMoneyflowReceiptController extends AbstractController
        implements ImportedMoneyflowReceiptControllerApi {
    private static final String MEDIA_TYPE_IMAGE_JPEG = "image/jpeg";
    private static final String MEDIA_TYPE_APPLICATION_PDF = "application/pdf";
    private final IImportedMoneyflowReceiptService importedMoneyflowReceiptService;
    private final IMoneyflowReceiptService moneyflowReceiptService;
    private final IMoneyflowService moneyflowService;
    private final IAccessRelationService accessRelationService;
    private final IUserService userService;
    private final ImportedMoneyflowReceiptTransportMapper importedMoneyflowReceiptTransportMapper;

    @Override
    public ResponseEntity<Void> createImportedMoneyflowReceipts(
            @RequestBody final CreateImportedMoneyflowReceiptsRequest request) {
        final UserID userId = super.getUserId();
        final User user = this.userService.getUserById(userId);
        final Group group = this.accessRelationService.getCurrentGroup(userId);
        final ValidationResult validationResult = new ValidationResult();
        final List<ImportedMoneyflowReceipt> importedMoneyflowReceipts = this.importedMoneyflowReceiptTransportMapper
                .mapBToA(request.getImportedMoneyflowReceiptTransports());
        importedMoneyflowReceipts.stream().forEach(imr -> {
            imr.setUser(user);
            imr.setGroup(group);
            imr.setId(null);
            validationResult
                    .mergeValidationResult(this.importedMoneyflowReceiptService.validateImportedMoneyflowReceipt(imr));
        });

        this.throwValidationExceptionIfInvalid(validationResult);

        importedMoneyflowReceipts.stream()
                .forEach(this.importedMoneyflowReceiptService::createImportedMoneyflowReceipt);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ShowImportImportedMoneyflowReceiptsResponse> showImportImportedMoneyflowReceipts() {
        final UserID userId = super.getUserId();
        final Group group = this.accessRelationService.getCurrentGroup(userId);
        final ShowImportImportedMoneyflowReceiptsResponse response = new ShowImportImportedMoneyflowReceiptsResponse();
        final List<ImportedMoneyflowReceipt> allImportedMoneyflowReceipts = this.importedMoneyflowReceiptService
                .getAllImportedMoneyflowReceipts(userId, group.getId());
        final List<ImportedMoneyflowReceiptTransport> allImportedMoneyflowReceiptTransports = this.importedMoneyflowReceiptTransportMapper
                .mapAToB(allImportedMoneyflowReceipts);
        response.setImportedMoneyflowReceiptTransports(allImportedMoneyflowReceiptTransports);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> deleteImportedMoneyflowReceiptById(final Long id) {
        final UserID userId = super.getUserId();
        final Group group = this.accessRelationService.getCurrentGroup(userId);
        final ImportedMoneyflowReceiptID importedMoneyflowReceiptId = new ImportedMoneyflowReceiptID(id);

        this.importedMoneyflowReceiptService.deleteImportedMoneyflowReceipt(userId, group.getId(),
                importedMoneyflowReceiptId);

        return ResponseEntity.noContent().build();

    }

    @Override
    public ResponseEntity<Void> importImportedMoneyflowReceipt(final Long id,
                                                               final Long moneyflowid) {
        final UserID userId = super.getUserId();
        final Group group = this.accessRelationService.getCurrentGroup(userId);
        final ImportedMoneyflowReceiptID importedMoneyflowReceiptId = new ImportedMoneyflowReceiptID(id);
        final MoneyflowID moneyflowId = new MoneyflowID(moneyflowid);
        final ImportedMoneyflowReceipt importedMoneyflowReceipt = this.importedMoneyflowReceiptService
                .getImportedMoneyflowReceiptById(userId, group.getId(), importedMoneyflowReceiptId);
        final Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);
        final MoneyflowReceipt checkMoneyflowReceipt = this.moneyflowReceiptService.getMoneyflowReceipt(userId,
                moneyflowId);

        if (checkMoneyflowReceipt != null) {
            throw new BusinessException("Moneyflow already has a receipt attached!", ErrorCode.RECEIPT_ALREADY_EXISTS);
        }

        if (importedMoneyflowReceipt != null && moneyflow != null) {
            final MoneyflowReceipt moneyflowReceipt = new MoneyflowReceipt();
            moneyflowReceipt.setMoneyflowId(moneyflowId);
            moneyflowReceipt.setReceipt(importedMoneyflowReceipt.getReceipt());
            if (MEDIA_TYPE_IMAGE_JPEG.equals(importedMoneyflowReceipt.getMediaType())) {
                moneyflowReceipt.setMoneyflowReceiptType(MoneyflowReceiptType.JPEG);
            } else if (MEDIA_TYPE_APPLICATION_PDF.equals(importedMoneyflowReceipt.getMediaType())) {
                moneyflowReceipt.setMoneyflowReceiptType(MoneyflowReceiptType.PDF);
            } else {
                throw new BusinessException("Media Type not supported", ErrorCode.UNSUPPORTED_MEDIA_TYPE);
            }
            this.moneyflowReceiptService.createMoneyflowReceipt(userId, moneyflowReceipt);
            this.importedMoneyflowReceiptService.deleteImportedMoneyflowReceipt(userId, group.getId(),
                    importedMoneyflowReceiptId);
        }

        return ResponseEntity.noContent().build();

    }
}
