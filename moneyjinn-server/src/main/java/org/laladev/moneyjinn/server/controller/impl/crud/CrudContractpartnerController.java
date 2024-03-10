//Copyright (c) 2023-2024 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.server.controller.impl.crud;

import java.util.List;

import org.laladev.moneyjinn.model.Contractpartner;
import org.laladev.moneyjinn.model.ContractpartnerID;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.server.controller.api.CrudContractpartnerControllerApi;
import org.laladev.moneyjinn.server.controller.impl.AbstractController;
import org.laladev.moneyjinn.server.controller.mapper.ContractpartnerTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.ValidationItemTransportMapper;
import org.laladev.moneyjinn.server.model.ContractpartnerTransport;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.IContractpartnerAccountService;
import org.laladev.moneyjinn.service.api.IContractpartnerService;
import org.laladev.moneyjinn.service.api.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class CrudContractpartnerController extends AbstractController implements CrudContractpartnerControllerApi {
	private final IAccessRelationService accessRelationService;
	private final IContractpartnerAccountService contractpartnerAccountService;
	private final IContractpartnerService contractpartnerService;
	private final IUserService userService;
	private final ContractpartnerTransportMapper contractpartnerTransportMapper;
	private final ValidationItemTransportMapper validationItemTransportMapper;

	@Override
	@PostConstruct
	protected void addBeanMapper() {
		this.registerBeanMapper(this.contractpartnerTransportMapper);
		this.registerBeanMapper(this.validationItemTransportMapper);
	}

	@Override
	public ResponseEntity<List<ContractpartnerTransport>> readAll() {
		final UserID userId = super.getUserId();
		final List<Contractpartner> contractpartners = this.contractpartnerService.getAllContractpartners(userId);

		return ResponseEntity.ok(super.mapList(contractpartners, ContractpartnerTransport.class));
	}

	@Override
	public ResponseEntity<ContractpartnerTransport> readOne(@PathVariable("id") final Long id) {
		final UserID userId = super.getUserId();
		final ContractpartnerID contractpartnerId = new ContractpartnerID(id);
		final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerId);

		if (contractpartner == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(super.map(contractpartner, ContractpartnerTransport.class));
	}

	@Override
	public ResponseEntity<ContractpartnerTransport> create(
			@RequestBody final ContractpartnerTransport contractpartnerTransport,
			@RequestHeader(value = HEADER_PREFER, required = false) final List<String> prefer) {
		final UserID userId = super.getUserId();
		final Contractpartner contractpartner = super.map(contractpartnerTransport, Contractpartner.class);
		final User user = this.userService.getUserById(userId);
		final Group group = this.accessRelationService.getCurrentGroup(userId);
		contractpartner.setId(null);
		contractpartner.setUser(user);
		contractpartner.setGroup(group);
		final ValidationResult validationResult = this.contractpartnerService.validateContractpartner(contractpartner);

		this.throwValidationExceptionIfInvalid(validationResult);

		final ContractpartnerID contractpartnerId = this.contractpartnerService.createContractpartner(contractpartner);

		contractpartner.setId(contractpartnerId);

		return this.preferedReturn(prefer, contractpartner, ContractpartnerTransport.class);

	}

	@Override
	public ResponseEntity<ContractpartnerTransport> update(
			@RequestBody final ContractpartnerTransport contractpartnerTransport,
			@RequestHeader(value = HEADER_PREFER, required = false) final List<String> prefer) {
		final UserID userId = super.getUserId();
		final Contractpartner contractpartner = super.map(contractpartnerTransport, Contractpartner.class);
		final User user = this.userService.getUserById(userId);
		final Group group = this.accessRelationService.getCurrentGroup(userId);
		contractpartner.setUser(user);
		contractpartner.setGroup(group);
		final ValidationResult validationResult = this.contractpartnerService.validateContractpartner(contractpartner);

		this.throwValidationExceptionIfInvalid(validationResult);

		this.contractpartnerService.updateContractpartner(contractpartner);

		return this.preferedReturn(prefer, contractpartner, ContractpartnerTransport.class);
	}

	@Override
	public ResponseEntity<Void> delete(@PathVariable("id") final Long id) {
		final UserID userId = super.getUserId();
		final Group group = this.accessRelationService.getCurrentGroup(userId);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(id);

		this.contractpartnerAccountService.deleteContractpartnerAccounts(userId, contractpartnerId);
		this.contractpartnerService.deleteContractpartner(userId, group.getId(), contractpartnerId);

		return ResponseEntity.noContent().build();
	}
}
