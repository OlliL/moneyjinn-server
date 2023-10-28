
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

import jakarta.inject.Inject;

class CreateContractpartnerTest extends AbstractContractpartnerTest {
	@Inject
	private IContractpartnerService contractpartnerService;

	@Override
	protected void loadMethod() {
		super.getMock().create(null, null);
	}

	private void testError(final ContractpartnerTransport transport, final ErrorCode errorCode) throws Exception {
		final List<ValidationItemTransport> validationItems = new ArrayList<>();
		validationItems
				.add(new ValidationItemTransportBuilder().withKey(null).withError(errorCode.getErrorCode()).build());
		final ValidationResponse expected = new ValidationResponse();
		expected.setValidationItemTransports(validationItems);
		expected.setResult(Boolean.FALSE);

		final ValidationResponse actual = super.callUsecaseExpect422(transport, ValidationResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_ContractpartnernameAlreadyExisting_Error() throws Exception {
		final ContractpartnerTransport transport = new ContractpartnerTransportBuilder().forNewContractpartner()
				.build();
		transport.setName(ContractpartnerTransportBuilder.CONTRACTPARTNER1_NAME);
		this.testError(transport, ErrorCode.NAME_ALREADY_EXISTS);
	}

	@Test
	void test_emptyContractpartnername_Error() throws Exception {
		final ContractpartnerTransport transport = new ContractpartnerTransportBuilder().forNewContractpartner()
				.build();
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
	void test_nullContractpartnername_Error() throws Exception {
		final ContractpartnerTransport transport = new ContractpartnerTransportBuilder().forNewContractpartner()
				.build();
		transport.setName(null);
		this.testError(transport, ErrorCode.NAME_MUST_NOT_BE_EMPTY);
	}

	@Test
	void test_standardRequest_Successfull_MinimalReturn() throws Exception {
		final ContractpartnerTransport transport = new ContractpartnerTransportBuilder().forNewContractpartner()
				.build();

		super.callUsecaseExpect204Minimal(transport);

		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(ContractpartnerTransportBuilder.NEXT_ID);
		final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerId);
		Assertions.assertEquals(ContractpartnerTransportBuilder.NEXT_ID, contractpartner.getId().getId());
		Assertions.assertEquals(ContractpartnerTransportBuilder.NEWCONTRACTPARTNER_NAME, contractpartner.getName());
	}

	@Test
	void test_standardRequest_Successfull_RepresentationReturn() throws Exception {
		final ContractpartnerTransport transport = new ContractpartnerTransportBuilder().forNewContractpartner()
				.build();

		final ContractpartnerTransport actualTransport = super.callUsecaseExpect200Representation(transport,
				ContractpartnerTransport.class);

		Assertions.assertEquals(ContractpartnerTransportBuilder.NEXT_ID, actualTransport.getId());

		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(ContractpartnerTransportBuilder.NEXT_ID);
		final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerId);
		Assertions.assertEquals(ContractpartnerTransportBuilder.NEXT_ID, contractpartner.getId().getId());
		Assertions.assertEquals(ContractpartnerTransportBuilder.NEWCONTRACTPARTNER_NAME, contractpartner.getName());
	}

	@Test
	void test_standardRequest_Successfull_DefaultReturn() throws Exception {
		final ContractpartnerTransport transport = new ContractpartnerTransportBuilder().forNewContractpartner()
				.build();

		super.callUsecaseExpect204(transport);

		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(ContractpartnerTransportBuilder.NEXT_ID);
		final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerId);
		Assertions.assertEquals(ContractpartnerTransportBuilder.NEXT_ID, contractpartner.getId().getId());
		Assertions.assertEquals(ContractpartnerTransportBuilder.NEWCONTRACTPARTNER_NAME, contractpartner.getName());
	}

	@Test
	void test_differentUserIdSet_ButIgnoredAndAlwaysCreatedWithOwnUserId() throws Exception {
		final ContractpartnerTransport transport = new ContractpartnerTransportBuilder().forNewContractpartner()
				.build();
		transport.setUserid(UserTransportBuilder.ADMIN_ID);

		super.callUsecaseExpect204(transport);

		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(ContractpartnerTransportBuilder.NEXT_ID);
		final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerId);
		Assertions.assertEquals(ContractpartnerTransportBuilder.NEXT_ID, contractpartner.getId().getId());
		Assertions.assertEquals(ContractpartnerTransportBuilder.NEWCONTRACTPARTNER_NAME, contractpartner.getName());
	}

	@Test
	void test_checkDefaults_SuccessfullNoContent() throws Exception {
		final ContractpartnerTransport transport = new ContractpartnerTransportBuilder().forNewContractpartner()
				.build();
		transport.setValidFrom(null);
		transport.setValidTil(null);

		super.callUsecaseExpect204(transport);

		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(ContractpartnerTransportBuilder.NEXT_ID);
		final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerId);
		Assertions.assertEquals(ContractpartnerTransportBuilder.NEXT_ID, contractpartner.getId().getId());
		Assertions.assertEquals(ContractpartnerTransportBuilder.NEWCONTRACTPARTNER_NAME, contractpartner.getName());
		Assertions.assertEquals(LocalDate.now(), contractpartner.getValidFrom());
		Assertions.assertEquals(LocalDate.parse("2999-12-31"), contractpartner.getValidTil());
	}

	@Test
	void test_optionalFields_SuccessfullNoContent() throws Exception {
		final ContractpartnerTransport transport = new ContractpartnerTransportBuilder().forNewContractpartner()
				.build();
		transport.setStreet(null);
		transport.setPostcode(null);
		transport.setTown(null);
		transport.setCountry(null);
		transport.setMoneyflowComment(null);
		transport.setPostingAccountId(null);

		super.callUsecaseExpect204(transport);

		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(ContractpartnerTransportBuilder.NEXT_ID);
		final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerId);
		Assertions.assertEquals(ContractpartnerTransportBuilder.NEXT_ID, contractpartner.getId().getId());
		Assertions.assertEquals(ContractpartnerTransportBuilder.NEWCONTRACTPARTNER_NAME, contractpartner.getName());
		Assertions.assertNull(contractpartner.getStreet());
		Assertions.assertNull(contractpartner.getPostcode());
		Assertions.assertNull(contractpartner.getTown());
		Assertions.assertNull(contractpartner.getCountry());
		Assertions.assertNull(contractpartner.getMoneyflowComment());
		Assertions.assertNull(contractpartner.getPostingAccount());
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403(new ContractpartnerTransport());
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {

		final ContractpartnerTransport transport = new ContractpartnerTransportBuilder().forNewContractpartner()
				.build();
		transport.setStreet(null);
		transport.setPostcode(null);
		transport.setTown(null);
		transport.setCountry(null);
		transport.setMoneyflowComment(null);
		transport.setPostingAccountId(null);

		super.callUsecaseExpect204(transport);

		final UserID userId = new UserID(UserTransportBuilder.ADMIN_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(1l);
		final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerId);
		Assertions.assertEquals(contractpartnerId.getId(), contractpartner.getId().getId());
		Assertions.assertEquals(ContractpartnerTransportBuilder.NEWCONTRACTPARTNER_NAME, contractpartner.getName());
	}
}