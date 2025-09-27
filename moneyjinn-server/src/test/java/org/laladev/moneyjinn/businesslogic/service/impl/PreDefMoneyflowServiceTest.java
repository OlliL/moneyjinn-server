package org.laladev.moneyjinn.businesslogic.service.impl;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.AbstractTest;
import org.laladev.moneyjinn.model.PreDefMoneyflow;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.service.api.IPreDefMoneyflowService;

class PreDefMoneyflowServiceTest extends AbstractTest {
    @Inject
    private IPreDefMoneyflowService preDefMoneyflowService;

    @Test
    void test_validateNullUser_raisesException() {
        final PreDefMoneyflow preDefMoneyflow = new PreDefMoneyflow();
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.preDefMoneyflowService.validatePreDefMoneyflow(preDefMoneyflow));
    }

    @Test
    void test_createWithInvalidEntity_raisesException() {
        final PreDefMoneyflow preDefMoneyflow = new PreDefMoneyflow();
        preDefMoneyflow.setUser(new User(new UserID(1L)));
        Assertions.assertThrows(BusinessException.class, () -> this.preDefMoneyflowService.createPreDefMoneyflow(preDefMoneyflow));
    }

    @Test
    void test_updateWithInvalidEntity_raisesException() {
        final PreDefMoneyflow preDefMoneyflow = new PreDefMoneyflow();
        preDefMoneyflow.setUser(new User(new UserID(1L)));
        Assertions.assertThrows(BusinessException.class, () -> this.preDefMoneyflowService.updatePreDefMoneyflow(preDefMoneyflow));
    }
}
