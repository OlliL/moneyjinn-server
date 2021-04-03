package org.laladev.moneyjinn.server.controller.monthlysettlement;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.monthlysettlement.ShowMonthlySettlementDeleteResponse;
import org.laladev.moneyjinn.core.rest.model.transport.MonthlySettlementTransport;
import org.laladev.moneyjinn.server.builder.MonthlySettlementTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class ShowMonthlySettlementDeleteTest extends AbstractControllerTest {

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

	@Test
	public void test_unknownMonthlySettlement_emptyResponseObject() throws Exception {
		final ShowMonthlySettlementDeleteResponse expected = new ShowMonthlySettlementDeleteResponse();
		final ShowMonthlySettlementDeleteResponse actual = super.callUsecaseWithoutContent("/1970/10", this.method,
				false, ShowMonthlySettlementDeleteResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	public void test_MonthlySettlement_completeResponseObject() throws Exception {
		final ShowMonthlySettlementDeleteResponse expected = new ShowMonthlySettlementDeleteResponse();

		final List<MonthlySettlementTransport> monthlySettlementTransports = new ArrayList<>();
		monthlySettlementTransports.add(new MonthlySettlementTransportBuilder().forMonthlySettlement1().build());
		monthlySettlementTransports.add(new MonthlySettlementTransportBuilder().forMonthlySettlement2().build());

		expected.setMonthlySettlementTransports(monthlySettlementTransports);

		final ShowMonthlySettlementDeleteResponse actual = super.callUsecaseWithoutContent("/2008/12", this.method,
				false, ShowMonthlySettlementDeleteResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	public void test_MonthlySettlement1AsDifferingUserOtherGroup_emptyResponseObject() throws Exception {
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
		final ShowMonthlySettlementDeleteResponse expected = new ShowMonthlySettlementDeleteResponse();

		final ShowMonthlySettlementDeleteResponse actual = super.callUsecaseWithoutContent("/2008/12", this.method,
				false, ShowMonthlySettlementDeleteResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	public void test_AuthorizationRequired_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("/2012/08", this.method, false,
				ErrorResponse.class);
		Assertions.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	public void test_emptyDatabase_noException() throws Exception {
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
		final ShowMonthlySettlementDeleteResponse expected = new ShowMonthlySettlementDeleteResponse();

		final ShowMonthlySettlementDeleteResponse actual = super.callUsecaseWithoutContent("/2008/12", this.method,
				false, ShowMonthlySettlementDeleteResponse.class);

		Assertions.assertEquals(expected, actual);
	}

}