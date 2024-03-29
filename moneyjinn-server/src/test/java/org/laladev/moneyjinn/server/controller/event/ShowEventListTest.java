
package org.laladev.moneyjinn.server.controller.event;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowID;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowStatus;
import org.laladev.moneyjinn.model.monthlysettlement.MonthlySettlement;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.ImportedMoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.EventControllerApi;
import org.laladev.moneyjinn.server.model.ShowEventListResponse;
import org.laladev.moneyjinn.service.api.IImportedMoneyflowService;
import org.laladev.moneyjinn.service.api.IMonthlySettlementService;

import jakarta.inject.Inject;

class ShowEventListTest extends AbstractWebUserControllerTest {
	@Inject
	private IMonthlySettlementService monthlySettlementService;
	@Inject
	private IImportedMoneyflowService importedMoneyflowService;

	@Override
	protected void loadMethod() {
		super.getMock(EventControllerApi.class).showEventList();
	}

	@Test
	void test_previousMonthIsNotSettled_completeResponseObject() throws Exception {
		final ShowEventListResponse expected = new ShowEventListResponse();
		final LocalDate lastMonth = LocalDate.now().minusMonths(1l);
		expected.setMonthlySettlementMissing(true);
		expected.setMonthlySettlementMonth((lastMonth.getMonthValue()));
		expected.setMonthlySettlementYear((lastMonth.getYear()));
		expected.setNumberOfImportedMoneyflows(2);

		final ShowEventListResponse actual = super.callUsecaseExpect200(ShowEventListResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_previousMonthIsSettled_completeResponseObject() throws Exception {
		final ShowEventListResponse expected = new ShowEventListResponse();
		final LocalDate lastMonth = LocalDate.now().minusMonths(1l);
		final MonthlySettlement monthlySettlement = new MonthlySettlement();
		monthlySettlement.setYear(lastMonth.getYear());
		monthlySettlement.setMonth(lastMonth.getMonth());
		monthlySettlement.setCapitalsource(
				new Capitalsource(new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID)));
		monthlySettlement.setAmount(BigDecimal.TEN);
		monthlySettlement.setUser(new User(new UserID(UserTransportBuilder.USER1_ID)));
		monthlySettlement.setGroup(new Group(new GroupID(GroupTransportBuilder.GROUP1_ID)));
		this.monthlySettlementService.upsertMonthlySettlements(Arrays.asList(monthlySettlement));
		expected.setMonthlySettlementMissing(false);
		expected.setMonthlySettlementMonth((lastMonth.getMonthValue()));
		expected.setMonthlySettlementYear((lastMonth.getYear()));
		expected.setNumberOfImportedMoneyflows(2);

		final ShowEventListResponse actual = super.callUsecaseExpect200(ShowEventListResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_deletedImportedMoneyflow_isIgnored() throws Exception {

		ShowEventListResponse actual = super.callUsecaseExpect200(ShowEventListResponse.class);

		Assertions.assertEquals(Integer.valueOf(2), actual.getNumberOfImportedMoneyflows());
		this.importedMoneyflowService.deleteImportedMoneyflowById(new UserID(UserTransportBuilder.USER1_ID),
				new ImportedMoneyflowID(ImportedMoneyflowTransportBuilder.IMPORTED_MONEYFLOW1_ID));

		actual = super.callUsecaseExpect200(ShowEventListResponse.class);

		Assertions.assertEquals(Integer.valueOf(1), actual.getNumberOfImportedMoneyflows());
	}

	@Test
	void test_importedImportedMoneyflow_isIgnored() throws Exception {

		ShowEventListResponse actual = super.callUsecaseExpect200(ShowEventListResponse.class);

		final int numberOfImportedMoneyflows = actual.getNumberOfImportedMoneyflows().intValue();
		this.importedMoneyflowService.updateImportedMoneyflowStatus(new UserID(UserTransportBuilder.USER1_ID),
				new ImportedMoneyflowID(ImportedMoneyflowTransportBuilder.IMPORTED_MONEYFLOW1_ID),
				ImportedMoneyflowStatus.PROCESSED);

		actual = super.callUsecaseExpect200(ShowEventListResponse.class);

		Assertions.assertEquals(Integer.valueOf(numberOfImportedMoneyflows - 1),
				actual.getNumberOfImportedMoneyflows());
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403();
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		super.callUsecaseExpect200(ShowEventListResponse.class);
	}
}