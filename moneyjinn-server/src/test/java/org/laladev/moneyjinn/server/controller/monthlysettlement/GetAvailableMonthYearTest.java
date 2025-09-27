package org.laladev.moneyjinn.server.controller.monthlysettlement;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.MonthlySettlementControllerApi;
import org.laladev.moneyjinn.server.model.GetAvailableMonthlySettlementMonthResponse;

import java.util.Arrays;

class GetAvailableMonthYearTest extends AbstractWebUserControllerTest {

    @Override
    protected void loadMethod() {
        super.getMock(MonthlySettlementControllerApi.class).getAvailableMonthYear(null);
    }

    private GetAvailableMonthlySettlementMonthResponse getDefaultResponse() {
        final GetAvailableMonthlySettlementMonthResponse expected = new GetAvailableMonthlySettlementMonthResponse();
        expected.setAllMonth(Arrays.asList(1, 2, 3, 4));
        expected.setAllYears(Arrays.asList(2008, 2009, 2010));
        expected.setYear(2010);
        return expected;
    }

    @Test
    void test_withYear_FullResponseObject() throws Exception {
        final GetAvailableMonthlySettlementMonthResponse expected = this.getDefaultResponse();
        final GetAvailableMonthlySettlementMonthResponse actual = super.callUsecaseExpect200(
                GetAvailableMonthlySettlementMonthResponse.class, 2010);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void test_withInvalidYear_FullResponseObject() throws Exception {
        final GetAvailableMonthlySettlementMonthResponse expected = this.getDefaultResponse();
        final GetAvailableMonthlySettlementMonthResponse actual = super.callUsecaseExpect200(
                GetAvailableMonthlySettlementMonthResponse.class, 1972);
        Assertions.assertEquals(expected, actual);
    }

    @Override
    protected void callUsecaseExpect403ForThisUsecase() throws Exception {
        super.callUsecaseExpect403WithUriVariables(2008);
    }

    @Override
    protected void callUsecaseEmptyDatabase() throws Exception {
        final GetAvailableMonthlySettlementMonthResponse expected = new GetAvailableMonthlySettlementMonthResponse();

        final GetAvailableMonthlySettlementMonthResponse actual = super.callUsecaseExpect200(
                GetAvailableMonthlySettlementMonthResponse.class, 2010);

        Assertions.assertEquals(expected, actual);
    }
}
