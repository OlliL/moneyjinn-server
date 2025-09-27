package org.laladev.moneyjinn.server.controller.crud.capitalsource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.model.CapitalsourceTransport;

class ReadOneCapitalsourceTest extends AbstractCapitalsourceTest {

    @Override
    protected void loadMethod() {
        super.getMock().readOne(null);
    }

    @Test
    void test_HappyCase_ResponseObject() throws Exception {
        final CapitalsourceTransport expected = new CapitalsourceTransportBuilder().forCapitalsource1().build();

        final CapitalsourceTransport actual = super.callUsecaseExpect200(CapitalsourceTransport.class,
                CapitalsourceTransportBuilder.CAPITALSOURCE1_ID);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void test_notExisting_NotFoundRaised() throws Exception {
        super.callUsecaseExpect404(CapitalsourceTransportBuilder.NON_EXISTING_ID);
    }

    @Override
    protected void callUsecaseExpect403ForThisUsecase() throws Exception {
        super.callUsecaseExpect403WithUriVariables(CapitalsourceTransportBuilder.NON_EXISTING_ID);
    }

    @Override
    protected void callUsecaseEmptyDatabase() throws Exception {
        super.callUsecaseExpect404(CapitalsourceTransportBuilder.NON_EXISTING_ID);
    }

}
