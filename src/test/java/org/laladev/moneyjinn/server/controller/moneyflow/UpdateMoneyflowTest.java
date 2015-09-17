package org.laladev.moneyjinn.server.controller.moneyflow;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.businesslogic.model.Moneyflow;
import org.laladev.moneyjinn.businesslogic.model.MoneyflowID;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.service.api.IAccessRelationService;
import org.laladev.moneyjinn.businesslogic.service.api.IMoneyflowService;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.moneyflow.UpdateMoneyflowRequest;
import org.laladev.moneyjinn.core.rest.model.moneyflow.UpdateMoneyflowResponse;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ContractpartnerTransport;
import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowTransport;
import org.laladev.moneyjinn.core.rest.model.transport.PostingAccountTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.builder.DateUtil;
import org.laladev.moneyjinn.server.builder.MoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.PostingAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;

public class UpdateMoneyflowTest extends AbstractControllerTest {

	@Inject
	IMoneyflowService moneyflowService;

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

	private void testError(final MoneyflowTransport transport, final ErrorCode errorCode,
			final List<CapitalsourceTransport> overrideCapitalsources,
			final List<ContractpartnerTransport> overrideContractpartner) throws Exception {
		final UpdateMoneyflowRequest request = new UpdateMoneyflowRequest();

		request.setMoneyflowTransport(transport);

		final List<ValidationItemTransport> validationItems = new ArrayList<>();
		validationItems.add(new ValidationItemTransportBuilder().withKey(transport.getId().intValue())
				.withError(errorCode.getErrorCode()).build());

		final UpdateMoneyflowResponse expected = new UpdateMoneyflowResponse();
		final List<PostingAccountTransport> postingAccountTransports = new ArrayList<>();
		postingAccountTransports.add(new PostingAccountTransportBuilder().forPostingAccount1().build());
		postingAccountTransports.add(new PostingAccountTransportBuilder().forPostingAccount2().build());
		postingAccountTransports.add(new PostingAccountTransportBuilder().forPostingAccount3().build());
		expected.setPostingAccountTransports(postingAccountTransports);

		final List<ContractpartnerTransport> contractpartnerTransports = new ArrayList<>();
		contractpartnerTransports.add(new ContractpartnerTransportBuilder().forContractpartner1().build());
		contractpartnerTransports.add(new ContractpartnerTransportBuilder().forContractpartner2().build());
		contractpartnerTransports.add(new ContractpartnerTransportBuilder().forContractpartner3().build());
		contractpartnerTransports.add(new ContractpartnerTransportBuilder().forContractpartner4().build());
		expected.setContractpartnerTransports(contractpartnerTransports);

		final List<CapitalsourceTransport> capitalsourceTransports = new ArrayList<>();
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource1().build());
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource2().build());
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource3().build());
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource4().build());
		expected.setCapitalsourceTransports(capitalsourceTransports);

		expected.setValidationItemTransports(validationItems);
		expected.setResult(Boolean.FALSE);

		if (overrideCapitalsources != null) {
			expected.setCapitalsourceTransports(overrideCapitalsources);
		}
		if (overrideContractpartner != null) {
			expected.setContractpartnerTransports(overrideContractpartner);
		}

		final UpdateMoneyflowResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				UpdateMoneyflowResponse.class);

		Assert.assertEquals(expected.getErrorResponse(), actual.getErrorResponse());
		Assert.assertEquals(expected.getResult(), actual.getResult());
		Assert.assertEquals(expected.getValidationItemTransports(), actual.getValidationItemTransports());
		Assert.assertEquals(expected.getPostingAccountTransports(), actual.getPostingAccountTransports());
		Assert.assertEquals(expected.getCapitalsourceTransports(), actual.getCapitalsourceTransports());
		Assert.assertEquals(expected.getContractpartnerTransports(), actual.getContractpartnerTransports());
		Assert.assertEquals(expected, actual);

	}

	@Test
	public void test_emptyComment_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		transport.setComment("");

		this.testError(transport, ErrorCode.COMMENT_IS_NOT_SET, null, null);
	}

	@Test
	public void test_nullComment_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		transport.setComment(null);

		this.testError(transport, ErrorCode.COMMENT_IS_NOT_SET, null, null);
	}

	@Test
	public void test_nullCapitalsource_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		transport.setCapitalsourceid(null);

		this.testError(transport, ErrorCode.CAPITALSOURCE_IS_NOT_SET, null, null);
	}

	@Test
	public void test_notExistingCapitalsource_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		transport.setCapitalsourceid(CapitalsourceTransportBuilder.NON_EXISTING_ID);

		this.testError(transport, ErrorCode.CAPITALSOURCE_DOES_NOT_EXIST, null, null);
	}

	@Test
	public void test_noLongerValidCapitalsource_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		transport.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE3_ID);

		this.testError(transport, ErrorCode.CAPITALSOURCE_USE_OUT_OF_VALIDITY, null, null);
	}

	@Test
	public void test_nullContractpartner_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		transport.setContractpartnerid(null);

		this.testError(transport, ErrorCode.CONTRACTPARTNER_IS_NOT_SET, null, null);
	}

	@Test
	public void test_notExistingContractpartner_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		transport.setContractpartnerid(ContractpartnerTransportBuilder.NON_EXISTING_ID);

		this.testError(transport, ErrorCode.CONTRACTPARTNER_DOES_NOT_EXIST, null, null);
	}

	@Test
	public void test_noLongerValidContractpartner_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		transport.setContractpartnerid(ContractpartnerTransportBuilder.CONTRACTPARTNER3_ID);

		this.testError(transport, ErrorCode.CONTRACTPARTNER_NO_LONGER_VALID, null, null);
	}

	@Test
	public void test_nullAmount_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		transport.setAmount(null);

		this.testError(transport, ErrorCode.AMOUNT_IS_ZERO, null, null);
	}

	@Test
	public void test_zeroAmount_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		transport.setAmount(BigDecimal.ZERO);

		this.testError(transport, ErrorCode.AMOUNT_IS_ZERO, null, null);
	}

	// make sure it 0 is compared with compareTo not with equals
	@Test
	public void test_0_00Amount_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		transport.setAmount(new BigDecimal("0.00000"));

		this.testError(transport, ErrorCode.AMOUNT_IS_ZERO, null, null);
	}

	@Test
	public void test_nullPostingAccount_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		transport.setPostingaccountid(null);

		this.testError(transport, ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED, null, null);
	}

	@Test
	public void test_nullBookingDate_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		transport.setBookingdate(null);

		this.testError(transport, ErrorCode.BOOKINGDATE_IN_WRONG_FORMAT, null, null);
	}

	@Test
	public void test_BookingDateBeforeGroupAssignment_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		transport.setBookingdate(DateUtil.getGMTDate("1970-01-01"));

		this.testError(transport, ErrorCode.BOOKINGDATE_OUTSIDE_GROUP_ASSIGNMENT, null, null);
	}

	@Test
	public void test_BookingDateAfterGroupAssignment_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		transport.setBookingdate(DateUtil.getGMTDate("2600-01-01"));

		this.testError(transport, ErrorCode.BOOKINGDATE_OUTSIDE_GROUP_ASSIGNMENT, null, null);
	}

	@Test
	public void test_notExistingPostingAccount_Error() throws Exception {
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();
		transport.setPostingaccountid(PostingAccountTransportBuilder.NON_EXISTING_ID);

		this.testError(transport, ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED, null, null);
	}

	@Test
	public void test_notExisting_NothingHappend() throws Exception {
		final UpdateMoneyflowRequest request = new UpdateMoneyflowRequest();

		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forMoneyflow1().build();

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
	public void test_NotGroupUseableCapitalsourceUsed_Error() throws Exception {
		this.userName = UserTransportBuilder.USER3_NAME;
		this.userPassword = UserTransportBuilder.USER3_PASSWORD;
		final MoneyflowTransport transport = new MoneyflowTransportBuilder().forNewMoneyflow().build();
		transport.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID);

		final List<CapitalsourceTransport> capitalsourceTransports = new ArrayList<>();
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource3().build());
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource4().build());
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource2().build());

		this.testError(transport, ErrorCode.CAPITALSOURCE_DOES_NOT_EXIST, capitalsourceTransports, null);
	}

	@Test
	public void test_AuthorizationRequired_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("", this.method, false, ErrorResponse.class);
		Assert.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

}