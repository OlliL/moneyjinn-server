
package org.laladev.moneyjinn.server.controller.etf;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.EtfEffectiveFlowTransportBuilder;
import org.laladev.moneyjinn.server.builder.EtfTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.controller.api.EtfControllerApi;
import org.laladev.moneyjinn.server.model.EtfEffectiveFlowTransport;
import org.laladev.moneyjinn.server.model.EtfSummaryTransport;
import org.laladev.moneyjinn.server.model.EtfTransport;
import org.laladev.moneyjinn.server.model.ListEtfOverviewResponse;
import org.springframework.test.context.jdbc.Sql;

class ListEtfOverviewTest extends AbstractControllerTest {
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
		super.getMock(EtfControllerApi.class).listEtfOverview(null, null);
	}

	@Test
	void test_standardRequest_FullResponseObject() throws Exception {
		final EtfTransport etf = new EtfTransportBuilder().forEtf1().build();
		final EtfEffectiveFlowTransport flow1 = new EtfEffectiveFlowTransportBuilder().forFlow1().build();
		final EtfEffectiveFlowTransport flow3 = new EtfEffectiveFlowTransportBuilder().forFlow3().build();

		final ListEtfOverviewResponse expected = new ListEtfOverviewResponse();

		final EtfSummaryTransport transport = new EtfSummaryTransport();
		final BigDecimal amount = flow1.getAmount().add(flow3.getAmount());
		final BigDecimal spentValue = flow1.getAmount().multiply(flow1.getPrice())
				.add(flow3.getAmount().multiply(flow3.getPrice()));
		transport.setIsin(etf.getIsin());
		transport.setName(etf.getName());
		transport.setChartUrl(etf.getChartUrl());
		transport.setAmount(amount);
		transport.setSpentValue(spentValue);
		// latest etfvalues table entry
		transport.setBuyPrice(new BigDecimal("666.000"));
		transport.setSellPrice(new BigDecimal("666.543"));
		transport.setPricesTimestamp(ZonedDateTime.of(2008, 12, 16, 22, 5, 2, 0, ZoneId.systemDefault()).toInstant()
				.atOffset(ZoneOffset.UTC));

		expected.setEtfSummaryTransports(Collections.singletonList(transport));

		final ListEtfOverviewResponse actual = super.callUsecaseExpect200(ListEtfOverviewResponse.class, 2008, 12);

		Assertions.assertEquals(expected, actual);

	}

	@Test
	void test_ImportRoleNotAllowed_ErrorResponse() throws Exception {
		this.userName = UserTransportBuilder.IMPORTUSER_NAME;
		this.userPassword = UserTransportBuilder.IMPORTUSER_PASSWORD;

		super.callUsecaseExpect403WithUriVariables(2008, 12);
	}

	@Test
	void test_AuthorizationRequired_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;

		super.callUsecaseExpect403WithUriVariables(2008, 12);
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	void test_emptyDatabase_noException() throws Exception {
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;

		super.callUsecaseExpect200(ListEtfOverviewResponse.class, 2008, 12);

	}
}