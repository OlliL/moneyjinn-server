package org.laladev.moneyjinn.businesslogic.service.impl;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.AbstractTest;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.service.api.IUserService;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UserServiceTest extends AbstractTest {
    @Inject
    private IUserService userService;

    @Test
    void test_createWithInvalidEntity_raisesException() {
        final User user = new User();
        assertThrows(BusinessException.class, () -> this.userService.createUser(user));
    }

    @Test
    void test_updateWithInvalidEntity_raisesException() {
        final User user = new User();
        assertThrows(BusinessException.class, () -> this.userService.updateUser(user));
    }
}
