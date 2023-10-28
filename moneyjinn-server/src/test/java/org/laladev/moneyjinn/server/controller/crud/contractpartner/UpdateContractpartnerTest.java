
package org.laladev.moneyjinn.server.controller.crud.contractpartner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.Contractpartner;
import org.laladev.moneyjinn.model.ContractpartnerID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.builder.PostingAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.model.ContractpartnerTransport;
import org.laladev.moneyjinn.server.model.ValidationItemTransport;
import org.laladev.moneyjinn.server.model.ValidationResponse;
import org.laladev.moneyjinn.service.api.IContractpartnerService;
import org.springframework.test.context.jdbc.Sql;

import jakarta.inject.Inject;

class UpdateContractpartnerTest extends AbstractContractpartnerTest {
	@Inject
	private IContractpartnerService contractpartnerService;

	@Override
	protected void loadMethod() {
		super.getMock().update(null, null);
	}

	private void testError(final ContractpartnerTransport transport, final ErrorCode errorCode) throws Exception {
		final List<ValidationItemTransport> validationItems = new ArrayList<>();
		validationItems.add(new ValidationItemTransportBuilder().withKey(transport.getId().toString())
				.withError(errorCode.getErrorCode()).build());
		final ValidationResponse expected = new ValidationResponse();
		expected.setValidationItemTransports(validationItems);
		expected.setResult(Boolean.FALSE);

		final ValidationResponse actual = super.callUsecaseExpect422(transport, ValidationResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_ContractpartnernameAlreadyExisting_Error() throws Exception {
		final ContractpartnerTransport transport = new ContractpartnerTransportBuilder().forContractpartner2().build();
		transport.setName(ContractpartnerTransportBuilder.CONTRACTPARTNER1_NAME);
		this.testError(transport, ErrorCode.NAME_ALREADY_EXISTS);
	}

	@Test
	void test_emptyContractpartnername_Error() throws Exception {
		final ContractpartnerTransport transport = new ContractpartnerTransportBuilder().forContractpartner2().build();
		transport.setName("");
		this.testError(transport, ErrorCode.NAME_MUST_NOT_BE_EMPTY);
	}

	@Test
	void test_invalidPostingAccount_Error() throws Exception {
		final ContractpartnerTransport transport = new ContractpartnerTransportBuilder().forNewContractpartner()
				.build();
		transport.setPostingAccountId(PostingAccountTransportBuilder.NON_EXISTING_ID);
		this.testError(transport, ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED);
	}

	@Test
	void test_ValidTilBeforeValidFrom_Error() throws Exception {
		final ContractpartnerTransport transport = new ContractpartnerTransportBuilder().forContractpartner2().build();
		transport.setValidTil(LocalDate.parse("2000-01-01"));
		transport.setValidFrom(LocalDate.parse("2010-01-01"));
		this.testError(transport, ErrorCode.VALIDFROM_AFTER_VALIDTIL);
	}

	@Test
	void test_ValidityPeriodOutOfUsage_Error() throws Exception {
		final ContractpartnerTransport transport = new ContractpartnerTransportBuilder().forContractpartner1().build();
		transport.setValidFrom(LocalDate.parse("2010-01-01"));
		this.testError(transport, ErrorCode.MONEYFLOWS_OUTSIDE_VALIDITY_PERIOD);
	}

	@Test
	void test_standardRequest_Successfull_DefaultReturn() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(
				ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);
		final ContractpartnerTransport transport = new ContractpartnerTransportBuilder().forContractpartner1().build();
		transport.setName("hugo");

		super.callUsecaseExpect204(transport);

		final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerId);
		Assertions.assertEquals(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID, contractpartner.getId().getId());
		Assertions.assertEquals("hugo", contractpartner.getName());
	}

	@Test
	void test_standardRequest_Successfull_MinimalReturn() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(
				ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);
		final ContractpartnerTransport transport = new ContractpartnerTransportBuilder().forContractpartner1().build();
		transport.setName("hugo");

		super.callUsecaseExpect204Minimal(transport);

		final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerId);
		Assertions.assertEquals(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID, contractpartner.getId().getId());
		Assertions.assertEquals("hugo", contractpartner.getName());
	}

	@Test
	void test_standardRequest_Successfull_RepresentationReturn() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(
				ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);
		final ContractpartnerTransport transport = new ContractpartnerTransportBuilder().forContractpartner1().build();
		transport.setName("hugo");

		final ContractpartnerTransport actualTransport = super.callUsecaseExpect200Representation(transport,
				ContractpartnerTransport.class);

		Assertions.assertEquals(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID, actualTransport.getId());

		final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerId);
		Assertions.assertEquals(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID, contractpartner.getId().getId());
		Assertions.assertEquals("hugo", contractpartner.getName());
	}

	@Test
	void test_ContractpartnerFromSameGroupButNotMe_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(
				ContractpartnerTransportBuilder.CONTRACTPARTNER3_ID);
		final ContractpartnerTransport transport = new ContractpartnerTransportBuilder().forContractpartner3().build();
		transport.setName("hugo");

		super.callUsecaseExpect204(transport);

		final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerId);
		Assertions.assertEquals(ContractpartnerTransportBuilder.CONTRACTPARTNER3_ID, contractpartner.getId().getId());
		Assertions.assertEquals("hugo", contractpartner.getName());
	}

	@Test
	void test_ContractpartnerFromDifferentGroup_notSuccessfull() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.ADMIN_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(
				ContractpartnerTransportBuilder.CONTRACTPARTNER5_ID);
		final ContractpartnerTransport transport = new ContractpartnerTransportBuilder().forContractpartner3().build();
		transport.setName("hugo");

		super.callUsecaseExpect204(transport);

		final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerId);
		Assertions.assertEquals(ContractpartnerTransportBuilder.CONTRACTPARTNER5_ID, contractpartner.getId().getId());
		Assertions.assertEquals(ContractpartnerTransportBuilder.CONTRACTPARTNER5_NAME, contractpartner.getName());
	}

	@Test
	void test_ImportRoleNotAllowed_ErrorResponse() throws Exception {
		super.setUsername(UserTransportBuilder.IMPORTUSER_NAME);
		super.setPassword(UserTransportBuilder.IMPORTUSER_PASSWORD);

		super.callUsecaseExpect403(new ContractpartnerTransport());
	}

	@Test
	void test_AuthorizationRequired_Error() throws Exception {
		super.setUsername(null);
		super.setPassword(null);

		super.callUsecaseExpect403(new ContractpartnerTransport());
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	void test_emptyDatabase_noException() throws Exception {
		super.setUsername(UserTransportBuilder.ADMIN_NAME);
		super.setPassword(UserTransportBuilder.ADMIN_PASSWORD);
		final ContractpartnerTransport transport = new ContractpartnerTransportBuilder().forContractpartner1().build();
		transport.setPostingAccountId(null); // otherwise not existing id referenced

		super.callUsecaseExpect204(transport);
	}
}