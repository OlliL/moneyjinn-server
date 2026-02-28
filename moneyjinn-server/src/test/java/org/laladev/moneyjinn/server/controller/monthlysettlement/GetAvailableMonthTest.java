package org.laladev.moneyjinn.server.controller.monthlysettlement;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.MonthlySettlementControllerApi;
import org.laladev.moneyjinn.server.model.GetAvailableMonthlySettlementMonthResponse;

import java.util.Arrays;

class GetAvailableMonthTest extends AbstractWebUserControllerTest {

    @Override
    protected void loadMethod() {
        super.getMock(MonthlySettlementControllerApi.class).getAvailableMonth();
    }

    private GetAvailableMonthlySettlementMonthResponse getDefaultResponse() {
        final GetAvailableMonthlySettlementMonthResponse expected = new GetAvailableMonthlySettlementMonthResponse();
        expected.setAllMonth(Arrays.asList(1, 2, 3, 4));
        expected.setAllYears(Arrays.asList(2008, 2009, 2010));
        expected.setYear(2010);
        expected.setMonth(4);
        return expected;
    }

    @Test
    void test_default_FullResponseObject() throws Exception {
        final GetAvailableMonthlySettlementMonthResponse expected = this.getDefaultResponse();
        final GetAvailableMonthlySettlementMonthResponse actual = super.callUsecaseExpect200(
                GetAvailableMonthlySettlementMonthResponse.class);
        Assertions.assertEquals(expected, actual);
    }

    @Override
    protected void callUsecaseExpect403ForThisUsecase() throws Exception {
        super.callUsecaseExpect403();
    }

    @Override
    protected void callUsecaseEmptyDatabase() throws Exception {
        final GetAvailableMonthlySettlementMonthResponse expected = new GetAvailableMonthlySettlementMonthResponse();

        final GetAvailableMonthlySettlementMonthResponse actual = super.callUsecaseExpect200(
                GetAvailableMonthlySettlementMonthResponse.class);

        Assertions.assertEquals(expected, actual);
    }
}
