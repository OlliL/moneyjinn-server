package org.laladev.moneyjinn.server.controller.predefmoneyflow;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.predefmoneyflow.ShowDeletePreDefMoneyflowResponse;
import org.laladev.moneyjinn.server.builder.PreDefMoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class ShowDeletePreDefMoneyflowTest extends AbstractControllerTest {

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

	@Test
	public void test_unknownPreDefMoneyflow_emptyResponseObject() throws Exception {
		final ShowDeletePreDefMoneyflowResponse expected = new ShowDeletePreDefMoneyflowResponse();
		final ShowDeletePreDefMoneyflowResponse actual = super.callUsecaseWithoutContent(
				"/" + PreDefMoneyflowTransportBuilder.NON_EXISTING_ID, this.method, false,
				ShowDeletePreDefMoneyflowResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	public void test_PreDefMoneyflowOwnedBySomeoneElse_emptyResponseObject() throws Exception {
		this.userName = UserTransportBuilder.USER3_NAME;
		this.userPassword = UserTransportBuilder.USER3_PASSWORD;
		final ShowDeletePreDefMoneyflowResponse expected = new ShowDeletePreDefMoneyflowResponse();
		final ShowDeletePreDefMoneyflowResponse actual = super.callUsecaseWithoutContent(
				"/" + PreDefMoneyflowTransportBuilder.PRE_DEF_MONEYFLOW1_ID, this.method, false,
				ShowDeletePreDefMoneyflowResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	public void test_PreDefMoneyflow1_completeResponseObject() throws Exception {
		final ShowDeletePreDefMoneyflowResponse expected = new ShowDeletePreDefMoneyflowResponse();
		expected.setPreDefMoneyflowTransport(new PreDefMoneyflowTransportBuilder().forPreDefMoneyflow1().build());

		final ShowDeletePreDefMoneyflowResponse actual = super.callUsecaseWithoutContent(
				"/" + PreDefMoneyflowTransportBuilder.PRE_DEF_MONEYFLOW1_ID, this.method, false,
				ShowDeletePreDefMoneyflowResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	public void test_AuthorizationRequired_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("/1", this.method, false, ErrorResponse.class);
		Assertions.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	public void test_emptyDatabase_noException() throws Exception {
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;

		final ShowDeletePreDefMoneyflowResponse expected = new ShowDeletePreDefMoneyflowResponse();

		final ShowDeletePreDefMoneyflowResponse actual = super.callUsecaseWithoutContent(
				"/" + PreDefMoneyflowTransportBuilder.PRE_DEF_MONEYFLOW1_ID, this.method, false,
				ShowDeletePreDefMoneyflowResponse.class);

		Assertions.assertEquals(expected, actual);
	}

}