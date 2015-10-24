//Copyright (c) 2015 Oliver Lehmann <oliver@laladev.org>
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.laladev.moneyjinn.businesslogic.model.BankAccount;
import org.laladev.moneyjinn.businesslogic.model.Contractpartner;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerAccount;
import org.laladev.moneyjinn.businesslogic.model.PostingAccount;
import org.laladev.moneyjinn.businesslogic.model.access.Group;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.businesslogic.model.exception.BusinessException;
import org.laladev.moneyjinn.businesslogic.model.moneyflow.ImportedMoneyflow;
import org.laladev.moneyjinn.businesslogic.model.moneyflow.ImportedMoneyflowID;
import org.laladev.moneyjinn.businesslogic.model.moneyflow.Moneyflow;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResult;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.businesslogic.service.api.IAccessRelationService;
import org.laladev.moneyjinn.businesslogic.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.businesslogic.service.api.IContractpartnerAccountService;
import org.laladev.moneyjinn.businesslogic.service.api.IContractpartnerService;
import org.laladev.moneyjinn.businesslogic.service.api.IImportedMoneyflowService;
import org.laladev.moneyjinn.businesslogic.service.api.IMoneyflowService;
import org.laladev.moneyjinn.businesslogic.service.api.IPostingAccountService;
import org.laladev.moneyjinn.businesslogic.service.api.IUserService;
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
import org.laladev.moneyjinn.server.annotation.RequiresAuthorization;
import org.laladev.moneyjinn.server.controller.mapper.CapitalsourceTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.ContractpartnerTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.ImportedMoneyflowTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.PostingAccountTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.ValidationItemTransportMapper;
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

	@Override
	protected void addBeanMapper() {
		this.registerBeanMapper(new CapitalsourceTransportMapper());
		this.registerBeanMapper(new ContractpartnerTransportMapper());
		this.registerBeanMapper(new PostingAccountTransportMapper());
		this.registerBeanMapper(new ImportedMoneyflowTransportMapper());
		this.registerBeanMapper(new ValidationItemTransportMapper());
	}

	private void fillShowAddImportedMoneyflowsResponse(final UserID userId,
			final ShowAddImportedMoneyflowsResponse response) {
		final LocalDate today = LocalDate.now();

		final List<Capitalsource> capitalsources = this.capitalsourceService.getGroupCapitalsourcesByDateRange(userId,
				today, today);
		if (capitalsources != null && !capitalsources.isEmpty()) {

			final List<CapitalsourceID> capitalsourceIds = capitalsources.stream().map(ms -> ms.getId())
					.collect(Collectors.toCollection(ArrayList::new));

			final List<ImportedMoneyflow> importedMoneyflows = this.importedMoneyflowService
					.getAllImportedMoneyflowsByCapitalsourceIds(userId, capitalsourceIds);

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
						.map(im -> im.getBankAccount()).collect(Collectors.toCollection(ArrayList::new));

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
			if (!capitalsource.isImportAllowed()) {
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
		this.importedMoneyflowService.deleteImportedMoneyflowById(userId, importedMoneyflowId);
	}

	@RequestMapping(value = "importImportedMoneyflows", method = { RequestMethod.POST })
	@RequiresAuthorization
	public ValidationResponse importImportedMoneyflows(@RequestBody final ImportImportedMoneyflowRequest request) {
		final UserID userId = super.getUserId();
		final List<ImportedMoneyflow> importedMoneyflows = super.mapList(request.getImportedMoneyflowTransports(),
				ImportedMoneyflow.class);

		final User user = this.userService.getUserById(userId);
		final Group group = this.accessRelationService.getAccessor(userId);

		final List<Moneyflow> moneyflows = new ArrayList<>();
		final ValidationResult validationResult = new ValidationResult();

		for (final ImportedMoneyflow importedMoneyflow : importedMoneyflows) {
			importedMoneyflow.setUser(user);
			importedMoneyflow.setGroup(group);
			final Moneyflow moneyflow = importedMoneyflow.getMoneyflow();
			moneyflows.add(moneyflow);
			final ValidationResult validationResultMoneyflow = this.moneyflowService.validateMoneyflow(moneyflow);
			if (!validationResultMoneyflow.isValid()) {
				for (final ValidationResultItem item : validationResultMoneyflow.getValidationResultItems()) {
					item.setKey(importedMoneyflow.getId());
				}
				validationResult.mergeValidationResult(validationResultMoneyflow);
			}
		}

		if (validationResult.isValid() == true) {

			this.moneyflowService.createMoneyflows(moneyflows);

			for (final ImportedMoneyflow impMoneyflow : importedMoneyflows) {
				if (impMoneyflow.getBankAccount() != null) {
					final ContractpartnerAccount contractpartnerAccount = new ContractpartnerAccount();
					contractpartnerAccount.setBankAccount(impMoneyflow.getBankAccount());
					contractpartnerAccount.setContractpartner(impMoneyflow.getContractpartner());
					final List<ContractpartnerAccount> contractpartnerAccounts = this.contractpartnerAccountService
							.getAllContractpartnerByAccounts(userId, Arrays.asList(impMoneyflow.getBankAccount()));
					if (contractpartnerAccounts == null || contractpartnerAccounts.isEmpty()) {
						this.contractpartnerAccountService.createContractpartnerAccount(userId, contractpartnerAccount);
					}

					// if the IBAN/BIC of the booking matches one of our own capitalsource which
					// must not be imported (because it has no HBCI access for example), create a
					// counterbooking for it automatically
					final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceByAccount(userId,
							impMoneyflow.getBankAccount(), impMoneyflow.getBookingDate());
					if (capitalsource != null && !capitalsource.isImportAllowed()) {
						impMoneyflow.setCapitalsource(capitalsource);
						impMoneyflow.setAmount(impMoneyflow.getAmount().negate());
						this.moneyflowService.createMoneyflows(Arrays.asList(impMoneyflow.getMoneyflow()));
					}
				}

				this.importedMoneyflowService.deleteImportedMoneyflowById(userId, impMoneyflow.getId());
			}
		} else {
			final ValidationResponse response = new ValidationResponse();
			response.setResult(false);
			response.setValidationItemTransports(
					super.mapList(validationResult.getValidationResultItems(), ValidationItemTransport.class));
			return response;
		}
		return null;
	}
}
