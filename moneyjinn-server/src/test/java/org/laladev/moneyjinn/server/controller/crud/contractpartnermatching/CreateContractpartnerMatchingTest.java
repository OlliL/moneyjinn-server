package org.laladev.moneyjinn.server.controller.crud.contractpartnermatching;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.ContractpartnerMatching;
import org.laladev.moneyjinn.model.ContractpartnerMatchingID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.server.builder.ContractpartnerMatchingTransportBuilder;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.model.ContractpartnerMatchingTransport;
import org.laladev.moneyjinn.server.model.ValidationItemTransport;
import org.laladev.moneyjinn.server.model.ValidationResponse;
import org.laladev.moneyjinn.service.api.IContractpartnerMatchingService;

import java.util.ArrayList;
import java.util.List;

class CreateContractpartnerMatchingTest extends AbstractContractpartnerMatchingTest {
    @Inject
    private IContractpartnerMatchingService contractpartnerMatchingService;

    @Override
    protected void loadMethod() {
        super.getMock().create(null, null);
    }

    private void testError(final ContractpartnerMatchingTransport transport, final ErrorCode errorCode)
            throws Exception {
        final List<ValidationItemTransport> validationItems = new ArrayList<>();
        validationItems
                .add(new ValidationItemTransportBuilder().withKey(null).withError(errorCode.getErrorCode()).build());
        final ValidationResponse expected = new ValidationResponse();
        expected.setValidationItemTransports(validationItems);
        expected.setResult(Boolean.FALSE);

        final ValidationResponse actual = super.callUsecaseExpect422(transport, ValidationResponse.class);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void test_emptyContractpartner_Error() throws Exception {
        final ContractpartnerMatchingTransport transport = new ContractpartnerMatchingTransportBuilder()
                .forNewContractpartnerMatching().withContractpartnerid(null).build();
        this.testError(transport, ErrorCode.CONTRACTPARTNER_IS_NOT_SET);
    }

    @Test
    void test_nonExistingContractpartner_Error() throws Exception {
        final ContractpartnerMatchingTransport transport = new ContractpartnerMatchingTransportBuilder()
                .forNewContractpartnerMatching().withContractpartnerid(ContractpartnerTransportBuilder.NON_EXISTING_ID)
                .build();
        this.testError(transport, ErrorCode.CONTRACTPARTNER_DOES_NOT_EXIST);
    }

    @Test
    void test_ContractpartnerMatchingAlreadyExisting_Error() throws Exception {
        final ContractpartnerMatchingTransport transport = new ContractpartnerMatchingTransportBuilder()
                .forNewContractpartnerMatching().withMatchingText(
                        ContractpartnerMatchingTransportBuilder.CONTRACTPARTNER_MATCHING1_MATCHING_TEXT)
                .build();
        this.testError(transport, ErrorCode.CONTRACTPARTNER_MAPPING_DUPLICATE);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = " ")
    void test_ContractpartnerMatchingEmptyMatchingText_Error(final String text) throws Exception {
        final ContractpartnerMatchingTransport transport = new ContractpartnerMatchingTransportBuilder()
                .forNewContractpartnerMatching().withMatchingText(text)
                .build();
        this.testError(transport, ErrorCode.COMMENT_IS_NOT_SET);
    }

    @Test
    void test_standardRequest_DefaultReturn() throws Exception {
        final ContractpartnerMatchingTransport transport = new ContractpartnerMatchingTransportBuilder()
                .forNewContractpartnerMatching().build();

        super.callUsecaseExpect204(transport);

        final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
        final ContractpartnerMatchingID contractpartnerMatchingId = new ContractpartnerMatchingID(
                ContractpartnerMatchingTransportBuilder.NEXT_ID);
        final ContractpartnerMatching contractpartnerMatching = this.contractpartnerMatchingService
                .getContractpartnerMatchingById(userId, contractpartnerMatchingId);
        Assertions.assertEquals(ContractpartnerMatchingTransportBuilder.NEXT_ID,
                contractpartnerMatching.getId().getId());
    }

    @Test
    void test_standardRequest_MinimalReturn() throws Exception {
        final ContractpartnerMatchingTransport transport = new ContractpartnerMatchingTransportBuilder()
                .forNewContractpartnerMatching().build();

        super.callUsecaseExpect204Minimal(transport);

        final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
        final ContractpartnerMatchingID contractpartnerMatchingId = new ContractpartnerMatchingID(
                ContractpartnerMatchingTransportBuilder.NEXT_ID);
        final ContractpartnerMatching contractpartnerMatching = this.contractpartnerMatchingService
                .getContractpartnerMatchingById(userId, contractpartnerMatchingId);
        Assertions.assertEquals(ContractpartnerMatchingTransportBuilder.NEXT_ID,
                contractpartnerMatching.getId().getId());
    }

    @Test
    void test_standardRequest_RepresentationReturn() throws Exception {
        final ContractpartnerMatchingTransport transport = new ContractpartnerMatchingTransportBuilder()
                .forNewContractpartnerMatching().build();

        final ContractpartnerMatchingTransport actualTransport = super.callUsecaseExpect200Representation(transport,
                ContractpartnerMatchingTransport.class);

        Assertions.assertEquals(ContractpartnerMatchingTransportBuilder.NEXT_ID, actualTransport.getId());
        final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
        final ContractpartnerMatchingID contractpartnerMatchingId = new ContractpartnerMatchingID(
                ContractpartnerMatchingTransportBuilder.NEXT_ID);
        final ContractpartnerMatching contractpartnerMatching = this.contractpartnerMatchingService
                .getContractpartnerMatchingById(userId, contractpartnerMatchingId);
        Assertions.assertEquals(ContractpartnerMatchingTransportBuilder.NEXT_ID,
                contractpartnerMatching.getId().getId());
    }

    @Override
    protected void callUsecaseExpect403ForThisUsecase() throws Exception {
        super.callUsecaseExpect403(new ContractpartnerMatchingTransport());
    }

    @Override
    protected void callUsecaseEmptyDatabase() throws Exception {
        final ContractpartnerMatchingTransport transport = new ContractpartnerMatchingTransportBuilder()
                .forNewContractpartnerMatching().build();

        this.testError(transport, ErrorCode.CONTRACTPARTNER_DOES_NOT_EXIST);
    }
}