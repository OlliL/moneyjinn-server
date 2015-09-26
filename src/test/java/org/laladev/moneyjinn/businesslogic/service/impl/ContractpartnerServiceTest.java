package org.laladev.moneyjinn.businesslogic.service.impl;

import javax.inject.Inject;

import org.junit.Test;
import org.laladev.moneyjinn.AbstractTest;
import org.laladev.moneyjinn.businesslogic.model.Contractpartner;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.exception.BusinessException;
import org.laladev.moneyjinn.businesslogic.service.api.IContractpartnerService;

public class ContractpartnerServiceTest extends AbstractTest {
	@Inject
	private IContractpartnerService contractpartnerService;

	@Test(expected = IllegalArgumentException.class)
	public void test_validateNullUser_raisesException() {
		final Contractpartner contractpartner = new Contractpartner();

		this.contractpartnerService.validateContractpartner(contractpartner);
	}

	@Test(expected = BusinessException.class)
	public void test_createWithInvalidEntity_raisesException() {
		final Contractpartner contractpartner = new Contractpartner();
		contractpartner.setUser(new User(new UserID(1l)));

		this.contractpartnerService.createContractpartner(contractpartner);
	}

	@Test(expected = BusinessException.class)
	public void test_updateWithInvalidEntity_raisesException() {
		final Contractpartner contractpartner = new Contractpartner();
		contractpartner.setUser(new User(new UserID(1l)));

		this.contractpartnerService.updateContractpartner(contractpartner);
	}
}
