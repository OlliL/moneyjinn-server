
package org.laladev.moneyjinn.businesslogic.service.impl;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.AbstractTest;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowSplitEntry;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.service.api.IMoneyflowSplitEntryService;

import jakarta.inject.Inject;

class MoneyflowSplitEntryServiceTest extends AbstractTest {
	@Inject
	private IMoneyflowSplitEntryService moneyflowSplitEntryService;
	final UserID user1Id = new UserID(UserTransportBuilder.USER1_ID);

	@Test
	void test_createWithInvalidEntity_raisesException() {
		final MoneyflowSplitEntry moneyflowSplitEntry = new MoneyflowSplitEntry();
		final List<MoneyflowSplitEntry> list = Collections.singletonList(moneyflowSplitEntry);
		Assertions.assertThrows(BusinessException.class, () -> {
			this.moneyflowSplitEntryService.createMoneyflowSplitEntries(this.user1Id, list);
		});
	}

	@Test
	void test_updateWithInvalidEntity_raisesException() {
		final MoneyflowSplitEntry moneyflowSplitEntry = new MoneyflowSplitEntry();
		Assertions.assertThrows(BusinessException.class, () -> {
			this.moneyflowSplitEntryService.updateMoneyflowSplitEntry(this.user1Id, moneyflowSplitEntry);
		});
	}
}
