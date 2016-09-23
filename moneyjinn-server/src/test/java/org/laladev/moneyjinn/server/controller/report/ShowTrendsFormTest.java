package org.laladev.moneyjinn.server.controller.report;

import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.report.ShowTrendsFormResponse;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.setting.ClientTrendCapitalsourceIDsSetting;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.IMonthlySettlementService;
import org.laladev.moneyjinn.service.api.ISettingService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class ShowTrendsFormTest extends AbstractControllerTest {

	@Inject
	ISettingService settingService;
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

	private ShowTrendsFormResponse getDefaultResponse() {
		final ShowTrendsFormResponse expected = new ShowTrendsFormResponse();

		final List<CapitalsourceTransport> capitalsourceTransports = new ArrayList<>();
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource1().build());
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource2().build());
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource3().build());
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource4().build());
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource5().build());
		expected.setCapitalsourceTransports(capitalsourceTransports);

		expected.setAllYears(Arrays.asList((short) 2008, (short) 2009, (short) 2010));
		return expected;
	}

	@Test
	public void test_TestLastSettlementInDecemberButFlowsInNextYear_nextYearIncludedInAllYears() throws Exception {
		final UserID userId1 = new UserID(UserTransportBuilder.USER1_ID);
		final UserID userId3 = new UserID(UserTransportBuilder.USER3_ID);

		this.monthlySettlementService.deleteMonthlySettlement(userId1, (short) 2010, Month.JANUARY);
		this.monthlySettlementService.deleteMonthlySettlement(userId1, (short) 2010, Month.FEBRUARY);
		this.monthlySettlementService.deleteMonthlySettlement(userId1, (short) 2010, Month.MARCH);
		this.monthlySettlementService.deleteMonthlySettlement(userId1, (short) 2010, Month.APRIL);
		this.monthlySettlementService.deleteMonthlySettlement(userId3, (short) 2010, Month.JANUARY);
		this.monthlySettlementService.deleteMonthlySettlement(userId3, (short) 2010, Month.FEBRUARY);
		this.monthlySettlementService.deleteMonthlySettlement(userId3, (short) 2010, Month.MARCH);
		this.monthlySettlementService.deleteMonthlySettlement(userId3, (short) 2010, Month.APRIL);

		final ShowTrendsFormResponse expected = this.getDefaultResponse();

		final ShowTrendsFormResponse actual = super.callUsecaseWithoutContent("", this.method, false,
				ShowTrendsFormResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_noSetting_defaultsResponse() throws Exception {
		final ShowTrendsFormResponse expected = this.getDefaultResponse();

		final ShowTrendsFormResponse actual = super.callUsecaseWithoutContent("", this.method, false,
				ShowTrendsFormResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_witDefaultSelection_defaultsResponse() throws Exception {
		final ClientTrendCapitalsourceIDsSetting setting = new ClientTrendCapitalsourceIDsSetting(
				Arrays.asList(new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID),
						new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID),
						new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE5_ID)));
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		this.settingService.setClientTrendCapitalsourceIDsSetting(userId, setting);

		final ShowTrendsFormResponse expected = this.getDefaultResponse();
		expected.setSettingTrendCapitalsourceIds(Arrays.asList(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID,
				CapitalsourceTransportBuilder.CAPITALSOURCE2_ID, CapitalsourceTransportBuilder.CAPITALSOURCE5_ID));

		final ShowTrendsFormResponse actual = super.callUsecaseWithoutContent("", this.method, false,
				ShowTrendsFormResponse.class);

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
		super.callUsecaseWithoutContent("", this.method, false, ShowTrendsFormResponse.class);
	}
}