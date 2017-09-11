package org.laladev.moneyjinn.server.controller.impl;

import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import org.laladev.moneyjinn.core.rest.model.importedbalance.CreateImportedBalanceRequest;
import org.laladev.moneyjinn.core.rest.model.transport.ImportedBalanceTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.model.BankAccount;
import org.laladev.moneyjinn.model.ImportedBalance;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceImport;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.server.controller.mapper.ImportedBalanceTransportMapper;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.IImportedBalanceService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.time.LocalDateTime;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequestMapping("/moneyflow/server/importedbalance/")
public class ImportedBalanceController extends AbstractController {

	@Inject
	private ICapitalsourceService capitalsourceService;
	@Inject
	private IImportedBalanceService importedBalanceService;

	@Override
	protected void addBeanMapper() {
		super.registerBeanMapper(new ImportedBalanceTransportMapper());
	}

	@RequestMapping(value = "createImportedBalance", method = { RequestMethod.POST })
	public ValidationResponse createImportedBalance(@RequestBody final CreateImportedBalanceRequest request) {
		final ImportedBalanceTransport importedBalanceTransport = request.getImportedBalanceTransport();

		final ImportedBalance importedBalance = super.map(importedBalanceTransport, ImportedBalance.class);

		final BankAccount bankAccount = new BankAccount(importedBalanceTransport.getAccountNumberCapitalsource(),
				importedBalanceTransport.getBankCodeCapitalsource());

		final LocalDateTime now = LocalDateTime.now();
		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceByAccount(null, bankAccount,
				now.toLocalDate());
		if (capitalsource != null) {
			if (capitalsource.getImportAllowed() == CapitalsourceImport.NOT_ALLOWED) {
				throw new BusinessException("Import of this capitalsource is not allowed!",
						ErrorCode.CAPITALSOURCE_IMPORT_NOT_ALLOWED);
			}
			importedBalance.setCapitalsource(capitalsource);
			importedBalance.setDate(now);

			final ValidationResult validationResult = this.importedBalanceService
					.validateImportedBalance(importedBalance);

			if (validationResult.isValid()) {
				this.importedBalanceService.upsertImportedBalance(importedBalance);
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
