package org.laladev.moneyjinn.server.controller.contractpartneraccount;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.contractpartneraccount.ShowEditContractpartnerAccountResponse;
import org.laladev.moneyjinn.server.builder.ContractpartnerAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class ShowEditContractpartnerAccountTest extends AbstractControllerTest {

	private final HttpMethod method = HttpMethod.GET;
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
	public void test_unknownContractpartnerAccount_emptyResponseObject() throws Exception {
		final ShowEditContractpartnerAccountResponse expected = new ShowEditContractpartnerAccountResponse();
		final ShowEditContractpartnerAccountResponse actual = super.callUsecaseWithoutContent(
				"/" + ContractpartnerAccountTransportBuilder.NON_EXISTING_ID, this.method, false,
				ShowEditContractpartnerAccountResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_ContractpartnerAccount1_completeResponseObject() throws Exception {
		final ShowEditContractpartnerAccountResponse expected = new ShowEditContractpartnerAccountResponse();
		expected.setContractpartnerAccountTransport(
				new ContractpartnerAccountTransportBuilder().forContractpartnerAccount1().build());

		final ShowEditContractpartnerAccountResponse actual = super.callUsecaseWithoutContent(
				"/" + ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID, this.method, false,
				ShowEditContractpartnerAccountResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_ContractpartnerAccount1AsDifferingUserSameGroup_completeResponseObject() throws Exception {
		this.userName = UserTransportBuilder.USER3_NAME;
		this.userPassword = UserTransportBuilder.USER3_PASSWORD;
		final ShowEditContractpartnerAccountResponse expected = new ShowEditContractpartnerAccountResponse();
		expected.setContractpartnerAccountTransport(
				new ContractpartnerAccountTransportBuilder().forContractpartnerAccount1().build());

		final ShowEditContractpartnerAccountResponse actual = super.callUsecaseWithoutContent(
				"/" + ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID, this.method, false,
				ShowEditContractpartnerAccountResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_ContractpartnerAccount1AsDifferingUserOtherGroup_emptyResponseObject() throws Exception {
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
		final ShowEditContractpartnerAccountResponse expected = new ShowEditContractpartnerAccountResponse();

		final ShowEditContractpartnerAccountResponse actual = super.callUsecaseWithoutContent(
				"/" + ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID, this.method, false,
				ShowEditContractpartnerAccountResponse.class);

		Assert.assertEquals(expected, actual);
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

		final ShowEditContractpartnerAccountResponse expected = new ShowEditContractpartnerAccountResponse();

		final ShowEditContractpartnerAccountResponse actual = super.callUsecaseWithoutContent(
				"/" + ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID, this.method, false,
				ShowEditContractpartnerAccountResponse.class);

		Assert.assertEquals(expected, actual);

	}

}