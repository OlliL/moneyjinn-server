
package org.laladev.moneyjinn.server.controller.etf;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSaleAskPrice;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSaleBidPrice;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSalePieces;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSaleTransactionCostsAbsolute;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSaleTransactionCostsRelative;
import org.laladev.moneyjinn.server.builder.EtfEffectiveFlowTransportBuilder;
import org.laladev.moneyjinn.server.builder.EtfFlowTransportBuilder;
import org.laladev.moneyjinn.server.builder.EtfTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.EtfControllerApi;
import org.laladev.moneyjinn.server.model.EtfEffectiveFlowTransport;
import org.laladev.moneyjinn.server.model.EtfFlowTransport;
import org.laladev.moneyjinn.server.model.EtfSummaryTransport;
import org.laladev.moneyjinn.server.model.EtfTransport;
import org.laladev.moneyjinn.server.model.ListEtfFlowsResponse;
import org.laladev.moneyjinn.service.api.ISettingService;

import jakarta.inject.Inject;

class ListEtfFlowsByIdTest extends AbstractWebUserControllerTest {
	@Inject
	private ISettingService settingService;

	private final static BigDecimal SETTING_SALE_ASK_PRICE = new BigDecimal("800.000");
	private final static BigDecimal SETTING_SALE_BID_PRICE = new BigDecimal("879.500");
	private final static BigDecimal SETTING_SALE_PIECES = new BigDecimal("10");
	private final static BigDecimal SETTING_SALE_TRANSACTION_COSTS_ABSOLUTE = new BigDecimal("0.99");
	private final static BigDecimal SETTING_SALE_TRANSACTION_COSTS_RELATIVE = new BigDecimal("0.25");

	@Override
	protected void loadMethod() {
		super.getMock(EtfControllerApi.class).listEtfFlowsById(null);
	}

	private UserID getUserId() {
		return new UserID(UserTransportBuilder.USER1_ID);
	}

	private void initEtfSettings() {
		this.settingService.setClientCalcEtfSaleAskPrice(this.getUserId(),
				new ClientCalcEtfSaleAskPrice(SETTING_SALE_ASK_PRICE));
		this.settingService.setClientCalcEtfSaleBidPrice(this.getUserId(),
				new ClientCalcEtfSaleBidPrice(SETTING_SALE_BID_PRICE));
		this.settingService.setClientCalcEtfSalePieces(this.getUserId(),
				new ClientCalcEtfSalePieces(SETTING_SALE_PIECES));
		this.settingService.setClientCalcEtfSaleTransactionCostsAbsolute(this.getUserId(),
				new ClientCalcEtfSaleTransactionCostsAbsolute(SETTING_SALE_TRANSACTION_COSTS_ABSOLUTE));
		this.settingService.setClientCalcEtfSaleTransactionCostsRelative(this.getUserId(),
				new ClientCalcEtfSaleTransactionCostsRelative(SETTING_SALE_TRANSACTION_COSTS_RELATIVE));
	}

	private ListEtfFlowsResponse getResponseForEtf1() {
		final ListEtfFlowsResponse expected = new ListEtfFlowsResponse();

		final List<EtfFlowTransport> allTransports = new ArrayList<>();
		allTransports.add(new EtfFlowTransportBuilder().forFlow11().build());
		allTransports.add(new EtfFlowTransportBuilder().forFlow10().build());
		allTransports.add(new EtfFlowTransportBuilder().forFlow9().build());
		allTransports.add(new EtfFlowTransportBuilder().forFlow8().build());
		allTransports.add(new EtfFlowTransportBuilder().forFlow7().build());
		allTransports.add(new EtfFlowTransportBuilder().forFlow6().build());
		allTransports.add(new EtfFlowTransportBuilder().forFlow5().build());
		allTransports.add(new EtfFlowTransportBuilder().forFlow4().build());
		allTransports.add(new EtfFlowTransportBuilder().forFlow3().build());
		allTransports.add(new EtfFlowTransportBuilder().forFlow2().build());
		allTransports.add(new EtfFlowTransportBuilder().forFlow1().build());
		expected.setEtfFlowTransports(allTransports);

		final List<EtfEffectiveFlowTransport> effectiveTransports = new ArrayList<>();
		effectiveTransports.add(new EtfEffectiveFlowTransportBuilder().forFlow11().build());
		effectiveTransports.add(new EtfEffectiveFlowTransportBuilder().forFlow9().build());
		effectiveTransports.add(new EtfEffectiveFlowTransportBuilder().forFlow8().build());
		effectiveTransports.add(new EtfEffectiveFlowTransportBuilder().forFlow6().build());
		expected.setEtfEffectiveFlowTransports(effectiveTransports);

		final EtfTransport etf = new EtfTransportBuilder().forEtf1().build();
		final EtfSummaryTransport transport = new EtfSummaryTransport();
		final BigDecimal amount = new BigDecimal("193.23400");
		final BigDecimal spentValue = new BigDecimal("149583.30078200");
		transport.setEtfId(etf.getEtfId());
		transport.setName(etf.getName());
		transport.setChartUrl(etf.getChartUrl());
		transport.setAmount(amount);
		transport.setSpentValue(spentValue);
		// latest etfvalues table entry
		transport.setBuyPrice(new BigDecimal("666.000"));
		transport.setSellPrice(new BigDecimal("666.543"));
		transport.setPricesTimestamp(ZonedDateTime.of(2012, 01, 16, 22, 5, 2, 0, ZoneId.systemDefault()).toInstant()
				.atOffset(ZoneOffset.UTC));
		expected.setEtfSummaryTransport(transport);

		return expected;
	}

	@Test
	void test_standardRequestWithoutSettings_FullResponseObject() throws Exception {
		final ListEtfFlowsResponse expected = this.getResponseForEtf1();

		final ListEtfFlowsResponse actual = super.callUsecaseExpect200(ListEtfFlowsResponse.class,
				EtfTransportBuilder.ETF_ID_1);

		Assertions.assertEquals(expected, actual);

	}

	@Test
	void test_standardRequestWithSettings_FullResponseObject() throws Exception {
		final ListEtfFlowsResponse expected = this.getResponseForEtf1();

		this.initEtfSettings();

		expected.setCalcEtfAskPrice(SETTING_SALE_ASK_PRICE);
		expected.setCalcEtfBidPrice(SETTING_SALE_BID_PRICE);
		expected.setCalcEtfSalePieces(SETTING_SALE_PIECES);
		expected.setCalcEtfTransactionCostsAbsolute(SETTING_SALE_TRANSACTION_COSTS_ABSOLUTE);
		expected.setCalcEtfTransactionCostsRelative(SETTING_SALE_TRANSACTION_COSTS_RELATIVE);

		final ListEtfFlowsResponse actual = super.callUsecaseExpect200(ListEtfFlowsResponse.class,
				EtfTransportBuilder.ETF_ID_1);

		Assertions.assertEquals(expected, actual);

	}

	@Test
	void test_notExisting_NotFoundRaised() throws Exception {
		super.callUsecaseExpect404(EtfTransportBuilder.NON_EXISTING_ID);
	}

	@Test
	void test_etfFromOtherGroup_nothingHappens() throws Exception {
		super.setUsername(UserTransportBuilder.ADMIN_NAME);
		super.setPassword(UserTransportBuilder.ADMIN_PASSWORD);

		super.callUsecaseExpect404(EtfTransportBuilder.ETF_ID_1);
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403WithUriVariables(EtfTransportBuilder.NON_EXISTING_ID);
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		super.callUsecaseExpect404(EtfTransportBuilder.NON_EXISTING_ID);
	}
}