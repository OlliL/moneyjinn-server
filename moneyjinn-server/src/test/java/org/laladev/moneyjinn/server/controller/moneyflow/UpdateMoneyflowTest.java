package org.laladev.moneyjinn.server.controller.moneyflow;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import org.laladev.moneyjinn.core.rest.model.moneyflow.UpdateMoneyflowRequest;
import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowSplitEntryTransport;
import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.model.Contractpartner;
import org.laladev.moneyjinn.model.ContractpartnerID;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.moneyflow.Moneyflow;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowID;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowSplitEntry;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.builder.DateUtil;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.MoneyflowSplitEntryTransportBuilder;
import org.laladev.moneyjinn.server.builder.MoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.PostingAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.IContractpartnerService;
import org.laladev.moneyjinn.service.api.IMoneyflowService;
import org.laladev.moneyjinn.service.api.IMoneyflowSplitEntryService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class UpdateMoneyflowTest extends AbstractControllerTest {

	@Inject
	private IMoneyflowService moneyflowService;
	@Inject
	private IMoneyflowSplitEntryService moneyflowSplitEntryService;
	@Inject
	private ICapitalsourceService capitalsourceService;
	@Inject
	private IContractpartnerService contractpartnerService;

	@Inject
	IAccessRelationService accessRelationService;

	private final HttpMethod method = HttpMethod.PUT;
	private String userName;
	private String userPassword;

	@Before
	public void setUp() {
		this.userName = UserTransportBuilder.USER1_NAME;
		this.userPassword = UserTransportBuilder.USER1_PASSWORD;
	}

	@Override
	protected String getUsername() {
		return this.userName;
	}

	@Override
	protected String getPassword() {
		return this.userPassword;
	}

	@Override
	protected String getUsecase() {
		return super.getUsecaseFromTestClassName(this.getClass());
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
					.withKey((validationId == null ? transport.getId() : validationId).intValue())
					.withError(errorCode.getErrorCode()).build());
		}

		final ValidationResponse expected = new ValidationResponse();
		expected.setValidationItemTransports(validationItems);
		expected.setResult(Boolean.FALSE);

		final ValidationResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				ValidationResponse.class);

		Assert.assertEquals(expected.getErrorResponse(), actual.getErrorResponse());
		Assert.assertEquals(expected.getResult(), actual.getResult());
		Assert.assertEquals(expected.getValidationItemTransports(), actual.getValidationItemTransports());
		Assert.assertEquals(expected, actual);

	}

	@Test
	public void test_emptyComment_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow2().build();
		transport.setComment("");

		this.testError(transport, ErrorCode.COMMENT_IS_NOT_SET);
	}

	@Test
	public void test_nullComment_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow2().build();
		transport.setComment(null);

		this.testError(transport, ErrorCode.COMMENT_IS_NOT_SET);
	}

	@Test
	public void test_nullCapitalsource_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		transport.setCapitalsourceid(null);

		this.testError(transport, ErrorCode.CAPITALSOURCE_IS_NOT_SET);
	}

	@Test
	public void test_notExistingCapitalsource_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		transport.setCapitalsourceid(CapitalsourceTransportBuilder.NON_EXISTING_ID);

		this.testError(transport, ErrorCode.CAPITALSOURCE_DOES_NOT_EXIST);
	}

	@Test
	public void test_creditCapitalsource_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		transport.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE5_ID);

		this.testError(transport, ErrorCode.CAPITALSOURCE_INVALID);
	}

	@Test
	public void test_BookingdateAfterCapitalsourceValidity_ValidityAdjusted() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceID = new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE3_ID);

		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE3_ID);

		final UpdateMoneyflowRequest request = new UpdateMoneyflowRequest();
		request.setMoneyflowTransport(transport);

		final Capitalsource capitalsourceOrig = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceID);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceID);
		Assert.assertNotEquals(capitalsourceOrig.getValidTil(), capitalsource.getValidTil());
		Assert.assertEquals(capitalsourceOrig.getValidFrom(), capitalsource.getValidFrom());
		Assert.assertEquals(transport.getBookingdate().toLocalDate(), capitalsource.getValidTil());
	}

	@Test
	public void test_BookingdateBeforeCapitalsourceValidity_ValidityAdjusted() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceID = new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE4_ID);

		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setCapitalsourceid(capitalsourceID.getId());
		transport.setBookingdate(DateUtil.getGMTDate("2000-01-01"));

		final UpdateMoneyflowRequest request = new UpdateMoneyflowRequest();
		request.setMoneyflowTransport(transport);

		final Capitalsource capitalsourceOrig = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceID);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceID);
		Assert.assertNotEquals(capitalsourceOrig.getValidFrom(), capitalsource.getValidFrom());
		Assert.assertEquals(capitalsourceOrig.getValidTil(), capitalsource.getValidTil());
		Assert.assertEquals(transport.getBookingdate().toLocalDate(), capitalsource.getValidFrom());
	}

	@Test
	public void test_nullContractpartner_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		transport.setContractpartnerid(null);

		this.testError(transport, ErrorCode.CONTRACTPARTNER_IS_NOT_SET);
	}

	@Test
	public void test_notExistingContractpartner_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		transport.setContractpartnerid(ContractpartnerTransportBuilder.NON_EXISTING_ID);

		this.testError(transport, ErrorCode.CONTRACTPARTNER_DOES_NOT_EXIST);
	}

	@Test
	public void test_BookingdateAfterContractpartnerValidity_ValidityAdjusted() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
		final ContractpartnerID contractpartnerID = new ContractpartnerID(
				ContractpartnerTransportBuilder.CONTRACTPARTNER3_ID);

		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setBookingdate(DateUtil.getGMTDate("2011-01-01"));
		transport.setContractpartnerid(contractpartnerID.getId());

		final UpdateMoneyflowRequest request = new UpdateMoneyflowRequest();
		request.setMoneyflowTransport(transport);

		final Contractpartner contractpartnerOrig = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerID);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerID);
		Assert.assertNotEquals(contractpartnerOrig.getValidTil(), contractpartner.getValidTil());
		Assert.assertEquals(contractpartnerOrig.getValidFrom(), contractpartner.getValidFrom());
		Assert.assertEquals(transport.getBookingdate().toLocalDate(), contractpartner.getValidTil());
	}

	@Test
	public void test_BookingdateBeforeContractpartnerValidity_ValidityAdjusted() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
		final ContractpartnerID contractpartnerID = new ContractpartnerID(
				ContractpartnerTransportBuilder.CONTRACTPARTNER4_ID);

		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setBookingdate(DateUtil.getGMTDate("2000-01-01"));
		transport.setContractpartnerid(contractpartnerID.getId());

		final UpdateMoneyflowRequest request = new UpdateMoneyflowRequest();
		request.setMoneyflowTransport(transport);

		final Contractpartner contractpartnerOrig = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerID);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerID);
		Assert.assertNotEquals(contractpartnerOrig.getValidFrom(), contractpartner.getValidFrom());
		Assert.assertEquals(contractpartnerOrig.getValidTil(), contractpartner.getValidTil());
		Assert.assertEquals(transport.getBookingdate().toLocalDate(), contractpartner.getValidFrom());
	}

	@Test
	public void test_nullAmount_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		transport.setAmount(null);

		this.testError(transport, ErrorCode.AMOUNT_IS_ZERO);
	}

	@Test
	public void test_zeroAmount_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		transport.setAmount(BigDecimal.ZERO);

		this.testError(transport, ErrorCode.AMOUNT_IS_ZERO);
	}

	// make sure it 0 is compared with compareTo not with equals
	@Test
	public void test_0_00Amount_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		transport.setAmount(new BigDecimal("0.00000"));

		this.testError(transport, ErrorCode.AMOUNT_IS_ZERO);
	}

	@Test
	public void test_nullPostingAccount_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow2().build();
		transport.setPostingaccountid(null);

		this.testError(transport, ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED);
	}

	@Test
	public void test_nullBookingDate_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		transport.setBookingdate(null);

		this.testError(transport, ErrorCode.BOOKINGDATE_IN_WRONG_FORMAT);
	}

	@Test
	public void test_BookingDateBeforeGroupAssignment_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		transport.setBookingdate(DateUtil.getGMTDate("1970-01-01"));

		this.testError(transport, ErrorCode.BOOKINGDATE_OUTSIDE_GROUP_ASSIGNMENT,
				ErrorCode.CAPITALSOURCE_USE_OUT_OF_VALIDITY, ErrorCode.CONTRACTPARTNER_NO_LONGER_VALID);
	}

	@Test
	public void test_BookingDateAfterGroupAssignment_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		transport.setBookingdate(DateUtil.getGMTDate("2600-01-01"));

		this.testError(transport, ErrorCode.BOOKINGDATE_OUTSIDE_GROUP_ASSIGNMENT);
	}

	@Test
	public void test_notExistingPostingAccount_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		transport.setPostingaccountid(PostingAccountTransportBuilder.NON_EXISTING_ID);

		this.testError(transport, ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED);
	}

	@Test
	public void test_notExisting_NothingHappend() throws Exception {
		final UpdateMoneyflowRequest request = new UpdateMoneyflowRequest();

		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		transport.setId(MoneyflowTransportBuilder.NEXT_ID);

		request.setMoneyflowTransport(transport);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.NEXT_ID);
		final Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);

		Assert.assertNull(moneyflow);
	}

	@Test
	public void test_existing_UpdateDone() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW2_ID);

		final UpdateMoneyflowRequest request = new UpdateMoneyflowRequest();

		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow2().build();

		Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);

		Assert.assertNotNull(moneyflow);
		Assert.assertEquals(transport.getAmount(), moneyflow.getAmount());
		Assert.assertEquals(transport.getCapitalsourceid(), moneyflow.getCapitalsource().getId().getId());
		Assert.assertEquals(transport.getComment(), moneyflow.getComment());
		Assert.assertEquals(transport.getContractpartnerid(), moneyflow.getContractpartner().getId().getId());
		Assert.assertEquals(transport.getPostingaccountid(), moneyflow.getPostingAccount().getId().getId());
		Assert.assertEquals(Short.valueOf("1").equals(transport.getPrivat()), moneyflow.isPrivat());
		Assert.assertEquals(transport.getBookingdate().toLocalDate(), moneyflow.getBookingDate());
		Assert.assertEquals(transport.getInvoicedate().toLocalDate(), moneyflow.getInvoiceDate());

		transport.setAmount(BigDecimal.valueOf(1020, 2));
		transport.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID);
		transport.setComment("hugo");
		transport.setContractpartnerid(ContractpartnerTransportBuilder.CONTRACTPARTNER2_ID);
		transport.setPostingaccountid(PostingAccountTransportBuilder.POSTING_ACCOUNT2_ID);
		transport.setPrivat(Short.valueOf("1"));
		transport.setBookingdate(DateUtil.getGMTDate("2009-01-02"));
		transport.setInvoicedate(DateUtil.getGMTDate("2009-01-03"));

		request.setMoneyflowTransport(transport);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);

		Assert.assertNotNull(moneyflow);
		Assert.assertEquals(transport.getAmount(), moneyflow.getAmount());
		Assert.assertEquals(transport.getCapitalsourceid(), moneyflow.getCapitalsource().getId().getId());
		Assert.assertEquals(transport.getComment(), moneyflow.getComment());
		Assert.assertEquals(transport.getContractpartnerid(), moneyflow.getContractpartner().getId().getId());
		Assert.assertEquals(transport.getPostingaccountid(), moneyflow.getPostingAccount().getId().getId());
		Assert.assertEquals(Short.valueOf("1").equals(transport.getPrivat()), moneyflow.isPrivat());
		Assert.assertEquals(transport.getBookingdate().toLocalDate(), moneyflow.getBookingDate());
		Assert.assertEquals(transport.getInvoicedate().toLocalDate(), moneyflow.getInvoiceDate());
	}

	@Test
	public void test_capitalsourceAndContractpartnerValidAtBookingDateButNotToday_UpdateDone() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW1_ID);

		final UpdateMoneyflowRequest request = new UpdateMoneyflowRequest();

		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();

		Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);

		Assert.assertNotNull(moneyflow);
		Assert.assertEquals(transport.getAmount(), moneyflow.getAmount());
		Assert.assertEquals(transport.getCapitalsourceid(), moneyflow.getCapitalsource().getId().getId());
		Assert.assertEquals(transport.getComment(), moneyflow.getComment());
		Assert.assertEquals(transport.getContractpartnerid(), moneyflow.getContractpartner().getId().getId());
		Assert.assertEquals(transport.getPostingaccountid(), moneyflow.getPostingAccount().getId().getId());
		Assert.assertEquals(Short.valueOf("1").equals(transport.getPrivat()), moneyflow.isPrivat());
		Assert.assertEquals(transport.getBookingdate().toLocalDate(), moneyflow.getBookingDate());
		Assert.assertEquals(transport.getInvoicedate().toLocalDate(), moneyflow.getInvoiceDate());

		transport.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE4_ID);
		transport.setContractpartnerid(ContractpartnerTransportBuilder.CONTRACTPARTNER3_ID);

		request.setMoneyflowTransport(transport);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);

		Assert.assertNotNull(moneyflow);
		Assert.assertEquals(transport.getAmount(), moneyflow.getAmount());
		Assert.assertEquals(transport.getCapitalsourceid(), moneyflow.getCapitalsource().getId().getId());
		Assert.assertEquals(transport.getComment(), moneyflow.getComment());
		Assert.assertEquals(transport.getContractpartnerid(), moneyflow.getContractpartner().getId().getId());
		Assert.assertEquals(transport.getPostingaccountid(), moneyflow.getPostingAccount().getId().getId());
		Assert.assertEquals(Short.valueOf("1").equals(transport.getPrivat()), moneyflow.isPrivat());
		Assert.assertEquals(transport.getBookingdate().toLocalDate(), moneyflow.getBookingDate());
		Assert.assertEquals(transport.getInvoicedate().toLocalDate(), moneyflow.getInvoiceDate());
	}

	@Test
	public void test_NotGroupUseableCapitalsourceUsed_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();

		transport.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE6_ID);

		this.testError(transport, ErrorCode.CAPITALSOURCE_DOES_NOT_EXIST);
	}

	@Test
	public void test_SplitEntries_DeletionMakesAmountUnbalanced_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		final List<Long> deleteMoneyflowSplitEntryIds = new ArrayList<>();
		deleteMoneyflowSplitEntryIds.add(MoneyflowSplitEntryTransportBuilder.MONEYFLOW_SPLIT_ENTRY1_ID);

		this.testError(transport, deleteMoneyflowSplitEntryIds, null, null, null,
				ErrorCode.SPLIT_ENTRIES_AMOUNT_IS_NOT_EQUALS_MONEYFLOW_AMOUNT);
	}

	@Test
	public void test_SplitEntries_UpdateMakesAmountUnbalanced_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();

		final MoneyflowSplitEntryTransport mseTransport = new MoneyflowSplitEntryTransportBuilder()
				.forMoneyflowSplitEntry1().build();
		mseTransport.setAmount(BigDecimal.TEN);

		this.testError(transport, null, Arrays.asList(mseTransport), null, null,
				ErrorCode.SPLIT_ENTRIES_AMOUNT_IS_NOT_EQUALS_MONEYFLOW_AMOUNT);
	}

	@Test
	public void test_SplitEntries_InsertMakesAmountUnbalanced_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();

		final MoneyflowSplitEntryTransport mseTransport = new MoneyflowSplitEntryTransportBuilder()
				.forMoneyflowSplitEntry1().build();

		this.testError(transport, null, null, Arrays.asList(mseTransport), null,
				ErrorCode.SPLIT_ENTRIES_AMOUNT_IS_NOT_EQUALS_MONEYFLOW_AMOUNT);
	}

	@Test
	public void test_SplitEntries_Update_emptyComment_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		final MoneyflowSplitEntryTransport mseTransport = new MoneyflowSplitEntryTransportBuilder()
				.forMoneyflowSplitEntry2().build();
		mseTransport.setComment("");

		this.testError(transport, null, Arrays.asList(mseTransport), null, mseTransport.getId(),
				ErrorCode.COMMENT_IS_NOT_SET);
	}

	@Test
	public void test_SplitEntries_Update_zeroAmount_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		final MoneyflowSplitEntryTransport mseTransport = new MoneyflowSplitEntryTransportBuilder()
				.forMoneyflowSplitEntry2().build();
		mseTransport.setAmount(BigDecimal.ZERO);

		this.testError(transport, null, Arrays.asList(mseTransport), null, mseTransport.getId(),
				ErrorCode.AMOUNT_IS_ZERO);
	}

	// make sure it 0 is compared with compareTo not with equals
	@Test
	public void test_SplitEntries_Update_0_00Amount_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		final MoneyflowSplitEntryTransport mseTransport = new MoneyflowSplitEntryTransportBuilder()
				.forMoneyflowSplitEntry2().build();
		mseTransport.setAmount(new BigDecimal("0.00000"));

		this.testError(transport, null, Arrays.asList(mseTransport), null, mseTransport.getId(),
				ErrorCode.AMOUNT_IS_ZERO);
	}

	@Test
	public void test_SplitEntries_Update_nullComment_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		final MoneyflowSplitEntryTransport mseTransport = new MoneyflowSplitEntryTransportBuilder()
				.forMoneyflowSplitEntry2().build();
		mseTransport.setComment(null);

		this.testError(transport, null, Arrays.asList(mseTransport), null, mseTransport.getId(),
				ErrorCode.COMMENT_IS_NOT_SET);
	}

	@Test
	public void test_SplitEntries_Update_nullAmount_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		final MoneyflowSplitEntryTransport mseTransport = new MoneyflowSplitEntryTransportBuilder()
				.forMoneyflowSplitEntry2().build();
		mseTransport.setAmount(null);

		this.testError(transport, null, Arrays.asList(mseTransport), null, mseTransport.getId(),
				ErrorCode.AMOUNT_IS_ZERO);
	}

	@Test
	public void test_SplitEntries_Update_nullPostingAccount_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		final MoneyflowSplitEntryTransport mseTransport = new MoneyflowSplitEntryTransportBuilder()
				.forMoneyflowSplitEntry2().build();
		mseTransport.setPostingaccountid(null);

		this.testError(transport, null, Arrays.asList(mseTransport), null, mseTransport.getId(),
				ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED);
	}

	@Test
	public void test_SplitEntries_Update_notExistingPostingAccount_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		final MoneyflowSplitEntryTransport mseTransport = new MoneyflowSplitEntryTransportBuilder()
				.forMoneyflowSplitEntry2().build();
		mseTransport.setPostingaccountid(PostingAccountTransportBuilder.NON_EXISTING_ID);

		this.testError(transport, null, Arrays.asList(mseTransport), null, mseTransport.getId(),
				ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED);
	}

	@Test
	public void test_SplitEntries_Insert_emptyComment_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow2().build();
		final MoneyflowSplitEntryTransport mseTransport = new MoneyflowSplitEntryTransportBuilder()
				.forNewMoneyflowSplitEntry().build();
		mseTransport.setComment("");

		this.testError(transport, null, null, Arrays.asList(mseTransport), mseTransport.getId(),
				ErrorCode.COMMENT_IS_NOT_SET);
	}

	@Test
	public void test_SplitEntries_Insert_zeroAmount_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow2().build();
		final MoneyflowSplitEntryTransport mseTransport = new MoneyflowSplitEntryTransportBuilder()
				.forNewMoneyflowSplitEntry().build();
		mseTransport.setAmount(BigDecimal.ZERO);

		this.testError(transport, null, null, Arrays.asList(mseTransport), mseTransport.getId(),
				ErrorCode.AMOUNT_IS_ZERO);
	}

	// make sure it 0 is compared with compareTo not with equals
	@Test
	public void test_SplitEntries_Insert_0_00Amount_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow2().build();
		final MoneyflowSplitEntryTransport mseTransport = new MoneyflowSplitEntryTransportBuilder()
				.forNewMoneyflowSplitEntry().build();
		mseTransport.setAmount(new BigDecimal("0.00000"));

		this.testError(transport, null, null, Arrays.asList(mseTransport), mseTransport.getId(),
				ErrorCode.AMOUNT_IS_ZERO);
	}

	@Test
	public void test_SplitEntries_Insert_nullComment_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow2().build();
		final MoneyflowSplitEntryTransport mseTransport = new MoneyflowSplitEntryTransportBuilder()
				.forNewMoneyflowSplitEntry().build();
		mseTransport.setComment(null);

		this.testError(transport, null, null, Arrays.asList(mseTransport), mseTransport.getId(),
				ErrorCode.COMMENT_IS_NOT_SET);
	}

	@Test
	public void test_SplitEntries_Insert_nullAmount_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow2().build();
		final MoneyflowSplitEntryTransport mseTransport = new MoneyflowSplitEntryTransportBuilder()
				.forNewMoneyflowSplitEntry().build();
		mseTransport.setAmount(null);

		this.testError(transport, null, null, Arrays.asList(mseTransport), mseTransport.getId(),
				ErrorCode.AMOUNT_IS_ZERO);
	}

	@Test
	public void test_SplitEntries_Insert_nullPostingAccount_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow2().build();
		final MoneyflowSplitEntryTransport mseTransport = new MoneyflowSplitEntryTransportBuilder()
				.forNewMoneyflowSplitEntry().build();
		mseTransport.setPostingaccountid(null);

		this.testError(transport, null, null, Arrays.asList(mseTransport), mseTransport.getId(),
				ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED);
	}

	@Test
	public void test_SplitEntries_Insert_notExistingPostingAccount_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow2().build();
		final MoneyflowSplitEntryTransport mseTransport = new MoneyflowSplitEntryTransportBuilder()
				.forNewMoneyflowSplitEntry().build();
		mseTransport.setPostingaccountid(PostingAccountTransportBuilder.NON_EXISTING_ID);

		this.testError(transport, null, null, Arrays.asList(mseTransport), mseTransport.getId(),
				ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED);
	}

	@Test
	public void test_SplitEntries_DeleteUpdateInsert_ChangesDone() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW1_ID);

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

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final List<MoneyflowSplitEntry> moneyflowSplitEntries = this.moneyflowSplitEntryService
				.getMoneyflowSplitEntries(userId, moneyflowId);
		Assert.assertEquals(moneyflowSplitEntries.get(0).getId().getId(),
				MoneyflowSplitEntryTransportBuilder.MONEYFLOW_SPLIT_ENTRY2_ID);
		Assert.assertEquals(moneyflowSplitEntries.get(0).getAmount(), new BigDecimal("-0.60"));
		Assert.assertEquals(moneyflowSplitEntries.get(1).getId().getId(), MoneyflowSplitEntryTransportBuilder.NEXT_ID);
		Assert.assertEquals(moneyflowSplitEntries.get(1).getAmount(), new BigDecimal("-0.50"));
		Assert.assertEquals(moneyflowSplitEntries.get(1).getComment(), "inserted");
	}

	@Test
	public void test_SplitEntries_DeleteUpdate_ChangeDone() throws Exception {
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

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final List<MoneyflowSplitEntry> moneyflowSplitEntries = this.moneyflowSplitEntryService
				.getMoneyflowSplitEntries(userId, moneyflowId);
		Assert.assertEquals(moneyflowSplitEntries.get(0).getId().getId(),
				MoneyflowSplitEntryTransportBuilder.MONEYFLOW_SPLIT_ENTRY2_ID);
		Assert.assertEquals(moneyflowSplitEntries.get(0).getAmount(), new BigDecimal("-1.10"));
	}

	private void test_SplitEntries_DeleteUpdate_With_Wrong_MoneyflowId_Corrected_Create_MSE_for_Moneyflow_2()
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

		super.callUsecaseWithContent("", this.method, request, true, Object.class);
	}

	private void test_SplitEntries_DeleteUpdate_With_Wrong_MoneyflowId_Corrected_Validate_MSE_for_Moneyflow_1()
			throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW1_ID);
		final List<MoneyflowSplitEntry> moneyflowSplitEntriesMoneyflow1 = this.moneyflowSplitEntryService
				.getMoneyflowSplitEntries(userId, moneyflowId);
		Assert.assertEquals(moneyflowSplitEntriesMoneyflow1.size(), 2);
		Assert.assertEquals(moneyflowSplitEntriesMoneyflow1.get(0).getId().getId(),
				MoneyflowSplitEntryTransportBuilder.MONEYFLOW_SPLIT_ENTRY1_ID);
		Assert.assertEquals(moneyflowSplitEntriesMoneyflow1.get(0).getAmount(),
				MoneyflowSplitEntryTransportBuilder.MONEYFLOW_SPLIT_ENTRY1_AMOUNT);
		Assert.assertEquals(moneyflowSplitEntriesMoneyflow1.get(1).getId().getId(),
				MoneyflowSplitEntryTransportBuilder.MONEYFLOW_SPLIT_ENTRY2_ID);
		Assert.assertEquals(moneyflowSplitEntriesMoneyflow1.get(1).getAmount(),
				MoneyflowSplitEntryTransportBuilder.MONEYFLOW_SPLIT_ENTRY2_AMOUNT);
	}

	private void test_SplitEntries_DeleteUpdate_With_Wrong_MoneyflowId_Corrected_Validate_MSE_for_Moneyflow_2()
			throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW2_ID);
		final List<MoneyflowSplitEntry> moneyflowSplitEntries = this.moneyflowSplitEntryService
				.getMoneyflowSplitEntries(userId, moneyflowId);
		Assert.assertEquals(moneyflowSplitEntries.get(0).getId().getId(), MoneyflowSplitEntryTransportBuilder.NEXT_ID);
		Assert.assertEquals(moneyflowSplitEntries.get(0).getAmount().compareTo(new BigDecimal(".10")), 0);
		Assert.assertEquals(moneyflowSplitEntries.get(0).getComment(), "inserted1");
		Assert.assertEquals(moneyflowSplitEntries.get(1).getId().getId(),
				Long.valueOf(Long.sum(MoneyflowSplitEntryTransportBuilder.NEXT_ID.longValue(), 1L)));
		Assert.assertEquals(moneyflowSplitEntries.get(1).getAmount().compareTo(BigDecimal.TEN), 0);
		Assert.assertEquals(moneyflowSplitEntries.get(1).getComment(), "inserted2");
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

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

	}

	@Test
	public void test_SplitEntries_DeleteUpdate_With_Wrong_MoneyflowId_Corrected() throws Exception {
		//
		// first create a new Moneyflow-Split Entry set for Moneyflow 2 and validate that everything
		// went OK
		//
		this.test_SplitEntries_DeleteUpdate_With_Wrong_MoneyflowId_Corrected_Create_MSE_for_Moneyflow_2();
		this.test_SplitEntries_DeleteUpdate_With_Wrong_MoneyflowId_Corrected_Validate_MSE_for_Moneyflow_2();

		//
		// Now Try to delete stuff which does not belong to this moneyflow - but in fact all sent
		// data should be silently ignored/dropped
		//
		this.test_SplitEntries_DeleteUpdate_With_Wrong_MoneyflowId_Corrected_Do_Wrong_Stuff_For_Moneyflow_2();

		//
		// Now check that neither Moneyflow 1 nor Moneyflow 2 was touched by the forged data sent
		// previously
		//
		this.test_SplitEntries_DeleteUpdate_With_Wrong_MoneyflowId_Corrected_Validate_MSE_for_Moneyflow_1();
		this.test_SplitEntries_DeleteUpdate_With_Wrong_MoneyflowId_Corrected_Validate_MSE_for_Moneyflow_2();
	}

	@Test
	public void test_SplitEntries_DeleteInsert_ChangesDone() throws Exception {
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

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final List<MoneyflowSplitEntry> moneyflowSplitEntries = this.moneyflowSplitEntryService
				.getMoneyflowSplitEntries(userId, moneyflowId);
		Assert.assertEquals(moneyflowSplitEntries.get(0).getId().getId(),
				MoneyflowSplitEntryTransportBuilder.MONEYFLOW_SPLIT_ENTRY2_ID);
		Assert.assertEquals(moneyflowSplitEntries.get(0).getAmount(), new BigDecimal("-0.10"));
		Assert.assertEquals(moneyflowSplitEntries.get(1).getId().getId(), MoneyflowSplitEntryTransportBuilder.NEXT_ID);
		Assert.assertEquals(moneyflowSplitEntries.get(1).getAmount(), new BigDecimal("-1.00"));
		Assert.assertEquals(moneyflowSplitEntries.get(1).getComment(), "inserted");
	}

	@Test
	public void test_SplitEntries_Insert_ChangesDone() throws Exception {
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

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final List<MoneyflowSplitEntry> moneyflowSplitEntries = this.moneyflowSplitEntryService
				.getMoneyflowSplitEntries(userId, moneyflowId);
		Assert.assertEquals(moneyflowSplitEntries.get(0).getId().getId(), MoneyflowSplitEntryTransportBuilder.NEXT_ID);
		Assert.assertEquals(moneyflowSplitEntries.get(0).getAmount(), new BigDecimal("10.10"));
		Assert.assertEquals(moneyflowSplitEntries.get(0).getComment(), "inserted");
	}

	@Test
	public void test_SplitEntries_Insert_With_Wrong_MoneyflowId_GetsCorrected() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW2_ID);

		final UpdateMoneyflowRequest request = new UpdateMoneyflowRequest();

		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow2().build();
		request.setMoneyflowTransport(transport);

		// Create a MSE Transport but for Moneyflow 1 - while we are going to make an Update to
		// Moneyflow 2
		final MoneyflowSplitEntryTransport insertTransport = new MoneyflowSplitEntryTransportBuilder()
				.forMoneyflowSplitEntry1().build();
		insertTransport.setAmount(new BigDecimal("10.10"));
		insertTransport.setComment("inserted");
		request.setInsertMoneyflowSplitEntryTransports(Arrays.asList(insertTransport));

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final List<MoneyflowSplitEntry> moneyflowSplitEntries = this.moneyflowSplitEntryService
				.getMoneyflowSplitEntries(userId, moneyflowId);
		Assert.assertEquals(moneyflowSplitEntries.get(0).getId().getId(), MoneyflowSplitEntryTransportBuilder.NEXT_ID);
		Assert.assertEquals(moneyflowSplitEntries.get(0).getAmount(), new BigDecimal("10.10"));
		Assert.assertEquals(moneyflowSplitEntries.get(0).getComment(), "inserted");
	}

	@Test
	public void test_SplitEntries_Delete_ChangesDone() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW1_ID);

		final UpdateMoneyflowRequest request = new UpdateMoneyflowRequest();

		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		request.setMoneyflowTransport(transport);

		request.setDeleteMoneyflowSplitEntryIds(
				Arrays.asList(MoneyflowSplitEntryTransportBuilder.MONEYFLOW_SPLIT_ENTRY1_ID,
						MoneyflowSplitEntryTransportBuilder.MONEYFLOW_SPLIT_ENTRY2_ID));

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final List<MoneyflowSplitEntry> moneyflowSplitEntries = this.moneyflowSplitEntryService
				.getMoneyflowSplitEntries(userId, moneyflowId);
		Assert.assertTrue(moneyflowSplitEntries.isEmpty());
	}

	@Test
	public void test_Splitentries_CommentAndPostingAccountForMainNotSpecified_TakenFromFirstSplitEntryBooking()
			throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW1_ID);

		final UpdateMoneyflowRequest request = new UpdateMoneyflowRequest();
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		transport.setComment(null);
		transport.setPostingaccountid(null);
		request.setMoneyflowTransport(transport);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);
		final List<MoneyflowSplitEntry> moneyflowSplitEntries = this.moneyflowSplitEntryService
				.getMoneyflowSplitEntries(userId, moneyflowId);

		Assert.assertNotNull(moneyflow);
		Assert.assertEquals(moneyflowSplitEntries.get(0).getComment(), moneyflow.getComment());
		Assert.assertEquals(moneyflowSplitEntries.get(0).getPostingAccount().getId(),
				moneyflow.getPostingAccount().getId());
	}

	@Test
	public void test_AuthorizationRequired_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("", this.method, false, ErrorResponse.class);
		Assert.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	public void test_emptyDatabase_noException() throws Exception {
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;

		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		final UpdateMoneyflowRequest request = new UpdateMoneyflowRequest();
		request.setMoneyflowTransport(transport);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);
	}

}