
package org.laladev.moneyjinn.server.controller.moneyflowreceipt;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowID;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowReceipt;
import org.laladev.moneyjinn.server.builder.MoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.MoneyflowReceiptControllerApi;
import org.laladev.moneyjinn.service.api.IMoneyflowReceiptService;

import jakarta.inject.Inject;

class DeleteMoneyflowReceiptTest extends AbstractWebUserControllerTest {
	@Inject
	private IMoneyflowReceiptService moneyflowReceiptService;

	@Override
	protected void loadMethod() {
		super.getMock(MoneyflowReceiptControllerApi.class).deleteMoneyflowReceipt(null);
	}

	@Test
	void test_unknownMoneyflowId_NoContent() throws Exception {
		super.callUsecaseExpect204WithUriVariables(MoneyflowTransportBuilder.NON_EXISTING_ID);
	}

	@Test
	void test_MoneyflowId1_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW1_ID);

		MoneyflowReceipt receipt = this.moneyflowReceiptService.getMoneyflowReceipt(userId, moneyflowId);
		Assertions.assertNotNull(receipt);
		Assertions.assertEquals(1L, receipt.getId().getId());

		super.callUsecaseExpect204WithUriVariables(MoneyflowTransportBuilder.MONEYFLOW1_ID);

		receipt = this.moneyflowReceiptService.getMoneyflowReceipt(userId, moneyflowId);
		Assertions.assertNull(receipt);
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