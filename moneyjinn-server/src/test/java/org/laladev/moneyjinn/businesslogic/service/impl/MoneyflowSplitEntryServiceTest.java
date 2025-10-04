package org.laladev.moneyjinn.businesslogic.service.impl;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.AbstractTest;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowSplitEntry;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.service.api.IMoneyflowSplitEntryService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class MoneyflowSplitEntryServiceTest extends AbstractTest {
    final UserID user1Id = new UserID(UserTransportBuilder.USER1_ID);
    @Inject
    private IMoneyflowSplitEntryService moneyflowSplitEntryService;

    @Test
    void test_createWithInvalidEntity_raisesException() {
        final MoneyflowSplitEntry moneyflowSplitEntry = new MoneyflowSplitEntry();
        final List<MoneyflowSplitEntry> list = Collections.singletonList(moneyflowSplitEntry);
        assertThrows(BusinessException.class,
                () -> this.moneyflowSplitEntryService.createMoneyflowSplitEntries(this.user1Id, list));
    }

    @Test
    void test_updateWithInvalidEntity_raisesException() {
        final MoneyflowSplitEntry moneyflowSplitEntry = new MoneyflowSplitEntry();
        assertThrows(BusinessException.class,
                () -> this.moneyflowSplitEntryService.updateMoneyflowSplitEntry(this.user1Id, moneyflowSplitEntry));
    }
}
