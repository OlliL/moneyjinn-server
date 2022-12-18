//Copyright (c) 2021 Oliver Lehmann <lehmann@ans-netz.de>
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

import java.util.List;

import jakarta.inject.Inject;

import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import org.laladev.moneyjinn.core.rest.model.importedmoneyflowreceipt.CreateImportedMoneyflowReceiptsRequest;
import org.laladev.moneyjinn.core.rest.model.importedmoneyflowreceipt.ShowImportImportedMoneyflowReceiptsResponse;
import org.laladev.moneyjinn.core.rest.model.importedmoneyflowreceipt.transport.ImportedMoneyflowReceiptTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowReceipt;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowReceiptID;
import org.laladev.moneyjinn.model.moneyflow.Moneyflow;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowID;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowReceipt;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowReceiptType;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.server.annotation.RequiresAuthorization;
import org.laladev.moneyjinn.server.controller.mapper.ImportedMoneyflowReceiptTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.ValidationItemTransportMapper;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.IImportedMoneyflowReceiptService;
import org.laladev.moneyjinn.service.api.IMoneyflowReceiptService;
import org.laladev.moneyjinn.service.api.IMoneyflowService;
import org.laladev.moneyjinn.service.api.IUserService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequestMapping("/moneyflow/server/importedmoneyflowreceipt/")
public class ImportedMoneyflowReceiptController extends AbstractController {
	@Inject
	private IImportedMoneyflowReceiptService importedMoneyflowReceiptService;
	@Inject
	private IMoneyflowReceiptService moneyflowReceiptService;
	@Inject
	private IMoneyflowService moneyflowService;
	@Inject
	private IAccessRelationService accessRelationService;
	@Inject
	private IUserService userService;

	private static final String MEDIA_TYPE_IMAGE_JPEG = "image/jpeg";
	private static final String MEDIA_TYPE_APPLICATION_PDF = "application/pdf";

	@Override
	protected void addBeanMapper() {
		this.registerBeanMapper(new ImportedMoneyflowReceiptTransportMapper());
		this.registerBeanMapper(new ValidationItemTransportMapper());
	}

	@RequestMapping(value = "createImportedMoneyflowReceipts", method = { RequestMethod.POST })
	@RequiresAuthorization
	public ValidationResponse createImportedMoneyflowReceipts(
			@RequestBody final CreateImportedMoneyflowReceiptsRequest request) {
		final UserID userId = super.getUserId();
		final User user = this.userService.getUserById(userId);
		final Group group = this.accessRelationService.getAccessor(userId);

		final ValidationResult validationResult = new ValidationResult();
		final List<ImportedMoneyflowReceipt> importedMoneyflowReceipts = super.mapList(
				request.getImportedMoneyflowReceiptTransports(), ImportedMoneyflowReceipt.class);

		importedMoneyflowReceipts.stream().forEach(iMR -> {
			iMR.setUser(user);
			iMR.setAccess(group);
			iMR.setId(null);
			validationResult
					.mergeValidationResult(this.importedMoneyflowReceiptService.validateImportedMoneyflowReceipt(iMR));
		});

		if (!validationResult.isValid()) {
			final ValidationResponse response = new ValidationResponse();
			response.setResult(false);
			response.setValidationItemTransports(
					super.mapList(validationResult.getValidationResultItems(), ValidationItemTransport.class));
			return response;
		}

		importedMoneyflowReceipts.stream()
				.forEach(iMR -> this.importedMoneyflowReceiptService.createImportedMoneyflowReceipt(iMR));

		return null;
	}

	@RequestMapping(value = "showImportImportedMoneyflowReceipts", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowImportImportedMoneyflowReceiptsResponse showImportImportedMoneyflowReceipts() {
		final UserID userId = super.getUserId();
		final Group group = this.accessRelationService.getAccessor(userId);

		final ShowImportImportedMoneyflowReceiptsResponse response = new ShowImportImportedMoneyflowReceiptsResponse();
		final List<ImportedMoneyflowReceipt> allImportedMoneyflowReceipts = this.importedMoneyflowReceiptService
				.getAllImportedMoneyflowReceipts(userId, group.getId());
		final List<ImportedMoneyflowReceiptTransport> allImportedMoneyflowReceiptTransports = super.mapList(
				allImportedMoneyflowReceipts, ImportedMoneyflowReceiptTransport.class);
		response.setImportedMoneyflowReceiptTransports(allImportedMoneyflowReceiptTransports);

		return response;
	}

	@RequestMapping(value = "deleteImportedMoneyflowReceiptById/{id}", method = { RequestMethod.DELETE })
	@RequiresAuthorization
	public void deleteImportedMoneyflowReceiptById(@PathVariable(value = "id") final Long id) {
		final UserID userId = super.getUserId();
		final Group group = this.accessRelationService.getAccessor(userId);

		final ImportedMoneyflowReceiptID importedMoneyflowReceiptId = new ImportedMoneyflowReceiptID(id);
		this.importedMoneyflowReceiptService.deleteImportedMoneyflowReceipt(userId, group.getId(),
				importedMoneyflowReceiptId);
	}

	@RequestMapping(value = "importImportedMoneyflowReceipt/{id}/{moneyflowid}", method = { RequestMethod.POST })
	@RequiresAuthorization
	public void importImportedMoneyflowReceipt(@PathVariable(value = "id") final Long id,
			@PathVariable(value = "moneyflowid") final Long moneyflowid) {
		final UserID userId = super.getUserId();
		final Group group = this.accessRelationService.getAccessor(userId);

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

	}
}
