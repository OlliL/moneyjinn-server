package org.laladev.moneyjinn.server.controller.moneyflow;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.Contractpartner;
import org.laladev.moneyjinn.model.ContractpartnerID;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.moneyflow.Moneyflow;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowID;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowSplitEntry;
import org.laladev.moneyjinn.server.builder.*;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.MoneyflowControllerApi;
import org.laladev.moneyjinn.server.model.*;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.IContractpartnerService;
import org.laladev.moneyjinn.service.api.IMoneyflowService;
import org.laladev.moneyjinn.service.api.IMoneyflowSplitEntryService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class UpdateMoneyflowV2Test extends AbstractWebUserControllerTest {
    @Inject
    private IMoneyflowService moneyflowService;
    @Inject
    private IMoneyflowSplitEntryService moneyflowSplitEntryService;
    @Inject
    private ICapitalsourceService capitalsourceService;
    @Inject
    private IContractpartnerService contractpartnerService;

    @Override
    protected void loadMethod() {
        super.getMock(MoneyflowControllerApi.class).updateMoneyflowV2(null);
    }

    private void testError(final MoneyflowTransport transport, final ErrorCode... errorCodes) throws Exception {
        this.testError(transport, null, null, null, null, errorCodes);
    }

    private void testError(final MoneyflowTransport transport, final List<Long> deleteMoneyflowSplitEntryIds,
                           final List<MoneyflowSplitEntryTransport> updateMoneyflowSplitEntryTransports,
                           final List<MoneyflowSplitEntryTransport> insertMoneyflowSplitEntryTransports, final Long validationId,
                           final ErrorCode... errorCodes) throws Exception {
        final UpdateMoneyflowRequest request = new UpdateMoneyflowRequest();
        request.setMoneyflowTransport(transport);
        request.setDeleteMoneyflowSplitEntryIds(deleteMoneyflowSplitEntryIds);
        request.setUpdateMoneyflowSplitEntryTransports(updateMoneyflowSplitEntryTransports);
        request.setInsertMoneyflowSplitEntryTransports(insertMoneyflowSplitEntryTransports);
        final List<ValidationItemTransport> validationItems = new ArrayList<>();
        for (final ErrorCode errorCode : errorCodes) {
            validationItems.add(new ValidationItemTransportBuilder()
                    .withKey((validationId == null ? transport.getId() : validationId).toString())
                    .withError(errorCode.getErrorCode()).build());
        }
        final ValidationResponse expected = new ValidationResponse();
        expected.setValidationItemTransports(validationItems);
        expected.setResult(Boolean.FALSE);

        final ValidationResponse actual = super.callUsecaseExpect422(request, ValidationResponse.class);

        Assertions.assertEquals(expected.getResult(), actual.getResult());
        Assertions.assertEquals(expected.getValidationItemTransports(), actual.getValidationItemTransports());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void test_emptyComment_Error() throws Exception {
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow2().build();
        transport.setComment("");
        this.testError(transport, ErrorCode.COMMENT_IS_NOT_SET);
    }

    @Test
    void test_nullComment_Error() throws Exception {
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow2().build();
        transport.setComment(null);
        this.testError(transport, ErrorCode.COMMENT_IS_NOT_SET);
    }

    @Test
    void test_nullCapitalsource_Error() throws Exception {
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
        transport.setCapitalsourceid(null);
        this.testError(transport, ErrorCode.CAPITALSOURCE_IS_NOT_SET);
    }

    @Test
    void test_notExistingCapitalsource_Error() throws Exception {
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
        transport.setCapitalsourceid(CapitalsourceTransportBuilder.NON_EXISTING_ID);
        this.testError(transport, ErrorCode.CAPITALSOURCE_DOES_NOT_EXIST);
    }

    @Test
    void test_creditCapitalsource_Error() throws Exception {
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
        transport.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE5_ID);
        this.testError(transport, ErrorCode.CAPITALSOURCE_INVALID);
    }

    @Test
    void test_AmountToBig_Error() throws Exception {
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
        transport.setAmount(new BigDecimal(9999999));
        this.testError(transport, ErrorCode.AMOUNT_TO_BIG);
    }

    @Test
    void test_BookingdateAfterCapitalsourceValidity_ValidityAdjusted() throws Exception {
        final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
        final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
        final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE3_ID);
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
        transport.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE3_ID);
        final UpdateMoneyflowRequest request = new UpdateMoneyflowRequest();
        request.setMoneyflowTransport(transport);
        final Capitalsource capitalsourceOrig = this.capitalsourceService.getCapitalsourceById(userId, groupId,
                capitalsourceId);

        super.callUsecaseExpect200(request, UpdateMoneyflowResponse.class);

        final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
                capitalsourceId);
        Assertions.assertNotEquals(capitalsourceOrig.getValidTil(), capitalsource.getValidTil());
        Assertions.assertEquals(capitalsourceOrig.getValidFrom(), capitalsource.getValidFrom());
        Assertions.assertEquals(transport.getBookingdate(), capitalsource.getValidTil());
    }

    @Test
    void test_BookingdateBeforeCapitalsourceValidity_ValidityAdjusted() throws Exception {
        final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
        final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
        final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE4_ID);
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
        transport.setCapitalsourceid(capitalsourceId.getId());
        transport.setBookingdate(LocalDate.parse("2000-01-01"));
        final UpdateMoneyflowRequest request = new UpdateMoneyflowRequest();
        request.setMoneyflowTransport(transport);
        final Capitalsource capitalsourceOrig = this.capitalsourceService.getCapitalsourceById(userId, groupId,
                capitalsourceId);

        super.callUsecaseExpect200(request, UpdateMoneyflowResponse.class);

        final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
                capitalsourceId);
        Assertions.assertNotEquals(capitalsourceOrig.getValidFrom(), capitalsource.getValidFrom());
        Assertions.assertEquals(capitalsourceOrig.getValidTil(), capitalsource.getValidTil());
        Assertions.assertEquals(transport.getBookingdate(), capitalsource.getValidFrom());
    }

    @Test
    void test_nullContractpartner_Error() throws Exception {
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
        transport.setContractpartnerid(null);
        this.testError(transport, ErrorCode.CONTRACTPARTNER_IS_NOT_SET);
    }

    @Test
    void test_notExistingContractpartner_Error() throws Exception {
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
        transport.setContractpartnerid(ContractpartnerTransportBuilder.NON_EXISTING_ID);
        this.testError(transport, ErrorCode.CONTRACTPARTNER_DOES_NOT_EXIST);
    }

    @Test
    void test_BookingdateAfterContractpartnerValidity_ValidityAdjusted() throws Exception {
        final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
        final ContractpartnerID contractpartnerId = new ContractpartnerID(
                ContractpartnerTransportBuilder.CONTRACTPARTNER3_ID);
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
        transport.setBookingdate(LocalDate.parse("2011-01-01"));
        transport.setContractpartnerid(contractpartnerId.getId());
        final UpdateMoneyflowRequest request = new UpdateMoneyflowRequest();
        request.setMoneyflowTransport(transport);
        final Contractpartner contractpartnerOrig = this.contractpartnerService.getContractpartnerById(userId,
                contractpartnerId);

        super.callUsecaseExpect200(request, UpdateMoneyflowResponse.class);

        final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
                contractpartnerId);
        Assertions.assertNotEquals(contractpartnerOrig.getValidTil(), contractpartner.getValidTil());
        Assertions.assertEquals(contractpartnerOrig.getValidFrom(), contractpartner.getValidFrom());
        Assertions.assertEquals(transport.getBookingdate(), contractpartner.getValidTil());
    }

    @Test
    void test_BookingdateBeforeContractpartnerValidity_ValidityAdjusted() throws Exception {
        final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
        final ContractpartnerID contractpartnerId = new ContractpartnerID(
                ContractpartnerTransportBuilder.CONTRACTPARTNER4_ID);
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
        transport.setBookingdate(LocalDate.parse("2000-01-01"));
        transport.setContractpartnerid(contractpartnerId.getId());
        final UpdateMoneyflowRequest request = new UpdateMoneyflowRequest();
        request.setMoneyflowTransport(transport);
        final Contractpartner contractpartnerOrig = this.contractpartnerService.getContractpartnerById(userId,
                contractpartnerId);

        super.callUsecaseExpect200(request, UpdateMoneyflowResponse.class);

        final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
                contractpartnerId);
        Assertions.assertNotEquals(contractpartnerOrig.getValidFrom(), contractpartner.getValidFrom());
        Assertions.assertEquals(contractpartnerOrig.getValidTil(), contractpartner.getValidTil());
        Assertions.assertEquals(transport.getBookingdate(), contractpartner.getValidFrom());
    }

    @Test
    void test_nullAmount_Error() throws Exception {
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
        transport.setAmount(null);
        this.testError(transport, ErrorCode.AMOUNT_IS_ZERO);
    }

    @Test
    void test_zeroAmount_Error() throws Exception {
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
        transport.setAmount(BigDecimal.ZERO);
        this.testError(transport, ErrorCode.AMOUNT_IS_ZERO);
    }

    // make sure it 0 is compared with compareTo not with equals
    @Test
    void test_0_00Amount_Error() throws Exception {
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
        transport.setAmount(new BigDecimal("0.00000"));
        this.testError(transport, ErrorCode.AMOUNT_IS_ZERO);
    }

    @Test
    void test_nullPostingAccount_Error() throws Exception {
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow2().build();
        transport.setPostingaccountid(null);
        this.testError(transport, ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED);
    }

    @Test
    void test_nullBookingDate_Error() throws Exception {
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
        transport.setBookingdate(null);
        this.testError(transport, ErrorCode.BOOKINGDATE_IN_WRONG_FORMAT);
    }

    @Test
    void test_BookingDateBeforeGroupAssignment_Error() throws Exception {
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
        transport.setBookingdate(LocalDate.parse("1970-01-01"));
        this.testError(transport, ErrorCode.BOOKINGDATE_OUTSIDE_GROUP_ASSIGNMENT,
                ErrorCode.CAPITALSOURCE_USE_OUT_OF_VALIDITY, ErrorCode.CONTRACTPARTNER_NO_LONGER_VALID);
    }

    @Test
    void test_BookingDateAfterGroupAssignment_Error() throws Exception {
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
        transport.setBookingdate(LocalDate.parse("2600-01-01"));
        this.testError(transport, ErrorCode.BOOKINGDATE_OUTSIDE_GROUP_ASSIGNMENT);
    }

    @Test
    void test_notExistingPostingAccount_Error() throws Exception {
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
        transport.setPostingaccountid(PostingAccountTransportBuilder.NON_EXISTING_ID);
        this.testError(transport, ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED);
    }

    @Test
    void test_notExisting_NothingHappend() throws Exception {
        final UpdateMoneyflowRequest request = new UpdateMoneyflowRequest();
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
        transport.setId(MoneyflowTransportBuilder.NEXT_ID);
        request.setMoneyflowTransport(transport);
        final ErrorResponse actual = super.callUsecaseExpect400(request, ErrorResponse.class);
        Assertions.assertEquals(ErrorCode.MONEYFLOW_DOES_NOT_EXISTS.getErrorCode(), actual.getCode());
        final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
        final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.NEXT_ID);
        final Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);
        Assertions.assertNull(moneyflow);
    }

    @Test
    void test_existing_UpdateDone() throws Exception {
        final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
        final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW2_ID);
        final UpdateMoneyflowRequest request = new UpdateMoneyflowRequest();
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow2().build();
        Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);
        Assertions.assertNotNull(moneyflow);
        Assertions.assertEquals(transport.getAmount(), moneyflow.getAmount());
        Assertions.assertEquals(transport.getCapitalsourceid(), moneyflow.getCapitalsource().getId().getId());
        Assertions.assertEquals(transport.getComment(), moneyflow.getComment());
        Assertions.assertEquals(transport.getContractpartnerid(), moneyflow.getContractpartner().getId().getId());
        Assertions.assertEquals(transport.getPostingaccountid(), moneyflow.getPostingAccount().getId().getId());
        Assertions.assertEquals(Integer.valueOf("1").equals(transport.getPrivat()), moneyflow.isPrivat());
        Assertions.assertEquals(transport.getBookingdate(), moneyflow.getBookingDate());
        Assertions.assertEquals(transport.getInvoicedate(), moneyflow.getInvoiceDate());
        transport.setAmount(BigDecimal.valueOf(1020, 2));
        transport.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID);
        transport.setComment("hugo");
        transport.setContractpartnerid(ContractpartnerTransportBuilder.CONTRACTPARTNER2_ID);
        transport.setPostingaccountid(PostingAccountTransportBuilder.POSTING_ACCOUNT2_ID);
        transport.setPrivat(Integer.valueOf("1"));
        transport.setBookingdate(LocalDate.parse("2009-01-02"));
        transport.setInvoicedate(LocalDate.parse("2009-01-03"));
        request.setMoneyflowTransport(transport);

        super.callUsecaseExpect200(request, UpdateMoneyflowResponse.class);

        moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);
        Assertions.assertNotNull(moneyflow);
        Assertions.assertEquals(transport.getAmount(), moneyflow.getAmount());
        Assertions.assertEquals(transport.getCapitalsourceid(), moneyflow.getCapitalsource().getId().getId());
        Assertions.assertEquals(transport.getComment(), moneyflow.getComment());
        Assertions.assertEquals(transport.getContractpartnerid(), moneyflow.getContractpartner().getId().getId());
        Assertions.assertEquals(transport.getPostingaccountid(), moneyflow.getPostingAccount().getId().getId());
        Assertions.assertEquals(Integer.valueOf("1").equals(transport.getPrivat()), moneyflow.isPrivat());
        Assertions.assertEquals(transport.getBookingdate(), moneyflow.getBookingDate());
        Assertions.assertEquals(transport.getInvoicedate(), moneyflow.getInvoiceDate());
    }

    @Test
    void test_capitalsourceAndContractpartnerValidAtBookingDateButNotToday_UpdateDone() throws Exception {
        final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
        final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW1_ID);
        final UpdateMoneyflowRequest request = new UpdateMoneyflowRequest();
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
        Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);
        Assertions.assertNotNull(moneyflow);
        Assertions.assertEquals(transport.getAmount(), moneyflow.getAmount());
        Assertions.assertEquals(transport.getCapitalsourceid(), moneyflow.getCapitalsource().getId().getId());
        Assertions.assertEquals(transport.getComment(), moneyflow.getComment());
        Assertions.assertEquals(transport.getContractpartnerid(), moneyflow.getContractpartner().getId().getId());
        Assertions.assertEquals(transport.getPostingaccountid(), moneyflow.getPostingAccount().getId().getId());
        Assertions.assertEquals(Integer.valueOf("1").equals(transport.getPrivat()), moneyflow.isPrivat());
        Assertions.assertEquals(transport.getBookingdate(), moneyflow.getBookingDate());
        Assertions.assertEquals(transport.getInvoicedate(), moneyflow.getInvoiceDate());
        transport.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE4_ID);
        transport.setContractpartnerid(ContractpartnerTransportBuilder.CONTRACTPARTNER3_ID);
        request.setMoneyflowTransport(transport);

        super.callUsecaseExpect200(request, UpdateMoneyflowResponse.class);

        moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);
        Assertions.assertNotNull(moneyflow);
        Assertions.assertEquals(transport.getAmount(), moneyflow.getAmount());
        Assertions.assertEquals(transport.getCapitalsourceid(), moneyflow.getCapitalsource().getId().getId());
        Assertions.assertEquals(transport.getComment(), moneyflow.getComment());
        Assertions.assertEquals(transport.getContractpartnerid(), moneyflow.getContractpartner().getId().getId());
        Assertions.assertEquals(transport.getPostingaccountid(), moneyflow.getPostingAccount().getId().getId());
        Assertions.assertEquals(Integer.valueOf("1").equals(transport.getPrivat()), moneyflow.isPrivat());
        Assertions.assertEquals(transport.getBookingdate(), moneyflow.getBookingDate());
        Assertions.assertEquals(transport.getInvoicedate(), moneyflow.getInvoiceDate());
    }

    @Test
    void test_NotGroupUseableCapitalsourceUsed_Error() throws Exception {
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
        transport.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE6_ID);
        this.testError(transport, ErrorCode.CAPITALSOURCE_DOES_NOT_EXIST);
    }

    @Test
    void test_SplitEntries_DeletionMakesAmountUnbalanced_Error() throws Exception {
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
        final List<Long> deleteMoneyflowSplitEntryIds = new ArrayList<>();
        deleteMoneyflowSplitEntryIds.add(MoneyflowSplitEntryTransportBuilder.MONEYFLOW_SPLIT_ENTRY1_ID);
        this.testError(transport, deleteMoneyflowSplitEntryIds, null, null, null,
                ErrorCode.SPLIT_ENTRIES_AMOUNT_IS_NOT_EQUALS_MONEYFLOW_AMOUNT);
    }

    @Test
    void test_SplitEntries_UpdateMakesAmountUnbalanced_Error() throws Exception {
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
        final MoneyflowSplitEntryTransport mseTransport = new MoneyflowSplitEntryTransportBuilder()
                .forMoneyflowSplitEntry1().build();
        mseTransport.setAmount(BigDecimal.TEN);
        this.testError(transport, null, Arrays.asList(mseTransport), null, null,
                ErrorCode.SPLIT_ENTRIES_AMOUNT_IS_NOT_EQUALS_MONEYFLOW_AMOUNT);
    }

    @Test
    void test_SplitEntries_InsertMakesAmountUnbalanced_Error() throws Exception {
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
        final MoneyflowSplitEntryTransport mseTransport = new MoneyflowSplitEntryTransportBuilder()
                .forMoneyflowSplitEntry1().build();
        this.testError(transport, null, null, Arrays.asList(mseTransport), null,
                ErrorCode.SPLIT_ENTRIES_AMOUNT_IS_NOT_EQUALS_MONEYFLOW_AMOUNT);
    }

    @Test
    void test_SplitEntries_Update_emptyComment_Error() throws Exception {
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
        final MoneyflowSplitEntryTransport mseTransport = new MoneyflowSplitEntryTransportBuilder()
                .forMoneyflowSplitEntry2().build();
        mseTransport.setComment("");
        this.testError(transport, null, Arrays.asList(mseTransport), null, mseTransport.getId(),
                ErrorCode.COMMENT_IS_NOT_SET);
    }

    @Test
    void test_SplitEntries_Update_zeroAmount_Error() throws Exception {
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
        final MoneyflowSplitEntryTransport mseTransport = new MoneyflowSplitEntryTransportBuilder()
                .forMoneyflowSplitEntry2().build();
        mseTransport.setAmount(BigDecimal.ZERO);
        this.testError(transport, null, Arrays.asList(mseTransport), null, mseTransport.getId(),
                ErrorCode.AMOUNT_IS_ZERO);
    }

    // make sure it 0 is compared with compareTo not with equals
    @Test
    void test_SplitEntries_Update_0_00Amount_Error() throws Exception {
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
        final MoneyflowSplitEntryTransport mseTransport = new MoneyflowSplitEntryTransportBuilder()
                .forMoneyflowSplitEntry2().build();
        mseTransport.setAmount(new BigDecimal("0.00000"));
        this.testError(transport, null, Arrays.asList(mseTransport), null, mseTransport.getId(),
                ErrorCode.AMOUNT_IS_ZERO);
    }

    @Test
    void test_SplitEntries_Update_nullComment_Error() throws Exception {
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
        final MoneyflowSplitEntryTransport mseTransport = new MoneyflowSplitEntryTransportBuilder()
                .forMoneyflowSplitEntry2().build();
        mseTransport.setComment(null);
        this.testError(transport, null, Arrays.asList(mseTransport), null, mseTransport.getId(),
                ErrorCode.COMMENT_IS_NOT_SET);
    }

    @Test
    void test_SplitEntries_Update_nullAmount_Error() throws Exception {
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
        final MoneyflowSplitEntryTransport mseTransport = new MoneyflowSplitEntryTransportBuilder()
                .forMoneyflowSplitEntry2().build();
        mseTransport.setAmount(null);
        this.testError(transport, null, Arrays.asList(mseTransport), null, mseTransport.getId(),
                ErrorCode.AMOUNT_IS_ZERO);
    }

    @Test
    void test_SplitEntries_Update_nullPostingAccount_Error() throws Exception {
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
        final MoneyflowSplitEntryTransport mseTransport = new MoneyflowSplitEntryTransportBuilder()
                .forMoneyflowSplitEntry2().build();
        mseTransport.setPostingaccountid(null);
        this.testError(transport, null, Arrays.asList(mseTransport), null, mseTransport.getId(),
                ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED);
    }

    @Test
    void test_SplitEntries_Update_notExistingPostingAccount_Error() throws Exception {
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
        final MoneyflowSplitEntryTransport mseTransport = new MoneyflowSplitEntryTransportBuilder()
                .forMoneyflowSplitEntry2().build();
        mseTransport.setPostingaccountid(PostingAccountTransportBuilder.NON_EXISTING_ID);
        this.testError(transport, null, Arrays.asList(mseTransport), null, mseTransport.getId(),
                ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED);
    }

    @Test
    void test_SplitEntries_Insert_emptyComment_Error() throws Exception {
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow2().build();
        final MoneyflowSplitEntryTransport mseTransport = new MoneyflowSplitEntryTransportBuilder()
                .forNewMoneyflowSplitEntry().build();
        mseTransport.setComment("");
        this.testError(transport, null, null, Arrays.asList(mseTransport), mseTransport.getId(),
                ErrorCode.COMMENT_IS_NOT_SET);
    }

    @Test
    void test_SplitEntries_Insert_zeroAmount_Error() throws Exception {
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow2().build();
        final MoneyflowSplitEntryTransport mseTransport = new MoneyflowSplitEntryTransportBuilder()
                .forNewMoneyflowSplitEntry().build();
        mseTransport.setAmount(BigDecimal.ZERO);
        this.testError(transport, null, null, Arrays.asList(mseTransport), mseTransport.getId(),
                ErrorCode.AMOUNT_IS_ZERO);
    }

    // make sure it 0 is compared with compareTo not with equals
    @Test
    void test_SplitEntries_Insert_0_00Amount_Error() throws Exception {
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow2().build();
        final MoneyflowSplitEntryTransport mseTransport = new MoneyflowSplitEntryTransportBuilder()
                .forNewMoneyflowSplitEntry().build();
        mseTransport.setAmount(new BigDecimal("0.00000"));
        this.testError(transport, null, null, Arrays.asList(mseTransport), mseTransport.getId(),
                ErrorCode.AMOUNT_IS_ZERO);
    }

    @Test
    void test_SplitEntries_Insert_nullComment_Error() throws Exception {
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow2().build();
        final MoneyflowSplitEntryTransport mseTransport = new MoneyflowSplitEntryTransportBuilder()
                .forNewMoneyflowSplitEntry().build();
        mseTransport.setComment(null);
        this.testError(transport, null, null, Arrays.asList(mseTransport), mseTransport.getId(),
                ErrorCode.COMMENT_IS_NOT_SET);
    }

    @Test
    void test_SplitEntries_Insert_nullAmount_Error() throws Exception {
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow2().build();
        final MoneyflowSplitEntryTransport mseTransport = new MoneyflowSplitEntryTransportBuilder()
                .forNewMoneyflowSplitEntry().build();
        mseTransport.setAmount(null);
        this.testError(transport, null, null, Arrays.asList(mseTransport), mseTransport.getId(),
                ErrorCode.AMOUNT_IS_ZERO);
    }

    @Test
    void test_SplitEntries_Insert_nullPostingAccount_Error() throws Exception {
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow2().build();
        final MoneyflowSplitEntryTransport mseTransport = new MoneyflowSplitEntryTransportBuilder()
                .forNewMoneyflowSplitEntry().build();
        mseTransport.setPostingaccountid(null);
        this.testError(transport, null, null, Arrays.asList(mseTransport), mseTransport.getId(),
                ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED);
    }

    @Test
    void test_SplitEntries_Insert_notExistingPostingAccount_Error() throws Exception {
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow2().build();
        final MoneyflowSplitEntryTransport mseTransport = new MoneyflowSplitEntryTransportBuilder()
                .forNewMoneyflowSplitEntry().build();
        mseTransport.setPostingaccountid(PostingAccountTransportBuilder.NON_EXISTING_ID);
        this.testError(transport, null, null, Arrays.asList(mseTransport), mseTransport.getId(),
                ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED);
    }

    @Test
    void test_SplitEntries_DeleteUpdateInsert_ChangesDone() throws Exception {
        final UpdateMoneyflowRequest request = new UpdateMoneyflowRequest();
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
        request.setMoneyflowTransport(transport);
        request.setDeleteMoneyflowSplitEntryIds(
                Arrays.asList(MoneyflowSplitEntryTransportBuilder.MONEYFLOW_SPLIT_ENTRY1_ID));
        final MoneyflowSplitEntryTransport updateTransport = new MoneyflowSplitEntryTransportBuilder()
                .forMoneyflowSplitEntry2().build();
        updateTransport.setAmount(new BigDecimal("-0.60"));
        request.setUpdateMoneyflowSplitEntryTransports(Arrays.asList(updateTransport));
        final MoneyflowSplitEntryTransport insertTransport = new MoneyflowSplitEntryTransportBuilder()
                .forMoneyflowSplitEntry1().build();
        insertTransport.setAmount(new BigDecimal("-0.50"));
        insertTransport.setComment("inserted");
        request.setInsertMoneyflowSplitEntryTransports(Arrays.asList(insertTransport));

        final UpdateMoneyflowResponse response = super.callUsecaseExpect200(request, UpdateMoneyflowResponse.class);

        Assertions.assertEquals(response.getMoneyflowSplitEntryTransports().getFirst(), updateTransport);
        insertTransport.setId(MoneyflowSplitEntryTransportBuilder.NEXT_ID);
        Assertions.assertEquals(response.getMoneyflowSplitEntryTransports().get(1), insertTransport);
        Assertions.assertEquals(response.getMoneyflowTransport(), transport);
    }

    @Test
    void test_SplitEntries_DeleteUpdate_ChangeDone() throws Exception {
        final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
        final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW1_ID);
        final UpdateMoneyflowRequest request = new UpdateMoneyflowRequest();
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
        request.setMoneyflowTransport(transport);
        request.setDeleteMoneyflowSplitEntryIds(
                Arrays.asList(MoneyflowSplitEntryTransportBuilder.MONEYFLOW_SPLIT_ENTRY1_ID));
        final MoneyflowSplitEntryTransport updateTransport = new MoneyflowSplitEntryTransportBuilder()
                .forMoneyflowSplitEntry2().build();
        updateTransport.setAmount(new BigDecimal("-1.10"));
        request.setUpdateMoneyflowSplitEntryTransports(Arrays.asList(updateTransport));

        super.callUsecaseExpect200(request, UpdateMoneyflowResponse.class);

        final List<MoneyflowSplitEntry> moneyflowSplitEntries = this.moneyflowSplitEntryService
                .getMoneyflowSplitEntries(userId, moneyflowId);
        Assertions.assertEquals(MoneyflowSplitEntryTransportBuilder.MONEYFLOW_SPLIT_ENTRY2_ID,
                moneyflowSplitEntries.getFirst().getId().getId());
        Assertions.assertEquals(moneyflowSplitEntries.getFirst().getAmount(), new BigDecimal("-1.10"));
    }

    private void test_SplitEntries_DeleteUpdate_With_Wrong_MoneyflowId_Corrected_Create_Mse_for_Moneyflow_2()
            throws Exception {
        final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW2_ID);
        final UpdateMoneyflowRequest request = new UpdateMoneyflowRequest();
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow2().build();
        request.setMoneyflowTransport(transport);
        final MoneyflowSplitEntryTransport insertTransport1 = new MoneyflowSplitEntryTransportBuilder()
                .forMoneyflowSplitEntry1().build();
        insertTransport1.setAmount(new BigDecimal(".10"));
        insertTransport1.setComment("inserted1");
        insertTransport1.setMoneyflowid(moneyflowId.getId());
        final MoneyflowSplitEntryTransport insertTransport2 = new MoneyflowSplitEntryTransportBuilder()
                .forMoneyflowSplitEntry1().build();
        insertTransport2.setAmount(BigDecimal.TEN);
        insertTransport2.setComment("inserted2");
        insertTransport2.setMoneyflowid(moneyflowId.getId());
        request.setInsertMoneyflowSplitEntryTransports(Arrays.asList(insertTransport1, insertTransport2));

        super.callUsecaseExpect200(request, UpdateMoneyflowResponse.class);
    }

    private void test_SplitEntries_DeleteUpdate_With_Wrong_MoneyflowId_Corrected_Validate_Mse_for_Moneyflow_1() {
        final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
        final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW1_ID);
        final List<MoneyflowSplitEntry> moneyflowSplitEntriesMoneyflow1 = this.moneyflowSplitEntryService
                .getMoneyflowSplitEntries(userId, moneyflowId);
        Assertions.assertEquals(2, moneyflowSplitEntriesMoneyflow1.size());
        Assertions.assertEquals(MoneyflowSplitEntryTransportBuilder.MONEYFLOW_SPLIT_ENTRY1_ID,
                moneyflowSplitEntriesMoneyflow1.getFirst().getId().getId());
        Assertions.assertEquals(MoneyflowSplitEntryTransportBuilder.MONEYFLOW_SPLIT_ENTRY1_AMOUNT,
                moneyflowSplitEntriesMoneyflow1.getFirst().getAmount());
        Assertions.assertEquals(MoneyflowSplitEntryTransportBuilder.MONEYFLOW_SPLIT_ENTRY2_ID,
                moneyflowSplitEntriesMoneyflow1.get(1).getId().getId());
        Assertions.assertEquals(MoneyflowSplitEntryTransportBuilder.MONEYFLOW_SPLIT_ENTRY2_AMOUNT,
                moneyflowSplitEntriesMoneyflow1.get(1).getAmount());
    }

    private void test_SplitEntries_DeleteUpdate_With_Wrong_MoneyflowId_Corrected_Validate_Mse_for_Moneyflow_2() {
        final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
        final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW2_ID);
        final List<MoneyflowSplitEntry> moneyflowSplitEntries = this.moneyflowSplitEntryService
                .getMoneyflowSplitEntries(userId, moneyflowId);
        Assertions.assertEquals(MoneyflowSplitEntryTransportBuilder.NEXT_ID,
                moneyflowSplitEntries.getFirst().getId().getId());
        Assertions.assertEquals(0, moneyflowSplitEntries.getFirst().getAmount().compareTo(new BigDecimal(".10")));
        Assertions.assertEquals("inserted1", moneyflowSplitEntries.getFirst().getComment());
        Assertions.assertEquals(moneyflowSplitEntries.get(1).getId().getId(),
                Long.valueOf(Long.sum(MoneyflowSplitEntryTransportBuilder.NEXT_ID.longValue(), 1L)));
        Assertions.assertEquals(0, moneyflowSplitEntries.get(1).getAmount().compareTo(BigDecimal.TEN));
        Assertions.assertEquals("inserted2", moneyflowSplitEntries.get(1).getComment());
    }

    private void test_SplitEntries_DeleteUpdate_With_Wrong_MoneyflowId_Corrected_Do_Wrong_Stuff_For_Moneyflow_2()
            throws Exception {
        final UpdateMoneyflowRequest request = new UpdateMoneyflowRequest();
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow2().build();
        request.setMoneyflowTransport(transport);
        // This belongs to Moneyflow 1 (must remain there)
        request.setDeleteMoneyflowSplitEntryIds(
                Arrays.asList(MoneyflowSplitEntryTransportBuilder.MONEYFLOW_SPLIT_ENTRY1_ID));
        // This belongs to Moneyflow 1 (must remain untouched)
        final MoneyflowSplitEntryTransport updateTransport = new MoneyflowSplitEntryTransportBuilder()
                .forMoneyflowSplitEntry2().build();
        updateTransport.setAmount(new BigDecimal("-5.10"));
        request.setUpdateMoneyflowSplitEntryTransports(Arrays.asList(updateTransport));

        super.callUsecaseExpect200(request, UpdateMoneyflowResponse.class);
    }

    @Test
    void test_SplitEntries_DeleteUpdate_With_Wrong_MoneyflowId_Corrected() throws Exception {
        //
        // first create a new Moneyflow-Split Entry set for Moneyflow 2 and validate
        // that everything
        // went OK
        //
        this.test_SplitEntries_DeleteUpdate_With_Wrong_MoneyflowId_Corrected_Create_Mse_for_Moneyflow_2();
        this.test_SplitEntries_DeleteUpdate_With_Wrong_MoneyflowId_Corrected_Validate_Mse_for_Moneyflow_2();
        //
        // Now Try to delete stuff which does not belong to this moneyflow - but in fact
        // all sent
        // data should be silently ignored/dropped
        //
        this.test_SplitEntries_DeleteUpdate_With_Wrong_MoneyflowId_Corrected_Do_Wrong_Stuff_For_Moneyflow_2();
        //
        // Now check that neither Moneyflow 1 nor Moneyflow 2 was touched by the forged
        // data sent
        // previously
        //
        this.test_SplitEntries_DeleteUpdate_With_Wrong_MoneyflowId_Corrected_Validate_Mse_for_Moneyflow_1();
        this.test_SplitEntries_DeleteUpdate_With_Wrong_MoneyflowId_Corrected_Validate_Mse_for_Moneyflow_2();
    }

    @Test
    void test_SplitEntries_DeleteInsert_ChangesDone() throws Exception {
        final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
        final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW1_ID);
        final UpdateMoneyflowRequest request = new UpdateMoneyflowRequest();
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
        request.setMoneyflowTransport(transport);
        request.setDeleteMoneyflowSplitEntryIds(
                Arrays.asList(MoneyflowSplitEntryTransportBuilder.MONEYFLOW_SPLIT_ENTRY1_ID));
        final MoneyflowSplitEntryTransport insertTransport = new MoneyflowSplitEntryTransportBuilder()
                .forMoneyflowSplitEntry1().build();
        insertTransport.setAmount(new BigDecimal("-1.00"));
        insertTransport.setComment("inserted");
        request.setInsertMoneyflowSplitEntryTransports(Arrays.asList(insertTransport));

        super.callUsecaseExpect200(request, UpdateMoneyflowResponse.class);

        final List<MoneyflowSplitEntry> moneyflowSplitEntries = this.moneyflowSplitEntryService
                .getMoneyflowSplitEntries(userId, moneyflowId);
        Assertions.assertEquals(MoneyflowSplitEntryTransportBuilder.MONEYFLOW_SPLIT_ENTRY2_ID,
                moneyflowSplitEntries.getFirst().getId().getId());
        Assertions.assertEquals(new BigDecimal("-0.10"), moneyflowSplitEntries.getFirst().getAmount());
        Assertions.assertEquals(MoneyflowSplitEntryTransportBuilder.NEXT_ID,
                moneyflowSplitEntries.get(1).getId().getId());
        Assertions.assertEquals(new BigDecimal("-1.00"), moneyflowSplitEntries.get(1).getAmount());
        Assertions.assertEquals("inserted", moneyflowSplitEntries.get(1).getComment());
    }

    @Test
    void test_SplitEntries_Insert_ChangesDone() throws Exception {
        final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
        final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW2_ID);
        final UpdateMoneyflowRequest request = new UpdateMoneyflowRequest();
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow2().build();
        request.setMoneyflowTransport(transport);
        final MoneyflowSplitEntryTransport insertTransport = new MoneyflowSplitEntryTransportBuilder()
                .forMoneyflowSplitEntry1().build();
        insertTransport.setAmount(new BigDecimal("10.10"));
        insertTransport.setComment("inserted");
        insertTransport.setMoneyflowid(moneyflowId.getId());
        request.setInsertMoneyflowSplitEntryTransports(Arrays.asList(insertTransport));

        super.callUsecaseExpect200(request, UpdateMoneyflowResponse.class);

        final List<MoneyflowSplitEntry> moneyflowSplitEntries = this.moneyflowSplitEntryService
                .getMoneyflowSplitEntries(userId, moneyflowId);
        Assertions.assertEquals(MoneyflowSplitEntryTransportBuilder.NEXT_ID,
                moneyflowSplitEntries.getFirst().getId().getId());
        Assertions.assertEquals(new BigDecimal("10.10"), moneyflowSplitEntries.getFirst().getAmount());
        Assertions.assertEquals("inserted", moneyflowSplitEntries.getFirst().getComment());
    }

    @Test
    void test_SplitEntries_Insert_With_Wrong_MoneyflowId_GetsCorrected() throws Exception {
        final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
        final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW2_ID);
        final UpdateMoneyflowRequest request = new UpdateMoneyflowRequest();
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow2().build();
        request.setMoneyflowTransport(transport);
        // Create a MSE Transport but for Moneyflow 1 - while we are going to make an
        // Update to
        // Moneyflow 2
        final MoneyflowSplitEntryTransport insertTransport = new MoneyflowSplitEntryTransportBuilder()
                .forMoneyflowSplitEntry1().build();
        insertTransport.setAmount(new BigDecimal("10.10"));
        insertTransport.setComment("inserted");
        request.setInsertMoneyflowSplitEntryTransports(Arrays.asList(insertTransport));

        super.callUsecaseExpect200(request, UpdateMoneyflowResponse.class);

        final List<MoneyflowSplitEntry> moneyflowSplitEntries = this.moneyflowSplitEntryService
                .getMoneyflowSplitEntries(userId, moneyflowId);
        Assertions.assertEquals(MoneyflowSplitEntryTransportBuilder.NEXT_ID,
                moneyflowSplitEntries.getFirst().getId().getId());
        Assertions.assertEquals(new BigDecimal("10.10"), moneyflowSplitEntries.getFirst().getAmount());
        Assertions.assertEquals("inserted", moneyflowSplitEntries.getFirst().getComment());
    }

    @Test
    void test_SplitEntries_Delete_ChangesDone() throws Exception {
        final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
        final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW1_ID);
        final UpdateMoneyflowRequest request = new UpdateMoneyflowRequest();
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
        request.setMoneyflowTransport(transport);
        request.setDeleteMoneyflowSplitEntryIds(
                Arrays.asList(MoneyflowSplitEntryTransportBuilder.MONEYFLOW_SPLIT_ENTRY1_ID,
                        MoneyflowSplitEntryTransportBuilder.MONEYFLOW_SPLIT_ENTRY2_ID));

        super.callUsecaseExpect200(request, UpdateMoneyflowResponse.class);

        final List<MoneyflowSplitEntry> moneyflowSplitEntries = this.moneyflowSplitEntryService
                .getMoneyflowSplitEntries(userId, moneyflowId);
        Assertions.assertTrue(moneyflowSplitEntries.isEmpty());
    }

    @Test
    void test_Splitentries_CommentAndPostingAccountForMainNotSpecified_TakenFromFirstSplitEntryBooking()
            throws Exception {
        final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
        final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW1_ID);
        final UpdateMoneyflowRequest request = new UpdateMoneyflowRequest();
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
        transport.setComment(null);
        transport.setPostingaccountid(null);
        request.setMoneyflowTransport(transport);

        super.callUsecaseExpect200(request, UpdateMoneyflowResponse.class);

        final Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);
        final List<MoneyflowSplitEntry> moneyflowSplitEntries = this.moneyflowSplitEntryService
                .getMoneyflowSplitEntries(userId, moneyflowId);
        Assertions.assertNotNull(moneyflow);
        Assertions.assertEquals(moneyflowSplitEntries.getFirst().getComment(), moneyflow.getComment());
        Assertions.assertEquals(moneyflowSplitEntries.getFirst().getPostingAccount().getId(),
                moneyflow.getPostingAccount().getId());
    }

    @Override
    protected void callUsecaseExpect403ForThisUsecase() throws Exception {
        super.callUsecaseExpect403(new UpdateMoneyflowRequest());
    }

    @Override
    protected void callUsecaseEmptyDatabase() throws Exception {
        final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
        final UpdateMoneyflowRequest request = new UpdateMoneyflowRequest();
        request.setMoneyflowTransport(transport);
        final ErrorResponse actual = super.callUsecaseExpect400(request, ErrorResponse.class);
        Assertions.assertEquals(ErrorCode.MONEYFLOW_DOES_NOT_EXISTS.getErrorCode(), actual.getCode());
    }
}