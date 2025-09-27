package org.laladev.moneyjinn.server.controller.monthlysettlement;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.MonthlySettlementControllerApi;
import org.laladev.moneyjinn.server.model.GetAvailableMonthlySettlementMonthResponse;

import java.util.Arrays;

class GetAvailableMonthYearMonthTest extends AbstractWebUserControllerTest {

    @Override
    protected void loadMethod() {
        super.getMock(MonthlySettlementControllerApi.class).getAvailableMonthYearMonth(null, null);
    }

    private GetAvailableMonthlySettlementMonthResponse getDefaultResponse() {
        final GetAvailableMonthlySettlementMonthResponse expected = new GetAvailableMonthlySettlementMonthResponse();
        expected.setAllMonth(Arrays.asList(1, 2, 3, 4));
        expected.setAllYears(Arrays.asList(2008, 2009, 2010));
        expected.setYear(2010);
        return expected;
    }

    private void invalidMonth(final int year, final int month) throws Exception {
        final GetAvailableMonthlySettlementMonthResponse expected = this.getDefaultResponse();
        final GetAvailableMonthlySettlementMonthResponse actual = super.callUsecaseExpect200(
                GetAvailableMonthlySettlementMonthResponse.class, year, month);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void test_withYearAndInvalidMonth_FullResponseObject() throws Exception {
        this.invalidMonth(2010, 10);
    }

    @Test
    void test_withInvalidYearAndInvalidMonth13_FullResponseObject() throws Exception {
        this.invalidMonth(1, 13);
    }

    @Test
    void test_withInvalidYearAndInvalidMonth0_FullResponseObject() throws Exception {
        this.invalidMonth(1, 0);
    }

    @Test
    void test_withYearAndMonth_FullResponseObject() throws Exception {
        final GetAvailableMonthlySettlementMonthResponse expected = new GetAvailableMonthlySettlementMonthResponse();
        expected.setAllMonth(Arrays.asList(11, 12));
        expected.setAllYears(Arrays.asList(2008, 2009, 2010));
        expected.setYear(2008);
        expected.setMonth(12);
        final GetAvailableMonthlySettlementMonthResponse actual = super.callUsecaseExpect200(
                GetAvailableMonthlySettlementMonthResponse.class, 2008, 12);
        Assertions.assertEquals(expected, actual);
    }

    @Override
    protected void callUsecaseExpect403ForThisUsecase() throws Exception {
        super.callUsecaseExpect403WithUriVariables(2008, 12);
    }

    @Override
    protected void callUsecaseEmptyDatabase() throws Exception {
        final GetAvailableMonthlySettlementMonthResponse expected = new GetAvailableMonthlySettlementMonthResponse();

        final GetAvailableMonthlySettlementMonthResponse actual = super.callUsecaseExpect200(
                GetAvailableMonthlySettlementMonthResponse.class, 2010, 10);

        Assertions.assertEquals(expected, actual);
    }
}
