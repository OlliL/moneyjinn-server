
package org.laladev.moneyjinn.server.controller.monthlysettlement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.MonthlySettlementTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.controller.api.MonthlySettlementControllerApi;
import org.laladev.moneyjinn.server.controller.mapper.CapitalsourceTransportMapper;
import org.laladev.moneyjinn.server.model.MonthlySettlementTransport;
import org.laladev.moneyjinn.server.model.ShowMonthlySettlementCreateResponse;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.springframework.test.context.jdbc.Sql;

import jakarta.inject.Inject;

class ShowMonthlySettlementCreateTest extends AbstractControllerTest {
	@Inject
	private ICapitalsourceService capitalsourceService;
	@Inject
	private CapitalsourceTransportMapper capitalsourceTransportMapper;

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
		super.getMock(MonthlySettlementControllerApi.class).showMonthlySettlementCreate();
	}

	@Test
	void test_nextUnsettledMonthExplicitlyAndByDefault_FullContentWithCalculatedAmount() throws Exception {
		final ShowMonthlySettlementCreateResponse expected = new ShowMonthlySettlementCreateResponse();
		final List<MonthlySettlementTransport> monthlySettlementTransports = new ArrayList<>();
		monthlySettlementTransports.add(new MonthlySettlementTransportBuilder().forMonthlySettlement1().withId(null)
				.withYear(2010).withMonth(5).withAmount(BigDecimal.ZERO).build());
		monthlySettlementTransports.add(new MonthlySettlementTransportBuilder().forMonthlySettlement2().withId(null)
				.withYear(2010).withMonth(5).build());
		expected.setMonthlySettlementTransports(monthlySettlementTransports);
		expected.setYear(2010);
		expected.setMonth(5);
		expected.setEditMode(0);

		final ShowMonthlySettlementCreateResponse actual = super.callUsecaseExpect200(
				ShowMonthlySettlementCreateResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_nextUnsettledMonthWithImportedData_FullContentWithCalculatedAmount() throws Exception {
		this.userName = UserTransportBuilder.USER3_NAME;
		this.userPassword = UserTransportBuilder.USER3_PASSWORD;
		final ShowMonthlySettlementCreateResponse expected = new ShowMonthlySettlementCreateResponse();
		final List<MonthlySettlementTransport> importedMonthlySettlementTransports = new ArrayList<>();
		importedMonthlySettlementTransports.add(new MonthlySettlementTransportBuilder().forMonthlySettlement3()
				.withId(null).withYear(2010).withMonth(5).withAmount(new BigDecimal("9.00")).build());
		expected.setImportedMonthlySettlementTransports(importedMonthlySettlementTransports);
		final List<MonthlySettlementTransport> monthlySettlementTransports = new ArrayList<>();
		monthlySettlementTransports.add(new MonthlySettlementTransportBuilder().forCapitalsource6().build());
		expected.setMonthlySettlementTransports(monthlySettlementTransports);
		expected.setYear(2010);
		expected.setMonth(5);
		expected.setEditMode(0);

		final ShowMonthlySettlementCreateResponse actual = super.callUsecaseExpect200(
				ShowMonthlySettlementCreateResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_ImportRoleNotAllowed_ErrorResponse() throws Exception {
		this.userName = UserTransportBuilder.IMPORTUSER_NAME;
		this.userPassword = UserTransportBuilder.IMPORTUSER_PASSWORD;

		super.callUsecaseExpect403();
	}

	@Test
	void test_AuthorizationRequired_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;

		super.callUsecaseExpect403();
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	void test_emptyDatabase_noException() throws Exception {
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
		final ShowMonthlySettlementCreateResponse expected = new ShowMonthlySettlementCreateResponse();
		final LocalDate now = LocalDate.now();
		expected.setYear(now.getYear());
		expected.setMonth(now.getMonthValue());
		expected.setEditMode(0);

		final ShowMonthlySettlementCreateResponse actual = super.callUsecaseExpect200(
				ShowMonthlySettlementCreateResponse.class);

		Assertions.assertEquals(expected, actual);
	}
}