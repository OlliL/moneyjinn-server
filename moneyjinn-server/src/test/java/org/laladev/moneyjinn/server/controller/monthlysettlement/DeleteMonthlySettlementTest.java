
package org.laladev.moneyjinn.server.controller.monthlysettlement;

import java.time.Month;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.monthlysettlement.MonthlySettlement;
import org.laladev.moneyjinn.server.builder.MonthlySettlementTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.MonthlySettlementControllerApi;
import org.laladev.moneyjinn.service.api.IMonthlySettlementService;

import jakarta.inject.Inject;

class DeleteMonthlySettlementTest extends AbstractWebUserControllerTest {
	@Inject
	private IMonthlySettlementService monthlySettlementService;

	@Override
	protected void loadMethod() {
		super.getMock(MonthlySettlementControllerApi.class).deleteMonthlySettlement(null, null);
	}

	@Test
	void test_regularMonthlySettlement_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		List<MonthlySettlement> monthlySettlements = this.monthlySettlementService
				.getAllMonthlySettlementsByYearMonth(userId, 2008, Month.DECEMBER);
		Assertions.assertNotNull(monthlySettlements);
		Assertions.assertEquals(3, monthlySettlements.size());
		super.callUsecaseExpect204WithUriVariables(2008, 12);
		monthlySettlements = this.monthlySettlementService.getAllMonthlySettlementsByYearMonth(userId, 2008,
				Month.DECEMBER);
		Assertions.assertNotNull(monthlySettlements);
		Assertions.assertEquals(1, monthlySettlements.size());
		Assertions.assertEquals(MonthlySettlementTransportBuilder.MONTHLYSETTLEMENT3_ID,
				monthlySettlements.iterator().next().getId().getId());
	}

	@Test
	void test_nonExistingMonthlySettlement_SuccessfullNoContent() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		List<MonthlySettlement> monthlySettlements = this.monthlySettlementService
				.getAllMonthlySettlementsByYearMonth(userId, 1970, Month.OCTOBER);
		Assertions.assertNotNull(monthlySettlements);
		Assertions.assertTrue(monthlySettlements.isEmpty());
		super.callUsecaseExpect204WithUriVariables(1970, 10);
		monthlySettlements = this.monthlySettlementService.getAllMonthlySettlementsByYearMonth(userId, 1970,
				Month.OCTOBER);
		Assertions.assertNotNull(monthlySettlements);
		Assertions.assertTrue(monthlySettlements.isEmpty());
	}

	@Test
	void test_MonthlySettlementFromDifferentGroup_notSuccessfull() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.ADMIN_ID);
		List<MonthlySettlement> monthlySettlements = this.monthlySettlementService
				.getAllMonthlySettlementsByYearMonth(userId, 2008, Month.DECEMBER);
		Assertions.assertNotNull(monthlySettlements);
		Assertions.assertTrue(monthlySettlements.isEmpty());

		super.callUsecaseExpect204WithUriVariables(2008, 12);

		monthlySettlements = this.monthlySettlementService.getAllMonthlySettlementsByYearMonth(userId, 2008,
				Month.DECEMBER);
		Assertions.assertNotNull(monthlySettlements);
		Assertions.assertTrue(monthlySettlements.isEmpty());
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403WithUriVariables(2008, 12);
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		super.callUsecaseExpect204WithUriVariables(2008, 12);
	}
}