package org.laladev.moneyjinn.server.controller.crud.contractpartneraccount;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.ContractpartnerAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.model.ContractpartnerAccountTransport;

import java.util.ArrayList;
import java.util.List;

class ReadAllContractpartnerAccountTest extends AbstractContractpartnerAccountTest {
    @Override
    protected void loadMethod() {
        super.getMock().readAll(null);
    }

    private List<ContractpartnerAccountTransport> getCompleteResponse() {
        final List<ContractpartnerAccountTransport> contractpartnerAccountTransports = new ArrayList<>();
        contractpartnerAccountTransports
                .add(new ContractpartnerAccountTransportBuilder().forContractpartnerAccount1().build());
        contractpartnerAccountTransports
                .add(new ContractpartnerAccountTransportBuilder().forContractpartnerAccount2().build());
        return contractpartnerAccountTransports;
    }

    @Test
    void test_default_FullResponseObject() throws Exception {
        final List<ContractpartnerAccountTransport> expected = this.getCompleteResponse();
        final ContractpartnerAccountTransport[] actual = super.callUsecaseExpect200(
                ContractpartnerAccountTransport[].class, ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);

        Assertions.assertArrayEquals(expected.toArray(), actual);
    }

    @Test
    void test_contractpartnerWithNoAccounts_responseWithNoAccounts() throws Exception {
        final ContractpartnerAccountTransport[] expected = {};
        final ContractpartnerAccountTransport[] actual = super.callUsecaseExpect200(
                ContractpartnerAccountTransport[].class, ContractpartnerTransportBuilder.CONTRACTPARTNER2_ID);

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    void test_notExistingContractpartner_emptyResponseObject() throws Exception {
        final ContractpartnerAccountTransport[] expected = {};
        final ContractpartnerAccountTransport[] actual = super.callUsecaseExpect200(
                ContractpartnerAccountTransport[].class, ContractpartnerTransportBuilder.NON_EXISTING_ID);

        Assertions.assertArrayEquals(expected, actual);
    }

    @Override
    protected void callUsecaseExpect403ForThisUsecase() throws Exception {
        super.callUsecaseExpect403WithUriVariables(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);
    }

    @Override
    protected void callUsecaseEmptyDatabase() throws Exception {
        final ContractpartnerAccountTransport[] expected = {};
        final ContractpartnerAccountTransport[] actual = super.callUsecaseExpect200(
                ContractpartnerAccountTransport[].class, ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);

        Assertions.assertArrayEquals(expected, actual);
    }
}
