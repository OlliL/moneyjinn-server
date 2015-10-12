package org.laladev.moneyjinn.server.controller.monthlysettlement;

import java.math.BigDecimal;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.monthlysettlement.MonthlySettlement;
import org.laladev.moneyjinn.businesslogic.service.api.IMonthlySettlementService;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import org.laladev.moneyjinn.core.rest.model.monthlysettlement.UpsertMonthlySettlementRequest;
import org.laladev.moneyjinn.core.rest.model.transport.MonthlySettlementTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.MonthlySettlementTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

// TODO Check that Settlements may only be created for owned Capitalsources.
public class UpsertMonthlySettlementTest extends AbstractControllerTest {

	@Inject
	IMonthlySettlementService monthlySettlementService;

	private final HttpMethod method = HttpMethod.POST;
	private String userName;
	private String userPassword;

	@Before
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

	private void testError(final List<MonthlySettlementTransport> transports, final ErrorCode errorCode)
			throws Exception {
		final UpsertMonthlySettlementRequest request = new UpsertMonthlySettlementRequest();

		request.setMonthlySettlementTransports(transports);

		final List<ValidationItemTransport> validationItems = new ArrayList<>();
		validationItems.add(new ValidationItemTransportBuilder().withKey(transports.get(0).getId().intValue())
				.withError(errorCode.getErrorCode()).build());

		final ValidationResponse expected = new ValidationResponse();
		expected.setValidationItemTransports(validationItems);
		expected.setResult(Boolean.FALSE);

		final ValidationResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				ValidationResponse.class);

		Assert.assertEquals(expected, actual);

	}

	@Test
	public void test_regularMonthlySettlementInsert_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);

		final UpsertMonthlySettlementRequest request = new UpsertMonthlySettlementRequest();
		final MonthlySettlementTransport monthlySettlementTransport = new MonthlySettlementTransportBuilder()
				.forNewMonthlySettlement().build();
		request.setMonthlySettlementTransports(Arrays.asList(monthlySettlementTransport));

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final List<MonthlySettlement> monthlySettlements = this.monthlySettlementService
				.getAllMonthlySettlementsByYearMonth(userId, monthlySettlementTransport.getYear(),
						Month.of(monthlySettlementTransport.getMonth().intValue()));

		Assert.assertNotNull(monthlySettlements);
		Assert.assertEquals(1, monthlySettlements.size());
		Assert.assertEquals(monthlySettlementTransport.getYear(), monthlySettlements.iterator().next().getYear());
		Assert.assertEquals(monthlySettlementTransport.getMonth().intValue(),
				monthlySettlements.iterator().next().getMonth().getValue());
	}

	@Test
	public void test_MonthlySettlementForOtherUserUpdate_Error() throws Exception {
		final MonthlySettlementTransport monthlySettlementTransport = new MonthlySettlementTransportBuilder()
				.forMonthlySettlement3().build();

		this.testError(Arrays.asList(monthlySettlementTransport), ErrorCode.CAPITALSOURCE_DOES_NOT_EXIST);
	}

	@Test
	public void test_notOwnedCapitalsource_Error() throws Exception {
		final MonthlySettlementTransport monthlySettlementTransport = new MonthlySettlementTransportBuilder()
				.forMonthlySettlement1().build();
		monthlySettlementTransport.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE3_ID);

		this.testError(Arrays.asList(monthlySettlementTransport), ErrorCode.CAPITALSOURCE_DOES_NOT_EXIST);
	}

	@Test
	public void test_regularMonthlySettlementUpdate_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);

		final UpsertMonthlySettlementRequest request = new UpsertMonthlySettlementRequest();
		final List<MonthlySettlementTransport> monthlySettlementTransports = new ArrayList<>();
		final MonthlySettlementTransport monthlySettlementTransport1 = new MonthlySettlementTransportBuilder()
				.forMonthlySettlement1().withAmount(BigDecimal.ZERO).build();
		final MonthlySettlementTransport monthlySettlementTransport2 = new MonthlySettlementTransportBuilder()
				.forMonthlySettlement2().withAmount(BigDecimal.ONE).build();

		monthlySettlementTransports.add(monthlySettlementTransport1);
		monthlySettlementTransports.add(monthlySettlementTransport2);

		request.setMonthlySettlementTransports(monthlySettlementTransports);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final List<MonthlySettlement> monthlySettlements = this.monthlySettlementService
				.getAllMonthlySettlementsByYearMonth(userId, monthlySettlementTransports.get(0).getYear(),
						Month.of(monthlySettlementTransports.get(0).getMonth().intValue()));

		Assert.assertNotNull(monthlySettlements);
		Assert.assertEquals(3, monthlySettlements.size());
		Assert.assertEquals(0,
				monthlySettlementTransport1.getAmount().compareTo(monthlySettlements.get(0).getAmount()));
		Assert.assertEquals(0,
				monthlySettlementTransport2.getAmount().compareTo(monthlySettlements.get(1).getAmount()));
		Assert.assertEquals(0, new MonthlySettlementTransportBuilder().forMonthlySettlement3().build().getAmount()
				.compareTo(monthlySettlements.get(2).getAmount()));
	}

	@Test
	public void test_AuthorizationRequired_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("", this.method, false, ErrorResponse.class);
		Assert.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	public void test_emptyDatabase_noException() throws Exception {
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
		final MonthlySettlementTransport monthlySettlementTransport = new MonthlySettlementTransportBuilder()
				.forMonthlySettlement3().build();

		this.testError(Arrays.asList(monthlySettlementTransport), ErrorCode.CAPITALSOURCE_DOES_NOT_EXIST);
	}

}