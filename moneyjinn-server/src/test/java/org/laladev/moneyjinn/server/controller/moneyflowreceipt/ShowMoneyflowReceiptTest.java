
package org.laladev.moneyjinn.server.controller.moneyflowreceipt;

import java.util.Base64;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.MoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.controller.api.MoneyflowReceiptControllerApi;
import org.laladev.moneyjinn.server.model.ShowMoneyflowReceiptResponse;
import org.springframework.test.context.jdbc.Sql;

class ShowMoneyflowReceiptTest extends AbstractControllerTest {
	private static final String MONEYFLOW_RECEIPT_1 = "FFFFFFFF";
	private static final Integer MONEYFLOW_RECEIPT_1_TYPE = 1;
	private static final String MONEYFLOW_RECEIPT_2 = "FFFFFFFF";
	private static final Integer MONEYFLOW_RECEIPT_2_TYPE = 2;
	private String userName;
	private String userPassword;

	@BeforeEach
	public void setUp() {
		this.userName = UserTransportBuilder.USER1_NAME;
		this.userPassword = UserTransportBuilder.USER1_PASSWORD;
	}

	@Override
	protected String getUsername() {
		return this.userName;
	}

	@Override
	protected String getPassword() {
		return this.userPassword;
	}

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

	@Test
	void test_AuthorizationRequired_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;

		super.callUsecaseExpect403WithUriVariables(MoneyflowTransportBuilder.MONEYFLOW1_ID);
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	void test_emptyDatabase_noException() throws Exception {
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
		final ShowMoneyflowReceiptResponse expected = new ShowMoneyflowReceiptResponse();

		final ShowMoneyflowReceiptResponse actual = super.callUsecaseExpect200(ShowMoneyflowReceiptResponse.class,
				MoneyflowTransportBuilder.MONEYFLOW1_ID);

		Assertions.assertEquals(expected, actual);
	}
}