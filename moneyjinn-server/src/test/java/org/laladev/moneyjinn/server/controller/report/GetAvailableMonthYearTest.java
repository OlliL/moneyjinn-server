
package org.laladev.moneyjinn.server.controller.report;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.ReportControllerApi;
import org.laladev.moneyjinn.server.model.GetAvailableReportMonthResponse;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.IMoneyflowService;

import jakarta.inject.Inject;

class GetAvailableMonthYearTest extends AbstractWebUserControllerTest {
	@Inject
	private ICapitalsourceService capitalsourceService;
	@Inject
	private IMoneyflowService moneyflowService;

	private static final List<Integer> ALL_YEARS = Arrays.asList(2008, 2009, 2010);

	@Override
	protected void loadMethod() {
		super.getMock(ReportControllerApi.class).getAvailableMonthYear(null);
	}

	private void assertEquals(final GetAvailableReportMonthResponse expected,
			final GetAvailableReportMonthResponse actual) {
		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_noArgumentOrOnlyYear_defaultsResponse() throws Exception {
		final GetAvailableReportMonthResponse expected = new GetAvailableReportMonthResponse();
		expected.setYear(2010);
		expected.setAllYears(ALL_YEARS);
		expected.setAllMonth(Arrays.asList(1, 2, 3, 4, 5));
		final GetAvailableReportMonthResponse actual = super.callUsecaseExpect200(GetAvailableReportMonthResponse.class,
				2010);
		this.assertEquals(expected, actual);
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403WithUriVariables(2010);
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		super.callUsecaseExpect200(GetAvailableReportMonthResponse.class, 2010);
	}
}