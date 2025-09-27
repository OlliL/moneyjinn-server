package org.laladev.moneyjinn.server.controller.crud.etfpreliminarylumpsum;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.etf.EtfPreliminaryLumpSumID;
import org.laladev.moneyjinn.server.builder.EtfPreliminaryLumpSumTransportBuilder;
import org.laladev.moneyjinn.server.builder.EtfTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.model.ErrorResponse;
import org.laladev.moneyjinn.server.model.EtfPreliminaryLumpSumTransport;
import org.laladev.moneyjinn.server.model.ValidationItemTransport;
import org.laladev.moneyjinn.server.model.ValidationResponse;
import org.laladev.moneyjinn.service.api.IEtfService;

import java.util.ArrayList;
import java.util.List;

class CreateEtfPreliminaryLumpSumTest extends AbstractEtfPreliminaryLumpSumTest {
    @Inject
    private IEtfService etfService;

    @Override
    protected void loadMethod() {
        super.getMock().create(null, null);
    }

    private void testError(final EtfPreliminaryLumpSumTransport transport, final ErrorCode errorCode) throws Exception {
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
    void test_standardRequest_Successfull_MinimalReturn() throws Exception {
        final var transport = new EtfPreliminaryLumpSumTransportBuilder().forNewYear().build();

        final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
        final var id = new EtfPreliminaryLumpSumID(EtfPreliminaryLumpSumTransportBuilder.NEXT_ID);

        var etfPreliminaryLumpSum = this.etfService.getEtfPreliminaryLumpSum(userId, id);
        Assertions.assertNull(etfPreliminaryLumpSum);

        super.callUsecaseExpect204Minimal(transport);

        etfPreliminaryLumpSum = this.etfService.getEtfPreliminaryLumpSum(userId, id);
        Assertions.assertNotNull(etfPreliminaryLumpSum);
    }

    @Test
    void test_standardRequest_Successfull_RepresentationReturn() throws Exception {
        final var transport = new EtfPreliminaryLumpSumTransportBuilder().forNewYear().build();

        final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
        final var id = new EtfPreliminaryLumpSumID(EtfPreliminaryLumpSumTransportBuilder.NEXT_ID);

        var etfPreliminaryLumpSum = this.etfService.getEtfPreliminaryLumpSum(userId, id);
        Assertions.assertNull(etfPreliminaryLumpSum);

        final EtfPreliminaryLumpSumTransport actualTransport = super.callUsecaseExpect200Representation(transport,
                EtfPreliminaryLumpSumTransport.class);

        Assertions.assertEquals(transport, actualTransport);

        etfPreliminaryLumpSum = this.etfService.getEtfPreliminaryLumpSum(userId, id);
        Assertions.assertNotNull(etfPreliminaryLumpSum);
    }

    @Test
    void test_standardRequest_Successfull_DefaultReturn() throws Exception {
        final var transport = new EtfPreliminaryLumpSumTransportBuilder().forNewYear().build();

        final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
        final var id = new EtfPreliminaryLumpSumID(EtfPreliminaryLumpSumTransportBuilder.NEXT_ID);

        var etfPreliminaryLumpSum = this.etfService.getEtfPreliminaryLumpSum(userId, id);
        Assertions.assertNull(etfPreliminaryLumpSum);

        super.callUsecaseExpect204(transport);

        etfPreliminaryLumpSum = this.etfService.getEtfPreliminaryLumpSum(userId, id);
        Assertions.assertNotNull(etfPreliminaryLumpSum);
    }

    @Test
    void test_EtfPreliminaryLumpSumNonExistingEtfId_Error() throws Exception {
        final var transport = new EtfPreliminaryLumpSumTransportBuilder().forNewYear().build();
        transport.setEtfId(EtfTransportBuilder.NON_EXISTING_ID);
        this.testError(transport, ErrorCode.NO_ETF_SPECIFIED);
    }

    @Test
    void test_EtfPreliminaryLumpSumNonEmptyEtfId_Error() throws Exception {
        final var transport = new EtfPreliminaryLumpSumTransportBuilder().forNewYear().build();
        transport.setEtfId(null);
        this.testError(transport, ErrorCode.NO_ETF_SPECIFIED);
    }

    @Test
    void test_EtfPreliminaryLumpSumAlreadyExisting_Error() throws Exception {
        final var transport = new EtfPreliminaryLumpSumTransportBuilder().for2009().build();
        final ErrorResponse actual = super.callUsecaseExpect400(transport, ErrorResponse.class);
        Assertions.assertEquals(ErrorCode.ETF_PRELIMINARY_LUMP_SUM_ALREADY_EXISTS.getErrorCode(), actual.getCode());
    }

    @Test
    void test_EtfPreliminaryLumpSumType1ButPiecePrice_Error() throws Exception {
        final var transport = new EtfPreliminaryLumpSumTransportBuilder().for2010().build();
        transport.setYear(2011);
        transport.setType(1);
        this.testError(transport, ErrorCode.ETF_PRELIMINARY_LUMP_SUM_PIECE_PRICE_MUST_BE_NULL);
    }

    @Test
    void test_EtfPreliminaryLumpSumType2ButMonthlyPrices_Error() throws Exception {
        final var transport = new EtfPreliminaryLumpSumTransportBuilder().for2009().build();
        transport.setYear(2011);
        transport.setType(2);
        this.testError(transport, ErrorCode.ETF_PRELIMINARY_LUMP_SUM_MONTHLY_PRICES_MUST_BE_NULL);
    }

    @Test
    void test_etfFromOtherGroup_Error() throws Exception {
        final var transport = new EtfPreliminaryLumpSumTransportBuilder().forNewYear().build();
        transport.setEtfId(EtfTransportBuilder.ETF_ID_2);
        this.testError(transport, ErrorCode.NO_ETF_SPECIFIED);
    }

    @Override
    protected void callUsecaseExpect403ForThisUsecase() throws Exception {
        super.callUsecaseExpect403(new EtfPreliminaryLumpSumTransport());
    }

    @Override
    protected void callUsecaseEmptyDatabase() throws Exception {

        final var transport = new EtfPreliminaryLumpSumTransportBuilder().forNewYear().build();

        super.callUsecaseExpect422(transport, ValidationResponse.class);
    }
}