
package org.laladev.moneyjinn.server.controller.crud.contractpartner;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.Contractpartner;
import org.laladev.moneyjinn.model.ContractpartnerAccount;
import org.laladev.moneyjinn.model.ContractpartnerID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.model.ErrorResponse;
import org.laladev.moneyjinn.service.api.IContractpartnerAccountService;
import org.laladev.moneyjinn.service.api.IContractpartnerService;

import jakarta.inject.Inject;

class DeleteContractpartnerTest extends AbstractContractpartnerTest {
	@Inject
	private IContractpartnerService contractpartnerService;
	@Inject
	private IContractpartnerAccountService contractpartnerAccountService;

	@Override
	protected void loadMethod() {
		super.getMock().delete(null);
	}

	@Test
	void test_regularContractpartnerWitPreDefMoneyflows_ErrorResponse() throws Exception {
		super.setUsername(UserTransportBuilder.USER3_NAME);
		super.setPassword(UserTransportBuilder.USER3_PASSWORD);
		final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(
				ContractpartnerTransportBuilder.CONTRACTPARTNER3_ID);
		final ErrorResponse expected = new ErrorResponse();
		expected.setCode(ErrorCode.CONTRACTPARTNER_IN_USE.getErrorCode());
		expected.setMessage("You may not delete a contractual partner who is still referenced by a flow of money!");
		Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);
		Assertions.assertNotNull(contractpartner);
		final ErrorResponse actual = super.callUsecaseExpect400(ErrorResponse.class,
				ContractpartnerTransportBuilder.CONTRACTPARTNER3_ID);

		contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);
		Assertions.assertNotNull(contractpartner);
		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_regularContractpartnerNoData_SuccessfullNoContent() throws Exception {
		super.setUsername(UserTransportBuilder.USER3_NAME);
		super.setPassword(UserTransportBuilder.USER3_PASSWORD);
		final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(
				ContractpartnerTransportBuilder.CONTRACTPARTNER4_ID);
		Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);
		Assertions.assertNotNull(contractpartner);

		super.callUsecaseExpect204WithUriVariables(ContractpartnerTransportBuilder.CONTRACTPARTNER4_ID);

		contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);
		Assertions.assertNull(contractpartner);
	}

	@Test
	void test_regularContractpartnerWith1Account_AccountsDeleted() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(
				ContractpartnerTransportBuilder.CONTRACTPARTNER4_ID);
		Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);
		Assertions.assertNotNull(contractpartner);
		List<ContractpartnerAccount> contractpartnerAccounts = this.contractpartnerAccountService
				.getContractpartnerAccounts(userId, contractpartnerId);
		Assertions.assertEquals(1, contractpartnerAccounts.size());

		super.callUsecaseExpect204WithUriVariables(ContractpartnerTransportBuilder.CONTRACTPARTNER4_ID);

		contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);
		Assertions.assertNull(contractpartner);
		contractpartnerAccounts = this.contractpartnerAccountService.getContractpartnerAccounts(userId,
				contractpartnerId);
		Assertions.assertEquals(0, contractpartnerAccounts.size());
	}

	@Test
	void test_regularContractpartnerWith2Accounts_AccountsDeleted() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(
				ContractpartnerTransportBuilder.CONTRACTPARTNER4_ID);
		Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);
		Assertions.assertNotNull(contractpartner);
		List<ContractpartnerAccount> contractpartnerAccounts = this.contractpartnerAccountService
				.getContractpartnerAccounts(userId, contractpartnerId);
		final ContractpartnerAccount contractpartnerAccount = contractpartnerAccounts.getFirst();
		contractpartnerAccount.getBankAccount().setAccountNumber("TEST12345");
		this.contractpartnerAccountService.createContractpartnerAccount(userId, contractpartnerAccount);
		contractpartnerAccounts = this.contractpartnerAccountService.getContractpartnerAccounts(userId,
				contractpartnerId);
		Assertions.assertEquals(2, contractpartnerAccounts.size());

		super.callUsecaseExpect204WithUriVariables(ContractpartnerTransportBuilder.CONTRACTPARTNER4_ID);

		contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);
		Assertions.assertNull(contractpartner);
		contractpartnerAccounts = this.contractpartnerAccountService.getContractpartnerAccounts(userId,
				contractpartnerId);
		Assertions.assertEquals(0, contractpartnerAccounts.size());
	}

	@Test
	void test_nonExistingContractpartner_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(
				ContractpartnerTransportBuilder.NON_EXISTING_ID);
		Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);
		Assertions.assertNull(contractpartner);

		super.callUsecaseExpect204WithUriVariables(ContractpartnerTransportBuilder.NON_EXISTING_ID);

		contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);
		Assertions.assertNull(contractpartner);
	}

	@Test
	void test_regularContractpartnerWithData_ErrorResponse() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(
				ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);
		final ErrorResponse expected = new ErrorResponse();
		expected.setCode(ErrorCode.CONTRACTPARTNER_IN_USE.getErrorCode());
		expected.setMessage("You may not delete a contractual partner who is still referenced by a flow of money!");
		Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);
		Assertions.assertNotNull(contractpartner);

		final ErrorResponse actual = super.callUsecaseExpect400(ErrorResponse.class,
				ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);

		contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);
		Assertions.assertNotNull(contractpartner);
		Assertions.assertEquals(expected, actual);
		final List<ContractpartnerAccount> contractpartnerAccounts = this.contractpartnerAccountService
				.getContractpartnerAccounts(userId, contractpartnerId);
		Assertions.assertEquals(2, contractpartnerAccounts.size());
	}

	@Test
	void test_ContractpartnerFromSameGroupButNotMe_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(
				ContractpartnerTransportBuilder.CONTRACTPARTNER4_ID);
		Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);
		Assertions.assertNotNull(contractpartner);

		super.callUsecaseExpect204WithUriVariables(ContractpartnerTransportBuilder.CONTRACTPARTNER4_ID);

		contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);
		Assertions.assertNull(contractpartner);
	}

	@Test
	void test_ContractpartnerFromDifferentGroup_notSuccessfull() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.ADMIN_ID);
		final ContractpartnerID contractpartnerId = new ContractpartnerID(
				ContractpartnerTransportBuilder.CONTRACTPARTNER5_ID);
		Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);
		Assertions.assertNotNull(contractpartner);

		super.callUsecaseExpect204WithUriVariables(ContractpartnerTransportBuilder.CONTRACTPARTNER5_ID);

		contractpartner = this.contractpartnerService.getContractpartnerById(userId, contractpartnerId);
		Assertions.assertNotNull(contractpartner);
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403WithUriVariables(ContractpartnerTransportBuilder.NON_EXISTING_ID);
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		super.callUsecaseExpect204WithUriVariables(ContractpartnerTransportBuilder.NON_EXISTING_ID);
	}
}