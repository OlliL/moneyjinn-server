
package org.laladev.moneyjinn.server.controller.etf;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.EtfTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.EtfControllerApi;
import org.laladev.moneyjinn.server.model.EtfSummaryTransport;
import org.laladev.moneyjinn.server.model.EtfTransport;
import org.laladev.moneyjinn.server.model.ListEtfOverviewResponse;

class ListEtfOverviewTest extends AbstractWebUserControllerTest {

	@Override
	protected void loadMethod() {
		super.getMock(EtfControllerApi.class).listEtfOverview(null, null);
	}

	@Test
	void test_standardRequest_FullResponseObject() throws Exception {
		final EtfTransport etf = new EtfTransportBuilder().forEtf1().build();
		final ListEtfOverviewResponse expected = new ListEtfOverviewResponse();

		final EtfSummaryTransport transport = new EtfSummaryTransport();
		final BigDecimal amount = new BigDecimal("93.234");
		final BigDecimal spentValue = new BigDecimal("73212.348782");
		transport.setIsin(etf.getIsin());
		transport.setName(etf.getName());
		transport.setChartUrl(etf.getChartUrl());
		transport.setAmount(amount);
		transport.setSpentValue(spentValue);
		// latest etfvalues table entry
		transport.setBuyPrice(new BigDecimal("666.000"));
		transport.setSellPrice(new BigDecimal("666.543"));
		transport.setPricesTimestamp(ZonedDateTime.of(2012, 01, 16, 22, 5, 2, 0, ZoneId.systemDefault()).toInstant()
				.atOffset(ZoneOffset.UTC));

		expected.setEtfSummaryTransports(Collections.singletonList(transport));

		final ListEtfOverviewResponse actual = super.callUsecaseExpect200(ListEtfOverviewResponse.class, 2010, 1);

		Assertions.assertEquals(expected, actual);

	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403WithUriVariables(2008, 12);
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		super.callUsecaseExpect200(ListEtfOverviewResponse.class, 2008, 12);
	}
}