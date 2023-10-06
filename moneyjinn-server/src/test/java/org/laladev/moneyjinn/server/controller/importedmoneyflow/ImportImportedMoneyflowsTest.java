
package org.laladev.moneyjinn.server.controller.importedmoneyflow;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.Contractpartner;
import org.laladev.moneyjinn.model.ContractpartnerAccount;
import org.laladev.moneyjinn.model.ContractpartnerID;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflow;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowStatus;
import org.laladev.moneyjinn.model.moneyflow.Moneyflow;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowID;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.ImportedMoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.MoneyflowSplitEntryTransportBuilder;
import org.laladev.moneyjinn.server.builder.MoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.PostingAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.controller.api.ImportedMoneyflowControllerApi;
import org.laladev.moneyjinn.server.model.ImportImportedMoneyflowRequest;
import org.laladev.moneyjinn.server.model.ImportedMoneyflowTransport;
import org.laladev.moneyjinn.server.model.MoneyflowSplitEntryTransport;
import org.laladev.moneyjinn.server.model.ValidationItemTransport;
import org.laladev.moneyjinn.server.model.ValidationResponse;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.IContractpartnerAccountService;
import org.laladev.moneyjinn.service.api.IContractpartnerService;
import org.laladev.moneyjinn.service.api.IImportedMoneyflowService;
import org.laladev.moneyjinn.service.api.IMoneyflowService;
import org.springframework.test.context.jdbc.Sql;

import jakarta.inject.Inject;

class ImportImportedMoneyflowsTest extends AbstractControllerTest {
	@Inject
	private IImportedMoneyflowService importedMoneyflowService;
	@Inject
	private IMoneyflowService moneyflowService;
	@Inject
	private ICapitalsourceService capitalsourceService;
	@Inject
	private IContractpartnerService contractpartnerService;
	@Inject
	private IContractpartnerAccountService contractpartnerAccountService;

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
	protected void loadMethod() {
		super.getMock(ImportedMoneyflowControllerApi.class).importImportedMoneyflows(null);
	}

	private void testError(final ImportedMoneyflowTransport transport, final ErrorCode... errorCodes) throws Exception {
		this.testError(transport, new ArrayList<>(), errorCodes);
	}

	private void testError(final ImportedMoneyflowTransport transport,
			final List<MoneyflowSplitEntryTransport> moneyflowSplitEntryTransports, final ErrorCode... errorCodes)
			throws Exception {
		final ImportImportedMoneyflowRequest request = new ImportImportedMoneyflowRequest();
		request.setImportedMoneyflowTransport(transport);
		request.setInsertMoneyflowSplitEntryTransports(moneyflowSplitEntryTransports);
		final ValidationResponse expected = new ValidationResponse();
		expected.setResult(Boolean.FALSE);
		final List<ValidationItemTransport> validationItems = new ArrayList<>();
		for (final ErrorCode errorCode : errorCodes) {
			validationItems.add(new ValidationItemTransportBuilder()
					.withKey(transport.getId() == null ? null : transport.getId().toString())
					.withError(errorCode.getErrorCode()).build());
		}
		expected.setValidationItemTransports(validationItems);

		final ValidationResponse actual = super.callUsecaseExpect422(request, ValidationResponse.class);

		Assertions.assertEquals(expected.getResult(), actual.getResult());
		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_standardRequest_Successfull() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final List<CapitalsourceID> capitalsourceIds = Arrays
				.asList(new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID));
		final ImportImportedMoneyflowRequest request = new ImportImportedMoneyflowRequest();
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder()
				.forImportedMoneyflow1ToImport().build();
		request.setImportedMoneyflowTransport(transport);
		List<ImportedMoneyflow> importedMoneyflows = this.importedMoneyflowService
				.getAllImportedMoneyflowsByCapitalsourceIds(userId, capitalsourceIds, ImportedMoneyflowStatus.CREATED);
		Assertions.assertNotNull(importedMoneyflows);
		final int sizeBeforeDeleteInStateCreated = importedMoneyflows.size();
		importedMoneyflows = this.importedMoneyflowService.getAllImportedMoneyflowsByCapitalsourceIds(userId,
				capitalsourceIds, ImportedMoneyflowStatus.PROCESSED);
		Assertions.assertNotNull(importedMoneyflows);
		final int sizeBeforeDeleteInStateProcessed = importedMoneyflows.size();
		importedMoneyflows = this.importedMoneyflowService.getAllImportedMoneyflowsByCapitalsourceIds(userId,
				capitalsourceIds, null);
		Assertions.assertNotNull(importedMoneyflows);
		final int sizeBeforeDelete = importedMoneyflows.size();

		super.callUsecaseExpect204(request);

		final Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId,
				new MoneyflowID(MoneyflowTransportBuilder.NEXT_ID));
		Assertions.assertNotNull(moneyflow);
		Assertions.assertEquals(transport.getAmount(), moneyflow.getAmount());
		Assertions.assertFalse(moneyflow.isPrivat());
		importedMoneyflows = this.importedMoneyflowService.getAllImportedMoneyflowsByCapitalsourceIds(userId,
				capitalsourceIds, ImportedMoneyflowStatus.CREATED);
		Assertions.assertNotNull(importedMoneyflows);
		Assertions.assertEquals(sizeBeforeDeleteInStateCreated - 1, importedMoneyflows.size());
		importedMoneyflows = this.importedMoneyflowService.getAllImportedMoneyflowsByCapitalsourceIds(userId,
				capitalsourceIds, ImportedMoneyflowStatus.PROCESSED);
		Assertions.assertNotNull(importedMoneyflows);
		Assertions.assertEquals(sizeBeforeDeleteInStateProcessed + 1, importedMoneyflows.size());
		// No delete happend - it is only marked as "processed"
		importedMoneyflows = this.importedMoneyflowService.getAllImportedMoneyflowsByCapitalsourceIds(userId,
				capitalsourceIds, null);
		Assertions.assertNotNull(importedMoneyflows);
		Assertions.assertEquals(sizeBeforeDelete, importedMoneyflows.size());
	}

	@Test
	void test_privateMoneyflow_Successfull() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ImportImportedMoneyflowRequest request = new ImportImportedMoneyflowRequest();
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder()
				.forImportedMoneyflow1ToImport().withPrivat(Integer.valueOf("1")).build();
		request.setImportedMoneyflowTransport(transport);

		super.callUsecaseExpect204(request);

		final Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId,
				new MoneyflowID(MoneyflowTransportBuilder.NEXT_ID));
		Assertions.assertNotNull(moneyflow);
		Assertions.assertEquals(transport.getAmount(), moneyflow.getAmount());
		Assertions.assertTrue(moneyflow.isPrivat());
	}

	@Test
	void test_NonprivateMoneyflow_Successfull() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ImportImportedMoneyflowRequest request = new ImportImportedMoneyflowRequest();
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder()
				.forImportedMoneyflow1ToImport().withPrivat(Integer.valueOf("0")).build();
		request.setImportedMoneyflowTransport(transport);

		super.callUsecaseExpect204(request);

		final Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId,
				new MoneyflowID(MoneyflowTransportBuilder.NEXT_ID));
		Assertions.assertNotNull(moneyflow);
		Assertions.assertEquals(transport.getAmount(), moneyflow.getAmount());
		Assertions.assertFalse(moneyflow.isPrivat());
	}

	@Test
	void test_nullBankAccount_Successfull() throws Exception {
		this.testBankAccount(null, null);
	}

	@Test
	void test_emptyBankAccount_Successfull() throws Exception {
		this.testBankAccount("", "");
	}

	@Test
	void test_blankBankAccount_Successfull() throws Exception {
		this.testBankAccount("  ", "  ");
	}

	private void testBankAccount(final String accountNumber, final String bankCode) throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final List<CapitalsourceID> capitalsourceIds = Arrays
				.asList(new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID));
		final ImportImportedMoneyflowRequest request = new ImportImportedMoneyflowRequest();
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder()
				.forImportedMoneyflow1ToImport().build();
		transport.setBankCode(bankCode);
		transport.setAccountNumber(accountNumber);
		request.setImportedMoneyflowTransport(transport);
		List<ImportedMoneyflow> importedMoneyflows = this.importedMoneyflowService
				.getAllImportedMoneyflowsByCapitalsourceIds(userId, capitalsourceIds,
						ImportedMoneyflowStatus.PROCESSED);
		Assertions.assertNotNull(importedMoneyflows);
		final int sizeBeforeImportInStateProcessed = importedMoneyflows.size();

		super.callUsecaseExpect204(request);

		final Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId,
				new MoneyflowID(MoneyflowTransportBuilder.NEXT_ID));
		Assertions.assertNotNull(moneyflow);
		Assertions.assertEquals(transport.getAmount(), moneyflow.getAmount());
		// Also make sure the Imported Moneyflow gets marked as processed!
		importedMoneyflows = this.importedMoneyflowService.getAllImportedMoneyflowsByCapitalsourceIds(userId,
				capitalsourceIds, ImportedMoneyflowStatus.PROCESSED);
		Assertions.assertNotNull(importedMoneyflows);
		Assertions.assertEquals(sizeBeforeImportInStateProcessed + 1, importedMoneyflows.size());
	}

	@Test
	void test_contractpartnerAccountUnknown_accountCreated() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(
				ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);
		final ImportImportedMoneyflowRequest request = new ImportImportedMoneyflowRequest();
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder()
				.forImportedMoneyflow2ToImport().build();
		request.setImportedMoneyflowTransport(transport);
		final List<ContractpartnerAccount> contractpartnerAccountsBeforeInsert = this.contractpartnerAccountService
				.getContractpartnerAccounts(userId, contractpartnerId);

		super.callUsecaseExpect204(request);

		final Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId,
				new MoneyflowID(MoneyflowTransportBuilder.NEXT_ID));
		Assertions.assertNotNull(moneyflow);
		Assertions.assertEquals(transport.getAmount(), moneyflow.getAmount());
		final List<ContractpartnerAccount> contractpartnerAccountsAfterInsert = new ArrayList<>(
				this.contractpartnerAccountService.getContractpartnerAccounts(userId, contractpartnerId));
		contractpartnerAccountsAfterInsert.removeAll(contractpartnerAccountsBeforeInsert);
		Assertions.assertEquals(1, contractpartnerAccountsAfterInsert.size());
		Assertions.assertEquals(transport.getAccountNumber(),
				contractpartnerAccountsAfterInsert.get(0).getBankAccount().getAccountNumber());
		Assertions.assertEquals(transport.getBankCode(),
				contractpartnerAccountsAfterInsert.get(0).getBankAccount().getBankCode());
	}

	@Test
	void test_bankAccountOfUnimportableCapitalsourceIsContractpartner_counterBookingCreated() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ImportImportedMoneyflowRequest request = new ImportImportedMoneyflowRequest();
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder()
				.forImportedMoneyflow2ToImport().build();
		transport.setAccountNumber(CapitalsourceTransportBuilder.CAPITALSOURCE2_ACCOUNTNUMBER);
		transport.setBankCode(CapitalsourceTransportBuilder.CAPITALSOURCE2_BANKCODE);
		request.setImportedMoneyflowTransport(transport);

		super.callUsecaseExpect204(request);

		Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId,
				new MoneyflowID(MoneyflowTransportBuilder.NEXT_ID));
		Assertions.assertNotNull(moneyflow);
		Assertions.assertEquals(transport.getAmount(), moneyflow.getAmount());
		moneyflow = this.moneyflowService.getMoneyflowById(userId,
				new MoneyflowID(MoneyflowTransportBuilder.NEXT_ID + 1));
		Assertions.assertNotNull(moneyflow);
		Assertions.assertEquals(transport.getAmount().negate(), moneyflow.getAmount());
		Assertions.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID,
				moneyflow.getCapitalsource().getId().getId());
	}

	@Test
	void test_bankAccountOfOnlyBalanceImportAllowedCapitalsourceIsContractpartner_counterBookingCreated()
			throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ImportImportedMoneyflowRequest request = new ImportImportedMoneyflowRequest();
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder()
				.forImportedMoneyflow2ToImport().build();
		transport.setAccountNumber(CapitalsourceTransportBuilder.CAPITALSOURCE3_ACCOUNTNUMBER);
		transport.setBankCode(CapitalsourceTransportBuilder.CAPITALSOURCE3_BANKCODE);
		transport.setBookingdate(LocalDate.parse("2000-01-02"));
		transport.setInvoicedate(LocalDate.parse("2000-01-01"));
		request.setImportedMoneyflowTransport(transport);

		super.callUsecaseExpect204(request);

		Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId,
				new MoneyflowID(MoneyflowTransportBuilder.NEXT_ID));
		Assertions.assertNotNull(moneyflow);
		Assertions.assertEquals(transport.getAmount(), moneyflow.getAmount());
		moneyflow = this.moneyflowService.getMoneyflowById(userId,
				new MoneyflowID(MoneyflowTransportBuilder.NEXT_ID + 1));
		Assertions.assertNotNull(moneyflow);
		Assertions.assertEquals(transport.getAmount().negate(), moneyflow.getAmount());
		Assertions.assertEquals(CapitalsourceTransportBuilder.CAPITALSOURCE3_ID,
				moneyflow.getCapitalsource().getId().getId());
	}

	@Test
	void test_bankAccountOfImportableCapitalsourceIsContractpartner_counterBookingNotCreated() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ImportImportedMoneyflowRequest request = new ImportImportedMoneyflowRequest();
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder()
				.forImportedMoneyflow2ToImport().build();
		transport.setAccountNumber(CapitalsourceTransportBuilder.CAPITALSOURCE1_ACCOUNTNUMBER);
		transport.setBankCode(CapitalsourceTransportBuilder.CAPITALSOURCE1_BANKCODE);
		request.setImportedMoneyflowTransport(transport);

		super.callUsecaseExpect204(request);

		Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId,
				new MoneyflowID(MoneyflowTransportBuilder.NEXT_ID));
		Assertions.assertNotNull(moneyflow);
		Assertions.assertEquals(transport.getAmount(), moneyflow.getAmount());
		moneyflow = this.moneyflowService.getMoneyflowById(userId,
				new MoneyflowID(MoneyflowTransportBuilder.NEXT_ID + 1));
		Assertions.assertNull(moneyflow);
	}

	@Test
	void test_bankAccountOfOfOnlyBalanceImportAllowedButCreditCapitalsourceIsContractpartner_counterBookingNotCreated()
			throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ImportImportedMoneyflowRequest request = new ImportImportedMoneyflowRequest();
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder()
				.forImportedMoneyflow2ToImport().build();
		transport.setAccountNumber(CapitalsourceTransportBuilder.CAPITALSOURCE5_ACCOUNTNUMBER);
		transport.setBankCode(CapitalsourceTransportBuilder.CAPITALSOURCE5_BANKCODE);
		request.setImportedMoneyflowTransport(transport);

		super.callUsecaseExpect204(request);

		Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId,
				new MoneyflowID(MoneyflowTransportBuilder.NEXT_ID));
		Assertions.assertNotNull(moneyflow);
		Assertions.assertEquals(transport.getAmount(), moneyflow.getAmount());
		moneyflow = this.moneyflowService.getMoneyflowById(userId,
				new MoneyflowID(MoneyflowTransportBuilder.NEXT_ID + 1));
		Assertions.assertNull(moneyflow);
	}

	@Test
	void test_BookingdateAfterCapitalsourceValidity_ValidityAdjusted() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE3_ID);
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder()
				.forImportedMoneyflow1ToImport().build();
		transport.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE3_ID);
		final ImportImportedMoneyflowRequest request = new ImportImportedMoneyflowRequest();
		request.setImportedMoneyflowTransport(transport);
		final Capitalsource capitalsourceOrig = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);

		super.callUsecaseExpect204(request);

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
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder()
				.forImportedMoneyflow1ToImport().build();
		transport.setCapitalsourceid(capitalsourceId.getId());
		transport.setBookingdate(LocalDate.parse("2000-01-01"));
		final ImportImportedMoneyflowRequest request = new ImportImportedMoneyflowRequest();
		request.setImportedMoneyflowTransport(transport);
		final Capitalsource capitalsourceOrig = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);

		super.callUsecaseExpect204(request);

		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);
		Assertions.assertNotEquals(capitalsourceOrig.getValidFrom(), capitalsource.getValidFrom());
		Assertions.assertEquals(capitalsourceOrig.getValidTil(), capitalsource.getValidTil());
		Assertions.assertEquals(transport.getBookingdate(), capitalsource.getValidFrom());
	}

	@Test
	void test_BookingdateAfterContractpartnerValidity_ValidityAdjusted() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(
				ContractpartnerTransportBuilder.CONTRACTPARTNER3_ID);
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder()
				.forImportedMoneyflow1ToImport().build();
		transport.setBookingdate(LocalDate.parse("2011-01-01"));
		transport.setContractpartnerid(contractpartnerId.getId());
		final ImportImportedMoneyflowRequest request = new ImportImportedMoneyflowRequest();
		request.setImportedMoneyflowTransport(transport);
		final Contractpartner contractpartnerOrig = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerId);

		super.callUsecaseExpect204(request);

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
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder()
				.forImportedMoneyflow1ToImport().build();
		transport.setBookingdate(LocalDate.parse("2000-01-01"));
		transport.setContractpartnerid(contractpartnerId.getId());
		final ImportImportedMoneyflowRequest request = new ImportImportedMoneyflowRequest();
		request.setImportedMoneyflowTransport(transport);
		final Contractpartner contractpartnerOrig = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerId);

		super.callUsecaseExpect204(request);

		final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerId);
		Assertions.assertNotEquals(contractpartnerOrig.getValidFrom(), contractpartner.getValidFrom());
		Assertions.assertEquals(contractpartnerOrig.getValidTil(), contractpartner.getValidTil());
		Assertions.assertEquals(transport.getBookingdate(), contractpartner.getValidFrom());
	}

	@Test
	void test_emptyComment_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder()
				.forImportedMoneyflow1ToImport().build();
		transport.setComment("");
		this.testError(transport, ErrorCode.COMMENT_IS_NOT_SET);
	}

	@Test
	void test_nullComment_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder()
				.forImportedMoneyflow1ToImport().build();
		transport.setComment(null);
		this.testError(transport, ErrorCode.COMMENT_IS_NOT_SET);
	}

	@Test
	void test_nullCapitalsource_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder()
				.forImportedMoneyflow1ToImport().build();
		transport.setCapitalsourceid(null);
		this.testError(transport, ErrorCode.CAPITALSOURCE_IS_NOT_SET);
	}

	@Test
	void test_notExistingCapitalsource_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder()
				.forImportedMoneyflow1ToImport().build();
		transport.setCapitalsourceid(CapitalsourceTransportBuilder.NON_EXISTING_ID);
		this.testError(transport, ErrorCode.CAPITALSOURCE_DOES_NOT_EXIST);
	}

	@Test
	void test_nullContractpartner_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder()
				.forImportedMoneyflow1ToImport().build();
		transport.setContractpartnerid(null);
		this.testError(transport, ErrorCode.CONTRACTPARTNER_IS_NOT_SET);
	}

	@Test
	void test_notExistingContractpartner_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder()
				.forImportedMoneyflow1ToImport().build();
		transport.setContractpartnerid(ContractpartnerTransportBuilder.NON_EXISTING_ID);
		this.testError(transport, ErrorCode.CONTRACTPARTNER_DOES_NOT_EXIST);
	}

	@Test
	void test_nullAmount_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder()
				.forImportedMoneyflow1ToImport().build();
		transport.setAmount(null);
		this.testError(transport, ErrorCode.AMOUNT_IS_ZERO);
	}

	@Test
	void test_zeroAmount_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder()
				.forImportedMoneyflow1ToImport().build();
		transport.setAmount(BigDecimal.ZERO);
		this.testError(transport, ErrorCode.AMOUNT_IS_ZERO);
	}

	// make sure it 0 is compared with compareTo not with equals
	@Test
	void test_0_00Amount_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder()
				.forImportedMoneyflow1ToImport().build();
		transport.setAmount(new BigDecimal("0.00000"));
		this.testError(transport, ErrorCode.AMOUNT_IS_ZERO);
	}

	@Test
	void test_nullPostingAccount_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder()
				.forImportedMoneyflow1ToImport().build();
		transport.setPostingaccountid(null);
		this.testError(transport, ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED);
	}

	@Test
	void test_nullBookingDate_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder()
				.forImportedMoneyflow1ToImport().build();
		transport.setBookingdate(null);
		this.testError(transport, ErrorCode.BOOKINGDATE_IN_WRONG_FORMAT);
	}

	@Test
	void test_BookingDateBeforeGroupAssignment_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder()
				.forImportedMoneyflow1ToImport().build();
		transport.setBookingdate(LocalDate.parse("1970-01-01"));
		this.testError(transport, ErrorCode.BOOKINGDATE_OUTSIDE_GROUP_ASSIGNMENT,
				ErrorCode.CAPITALSOURCE_USE_OUT_OF_VALIDITY, ErrorCode.CONTRACTPARTNER_NO_LONGER_VALID);
	}

	@Test
	void test_BookingDateAfterGroupAssignment_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder()
				.forImportedMoneyflow1ToImport().build();
		transport.setBookingdate(LocalDate.parse("2600-01-01"));
		this.testError(transport, ErrorCode.BOOKINGDATE_OUTSIDE_GROUP_ASSIGNMENT);
	}

	@Test
	void test_notExistingPostingAccount_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder()
				.forImportedMoneyflow1ToImport().build();
		transport.setPostingaccountid(PostingAccountTransportBuilder.NON_EXISTING_ID);
		this.testError(transport, ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED);
	}

	private MoneyflowSplitEntryTransport getMseTransport2(final ImportedMoneyflowTransport transport) {
		return new MoneyflowSplitEntryTransportBuilder().withAmount(transport.getAmount().divide(new BigDecimal(2)))
				.withComment("comment2").withPostingaccountid(PostingAccountTransportBuilder.POSTING_ACCOUNT2_ID)
				.build();
	}

	private MoneyflowSplitEntryTransport getMseTransport1(final ImportedMoneyflowTransport transport) {
		return new MoneyflowSplitEntryTransportBuilder().withAmount(transport.getAmount().divide(new BigDecimal(2)))
				.withComment("comment1").withPostingaccountid(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID)
				.build();
	}

	@Test
	void test_SplitEntries_SumNotMatchingOverallAmount_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder()
				.forImportedMoneyflow1ToImport().build();
		final MoneyflowSplitEntryTransport mseTransport1 = this.getMseTransport1(transport);
		mseTransport1.setAmount(transport.getAmount());
		final MoneyflowSplitEntryTransport mseTransport2 = this.getMseTransport2(transport);
		this.testError(transport, Arrays.asList(mseTransport1, mseTransport2),
				ErrorCode.SPLIT_ENTRIES_AMOUNT_IS_NOT_EQUALS_MONEYFLOW_AMOUNT);
	}

	@Test
	void test_Splitentries_CommentAndPostingAccountForMainNotSpecified_TakenFromFirstSplitEntryBooking()
			throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.NEXT_ID);
		final ImportImportedMoneyflowRequest request = new ImportImportedMoneyflowRequest();
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder()
				.forImportedMoneyflow1ToImport().build();
		transport.setComment(null);
		transport.setPostingaccountid(null);
		request.setImportedMoneyflowTransport(transport);
		final MoneyflowSplitEntryTransport mseTransport1 = this.getMseTransport1(transport);
		final MoneyflowSplitEntryTransport mseTransport2 = this.getMseTransport2(transport);
		request.setInsertMoneyflowSplitEntryTransports(Arrays.asList(mseTransport1, mseTransport2));

		super.callUsecaseExpect204(request);

		final Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);
		Assertions.assertNotNull(moneyflow);
		Assertions.assertEquals(mseTransport1.getComment(), moneyflow.getComment());
		Assertions.assertEquals(mseTransport1.getPostingaccountid(), moneyflow.getPostingAccount().getId().getId());
	}

	@Test
	void test_SplitEntries_Insert_emptyComment_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder()
				.forImportedMoneyflow1ToImport().build();
		final MoneyflowSplitEntryTransport mseTransport1 = this.getMseTransport1(transport);
		mseTransport1.setComment("");
		final MoneyflowSplitEntryTransport mseTransport2 = this.getMseTransport2(transport);

		this.testError(transport, Arrays.asList(mseTransport1, mseTransport2), ErrorCode.COMMENT_IS_NOT_SET);
	}

	@Test
	void test_SplitEntries_Insert_zeroAmount_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder()
				.forImportedMoneyflow1ToImport().build();
		final MoneyflowSplitEntryTransport mseTransport1 = this.getMseTransport1(transport);
		mseTransport1.setAmount(BigDecimal.ZERO);
		final MoneyflowSplitEntryTransport mseTransport2 = this.getMseTransport2(transport);
		mseTransport2.setAmount(transport.getAmount());

		this.testError(transport, Arrays.asList(mseTransport1, mseTransport2), ErrorCode.AMOUNT_IS_ZERO);
	}

	// make sure it 0 is compared with compareTo not with equals
	@Test
	void test_SplitEntries_Insert_0_00Amount_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder()
				.forImportedMoneyflow1ToImport().build();
		final MoneyflowSplitEntryTransport mseTransport1 = this.getMseTransport1(transport);
		mseTransport1.setAmount(new BigDecimal("0.00000"));
		final MoneyflowSplitEntryTransport mseTransport2 = this.getMseTransport2(transport);
		mseTransport2.setAmount(transport.getAmount());

		this.testError(transport, Arrays.asList(mseTransport1, mseTransport2), ErrorCode.AMOUNT_IS_ZERO);
	}

	@Test
	void test_SplitEntries_Insert_nullComment_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder()
				.forImportedMoneyflow1ToImport().build();
		final MoneyflowSplitEntryTransport mseTransport1 = this.getMseTransport1(transport);
		mseTransport1.setComment(null);
		final MoneyflowSplitEntryTransport mseTransport2 = this.getMseTransport2(transport);

		this.testError(transport, Arrays.asList(mseTransport1, mseTransport2), ErrorCode.COMMENT_IS_NOT_SET);
	}

	@Test
	void test_SplitEntries_Insert_nullAmount_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder()
				.forImportedMoneyflow1ToImport().build();
		final MoneyflowSplitEntryTransport mseTransport1 = this.getMseTransport1(transport);
		mseTransport1.setAmount(null);
		final MoneyflowSplitEntryTransport mseTransport2 = this.getMseTransport2(transport);
		mseTransport2.setAmount(transport.getAmount());

		this.testError(transport, Arrays.asList(mseTransport1, mseTransport2), ErrorCode.AMOUNT_IS_ZERO);
	}

	@Test
	void test_SplitEntries_Insert_nullPostingAccount_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder()
				.forImportedMoneyflow1ToImport().build();
		final MoneyflowSplitEntryTransport mseTransport1 = this.getMseTransport1(transport);
		mseTransport1.setPostingaccountid(null);
		final MoneyflowSplitEntryTransport mseTransport2 = this.getMseTransport2(transport);
		// Hack because error is reported for the MoneyflowSplitEntry which has ID null
		// set
		this.testError(transport, Arrays.asList(mseTransport1, mseTransport2), ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED);
	}

	@Test
	void test_SplitEntries_Insert_notExistingPostingAccount_Error() throws Exception {
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder()
				.forImportedMoneyflow1ToImport().build();
		final MoneyflowSplitEntryTransport mseTransport1 = this.getMseTransport1(transport);
		mseTransport1.setPostingaccountid(PostingAccountTransportBuilder.NON_EXISTING_ID);
		final MoneyflowSplitEntryTransport mseTransport2 = this.getMseTransport2(transport);

		this.testError(transport, Arrays.asList(mseTransport1, mseTransport2), ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED);
	}

	@Test
	void test_AuthorizationRequired_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;

		super.callUsecaseExpect403(new ImportImportedMoneyflowRequest());
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	void test_emptyDatabase_noException() throws Exception {
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
		final ImportImportedMoneyflowRequest request = new ImportImportedMoneyflowRequest();
		final ImportedMoneyflowTransport transport = new ImportedMoneyflowTransportBuilder()
				.forImportedMoneyflow1ToImport().build();
		request.setImportedMoneyflowTransport(transport);

		super.callUsecaseExpect204(request);
	}
}