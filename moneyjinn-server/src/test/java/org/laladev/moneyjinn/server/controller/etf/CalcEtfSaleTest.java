
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
	private final static Long SETTING_ETF_ID = EtfTransportBuilder.ETF_ID_1;
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
		request.setEtfId(SETTING_ETF_ID);
		request.setPieces(SETTING_SALE_PIECES);
		request.setTransactionCosts(SETTING_SALE_TRANSACTION_COSTS);

		final CalcEtfSaleResponse expected = this.getStandardRequestExpected();
		final CalcEtfSaleResponse actual = super.callUsecaseExpect200(request, CalcEtfSaleResponse.class);

		Assertions.assertEquals(expected, actual);

	}

	@Test
	void test_sellTooMuchPieces_ValidationError() throws Exception {

		final CalcEtfSaleRequest request = new CalcEtfSaleRequest();
		request.setAskPrice(SETTING_SALE_ASK_PRICE);
		request.setBidPrice(SETTING_SALE_BID_PRICE);
		request.setEtfId(SETTING_ETF_ID);
		request.setPieces(new BigDecimal(100000));
		request.setTransactionCosts(SETTING_SALE_TRANSACTION_COSTS);

		final ValidationResponse actual = super.callUsecaseExpect422(request, ValidationResponse.class);

		Assertions.assertFalse(actual.getResult());
		Assertions.assertEquals(ErrorCode.AMOUNT_TO_HIGH.getErrorCode(),
				actual.getValidationItemTransports().get(0).getError());

	}

	@Test
	void test_standardRequestNegativeInput_AbsoluteValuesAreUsed() throws Exception {

		final CalcEtfSaleRequest request = new CalcEtfSaleRequest();
		request.setAskPrice(SETTING_SALE_ASK_PRICE.negate());
		request.setBidPrice(SETTING_SALE_BID_PRICE.negate());
		request.setEtfId(SETTING_ETF_ID);
		request.setPieces(SETTING_SALE_PIECES.negate());
		request.setTransactionCosts(SETTING_SALE_TRANSACTION_COSTS);

		final CalcEtfSaleResponse expected = this.getStandardRequestExpected();
		final CalcEtfSaleResponse actual = super.callUsecaseExpect200(request, CalcEtfSaleResponse.class);

		Assertions.assertEquals(expected, actual);

	}

	private CalcEtfSaleResponse getStandardRequestExpected() {
		final BigDecimal pieces = SETTING_SALE_PIECES;
		final BigDecimal sellPrice = SETTING_SALE_BID_PRICE.multiply(pieces).setScale(2);
		final BigDecimal newBuyPrice = SETTING_SALE_ASK_PRICE.multiply(pieces).setScale(2);
		final BigDecimal originalBuyPrice = new BigDecimal("8020.482782").setScale(2, RoundingMode.HALF_UP);

		final CalcEtfSaleResponse expected = new CalcEtfSaleResponse();
		expected.setEtfId(SETTING_ETF_ID);
		expected.setEtfId(SETTING_ETF_ID);
		expected.setOriginalBuyPrice(originalBuyPrice);
		expected.setSellPrice(sellPrice);
		expected.setNewBuyPrice(newBuyPrice);
		expected.setProfit(new BigDecimal("974.52"));
		expected.setChargeable(new BigDecimal("668.23"));
		expected.setTransactionCosts(new BigDecimal("1.98"));
		expected.setRebuyLosses(new BigDecimal("5.00"));
		expected.setOverallCosts(new BigDecimal("6.98"));
		expected.setPieces(pieces);
		expected.accumulatedPreliminaryLumpSum(new BigDecimal("19.91"));
		return expected;
	}

	@Test
	void test_preliminaryLumpSumBuyingMonthAndNextJanuary_LumpSumCorrectlySummedUp() throws Exception {

		final CalcEtfSaleRequest request = new CalcEtfSaleRequest();
		request.setAskPrice(SETTING_SALE_ASK_PRICE);
		request.setBidPrice(SETTING_SALE_BID_PRICE);
		request.setEtfId(SETTING_ETF_ID);
		request.setPieces(new BigDecimal("2"));
		request.setTransactionCosts(SETTING_SALE_TRANSACTION_COSTS);

		final BigDecimal expectedSum = new BigDecimal("6.19");
		final CalcEtfSaleResponse actual = super.callUsecaseExpect200(request, CalcEtfSaleResponse.class);

		Assertions.assertEquals(expectedSum, actual.getAccumulatedPreliminaryLumpSum());
	}

	@Test
	void test_preliminaryLumpSumBuyingMonthWithBuySaleBuyButOnlyFirstBuyedPiecesSold_LumpSumCorrectlySummedUp()
			throws Exception {

		final CalcEtfSaleRequest request = new CalcEtfSaleRequest();
		request.setAskPrice(SETTING_SALE_ASK_PRICE);
		request.setBidPrice(SETTING_SALE_BID_PRICE);
		request.setEtfId(SETTING_ETF_ID);
		request.setPieces(new BigDecimal("160"));
		request.setTransactionCosts(SETTING_SALE_TRANSACTION_COSTS);

		// 02.234pcs -> (9.22€ / 6.5pcs + 156.11€ / 93.234pcs) * 2.234pcs = 6.909€
		// 81.000pcs -> (156.11€ / 093.234pcs) * 81.000pcs = 135.625€
		// 76.766pcs -> (109.32€ / 110.000pcs) * 76.766pcs = 117.821€
		// --> 6.909€ + 135.625€ + 117.821€ == 260.36€
		final BigDecimal expectedSum = new BigDecimal("260.36");
		final CalcEtfSaleResponse actual = super.callUsecaseExpect200(request, CalcEtfSaleResponse.class);

		Assertions.assertEquals(expectedSum, actual.getAccumulatedPreliminaryLumpSum());
	}

	@Test
	void test_preliminaryLumpSumBuyingMonthWithBuySaleBuyPiecesSoldFromBothBuyStacks_LumpSumCorrectlySummedUp()
			throws Exception {

		final CalcEtfSaleRequest request = new CalcEtfSaleRequest();
		request.setAskPrice(SETTING_SALE_ASK_PRICE);
		request.setBidPrice(SETTING_SALE_BID_PRICE);
		request.setEtfId(SETTING_ETF_ID);
		request.setPieces(new BigDecimal("180"));
		request.setTransactionCosts(SETTING_SALE_TRANSACTION_COSTS);

		// 02.234pcs -> (9.22€ / 6.5pcs + 156.11€ / 93.234pcs) * 2.234pcs = 6.909€
		// 81.000pcs -> (156.11€ / 093.234pcs) * 81.000pcs = 135.625€
		// 96.766pcs -> (109.32€ / 110.000pcs) * 96.766pcs = 148.518€
		// --> 6.909€ + 135.625€ + 148.518€ == 291.05€
		final BigDecimal expectedSum = new BigDecimal("291.05");
		final CalcEtfSaleResponse actual = super.callUsecaseExpect200(request, CalcEtfSaleResponse.class);

		Assertions.assertEquals(expectedSum, actual.getAccumulatedPreliminaryLumpSum());
	}

	@Test
	void test_invalidEtfIdWithPieces_emptyResponse() throws Exception {

		final CalcEtfSaleRequest request = new CalcEtfSaleRequest();
		request.setAskPrice(SETTING_SALE_ASK_PRICE);
		request.setBidPrice(SETTING_SALE_BID_PRICE);
		request.setEtfId(EtfTransportBuilder.NON_EXISTING_ID);
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
		request.setEtfId(SETTING_ETF_ID);
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
		request.setEtfId(SETTING_ETF_ID);
		request.setPieces(SETTING_SALE_PIECES);
		request.setTransactionCosts(SETTING_SALE_TRANSACTION_COSTS);

		final CalcEtfSaleResponse expected = new CalcEtfSaleResponse();
		final CalcEtfSaleResponse actual = super.callUsecaseExpect200(request, CalcEtfSaleResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_etfIdNotSet_emptyResponse() throws Exception {

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
		request.setEtfId(SETTING_ETF_ID);
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
		request.setEtfId(SETTING_ETF_ID);
		request.setPieces(SETTING_SALE_PIECES);

		final CalcEtfSaleResponse expected = new CalcEtfSaleResponse();
		final CalcEtfSaleResponse actual = super.callUsecaseExpect200(request, CalcEtfSaleResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_invalidEtfIdWith0Pieces_emptyResponse() throws Exception {

		final CalcEtfSaleRequest request = new CalcEtfSaleRequest();
		request.setAskPrice(SETTING_SALE_ASK_PRICE);
		request.setBidPrice(SETTING_SALE_BID_PRICE);
		request.setEtfId(EtfTransportBuilder.NON_EXISTING_ID);
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