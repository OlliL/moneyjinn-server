package org.laladev.moneyjinn.businesslogic.service.impl;

import javax.inject.Inject;

import org.junit.Test;
import org.laladev.moneyjinn.AbstractTest;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerAccount;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.exception.BusinessException;
import org.laladev.moneyjinn.businesslogic.service.api.IContractpartnerAccountService;

public class ContractpartnerAccountServiceTest extends AbstractTest {
	@Inject
	private IContractpartnerAccountService contractpartnerAccountService;

	@Test(expected = BusinessException.class)
	public void test_createWithInvalidEntity_raisesException() {
		final ContractpartnerAccount contractpartnerAccount = new ContractpartnerAccount();

		this.contractpartnerAccountService.createContractpartnerAccount(new UserID(1l), contractpartnerAccount);
	}

	@Test(expected = BusinessException.class)
	public void test_updateWithInvalidEntity_raisesException() {
		final ContractpartnerAccount contractpartnerAccount = new ContractpartnerAccount();

		this.contractpartnerAccountService.updateContractpartnerAccount(new UserID(1l), contractpartnerAccount);
	}
}
