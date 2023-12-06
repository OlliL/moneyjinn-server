
package org.laladev.moneyjinn.server.controller.moneyflow;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.MoneyflowSplitEntryTransportBuilder;
import org.laladev.moneyjinn.server.builder.MoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.MoneyflowControllerApi;
import org.laladev.moneyjinn.server.model.MoneyflowSplitEntryTransport;
import org.laladev.moneyjinn.server.model.MoneyflowTransport;
import org.laladev.moneyjinn.server.model.SearchMoneyflowsByAmountResponse;

class SearchMoneyflowsByAmountTest extends AbstractWebUserControllerTest {
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
		super.setUsername(UserTransportBuilder.USER3_NAME);
		super.setPassword(UserTransportBuilder.USER3_PASSWORD);
		final SearchMoneyflowsByAmountResponse expected = new SearchMoneyflowsByAmountResponse();
		final ArrayList<MoneyflowTransport> moneyflowTransports = new ArrayList<>();
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow14().build());
		expected.setMoneyflowTransports(moneyflowTransports);

		final SearchMoneyflowsByAmountResponse actual = super.callUsecaseExpect200(
				SearchMoneyflowsByAmountResponse.class, 10, 20091130, 20100202);

		Assertions.assertEquals(expected, actual);
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403WithUriVariables(10, 20091130, 20100202);
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		super.callUsecaseExpect204WithUriVariables(10, 20091130, 20100202);
	}
}