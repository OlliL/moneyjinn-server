package org.laladev.moneyjinn.server.controller.importedmoneyflow;

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
import org.laladev.moneyjinn.core.rest.model.importedmoneyflow.ImportImportedMoneyflowRequest;
import org.laladev.moneyjinn.core.rest.model.transport.ImportedMoneyflowTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.model.ContractpartnerAccount;
import org.laladev.moneyjinn.model.ContractpartnerID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflow;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowStatus;
import org.laladev.moneyjinn.model.moneyflow.Moneyflow;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowID;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.builder.DateUtil;
import org.laladev.moneyjinn.server.builder.ImportedMoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.MoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.PostingAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.IContractpartnerAccountService;
import org.laladev.moneyjinn.service.api.IImportedMoneyflowService;
import org.laladev.moneyjinn.service.api.IMoneyflowService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class ImportImportedMoneyflowsTest extends AbstractControllerTest {

	@Inject
	IImportedMoneyflowService importedMoneyflowService;

	@Inject
	IMoneyflowService moneyflowService;

	@Inject
	ICapitalsourceService capitalsourceService;

	@Inject
	IContractpartnerAccountService contractpartnerAccountService;

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

	private void testError(final ImportedMoneyflowTransport transport, final ErrorCode... errorCodes) throws Exception {
		final ImportImportedMoneyflowRequest request = new ImportImportedMoneyflowRequest();

		request.setImportedMoneyflowTransports(Arrays.asList(transport));

		final ValidationResponse expected = new ValidationResponse();
		expected.setResult(Boolean.FALSE);

		final List<ValidationItemTransport> validationItems = new ArrayList<>();
		for (final ErrorCode errorCode : errorCodes) {
			validationItems.add(new ValidationItemTransportBuilder().withKey(transport.getId().intValue()).withError(errorCode.getErrorCode()).build());
		}
		expected.setValidationItemTransports(validationItems);

		final ValidationResponse actual = super.callUsecaseWithContent("", this.method, request, false, ValidationResponse.class);

		Assert.assertEquals(expected.getErrorResponse(), actual.getErrorResponse());
		Assert.assertEquals(expected.getResult(), actual.getResult());
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_standardRequest_Successfull() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final List<CapitalsourceID> capitalsourceIds = Arrays.asList(new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID));

		final ImportImportedMoneyflowRequest request = new ImportImportedMoneyflowRequest();

		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder().forImportedMoneyflow1ToImport().build();

		request.setImportedMoneyflowTransports(Arrays.asList(transport));

		List<ImportedMoneyflow> importedMoneyflows = this.importedMoneyflowService.getAllImportedMoneyflowsByCapitalsourceIds(userId, capitalsourceIds,
				ImportedMoneyflowStatus.CREATED);
		Assert.assertNotNull(importedMoneyflows);
		final int sizeBeforeDeleteInStateCreated = importedMoneyflows.size();
		importedMoneyflows = this.importedMoneyflowService.getAllImportedMoneyflowsByCapitalsourceIds(userId, capitalsourceIds,
				ImportedMoneyflowStatus.PROCESSED);
		Assert.assertNotNull(importedMoneyflows);
		final int sizeBeforeDeleteInStateProcessed = importedMoneyflows.size();
		importedMoneyflows = this.importedMoneyflowService.getAllImportedMoneyflowsByCapitalsourceIds(userId, capitalsourceIds, null);
		Assert.assertNotNull(importedMoneyflows);
		final int sizeBeforeDelete = importedMoneyflows.size();

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId, new MoneyflowID(MoneyflowTransportBuilder.NEXT_ID));

		Assert.assertNotNull(moneyflow);
		Assert.assertEquals(transport.getAmount(), moneyflow.getAmount());
		Assert.assertFalse(moneyflow.isPrivat());

		importedMoneyflows = this.importedMoneyflowService.getAllImportedMoneyflowsByCapitalsourceIds(userId, capitalsourceIds,
				ImportedMoneyflowStatus.CREATED);
		Assert.assertNotNull(importedMoneyflows);
		Assert.assertEquals(sizeBeforeDeleteInStateCreated - 1, importedMoneyflows.size());

		importedMoneyflows = this.importedMoneyflowService.getAllImportedMoneyflowsByCapitalsourceIds(userId, capitalsourceIds,
				ImportedMoneyflowStatus.PROCESSED);
		Assert.assertNotNull(importedMoneyflows);
		Assert.assertEquals(sizeBeforeDeleteInStateProcessed + 1, importedMoneyflows.size());

		// No delete happend - it is only marked as "processed"
		importedMoneyflows = this.importedMoneyflowService.getAllImportedMoneyflowsByCapitalsourceIds(userId, capitalsourceIds, null);

		Assert.assertNotNull(importedMoneyflows);
		Assert.assertEquals(sizeBeforeDelete, importedMoneyflows.size());
	}

	@Test
	public void test_privateMoneyflow_Successfull() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ImportImportedMoneyflowRequest request = new ImportImportedMoneyflowRequest();

		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder().forImportedMoneyflow1ToImport().withPrivat(Short.valueOf("1"))
				.build();

		request.setImportedMoneyflowTransports(Arrays.asList(transport));

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId, new MoneyflowID(MoneyflowTransportBuilder.NEXT_ID));

		Assert.assertNotNull(moneyflow);
		Assert.assertEquals(transport.getAmount(), moneyflow.getAmount());
		Assert.assertTrue(moneyflow.isPrivat());

	}

	@Test
	public void test_NonprivateMoneyflow_Successfull() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ImportImportedMoneyflowRequest request = new ImportImportedMoneyflowRequest();

		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder().forImportedMoneyflow1ToImport().withPrivat(Short.valueOf("0"))
				.build();

		request.setImportedMoneyflowTransports(Arrays.asList(transport));

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId, new MoneyflowID(MoneyflowTransportBuilder.NEXT_ID));

		Assert.assertNotNull(moneyflow);
		Assert.assertEquals(transport.getAmount(), moneyflow.getAmount());
		Assert.assertFalse(moneyflow.isPrivat());

	}

	@Test
	public void test_emptyBankAccount_Successfull() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);

		final ImportImportedMoneyflowRequest request = new ImportImportedMoneyflowRequest();

		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder().forImportedMoneyflow1ToImport().build();
		transport.setBankCode(null);
		transport.setAccountNumber(null);

		request.setImportedMoneyflowTransports(Arrays.asList(transport));

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId, new MoneyflowID(MoneyflowTransportBuilder.NEXT_ID));

		Assert.assertNotNull(moneyflow);
		Assert.assertEquals(transport.getAmount(), moneyflow.getAmount());
	}

	@Test
	public void test_contractpartnerAccountUnknown_accountCreated() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);

		final ImportImportedMoneyflowRequest request = new ImportImportedMoneyflowRequest();

		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder().forImportedMoneyflow2ToImport().build();

		request.setImportedMoneyflowTransports(Arrays.asList(transport));

		final List<ContractpartnerAccount> contractpartnerAccountsBeforeInsert = this.contractpartnerAccountService.getContractpartnerAccounts(userId,
				contractpartnerId);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId, new MoneyflowID(MoneyflowTransportBuilder.NEXT_ID));

		Assert.assertNotNull(moneyflow);
		Assert.assertEquals(transport.getAmount(), moneyflow.getAmount());

		final List<ContractpartnerAccount> contractpartnerAccountsAfterInsert = this.contractpartnerAccountService.getContractpartnerAccounts(userId,
				contractpartnerId);

		contractpartnerAccountsAfterInsert.removeAll(contractpartnerAccountsBeforeInsert);

		Assert.assertEquals(1, contractpartnerAccountsAfterInsert.size());
		Assert.assertEquals(transport.getAccountNumber(), contractpartnerAccountsAfterInsert.get(0).getBankAccount().getAccountNumber());
		Assert.assertEquals(transport.getBankCode(), contractpartnerAccountsAfterInsert.get(0).getBankAccount().getBankCode());

	}

	@Test
	public void test_bankAccountOfUnimportableCapitalsourceIsContractpartner_counterBookingCreated() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ImportImportedMoneyflowRequest request = new ImportImportedMoneyflowRequest();

		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder().forImportedMoneyflow2ToImport().build();
		transport.setAccountNumber(CapitalsourceTransportBuilder.CAPITALSOURCE2_ACCOUNTNUMBER);
		transport.setBankCode(CapitalsourceTransportBuilder.CAPITALSOURCE2_BANKCODE);
		request.setImportedMoneyflowTransports(Arrays.asList(transport));

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId, new MoneyflowID(MoneyflowTransportBuilder.NEXT_ID));

		Assert.assertNotNull(moneyflow);
		Assert.assertEquals(transport.getAmount(), moneyflow.getAmount());

		moneyflow = this.moneyflowService.getMoneyflowById(userId, new MoneyflowID(MoneyflowTransportBuilder.NEXT_ID + 1));

		Assert.assertNotNull(moneyflow);
		Assert.assertEquals(transport.getAmount().negate(), moneyflow.getAmount());
		Assert.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID, moneyflow.getCapitalsource().getId().getId());

	}

	@Test
	public void test_bankAccountOfOnlyBalanceImportAllowedCapitalsourceIsContractpartner_counterBookingCreated() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ImportImportedMoneyflowRequest request = new ImportImportedMoneyflowRequest();

		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder().forImportedMoneyflow2ToImport().build();
		transport.setAccountNumber(CapitalsourceTransportBuilder.CAPITALSOURCE3_ACCOUNTNUMBER);
		transport.setBankCode(CapitalsourceTransportBuilder.CAPITALSOURCE3_BANKCODE);
		transport.setBookingdate(DateUtil.getGMTDate("2000-01-02"));
		transport.setInvoicedate(DateUtil.getGMTDate("2000-01-01"));

		request.setImportedMoneyflowTransports(Arrays.asList(transport));

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId, new MoneyflowID(MoneyflowTransportBuilder.NEXT_ID));

		Assert.assertNotNull(moneyflow);
		Assert.assertEquals(transport.getAmount(), moneyflow.getAmount());

		moneyflow = this.moneyflowService.getMoneyflowById(userId, new MoneyflowID(MoneyflowTransportBuilder.NEXT_ID + 1));

		Assert.assertNotNull(moneyflow);
		Assert.assertEquals(transport.getAmount().negate(), moneyflow.getAmount());
		Assert.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE3_ID, moneyflow.getCapitalsource().getId().getId());

	}

	@Test
	public void test_bankAccountOfImportableCapitalsourceIsContractpartner_counterBookingNotCreated() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ImportImportedMoneyflowRequest request = new ImportImportedMoneyflowRequest();

		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder().forImportedMoneyflow2ToImport().build();
		transport.setAccountNumber(CapitalsourceTransportBuilder.CAPITALSOURCE1_ACCOUNTNUMBER);
		transport.setBankCode(CapitalsourceTransportBuilder.CAPITALSOURCE1_BANKCODE);
		request.setImportedMoneyflowTransports(Arrays.asList(transport));

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId, new MoneyflowID(MoneyflowTransportBuilder.NEXT_ID));

		Assert.assertNotNull(moneyflow);
		Assert.assertEquals(transport.getAmount(), moneyflow.getAmount());

		moneyflow = this.moneyflowService.getMoneyflowById(userId, new MoneyflowID(MoneyflowTransportBuilder.NEXT_ID + 1));

		Assert.assertNull(moneyflow);

	}

	@Test
	public void test_bankAccountOfOfOnlyBalanceImportAllowedButCreditCapitalsourceIsContractpartner_counterBookingNotCreated() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ImportImportedMoneyflowRequest request = new ImportImportedMoneyflowRequest();

		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder().forImportedMoneyflow2ToImport().build();
		transport.setAccountNumber(CapitalsourceTransportBuilder.CAPITALSOURCE5_ACCOUNTNUMBER);
		transport.setBankCode(CapitalsourceTransportBuilder.CAPITALSOURCE5_BANKCODE);
		request.setImportedMoneyflowTransports(Arrays.asList(transport));

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId, new MoneyflowID(MoneyflowTransportBuilder.NEXT_ID));

		Assert.assertNotNull(moneyflow);
		Assert.assertEquals(transport.getAmount(), moneyflow.getAmount());

		moneyflow = this.moneyflowService.getMoneyflowById(userId, new MoneyflowID(MoneyflowTransportBuilder.NEXT_ID + 1));

		Assert.assertNull(moneyflow);

	}

	@Test
	public void test_emptyComment_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder().forImportedMoneyflow1ToImport().build();
		transport.setComment("");

		this.testError(transport, ErrorCode.COMMENT_IS_NOT_SET);
	}

	@Test
	public void test_nullComment_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder().forImportedMoneyflow1ToImport().build();
		transport.setComment(null);

		this.testError(transport, ErrorCode.COMMENT_IS_NOT_SET);
	}

	@Test
	public void test_nullCapitalsource_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder().forImportedMoneyflow1ToImport().build();
		transport.setCapitalsourceid(null);

		this.testError(transport, ErrorCode.CAPITALSOURCE_IS_NOT_SET);
	}

	@Test
	public void test_notExistingCapitalsource_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder().forImportedMoneyflow1ToImport().build();
		transport.setCapitalsourceid(CapitalsourceTransportBuilder.NON_EXISTING_ID);

		this.testError(transport, ErrorCode.CAPITALSOURCE_DOES_NOT_EXIST);
	}

	@Test
	public void test_noLongerValidCapitalsource_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder().forImportedMoneyflow1ToImport().build();
		transport.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE3_ID);

		this.testError(transport, ErrorCode.CAPITALSOURCE_USE_OUT_OF_VALIDITY);
	}

	@Test
	public void test_nullContractpartner_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder().forImportedMoneyflow1ToImport().build();
		transport.setContractpartnerid(null);

		this.testError(transport, ErrorCode.CONTRACTPARTNER_IS_NOT_SET);
	}

	@Test
	public void test_notExistingContractpartner_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder().forImportedMoneyflow1ToImport().build();
		transport.setContractpartnerid(ContractpartnerTransportBuilder.NON_EXISTING_ID);

		this.testError(transport, ErrorCode.CONTRACTPARTNER_DOES_NOT_EXIST);
	}

	@Test
	public void test_noLongerValidContractpartner_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder().forImportedMoneyflow1ToImport().build();
		transport.setContractpartnerid(ContractpartnerTransportBuilder.CONTRACTPARTNER3_ID);
		transport.setBookingdate(DateUtil.getGMTDate("2011-01-01"));

		this.testError(transport, ErrorCode.CONTRACTPARTNER_NO_LONGER_VALID);
	}

	@Test
	public void test_nullAmount_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder().forImportedMoneyflow1ToImport().build();
		transport.setAmount(null);

		this.testError(transport, ErrorCode.AMOUNT_IS_ZERO);
	}

	@Test
	public void test_zeroAmount_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder().forImportedMoneyflow1ToImport().build();
		transport.setAmount(BigDecimal.ZERO);

		this.testError(transport, ErrorCode.AMOUNT_IS_ZERO);
	}

	// make sure it 0 is compared with compareTo not with equals
	@Test
	public void test_0_00Amount_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder().forImportedMoneyflow1ToImport().build();
		transport.setAmount(new BigDecimal("0.00000"));

		this.testError(transport, ErrorCode.AMOUNT_IS_ZERO);
	}

	@Test
	public void test_nullPostingAccount_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder().forImportedMoneyflow1ToImport().build();
		transport.setPostingaccountid(null);

		this.testError(transport, ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED);
	}

	@Test
	public void test_nullBookingDate_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder().forImportedMoneyflow1ToImport().build();
		transport.setBookingdate(null);

		this.testError(transport, ErrorCode.BOOKINGDATE_IN_WRONG_FORMAT);
	}

	@Test
	public void test_BookingDateBeforeGroupAssignment_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder().forImportedMoneyflow1ToImport().build();
		transport.setBookingdate(DateUtil.getGMTDate("1970-01-01"));

		this.testError(transport, ErrorCode.BOOKINGDATE_OUTSIDE_GROUP_ASSIGNMENT, ErrorCode.CAPITALSOURCE_USE_OUT_OF_VALIDITY,
				ErrorCode.CONTRACTPARTNER_NO_LONGER_VALID);
	}

	@Test
	public void test_BookingDateAfterGroupAssignment_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder().forImportedMoneyflow1ToImport().build();
		transport.setBookingdate(DateUtil.getGMTDate("2600-01-01"));

		this.testError(transport, ErrorCode.BOOKINGDATE_OUTSIDE_GROUP_ASSIGNMENT);
	}

	@Test
	public void test_notExistingPostingAccount_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder().forImportedMoneyflow1ToImport().build();
		transport.setPostingaccountid(PostingAccountTransportBuilder.NON_EXISTING_ID);

		this.testError(transport, ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED);
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
		final ImportImportedMoneyflowRequest request = new ImportImportedMoneyflowRequest();

		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder().forImportedMoneyflow1ToImport().build();
		request.setImportedMoneyflowTransports(Arrays.asList(transport));

		super.callUsecaseWithContent("", this.method, request, false, ValidationResponse.class);
	}

}