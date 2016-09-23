package org.laladev.moneyjinn.businesslogic.service.impl;

import javax.inject.Inject;

import org.junit.Test;
import org.laladev.moneyjinn.AbstractTest;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.service.api.IUserService;

public class UserServiceTest extends AbstractTest {
	@Inject
	private IUserService userService;

	@Test(expected = BusinessException.class)
	public void test_createWithInvalidEntity_raisesException() {
		final User user = new User();

		this.userService.createUser(user);
	}

	@Test(expected = BusinessException.class)
	public void test_updateWithInvalidEntity_raisesException() {
		final User user = new User();

		this.userService.updateUser(user);
	}
}
