package org.laladev.moneyjinn.server.controller.contractpartner;

import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.model.Contractpartner;
import org.laladev.moneyjinn.model.ContractpartnerAccount;
import org.laladev.moneyjinn.model.ContractpartnerID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.IContractpartnerAccountService;
import org.laladev.moneyjinn.service.api.IContractpartnerService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class DeleteContractpartnerTest extends AbstractControllerTest {

	@Inject
	IContractpartnerService contractpartnerService;
	@Inject
	IContractpartnerAccountService contractpartnerAccountService;

	private final HttpMethod method = HttpMethod.DELETE;
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

	@Test
	public void test_regularContractpartnerWitPreDefMoneyflows_ErrorResponse() throws Exception {
		this.userName = UserTransportBuilder.USER3_NAME;
		this.userPassword = UserTransportBuilder.USER3_PASSWORD;
		final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(ContractpartnerTransportBuilder.CONTRACTPARTNER3_ID);

		final ErrorResponse expected = new ErrorResponse();
		expected.setCode(ErrorCode.CONTRACTPARTNER_IN_USE.getErrorCode());
		expected.setMessage("You may not delete a contractual partner who is still referenced by a flow of money!");

		Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);

		Assert.assertNotNull(contractpartner);

		final ErrorResponse response = super.callUsecaseWithoutContent("/" + ContractpartnerTransportBuilder.CONTRACTPARTNER3_ID, this.method, false,
				ErrorResponse.class);

		contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);

		Assert.assertNotNull(contractpartner);
		Assert.assertEquals(expected, response);
	}

	@Test
	public void test_regularContractpartnerNoData_SuccessfullNoContent() throws Exception {
		this.userName = UserTransportBuilder.USER3_NAME;
		this.userPassword = UserTransportBuilder.USER3_PASSWORD;
		final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(ContractpartnerTransportBuilder.CONTRACTPARTNER4_ID);

		Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);

		Assert.assertNotNull(contractpartner);

		super.callUsecaseWithoutContent("/" + ContractpartnerTransportBuilder.CONTRACTPARTNER4_ID, this.method, true, Object.class);

		contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);

		Assert.assertNull(contractpartner);
	}

	@Test
	public void test_regularContractpartnerWith1Account_AccountsDeleted() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(ContractpartnerTransportBuilder.CONTRACTPARTNER4_ID);

		Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);
		Assert.assertNotNull(contractpartner);

		List<ContractpartnerAccount> contractpartnerAccounts = this.contractpartnerAccountService.getContractpartnerAccounts(userId, contractpartnerId);
		Assert.assertEquals(1, contractpartnerAccounts.size());

		super.callUsecaseWithoutContent("/" + ContractpartnerTransportBuilder.CONTRACTPARTNER4_ID, this.method, true, Object.class);

		contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);
		Assert.assertNull(contractpartner);

		contractpartnerAccounts = this.contractpartnerAccountService.getContractpartnerAccounts(userId, contractpartnerId);
		Assert.assertEquals(0, contractpartnerAccounts.size());
	}

	@Test
	public void test_regularContractpartnerWith2Accounts_AccountsDeleted() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(ContractpartnerTransportBuilder.CONTRACTPARTNER4_ID);

		Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);
		Assert.assertNotNull(contractpartner);

		List<ContractpartnerAccount> contractpartnerAccounts = this.contractpartnerAccountService.getContractpartnerAccounts(userId, contractpartnerId);
		ContractpartnerAccount contractpartnerAccount = contractpartnerAccounts.get(0);
		contractpartnerAccount.getBankAccount().setAccountNumber("TEST12345");
		this.contractpartnerAccountService.createContractpartnerAccount(userId, contractpartnerAccount);
		contractpartnerAccounts = this.contractpartnerAccountService.getContractpartnerAccounts(userId, contractpartnerId);
		Assert.assertEquals(2, contractpartnerAccounts.size());

		super.callUsecaseWithoutContent("/" + ContractpartnerTransportBuilder.CONTRACTPARTNER4_ID, this.method, true, Object.class);

		contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);
		Assert.assertNull(contractpartner);

		contractpartnerAccounts = this.contractpartnerAccountService.getContractpartnerAccounts(userId, contractpartnerId);
		Assert.assertEquals(0, contractpartnerAccounts.size());
	}

	@Test
	public void test_nonExistingContractpartner_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(ContractpartnerTransportBuilder.NON_EXISTING_ID);

		Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);

		Assert.assertNull(contractpartner);

		super.callUsecaseWithoutContent("/" + ContractpartnerTransportBuilder.NON_EXISTING_ID, this.method, true, Object.class);

		contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);

		Assert.assertNull(contractpartner);
	}

	@Test
	public void test_regularContractpartnerWithData_ErrorResponse() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);

		final ErrorResponse expected = new ErrorResponse();
		expected.setCode(ErrorCode.CONTRACTPARTNER_IN_USE.getErrorCode());
		expected.setMessage("You may not delete a contractual partner who is still referenced by a flow of money!");

		Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);

		Assert.assertNotNull(contractpartner);

		final ErrorResponse response = super.callUsecaseWithoutContent("/" + ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID, this.method, false,
				ErrorResponse.class);

		contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);

		Assert.assertNotNull(contractpartner);
		Assert.assertEquals(expected, response);

		final List<ContractpartnerAccount> contractpartnerAccounts = this.contractpartnerAccountService.getContractpartnerAccounts(userId, contractpartnerId);
		Assert.assertEquals(2, contractpartnerAccounts.size());
	}

	@Test
	public void test_ContractpartnerFromSameGroupButNotMe_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(ContractpartnerTransportBuilder.CONTRACTPARTNER4_ID);

		Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);

		Assert.assertNotNull(contractpartner);

		super.callUsecaseWithoutContent("/" + ContractpartnerTransportBuilder.CONTRACTPARTNER4_ID, this.method, true, Object.class);

		contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);

		Assert.assertNull(contractpartner);
	}

	@Test
	public void test_ContractpartnerFromDifferentGroup_notSuccessfull() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.ADMIN_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(ContractpartnerTransportBuilder.CONTRACTPARTNER5_ID);

		Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);

		Assert.assertNotNull(contractpartner);

		super.callUsecaseWithoutContent("/" + ContractpartnerTransportBuilder.CONTRACTPARTNER5_ID, this.method, true, Object.class);

		contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);

		Assert.assertNotNull(contractpartner);
	}

	@Test
	public void test_AuthorizationRequired_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("/1", this.method, false, ErrorResponse.class);
		Assert.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	public void test_emptyDatabase_noException() throws Exception {
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;

		super.callUsecaseWithoutContent("/" + ContractpartnerTransportBuilder.NON_EXISTING_ID, this.method, true, Object.class);

	}
}