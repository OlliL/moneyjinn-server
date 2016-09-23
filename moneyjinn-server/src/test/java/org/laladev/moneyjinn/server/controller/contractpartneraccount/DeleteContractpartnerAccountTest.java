package org.laladev.moneyjinn.server.controller.contractpartneraccount;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.model.ContractpartnerAccount;
import org.laladev.moneyjinn.model.ContractpartnerAccountID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.server.builder.ContractpartnerAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.IContractpartnerAccountService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class DeleteContractpartnerAccountTest extends AbstractControllerTest {

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
	public void test_regularContractpartnerAccountNoData_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(
				ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT2_ID);

		ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
				.getContractpartnerAccountById(userId, contractpartnerAccountId);

		Assert.assertNotNull(contractpartnerAccount);

		super.callUsecaseWithoutContent("/" + ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT2_ID,
				this.method, true, Object.class);

		contractpartnerAccount = this.contractpartnerAccountService.getContractpartnerAccountById(userId,
				contractpartnerAccountId);

		Assert.assertNull(contractpartnerAccount);
	}

	@Test
	public void test_nonExistingContractpartnerAccount_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(
				ContractpartnerAccountTransportBuilder.NON_EXISTING_ID);

		ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
				.getContractpartnerAccountById(userId, contractpartnerAccountId);

		Assert.assertNull(contractpartnerAccount);

		super.callUsecaseWithoutContent("/" + ContractpartnerAccountTransportBuilder.NON_EXISTING_ID, this.method, true,
				Object.class);

		contractpartnerAccount = this.contractpartnerAccountService.getContractpartnerAccountById(userId,
				contractpartnerAccountId);

		Assert.assertNull(contractpartnerAccount);
	}

	@Test
	public void test_ContractpartnerAccountFromSameGroupButNotMe_SuccessfullNoContent() throws Exception {
		this.userName = UserTransportBuilder.USER3_NAME;
		this.userPassword = UserTransportBuilder.USER3_PASSWORD;
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(
				ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID);

		// Uncommented because of Cache eviction does not work right now for differing users
		// ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
		// .getContractpartnerAccountById(userId, contractpartnerAccountId);
		//
		// Assert.assertNotNull(contractpartnerAccount);

		super.callUsecaseWithoutContent("/" + ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID,
				this.method, true, Object.class);

		final ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
				.getContractpartnerAccountById(userId, contractpartnerAccountId);

		Assert.assertNull(contractpartnerAccount);
	}

	@Test
	public void test_ContractpartnerAccountFromDifferentGroup_notSuccessfull() throws Exception {
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(
				ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID);

		// Uncommented because of Cache eviction does not work right now for differing users
		// ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
		// .getContractpartnerAccountById(userId, contractpartnerAccountId);
		//
		// Assert.assertNotNull(contractpartnerAccount);

		super.callUsecaseWithoutContent("/" + ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID,
				this.method, true, Object.class);

		final ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
				.getContractpartnerAccountById(userId, contractpartnerAccountId);

		Assert.assertNotNull(contractpartnerAccount);
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

		super.callUsecaseWithoutContent("/" + ContractpartnerAccountTransportBuilder.NON_EXISTING_ID, this.method, true,
				Object.class);

	}
}