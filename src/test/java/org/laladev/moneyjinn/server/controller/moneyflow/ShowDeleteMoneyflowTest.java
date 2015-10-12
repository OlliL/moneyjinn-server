package org.laladev.moneyjinn.server.controller.moneyflow;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.moneyflow.ShowDeleteMoneyflowResponse;
import org.laladev.moneyjinn.server.builder.MoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class ShowDeleteMoneyflowTest extends AbstractControllerTest {

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
	public void test_unknownMoneyflow_emptyResponseObject() throws Exception {
		final ShowDeleteMoneyflowResponse expected = new ShowDeleteMoneyflowResponse();
		final ShowDeleteMoneyflowResponse actual = super.callUsecaseWithoutContent(
				"/" + MoneyflowTransportBuilder.NON_EXISTING_ID, this.method, false, ShowDeleteMoneyflowResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_MoneyflowOwnedBySomeoneElse_emptyResponseObject() throws Exception {
		this.userName = UserTransportBuilder.USER3_NAME;
		this.userPassword = UserTransportBuilder.USER3_PASSWORD;
		final ShowDeleteMoneyflowResponse expected = new ShowDeleteMoneyflowResponse();
		final ShowDeleteMoneyflowResponse actual = super.callUsecaseWithoutContent(
				"/" + MoneyflowTransportBuilder.MONEYFLOW1_ID, this.method, false, ShowDeleteMoneyflowResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_Moneyflow1_completeResponseObject() throws Exception {
		final ShowDeleteMoneyflowResponse expected = new ShowDeleteMoneyflowResponse();
		expected.setMoneyflowTransport(new MoneyflowTransportBuilder().forMoneyflow1().build());

		final ShowDeleteMoneyflowResponse actual = super.callUsecaseWithoutContent(
				"/" + MoneyflowTransportBuilder.MONEYFLOW1_ID, this.method, false, ShowDeleteMoneyflowResponse.class);

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
		final ShowDeleteMoneyflowResponse expected = new ShowDeleteMoneyflowResponse();
		final ShowDeleteMoneyflowResponse actual = super.callUsecaseWithoutContent(
				"/" + MoneyflowTransportBuilder.MONEYFLOW1_ID, this.method, false, ShowDeleteMoneyflowResponse.class);

		Assert.assertEquals(expected, actual);
	}
}