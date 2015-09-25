package org.laladev.moneyjinn.server.controller.contractpartneraccount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerAccount;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerAccountID;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.service.api.IAccessRelationService;
import org.laladev.moneyjinn.businesslogic.service.api.IContractpartnerAccountService;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import org.laladev.moneyjinn.core.rest.model.contractpartneraccount.UpdateContractpartnerAccountRequest;
import org.laladev.moneyjinn.core.rest.model.transport.ContractpartnerAccountTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.server.builder.ContractpartnerAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;

public class UpdateContractpartnerAccountTest extends AbstractControllerTest {

	@Inject
	IContractpartnerAccountService contractpartnerAccountService;

	@Inject
	IAccessRelationService accessRelationService;

	private final HttpMethod method = HttpMethod.PUT;
	private String userName;
	private String userPassword;

	@Before
	public void setUp() {
		this.userName = UserTransportBuilder.USER1_NAME;
		this.userPassword = UserTransportBuilder.USER1_PASSWORD;
	}

	@Override
	protected String getUsername() {
		return this.userName;
	}

	@Override
	protected String getPassword() {
		return this.userPassword;
	}

	@Override
	protected String getUsecase() {
		return super.getUsecaseFromTestClassName(this.getClass());
	}

	private void testError(final ContractpartnerAccountTransport transport, final ErrorCode errorCode,
			final String contractpartnerName) throws Exception {
		final UpdateContractpartnerAccountRequest request = new UpdateContractpartnerAccountRequest();

		request.setContractpartnerAccountTransport(transport);

		final List<ValidationItemTransport> validationItems = new ArrayList<>();
		validationItems.add(new ValidationItemTransportBuilder().withKey(transport.getId().intValue())
				.withError(errorCode.getErrorCode()).withVariableArray(Arrays.asList(contractpartnerName)).build());

		final ValidationResponse expected = new ValidationResponse();
		expected.setValidationItemTransports(validationItems);
		expected.setResult(Boolean.FALSE);

		final ValidationResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				ValidationResponse.class);

		Assert.assertEquals(expected, actual);

	}

	@Test
	public void test_ContractpartnerAccountAlreadyExisting_Error() throws Exception {

		final ContractpartnerAccountTransport transport1 = new ContractpartnerAccountTransportBuilder()
				.forContractpartnerAccount1().build();
		final ContractpartnerAccountTransport transport2 = new ContractpartnerAccountTransportBuilder()
				.forContractpartnerAccount2().build();
		transport2.setAccountNumber(transport1.getAccountNumber());
		transport2.setBankCode(transport1.getBankCode());

		this.testError(transport2, ErrorCode.ACCOUNT_ALREADY_ASSIGNED_TO_OTHER_PARTNER,
				ContractpartnerTransportBuilder.CONTRACTPARTNER1_NAME);
	}

	@Test
	public void test_AccountNumberAlreadyUsedButNotBankCode_Successfull() throws Exception {

		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(
				ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT2_ID);
		final ContractpartnerAccountTransport transport1 = new ContractpartnerAccountTransportBuilder()
				.forContractpartnerAccount1().build();
		final ContractpartnerAccountTransport transport2 = new ContractpartnerAccountTransportBuilder()
				.forContractpartnerAccount2().build();
		transport2.setAccountNumber(transport1.getAccountNumber());

		final UpdateContractpartnerAccountRequest request = new UpdateContractpartnerAccountRequest();
		request.setContractpartnerAccountTransport(transport2);
		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
				.getContractpartnerAccountById(userId, contractpartnerAccountId);
		Assert.assertEquals(ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT2_ID,
				contractpartnerAccount.getId().getId());
		Assert.assertEquals(transport2.getAccountNumber(), contractpartnerAccount.getBankAccount().getAccountNumber());
		Assert.assertEquals(transport2.getBankCode(), contractpartnerAccount.getBankAccount().getBankCode());
	}

	@Test
	public void test_BankCodeAlreadyUsedButNotAccountNumber_Successfull() throws Exception {

		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(
				ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT2_ID);
		final ContractpartnerAccountTransport transport1 = new ContractpartnerAccountTransportBuilder()
				.forContractpartnerAccount1().build();
		final ContractpartnerAccountTransport transport2 = new ContractpartnerAccountTransportBuilder()
				.forContractpartnerAccount2().build();
		transport2.setBankCode(transport1.getBankCode());

		final UpdateContractpartnerAccountRequest request = new UpdateContractpartnerAccountRequest();
		request.setContractpartnerAccountTransport(transport2);
		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
				.getContractpartnerAccountById(userId, contractpartnerAccountId);
		Assert.assertEquals(ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT2_ID,
				contractpartnerAccount.getId().getId());
		Assert.assertEquals(transport2.getAccountNumber(), contractpartnerAccount.getBankAccount().getAccountNumber());
		Assert.assertEquals(transport2.getBankCode(), contractpartnerAccount.getBankAccount().getBankCode());
	}

	@Test
	public void test_standardRequest_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(
				ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID);

		final UpdateContractpartnerAccountRequest request = new UpdateContractpartnerAccountRequest();

		final ContractpartnerAccountTransport transport = new ContractpartnerAccountTransportBuilder()
				.forContractpartnerAccount1().build();
		transport.setAccountNumber("1");
		transport.setBankCode("2");
		request.setContractpartnerAccountTransport(transport);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
				.getContractpartnerAccountById(userId, contractpartnerAccountId);

		Assert.assertEquals(ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID,
				contractpartnerAccount.getId().getId());
		Assert.assertEquals("1", contractpartnerAccount.getBankAccount().getAccountNumber());
		Assert.assertEquals("2", contractpartnerAccount.getBankAccount().getBankCode());
	}

	@Test
	public void test_editContractpartnerAccountOwnedBySomeoneElse_notSuccessfull() throws Exception {
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(
				ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID);

		final UpdateContractpartnerAccountRequest request = new UpdateContractpartnerAccountRequest();

		final ContractpartnerAccountTransport transport = new ContractpartnerAccountTransportBuilder()
				.forContractpartnerAccount1().build();
		transport.setAccountNumber("1");
		transport.setBankCode("2");
		request.setContractpartnerAccountTransport(transport);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
				.getContractpartnerAccountById(userId, contractpartnerAccountId);

		final ContractpartnerAccountTransport transportOriginal = new ContractpartnerAccountTransportBuilder()
				.forContractpartnerAccount1().build();
		Assert.assertEquals(ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID,
				contractpartnerAccount.getId().getId());
		Assert.assertEquals(transportOriginal.getAccountNumber(),
				contractpartnerAccount.getBankAccount().getAccountNumber());
		Assert.assertEquals(transportOriginal.getBankCode(), contractpartnerAccount.getBankAccount().getBankCode());
	}

	@Test
	public void test_AuthorizationRequired_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("", this.method, false, ErrorResponse.class);
		Assert.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

}