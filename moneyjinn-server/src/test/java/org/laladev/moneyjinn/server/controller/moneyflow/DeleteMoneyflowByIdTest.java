
package org.laladev.moneyjinn.server.controller.moneyflow;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.moneyflow.Moneyflow;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowID;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowReceipt;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowSplitEntry;
import org.laladev.moneyjinn.server.builder.MoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.MoneyflowControllerApi;
import org.laladev.moneyjinn.service.api.IMoneyflowReceiptService;
import org.laladev.moneyjinn.service.api.IMoneyflowService;
import org.laladev.moneyjinn.service.api.IMoneyflowSplitEntryService;

import jakarta.inject.Inject;

class DeleteMoneyflowByIdTest extends AbstractWebUserControllerTest {
	@Inject
	private IMoneyflowService moneyflowService;
	@Inject
	private IMoneyflowSplitEntryService moneyflowSplitEntryService;
	@Inject
	private IMoneyflowReceiptService moneyflowReceiptService;

	@Override
	protected void loadMethod() {
		super.getMock(MoneyflowControllerApi.class).deleteMoneyflowById(null);
	}

	@Test
	void test_regularMoneyflow_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW1_ID);
		Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);
		Assertions.assertNotNull(moneyflow);

		super.callUsecaseExpect204WithUriVariables(MoneyflowTransportBuilder.MONEYFLOW1_ID);

		// Validate that everything was deleted (and do not rely on foreign key
		// constraints)
		moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);
		Assertions.assertNull(moneyflow);
		final MoneyflowReceipt moneyflowReceipt = this.moneyflowReceiptService.getMoneyflowReceipt(userId, moneyflowId);
		Assertions.assertNull(moneyflowReceipt);
		final List<MoneyflowSplitEntry> moneyflowSplitEntries = this.moneyflowSplitEntryService
				.getMoneyflowSplitEntries(userId, moneyflowId);
		Assertions.assertTrue(moneyflowSplitEntries.isEmpty());
	}

	@Test
	void test_nonExistingMoneyflow_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.NON_EXISTING_ID);
		final Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);
		Assertions.assertNull(moneyflow);

		super.callUsecaseExpect204WithUriVariables(MoneyflowTransportBuilder.NON_EXISTING_ID);

	}

	@Test
	void test_MoneyflowOwnedBySomeoneElse_noDeletionHappend() throws Exception {
		super.setUsername(UserTransportBuilder.USER3_NAME);
		super.setPassword(UserTransportBuilder.USER3_PASSWORD);

		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW1_ID);
		Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);
		Assertions.assertNotNull(moneyflow);

		super.callUsecaseExpect204WithUriVariables(MoneyflowTransportBuilder.MONEYFLOW1_ID);

		moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);
		Assertions.assertNotNull(moneyflow);
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403WithUriVariables(MoneyflowTransportBuilder.MONEYFLOW1_ID);
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		super.callUsecaseExpect204WithUriVariables(MoneyflowTransportBuilder.MONEYFLOW1_ID);
	}
}