
package org.laladev.moneyjinn.server.controller.crud.contractpartneraccount;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.ContractpartnerAccount;
import org.laladev.moneyjinn.model.ContractpartnerAccountID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.server.builder.ContractpartnerAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.model.ContractpartnerAccountTransport;
import org.laladev.moneyjinn.server.model.ValidationItemTransport;
import org.laladev.moneyjinn.server.model.ValidationResponse;
import org.laladev.moneyjinn.service.api.IContractpartnerAccountService;
import org.springframework.test.context.jdbc.Sql;

import jakarta.inject.Inject;

class CreateContractpartnerAccountTest extends AbstractContractpartnerAccountTest {
	@Inject
	private IContractpartnerAccountService contractpartnerAccountService;

	@Override
	protected void loadMethod() {
		super.getMock().create(null, null);
	}

	private void testError(final ContractpartnerAccountTransport transport, final ErrorCode errorCode)
			throws Exception {
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
	void test_ToLongAccountnumber_Error() throws Exception {
		final ContractpartnerAccountTransport transport = new ContractpartnerAccountTransportBuilder()
				.forNewContractpartnerAccount().build();
		transport.setAccountNumber("12345678901234567890123456789012345");
		this.testError(transport, ErrorCode.ACCOUNT_NUMBER_TO_LONG);
	}

	@Test
	void test_ToLongBankcode_Error() throws Exception {
		final ContractpartnerAccountTransport transport = new ContractpartnerAccountTransportBuilder()
				.forNewContractpartnerAccount().build();
		transport.setBankCode("123456789012");
		this.testError(transport, ErrorCode.BANK_CODE_TO_LONG);
	}

	@Test
	void test_AccountnumberInvalidChar_Error() throws Exception {
		final ContractpartnerAccountTransport transport = new ContractpartnerAccountTransportBuilder()
				.forNewContractpartnerAccount().build();
		transport.setAccountNumber("+");
		this.testError(transport, ErrorCode.ACCOUNT_NUMBER_CONTAINS_ILLEGAL_CHARS);
	}

	@Test
	void test_BankcodeInvalidChar_Error() throws Exception {
		final ContractpartnerAccountTransport transport = new ContractpartnerAccountTransportBuilder()
				.forNewContractpartnerAccount().build();
		transport.setBankCode("+");
		this.testError(transport, ErrorCode.BANK_CODE_CONTAINS_ILLEGAL_CHARS);
	}

	@Test
	void test_emptyContractpartner_Error() throws Exception {
		final ContractpartnerAccountTransport transport = new ContractpartnerAccountTransportBuilder()
				.forNewContractpartnerAccount().withContractpartnerid(null).build();
		this.testError(transport, ErrorCode.CONTRACTPARTNER_IS_NOT_SET);
	}

	@Test
	void test_nonExistingContractpartner_Error() throws Exception {
		final ContractpartnerAccountTransport transport = new ContractpartnerAccountTransportBuilder()
				.forNewContractpartnerAccount().withContractpartnerid(ContractpartnerTransportBuilder.NON_EXISTING_ID)
				.build();
		this.testError(transport, ErrorCode.CONTRACTPARTNER_DOES_NOT_EXIST);
	}

	@Test
	void test_standardRequest_DefaultReturn() throws Exception {
		final ContractpartnerAccountTransport transport = new ContractpartnerAccountTransportBuilder()
				.forNewContractpartnerAccount().build();

		super.callUsecaseExpect204(transport);

		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(
				ContractpartnerAccountTransportBuilder.NEXT_ID);
		final ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
				.getContractpartnerAccountById(userId, contractpartnerAccountId);
		Assertions.assertEquals(ContractpartnerAccountTransportBuilder.NEXT_ID, contractpartnerAccount.getId().getId());
	}

	@Test
	void test_standardRequest_MinimalReturn() throws Exception {
		final ContractpartnerAccountTransport transport = new ContractpartnerAccountTransportBuilder()
				.forNewContractpartnerAccount().build();

		super.callUsecaseExpect204Minimal(transport);

		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(
				ContractpartnerAccountTransportBuilder.NEXT_ID);
		final ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
				.getContractpartnerAccountById(userId, contractpartnerAccountId);
		Assertions.assertEquals(ContractpartnerAccountTransportBuilder.NEXT_ID, contractpartnerAccount.getId().getId());
	}

	@Test
	void test_standardRequest_RepresentationReturn() throws Exception {
		final ContractpartnerAccountTransport transport = new ContractpartnerAccountTransportBuilder()
				.forNewContractpartnerAccount().build();

		final ContractpartnerAccountTransport actualTransport = super.callUsecaseExpect200Representation(transport,
				ContractpartnerAccountTransport.class);

		Assertions.assertEquals(ContractpartnerAccountTransportBuilder.NEXT_ID, actualTransport.getId());
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(
				ContractpartnerAccountTransportBuilder.NEXT_ID);
		final ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
				.getContractpartnerAccountById(userId, contractpartnerAccountId);
		Assertions.assertEquals(ContractpartnerAccountTransportBuilder.NEXT_ID, contractpartnerAccount.getId().getId());
	}

	@Test
	void test_Bic8Digits_fillesUpTo11Digits() throws Exception {
		final ContractpartnerAccountTransport transport = new ContractpartnerAccountTransportBuilder()
				.forNewContractpartnerAccount().build();
		transport.setBankCode("ABCDEFGH");

		super.callUsecaseExpect204(transport);

		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(
				ContractpartnerAccountTransportBuilder.NEXT_ID);
		final ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
				.getContractpartnerAccountById(userId, contractpartnerAccountId);
		Assertions.assertEquals(ContractpartnerAccountTransportBuilder.NEXT_ID, contractpartnerAccount.getId().getId());
		Assertions.assertEquals(transport.getBankCode() + "XXX", contractpartnerAccount.getBankAccount().getBankCode());
	}

	@Test
	void test_AuthorizationRequired_Error() throws Exception {
		super.setUsername(null);
		super.setPassword(null);

		super.callUsecaseExpect403(new ContractpartnerAccountTransport());
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	void test_emptyDatabase_noException() throws Exception {
		super.setUsername(UserTransportBuilder.ADMIN_NAME);
		super.setPassword(UserTransportBuilder.ADMIN_PASSWORD);
		final ContractpartnerAccountTransport transport = new ContractpartnerAccountTransportBuilder()
				.forNewContractpartnerAccount().build();

		this.testError(transport, ErrorCode.CONTRACTPARTNER_DOES_NOT_EXIST);
	}
}