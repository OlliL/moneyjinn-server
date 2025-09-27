package org.laladev.moneyjinn.server.controller.predefmoneyflow;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.PreDefMoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.PreDefMoneyflowControllerApi;
import org.laladev.moneyjinn.server.model.PreDefMoneyflowTransport;
import org.laladev.moneyjinn.server.model.ShowPreDefMoneyflowListResponse;

import java.util.ArrayList;
import java.util.List;

class ShowPreDefMoneyflowListTest extends AbstractWebUserControllerTest {

    @Override
    protected void loadMethod() {
        super.getMock(PreDefMoneyflowControllerApi.class).showPreDefMoneyflowList();
    }

    private ShowPreDefMoneyflowListResponse getCompleteResponse() {
        final ShowPreDefMoneyflowListResponse expected = new ShowPreDefMoneyflowListResponse();
        final List<PreDefMoneyflowTransport> preDefMoneyflowTransports = new ArrayList<>();
        preDefMoneyflowTransports.add(new PreDefMoneyflowTransportBuilder().forPreDefMoneyflow1().build());
        preDefMoneyflowTransports.add(new PreDefMoneyflowTransportBuilder().forPreDefMoneyflow3().build());
        expected.setPreDefMoneyflowTransports(preDefMoneyflowTransports);
        return expected;
    }

    @Test
    void test_default_FullResponseObject() throws Exception {
        final ShowPreDefMoneyflowListResponse expected = this.getCompleteResponse();

        final ShowPreDefMoneyflowListResponse actual = super.callUsecaseExpect200(
                ShowPreDefMoneyflowListResponse.class);

        Assertions.assertEquals(expected, actual);
    }

    @Override
    protected void callUsecaseExpect403ForThisUsecase() throws Exception {
        super.callUsecaseExpect403();
    }

    @Override
    protected void callUsecaseEmptyDatabase() throws Exception {
        final ShowPreDefMoneyflowListResponse expected = new ShowPreDefMoneyflowListResponse();

        final ShowPreDefMoneyflowListResponse actual = super.callUsecaseExpect200(
                ShowPreDefMoneyflowListResponse.class);

        Assertions.assertEquals(expected, actual);
    }

}
