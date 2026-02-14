package org.laladev.moneyjinn.server.controller.moneyflow;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.MoneyflowSplitEntryTransportBuilder;
import org.laladev.moneyjinn.server.builder.MoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.MoneyflowControllerApi;
import org.laladev.moneyjinn.server.model.MoneyflowSplitEntryTransport;
import org.laladev.moneyjinn.server.model.ShowEditMoneyflowResponse;

import java.util.ArrayList;
import java.util.List;

class ShowEditMoneyflowTest extends AbstractWebUserControllerTest {

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
    void test_MoneyflowOwnedBySomeoneElseAndPrivate_emptyResponseObject() throws Exception {
        super.setUsername(UserTransportBuilder.USER3_NAME);
        super.setPassword(UserTransportBuilder.USER3_PASSWORD);

        final ShowEditMoneyflowResponse expected = new ShowEditMoneyflowResponse();

        final ShowEditMoneyflowResponse actual = super.callUsecaseExpect200(ShowEditMoneyflowResponse.class,
                MoneyflowTransportBuilder.MONEYFLOW3_ID);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void test_MoneyflowOwnedBySomeoneElseAndNotPrivate_emptyResponseObject() throws Exception {
        super.setUsername(UserTransportBuilder.USER3_NAME);
        super.setPassword(UserTransportBuilder.USER3_PASSWORD);

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

    @Override
    protected void callUsecaseExpect403ForThisUsecase() throws Exception {
        super.callUsecaseExpect403WithUriVariables(MoneyflowTransportBuilder.MONEYFLOW1_ID);
    }

    @Override
    protected void callUsecaseEmptyDatabase() throws Exception {
        final ShowEditMoneyflowResponse expected = new ShowEditMoneyflowResponse();

        final ShowEditMoneyflowResponse actual = super.callUsecaseExpect200(ShowEditMoneyflowResponse.class,
                MoneyflowTransportBuilder.MONEYFLOW1_ID);

        Assertions.assertEquals(expected, actual);
    }
}