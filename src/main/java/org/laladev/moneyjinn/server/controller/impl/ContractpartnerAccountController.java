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

import java.util.List;

import javax.inject.Inject;

import org.laladev.moneyjinn.businesslogic.model.Contractpartner;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerAccount;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerAccountID;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerID;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResult;
import org.laladev.moneyjinn.businesslogic.service.api.IContractpartnerAccountService;
import org.laladev.moneyjinn.businesslogic.service.api.IContractpartnerService;
import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import org.laladev.moneyjinn.core.rest.model.contractpartneraccount.AbstractContractpartnerAccountResponse;
import org.laladev.moneyjinn.core.rest.model.contractpartneraccount.CreateContractpartnerAccountRequest;
import org.laladev.moneyjinn.core.rest.model.contractpartneraccount.ShowContractpartnerAccountListResponse;
import org.laladev.moneyjinn.core.rest.model.contractpartneraccount.ShowDeleteContractpartnerAccountResponse;
import org.laladev.moneyjinn.core.rest.model.contractpartneraccount.ShowEditContractpartnerAccountResponse;
import org.laladev.moneyjinn.core.rest.model.contractpartneraccount.UpdateContractpartnerAccountRequest;
import org.laladev.moneyjinn.core.rest.model.transport.ContractpartnerAccountTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.server.annotation.RequiresAuthorization;
import org.laladev.moneyjinn.server.controller.mapper.ContractpartnerAccountTransportMapper;
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
@RequestMapping("/moneyflow/server/contractpartneraccount/")
public class ContractpartnerAccountController extends AbstractController {
	@Inject
	IContractpartnerService contractpartnerService;
	@Inject
	IContractpartnerAccountService contractpartnerAccountService;

	@Override
	protected void addBeanMapper() {
		super.registerBeanMapper(new ContractpartnerAccountTransportMapper());
		super.registerBeanMapper(new ValidationItemTransportMapper());
	}

	@RequestMapping(value = "showContractpartnerAccountList/{id}", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowContractpartnerAccountListResponse showContractpartnerAccountList(
			@PathVariable(value = "id") final Long id) {
		final UserID userId = super.getUserId();
		final ContractpartnerID contractpartnerId = new ContractpartnerID(id);

		final List<ContractpartnerAccount> contractpartnerAccounts = this.contractpartnerAccountService
				.getContractpartnerAccounts(userId, contractpartnerId);

		final ShowContractpartnerAccountListResponse response = new ShowContractpartnerAccountListResponse();

		String contractpartnerName = null;
		if (contractpartnerAccounts != null && !contractpartnerAccounts.isEmpty()) {
			final List<ContractpartnerAccountTransport> contractpartnerAccountTransports = super.mapList(
					contractpartnerAccounts, ContractpartnerAccountTransport.class);
			response.setContractpartnerAccountTransports(contractpartnerAccountTransports);

			contractpartnerName = contractpartnerAccounts.iterator().next().getContractpartner().getName();
		} else {
			final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
					contractpartnerId);
			if (contractpartner != null) {
				contractpartnerName = contractpartner.getName();
			}
		}

		response.setContractpartnerName(contractpartnerName);

		return response;

	}

	@RequestMapping(value = "createContractpartnerAccount", method = { RequestMethod.POST })
	@RequiresAuthorization
	public ValidationResponse createContractpartnerAccount(
			@RequestBody final CreateContractpartnerAccountRequest request) {
		final UserID userId = super.getUserId();

		final ContractpartnerAccount contractpartnerAccount = super.map(request.getContractpartnerAccountTransport(),
				ContractpartnerAccount.class);
		contractpartnerAccount.setId(null);

		final ValidationResult validationResult = this.contractpartnerAccountService
				.validateContractpartnerAccount(userId, contractpartnerAccount);

		if (!validationResult.isValid()) {
			final ValidationResponse response = new ValidationResponse();
			response.setResult(validationResult.isValid());
			response.setValidationItemTransports(
					super.mapList(validationResult.getValidationResultItems(), ValidationItemTransport.class));
			return response;
		}
		this.contractpartnerAccountService.createContractpartnerAccount(userId, contractpartnerAccount);
		return null;
	}

	@RequestMapping(value = "updateContractpartnerAccount", method = { RequestMethod.PUT })
	@RequiresAuthorization
	public ValidationResponse updateContractpartnerAccount(
			@RequestBody final UpdateContractpartnerAccountRequest request) {
		final UserID userId = super.getUserId();

		final ContractpartnerAccount contractpartnerAccount = super.map(request.getContractpartnerAccountTransport(),
				ContractpartnerAccount.class);

		final ValidationResult validationResult = this.contractpartnerAccountService
				.validateContractpartnerAccount(userId, contractpartnerAccount);

		if (!validationResult.isValid()) {
			final ValidationResponse response = new ValidationResponse();
			response.setResult(validationResult.isValid());
			response.setValidationItemTransports(
					super.mapList(validationResult.getValidationResultItems(), ValidationItemTransport.class));
			return response;
		}
		this.contractpartnerAccountService.updateContractpartnerAccount(userId, contractpartnerAccount);
		return null;
	}

	@RequestMapping(value = "deleteContractpartnerAccount/{id}", method = { RequestMethod.DELETE })
	@RequiresAuthorization
	public void deleteContractpartnerAccount(@PathVariable(value = "id") final Long id) {
		final UserID userId = super.getUserId();
		final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(id);
		this.contractpartnerAccountService.deleteContractpartnerAccountById(userId, contractpartnerAccountId);
	}

	@RequestMapping(value = "showEditContractpartnerAccount/{id}", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowEditContractpartnerAccountResponse showEditContractpartnerAccount(
			@PathVariable(value = "id") final Long id) {
		final UserID userId = super.getUserId();
		final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(id);
		final ShowEditContractpartnerAccountResponse response = new ShowEditContractpartnerAccountResponse();
		this.fillAbstractContractpartnerAccountResponse(response, userId, contractpartnerAccountId);
		return response;
	}

	@RequestMapping(value = "showDeleteContractpartnerAccount/{id}", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowDeleteContractpartnerAccountResponse showDeleteContractpartnerAccount(
			@PathVariable(value = "id") final Long id) {
		final UserID userId = super.getUserId();
		final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(id);
		final ShowDeleteContractpartnerAccountResponse response = new ShowDeleteContractpartnerAccountResponse();
		this.fillAbstractContractpartnerAccountResponse(response, userId, contractpartnerAccountId);
		return response;
	}

	private void fillAbstractContractpartnerAccountResponse(final AbstractContractpartnerAccountResponse response,
			final UserID userId, final ContractpartnerAccountID contractpartnerAccountId) {
		final ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
				.getContractpartnerAccountById(userId, contractpartnerAccountId);
		final ContractpartnerAccountTransport contractpartnerAccountTransport = super.map(contractpartnerAccount,
				ContractpartnerAccountTransport.class);
		response.setContractpartnerAccountTransport(contractpartnerAccountTransport);
	}

}
