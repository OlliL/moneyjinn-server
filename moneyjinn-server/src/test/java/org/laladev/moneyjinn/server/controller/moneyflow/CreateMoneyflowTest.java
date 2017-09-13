package org.laladev.moneyjinn.server.controller.moneyflow;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import org.laladev.moneyjinn.core.rest.model.moneyflow.CreateMoneyflowRequest;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;
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
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.builder.DateUtil;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.MoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.PostingAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.PreDefMoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.IContractpartnerService;
import org.laladev.moneyjinn.service.api.IMoneyflowService;
import org.laladev.moneyjinn.service.api.IPreDefMoneyflowService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class CreateMoneyflowTest extends AbstractControllerTest {

	@Inject
	private IMoneyflowService moneyflowService;
	@Inject
	private IPreDefMoneyflowService preDefMoneyflowService;
	@Inject
	private ICapitalsourceService capitalsourceService;
	@Inject
	private IContractpartnerService contractpartnerService;

	private final HttpMethod method = HttpMethod.POST;
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
		final CreateMoneyflowRequest request = new CreateMoneyflowRequest();

		request.setMoneyflowTransport(transport);

		final ValidationResponse expected = this.getCompleteResponseObject();
		expected.setResult(Boolean.FALSE);

		final List<ValidationItemTransport> validationItems = new ArrayList<>();
		for (final ErrorCode errorCode : errorCodes) {
			validationItems.add(new ValidationItemTransportBuilder().withKey(transport.getId().intValue())
					.withError(errorCode.getErrorCode()).build());
		}
		expected.setValidationItemTransports(validationItems);

		final ValidationResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				ValidationResponse.class);

		Assert.assertEquals(expected.getErrorResponse(), actual.getErrorResponse());
		Assert.assertEquals(expected.getResult(), actual.getResult());
		Assert.assertEquals(expected.getValidationItemTransports(), actual.getValidationItemTransports());
		Assert.assertEquals(expected, actual);

	}

	private ValidationResponse getCompleteResponseObject() {
		final ValidationResponse expected = new ValidationResponse();

		expected.setResult(Boolean.TRUE);
		return expected;
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
	public void test_BookingdateAfterCapitalsourceValidity_ValidityAdjusted() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceID = new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE3_ID);

		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE3_ID);

		final CreateMoneyflowRequest request = new CreateMoneyflowRequest();
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

		final CreateMoneyflowRequest request = new CreateMoneyflowRequest();
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
		final ContractpartnerID contractpartnerID = new ContractpartnerID(
				ContractpartnerTransportBuilder.CONTRACTPARTNER3_ID);

		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setBookingdate(DateUtil.getGMTDate("2011-01-01"));
		transport.setContractpartnerid(contractpartnerID.getId());

		final CreateMoneyflowRequest request = new CreateMoneyflowRequest();
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

		final CreateMoneyflowRequest request = new CreateMoneyflowRequest();
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
		final CapitalsourceID capitalsourceID = new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE3_ID);
		final ContractpartnerID contractpartnerID = new ContractpartnerID(
				ContractpartnerTransportBuilder.CONTRACTPARTNER3_ID);

		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setCapitalsourceid(capitalsourceID.getId());
		transport.setContractpartnerid(contractpartnerID.getId());
		transport.setBookingdate(DateUtil.getGMTDate("1970-01-01"));

		final Capitalsource capitalsourceOrig = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceID);
		Assert.assertNotNull(capitalsourceOrig);

		final Contractpartner contractpartnerOrig = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerID);
		Assert.assertNotNull(contractpartnerOrig);

		this.testError(transport, ErrorCode.BOOKINGDATE_OUTSIDE_GROUP_ASSIGNMENT,
				ErrorCode.CAPITALSOURCE_USE_OUT_OF_VALIDITY, ErrorCode.CONTRACTPARTNER_NO_LONGER_VALID);

		// make sure, The validity period of Capitalsource and Contractpartner where not adjusted
		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceID);
		final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerID);

		Assert.assertEquals(capitalsourceOrig, capitalsource);
		Assert.assertEquals(contractpartnerOrig, contractpartner);
	}

	@Test
	public void test_BookingDateAfterGroupAssignment_Error() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceID = new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE3_ID);
		final ContractpartnerID contractpartnerID = new ContractpartnerID(
				ContractpartnerTransportBuilder.CONTRACTPARTNER3_ID);

		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setCapitalsourceid(capitalsourceID.getId());
		transport.setContractpartnerid(contractpartnerID.getId());
		transport.setBookingdate(DateUtil.getGMTDate("2600-01-01"));

		final Capitalsource capitalsourceOrig = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceID);
		Assert.assertNotNull(capitalsourceOrig);

		final Contractpartner contractpartnerOrig = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerID);
		Assert.assertNotNull(contractpartnerOrig);

		this.testError(transport, ErrorCode.BOOKINGDATE_OUTSIDE_GROUP_ASSIGNMENT,
				ErrorCode.CAPITALSOURCE_USE_OUT_OF_VALIDITY, ErrorCode.CONTRACTPARTNER_NO_LONGER_VALID);

		// make sure, The validity period of Capitalsource and Contractpartner where not adjusted
		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceID);
		final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerID);

		Assert.assertEquals(capitalsourceOrig, capitalsource);
		Assert.assertEquals(contractpartnerOrig, contractpartner);
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

		Assert.assertNull(moneyflow);
		request.setMoneyflowTransport(transport);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);

		Assert.assertNotNull(moneyflow);
		Assert.assertEquals(transport.getAmount(), moneyflow.getAmount());
		Assert.assertEquals(transport.getCapitalsourceid(), moneyflow.getCapitalsource().getId().getId());
		Assert.assertEquals(transport.getComment(), moneyflow.getComment());
		Assert.assertEquals(transport.getContractpartnerid(), moneyflow.getContractpartner().getId().getId());
		Assert.assertEquals(Short.valueOf("1").equals(transport.getPrivat()), moneyflow.isPrivat());
		Assert.assertEquals(transport.getBookingdate().toLocalDate(), moneyflow.getBookingDate());
		Assert.assertEquals(transport.getBookingdate().toLocalDate(), moneyflow.getInvoiceDate());
		Assert.assertEquals(transport.getPostingaccountid(), moneyflow.getPostingAccount().getId().getId());
	}

	@Test
	public void test_oneNewMoneyflowSaveAsPreDefMoneyflow_Saved() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.NEXT_ID);

		final CreateMoneyflowRequest request = new CreateMoneyflowRequest();

		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();

		Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);

		Assert.assertNull(moneyflow);
		request.setMoneyflowTransport(transport);
		request.setSaveAsPreDefMoneyflow((short) 1);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);

		Assert.assertNotNull(moneyflow);
		Assert.assertEquals(transport.getAmount(), moneyflow.getAmount());
		Assert.assertEquals(transport.getCapitalsourceid(), moneyflow.getCapitalsource().getId().getId());
		Assert.assertEquals(transport.getComment(), moneyflow.getComment());
		Assert.assertEquals(transport.getContractpartnerid(), moneyflow.getContractpartner().getId().getId());
		Assert.assertEquals(Short.valueOf("1").equals(transport.getPrivat()), moneyflow.isPrivat());
		Assert.assertEquals(transport.getBookingdate().toLocalDate(), moneyflow.getBookingDate());
		Assert.assertEquals(transport.getInvoicedate().toLocalDate(), moneyflow.getInvoiceDate());
		Assert.assertEquals(transport.getPostingaccountid(), moneyflow.getPostingAccount().getId().getId());

		final PreDefMoneyflow preDefMoneyflow = this.preDefMoneyflowService.getPreDefMoneyflowById(userId,
				new PreDefMoneyflowID(PreDefMoneyflowTransportBuilder.NEXT_ID));

		Assert.assertNotNull(preDefMoneyflow);
		Assert.assertEquals(preDefMoneyflow.getAmount(), moneyflow.getAmount());
		Assert.assertEquals(preDefMoneyflow.getCapitalsource().getId(), moneyflow.getCapitalsource().getId());
		Assert.assertEquals(preDefMoneyflow.getComment(), moneyflow.getComment());
		Assert.assertEquals(preDefMoneyflow.getContractpartner().getId(), moneyflow.getContractpartner().getId());
		Assert.assertEquals(preDefMoneyflow.isOnceAMonth(), false);
		Assert.assertEquals(preDefMoneyflow.getPostingAccount().getId(), moneyflow.getPostingAccount().getId());
		Assert.assertEquals(preDefMoneyflow.getLastUsedDate(), LocalDate.now());

	}

	@Test
	public void test_preDefMoneyflowSelected_LastUsedUpdatedAndOnceAMonthRespected() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final PreDefMoneyflowID preDefMoneyflowId = new PreDefMoneyflowID(
				PreDefMoneyflowTransportBuilder.PRE_DEF_MONEYFLOW1_ID);

		final CreateMoneyflowRequest request = new CreateMoneyflowRequest();

		final PreDefMoneyflow preDefMoneyflowOrig = this.preDefMoneyflowService.getPreDefMoneyflowById(userId,
				preDefMoneyflowId);

		Assert.assertNotNull(preDefMoneyflowOrig);
		Assert.assertEquals(true, preDefMoneyflowOrig.isOnceAMonth());
		Assert.assertEquals(null, preDefMoneyflowOrig.getLastUsedDate());

		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		request.setMoneyflowTransport(transport);
		request.setUsedPreDefMoneyflowId(preDefMoneyflowId.getId());

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final PreDefMoneyflow preDefMoneyflow = this.preDefMoneyflowService.getPreDefMoneyflowById(userId,
				preDefMoneyflowId);
		Assert.assertNotNull(preDefMoneyflow);
		Assert.assertEquals(preDefMoneyflow.getAmount(), preDefMoneyflowOrig.getAmount());
		Assert.assertEquals(preDefMoneyflow.getCapitalsource().getId(), preDefMoneyflowOrig.getCapitalsource().getId());
		Assert.assertEquals(preDefMoneyflow.getComment(), preDefMoneyflowOrig.getComment());
		Assert.assertEquals(preDefMoneyflow.getContractpartner().getId(),
				preDefMoneyflowOrig.getContractpartner().getId());
		Assert.assertEquals(preDefMoneyflow.isOnceAMonth(), true);
		Assert.assertEquals(preDefMoneyflow.getPostingAccount().getId(),
				preDefMoneyflowOrig.getPostingAccount().getId());
		Assert.assertEquals(preDefMoneyflow.getLastUsedDate(), LocalDate.now());
	}

	@Test
	public void test_preDefMoneyflowSelectedAndUpdated_UpdateDoneLastUsedDateSet() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final PreDefMoneyflowID preDefMoneyflowId = new PreDefMoneyflowID(
				PreDefMoneyflowTransportBuilder.PRE_DEF_MONEYFLOW1_ID);

		final CreateMoneyflowRequest request = new CreateMoneyflowRequest();

		PreDefMoneyflow preDefMoneyflow = this.preDefMoneyflowService.getPreDefMoneyflowById(userId, preDefMoneyflowId);

		Assert.assertNotNull(preDefMoneyflow);
		Assert.assertEquals(true, preDefMoneyflow.isOnceAMonth());
		Assert.assertEquals(null, preDefMoneyflow.getLastUsedDate());

		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		request.setMoneyflowTransport(transport);
		request.setUsedPreDefMoneyflowId(preDefMoneyflowId.getId());
		request.setSaveAsPreDefMoneyflow((short) 1);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		preDefMoneyflow = this.preDefMoneyflowService.getPreDefMoneyflowById(userId, preDefMoneyflowId);
		Assert.assertNotNull(preDefMoneyflow);
		Assert.assertEquals(preDefMoneyflow.getAmount(), transport.getAmount());
		Assert.assertEquals(preDefMoneyflow.getCapitalsource().getId().getId(), transport.getCapitalsourceid());
		Assert.assertEquals(preDefMoneyflow.getComment(), transport.getComment());
		Assert.assertEquals(preDefMoneyflow.getContractpartner().getId().getId(), transport.getContractpartnerid());
		Assert.assertEquals(preDefMoneyflow.isOnceAMonth(), true);
		Assert.assertEquals(preDefMoneyflow.getPostingAccount().getId().getId(), transport.getPostingaccountid());
		Assert.assertEquals(preDefMoneyflow.getLastUsedDate(), LocalDate.now());
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

		final CreateMoneyflowRequest request = new CreateMoneyflowRequest();

		request.setMoneyflowTransport(transport);

		super.callUsecaseWithContent("", this.method, request, false, ValidationResponse.class);
	}
}