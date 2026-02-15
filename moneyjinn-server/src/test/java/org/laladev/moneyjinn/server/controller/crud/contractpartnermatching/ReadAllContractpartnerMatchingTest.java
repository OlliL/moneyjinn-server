package org.laladev.moneyjinn.server.controller.crud.contractpartnermatching;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.ContractpartnerMatchingTransportBuilder;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.model.ContractpartnerMatchingTransport;

import java.util.ArrayList;
import java.util.List;

class ReadAllContractpartnerMatchingTest extends AbstractContractpartnerMatchingTest {
    @Override
    protected void loadMethod() {
        super.getMock().readAll();
    }

    private List<ContractpartnerMatchingTransport> getCompleteResponse() {
        final List<ContractpartnerMatchingTransport> contractpartnerMatchingTransports = new ArrayList<>();
        contractpartnerMatchingTransports
                .add(new ContractpartnerMatchingTransportBuilder().forContractpartnerMatching1().build());
        contractpartnerMatchingTransports
                .add(new ContractpartnerMatchingTransportBuilder().forContractpartnerMatching2().build());
        contractpartnerMatchingTransports
                .add(new ContractpartnerMatchingTransportBuilder().forContractpartnerMatching3().build());
        return contractpartnerMatchingTransports;
    }

    @Test
    void test_default_FullResponseObject() throws Exception {
        final List<ContractpartnerMatchingTransport> expected = this.getCompleteResponse();
        final ContractpartnerMatchingTransport[] actual = super.callUsecaseExpect200(
                ContractpartnerMatchingTransport[].class);

        Assertions.assertArrayEquals(expected.toArray(), actual);
    }

    @Test
    void test_userWithNoData_EmptyResponseObject() throws Exception {
        super.setUsername(UserTransportBuilder.ADMIN_NAME);
        super.setPassword(UserTransportBuilder.ADMIN_PASSWORD);
        final ContractpartnerMatchingTransport[] actual = super.callUsecaseExpect200(
                ContractpartnerMatchingTransport[].class);

        Assertions.assertEquals(0, actual.length);
    }

    @Override
    protected void callUsecaseExpect403ForThisUsecase() throws Exception {
        super.callUsecaseExpect403WithUriVariables(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);
    }

    @Override
    protected void callUsecaseEmptyDatabase() throws Exception {
        final ContractpartnerMatchingTransport[] expected = {};
        final ContractpartnerMatchingTransport[] actual = super.callUsecaseExpect200(
                ContractpartnerMatchingTransport[].class, ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);

        Assertions.assertArrayEquals(expected, actual);
    }
}
