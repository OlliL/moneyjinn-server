
package org.laladev.moneyjinn.server.controller.monthlysettlement;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.MonthlySettlementTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.MonthlySettlementControllerApi;
import org.laladev.moneyjinn.server.model.MonthlySettlementTransport;
import org.laladev.moneyjinn.server.model.ShowMonthlySettlementListResponse;

class ShowMonthlySettlementListV2Test extends AbstractWebUserControllerTest {

	@Override
	protected void loadMethod() {
		super.getMock(MonthlySettlementControllerApi.class).showMonthlySettlementListV2(null, null);
	}

	@Test
	void test_withYearAndInvalidMonth_EmptyResponseObject() throws Exception {
		final ShowMonthlySettlementListResponse expected = new ShowMonthlySettlementListResponse();
		expected.setMonthlySettlementTransports(new ArrayList<>());
		final ShowMonthlySettlementListResponse actual = super.callUsecaseExpect200(
				ShowMonthlySettlementListResponse.class, 2010, 10);
		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_withInvalidYearAndInvalidMonth13_EmptyResponseObject() throws Exception {
		final ShowMonthlySettlementListResponse expected = new ShowMonthlySettlementListResponse();
		final ShowMonthlySettlementListResponse actual = super.callUsecaseExpect200(
				ShowMonthlySettlementListResponse.class, 1, 13);
		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_withInvalidYearAndInvalidMonth0_EmptyResponseObject() throws Exception {
		final ShowMonthlySettlementListResponse expected = new ShowMonthlySettlementListResponse();
		final ShowMonthlySettlementListResponse actual = super.callUsecaseExpect200(
				ShowMonthlySettlementListResponse.class, 1, 0);
		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_withYearAndMonth_FullResponseObject() throws Exception {
		final List<MonthlySettlementTransport> monthlySettlementTransports = new ArrayList<>();
		monthlySettlementTransports.add(new MonthlySettlementTransportBuilder().forMonthlySettlement1().build());
		monthlySettlementTransports.add(new MonthlySettlementTransportBuilder().forMonthlySettlement2().build());
		monthlySettlementTransports.add(new MonthlySettlementTransportBuilder().forMonthlySettlement3().build());
		final ShowMonthlySettlementListResponse expected = new ShowMonthlySettlementListResponse();
		expected.setMonthlySettlementTransports(monthlySettlementTransports);

		final ShowMonthlySettlementListResponse actual = super.callUsecaseExpect200(
				ShowMonthlySettlementListResponse.class, 2008, 12);

		Assertions.assertEquals(expected, actual);
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403WithUriVariables(2008, 12);
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		final ShowMonthlySettlementListResponse expected = new ShowMonthlySettlementListResponse();
		expected.setMonthlySettlementTransports(new ArrayList<>());

		final ShowMonthlySettlementListResponse actual = super.callUsecaseExpect200(
				ShowMonthlySettlementListResponse.class, 2020, 10);

		Assertions.assertEquals(expected, actual);
	}
}
