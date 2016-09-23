package org.laladev.moneyjinn.server.controller.event;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.businesslogic.model.access.Group;
import org.laladev.moneyjinn.businesslogic.model.access.GroupID;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.businesslogic.model.monthlysettlement.MonthlySettlement;
import org.laladev.moneyjinn.businesslogic.service.api.IMonthlySettlementService;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.event.ShowEventListResponse;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class ShowEventListTest extends AbstractControllerTest {

	@Inject
	IMonthlySettlementService monthlySettlementService;

	private final HttpMethod method = HttpMethod.GET;
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
	public void test_previousMonthIsNotSettled_completeResponseObject() throws Exception {
		final ShowEventListResponse expected = new ShowEventListResponse();
		final LocalDate lastMonth = LocalDate.now().minusMonths(1l);
		expected.setMonthlySettlementMissing(true);
		expected.setMonthlySettlementMonth((short) (lastMonth.getMonthValue()));
		expected.setMonthlySettlementYear((short) (lastMonth.getYear()));
		expected.setMonthlySettlementNumberOfAddableSettlements(3);
		expected.setNumberOfImportedMoneyflows(2);

		final ShowEventListResponse actual = super.callUsecaseWithoutContent("", this.method, false,
				ShowEventListResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_previousMonthIsSettled_completeResponseObject() throws Exception {
		final ShowEventListResponse expected = new ShowEventListResponse();
		final LocalDate lastMonth = LocalDate.now().minusMonths(1l);

		final MonthlySettlement monthlySettlement = new MonthlySettlement();
		monthlySettlement.setYear((short) lastMonth.getYear());
		monthlySettlement.setMonth(lastMonth.getMonth());
		monthlySettlement.setCapitalsource(
				new Capitalsource(new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID)));
		monthlySettlement.setAmount(BigDecimal.TEN);
		monthlySettlement.setUser(new User(new UserID(UserTransportBuilder.USER1_ID)));
		monthlySettlement.setGroup(new Group(new GroupID(GroupTransportBuilder.GROUP1_ID)));
		this.monthlySettlementService.upsertMonthlySettlements(Arrays.asList(monthlySettlement));

		expected.setMonthlySettlementMissing(false);
		expected.setMonthlySettlementMonth((short) (lastMonth.getMonthValue()));
		expected.setMonthlySettlementYear((short) (lastMonth.getYear()));
		expected.setMonthlySettlementNumberOfAddableSettlements(3);
		expected.setNumberOfImportedMoneyflows(2);

		final ShowEventListResponse actual = super.callUsecaseWithoutContent("", this.method, false,
				ShowEventListResponse.class);

		Assert.assertEquals(expected, actual);
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
		super.callUsecaseWithoutContent("", this.method, false, ShowEventListResponse.class);
	}
}