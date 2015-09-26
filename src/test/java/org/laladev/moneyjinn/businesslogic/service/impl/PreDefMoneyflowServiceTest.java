package org.laladev.moneyjinn.businesslogic.service.impl;

import javax.inject.Inject;

import org.junit.Test;
import org.laladev.moneyjinn.AbstractTest;
import org.laladev.moneyjinn.businesslogic.model.PreDefMoneyflow;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.exception.BusinessException;
import org.laladev.moneyjinn.businesslogic.service.api.IPreDefMoneyflowService;

public class PreDefMoneyflowServiceTest extends AbstractTest {
	@Inject
	private IPreDefMoneyflowService preDefMoneyflowService;

	@Test(expected = IllegalArgumentException.class)
	public void test_validateNullUser_raisesException() {
		final PreDefMoneyflow preDefMoneyflow = new PreDefMoneyflow();

		this.preDefMoneyflowService.validatePreDefMoneyflow(preDefMoneyflow);
	}

	@Test(expected = BusinessException.class)
	public void test_createWithInvalidEntity_raisesException() {
		final PreDefMoneyflow preDefMoneyflow = new PreDefMoneyflow();
		preDefMoneyflow.setUser(new User(new UserID(1l)));

		this.preDefMoneyflowService.createPreDefMoneyflow(preDefMoneyflow);
	}

	@Test(expected = BusinessException.class)
	public void test_updateWithInvalidEntity_raisesException() {
		final PreDefMoneyflow preDefMoneyflow = new PreDefMoneyflow();
		preDefMoneyflow.setUser(new User(new UserID(1l)));

		this.preDefMoneyflowService.updatePreDefMoneyflow(preDefMoneyflow);
	}
}
