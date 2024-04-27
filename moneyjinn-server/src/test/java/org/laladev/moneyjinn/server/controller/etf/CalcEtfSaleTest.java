
package org.laladev.moneyjinn.server.controller.etf;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.server.builder.EtfTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.EtfControllerApi;
import org.laladev.moneyjinn.server.model.CalcEtfSaleRequest;
import org.laladev.moneyjinn.server.model.CalcEtfSaleResponse;
import org.laladev.moneyjinn.server.model.ValidationResponse;

class CalcEtfSaleTest extends AbstractWebUserControllerTest {

	private final static BigDecimal SETTING_SALE_ASK_PRICE = new BigDecimal("900.000");
	private final static BigDecimal SETTING_SALE_BID_PRICE = new BigDecimal("899.500");
	private final static String SETTING_ISIN = EtfTransportBuilder.ISIN;
	private final static BigDecimal SETTING_SALE_PIECES = new BigDecimal("10");
	private final static BigDecimal SETTING_SALE_TRANSACTION_COSTS = new BigDecimal("0.99");

	@Override
	protected void loadMethod() {
		super.getMock(EtfControllerApi.class).calcEtfSale(null);
	}

	@Test
	void test_standardRequest_FullResponseObject() throws Exception {

		final CalcEtfSaleRequest request = new CalcEtfSaleRequest();
		request.setAskPrice(SETTING_SALE_ASK_PRICE);
		request.setBidPrice(SETTING_SALE_BID_PRICE);
		request.setIsin(SETTING_ISIN);
		request.setPieces(SETTING_SALE_PIECES);
		request.setTransactionCosts(SETTING_SALE_TRANSACTION_COSTS);

		final CalcEtfSaleResponse expected = this.getExpected();
		final CalcEtfSaleResponse actual = super.callUsecaseExpect200(request, CalcEtfSaleResponse.class);

		Assertions.assertEquals(expected, actual);

	}

	@Test
	void test_sellTooMuchPieces_ValidationError() throws Exception {

		final CalcEtfSaleRequest request = new CalcEtfSaleRequest();
		request.setAskPrice(SETTING_SALE_ASK_PRICE);
		request.setBidPrice(SETTING_SALE_BID_PRICE);
		request.setIsin(SETTING_ISIN);
		request.setPieces(new BigDecimal(100000));
		request.setTransactionCosts(SETTING_SALE_TRANSACTION_COSTS);

		final ValidationResponse actual = super.callUsecaseExpect422(request, ValidationResponse.class);

		Assertions.assertFalse(actual.getResult());
		Assertions.assertEquals(ErrorCode.AMOUNT_TO_HIGH.getErrorCode(),
				actual.getValidationItemTransports().get(0).getError());

	}

	@Test
	void test_negativeInput_AbsoluteValuesAreUsed() throws Exception {

		final CalcEtfSaleRequest request = new CalcEtfSaleRequest();
		request.setAskPrice(SETTING_SALE_ASK_PRICE.negate());
		request.setBidPrice(SETTING_SALE_BID_PRICE.negate());
		request.setIsin(SETTING_ISIN);
		request.setPieces(SETTING_SALE_PIECES.negate());
		request.setTransactionCosts(SETTING_SALE_TRANSACTION_COSTS);

		final CalcEtfSaleResponse expected = this.getExpected();
		final CalcEtfSaleResponse actual = super.callUsecaseExpect200(request, CalcEtfSaleResponse.class);

		Assertions.assertEquals(expected, actual);

	}

	private CalcEtfSaleResponse getExpected() {
		final BigDecimal pieces = BigDecimal.TEN;
		final BigDecimal sellPrice = SETTING_SALE_BID_PRICE.multiply(pieces).setScale(2);
		final BigDecimal newBuyPrice = SETTING_SALE_ASK_PRICE.multiply(pieces).setScale(2);
		final BigDecimal originalBuyPrice = new BigDecimal("8020.482782").setScale(2, RoundingMode.HALF_UP);

		final CalcEtfSaleResponse expected = new CalcEtfSaleResponse();
		expected.setIsin(SETTING_ISIN);
		expected.setOriginalBuyPrice(originalBuyPrice);
		expected.setSellPrice(sellPrice);
		expected.setNewBuyPrice(newBuyPrice);
		expected.setProfit(new BigDecimal("974.52"));
		expected.setChargeable(new BigDecimal("682.17"));
		expected.setTransactionCosts(new BigDecimal("1.98"));
		expected.setRebuyLosses(new BigDecimal("5.00"));
		expected.setOverallCosts(new BigDecimal("6.98"));
		expected.setPieces(pieces);
		expected.accumulatedPreliminaryLumpSum(BigDecimal.ZERO.setScale(2));
		return expected;
	}

	@Test
	void test_invalidIsinWithPieces_emptyResponse() throws Exception {

		final CalcEtfSaleRequest request = new CalcEtfSaleRequest();
		request.setAskPrice(SETTING_SALE_ASK_PRICE);
		request.setBidPrice(SETTING_SALE_BID_PRICE);
		request.setIsin("NOTEXISTING");
		request.setPieces(SETTING_SALE_PIECES);
		request.setTransactionCosts(SETTING_SALE_TRANSACTION_COSTS);

		final CalcEtfSaleResponse expected = new CalcEtfSaleResponse();
		final CalcEtfSaleResponse actual = super.callUsecaseExpect200(request, CalcEtfSaleResponse.class);

		Assertions.assertEquals(expected, actual);

	}

	@Test
	void test_askPriceNotSet_emptyResponse() throws Exception {

		final CalcEtfSaleRequest request = new CalcEtfSaleRequest();
		request.setBidPrice(SETTING_SALE_BID_PRICE);
		request.setIsin(SETTING_ISIN);
		request.setPieces(SETTING_SALE_PIECES);
		request.setTransactionCosts(SETTING_SALE_TRANSACTION_COSTS);

		final CalcEtfSaleResponse expected = new CalcEtfSaleResponse();
		final CalcEtfSaleResponse actual = super.callUsecaseExpect200(request, CalcEtfSaleResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_bidPriceNotSet_emptyResponse() throws Exception {

		final CalcEtfSaleRequest request = new CalcEtfSaleRequest();
		request.setAskPrice(SETTING_SALE_ASK_PRICE);
		request.setIsin(SETTING_ISIN);
		request.setPieces(SETTING_SALE_PIECES);
		request.setTransactionCosts(SETTING_SALE_TRANSACTION_COSTS);

		final CalcEtfSaleResponse expected = new CalcEtfSaleResponse();
		final CalcEtfSaleResponse actual = super.callUsecaseExpect200(request, CalcEtfSaleResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_isinNotSet_emptyResponse() throws Exception {

		final CalcEtfSaleRequest request = new CalcEtfSaleRequest();
		request.setAskPrice(SETTING_SALE_ASK_PRICE);
		request.setBidPrice(SETTING_SALE_BID_PRICE);
		request.setPieces(SETTING_SALE_PIECES);
		request.setTransactionCosts(SETTING_SALE_TRANSACTION_COSTS);

		final CalcEtfSaleResponse expected = new CalcEtfSaleResponse();
		final CalcEtfSaleResponse actual = super.callUsecaseExpect200(request, CalcEtfSaleResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_piecesNotSet_emptyResponse() throws Exception {

		final CalcEtfSaleRequest request = new CalcEtfSaleRequest();
		request.setAskPrice(SETTING_SALE_ASK_PRICE);
		request.setBidPrice(SETTING_SALE_BID_PRICE);
		request.setIsin(SETTING_ISIN);
		request.setTransactionCosts(SETTING_SALE_TRANSACTION_COSTS);

		final CalcEtfSaleResponse expected = new CalcEtfSaleResponse();
		final CalcEtfSaleResponse actual = super.callUsecaseExpect200(request, CalcEtfSaleResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_transactionCostsNotSet_emptyResponse() throws Exception {

		final CalcEtfSaleRequest request = new CalcEtfSaleRequest();
		request.setAskPrice(SETTING_SALE_ASK_PRICE);
		request.setBidPrice(SETTING_SALE_BID_PRICE);
		request.setIsin(SETTING_ISIN);
		request.setPieces(SETTING_SALE_PIECES);

		final CalcEtfSaleResponse expected = new CalcEtfSaleResponse();
		final CalcEtfSaleResponse actual = super.callUsecaseExpect200(request, CalcEtfSaleResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_invalidIsinWith0Pieces_emptyResponse() throws Exception {

		final CalcEtfSaleRequest request = new CalcEtfSaleRequest();
		request.setAskPrice(SETTING_SALE_ASK_PRICE);
		request.setBidPrice(SETTING_SALE_BID_PRICE);
		request.setIsin("NOTEXISTING");
		request.setPieces(BigDecimal.ZERO);
		request.setTransactionCosts(SETTING_SALE_TRANSACTION_COSTS);

		final CalcEtfSaleResponse expected = new CalcEtfSaleResponse();
		final CalcEtfSaleResponse actual = super.callUsecaseExpect200(request, CalcEtfSaleResponse.class);

		Assertions.assertEquals(expected, actual);

	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403(new CalcEtfSaleRequest());
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		super.callUsecaseExpect200(new CalcEtfSaleRequest(), CalcEtfSaleResponse.class);
	}

}