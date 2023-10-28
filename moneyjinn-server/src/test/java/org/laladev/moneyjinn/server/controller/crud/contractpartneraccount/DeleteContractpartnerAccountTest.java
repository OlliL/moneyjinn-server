
package org.laladev.moneyjinn.server.controller.crud.contractpartneraccount;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.ContractpartnerAccount;
import org.laladev.moneyjinn.model.ContractpartnerAccountID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.server.builder.ContractpartnerAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.service.api.IContractpartnerAccountService;

import jakarta.inject.Inject;

class DeleteContractpartnerAccountTest extends AbstractContractpartnerAccountTest {
	@Inject
	private IContractpartnerAccountService contractpartnerAccountService;

	@Override
	protected void loadMethod() {
		super.getMock().delete(null);
	}

	@Test
	void test_regularContractpartnerAccountNoData_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(
				ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT2_ID);
		ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
				.getContractpartnerAccountById(userId, contractpartnerAccountId);
		Assertions.assertNotNull(contractpartnerAccount);

		super.callUsecaseExpect204WithUriVariables(ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT2_ID);

		contractpartnerAccount = this.contractpartnerAccountService.getContractpartnerAccountById(userId,
				contractpartnerAccountId);
		Assertions.assertNull(contractpartnerAccount);
	}

	@Test
	void test_nonExistingContractpartnerAccount_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(
				ContractpartnerAccountTransportBuilder.NON_EXISTING_ID);
		ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
				.getContractpartnerAccountById(userId, contractpartnerAccountId);
		Assertions.assertNull(contractpartnerAccount);

		super.callUsecaseExpect204WithUriVariables(ContractpartnerAccountTransportBuilder.NON_EXISTING_ID);

		contractpartnerAccount = this.contractpartnerAccountService.getContractpartnerAccountById(userId,
				contractpartnerAccountId);
		Assertions.assertNull(contractpartnerAccount);
	}

	@Test
	void test_ContractpartnerAccountFromSameGroupButNotMe_SuccessfullNoContent() throws Exception {
		super.setUsername(UserTransportBuilder.USER3_NAME);
		super.setPassword(UserTransportBuilder.USER3_PASSWORD);
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(
				ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID);
		// Uncommented because of Cache eviction does not work right now for differing
		// users
		// ContractpartnerAccount contractpartnerAccount =
		// this.contractpartnerAccountService
		// .getContractpartnerAccountById(userId, contractpartnerAccountId);
		//
		// Assertions.assertNotNull(contractpartnerAccount);

		super.callUsecaseExpect204WithUriVariables(ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID);

		final ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
				.getContractpartnerAccountById(userId, contractpartnerAccountId);
		Assertions.assertNull(contractpartnerAccount);
	}

	@Test
	void test_ContractpartnerAccountFromDifferentGroup_notSuccessfull() throws Exception {
		super.setUsername(UserTransportBuilder.ADMIN_NAME);
		super.setPassword(UserTransportBuilder.ADMIN_PASSWORD);
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(
				ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID);
		// Uncommented because of Cache eviction does not work right now for differing
		// users
		// ContractpartnerAccount contractpartnerAccount =
		// this.contractpartnerAccountService
		// .getContractpartnerAccountById(userId, contractpartnerAccountId);
		//
		// Assertions.assertNotNull(contractpartnerAccount);

		super.callUsecaseExpect204WithUriVariables(ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID);

		final ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
				.getContractpartnerAccountById(userId, contractpartnerAccountId);
		Assertions.assertNotNull(contractpartnerAccount);
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403WithUriVariables(ContractpartnerAccountTransportBuilder.NON_EXISTING_ID);
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		super.callUsecaseExpect204WithUriVariables(ContractpartnerAccountTransportBuilder.NON_EXISTING_ID);
	}
}