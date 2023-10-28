
package org.laladev.moneyjinn.server.controller.moneyflowreceipt;

import java.util.Base64;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.MoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.MoneyflowReceiptControllerApi;
import org.laladev.moneyjinn.server.model.ShowMoneyflowReceiptResponse;

class ShowMoneyflowReceiptTest extends AbstractWebUserControllerTest {
	private static final String MONEYFLOW_RECEIPT_1 = "FFFFFFFF";
	private static final Integer MONEYFLOW_RECEIPT_1_TYPE = 1;
	private static final String MONEYFLOW_RECEIPT_2 = "FFFFFFFF";
	private static final Integer MONEYFLOW_RECEIPT_2_TYPE = 2;

	@Override
	protected void loadMethod() {
		super.getMock(MoneyflowReceiptControllerApi.class).showMoneyflowReceipt(null);
	}

	@Test
	void test_unknownMoneyflowReceipt_emptyResponseObject() throws Exception {
		final ShowMoneyflowReceiptResponse expected = new ShowMoneyflowReceiptResponse();
		final ShowMoneyflowReceiptResponse actual = super.callUsecaseExpect200(ShowMoneyflowReceiptResponse.class,
				MoneyflowTransportBuilder.NON_EXISTING_ID);
		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_MoneyflowReceipt1_completeResponseObject() throws Exception {
		final ShowMoneyflowReceiptResponse expected = new ShowMoneyflowReceiptResponse();
		expected.setReceipt(Base64.getEncoder().encodeToString(MONEYFLOW_RECEIPT_1.getBytes()));
		expected.setReceiptType(MONEYFLOW_RECEIPT_1_TYPE);
		final ShowMoneyflowReceiptResponse actual = super.callUsecaseExpect200(ShowMoneyflowReceiptResponse.class,
				MoneyflowTransportBuilder.MONEYFLOW1_ID);
		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_MoneyflowReceipt2_completeResponseObject() throws Exception {
		final ShowMoneyflowReceiptResponse expected = new ShowMoneyflowReceiptResponse();
		expected.setReceipt(Base64.getEncoder().encodeToString(MONEYFLOW_RECEIPT_2.getBytes()));
		expected.setReceiptType(MONEYFLOW_RECEIPT_2_TYPE);
		final ShowMoneyflowReceiptResponse actual = super.callUsecaseExpect200(ShowMoneyflowReceiptResponse.class,
				MoneyflowTransportBuilder.MONEYFLOW2_ID);
		Assertions.assertEquals(expected, actual);
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403WithUriVariables(MoneyflowTransportBuilder.MONEYFLOW1_ID);
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		final ShowMoneyflowReceiptResponse expected = new ShowMoneyflowReceiptResponse();

		final ShowMoneyflowReceiptResponse actual = super.callUsecaseExpect200(ShowMoneyflowReceiptResponse.class,
				MoneyflowTransportBuilder.MONEYFLOW1_ID);

		Assertions.assertEquals(expected, actual);
	}
}