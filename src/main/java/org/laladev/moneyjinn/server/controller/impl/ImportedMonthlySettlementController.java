package org.laladev.moneyjinn.server.controller.impl;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import javax.inject.Inject;

import org.laladev.moneyjinn.businesslogic.model.BankAccount;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.businesslogic.model.exception.BusinessException;
import org.laladev.moneyjinn.businesslogic.model.monthlysettlement.ImportedMonthlySettlement;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResult;
import org.laladev.moneyjinn.businesslogic.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.businesslogic.service.api.IImportedMonthlySettlementService;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import org.laladev.moneyjinn.core.rest.model.importedmonthlysettlement.CreateImportedMonthlySettlementRequest;
import org.laladev.moneyjinn.core.rest.model.transport.ImportedMonthlySettlementTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.server.controller.mapper.ImportedMonthlySettlementTransportMapper;
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
	ICapitalsourceService capitalsourceService;
	@Inject
	IImportedMonthlySettlementService importedMonthlySettlementService;

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
			if (!capitalsource.isImportAllowed()) {
				throw new BusinessException("Import of this capitalsource is not allowed!",
						ErrorCode.CAPITALSOURCE_IMPORT_NOT_ALLOWED);
			}
			importedMonthlySettlement.setCapitalsource(capitalsource);

			final ValidationResult validationResult = this.importedMonthlySettlementService
					.validateImportedMonthlySettlement(importedMonthlySettlement);

			if (validationResult.isValid()) {
				this.importedMonthlySettlementService.createImportedMonthlySettlement(importedMonthlySettlement);
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