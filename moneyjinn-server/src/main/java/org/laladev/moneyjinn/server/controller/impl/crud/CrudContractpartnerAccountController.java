package org.laladev.moneyjinn.server.controller.impl.crud;

import java.util.List;

import org.laladev.moneyjinn.model.ContractpartnerAccount;
import org.laladev.moneyjinn.model.ContractpartnerAccountID;
import org.laladev.moneyjinn.model.ContractpartnerID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.server.controller.api.CrudContractpartnerAccountControllerApi;
import org.laladev.moneyjinn.server.controller.impl.AbstractController;
import org.laladev.moneyjinn.server.controller.mapper.ContractpartnerAccountTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.ValidationItemTransportMapper;
import org.laladev.moneyjinn.server.model.ContractpartnerAccountTransport;
import org.laladev.moneyjinn.service.api.IContractpartnerAccountService;
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
public class CrudContractpartnerAccountController extends AbstractController
		implements CrudContractpartnerAccountControllerApi {
	private final IContractpartnerAccountService contractpartnerAccountService;
	private final ContractpartnerAccountTransportMapper contractpartnerAccountTransportMapper;
	private final ValidationItemTransportMapper validationItemTransportMapper;

	@Override
	@PostConstruct
	protected void addBeanMapper() {
		this.registerBeanMapper(this.contractpartnerAccountTransportMapper);
		this.registerBeanMapper(this.validationItemTransportMapper);
	}

	@Override
	public ResponseEntity<List<ContractpartnerAccountTransport>> readAll(@PathVariable("id") final Long id) {
		final UserID userId = super.getUserId();
		final ContractpartnerID contractpartnerId = new ContractpartnerID(id);
		final List<ContractpartnerAccount> contractpartnerAccounts = this.contractpartnerAccountService
				.getContractpartnerAccounts(userId, contractpartnerId);

		return ResponseEntity.ok(super.mapList(contractpartnerAccounts, ContractpartnerAccountTransport.class));
	}

	@Override
	public ResponseEntity<ContractpartnerAccountTransport> create(
			@RequestBody final ContractpartnerAccountTransport contractpartnerAccountTransport,
			@RequestHeader(value = HEADER_PREFER, required = false) final List<String> prefer) {
		final UserID userId = super.getUserId();
		final ContractpartnerAccount contractpartnerAccount = super.map(contractpartnerAccountTransport,
				ContractpartnerAccount.class);
		contractpartnerAccount.setId(null);
		final ValidationResult validationResult = this.contractpartnerAccountService
				.validateContractpartnerAccount(userId, contractpartnerAccount);

		this.throwValidationExceptionIfInvalid(validationResult);

		final ContractpartnerAccountID contractpartnerAccountId = this.contractpartnerAccountService
				.createContractpartnerAccount(userId, contractpartnerAccount);

		contractpartnerAccount.setId(contractpartnerAccountId);

		return this.preferedReturn(prefer, contractpartnerAccount, ContractpartnerAccountTransport.class);

	}

	@Override
	public ResponseEntity<ContractpartnerAccountTransport> update(
			@RequestBody final ContractpartnerAccountTransport contractpartnerAccountTransport,
			@RequestHeader(value = HEADER_PREFER, required = false) final List<String> prefer) {
		final UserID userId = super.getUserId();
		final ContractpartnerAccount contractpartnerAccount = super.map(contractpartnerAccountTransport,
				ContractpartnerAccount.class);
		final ValidationResult validationResult = this.contractpartnerAccountService
				.validateContractpartnerAccount(userId, contractpartnerAccount);

		this.throwValidationExceptionIfInvalid(validationResult);

		this.contractpartnerAccountService.updateContractpartnerAccount(userId, contractpartnerAccount);

		return this.preferedReturn(prefer, contractpartnerAccount, ContractpartnerAccountTransport.class);
	}

	@Override
	public ResponseEntity<Void> delete(@PathVariable("id") final Long id) {
		final UserID userId = super.getUserId();
		final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(id);

		this.contractpartnerAccountService.deleteContractpartnerAccountById(userId, contractpartnerAccountId);

		return ResponseEntity.noContent().build();
	}
}
