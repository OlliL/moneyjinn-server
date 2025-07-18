//Copyright (c) 2015-2025 Oliver Lehmann <lehmann@ans-netz.de>
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.BankAccount;
import org.laladev.moneyjinn.model.Contractpartner;
import org.laladev.moneyjinn.model.ContractpartnerAccount;
import org.laladev.moneyjinn.model.access.AccessRelation;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceImport;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceType;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflow;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowID;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowStatus;
import org.laladev.moneyjinn.model.moneyflow.Moneyflow;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowID;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowSplitEntry;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.server.controller.api.ImportedMoneyflowControllerApi;
import org.laladev.moneyjinn.server.controller.mapper.ImportedMoneyflowTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.MoneyflowSplitEntryTransportMapper;
import org.laladev.moneyjinn.server.model.CreateImportedMoneyflowRequest;
import org.laladev.moneyjinn.server.model.ImportImportedMoneyflowRequest;
import org.laladev.moneyjinn.server.model.ImportedMoneyflowTransport;
import org.laladev.moneyjinn.server.model.ShowAddImportedMoneyflowsResponse;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.IContractpartnerAccountService;
import org.laladev.moneyjinn.service.api.IContractpartnerService;
import org.laladev.moneyjinn.service.api.IImportedMoneyflowService;
import org.laladev.moneyjinn.service.api.IMoneyflowService;
import org.laladev.moneyjinn.service.api.IMoneyflowSplitEntryService;
import org.laladev.moneyjinn.service.api.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ImportedMoneyflowController extends AbstractController implements ImportedMoneyflowControllerApi {
	private final IUserService userService;
	private final IAccessRelationService accessRelationService;
	private final ICapitalsourceService capitalsourceService;
	private final IContractpartnerService contractpartnerService;
	private final IContractpartnerAccountService contractpartnerAccountService;
	private final IMoneyflowService moneyflowService;
	private final IImportedMoneyflowService importedMoneyflowService;
	private final IMoneyflowSplitEntryService moneyflowSplitEntryService;
	private final ImportedMoneyflowTransportMapper importedMoneyflowTransportMapper;
	private final MoneyflowSplitEntryTransportMapper moneyflowSplitEntryTransportMapper;

	private ValidationResult checkIfAmountIsEqual(final Moneyflow moneyflow,
			final List<MoneyflowSplitEntry> moneyflowSplitEntries) {
		final ValidationResult validationResult = new ValidationResult();
		if (!moneyflowSplitEntries.isEmpty()) {
			final BigDecimal sumOfSplitEntriesAmount = moneyflowSplitEntries.stream()
					.map(MoneyflowSplitEntry::getAmount).filter(Objects::nonNull)
					.reduce(BigDecimal.ZERO, BigDecimal::add);
			if (sumOfSplitEntriesAmount.compareTo(moneyflow.getAmount()) != 0) {
				validationResult.addValidationResultItem(new ValidationResultItem(moneyflow.getId(),
						ErrorCode.SPLIT_ENTRIES_AMOUNT_IS_NOT_EQUALS_MONEYFLOW_AMOUNT));
			}
		}
		return validationResult;
	}

	private void fillShowAddImportedMoneyflowsResponse(final UserID userId,
			final ShowAddImportedMoneyflowsResponse response) {
		final LocalDate today = LocalDate.now();
		final List<Capitalsource> capitalsources = this.capitalsourceService
				.getGroupBookableCapitalsourcesByDateRange(userId, today, today);
		if (!capitalsources.isEmpty()) {
			final List<CapitalsourceID> capitalsourceIds = capitalsources.stream().map(Capitalsource::getId).toList();
			final List<ImportedMoneyflow> importedMoneyflows = this.importedMoneyflowService
					.getAllImportedMoneyflowsByCapitalsourceIds(userId, capitalsourceIds,
							ImportedMoneyflowStatus.CREATED);
			if (!importedMoneyflows.isEmpty()) {

				final List<BankAccount> contractpartnerBankAccounts = importedMoneyflows.stream()
						.map(ImportedMoneyflow::getBankAccount).toList();

				final List<ContractpartnerAccount> contractpartnerAccounts = this.contractpartnerAccountService
						.getAllContractpartnerByAccounts(userId, contractpartnerBankAccounts);

				if (!contractpartnerAccounts.isEmpty()) {
					final Map<BankAccount, Contractpartner> bankAccountToContractpartner = new HashMap<>();

					for (final ContractpartnerAccount contractpartnerAccount : contractpartnerAccounts) {
						bankAccountToContractpartner.put(contractpartnerAccount.getBankAccount(),
								contractpartnerAccount.getContractpartner());
					}

					// match IBAN/BIC from the imported moneyflows to contractpartners via the
					// contractpartneraccounts
					for (final ImportedMoneyflow importedMoneyflow : importedMoneyflows) {
						final Contractpartner contractpartner = bankAccountToContractpartner
								.get(importedMoneyflow.getBankAccount());
						importedMoneyflow.setContractpartner(contractpartner);
					}
				}

				response.setImportedMoneyflowTransports(
						this.importedMoneyflowTransportMapper.mapAToB(importedMoneyflows));
			}
		}
	}

	@Override
	public ResponseEntity<ShowAddImportedMoneyflowsResponse> showAddImportedMoneyflows() {
		final UserID userId = super.getUserId();
		final ShowAddImportedMoneyflowsResponse response = new ShowAddImportedMoneyflowsResponse();
		this.fillShowAddImportedMoneyflowsResponse(userId, response);
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<Void> createImportedMoneyflow(@RequestBody final CreateImportedMoneyflowRequest request) {
		final ImportedMoneyflowTransport importedMoneyflowTransport = request.getImportedMoneyflowTransport();
		final ImportedMoneyflow importedMoneyflow = this.importedMoneyflowTransportMapper
				.mapBToA(importedMoneyflowTransport);
		final BankAccount bankAccount = new BankAccount(importedMoneyflowTransport.getAccountNumberCapitalsource(),
				importedMoneyflowTransport.getBankCodeCapitalsource());
		final LocalDateTime now = LocalDateTime.now();
		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceByAccount(null, bankAccount,
				now.toLocalDate());
		if (capitalsource != null) {
			if (capitalsource.getImportAllowed() != CapitalsourceImport.ALL_ALLOWED) {
				throw new BusinessException("Import of this capitalsource is not allowed!",
						ErrorCode.CAPITALSOURCE_IMPORT_NOT_ALLOWED);
			}
			importedMoneyflow.setCapitalsource(capitalsource);
			final ValidationResult validationResult = this.importedMoneyflowService
					.validateImportedMoneyflow(importedMoneyflow);

			this.throwValidationExceptionIfInvalid(validationResult);

			this.importedMoneyflowService.createImportedMoneyflow(importedMoneyflow);

			return ResponseEntity.noContent().build();
		} else {
			throw new BusinessException("No matching capitalsource found!", ErrorCode.CAPITALSOURCE_NOT_FOUND);
		}
	}

	@Override
	public ResponseEntity<Void> deleteImportedMoneyflowById(final Long id) {
		final UserID userId = super.getUserId();
		final ImportedMoneyflowID importedMoneyflowId = new ImportedMoneyflowID(id);

		this.importedMoneyflowService.updateImportedMoneyflowStatus(userId, importedMoneyflowId,
				ImportedMoneyflowStatus.IGNORED);

		return ResponseEntity.noContent().build();

	}

	/**
	 * Checks if capitalsource and contractparter are valid on bookingdate -
	 * otherwise the validity is modified. Also fills comment and postingaccount if
	 * it is empty and MoneyflowSplitEntries where provided with data from the first
	 * MoneyflowSplitEntry.
	 *
	 * @param moneyflow             Moneyflow
	 * @param moneyflowSplitEntries MoneyflowSplitEntries
	 */
	private void prepareForValidityCheck(final Moneyflow moneyflow,
			final List<MoneyflowSplitEntry> moneyflowSplitEntries) {
		final LocalDate bookingDate = moneyflow.getBookingDate();
		final UserID userId = moneyflow.getUser().getId();
		final Group group = moneyflow.getGroup();
		if (bookingDate != null) {
			final AccessRelation accessRelation = this.accessRelationService
					.getCurrentAccessRelationById(moneyflow.getUser().getId());
			// Only modify Capitalsources or Contractpartner if the Bookingdate is within
			// the
			// current group assignment validity period
			if (!bookingDate.isBefore(accessRelation.getValidFrom())
					&& !bookingDate.isAfter(accessRelation.getValidTil())) {
				// Check if used Capitalsource is valid at bookingDate - if not, change its
				// validity so it fits.
				if (moneyflow.getCapitalsource() != null) {
					final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId,
							group.getId(), moneyflow.getCapitalsource().getId());
					if (capitalsource != null && capitalsource.groupUseAllowed(userId)) {
						final boolean bookingDateIsBeforeValidity = bookingDate.isBefore(capitalsource.getValidFrom());
						final boolean bookingDateIsAfterValidity = bookingDate.isAfter(capitalsource.getValidTil());
						if (bookingDateIsBeforeValidity) {
							capitalsource.setValidFrom(bookingDate);
						}
						if (bookingDateIsAfterValidity) {
							capitalsource.setValidTil(bookingDate);
						}
						if (bookingDateIsAfterValidity || bookingDateIsBeforeValidity) {
							this.capitalsourceService.updateCapitalsource(capitalsource);
						}
					}
				}
				// Check if used Contractpartner is valid at bookingDate - if not, change its
				// validity so it fits.
				if (moneyflow.getContractpartner() != null) {
					final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
							moneyflow.getContractpartner().getId());
					if (contractpartner != null) {
						final boolean bookingDateIsBeforeValidity = bookingDate
								.isBefore(contractpartner.getValidFrom());
						final boolean bookingDateIsAfterValidity = bookingDate.isAfter(contractpartner.getValidTil());
						if (bookingDateIsBeforeValidity) {
							contractpartner.setValidFrom(bookingDate);
						}
						if (bookingDateIsAfterValidity) {
							contractpartner.setValidTil(bookingDate);
						}
						if (bookingDateIsAfterValidity || bookingDateIsBeforeValidity) {
							this.contractpartnerService.updateContractpartner(contractpartner);
						}
					}
				}
			}
		}
		// use the comment and postingaccount of the 1st split booking for the main
		// booking if
		// nothing is specified
		if (!moneyflowSplitEntries.isEmpty()) {
			final MoneyflowSplitEntry moneyflowSplitEntry = moneyflowSplitEntries.getFirst();
			if (moneyflow.getComment() == null || moneyflow.getComment().trim().isEmpty()) {
				moneyflow.setComment(moneyflowSplitEntry.getComment());
			}
			if (moneyflow.getPostingAccount() == null) {
				moneyflow.setPostingAccount(moneyflowSplitEntry.getPostingAccount());
			}
		}
	}

	@Override
	public ResponseEntity<Void> importImportedMoneyflows(@RequestBody final ImportImportedMoneyflowRequest request) {
		final UserID userId = super.getUserId();
		final ImportedMoneyflow importedMoneyflow = this.importedMoneyflowTransportMapper
				.mapBToA(request.getImportedMoneyflowTransport());
		final List<MoneyflowSplitEntry> moneyflowSplitEntries = this.moneyflowSplitEntryTransportMapper
				.mapBToA(request.getInsertMoneyflowSplitEntryTransports());

		if (importedMoneyflow.getId() == null) {
			return ResponseEntity.noContent().build();
		}

		final ImportedMoneyflow existingImportedMoneyflow = this.importedMoneyflowService
				.getImportedMoneyflowById(userId, importedMoneyflow.getId());

		if (existingImportedMoneyflow == null
				|| !ImportedMoneyflowStatus.CREATED.equals(existingImportedMoneyflow.getStatus())) {
			return ResponseEntity.noContent().build();
		}

		final User user = this.userService.getUserById(userId);
		final Group group = this.accessRelationService.getCurrentGroup(userId);
		importedMoneyflow.setUser(user);
		importedMoneyflow.setGroup(group);
		final Moneyflow moneyflow = importedMoneyflow.getMoneyflow();
		this.prepareForValidityCheck(moneyflow, moneyflowSplitEntries);
		final ValidationResult validationResult = this.moneyflowService.validateMoneyflow(moneyflow);

		if (!moneyflowSplitEntries.isEmpty()) {
			moneyflowSplitEntries.stream().forEach(mse -> validationResult
					.mergeValidationResult(this.moneyflowSplitEntryService.validateMoneyflowSplitEntry(mse)));
			validationResult.mergeValidationResult(this.checkIfAmountIsEqual(moneyflow, moneyflowSplitEntries));
		}

		for (final ValidationResultItem item : validationResult.getValidationResultItems()) {
			item.setKey(importedMoneyflow.getId());
		}

		this.throwValidationExceptionIfInvalid(validationResult);

		final MoneyflowID moneyflowId = this.moneyflowService.createMoneyflow(moneyflow);
		if (!moneyflowSplitEntries.isEmpty()) {
			moneyflowSplitEntries.stream().forEach(mse -> mse.setMoneyflowId(moneyflowId));
			this.moneyflowSplitEntryService.createMoneyflowSplitEntries(userId, moneyflowSplitEntries);
		}
		/*
		 * Add the BankAccount information to the selected contractpartner so it can be
		 * preselected the next time something is imported with the same BankAccount.
		 * Additionally, create a counter booking if the BankAccount is also a
		 * capitalsource in our system which does not support importing moneyflows. For
		 * example if a moneyflow from Capitalsource 1 to Capitalsource 2 happend.
		 * Importing is only allowed for Capitalsource 2. Then the matching booking for
		 * Capitalsource 1 will be created here automatically.
		 */
		if (importedMoneyflow.getBankAccount() != null) {
			final ContractpartnerAccount contractpartnerAccount = new ContractpartnerAccount();
			contractpartnerAccount.setBankAccount(importedMoneyflow.getBankAccount());
			contractpartnerAccount.setContractpartner(importedMoneyflow.getContractpartner());
			final List<ContractpartnerAccount> contractpartnerAccounts = this.contractpartnerAccountService
					.getAllContractpartnerByAccounts(userId,
							Collections.singletonList(importedMoneyflow.getBankAccount()));
			if (contractpartnerAccounts == null || contractpartnerAccounts.isEmpty()) {
				this.contractpartnerAccountService.createContractpartnerAccount(userId, contractpartnerAccount);
			}
			// if the IBAN/BIC of the booking matches one of our own capitalsource which
			// must not be imported (because it has no HBCI access for example), create a
			// counterbooking for it automatically. Do not do it for a credit type
			// capitalsource at this just makes no sense
			final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceByAccount(userId,
					importedMoneyflow.getBankAccount(), importedMoneyflow.getBookingDate());
			if (capitalsource != null && capitalsource.getImportAllowed() != CapitalsourceImport.ALL_ALLOWED
					&& capitalsource.getType() != CapitalsourceType.CREDIT) {
				importedMoneyflow.setCapitalsource(capitalsource);
				importedMoneyflow.setAmount(importedMoneyflow.getAmount().negate());
				this.moneyflowService.createMoneyflow(importedMoneyflow.getMoneyflow());
			}
		}
		this.importedMoneyflowService.updateImportedMoneyflowStatus(userId, importedMoneyflow.getId(),
				ImportedMoneyflowStatus.PROCESSED);

		return ResponseEntity.noContent().build();
	}
}
