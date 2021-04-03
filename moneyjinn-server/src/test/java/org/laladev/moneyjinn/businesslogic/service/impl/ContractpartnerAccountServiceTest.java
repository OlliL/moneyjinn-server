package org.laladev.moneyjinn.businesslogic.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.AbstractTest;
import org.laladev.moneyjinn.model.ContractpartnerAccount;
import org.laladev.moneyjinn.model.ContractpartnerAccountID;
import org.laladev.moneyjinn.model.ContractpartnerID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.server.builder.ContractpartnerAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.service.api.IContractpartnerAccountService;

public class ContractpartnerAccountServiceTest extends AbstractTest {
	@Inject
	private IContractpartnerAccountService contractpartnerAccountService;

	@Test
	public void test_createWithInvalidEntity_raisesException() {
		final ContractpartnerAccount contractpartnerAccount = new ContractpartnerAccount();

		Assertions.assertThrows(BusinessException.class, () -> {
			this.contractpartnerAccountService.createContractpartnerAccount(new UserID(1l), contractpartnerAccount);
		});

	}

	@Test
	public void test_updateWithInvalidEntity_raisesException() {
		final ContractpartnerAccount contractpartnerAccount = new ContractpartnerAccount();

		Assertions.assertThrows(BusinessException.class, () -> {
			this.contractpartnerAccountService.updateContractpartnerAccount(new UserID(1l), contractpartnerAccount);
		});
	}

	@Test
	public void test_userAeditsContractpartnerAccount_userBsameGroupSeesCachedChange() {
		final UserID user1ID = new UserID(UserTransportBuilder.USER1_ID);
		final UserID user2ID = new UserID(UserTransportBuilder.USER2_ID);

		// this caches
		ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
				.getContractpartnerAccountById(user2ID, new ContractpartnerAccountID(
						ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID));

		contractpartnerAccount = this.contractpartnerAccountService.getContractpartnerAccountById(user1ID,
				new ContractpartnerAccountID(ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID));

		final String comment = String.valueOf(System.currentTimeMillis());
		contractpartnerAccount.getBankAccount().setAccountNumber(comment);

		// this should also modify the cache of user 1!
		this.contractpartnerAccountService.updateContractpartnerAccount(user1ID, contractpartnerAccount);

		// this should now retrieve the changed cache entry!
		contractpartnerAccount = this.contractpartnerAccountService.getContractpartnerAccountById(user2ID,
				new ContractpartnerAccountID(ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID));

		Assertions.assertEquals(comment, contractpartnerAccount.getBankAccount().getAccountNumber());
	}

	@Test
	public void test_userAaddsAContractpartnerAccount_userBsameGroupSeessItTooBecauseCacheWasReset() {
		final UserID user1ID = new UserID(UserTransportBuilder.USER1_ID);
		final UserID user2ID = new UserID(UserTransportBuilder.USER2_ID);
		final ContractpartnerID contractpartnerID = new ContractpartnerID(
				ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);

		// this caches
		final List<ContractpartnerAccount> allContractpartnerAccounts1 = this.contractpartnerAccountService
				.getContractpartnerAccounts(user1ID, contractpartnerID);

		final ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
				.getContractpartnerAccountById(user2ID, new ContractpartnerAccountID(
						ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID));

		final String comment = String.valueOf(System.currentTimeMillis());
		contractpartnerAccount.getBankAccount().setAccountNumber(comment);

		// this should also modify the cache of user 1!
		this.contractpartnerAccountService.createContractpartnerAccount(user2ID, contractpartnerAccount);

		final List<ContractpartnerAccount> allContractpartnerAccounts2 = this.contractpartnerAccountService
				.getContractpartnerAccounts(user1ID, contractpartnerID);

		// Cache of user1 should have been invalidated and the added ContractpartnerAccount should
		// be now in the List of all ContractpartnerAccounts.
		Assertions.assertNotEquals(allContractpartnerAccounts1.size(), allContractpartnerAccounts2.size());
	}

}
