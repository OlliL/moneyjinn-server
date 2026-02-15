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
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.model.ContractpartnerMatchingTransport;
import org.laladev.moneyjinn.server.model.ValidationItemTransport;
import org.laladev.moneyjinn.server.model.ValidationResponse;
import org.laladev.moneyjinn.service.api.IContractpartnerMatchingService;

import java.util.ArrayList;
import java.util.List;

class UpdateContractpartnerMatchingTest extends
        AbstractContractpartnerMatchingTest {
    @Inject
    private IContractpartnerMatchingService contractpartnerMatchingService;

    @Override
    protected void loadMethod() {
        super.getMock().update(null, null);
    }

    private void testError(final ContractpartnerMatchingTransport transport,
                           final ErrorCode... errorCodes) throws Exception {
        final List<ValidationItemTransport> validationItems = new ArrayList<>();
        for (final ErrorCode errorCode : errorCodes) {
            final ValidationItemTransportBuilder builder = new ValidationItemTransportBuilder()
                    .withKey(transport.getId().toString()).withError(errorCode.getErrorCode());
            validationItems.add(builder.build());
        }
        final ValidationResponse expected = new ValidationResponse();
        expected.setValidationItemTransports(validationItems);
        expected.setResult(Boolean.FALSE);
        final ValidationResponse actual = super.callUsecaseExpect422(transport, ValidationResponse.class);
        Assertions.assertEquals(expected, actual);
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
    void test_ContractpartnerMatchingAlreadyExisting_Error() throws Exception {
        final ContractpartnerMatchingTransport transport1 = new ContractpartnerMatchingTransportBuilder()
                .forContractpartnerMatching1().build();
        final ContractpartnerMatchingTransport transport2 = new ContractpartnerMatchingTransportBuilder()
                .forContractpartnerMatching2().build();
        transport2.setMatchingText(transport1.getMatchingText());
        this.testError(transport2, ErrorCode.CONTRACTPARTNER_MAPPING_DUPLICATE);
    }

    @Test
    void test_standardRequest_DefaultReturn() throws Exception {
        final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
        final ContractpartnerMatchingID contractpartnerMatchingId = new ContractpartnerMatchingID(
                ContractpartnerMatchingTransportBuilder.CONTRACTPARTNER_MATCHING1_ID);
        final ContractpartnerMatchingTransport transport = new ContractpartnerMatchingTransportBuilder()
                .forContractpartnerMatching1().build();
        transport.setMatchingText("1");

        super.callUsecaseExpect204(transport);

        final ContractpartnerMatching contractpartnerMatching = this.contractpartnerMatchingService
                .getContractpartnerMatchingById(userId, contractpartnerMatchingId);
        Assertions.assertEquals(ContractpartnerMatchingTransportBuilder.CONTRACTPARTNER_MATCHING1_ID,
                contractpartnerMatching.getId().getId());
        Assertions.assertEquals("1", contractpartnerMatching.getMatchingText());
    }

    @Test
    void test_standardRequest_MinimalReturn() throws Exception {
        final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
        final ContractpartnerMatchingID contractpartnerMatchingId = new ContractpartnerMatchingID(
                ContractpartnerMatchingTransportBuilder.CONTRACTPARTNER_MATCHING1_ID);
        final ContractpartnerMatchingTransport transport = new ContractpartnerMatchingTransportBuilder()
                .forContractpartnerMatching1().build();
        transport.setMatchingText("1");

        super.callUsecaseExpect204Minimal(transport);

        final ContractpartnerMatching contractpartnerMatching = this.contractpartnerMatchingService
                .getContractpartnerMatchingById(userId, contractpartnerMatchingId);
        Assertions.assertEquals(ContractpartnerMatchingTransportBuilder.CONTRACTPARTNER_MATCHING1_ID,
                contractpartnerMatching.getId().getId());
        Assertions.assertEquals("1", contractpartnerMatching.getMatchingText());
    }

    @Test
    void test_standardRequest_RepresentationReturn() throws Exception {
        final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
        final ContractpartnerMatchingID contractpartnerMatchingId = new ContractpartnerMatchingID(
                ContractpartnerMatchingTransportBuilder.CONTRACTPARTNER_MATCHING1_ID);
        final ContractpartnerMatchingTransport transport = new ContractpartnerMatchingTransportBuilder()
                .forContractpartnerMatching1().build();
        transport.setMatchingText("1");

        final ContractpartnerMatchingTransport actualTransport = super.callUsecaseExpect200Representation(transport,
                ContractpartnerMatchingTransport.class);

        Assertions.assertEquals(ContractpartnerMatchingTransportBuilder.CONTRACTPARTNER_MATCHING1_ID,
                actualTransport.getId());

        final ContractpartnerMatching contractpartnerMatching = this.contractpartnerMatchingService
                .getContractpartnerMatchingById(userId, contractpartnerMatchingId);
        Assertions.assertEquals(ContractpartnerMatchingTransportBuilder.CONTRACTPARTNER_MATCHING1_ID,
                contractpartnerMatching.getId().getId());
        Assertions.assertEquals("1", contractpartnerMatching.getMatchingText());
    }

    @Test
    void test_Bic8Digits_fillesUpTo11Digits() throws Exception {
        final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
        final ContractpartnerMatchingID contractpartnerMatchingId = new ContractpartnerMatchingID(
                ContractpartnerMatchingTransportBuilder.CONTRACTPARTNER_MATCHING1_ID);
        final ContractpartnerMatchingTransport transport = new ContractpartnerMatchingTransportBuilder()
                .forContractpartnerMatching1().build();
        transport.setMatchingText("1");

        super.callUsecaseExpect204(transport);

        final ContractpartnerMatching contractpartnerMatching = this.contractpartnerMatchingService
                .getContractpartnerMatchingById(userId, contractpartnerMatchingId);
        Assertions.assertEquals(ContractpartnerMatchingTransportBuilder.CONTRACTPARTNER_MATCHING1_ID,
                contractpartnerMatching.getId().getId());
        Assertions.assertEquals("1", contractpartnerMatching.getMatchingText());
    }

    @Test
    void test_editContractpartnerMatchingOwnedBySomeoneElse_notSuccessfull() throws Exception {
        super.setUsername(UserTransportBuilder.ADMIN_NAME);
        super.setPassword(UserTransportBuilder.ADMIN_PASSWORD);
        final ContractpartnerMatchingTransport transport = new ContractpartnerMatchingTransportBuilder()
                .forContractpartnerMatching1().build();
        transport.setMatchingText("1");
        this.testError(transport, ErrorCode.CONTRACTPARTNER_DOES_NOT_EXIST);
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