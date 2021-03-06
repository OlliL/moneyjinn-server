//Copyright (c) 2015-2017 Oliver Lehmann <lehmann@ans-netz.de>
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

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import javax.inject.Inject;

import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import org.laladev.moneyjinn.core.rest.model.importedmonthlysettlement.CreateImportedMonthlySettlementRequest;
import org.laladev.moneyjinn.core.rest.model.transport.ImportedMonthlySettlementTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.model.BankAccount;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceImport;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.model.monthlysettlement.ImportedMonthlySettlement;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.server.controller.mapper.ImportedMonthlySettlementTransportMapper;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.IImportedMonthlySettlementService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequestMapping("/moneyflow/server/importedmonthlysettlement/")
public class ImportedMonthlySettlementController extends AbstractController {

	@Inject
	private ICapitalsourceService capitalsourceService;
	@Inject
	private IImportedMonthlySettlementService importedMonthlySettlementService;

	@Override
	protected void addBeanMapper() {
		super.registerBeanMapper(new ImportedMonthlySettlementTransportMapper());
	}

	@RequestMapping(value = "createImportedMonthlySettlement", method = { RequestMethod.POST })
	public ValidationResponse createImportedMonthlySettlement(
			@RequestBody final CreateImportedMonthlySettlementRequest request) {
		final ImportedMonthlySettlementTransport importedMonthlySettlementTransport = request
				.getImportedMonthlySettlementTransport();

		final ImportedMonthlySettlement importedMonthlySettlement = super.map(importedMonthlySettlementTransport,
				ImportedMonthlySettlement.class);

		final BankAccount bankAccount = new BankAccount(
				importedMonthlySettlementTransport.getAccountNumberCapitalsource(),
				importedMonthlySettlementTransport.getBankCodeCapitalsource());

		final LocalDate beginOfMonth = LocalDate.of(importedMonthlySettlement.getYear(),
				importedMonthlySettlement.getMonth(), 1);
		final LocalDate endOfMonth = beginOfMonth.with(TemporalAdjusters.lastDayOfMonth());
		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceByAccount(null, bankAccount,
				endOfMonth);
		if (capitalsource != null) {
			if (capitalsource.getImportAllowed() == CapitalsourceImport.NOT_ALLOWED) {
				throw new BusinessException("Import of this capitalsource is not allowed!",
						ErrorCode.CAPITALSOURCE_IMPORT_NOT_ALLOWED);
			}
			importedMonthlySettlement.setCapitalsource(capitalsource);

			final ValidationResult validationResult = this.importedMonthlySettlementService
					.validateImportedMonthlySettlement(importedMonthlySettlement);

			if (validationResult.isValid()) {
				this.importedMonthlySettlementService.upsertImportedMonthlySettlement(importedMonthlySettlement);
			} else {
				final ValidationResponse response = new ValidationResponse();
				response.setResult(false);
				response.setValidationItemTransports(
						super.mapList(validationResult.getValidationResultItems(), ValidationItemTransport.class));
				return response;
			}
		} else {
			throw new BusinessException("No matching capitalsource found!", ErrorCode.CAPITALSOURCE_NOT_FOUND);
		}
		return null;
	}
}
