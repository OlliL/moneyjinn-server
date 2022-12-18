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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.inject.Inject;

import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import org.laladev.moneyjinn.core.rest.model.importedmoneyflow.CreateImportedMoneyflowRequest;
import org.laladev.moneyjinn.core.rest.model.importedmoneyflow.ImportImportedMoneyflowRequest;
import org.laladev.moneyjinn.core.rest.model.importedmoneyflow.ShowAddImportedMoneyflowsResponse;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ContractpartnerTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ImportedMoneyflowTransport;
import org.laladev.moneyjinn.core.rest.model.transport.PostingAccountTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.model.BankAccount;
import org.laladev.moneyjinn.model.Contractpartner;
import org.laladev.moneyjinn.model.ContractpartnerAccount;
import org.laladev.moneyjinn.model.PostingAccount;
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
import org.laladev.moneyjinn.server.annotation.RequiresAuthorization;
import org.laladev.moneyjinn.server.controller.mapper.CapitalsourceTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.ContractpartnerTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.ImportedMoneyflowTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.MoneyflowSplitEntryTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.PostingAccountTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.ValidationItemTransportMapper;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.IContractpartnerAccountService;
import org.laladev.moneyjinn.service.api.IContractpartnerService;
import org.laladev.moneyjinn.service.api.IImportedMoneyflowService;
import org.laladev.moneyjinn.service.api.IMoneyflowService;
import org.laladev.moneyjinn.service.api.IMoneyflowSplitEntryService;
import org.laladev.moneyjinn.service.api.IPostingAccountService;
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
@RequestMapping("/moneyflow/server/importedmoneyflow/")
public class ImportedMoneyflowController extends AbstractController {
	@Inject
	private IUserService userService;
	@Inject
	private IAccessRelationService accessRelationService;
	@Inject
	private ICapitalsourceService capitalsourceService;
	@Inject
	private IContractpartnerService contractpartnerService;
	@Inject
	private IContractpartnerAccountService contractpartnerAccountService;
	@Inject
	private IPostingAccountService postingAccountService;
	@Inject
	private IMoneyflowService moneyflowService;
	@Inject
	private IImportedMoneyflowService importedMoneyflowService;
	@Inject
	private IMoneyflowSplitEntryService moneyflowSplitEntryService;

	@Override
	protected void addBeanMapper() {
		this.registerBeanMapper(new CapitalsourceTransportMapper());
		this.registerBeanMapper(new ContractpartnerTransportMapper());
		this.registerBeanMapper(new PostingAccountTransportMapper());
		this.registerBeanMapper(new ImportedMoneyflowTransportMapper());
		this.registerBeanMapper(new ValidationItemTransportMapper());
		super.registerBeanMapper(new MoneyflowSplitEntryTransportMapper());
	}

	private ValidationResult checkIfAmountIsEqual(final Moneyflow moneyflow,
			final List<MoneyflowSplitEntry> moneyflowSplitEntries) {
		final ValidationResult validationResult = new ValidationResult();

		if (!moneyflowSplitEntries.isEmpty()) {
			final BigDecimal sumOfSplitEntriesAmount = moneyflowSplitEntries.stream()
					.map(MoneyflowSplitEntry::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

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
		if (capitalsources != null && !capitalsources.isEmpty()) {

			final List<CapitalsourceID> capitalsourceIds = capitalsources.stream().map(Capitalsource::getId)
					.collect(Collectors.toCollection(ArrayList::new));

			final List<ImportedMoneyflow> importedMoneyflows = this.importedMoneyflowService
					.getAllImportedMoneyflowsByCapitalsourceIds(userId, capitalsourceIds,
							ImportedMoneyflowStatus.CREATED);

			if (importedMoneyflows != null && !importedMoneyflows.isEmpty()) {
				response.setCapitalsourceTransports(super.mapList(capitalsources, CapitalsourceTransport.class));

				final List<Contractpartner> contractpartners = this.contractpartnerService
						.getAllContractpartnersByDateRange(userId, today, today);
				if (contractpartners != null && !contractpartners.isEmpty()) {
					response.setContractpartnerTransports(
							super.mapList(contractpartners, ContractpartnerTransport.class));
				}

				final List<PostingAccount> postingAccounts = this.postingAccountService.getAllPostingAccounts();
				if (postingAccounts != null && !postingAccounts.isEmpty()) {
					response.setPostingAccountTransports(super.mapList(postingAccounts, PostingAccountTransport.class));
				}

				final List<BankAccount> contractpartnerBankAccounts = importedMoneyflows.stream()
						.map(ImportedMoneyflow::getBankAccount).collect(Collectors.toCollection(ArrayList::new));

				final List<ContractpartnerAccount> contractpartnerAccounts = this.contractpartnerAccountService
						.getAllContractpartnerByAccounts(userId, contractpartnerBankAccounts);

				final Map<BankAccount, Contractpartner> bankAccountToContractpartner = new HashMap<>();
				if (contractpartnerAccounts != null && !contractpartnerAccounts.isEmpty()) {
					for (final ContractpartnerAccount contractpartnerAccount : contractpartnerAccounts) {
						bankAccountToContractpartner.put(contractpartnerAccount.getBankAccount(),
								contractpartnerAccount.getContractpartner());
					}
				}

				// match IBAN/BIC from the imported moneyflows to contractpartners via the
				// contractpartneraccounts
				for (final ImportedMoneyflow importedMoneyflow : importedMoneyflows) {
					final Contractpartner contractpartner = bankAccountToContractpartner
							.get(importedMoneyflow.getBankAccount());
					if (contractpartner != null) {
						importedMoneyflow.setContractpartner(contractpartner);
					}
				}

				response.setImportedMoneyflowTransports(
						super.mapList(importedMoneyflows, ImportedMoneyflowTransport.class));
			}
		}

	}

	@RequestMapping(value = "showAddImportedMoneyflows", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowAddImportedMoneyflowsResponse showAddImportedMoneyflows() {
		final UserID userId = super.getUserId();
		final ShowAddImportedMoneyflowsResponse response = new ShowAddImportedMoneyflowsResponse();

		this.fillShowAddImportedMoneyflowsResponse(userId, response);

		return response;
	}

	@RequestMapping(value = "createImportedMoneyflow", method = { RequestMethod.POST })
	public ValidationResponse createImportedMoneyflow(@RequestBody final CreateImportedMoneyflowRequest request) {
		final ImportedMoneyflowTransport importedMoneyflowTransport = request.getImportedMoneyflowTransport();

		final ImportedMoneyflow importedMoneyflow = super.map(importedMoneyflowTransport, ImportedMoneyflow.class);

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

			if (validationResult.isValid()) {
				this.importedMoneyflowService.createImportedMoneyflow(importedMoneyflow);
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

	@RequestMapping(value = "deleteImportedMoneyflowById/{id}", method = { RequestMethod.DELETE })
	@RequiresAuthorization
	public void deleteImportedMoneyflowById(@PathVariable(value = "id") final Long id) {
		final UserID userId = super.getUserId();
		final ImportedMoneyflowID importedMoneyflowId = new ImportedMoneyflowID(id);
		this.importedMoneyflowService.updateImportedMoneyflowStatus(userId, importedMoneyflowId,
				ImportedMoneyflowStatus.IGNORED);
	}

	/**
	 * Checks if capitalsource and contractparter are valid on bookingdate - otherwise the validity
	 * is modified. Also fills comment and postingaccount if it is empty and MoneyflowSplitEntries
	 * where provided with data from the first MoneyflowSplitEntry.
	 *
	 * @param moneyflow
	 *            Moneyflow
	 * @param moneyflowSplitEntries
	 *            MoneyflowSplitEntries
	 */
	private void prepareForValidityCheck(final Moneyflow moneyflow,
			final List<MoneyflowSplitEntry> moneyflowSplitEntries) {
		final LocalDate bookingDate = moneyflow.getBookingDate();

		final UserID userId = moneyflow.getUser().getId();
		final Group group = moneyflow.getGroup();

		if (bookingDate != null) {

			final AccessRelation accessRelation = this.accessRelationService
					.getAccessRelationById(moneyflow.getUser().getId(), LocalDate.now());
			// Only modify Capitalsources or Contractpartner if the Bookingdate is within the
			// current group assignment validity period
			if (!bookingDate.isBefore(accessRelation.getValidFrom())
					&& !bookingDate.isAfter(accessRelation.getValidTil())) {
				// Check if used Capitalsource is valid at bookingDate - if not, change its
				// validity so it fits.
				if (moneyflow.getCapitalsource() != null) {
					final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId,
							group.getId(), moneyflow.getCapitalsource().getId());

					if (capitalsource != null) {
						final boolean userMayUseCapitalsource = capitalsource.getUser().getId()
								.equals(moneyflow.getUser().getId()) || capitalsource.isGroupUse();

						if (userMayUseCapitalsource) {
							final boolean bookingDateIsBeforeValidity = bookingDate
									.isBefore(capitalsource.getValidFrom());
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

		// use the comment and postingaccount of the 1st split booking for the main booking if
		// nothing is specified
		if (!moneyflowSplitEntries.isEmpty()) {
			final MoneyflowSplitEntry moneyflowSplitEntry = moneyflowSplitEntries.iterator().next();

			if (moneyflow.getComment() == null || moneyflow.getComment().trim().isEmpty()) {
				moneyflow.setComment(moneyflowSplitEntry.getComment());
			}

			if (moneyflow.getPostingAccount() == null) {
				moneyflow.setPostingAccount(moneyflowSplitEntry.getPostingAccount());
			}
		}
	}

	@RequestMapping(value = "importImportedMoneyflows", method = { RequestMethod.POST })
	@RequiresAuthorization
	public ValidationResponse importImportedMoneyflows(@RequestBody final ImportImportedMoneyflowRequest request) {
		final UserID userId = super.getUserId();
		final ImportedMoneyflow importedMoneyflow = super.map(request.getImportedMoneyflowTransport(),
				ImportedMoneyflow.class);

		final List<MoneyflowSplitEntry> moneyflowSplitEntries = super.mapList(
				request.getInsertMoneyflowSplitEntryTransports(), MoneyflowSplitEntry.class);

		final User user = this.userService.getUserById(userId);
		final Group group = this.accessRelationService.getAccessor(userId);

		importedMoneyflow.setUser(user);
		importedMoneyflow.setGroup(group);

		final Moneyflow moneyflow = importedMoneyflow.getMoneyflow();
		this.prepareForValidityCheck(moneyflow, moneyflowSplitEntries);

		final ValidationResult validationResult = this.moneyflowService.validateMoneyflow(moneyflow);

		if (validationResult.isValid()) {
			if (!moneyflowSplitEntries.isEmpty()) {
				moneyflowSplitEntries.stream().forEach(mse -> {
					validationResult
							.mergeValidationResult(this.moneyflowSplitEntryService.validateMoneyflowSplitEntry(mse));
				});

				if (validationResult.isValid()) {
					validationResult.mergeValidationResult(this.checkIfAmountIsEqual(moneyflow, moneyflowSplitEntries));
				}
			}
		}

		if (validationResult.isValid()) {
			final MoneyflowID moneyflowId = this.moneyflowService.createMoneyflow(moneyflow);
			if (!moneyflowSplitEntries.isEmpty()) {
				moneyflowSplitEntries.stream().forEach(mse -> mse.setMoneyflowId(moneyflowId));
				this.moneyflowSplitEntryService.createMoneyflowSplitEntries(userId, moneyflowSplitEntries);
			}

			/*
			 * Add the BankAccount information to the selected contractpartner so it can be
			 * preselected the next time something is imported with the same BankAccount.
			 * Additionally, create a counter booking if the BankAccount is also a capitalsource in
			 * our system which does not support importing moneyflows. For example if a moneyflow
			 * from Capitalsource 1 to Capitalsource 2 happend. Importing is only allowed for
			 * Capitalsource 2. Then the matching booking for Capitalsource 1 will be created here
			 * automatically.
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
		} else {
			for (final ValidationResultItem item : validationResult.getValidationResultItems()) {
				item.setKey(importedMoneyflow.getId());
			}
			final ValidationResponse response = new ValidationResponse();
			response.setResult(false);
			response.setValidationItemTransports(
					super.mapList(validationResult.getValidationResultItems(), ValidationItemTransport.class));
			return response;
		}
		return null;
	}
}
