
package org.laladev.moneyjinn.server.controller.moneyflow;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.MoneyflowSplitEntryTransportBuilder;
import org.laladev.moneyjinn.server.builder.MoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.controller.api.MoneyflowControllerApi;
import org.laladev.moneyjinn.server.model.MoneyflowSplitEntryTransport;
import org.laladev.moneyjinn.server.model.ShowEditMoneyflowResponse;
import org.springframework.test.context.jdbc.Sql;

class ShowEditMoneyflowTest extends AbstractControllerTest {
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
	protected void loadMethod() {
		super.getMock(MoneyflowControllerApi.class).showEditMoneyflow(null);
	}

	@Test
	void test_unknownMoneyflow_emptyResponseObject() throws Exception {
		final ShowEditMoneyflowResponse expected = new ShowEditMoneyflowResponse();

		final ShowEditMoneyflowResponse actual = super.callUsecaseExpect200(ShowEditMoneyflowResponse.class,
				MoneyflowTransportBuilder.NON_EXISTING_ID);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_MoneyflowOwnedBySomeoneElse_emptyResponseObject() throws Exception {
		this.userName = UserTransportBuilder.USER3_NAME;
		this.userPassword = UserTransportBuilder.USER3_PASSWORD;
		final ShowEditMoneyflowResponse expected = new ShowEditMoneyflowResponse();

		final ShowEditMoneyflowResponse actual = super.callUsecaseExpect200(ShowEditMoneyflowResponse.class,
				MoneyflowTransportBuilder.MONEYFLOW1_ID);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_Moneyflow1_completeResponseObject() throws Exception {
		final ShowEditMoneyflowResponse expected = new ShowEditMoneyflowResponse();
		expected.setMoneyflowTransport(new MoneyflowTransportBuilder().forMoneyflow1().build());
		final List<MoneyflowSplitEntryTransport> moneyflowSplitEntryTransports = new ArrayList<>();
		moneyflowSplitEntryTransports.add(new MoneyflowSplitEntryTransportBuilder().forMoneyflowSplitEntry1().build());
		moneyflowSplitEntryTransports.add(new MoneyflowSplitEntryTransportBuilder().forMoneyflowSplitEntry2().build());
		expected.setMoneyflowSplitEntryTransports(moneyflowSplitEntryTransports);
		expected.setHasReceipt(true);

		final ShowEditMoneyflowResponse actual = super.callUsecaseExpect200(ShowEditMoneyflowResponse.class,
				MoneyflowTransportBuilder.MONEYFLOW1_ID);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_AuthorizationRequired_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;

		super.callUsecaseExpect403WithUriVariables(MoneyflowTransportBuilder.MONEYFLOW1_ID);
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	void test_emptyDatabase_noException() throws Exception {
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
		final ShowEditMoneyflowResponse expected = new ShowEditMoneyflowResponse();

		final ShowEditMoneyflowResponse actual = super.callUsecaseExpect200(ShowEditMoneyflowResponse.class,
				MoneyflowTransportBuilder.MONEYFLOW1_ID);

		Assertions.assertEquals(expected, actual);
	}
}