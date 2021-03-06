package org.laladev.moneyjinn.server.controller.moneyflowreceipt;

import java.util.Base64;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.moneyflow.ShowMoneyflowReceiptResponse;
import org.laladev.moneyjinn.server.builder.MoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class ShowMoneyflowReceiptTest extends AbstractControllerTest {

	private static final String MONEYFLOW_RECEIPT_1 = "FFFFFFFF";
	private static final Short MONEYFLOW_RECEIPT_1_TYPE = (short) 1;
	private static final String MONEYFLOW_RECEIPT_2 = "FFFFFFFF";
	private static final Short MONEYFLOW_RECEIPT_2_TYPE = (short) 2;
	private final HttpMethod method = HttpMethod.GET;
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
	protected String getUsecase() {
		return super.getUsecaseFromTestClassName(this.getClass());
	}

	private static byte[] hexStringToByteArray(final String s) {
		final int len = s.length();
		final byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	@Test
	public void test_unknownMoneyflowReceipt_emptyResponseObject() throws Exception {
		final ShowMoneyflowReceiptResponse expected = new ShowMoneyflowReceiptResponse();
		final ShowMoneyflowReceiptResponse actual = super.callUsecaseWithoutContent(
				"/" + MoneyflowTransportBuilder.NON_EXISTING_ID, this.method, false,
				ShowMoneyflowReceiptResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	public void test_MoneyflowReceipt1_completeResponseObject() throws Exception {
		final ShowMoneyflowReceiptResponse expected = new ShowMoneyflowReceiptResponse();
		expected.setReceipt(Base64.getEncoder().encodeToString(hexStringToByteArray(MONEYFLOW_RECEIPT_1)));
		expected.setReceiptType(MONEYFLOW_RECEIPT_1_TYPE);

		final ShowMoneyflowReceiptResponse actual = super.callUsecaseWithoutContent(
				"/" + MoneyflowTransportBuilder.MONEYFLOW1_ID, this.method, false, ShowMoneyflowReceiptResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	public void test_MoneyflowReceipt2_completeResponseObject() throws Exception {
		final ShowMoneyflowReceiptResponse expected = new ShowMoneyflowReceiptResponse();
		expected.setReceipt(Base64.getEncoder().encodeToString(hexStringToByteArray(MONEYFLOW_RECEIPT_2)));
		expected.setReceiptType(MONEYFLOW_RECEIPT_2_TYPE);

		final ShowMoneyflowReceiptResponse actual = super.callUsecaseWithoutContent(
				"/" + MoneyflowTransportBuilder.MONEYFLOW2_ID, this.method, false, ShowMoneyflowReceiptResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	public void test_AuthorizationRequired_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("/1", this.method, false, ErrorResponse.class);
		Assertions.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	public void test_emptyDatabase_noException() throws Exception {
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;

		final ShowMoneyflowReceiptResponse expected = new ShowMoneyflowReceiptResponse();

		final ShowMoneyflowReceiptResponse actual = super.callUsecaseWithoutContent(
				"/" + MoneyflowTransportBuilder.MONEYFLOW1_ID, this.method, false, ShowMoneyflowReceiptResponse.class);

		Assertions.assertEquals(expected, actual);
	}

}