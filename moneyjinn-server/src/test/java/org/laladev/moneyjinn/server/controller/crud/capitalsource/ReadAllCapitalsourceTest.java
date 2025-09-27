package org.laladev.moneyjinn.server.controller.crud.capitalsource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.model.CapitalsourceTransport;

import java.util.ArrayList;
import java.util.List;

class ReadAllCapitalsourceTest extends AbstractCapitalsourceTest {

    @Override
    protected void loadMethod() {
        super.getMock().readAll();
    }

    private List<CapitalsourceTransport> getCompleteResponse() {
        final List<CapitalsourceTransport> capitalsourceTransports = new ArrayList<>();
        capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource1().build());
        capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource2().build());
        capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource3().build());
        capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource4().build());
        capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource5().build());
        capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource6().build());
        return capitalsourceTransports;
    }

    @Test
    void test_FullResponseObject() throws Exception {
        final List<CapitalsourceTransport> expected = this.getCompleteResponse();

        final CapitalsourceTransport[] actual = super.callUsecaseExpect200(CapitalsourceTransport[].class);

        Assertions.assertArrayEquals(expected.toArray(), actual);

    }

    @Override
    protected void callUsecaseExpect403ForThisUsecase() throws Exception {
        super.callUsecaseExpect403();
    }

    @Override
    protected void callUsecaseEmptyDatabase() throws Exception {
        final CapitalsourceTransport[] expected = {};
        final CapitalsourceTransport[] actual = super.callUsecaseExpect200(CapitalsourceTransport[].class);

        Assertions.assertArrayEquals(expected, actual);
    }
}
