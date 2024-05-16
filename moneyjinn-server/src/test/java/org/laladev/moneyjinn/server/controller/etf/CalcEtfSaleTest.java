
package org.laladev.moneyjinn.server.controller.etf;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.server.builder.EtfTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.EtfControllerApi;
import org.laladev.moneyjinn.server.model.CalcEtfSaleRequest;
import org.laladev.moneyjinn.server.model.CalcEtfSaleResponse;
import org.laladev.moneyjinn.server.model.ValidationResponse;

class CalcEtfSaleTest extends AbstractWebUserControllerTest {

	private final static BigDecimal SETTING_SALE_ASK_PRICE = new BigDecimal("900.000");
	private final static BigDecimal SETTING_SALE_BID_PRICE = new BigDecimal("899.500");
	private final static Long SETTING_ETF_ID = EtfTransportBuilder.ETF_ID_1;
	private final static BigDecimal SETTING_SALE_PIECES = new BigDecimal("10");
	private final static BigDecimal SETTING_SALE_TRANSACTION_COSTS_ABSOLUTE = new BigDecimal("0.99");
	private final static BigDecimal SETTING_SALE_TRANSACTION_COSTS_RELATIVE = new BigDecimal("0.25");

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
		request.setTransactionCostsAbsolute(SETTING_SALE_TRANSACTION_COSTS_ABSOLUTE);
		request.setTransactionCostsRelative(SETTING_SALE_TRANSACTION_COSTS_RELATIVE);

		final CalcEtfSaleResponse expected = this.getStandardRequestExpected();
		final CalcEtfSaleResponse actual = super.callUsecaseExpect200(request, CalcEtfSaleResponse.class);

		Assertions.assertEquals(expected, actual);

	}

	@Test
	void test_highOrderValue_maxTransactionCost() throws Exception {

		final CalcEtfSaleRequest request = new CalcEtfSaleRequest();
		request.setAskPrice(new BigDecimal("10000000"));
		request.setBidPrice(new BigDecimal("10000000"));
		request.setEtfId(SETTING_ETF_ID);
		request.setPieces(SETTING_SALE_PIECES);
		request.setTransactionCostsAbsolute(SETTING_SALE_TRANSACTION_COSTS_ABSOLUTE);
		request.setTransactionCostsRelative(SETTING_SALE_TRANSACTION_COSTS_RELATIVE);
		request.setTransactionCostsMaximum(new BigDecimal("10.00"));

		final CalcEtfSaleResponse actual = super.callUsecaseExpect200(request, CalcEtfSaleResponse.class);

		final BigDecimal maxRelative = new BigDecimal("9.01");
		Assertions.assertEquals(maxRelative, actual.getTransactionCostsRelativeBuy());
		Assertions.assertEquals(maxRelative, actual.getTransactionCostsRelativeSell());

	}

	@Test
	void test_etfOwnedByDifferentGroup_EmptyResponse() throws Exception {
		super.setUsername(UserTransportBuilder.ADMIN_NAME);
		super.setPassword(UserTransportBuilder.ADMIN_PASSWORD);

		final CalcEtfSaleRequest request = new CalcEtfSaleRequest();
		request.setAskPrice(SETTING_SALE_ASK_PRICE);
		request.setBidPrice(SETTING_SALE_BID_PRICE);
		request.setEtfId(SETTING_ETF_ID);
		request.setPieces(SETTING_SALE_PIECES);
		request.setTransactionCostsAbsolute(SETTING_SALE_TRANSACTION_COSTS_ABSOLUTE);

		final CalcEtfSaleResponse expected = new CalcEtfSaleResponse();
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
		request.setTransactionCostsAbsolute(SETTING_SALE_TRANSACTION_COSTS_ABSOLUTE);
		request.setTransactionCostsRelative(SETTING_SALE_TRANSACTION_COSTS_RELATIVE);

		final ValidationResponse actual = super.callUsecaseExpect422(request, ValidationResponse.class);

		Assertions.assertFalse(actual.getResult());
		Assertions.assertEquals(ErrorCode.AMOUNT_TO_HIGH.getErrorCode(),
				actual.getValidationItemTransports().getFirst().getError());

	}

	@Test
	void test_standardRequestNegativeInput_AbsoluteValuesAreUsed() throws Exception {

		final CalcEtfSaleRequest request = new CalcEtfSaleRequest();
		request.setAskPrice(SETTING_SALE_ASK_PRICE.negate());
		request.setBidPrice(SETTING_SALE_BID_PRICE.negate());
		request.setEtfId(SETTING_ETF_ID);
		request.setPieces(SETTING_SALE_PIECES.negate());
		request.setTransactionCostsAbsolute(SETTING_SALE_TRANSACTION_COSTS_ABSOLUTE);
		request.setTransactionCostsRelative(SETTING_SALE_TRANSACTION_COSTS_RELATIVE);

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
		expected.setTransactionCostsAbsoluteSell(new BigDecimal("0.99"));
		expected.setTransactionCostsRelativeSell(new BigDecimal("22.49"));
		expected.setTransactionCostsAbsoluteBuy(new BigDecimal("0.99"));
		expected.setTransactionCostsRelativeBuy(new BigDecimal("22.50"));
		expected.setRebuyLosses(new BigDecimal("5.00"));
		expected.setOverallCosts(new BigDecimal("51.97"));
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
		request.setTransactionCostsAbsolute(SETTING_SALE_TRANSACTION_COSTS_ABSOLUTE);
		request.setTransactionCostsRelative(SETTING_SALE_TRANSACTION_COSTS_RELATIVE);

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
		request.setTransactionCostsAbsolute(SETTING_SALE_TRANSACTION_COSTS_ABSOLUTE);
		request.setTransactionCostsRelative(SETTING_SALE_TRANSACTION_COSTS_RELATIVE);

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
		request.setTransactionCostsAbsolute(SETTING_SALE_TRANSACTION_COSTS_ABSOLUTE);
		request.setTransactionCostsRelative(SETTING_SALE_TRANSACTION_COSTS_RELATIVE);

		final BigDecimal expectedSum = new BigDecimal("291.06");
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
		request.setTransactionCostsAbsolute(SETTING_SALE_TRANSACTION_COSTS_ABSOLUTE);

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
		request.setTransactionCostsAbsolute(SETTING_SALE_TRANSACTION_COSTS_ABSOLUTE);

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
		request.setTransactionCostsAbsolute(SETTING_SALE_TRANSACTION_COSTS_ABSOLUTE);

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
		request.setTransactionCostsAbsolute(SETTING_SALE_TRANSACTION_COSTS_ABSOLUTE);

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
		request.setTransactionCostsAbsolute(SETTING_SALE_TRANSACTION_COSTS_ABSOLUTE);

		final CalcEtfSaleResponse expected = new CalcEtfSaleResponse();
		final CalcEtfSaleResponse actual = super.callUsecaseExpect200(request, CalcEtfSaleResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_transactionCostsNotSet_0assumed() throws Exception {

		final CalcEtfSaleRequest request = new CalcEtfSaleRequest();
		request.setAskPrice(SETTING_SALE_ASK_PRICE);
		request.setBidPrice(SETTING_SALE_BID_PRICE);
		request.setEtfId(SETTING_ETF_ID);
		request.setPieces(SETTING_SALE_PIECES);

		final CalcEtfSaleResponse expected = this.getStandardRequestExpected();
		expected.setTransactionCostsAbsoluteBuy(new BigDecimal("0"));
		expected.setTransactionCostsAbsoluteSell(new BigDecimal("0"));
		expected.setTransactionCostsRelativeBuy(new BigDecimal("0.00"));
		expected.setTransactionCostsRelativeSell(new BigDecimal("0.00"));
		expected.setRebuyLosses(new BigDecimal("5.00"));
		expected.setOverallCosts(new BigDecimal("5.00"));
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
		request.setTransactionCostsAbsolute(SETTING_SALE_TRANSACTION_COSTS_ABSOLUTE);

		final CalcEtfSaleResponse expected = new CalcEtfSaleResponse();
		final CalcEtfSaleResponse actual = super.callUsecaseExpect200(request, CalcEtfSaleResponse.class);

		Assertions.assertEquals(expected, actual);

	}

	@Test
	void test_etfFromOtherGroup_nothingHappens() throws Exception {
		super.setUsername(UserTransportBuilder.ADMIN_NAME);
		super.setPassword(UserTransportBuilder.ADMIN_PASSWORD);
		final CalcEtfSaleRequest request = new CalcEtfSaleRequest();
		request.setAskPrice(SETTING_SALE_ASK_PRICE);
		request.setBidPrice(SETTING_SALE_BID_PRICE);
		request.setEtfId(SETTING_ETF_ID);
		request.setPieces(SETTING_SALE_PIECES);
		request.setTransactionCostsAbsolute(SETTING_SALE_TRANSACTION_COSTS_ABSOLUTE);
		request.setTransactionCostsRelative(SETTING_SALE_TRANSACTION_COSTS_RELATIVE);

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