
package org.laladev.moneyjinn.server.controller.crud.contractpartneraccount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.ContractpartnerAccount;
import org.laladev.moneyjinn.model.ContractpartnerAccountID;
import org.laladev.moneyjinn.model.ContractpartnerID;
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

class UpdateContractpartnerAccountTest extends AbstractContractpartnerAccountTest {
	@Inject
	private IContractpartnerAccountService contractpartnerAccountService;

	@Override
	protected void loadMethod() {
		super.getMock().update(null, null);
	}

	private void testError(final ContractpartnerAccountTransport transport, final String contractpartnerName,
			final ErrorCode... errorCodes) throws Exception {
		final List<ValidationItemTransport> validationItems = new ArrayList<>();
		for (final ErrorCode errorCode : errorCodes) {
			final ValidationItemTransportBuilder builder = new ValidationItemTransportBuilder()
					.withKey(transport.getId().toString()).withError(errorCode.getErrorCode());
			if (contractpartnerName != null) {
				builder.withVariableArray(Arrays.asList(contractpartnerName));
			}
			validationItems.add(builder.build());
		}
		final ValidationResponse expected = new ValidationResponse();
		expected.setValidationItemTransports(validationItems);
		expected.setResult(Boolean.FALSE);
		final ValidationResponse actual = super.callUsecaseExpect422(transport, ValidationResponse.class);
		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_ContractpartnerAccountNullBankaccount_Error() throws Exception {
		final ContractpartnerAccountTransport transport = new ContractpartnerAccountTransportBuilder()
				.forContractpartnerAccount2().build();
		transport.setAccountNumber(null);
		transport.setBankCode(null);
		this.testError(transport, null, ErrorCode.BANK_CODE_CONTAINS_ILLEGAL_CHARS_OR_IS_EMPTY,
				ErrorCode.ACCOUNT_NUMBER_CONTAINS_ILLEGAL_CHARS_OR_IS_EMPTY);
	}

	@Test
	void test_ContractpartnerAccountAlreadyExisting_Error() throws Exception {
		final ContractpartnerAccountTransport transport1 = new ContractpartnerAccountTransportBuilder()
				.forContractpartnerAccount1().build();
		final ContractpartnerAccountTransport transport2 = new ContractpartnerAccountTransportBuilder()
				.forContractpartnerAccount2().build();
		transport2.setAccountNumber(transport1.getAccountNumber());
		transport2.setBankCode(transport1.getBankCode());
		this.testError(transport2, ContractpartnerTransportBuilder.CONTRACTPARTNER1_NAME,
				ErrorCode.ACCOUNT_ALREADY_ASSIGNED_TO_OTHER_PARTNER);
	}

	@Test
	void test_AccountNumberAlreadyUsedButNotBankCode_Successfull() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(
				ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT2_ID);
		final ContractpartnerAccountTransport transport1 = new ContractpartnerAccountTransportBuilder()
				.forContractpartnerAccount1().build();
		final ContractpartnerAccountTransport transport2 = new ContractpartnerAccountTransportBuilder()
				.forContractpartnerAccount2().build();
		transport2.setAccountNumber(transport1.getAccountNumber());

		super.callUsecaseExpect204(transport2);

		final ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
				.getContractpartnerAccountById(userId, contractpartnerAccountId);
		Assertions.assertEquals(ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT2_ID,
				contractpartnerAccount.getId().getId());
		Assertions.assertEquals(transport2.getAccountNumber(),
				contractpartnerAccount.getBankAccount().getAccountNumber());
		Assertions.assertEquals(transport2.getBankCode(), contractpartnerAccount.getBankAccount().getBankCode());
	}

	@Test
	void test_BankCodeAlreadyUsedButNotAccountNumber_Successfull() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(
				ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT2_ID);
		final ContractpartnerAccountTransport transport1 = new ContractpartnerAccountTransportBuilder()
				.forContractpartnerAccount1().build();
		final ContractpartnerAccountTransport transport2 = new ContractpartnerAccountTransportBuilder()
				.forContractpartnerAccount2().build();
		transport2.setBankCode(transport1.getBankCode());

		super.callUsecaseExpect204(transport2);

		final ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
				.getContractpartnerAccountById(userId, contractpartnerAccountId);
		Assertions.assertEquals(ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT2_ID,
				contractpartnerAccount.getId().getId());
		Assertions.assertEquals(transport2.getAccountNumber(),
				contractpartnerAccount.getBankAccount().getAccountNumber());
		Assertions.assertEquals(transport2.getBankCode(), contractpartnerAccount.getBankAccount().getBankCode());
	}

	@Test
	void test_standardRequest_DefaultReturn() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(
				ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID);
		final ContractpartnerAccountTransport transport = new ContractpartnerAccountTransportBuilder()
				.forContractpartnerAccount1().build();
		transport.setAccountNumber("1");
		transport.setBankCode("2");

		super.callUsecaseExpect204(transport);

		final ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
				.getContractpartnerAccountById(userId, contractpartnerAccountId);
		Assertions.assertEquals(ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID,
				contractpartnerAccount.getId().getId());
		Assertions.assertEquals("1", contractpartnerAccount.getBankAccount().getAccountNumber());
		Assertions.assertEquals("2", contractpartnerAccount.getBankAccount().getBankCode());
	}

	@Test
	void test_standardRequest_MinimalReturn() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(
				ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID);
		final ContractpartnerAccountTransport transport = new ContractpartnerAccountTransportBuilder()
				.forContractpartnerAccount1().build();
		transport.setAccountNumber("1");
		transport.setBankCode("2");

		super.callUsecaseExpect204(transport);

		final ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
				.getContractpartnerAccountById(userId, contractpartnerAccountId);
		Assertions.assertEquals(ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID,
				contractpartnerAccount.getId().getId());
		Assertions.assertEquals("1", contractpartnerAccount.getBankAccount().getAccountNumber());
		Assertions.assertEquals("2", contractpartnerAccount.getBankAccount().getBankCode());
	}

	@Test
	void test_standardRequest_RepresentationReturn() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(
				ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID);
		final ContractpartnerAccountTransport transport = new ContractpartnerAccountTransportBuilder()
				.forContractpartnerAccount1().build();
		transport.setAccountNumber("1");
		transport.setBankCode("2");

		final ContractpartnerAccountTransport actualTransport = super.callUsecaseExpect200Representation(transport,
				ContractpartnerAccountTransport.class);

		Assertions.assertEquals(ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID,
				actualTransport.getId());

		final ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
				.getContractpartnerAccountById(userId, contractpartnerAccountId);
		Assertions.assertEquals(ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID,
				contractpartnerAccount.getId().getId());
		Assertions.assertEquals("1", contractpartnerAccount.getBankAccount().getAccountNumber());
		Assertions.assertEquals("2", contractpartnerAccount.getBankAccount().getBankCode());
	}

	@Test
	void test_Bic8Digits_fillesUpTo11Digits() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(
				ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID);
		final ContractpartnerAccountTransport transport = new ContractpartnerAccountTransportBuilder()
				.forContractpartnerAccount1().build();
		transport.setAccountNumber("1");
		transport.setBankCode("ABCDEFGH");

		super.callUsecaseExpect204(transport);

		final ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
				.getContractpartnerAccountById(userId, contractpartnerAccountId);
		Assertions.assertEquals(ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID,
				contractpartnerAccount.getId().getId());
		Assertions.assertEquals("1", contractpartnerAccount.getBankAccount().getAccountNumber());
		Assertions.assertEquals(transport.getBankCode() + "XXX", contractpartnerAccount.getBankAccount().getBankCode());
	}

	@Test
	void test_standardRequestChangingContractpartner_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerID contractpartner1Id = new ContractpartnerID(
				ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);
		final ContractpartnerID contractpartner2Id = new ContractpartnerID(
				ContractpartnerTransportBuilder.CONTRACTPARTNER2_ID);
		final ContractpartnerAccountTransport transport = new ContractpartnerAccountTransportBuilder()
				.forContractpartnerAccount1().build();
		transport.setContractpartnerid(ContractpartnerTransportBuilder.CONTRACTPARTNER2_ID);
		List<ContractpartnerAccount> contractpartner1Accounts = this.contractpartnerAccountService
				.getContractpartnerAccounts(userId, contractpartner1Id);
		List<ContractpartnerAccount> contractpartner2Accounts = this.contractpartnerAccountService
				.getContractpartnerAccounts(userId, contractpartner2Id);
		Assertions.assertEquals(2, contractpartner1Accounts.size());
		Assertions.assertTrue(contractpartner2Accounts.isEmpty());

		super.callUsecaseExpect204(transport);

		contractpartner1Accounts = this.contractpartnerAccountService.getContractpartnerAccounts(userId,
				contractpartner1Id);
		contractpartner2Accounts = this.contractpartnerAccountService.getContractpartnerAccounts(userId,
				contractpartner2Id);
		Assertions.assertEquals(1, contractpartner1Accounts.size());
		Assertions.assertEquals(1, contractpartner2Accounts.size());
	}

	@Test
	void test_editContractpartnerAccountOwnedBySomeoneElse_notSuccessfull() throws Exception {
		super.setUsername(UserTransportBuilder.ADMIN_NAME);
		super.setPassword(UserTransportBuilder.ADMIN_PASSWORD);
		final ContractpartnerAccountTransport transport = new ContractpartnerAccountTransportBuilder()
				.forContractpartnerAccount1().build();
		transport.setAccountNumber("1");
		transport.setBankCode("2");
		this.testError(transport, null, ErrorCode.CONTRACTPARTNER_DOES_NOT_EXIST);
	}

	@Test
	void test_ImportRoleNotAllowed_ErrorResponse() throws Exception {
		super.setUsername(UserTransportBuilder.IMPORTUSER_NAME);
		super.setPassword(UserTransportBuilder.IMPORTUSER_PASSWORD);

		super.callUsecaseExpect403(new ContractpartnerAccountTransport());
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

		this.testError(transport, null, ErrorCode.CONTRACTPARTNER_DOES_NOT_EXIST);
	}
}