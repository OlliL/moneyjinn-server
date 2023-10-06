
package org.laladev.moneyjinn.businesslogic.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.AbstractTest;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.service.api.IUserService;

import jakarta.inject.Inject;

class UserServiceTest extends AbstractTest {
	@Inject
	private IUserService userService;

	@Test
	void test_createWithInvalidEntity_raisesException() {
		final User user = new User();
		Assertions.assertThrows(BusinessException.class, () -> {
			this.userService.createUser(user);
		});
	}

	@Test
	void test_updateWithInvalidEntity_raisesException() {
		final User user = new User();
		Assertions.assertThrows(BusinessException.class, () -> {
			this.userService.updateUser(user);
		});
	}
}
