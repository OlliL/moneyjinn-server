package org.laladev.moneyjinn.server.controller.contractpartneraccount;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.contractpartneraccount.ShowContractpartnerAccountListResponse;
import org.laladev.moneyjinn.core.rest.model.transport.ContractpartnerAccountTransport;
import org.laladev.moneyjinn.server.builder.ContractpartnerAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class ShowContractpartnerAccountListTest extends AbstractControllerTest {

	private final HttpMethod method = HttpMethod.GET;
	private String userName;
	private String userPassword;

	@BeforeEach
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

	private ShowContractpartnerAccountListResponse getCompleteResponse() {
		final ShowContractpartnerAccountListResponse expected = new ShowContractpartnerAccountListResponse();
		final List<ContractpartnerAccountTransport> contractpartnerAccountTransports = new ArrayList<>();
		contractpartnerAccountTransports
				.add(new ContractpartnerAccountTransportBuilder().forContractpartnerAccount1().build());
		contractpartnerAccountTransports
				.add(new ContractpartnerAccountTransportBuilder().forContractpartnerAccount2().build());
		expected.setContractpartnerAccountTransports(contractpartnerAccountTransports);
		expected.setContractpartnerName(ContractpartnerTransportBuilder.CONTRACTPARTNER1_NAME);
		return expected;
	}

	@Test
	public void test_default_FullResponseObject() throws Exception {
		final ShowContractpartnerAccountListResponse expected = this.getCompleteResponse();

		final ShowContractpartnerAccountListResponse actual = super.callUsecaseWithoutContent(
				"/" + ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID, this.method, false,
				ShowContractpartnerAccountListResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	public void test_contractpartnerWithNoAccounts_responseWithNoAccountsButName() throws Exception {
		final ShowContractpartnerAccountListResponse expected = new ShowContractpartnerAccountListResponse();
		expected.setContractpartnerName(ContractpartnerTransportBuilder.CONTRACTPARTNER2_NAME);

		final ShowContractpartnerAccountListResponse actual = super.callUsecaseWithoutContent(
				"/" + ContractpartnerTransportBuilder.CONTRACTPARTNER2_ID, this.method, false,
				ShowContractpartnerAccountListResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	public void test_notExistingContractpartner_emptyResponseObject() throws Exception {
		final ShowContractpartnerAccountListResponse expected = new ShowContractpartnerAccountListResponse();

		final ShowContractpartnerAccountListResponse actual = super.callUsecaseWithoutContent(
				"/" + ContractpartnerTransportBuilder.NON_EXISTING_ID, this.method, false,
				ShowContractpartnerAccountListResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	public void test_AuthorizationRequired_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent(
				"/" + ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID, this.method, false, ErrorResponse.class);
		Assertions.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	public void test_emptyDatabase_noException() throws Exception {
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;

		final ShowContractpartnerAccountListResponse expected = new ShowContractpartnerAccountListResponse();

		final ShowContractpartnerAccountListResponse actual = super.callUsecaseWithoutContent(
				"/" + ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID, this.method, false,
				ShowContractpartnerAccountListResponse.class);

		Assertions.assertEquals(expected, actual);

	}

}
