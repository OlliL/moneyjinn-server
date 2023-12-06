
package org.laladev.moneyjinn.server.controller.report;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowID;
import org.laladev.moneyjinn.server.builder.MoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.ReportControllerApi;
import org.laladev.moneyjinn.server.model.GetAvailableReportMonthResponse;
import org.laladev.moneyjinn.service.api.IMoneyflowService;

import jakarta.inject.Inject;

class GetAvailableMonthYearMonthTest extends AbstractWebUserControllerTest {
	@Inject
	private IMoneyflowService moneyflowService;

	private static final List<Integer> ALL_YEARS = Arrays.asList(2008, 2009, 2010);

	@Override
	protected void loadMethod() {
		super.getMock(ReportControllerApi.class).getAvailableMonthYearMonth(null, null);
	}

	private void assertEquals(final GetAvailableReportMonthResponse expected,
			final GetAvailableReportMonthResponse actual) {
		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_invalidMonth_defaultsResponse() throws Exception {
		final GetAvailableReportMonthResponse expected = new GetAvailableReportMonthResponse();
		expected.setYear(2010);
		expected.setAllYears(ALL_YEARS);
		expected.setAllMonth(Arrays.asList(1, 2, 3, 4, 5));

		GetAvailableReportMonthResponse actual = super.callUsecaseExpect200(GetAvailableReportMonthResponse.class, 2010,
				13);

		this.assertEquals(expected, actual);

		actual = super.callUsecaseExpect200(GetAvailableReportMonthResponse.class, 2010, 0);

		this.assertEquals(expected, actual);

		actual = super.callUsecaseExpect200(GetAvailableReportMonthResponse.class, 2010, 11);

		this.assertEquals(expected, actual);
	}

	@Test
	void test_DecemberFirstMonthAtAllSettledAndAlsoPreviousMonthSettled_completeResponse() throws Exception {
		final GetAvailableReportMonthResponse expected = new GetAvailableReportMonthResponse();
		expected.setYear(2008);
		expected.setAllYears(ALL_YEARS);
		expected.setAllMonth(Arrays.asList(12));
		expected.setMonth(12);
		expected.setNextMonth(1);
		expected.setNextYear(2009);
		expected.setNextMonthHasMoneyflows(1);

		final GetAvailableReportMonthResponse actual = super.callUsecaseExpect200(GetAvailableReportMonthResponse.class,
				2008, 12);

		this.assertEquals(expected, actual);
	}

	@Test
	void test_JanuarySettledAndAlsoPreviousMonthSettled_completeResponse() throws Exception {
		final GetAvailableReportMonthResponse expected = new GetAvailableReportMonthResponse();
		expected.setYear(2009);
		expected.setAllYears(ALL_YEARS);
		expected.setAllMonth(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));
		expected.setMonth(1);
		expected.setPreviousMonth(12);
		expected.setPreviousYear(2008);
		expected.setNextMonth(2);
		expected.setNextYear(2009);
		expected.setNextMonthHasMoneyflows(1);
		expected.setPreviousMonthHasMoneyflows(1);

		final GetAvailableReportMonthResponse actual = super.callUsecaseExpect200(GetAvailableReportMonthResponse.class,
				2009, 1);

		this.assertEquals(expected, actual);
	}

	@Test
	void test_DecemberSettledAndAlsoPreviousMonthSettled_completeResponse() throws Exception {
		final GetAvailableReportMonthResponse expected = new GetAvailableReportMonthResponse();
		expected.setYear(2009);
		expected.setAllYears(ALL_YEARS);
		expected.setAllMonth(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));
		expected.setMonth(12);
		expected.setPreviousMonth(11);
		expected.setPreviousYear(2009);
		expected.setNextMonth(1);
		expected.setNextYear(2010);
		expected.setNextMonthHasMoneyflows(1);
		expected.setPreviousMonthHasMoneyflows(1);

		final GetAvailableReportMonthResponse actual = super.callUsecaseExpect200(GetAvailableReportMonthResponse.class,
				2009, 12);

		this.assertEquals(expected, actual);
	}

	@Test
	void test_DecemberAndNoMoneyflowsNextMonth_completeResponse() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW14_ID);
		this.moneyflowService.deleteMoneyflow(userId, moneyflowId);
		final GetAvailableReportMonthResponse expected = new GetAvailableReportMonthResponse();
		expected.setYear(2009);
		expected.setAllYears(ALL_YEARS);
		expected.setAllMonth(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));
		expected.setMonth(12);
		expected.setPreviousMonth(11);
		expected.setPreviousYear(2009);
		expected.setNextMonth(2);
		expected.setNextYear(2010);
		expected.setPreviousMonthHasMoneyflows(1);
		expected.setNextMonthHasMoneyflows(1);

		final GetAvailableReportMonthResponse actual = super.callUsecaseExpect200(GetAvailableReportMonthResponse.class,
				2009, 12);

		this.assertEquals(expected, actual);
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403WithUriVariables(2010, 1);
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		super.callUsecaseExpect200(GetAvailableReportMonthResponse.class, 2010, 1);
	}
}