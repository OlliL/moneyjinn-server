package org.laladev.moneyjinn.server.controller.crud.contractpartnermatching;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.ContractpartnerMatching;
import org.laladev.moneyjinn.model.ContractpartnerMatchingID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.server.builder.ContractpartnerMatchingTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.service.api.IContractpartnerMatchingService;

class DeleteContractpartnerMatchingTest extends AbstractContractpartnerMatchingTest {
    @Inject
    private IContractpartnerMatchingService contractpartnerMatchingService;

    @Override
    protected void loadMethod() {
        super.getMock().delete(null);
    }

    @Test
    void test_regularContractpartnerMatchingNoData_SuccessfullNoContent() throws Exception {
        final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
        final ContractpartnerMatchingID contractpartnerMatchingId = new ContractpartnerMatchingID(
                ContractpartnerMatchingTransportBuilder.CONTRACTPARTNER_MATCHING2_ID);
        ContractpartnerMatching contractpartnerMatching = this.contractpartnerMatchingService
                .getContractpartnerMatchingById(userId, contractpartnerMatchingId);
        Assertions.assertNotNull(contractpartnerMatching);

        super.callUsecaseExpect204WithUriVariables(
                ContractpartnerMatchingTransportBuilder.CONTRACTPARTNER_MATCHING2_ID);

        contractpartnerMatching = this.contractpartnerMatchingService.getContractpartnerMatchingById(userId,
                contractpartnerMatchingId);
        Assertions.assertNull(contractpartnerMatching);
    }

    @Test
    void test_nonExistingContractpartnerMatching_SuccessfullNoContent() throws Exception {
        final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
        final ContractpartnerMatchingID contractpartnerMatchingId = new ContractpartnerMatchingID(
                ContractpartnerMatchingTransportBuilder.NON_EXISTING_ID);
        ContractpartnerMatching contractpartnerMatching = this.contractpartnerMatchingService
                .getContractpartnerMatchingById(userId, contractpartnerMatchingId);
        Assertions.assertNull(contractpartnerMatching);

        super.callUsecaseExpect204WithUriVariables(ContractpartnerMatchingTransportBuilder.NON_EXISTING_ID);

        contractpartnerMatching = this.contractpartnerMatchingService.getContractpartnerMatchingById(userId,
                contractpartnerMatchingId);
        Assertions.assertNull(contractpartnerMatching);
    }

    @Test
    void test_ContractpartnerMatchingFromSameGroupButNotMe_SuccessfullNoContent() throws Exception {
        super.setUsername(UserTransportBuilder.USER3_NAME);
        super.setPassword(UserTransportBuilder.USER3_PASSWORD);
        final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
        final ContractpartnerMatchingID contractpartnerMatchingId = new ContractpartnerMatchingID(
                ContractpartnerMatchingTransportBuilder.CONTRACTPARTNER_MATCHING1_ID);
        ContractpartnerMatching contractpartnerMatching = this.contractpartnerMatchingService
                .getContractpartnerMatchingById(userId, contractpartnerMatchingId);
        Assertions.assertNotNull(contractpartnerMatching);

        super.callUsecaseExpect204WithUriVariables(
                ContractpartnerMatchingTransportBuilder.CONTRACTPARTNER_MATCHING1_ID);

        contractpartnerMatching = this.contractpartnerMatchingService
                .getContractpartnerMatchingById(userId, contractpartnerMatchingId);
        Assertions.assertNull(contractpartnerMatching);
    }

    @Test
    void test_ContractpartnerMatchingFromDifferentGroup_notSuccessfull() throws Exception {
        super.setUsername(UserTransportBuilder.ADMIN_NAME);
        super.setPassword(UserTransportBuilder.ADMIN_PASSWORD);
        final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
        final ContractpartnerMatchingID contractpartnerMatchingId = new ContractpartnerMatchingID(
                ContractpartnerMatchingTransportBuilder.CONTRACTPARTNER_MATCHING1_ID);
        ContractpartnerMatching contractpartnerMatching = this.contractpartnerMatchingService
                .getContractpartnerMatchingById(userId, contractpartnerMatchingId);
        Assertions.assertNotNull(contractpartnerMatching);

        super.callUsecaseExpect204WithUriVariables(
                ContractpartnerMatchingTransportBuilder.CONTRACTPARTNER_MATCHING1_ID);

        contractpartnerMatching = this.contractpartnerMatchingService.getContractpartnerMatchingById(userId,
                contractpartnerMatchingId);
        Assertions.assertNotNull(contractpartnerMatching);
    }

    @Override
    protected void callUsecaseExpect403ForThisUsecase() throws Exception {
        super.callUsecaseExpect403WithUriVariables(ContractpartnerMatchingTransportBuilder.NON_EXISTING_ID);
    }

    @Override
    protected void callUsecaseEmptyDatabase() throws Exception {
        super.callUsecaseExpect204WithUriVariables(ContractpartnerMatchingTransportBuilder.NON_EXISTING_ID);
    }
}