package org.laladev.moneyjinn.server.controller.moneyflow;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import org.laladev.moneyjinn.core.rest.model.moneyflow.CreateMoneyflowRequest;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;
import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowSplitEntryTransport;
import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowTransport;
import org.laladev.moneyjinn.core.rest.model.transport.PreDefMoneyflowTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.model.Contractpartner;
import org.laladev.moneyjinn.model.ContractpartnerID;
import org.laladev.moneyjinn.model.PreDefMoneyflow;
import org.laladev.moneyjinn.model.PreDefMoneyflowID;
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
import org.laladev.moneyjinn.server.builder.PreDefMoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.IContractpartnerService;
import org.laladev.moneyjinn.service.api.IMoneyflowService;
import org.laladev.moneyjinn.service.api.IMoneyflowSplitEntryService;
import org.laladev.moneyjinn.service.api.IPreDefMoneyflowService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class CreateMoneyflowTest extends AbstractControllerTest {

	@Inject
	private IMoneyflowService moneyflowService;
	@Inject
	private IMoneyflowSplitEntryService moneyflowSpliEntryService;
	@Inject
	private IPreDefMoneyflowService preDefMoneyflowService;
	@Inject
	private ICapitalsourceService capitalsourceService;
	@Inject
	private IContractpartnerService contractpartnerService;

	private final HttpMethod method = HttpMethod.POST;
	private String userName;
	private String userPassword;

	@BeforeEach
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

	private void testError(final MoneyflowTransport transport,
			final List<MoneyflowSplitEntryTransport> moneyflowSplitEntryTransports, final ErrorCode... errorCodes)
			throws Exception {
		final CreateMoneyflowRequest request = new CreateMoneyflowRequest();

		request.setMoneyflowTransport(transport);
		request.setInsertMoneyflowSplitEntryTransports(moneyflowSplitEntryTransports);

		final ValidationResponse expected = new ValidationResponse();
		expected.setResult(Boolean.FALSE);

		final List<ValidationItemTransport> validationItems = new ArrayList<>();
		for (final ErrorCode errorCode : errorCodes) {
			validationItems.add(new ValidationItemTransportBuilder()
					.withKey(transport.getId() == null ? null : transport.getId().intValue())
					.withError(errorCode.getErrorCode()).build());
		}
		expected.setValidationItemTransports(validationItems);

		final ValidationResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				ValidationResponse.class);

		Assertions.assertEquals(expected.getErrorResponse(), actual.getErrorResponse());
		Assertions.assertEquals(expected.getResult(), actual.getResult());
		Assertions.assertEquals(expected.getValidationItemTransports(), actual.getValidationItemTransports());
		Assertions.assertEquals(expected, actual);
	}

	private void testError(final MoneyflowTransport transport, final ErrorCode... errorCodes) throws Exception {
		this.testError(transport, new ArrayList<>(), errorCodes);
	}

	@Test
	public void test_emptyComment_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setComment("");

		this.testError(transport, ErrorCode.COMMENT_IS_NOT_SET);
	}

	@Test
	public void test_nullComment_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setComment(null);

		this.testError(transport, ErrorCode.COMMENT_IS_NOT_SET);
	}

	@Test
	public void test_nullCapitalsource_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setCapitalsourceid(null);

		this.testError(transport, ErrorCode.CAPITALSOURCE_IS_NOT_SET);
	}

	@Test
	public void test_notExistingCapitalsource_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setCapitalsourceid(CapitalsourceTransportBuilder.NON_EXISTING_ID);

		this.testError(transport, ErrorCode.CAPITALSOURCE_DOES_NOT_EXIST);
	}

	@Test
	public void test_creditCapitalsource_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE5_ID);

		this.testError(transport, ErrorCode.CAPITALSOURCE_INVALID);
	}

	@Test
	public void test_AmountToBig_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setAmount(new BigDecimal(9999999));

		this.testError(transport, ErrorCode.AMOUNT_TO_BIG);
	}

	@Test
	public void test_BookingdateAfterCapitalsourceValidity_ValidityAdjusted() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE3_ID);

		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE3_ID);

		final CreateMoneyflowRequest request = new CreateMoneyflowRequest();
		request.setMoneyflowTransport(transport);

		final Capitalsource capitalsourceOrig = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);
		Assertions.assertNotEquals(capitalsourceOrig.getValidTil(), capitalsource.getValidTil());
		Assertions.assertEquals(capitalsourceOrig.getValidFrom(), capitalsource.getValidFrom());
		Assertions.assertEquals(transport.getBookingdate().toLocalDate(), capitalsource.getValidTil());
	}

	@Test
	public void test_BookingdateBeforeCapitalsourceValidity_ValidityAdjusted() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE4_ID);

		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setCapitalsourceid(capitalsourceId.getId());
		transport.setBookingdate(DateUtil.getGmtDate("2000-01-01"));

		final CreateMoneyflowRequest request = new CreateMoneyflowRequest();
		request.setMoneyflowTransport(transport);

		final Capitalsource capitalsourceOrig = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);
		Assertions.assertNotEquals(capitalsourceOrig.getValidFrom(), capitalsource.getValidFrom());
		Assertions.assertEquals(capitalsourceOrig.getValidTil(), capitalsource.getValidTil());
		Assertions.assertEquals(transport.getBookingdate().toLocalDate(), capitalsource.getValidFrom());
	}

	@Test
	public void test_nullContractpartner_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setContractpartnerid(null);

		this.testError(transport, ErrorCode.CONTRACTPARTNER_IS_NOT_SET);
	}

	@Test
	public void test_notExistingContractpartner_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setContractpartnerid(ContractpartnerTransportBuilder.NON_EXISTING_ID);

		this.testError(transport, ErrorCode.CONTRACTPARTNER_DOES_NOT_EXIST);
	}

	@Test
	public void test_BookingdateAfterContractpartnerValidity_ValidityAdjusted() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(
				ContractpartnerTransportBuilder.CONTRACTPARTNER3_ID);

		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setBookingdate(DateUtil.getGmtDate("2011-01-01"));
		transport.setContractpartnerid(contractpartnerId.getId());

		final CreateMoneyflowRequest request = new CreateMoneyflowRequest();
		request.setMoneyflowTransport(transport);

		final Contractpartner contractpartnerOrig = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerId);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerId);
		Assertions.assertNotEquals(contractpartnerOrig.getValidTil(), contractpartner.getValidTil());
		Assertions.assertEquals(contractpartnerOrig.getValidFrom(), contractpartner.getValidFrom());
		Assertions.assertEquals(transport.getBookingdate().toLocalDate(), contractpartner.getValidTil());
	}

	@Test
	public void test_BookingdateBeforeContractpartnerValidity_ValidityAdjusted() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(
				ContractpartnerTransportBuilder.CONTRACTPARTNER4_ID);

		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setBookingdate(DateUtil.getGmtDate("2000-01-01"));
		transport.setContractpartnerid(contractpartnerId.getId());

		final CreateMoneyflowRequest request = new CreateMoneyflowRequest();
		request.setMoneyflowTransport(transport);

		final Contractpartner contractpartnerOrig = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerId);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerId);
		Assertions.assertNotEquals(contractpartnerOrig.getValidFrom(), contractpartner.getValidFrom());
		Assertions.assertEquals(contractpartnerOrig.getValidTil(), contractpartner.getValidTil());
		Assertions.assertEquals(transport.getBookingdate().toLocalDate(), contractpartner.getValidFrom());
	}

	@Test
	public void test_nullAmount_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setAmount(null);

		this.testError(transport, ErrorCode.AMOUNT_IS_ZERO);
	}

	@Test
	public void test_zeroAmount_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setAmount(BigDecimal.ZERO);

		this.testError(transport, ErrorCode.AMOUNT_IS_ZERO);
	}

	// make sure it 0 is compared with compareTo not with equals
	@Test
	public void test_0_00Amount_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setAmount(new BigDecimal("0.00000"));

		this.testError(transport, ErrorCode.AMOUNT_IS_ZERO);
	}

	@Test
	public void test_nullPostingAccount_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setPostingaccountid(null);

		this.testError(transport, ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED);
	}

	@Test
	public void test_nullBookingDate_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setBookingdate(null);

		this.testError(transport, ErrorCode.BOOKINGDATE_IN_WRONG_FORMAT);
	}

	@Test
	public void test_BookingDateBeforeGroupAssignment_Error() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE3_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(
				ContractpartnerTransportBuilder.CONTRACTPARTNER3_ID);

		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setCapitalsourceid(capitalsourceId.getId());
		transport.setContractpartnerid(contractpartnerId.getId());
		transport.setBookingdate(DateUtil.getGmtDate("1970-01-01"));

		final Capitalsource capitalsourceOrig = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);
		Assertions.assertNotNull(capitalsourceOrig);

		final Contractpartner contractpartnerOrig = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerId);
		Assertions.assertNotNull(contractpartnerOrig);

		this.testError(transport, ErrorCode.BOOKINGDATE_OUTSIDE_GROUP_ASSIGNMENT,
				ErrorCode.CAPITALSOURCE_USE_OUT_OF_VALIDITY, ErrorCode.CONTRACTPARTNER_NO_LONGER_VALID);

		// make sure, The validity period of Capitalsource and Contractpartner where not adjusted
		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);
		final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerId);

		Assertions.assertEquals(capitalsourceOrig, capitalsource);
		Assertions.assertEquals(contractpartnerOrig, contractpartner);
	}

	@Test
	public void test_BookingDateAfterGroupAssignment_Error() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE3_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(
				ContractpartnerTransportBuilder.CONTRACTPARTNER3_ID);

		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setCapitalsourceid(capitalsourceId.getId());
		transport.setContractpartnerid(contractpartnerId.getId());
		transport.setBookingdate(DateUtil.getGmtDate("2600-01-01"));

		final Capitalsource capitalsourceOrig = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);
		Assertions.assertNotNull(capitalsourceOrig);

		final Contractpartner contractpartnerOrig = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerId);
		Assertions.assertNotNull(contractpartnerOrig);

		this.testError(transport, ErrorCode.BOOKINGDATE_OUTSIDE_GROUP_ASSIGNMENT,
				ErrorCode.CAPITALSOURCE_USE_OUT_OF_VALIDITY, ErrorCode.CONTRACTPARTNER_NO_LONGER_VALID);

		// make sure, The validity period of Capitalsource and Contractpartner where not adjusted
		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);
		final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerId);

		Assertions.assertEquals(capitalsourceOrig, capitalsource);
		Assertions.assertEquals(contractpartnerOrig, contractpartner);
	}

	@Test
	public void test_notExistingPostingAccount_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setPostingaccountid(PostingAccountTransportBuilder.NON_EXISTING_ID);

		this.testError(transport, ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED);
	}

	@Test
	public void test_oneNewMoneyflowWithoutInvoiceDate_CreationDone() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.NEXT_ID);

		final CreateMoneyflowRequest request = new CreateMoneyflowRequest();

		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setInvoicedate(null);

		Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);

		Assertions.assertNull(moneyflow);
		request.setMoneyflowTransport(transport);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);

		Assertions.assertNotNull(moneyflow);
		Assertions.assertEquals(transport.getAmount(), moneyflow.getAmount());
		Assertions.assertEquals(transport.getCapitalsourceid(), moneyflow.getCapitalsource().getId().getId());
		Assertions.assertEquals(transport.getComment(), moneyflow.getComment());
		Assertions.assertEquals(transport.getContractpartnerid(), moneyflow.getContractpartner().getId().getId());
		Assertions.assertEquals(Short.valueOf("1").equals(transport.getPrivat()), moneyflow.isPrivat());
		Assertions.assertEquals(transport.getBookingdate().toLocalDate(), moneyflow.getBookingDate());
		Assertions.assertEquals(transport.getBookingdate().toLocalDate(), moneyflow.getInvoiceDate());
		Assertions.assertEquals(transport.getPostingaccountid(), moneyflow.getPostingAccount().getId().getId());
	}

	@Test
	public void test_oneNewMoneyflowSaveAsPreDefMoneyflow_Saved() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.NEXT_ID);

		final CreateMoneyflowRequest request = new CreateMoneyflowRequest();

		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();

		Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);

		Assertions.assertNull(moneyflow);
		request.setMoneyflowTransport(transport);
		request.setSaveAsPreDefMoneyflow((short) 1);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);

		Assertions.assertNotNull(moneyflow);
		Assertions.assertEquals(transport.getAmount(), moneyflow.getAmount());
		Assertions.assertEquals(transport.getCapitalsourceid(), moneyflow.getCapitalsource().getId().getId());
		Assertions.assertEquals(transport.getComment(), moneyflow.getComment());
		Assertions.assertEquals(transport.getContractpartnerid(), moneyflow.getContractpartner().getId().getId());
		Assertions.assertEquals(Short.valueOf("1").equals(transport.getPrivat()), moneyflow.isPrivat());
		Assertions.assertEquals(transport.getBookingdate().toLocalDate(), moneyflow.getBookingDate());
		Assertions.assertEquals(transport.getInvoicedate().toLocalDate(), moneyflow.getInvoiceDate());
		Assertions.assertEquals(transport.getPostingaccountid(), moneyflow.getPostingAccount().getId().getId());

		final PreDefMoneyflow preDefMoneyflow = this.preDefMoneyflowService.getPreDefMoneyflowById(userId,
				new PreDefMoneyflowID(PreDefMoneyflowTransportBuilder.NEXT_ID));

		Assertions.assertNotNull(preDefMoneyflow);
		Assertions.assertEquals(preDefMoneyflow.getAmount(), moneyflow.getAmount());
		Assertions.assertEquals(preDefMoneyflow.getCapitalsource().getId(), moneyflow.getCapitalsource().getId());
		Assertions.assertEquals(preDefMoneyflow.getComment(), moneyflow.getComment());
		Assertions.assertEquals(preDefMoneyflow.getContractpartner().getId(), moneyflow.getContractpartner().getId());
		Assertions.assertEquals(preDefMoneyflow.isOnceAMonth(), false);
		Assertions.assertEquals(preDefMoneyflow.getPostingAccount().getId(), moneyflow.getPostingAccount().getId());
		Assertions.assertEquals(preDefMoneyflow.getLastUsedDate(), LocalDate.now());

	}

	@Test
	public void test_preDefMoneyflowSelected_LastUsedUpdatedAndOnceAMonthRespected() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final PreDefMoneyflowID preDefMoneyflowId = new PreDefMoneyflowID(
				PreDefMoneyflowTransportBuilder.PRE_DEF_MONEYFLOW1_ID);

		final CreateMoneyflowRequest request = new CreateMoneyflowRequest();

		final PreDefMoneyflow preDefMoneyflowOrig = this.preDefMoneyflowService.getPreDefMoneyflowById(userId,
				preDefMoneyflowId);

		Assertions.assertNotNull(preDefMoneyflowOrig);
		Assertions.assertEquals(true, preDefMoneyflowOrig.isOnceAMonth());
		Assertions.assertEquals(null, preDefMoneyflowOrig.getLastUsedDate());

		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		request.setMoneyflowTransport(transport);
		request.setUsedPreDefMoneyflowId(preDefMoneyflowId.getId());

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final PreDefMoneyflow preDefMoneyflow = this.preDefMoneyflowService.getPreDefMoneyflowById(userId,
				preDefMoneyflowId);
		Assertions.assertNotNull(preDefMoneyflow);
		Assertions.assertEquals(preDefMoneyflow.getAmount(), preDefMoneyflowOrig.getAmount());
		Assertions.assertEquals(preDefMoneyflow.getCapitalsource().getId(),
				preDefMoneyflowOrig.getCapitalsource().getId());
		Assertions.assertEquals(preDefMoneyflow.getComment(), preDefMoneyflowOrig.getComment());
		Assertions.assertEquals(preDefMoneyflow.getContractpartner().getId(),
				preDefMoneyflowOrig.getContractpartner().getId());
		Assertions.assertEquals(preDefMoneyflow.isOnceAMonth(), true);
		Assertions.assertEquals(preDefMoneyflow.getPostingAccount().getId(),
				preDefMoneyflowOrig.getPostingAccount().getId());
		Assertions.assertEquals(preDefMoneyflow.getLastUsedDate(), LocalDate.now());
	}

	@Test
	public void test_preDefMoneyflowSelectedAndUpdated_UpdateDoneLastUsedDateSet() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final PreDefMoneyflowID preDefMoneyflowId = new PreDefMoneyflowID(
				PreDefMoneyflowTransportBuilder.PRE_DEF_MONEYFLOW1_ID);

		final CreateMoneyflowRequest request = new CreateMoneyflowRequest();

		PreDefMoneyflow preDefMoneyflow = this.preDefMoneyflowService.getPreDefMoneyflowById(userId, preDefMoneyflowId);

		Assertions.assertNotNull(preDefMoneyflow);
		Assertions.assertEquals(true, preDefMoneyflow.isOnceAMonth());
		Assertions.assertEquals(null, preDefMoneyflow.getLastUsedDate());

		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		request.setMoneyflowTransport(transport);
		request.setUsedPreDefMoneyflowId(preDefMoneyflowId.getId());
		request.setSaveAsPreDefMoneyflow((short) 1);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		preDefMoneyflow = this.preDefMoneyflowService.getPreDefMoneyflowById(userId, preDefMoneyflowId);
		Assertions.assertNotNull(preDefMoneyflow);
		Assertions.assertEquals(preDefMoneyflow.getAmount(), transport.getAmount());
		Assertions.assertEquals(preDefMoneyflow.getCapitalsource().getId().getId(), transport.getCapitalsourceid());
		Assertions.assertEquals(preDefMoneyflow.getComment(), transport.getComment());
		Assertions.assertEquals(preDefMoneyflow.getContractpartner().getId().getId(), transport.getContractpartnerid());
		Assertions.assertEquals(preDefMoneyflow.isOnceAMonth(), true);
		Assertions.assertEquals(preDefMoneyflow.getPostingAccount().getId().getId(), transport.getPostingaccountid());
		Assertions.assertEquals(preDefMoneyflow.getLastUsedDate(), LocalDate.now());
	}

	@Test
	public void test_NotGroupUseableCapitalsourceUsed_Error() throws Exception {
		this.userName = UserTransportBuilder.USER3_NAME;
		this.userPassword = UserTransportBuilder.USER3_PASSWORD;
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID);

		final List<CapitalsourceTransport> capitalsourceTransports = new ArrayList<>();
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource6().build());
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource2().build());
		final List<PreDefMoneyflowTransport> preDefMoneyflowTransports = new ArrayList<>();
		preDefMoneyflowTransports.add(new PreDefMoneyflowTransportBuilder().forPreDefMoneyflow2().build());

		this.testError(transport, ErrorCode.CAPITALSOURCE_DOES_NOT_EXIST);
	}

	@Test
	public void test_SplitEntries_Successfull() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.NEXT_ID);

		final CreateMoneyflowRequest request = new CreateMoneyflowRequest();
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		request.setMoneyflowTransport(transport);

		final MoneyflowSplitEntryTransport mseTransport1 = this.getMseTransport1(transport);
		final MoneyflowSplitEntryTransport mseTransport2 = this.getMseTransport2(transport);
		request.setInsertMoneyflowSplitEntryTransports(Arrays.asList(mseTransport1, mseTransport2));

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final List<MoneyflowSplitEntry> moneyflowSplitEntries = this.moneyflowSpliEntryService
				.getMoneyflowSplitEntries(userId, moneyflowId);
		Assertions.assertEquals(2, moneyflowSplitEntries.size());
		Assertions.assertEquals(moneyflowSplitEntries.get(0).getAmount(), mseTransport1.getAmount());
		Assertions.assertEquals(moneyflowSplitEntries.get(0).getComment(), mseTransport1.getComment());
		Assertions.assertEquals(moneyflowSplitEntries.get(0).getPostingAccount().getId().getId(),
				mseTransport1.getPostingaccountid());
		Assertions.assertEquals(moneyflowSplitEntries.get(1).getAmount(), mseTransport2.getAmount());
		Assertions.assertEquals(moneyflowSplitEntries.get(1).getComment(), mseTransport2.getComment());
		Assertions.assertEquals(moneyflowSplitEntries.get(1).getPostingAccount().getId().getId(),
				mseTransport2.getPostingaccountid());

	}

	private MoneyflowSplitEntryTransport getMseTransport2(final MoneyflowTransport transport) {
		return new MoneyflowSplitEntryTransportBuilder().withAmount(transport.getAmount().divide(new BigDecimal(2)))
				.withComment("comment2").withPostingaccountid(PostingAccountTransportBuilder.POSTING_ACCOUNT2_ID)
				.build();
	}

	private MoneyflowSplitEntryTransport getMseTransport1(final MoneyflowTransport transport) {
		return new MoneyflowSplitEntryTransportBuilder().withAmount(transport.getAmount().divide(new BigDecimal(2)))
				.withComment("comment1").withPostingaccountid(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID)
				.build();
	}

	@Test
	public void test_SplitEntries_SumNotMatchingOverallAmount_Error() throws Exception {
		final CreateMoneyflowRequest request = new CreateMoneyflowRequest();
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		request.setMoneyflowTransport(transport);

		final MoneyflowSplitEntryTransport mseTransport1 = this.getMseTransport1(transport);
		mseTransport1.setAmount(transport.getAmount());
		final MoneyflowSplitEntryTransport mseTransport2 = this.getMseTransport2(transport);

		this.testError(transport, Arrays.asList(mseTransport1, mseTransport2),
				ErrorCode.SPLIT_ENTRIES_AMOUNT_IS_NOT_EQUALS_MONEYFLOW_AMOUNT);
	}

	@Test
	public void test_Splitentries_CommentAndPostingAccountForMainNotSpecified_TakenFromFirstSplitEntryBooking()
			throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.NEXT_ID);

		final CreateMoneyflowRequest request = new CreateMoneyflowRequest();
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setComment(null);
		transport.setPostingaccountid(null);
		request.setMoneyflowTransport(transport);

		final MoneyflowSplitEntryTransport mseTransport1 = this.getMseTransport1(transport);
		final MoneyflowSplitEntryTransport mseTransport2 = this.getMseTransport2(transport);
		request.setInsertMoneyflowSplitEntryTransports(Arrays.asList(mseTransport1, mseTransport2));

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);

		Assertions.assertNotNull(moneyflow);
		Assertions.assertEquals(mseTransport1.getComment(), moneyflow.getComment());
		Assertions.assertEquals(mseTransport1.getPostingaccountid(), moneyflow.getPostingAccount().getId().getId());
	}

	@Test
	public void test_SplitEntries_Insert_emptyComment_Error() throws Exception {
		final CreateMoneyflowRequest request = new CreateMoneyflowRequest();
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		request.setMoneyflowTransport(transport);

		final MoneyflowSplitEntryTransport mseTransport1 = this.getMseTransport1(transport);
		mseTransport1.setComment("");
		final MoneyflowSplitEntryTransport mseTransport2 = this.getMseTransport2(transport);

		// Hack because error is reported for the MoneyflowSplitEntry which has ID null set
		transport.setId(null);
		this.testError(transport, Arrays.asList(mseTransport1, mseTransport2), ErrorCode.COMMENT_IS_NOT_SET);
	}

	@Test
	public void test_SplitEntries_Insert_zeroAmount_Error() throws Exception {
		final CreateMoneyflowRequest request = new CreateMoneyflowRequest();
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		request.setMoneyflowTransport(transport);

		final MoneyflowSplitEntryTransport mseTransport1 = this.getMseTransport1(transport);
		mseTransport1.setAmount(BigDecimal.ZERO);
		final MoneyflowSplitEntryTransport mseTransport2 = this.getMseTransport2(transport);
		mseTransport2.setAmount(transport.getAmount());

		// Hack because error is reported for the MoneyflowSplitEntry which has ID null set
		transport.setId(null);
		this.testError(transport, Arrays.asList(mseTransport1, mseTransport2), ErrorCode.AMOUNT_IS_ZERO);
	}

	// make sure it 0 is compared with compareTo not with equals
	@Test
	public void test_SplitEntries_Insert_0_00Amount_Error() throws Exception {
		final CreateMoneyflowRequest request = new CreateMoneyflowRequest();
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		request.setMoneyflowTransport(transport);

		final MoneyflowSplitEntryTransport mseTransport1 = this.getMseTransport1(transport);
		mseTransport1.setAmount(new BigDecimal("0.00000"));
		final MoneyflowSplitEntryTransport mseTransport2 = this.getMseTransport2(transport);
		mseTransport2.setAmount(transport.getAmount());

		// Hack because error is reported for the MoneyflowSplitEntry which has ID null set
		transport.setId(null);
		this.testError(transport, Arrays.asList(mseTransport1, mseTransport2), ErrorCode.AMOUNT_IS_ZERO);
	}

	@Test
	public void test_SplitEntries_Insert_nullComment_Error() throws Exception {
		final CreateMoneyflowRequest request = new CreateMoneyflowRequest();
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		request.setMoneyflowTransport(transport);

		final MoneyflowSplitEntryTransport mseTransport1 = this.getMseTransport1(transport);
		mseTransport1.setComment(null);
		final MoneyflowSplitEntryTransport mseTransport2 = this.getMseTransport2(transport);

		// Hack because error is reported for the MoneyflowSplitEntry which has ID null set
		transport.setId(null);
		this.testError(transport, Arrays.asList(mseTransport1, mseTransport2), ErrorCode.COMMENT_IS_NOT_SET);
	}

	@Test
	public void test_SplitEntries_Insert_nullAmount_Error() throws Exception {
		final CreateMoneyflowRequest request = new CreateMoneyflowRequest();
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		request.setMoneyflowTransport(transport);

		final MoneyflowSplitEntryTransport mseTransport1 = this.getMseTransport1(transport);
		mseTransport1.setAmount(null);
		final MoneyflowSplitEntryTransport mseTransport2 = this.getMseTransport2(transport);
		mseTransport2.setAmount(transport.getAmount());

		// Hack because error is reported for the MoneyflowSplitEntry which has ID null set
		transport.setId(null);
		this.testError(transport, Arrays.asList(mseTransport1, mseTransport2), ErrorCode.AMOUNT_IS_ZERO);
	}

	@Test
	public void test_SplitEntries_Insert_nullPostingAccount_Error() throws Exception {
		final CreateMoneyflowRequest request = new CreateMoneyflowRequest();
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		request.setMoneyflowTransport(transport);

		final MoneyflowSplitEntryTransport mseTransport1 = this.getMseTransport1(transport);
		mseTransport1.setPostingaccountid(null);
		final MoneyflowSplitEntryTransport mseTransport2 = this.getMseTransport2(transport);

		// Hack because error is reported for the MoneyflowSplitEntry which has ID null set
		transport.setId(null);
		this.testError(transport, Arrays.asList(mseTransport1, mseTransport2), ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED);
	}

	// TODO implement me
	@Test
	public void test_SplitEntries_Insert_notExistingPostingAccount_Error() throws Exception {
		final CreateMoneyflowRequest request = new CreateMoneyflowRequest();
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		request.setMoneyflowTransport(transport);

		final MoneyflowSplitEntryTransport mseTransport1 = this.getMseTransport1(transport);
		mseTransport1.setPostingaccountid(PostingAccountTransportBuilder.NON_EXISTING_ID);
		final MoneyflowSplitEntryTransport mseTransport2 = this.getMseTransport2(transport);

		// Hack because error is reported for the MoneyflowSplitEntry which has ID null set
		transport.setId(null);
		this.testError(transport, Arrays.asList(mseTransport1, mseTransport2), ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED);
	}

	@Test
	public void test_AuthorizationRequired_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("", this.method, false, ErrorResponse.class);
		Assertions.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	public void test_emptyDatabase_noException() throws Exception {
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();

		final CreateMoneyflowRequest request = new CreateMoneyflowRequest();

		request.setMoneyflowTransport(transport);

		super.callUsecaseWithContent("", this.method, request, false, ValidationResponse.class);
	}
}