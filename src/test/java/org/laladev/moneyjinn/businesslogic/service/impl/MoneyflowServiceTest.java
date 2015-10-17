package org.laladev.moneyjinn.businesslogic.service.impl;

import java.util.Arrays;

import javax.inject.Inject;

import org.junit.Test;
import org.laladev.moneyjinn.AbstractTest;
import org.laladev.moneyjinn.businesslogic.model.access.Group;
import org.laladev.moneyjinn.businesslogic.model.access.GroupID;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.exception.BusinessException;
import org.laladev.moneyjinn.businesslogic.model.moneyflow.Moneyflow;
import org.laladev.moneyjinn.businesslogic.service.api.IMoneyflowService;

public class MoneyflowServiceTest extends AbstractTest {
	@Inject
	private IMoneyflowService moneyflowService;

	@Test(expected = IllegalArgumentException.class)
	public void test_validateNullUser_raisesException() {
		final Moneyflow moneyflow = new Moneyflow();
		moneyflow.setGroup(new Group(new GroupID(1l)));

		this.moneyflowService.validateMoneyflow(moneyflow);
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_validateNullGroup_raisesException() {
		final Moneyflow moneyflow = new Moneyflow();
		moneyflow.setUser(new User(new UserID(1l)));

		this.moneyflowService.validateMoneyflow(moneyflow);
	}

	@Test(expected = BusinessException.class)
	public void test_createWithInvalidEntity_raisesException() {
		final Moneyflow moneyflow = new Moneyflow();
		moneyflow.setUser(new User(new UserID(1l)));
		moneyflow.setGroup(new Group(new GroupID(1l)));

		this.moneyflowService.createMoneyflows(Arrays.asList(moneyflow));
	}

	@Test(expected = BusinessException.class)
	public void test_updateWithInvalidEntity_raisesException() {
		final Moneyflow moneyflow = new Moneyflow();
		moneyflow.setUser(new User(new UserID(1l)));
		moneyflow.setGroup(new Group(new GroupID(1l)));

		this.moneyflowService.updateMoneyflow(moneyflow);
	}
}
