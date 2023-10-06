
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
import org.laladev.moneyjinn.server.model.MoneyflowTransport;
import org.laladev.moneyjinn.server.model.SearchMoneyflowsByAmountResponse;
import org.laladev.moneyjinn.service.api.IMoneyflowService;
import org.springframework.test.context.jdbc.Sql;

import jakarta.inject.Inject;

class SearchMoneyflowsByAmountTest extends AbstractControllerTest {
	private String userName;
	private String userPassword;

	@Inject
	IMoneyflowService moneyflowService;

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
		super.getMock(MoneyflowControllerApi.class).searchMoneyflowsByAmount(null, null, null);
	}

	@Test
	void test_searchSingleFlowPositiveAmount_successfull() throws Exception {
		final SearchMoneyflowsByAmountResponse expected = new SearchMoneyflowsByAmountResponse();
		final ArrayList<MoneyflowTransport> moneyflowTransports = new ArrayList<>();
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow1().build());
		expected.setMoneyflowTransports(moneyflowTransports);
		final List<MoneyflowSplitEntryTransport> moneyflowSplitEntryTransports = new ArrayList<>();
		moneyflowSplitEntryTransports.add(new MoneyflowSplitEntryTransportBuilder().forMoneyflowSplitEntry1().build());
		moneyflowSplitEntryTransports.add(new MoneyflowSplitEntryTransportBuilder().forMoneyflowSplitEntry2().build());
		expected.setMoneyflowSplitEntryTransports(moneyflowSplitEntryTransports);

		final SearchMoneyflowsByAmountResponse actual = super.callUsecaseExpect200(
				SearchMoneyflowsByAmountResponse.class, 1.10, 20081231, 20090102);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_searchNegativeAmount_negativeAndPositiveAmountAreFound() throws Exception {
		final SearchMoneyflowsByAmountResponse expected = new SearchMoneyflowsByAmountResponse();
		final ArrayList<MoneyflowTransport> moneyflowTransports = new ArrayList<>();
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow13().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow14().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow15().build());
		expected.setMoneyflowTransports(moneyflowTransports);

		final SearchMoneyflowsByAmountResponse actual = super.callUsecaseExpect200(
				SearchMoneyflowsByAmountResponse.class, 10, 20091201, 20100201);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_searchSingleFlowOwnedBySomeoneElseAndPrivate_notIncluded() throws Exception {
		this.userName = UserTransportBuilder.USER3_NAME;
		this.userPassword = UserTransportBuilder.USER3_PASSWORD;
		final SearchMoneyflowsByAmountResponse expected = new SearchMoneyflowsByAmountResponse();
		final ArrayList<MoneyflowTransport> moneyflowTransports = new ArrayList<>();
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow14().build());
		expected.setMoneyflowTransports(moneyflowTransports);

		final SearchMoneyflowsByAmountResponse actual = super.callUsecaseExpect200(
				SearchMoneyflowsByAmountResponse.class, 10, 20091130, 20100202);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_AuthorizationRequired_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;

		super.callUsecaseExpect403WithUriVariables(10, 20091130, 20100202);
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	void test_emptyDatabase_noException() throws Exception {
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;

		super.callUsecaseExpect204WithUriVariables(10, 20091130, 20100202);
	}
}