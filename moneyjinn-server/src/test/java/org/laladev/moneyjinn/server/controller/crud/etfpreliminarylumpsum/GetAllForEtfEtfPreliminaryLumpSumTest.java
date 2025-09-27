package org.laladev.moneyjinn.server.controller.crud.etfpreliminarylumpsum;

import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.EtfPreliminaryLumpSumTransportBuilder;
import org.laladev.moneyjinn.server.builder.EtfTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.model.EtfPreliminaryLumpSumTransport;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GetAllForEtfEtfPreliminaryLumpSumTest extends AbstractEtfPreliminaryLumpSumTest {

    @Override
    protected void loadMethod() {
        super.getMock().getAllForEtf(null);
    }

    @Test
    void test_HappyCase_ResponseObject() throws Exception {
        final EtfPreliminaryLumpSumTransport[] expected = {
                new EtfPreliminaryLumpSumTransportBuilder().for2009().build(),
                new EtfPreliminaryLumpSumTransportBuilder().for2010().build()};
        final EtfPreliminaryLumpSumTransport[] actual = super.callUsecaseExpect200(
                EtfPreliminaryLumpSumTransport[].class, EtfTransportBuilder.ETF_ID_1);

        assertNotNull(actual);
        assertArrayEquals(expected, actual);

    }

    @Test
    void test_notExisting_EmptyResponse() throws Exception {
        super.callUsecaseExpect404(EtfTransportBuilder.NON_EXISTING_ID);
    }

    @Test
    void test_etfFromOtherGroup_EmptyResponse() throws Exception {
        super.setUsername(UserTransportBuilder.ADMIN_NAME);
        super.setPassword(UserTransportBuilder.ADMIN_PASSWORD);
        super.callUsecaseExpect404(EtfTransportBuilder.ETF_ID_1);
    }

    @Override
    protected void callUsecaseExpect403ForThisUsecase() throws Exception {
        super.callUsecaseExpect403WithUriVariables(EtfTransportBuilder.ETF_ID_1);
    }

    @Override
    protected void callUsecaseEmptyDatabase() throws Exception {
        super.callUsecaseExpect404(EtfTransportBuilder.ETF_ID_1);
    }

}
