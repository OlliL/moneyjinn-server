package org.laladev.moneyjinn.server.controller.monthlysettlement;

import java.time.Month;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.monthlysettlement.MonthlySettlement;
import org.laladev.moneyjinn.businesslogic.service.api.IMonthlySettlementService;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.server.builder.MonthlySettlementTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;

public class DeleteMonthlySettlementTest extends AbstractControllerTest {

	@Inject
	IMonthlySettlementService monthlySettlementService;

	private final HttpMethod method = HttpMethod.DELETE;
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

	@Test
	public void test_regularMonthlySettlement_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);

		List<MonthlySettlement> monthlySettlements = this.monthlySettlementService
				.getAllMonthlySettlementsByYearMonth(userId, (short) 2008, Month.DECEMBER);

		Assert.assertNotNull(monthlySettlements);
		Assert.assertEquals(3, monthlySettlements.size());

		super.callUsecaseWithoutContent("/2008/12", this.method, true, Object.class);

		monthlySettlements = this.monthlySettlementService.getAllMonthlySettlementsByYearMonth(userId, (short) 2008,
				Month.DECEMBER);

		Assert.assertNotNull(monthlySettlements);
		Assert.assertEquals(1, monthlySettlements.size());
		Assert.assertEquals(MonthlySettlementTransportBuilder.MONTHLYSETTLEMENT3_ID,
				monthlySettlements.iterator().next().getId().getId());
	}

	@Test
	public void test_nonExistingMonthlySettlement_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);

		List<MonthlySettlement> monthlySettlements = this.monthlySettlementService
				.getAllMonthlySettlementsByYearMonth(userId, (short) 1970, Month.OCTOBER);

		Assert.assertNotNull(monthlySettlements);
		Assert.assertTrue(monthlySettlements.isEmpty());

		super.callUsecaseWithoutContent("/1970/10", this.method, true, Object.class);

		monthlySettlements = this.monthlySettlementService.getAllMonthlySettlementsByYearMonth(userId, (short) 1970,
				Month.OCTOBER);

		Assert.assertNotNull(monthlySettlements);
		Assert.assertTrue(monthlySettlements.isEmpty());
	}

	@Test
	public void test_MonthlySettlementFromDifferentGroup_notSuccessfull() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.ADMIN_ID);
		List<MonthlySettlement> monthlySettlements = this.monthlySettlementService
				.getAllMonthlySettlementsByYearMonth(userId, (short) 2008, Month.DECEMBER);

		Assert.assertNotNull(monthlySettlements);
		Assert.assertTrue(monthlySettlements.isEmpty());

		super.callUsecaseWithoutContent("/2008/12", this.method, true, Object.class);

		monthlySettlements = this.monthlySettlementService.getAllMonthlySettlementsByYearMonth(userId, (short) 2008,
				Month.DECEMBER);

		Assert.assertNotNull(monthlySettlements);
		Assert.assertTrue(monthlySettlements.isEmpty());
	}

	@Test
	public void test_AuthorizationRequired_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("/2008/12", this.method, false,
				ErrorResponse.class);
		Assert.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

}