package org.laladev.moneyjinn.server.controller.moneyflow;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.moneyflow.CreateMoneyflowsRequest;
import org.laladev.moneyjinn.core.rest.model.moneyflow.CreateMoneyflowsResponse;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ContractpartnerTransport;
import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowTransport;
import org.laladev.moneyjinn.core.rest.model.transport.PostingAccountTransport;
import org.laladev.moneyjinn.core.rest.model.transport.PreDefMoneyflowTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.model.PreDefMoneyflow;
import org.laladev.moneyjinn.model.PreDefMoneyflowID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.moneyflow.Moneyflow;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowID;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.builder.DateUtil;
import org.laladev.moneyjinn.server.builder.MoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.PostingAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.PreDefMoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.IMoneyflowService;
import org.laladev.moneyjinn.service.api.IPreDefMoneyflowService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class CreateMoneyflowsTest extends AbstractControllerTest {

	@Inject
	IMoneyflowService moneyflowService;
	@Inject
	IPreDefMoneyflowService preDefMoneyflowService;

	@Inject
	IAccessRelationService accessRelationService;

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

	private void testError(final MoneyflowTransport transport, final List<CapitalsourceTransport> overrideCapitalsources,
			final List<ContractpartnerTransport> overrideContractpartner, final List<PreDefMoneyflowTransport> overridePreDefMoneyflows,
			final ErrorCode... errorCodes) throws Exception {
		final CreateMoneyflowsRequest request = new CreateMoneyflowsRequest();

		request.setMoneyflowTransports(Arrays.asList(transport));

		final CreateMoneyflowsResponse expected = this.getCompleteResponseObject();
		expected.setResult(Boolean.FALSE);

		final List<ValidationItemTransport> validationItems = new ArrayList<>();
		for (final ErrorCode errorCode : errorCodes) {
			validationItems.add(new ValidationItemTransportBuilder().withKey(transport.getId().intValue()).withError(errorCode.getErrorCode()).build());
		}
		expected.setValidationItemTransports(validationItems);
		if (overrideCapitalsources != null) {
			expected.setCapitalsourceTransports(overrideCapitalsources);
		}
		if (overrideContractpartner != null) {
			expected.setContractpartnerTransports(overrideContractpartner);
		}
		if (overridePreDefMoneyflows != null) {
			expected.setPreDefMoneyflowTransports(overridePreDefMoneyflows);
		}

		final CreateMoneyflowsResponse actual = super.callUsecaseWithContent("", this.method, request, false, CreateMoneyflowsResponse.class);

		Assert.assertEquals(expected.getErrorResponse(), actual.getErrorResponse());
		Assert.assertEquals(expected.getResult(), actual.getResult());
		Assert.assertEquals(expected.getValidationItemTransports(), actual.getValidationItemTransports());
		Assert.assertEquals(expected.getPostingAccountTransports(), actual.getPostingAccountTransports());
		Assert.assertEquals(expected.getCapitalsourceTransports(), actual.getCapitalsourceTransports());
		Assert.assertEquals(expected.getContractpartnerTransports(), actual.getContractpartnerTransports());
		Assert.assertEquals(expected.getPreDefMoneyflowTransports(), actual.getPreDefMoneyflowTransports());
		Assert.assertEquals(expected, actual);

	}

	private CreateMoneyflowsResponse getCompleteResponseObject() {
		final CreateMoneyflowsResponse expected = new CreateMoneyflowsResponse();
		final List<PostingAccountTransport> postingAccountTransports = new ArrayList<>();
		postingAccountTransports.add(new PostingAccountTransportBuilder().forPostingAccount1().build());
		postingAccountTransports.add(new PostingAccountTransportBuilder().forPostingAccount2().build());
		postingAccountTransports.add(new PostingAccountTransportBuilder().forPostingAccount3().build());
		expected.setPostingAccountTransports(postingAccountTransports);

		final List<ContractpartnerTransport> contractpartnerTransports = new ArrayList<>();
		contractpartnerTransports.add(new ContractpartnerTransportBuilder().forContractpartner1().build());
		contractpartnerTransports.add(new ContractpartnerTransportBuilder().forContractpartner2().build());
		expected.setContractpartnerTransports(contractpartnerTransports);

		final List<CapitalsourceTransport> capitalsourceTransports = new ArrayList<>();
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource1().build());
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource2().build());
		expected.setCapitalsourceTransports(capitalsourceTransports);

		final List<PreDefMoneyflowTransport> preDefMoneyflowTransports = new ArrayList<>();
		preDefMoneyflowTransports.add(new PreDefMoneyflowTransportBuilder().forPreDefMoneyflow1().build());
		preDefMoneyflowTransports.add(new PreDefMoneyflowTransportBuilder().forPreDefMoneyflow3().build());
		expected.setPreDefMoneyflowTransports(preDefMoneyflowTransports);

		expected.setSettingNumberOfFreeMoneyflows(1);
		expected.setResult(Boolean.TRUE);
		return expected;
	}

	@Test
	public void test_emptyComment_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setComment("");

		this.testError(transport, null, null, null, ErrorCode.COMMENT_IS_NOT_SET);
	}

	@Test
	public void test_nullComment_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setComment(null);

		this.testError(transport, null, null, null, ErrorCode.COMMENT_IS_NOT_SET);
	}

	@Test
	public void test_nullCapitalsource_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setCapitalsourceid(null);

		this.testError(transport, null, null, null, ErrorCode.CAPITALSOURCE_IS_NOT_SET);
	}

	@Test
	public void test_notExistingCapitalsource_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setCapitalsourceid(CapitalsourceTransportBuilder.NON_EXISTING_ID);

		this.testError(transport, null, null, null, ErrorCode.CAPITALSOURCE_DOES_NOT_EXIST);
	}

	@Test
	public void test_creditCapitalsource_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE5_ID);

		this.testError(transport, null, null, null, ErrorCode.CAPITALSOURCE_INVALID);
	}

	@Test
	public void test_noLongerValidCapitalsource_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE3_ID);

		this.testError(transport, null, null, null, ErrorCode.CAPITALSOURCE_USE_OUT_OF_VALIDITY);
	}

	@Test
	public void test_nullContractpartner_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setContractpartnerid(null);

		this.testError(transport, null, null, null, ErrorCode.CONTRACTPARTNER_IS_NOT_SET);
	}

	@Test
	public void test_notExistingContractpartner_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setContractpartnerid(ContractpartnerTransportBuilder.NON_EXISTING_ID);

		this.testError(transport, null, null, null, ErrorCode.CONTRACTPARTNER_DOES_NOT_EXIST);
	}

	@Test
	public void test_noLongerValidContractpartner_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setBookingdate(DateUtil.getGMTDate("2011-01-01"));
		transport.setContractpartnerid(ContractpartnerTransportBuilder.CONTRACTPARTNER3_ID);

		this.testError(transport, null, null, null, ErrorCode.CONTRACTPARTNER_NO_LONGER_VALID);
	}

	@Test
	public void test_nullAmount_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setAmount(null);

		this.testError(transport, null, null, null, ErrorCode.AMOUNT_IS_ZERO);
	}

	@Test
	public void test_zeroAmount_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setAmount(BigDecimal.ZERO);

		this.testError(transport, null, null, null, ErrorCode.AMOUNT_IS_ZERO);
	}

	// make sure it 0 is compared with compareTo not with equals
	@Test
	public void test_0_00Amount_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setAmount(new BigDecimal("0.00000"));

		this.testError(transport, null, null, null, ErrorCode.AMOUNT_IS_ZERO);
	}

	@Test
	public void test_nullPostingAccount_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setPostingaccountid(null);

		this.testError(transport, null, null, null, ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED);
	}

	@Test
	public void test_nullBookingDate_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setBookingdate(null);

		this.testError(transport, null, null, null, ErrorCode.BOOKINGDATE_IN_WRONG_FORMAT);
	}

	@Test
	public void test_BookingDateBeforeGroupAssignment_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setBookingdate(DateUtil.getGMTDate("1970-01-01"));

		this.testError(transport, null, null, null, ErrorCode.BOOKINGDATE_OUTSIDE_GROUP_ASSIGNMENT, ErrorCode.CAPITALSOURCE_USE_OUT_OF_VALIDITY,
				ErrorCode.CONTRACTPARTNER_NO_LONGER_VALID);
	}

	@Test
	public void test_BookingDateAfterGroupAssignment_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setBookingdate(DateUtil.getGMTDate("2600-01-01"));

		this.testError(transport, null, null, null, ErrorCode.BOOKINGDATE_OUTSIDE_GROUP_ASSIGNMENT);
	}

	@Test
	public void test_notExistingPostingAccount_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setPostingaccountid(PostingAccountTransportBuilder.NON_EXISTING_ID);

		this.testError(transport, null, null, null, ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED);
	}

	@Test
	public void test_oneNewMoneyflowWithoutInvoiceDate_CreationDone() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.NEXT_ID);

		final CreateMoneyflowsRequest request = new CreateMoneyflowsRequest();

		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setInvoicedate(null);

		Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);

		Assert.assertNull(moneyflow);
		request.setMoneyflowTransports(Arrays.asList(transport));

		final CreateMoneyflowsResponse expected = this.getCompleteResponseObject();

		final CreateMoneyflowsResponse actual = super.callUsecaseWithContent("", this.method, request, false, CreateMoneyflowsResponse.class);

		Assert.assertEquals(expected, actual);

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
	public void test_twoNewMoneyflows_CreationDone() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.NEXT_ID);

		final CreateMoneyflowsRequest request = new CreateMoneyflowsRequest();

		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();

		Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);

		Assert.assertNull(moneyflow);
		request.setMoneyflowTransports(Arrays.asList(transport, transport));

		final CreateMoneyflowsResponse expected = this.getCompleteResponseObject();

		final CreateMoneyflowsResponse actual = super.callUsecaseWithContent("", this.method, request, false, CreateMoneyflowsResponse.class);

		Assert.assertEquals(expected, actual);

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

		moneyflow = this.moneyflowService.getMoneyflowById(userId, new MoneyflowID(MoneyflowTransportBuilder.NEXT_ID + 1));

		Assert.assertNotNull(moneyflow);
		Assert.assertEquals(transport.getAmount(), moneyflow.getAmount());
		Assert.assertEquals(transport.getCapitalsourceid(), moneyflow.getCapitalsource().getId().getId());
		Assert.assertEquals(transport.getComment(), moneyflow.getComment());
		Assert.assertEquals(transport.getContractpartnerid(), moneyflow.getContractpartner().getId().getId());
		Assert.assertEquals(Short.valueOf("1").equals(transport.getPrivat()), moneyflow.isPrivat());
		Assert.assertEquals(transport.getBookingdate().toLocalDate(), moneyflow.getBookingDate());
		Assert.assertEquals(transport.getInvoicedate().toLocalDate(), moneyflow.getInvoiceDate());
		Assert.assertEquals(transport.getPostingaccountid(), moneyflow.getPostingAccount().getId().getId());
	}

	@Test
	public void test_preDefMoneyflowSelected_LastUsedUpdatedAndOnceAMonthRespected() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final PreDefMoneyflowID preDefMoneyflowId = new PreDefMoneyflowID(PreDefMoneyflowTransportBuilder.PRE_DEF_MONEYFLOW1_ID);

		final CreateMoneyflowsRequest request = new CreateMoneyflowsRequest();

		PreDefMoneyflow preDefMoneyflow = this.preDefMoneyflowService.getPreDefMoneyflowById(userId, preDefMoneyflowId);

		Assert.assertNotNull(preDefMoneyflow);
		Assert.assertEquals(true, preDefMoneyflow.isOnceAMonth());
		Assert.assertEquals(null, preDefMoneyflow.getLastUsedDate());

		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		request.setMoneyflowTransports(Arrays.asList(transport));
		request.setUsedPreDefMoneyflowIds(Arrays.asList(preDefMoneyflowId.getId()));

		final CreateMoneyflowsResponse expected = this.getCompleteResponseObject();
		final List<PreDefMoneyflowTransport> preDefMoneyflowTransports = new ArrayList<>();
		preDefMoneyflowTransports.add(new PreDefMoneyflowTransportBuilder().forPreDefMoneyflow3().build());
		expected.setPreDefMoneyflowTransports(preDefMoneyflowTransports);

		final CreateMoneyflowsResponse actual = super.callUsecaseWithContent("", this.method, request, false, CreateMoneyflowsResponse.class);

		// PreDefMoneyflow ID is no longer included as it was already used "this month"
		Assert.assertEquals(expected, actual);

		preDefMoneyflow = this.preDefMoneyflowService.getPreDefMoneyflowById(userId, preDefMoneyflowId);
		Assert.assertNotNull(preDefMoneyflow);
		Assert.assertEquals(true, preDefMoneyflow.isOnceAMonth());
		// LastUsed is set to "today"
		Assert.assertEquals(LocalDate.now(), preDefMoneyflow.getLastUsedDate());
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

		this.testError(transport, capitalsourceTransports, null, preDefMoneyflowTransports, ErrorCode.CAPITALSOURCE_DOES_NOT_EXIST);
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

		final CreateMoneyflowsRequest request = new CreateMoneyflowsRequest();

		request.setMoneyflowTransports(Arrays.asList(transport));

		super.callUsecaseWithContent("", this.method, request, false, CreateMoneyflowsResponse.class);
	}
}