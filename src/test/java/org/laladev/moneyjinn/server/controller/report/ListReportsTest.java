package org.laladev.moneyjinn.server.controller.report;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.businesslogic.model.ImportedBalance;
import org.laladev.moneyjinn.businesslogic.model.access.GroupID;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.businesslogic.model.moneyflow.MoneyflowID;
import org.laladev.moneyjinn.businesslogic.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.businesslogic.service.api.IImportedBalanceService;
import org.laladev.moneyjinn.businesslogic.service.api.IMoneyflowService;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.report.ListReportsResponse;
import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ReportTurnoverCapitalsourceTransport;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.MoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.ReportTurnoverCapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

// TODO: Test "private" handling
public class ListReportsTest extends AbstractControllerTest {

	@Inject
	private IImportedBalanceService importedBalanceService;
	@Inject
	private ICapitalsourceService capitalsourceService;
	@Inject
	private IMoneyflowService moneyflowService;

	private static final List<Short> ALL_YEARS = Arrays.asList((short) 2008, (short) 2009, (short) 2010);
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
	public void test_noArgumentOrOnlyYear_defaultsResponse() throws Exception {
		final ListReportsResponse expected = new ListReportsResponse();
		expected.setYear((short) 2010);
		expected.setAllYears(ALL_YEARS);
		expected.setAllMonth(Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4, (short) 5));

		ListReportsResponse actual = super.callUsecaseWithoutContent("", this.method, false, ListReportsResponse.class);

		Assert.assertEquals(expected, actual);

		actual = super.callUsecaseWithoutContent("/2010", this.method, false, ListReportsResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_invalidMonth_defaultsResponse() throws Exception {
		final ListReportsResponse expected = new ListReportsResponse();
		expected.setYear((short) 2010);
		expected.setAllYears(ALL_YEARS);
		expected.setAllMonth(Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4, (short) 5));

		ListReportsResponse actual = super.callUsecaseWithoutContent("/2010/13", this.method, false,
				ListReportsResponse.class);

		Assert.assertEquals(expected, actual);

		actual = super.callUsecaseWithoutContent("/2010/0", this.method, false, ListReportsResponse.class);

		Assert.assertEquals(expected, actual);

		actual = super.callUsecaseWithoutContent("/2010/11", this.method, false, ListReportsResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_DecemberFirstMonthAtAllSettledAndAlsoPreviousMonthSettled_completeResponse() throws Exception {
		final ListReportsResponse expected = new ListReportsResponse();
		expected.setYear((short) 2008);
		expected.setAllYears(ALL_YEARS);
		expected.setAllMonth(Arrays.asList((short) 12));
		expected.setMonth((short) 12);

		final List<MoneyflowTransport> moneyflowTransports = new ArrayList<>();
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow2().build());
		expected.setMoneyflowTransports(moneyflowTransports);

		final List<ReportTurnoverCapitalsourceTransport> reportTurnoverCapitalsourceTransports = new ArrayList<>();
		reportTurnoverCapitalsourceTransports
				.add(new ReportTurnoverCapitalsourceTransportBuilder().forReport_2008_12_Capitalsource1().build());
		reportTurnoverCapitalsourceTransports
				.add(new ReportTurnoverCapitalsourceTransportBuilder().forReport_2008_12_Capitalsource2().build());
		reportTurnoverCapitalsourceTransports
				.add(new ReportTurnoverCapitalsourceTransportBuilder().forReport_2008_12_Capitalsource4().build());
		expected.setReportTurnoverCapitalsourceTransports(reportTurnoverCapitalsourceTransports);

		expected.setTurnoverEndOfYearCalculated(new BigDecimal("10.10"));
		expected.setAmountBeginOfYear(new BigDecimal("99.90"));

		expected.setNextMonth((short) 1);
		expected.setNextYear((short) 2009);

		expected.setNextMonthHasMoneyflows((short) 1);

		final ListReportsResponse actual = super.callUsecaseWithoutContent("/2008/12", this.method, false,
				ListReportsResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_JanuarySettledAndAlsoPreviousMonthSettled_completeResponse() throws Exception {
		final ListReportsResponse expected = new ListReportsResponse();
		expected.setYear((short) 2009);
		expected.setAllYears(ALL_YEARS);
		expected.setAllMonth(Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7,
				(short) 8, (short) 9, (short) 10, (short) 11, (short) 12));
		expected.setMonth((short) 1);

		final List<MoneyflowTransport> moneyflowTransports = new ArrayList<>();
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow1().build());
		expected.setMoneyflowTransports(moneyflowTransports);

		final List<ReportTurnoverCapitalsourceTransport> reportTurnoverCapitalsourceTransports = new ArrayList<>();
		reportTurnoverCapitalsourceTransports
				.add(new ReportTurnoverCapitalsourceTransportBuilder().forReport_2009_01_Capitalsource1().build());
		reportTurnoverCapitalsourceTransports
				.add(new ReportTurnoverCapitalsourceTransportBuilder().forReport_2009_01_Capitalsource2().build());
		reportTurnoverCapitalsourceTransports
				.add(new ReportTurnoverCapitalsourceTransportBuilder().forReport_2009_01_Capitalsource4().build());
		expected.setReportTurnoverCapitalsourceTransports(reportTurnoverCapitalsourceTransports);

		expected.setTurnoverEndOfYearCalculated(new BigDecimal("-1.10"));
		expected.setAmountBeginOfYear(new BigDecimal("110.00"));

		expected.setPreviousMonth((short) 12);
		expected.setPreviousYear((short) 2008);
		expected.setNextMonth((short) 2);
		expected.setNextYear((short) 2009);

		expected.setNextMonthHasMoneyflows((short) 1);
		expected.setPreviousMonthHasMoneyflows((short) 1);

		final ListReportsResponse actual = super.callUsecaseWithoutContent("/2009/1", this.method, false,
				ListReportsResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_DecemberSettledAndAlsoPreviousMonthSettled_completeResponse() throws Exception {
		final ListReportsResponse expected = new ListReportsResponse();
		expected.setYear((short) 2009);
		expected.setAllYears(ALL_YEARS);
		expected.setAllMonth(Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7,
				(short) 8, (short) 9, (short) 10, (short) 11, (short) 12));
		expected.setMonth((short) 12);

		final List<MoneyflowTransport> moneyflowTransports = new ArrayList<>();
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow13().build());
		expected.setMoneyflowTransports(moneyflowTransports);

		final List<ReportTurnoverCapitalsourceTransport> reportTurnoverCapitalsourceTransports = new ArrayList<>();
		reportTurnoverCapitalsourceTransports
				.add(new ReportTurnoverCapitalsourceTransportBuilder().forReport_2009_12_Capitalsource1().build());
		reportTurnoverCapitalsourceTransports
				.add(new ReportTurnoverCapitalsourceTransportBuilder().forReport_2009_12_Capitalsource2().build());
		reportTurnoverCapitalsourceTransports
				.add(new ReportTurnoverCapitalsourceTransportBuilder().forReport_2009_12_Capitalsource4().build());
		expected.setReportTurnoverCapitalsourceTransports(reportTurnoverCapitalsourceTransports);

		expected.setTurnoverEndOfYearCalculated(new BigDecimal("8.90"));
		expected.setAmountBeginOfYear(new BigDecimal("110.00"));

		expected.setPreviousMonth((short) 11);
		expected.setPreviousYear((short) 2009);
		expected.setNextMonth((short) 1);
		expected.setNextYear((short) 2010);

		expected.setNextMonthHasMoneyflows((short) 1);
		expected.setPreviousMonthHasMoneyflows((short) 1);

		final ListReportsResponse actual = super.callUsecaseWithoutContent("/2009/12", this.method, false,
				ListReportsResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_DecemberAndNoMoneyflowsNextMonth_completeResponse() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW14_ID);
		this.moneyflowService.deleteMoneyflow(userId, moneyflowId);

		final ListReportsResponse expected = new ListReportsResponse();
		expected.setYear((short) 2009);
		expected.setAllYears(ALL_YEARS);
		expected.setAllMonth(Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6, (short) 7,
				(short) 8, (short) 9, (short) 10, (short) 11, (short) 12));
		expected.setMonth((short) 12);

		final List<MoneyflowTransport> moneyflowTransports = new ArrayList<>();
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow13().build());
		expected.setMoneyflowTransports(moneyflowTransports);

		final List<ReportTurnoverCapitalsourceTransport> reportTurnoverCapitalsourceTransports = new ArrayList<>();
		reportTurnoverCapitalsourceTransports
				.add(new ReportTurnoverCapitalsourceTransportBuilder().forReport_2009_12_Capitalsource1().build());
		reportTurnoverCapitalsourceTransports
				.add(new ReportTurnoverCapitalsourceTransportBuilder().forReport_2009_12_Capitalsource2().build());
		reportTurnoverCapitalsourceTransports
				.add(new ReportTurnoverCapitalsourceTransportBuilder().forReport_2009_12_Capitalsource4().build());
		expected.setReportTurnoverCapitalsourceTransports(reportTurnoverCapitalsourceTransports);

		expected.setTurnoverEndOfYearCalculated(new BigDecimal("8.90"));
		expected.setAmountBeginOfYear(new BigDecimal("110.00"));

		expected.setPreviousMonth((short) 11);
		expected.setPreviousYear((short) 2009);
		expected.setNextMonth((short) 2);
		expected.setNextYear((short) 2010);

		expected.setPreviousMonthHasMoneyflows((short) 1);
		expected.setNextMonthHasMoneyflows((short) 1);

		final ListReportsResponse actual = super.callUsecaseWithoutContent("/2009/12", this.method, false,
				ListReportsResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_JanuarySettledButOneSourceNotSettledAndAlsoPreviousMonthSettled_completeResponse()
			throws Exception {
		final ListReportsResponse expected = new ListReportsResponse();
		expected.setYear((short) 2010);
		expected.setAllYears(ALL_YEARS);
		expected.setAllMonth(Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4, (short) 5));
		expected.setMonth((short) 1);

		final List<MoneyflowTransport> moneyflowTransports = new ArrayList<>();
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow14().build());
		expected.setMoneyflowTransports(moneyflowTransports);

		final List<ReportTurnoverCapitalsourceTransport> reportTurnoverCapitalsourceTransports = new ArrayList<>();
		reportTurnoverCapitalsourceTransports
				.add(new ReportTurnoverCapitalsourceTransportBuilder().forReport_2010_01_Capitalsource1().build());
		reportTurnoverCapitalsourceTransports
				.add(new ReportTurnoverCapitalsourceTransportBuilder().forReport_2010_01_Capitalsource2().build());
		reportTurnoverCapitalsourceTransports
				.add(new ReportTurnoverCapitalsourceTransportBuilder().forReport_2010_01_Capitalsource4().build());
		expected.setReportTurnoverCapitalsourceTransports(reportTurnoverCapitalsourceTransports);

		expected.setTurnoverEndOfYearCalculated(new BigDecimal("-10.00"));
		expected.setAmountBeginOfYear(new BigDecimal("118.90"));

		expected.setPreviousMonth((short) 12);
		expected.setPreviousYear((short) 2009);
		expected.setNextMonth((short) 2);
		expected.setNextYear((short) 2010);

		expected.setNextMonthHasMoneyflows((short) 1);
		expected.setPreviousMonthHasMoneyflows((short) 1);

		final ListReportsResponse actual = super.callUsecaseWithoutContent("/2010/01", this.method, false,
				ListReportsResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_MoneyflowOfDifferentGroupMember_shownTurnoverSortingCorrect() throws Exception {
		this.userName = UserTransportBuilder.USER3_NAME;
		this.userPassword = UserTransportBuilder.USER3_PASSWORD;
		final ListReportsResponse expected = new ListReportsResponse();
		expected.setYear((short) 2010);
		expected.setAllYears(ALL_YEARS);
		expected.setAllMonth(Arrays.asList((short) 1, (short) 5));
		expected.setMonth((short) 1);

		final List<MoneyflowTransport> moneyflowTransports = new ArrayList<>();
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow14().build());
		expected.setMoneyflowTransports(moneyflowTransports);

		final List<ReportTurnoverCapitalsourceTransport> reportTurnoverCapitalsourceTransports = new ArrayList<>();
		reportTurnoverCapitalsourceTransports
				.add(new ReportTurnoverCapitalsourceTransportBuilder().forReport_2010_01_Capitalsource4().build());
		reportTurnoverCapitalsourceTransports
				.add(new ReportTurnoverCapitalsourceTransportBuilder().forReport_2010_01_Capitalsource1().build());
		reportTurnoverCapitalsourceTransports
				.add(new ReportTurnoverCapitalsourceTransportBuilder().forReport_2010_01_Capitalsource2().build());
		expected.setReportTurnoverCapitalsourceTransports(reportTurnoverCapitalsourceTransports);

		expected.setTurnoverEndOfYearCalculated(new BigDecimal("-10.00"));
		expected.setAmountBeginOfYear(new BigDecimal("118.90"));

		expected.setPreviousMonth((short) 12);
		expected.setPreviousYear((short) 2009);
		expected.setNextMonth((short) 5);
		expected.setNextYear((short) 2010);

		final ListReportsResponse actual = super.callUsecaseWithoutContent("/2010/01", this.method, false,
				ListReportsResponse.class);

		expected.setPreviousMonth((short) 1);
		expected.setPreviousYear((short) 2009);
		expected.setNextMonth((short) 5);
		expected.setNextYear((short) 2010);

		expected.setNextMonthHasMoneyflows((short) 1);
		expected.setPreviousMonthHasMoneyflows((short) 1);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_FebruarySettledButOneSourceNotSettledAndAlsoPreviousMonthSettledButOneSourceNotSettled_completeResponse()
			throws Exception {
		final ListReportsResponse expected = new ListReportsResponse();
		expected.setYear((short) 2010);
		expected.setAllYears(ALL_YEARS);
		expected.setAllMonth(Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4, (short) 5));
		expected.setMonth((short) 2);

		final List<MoneyflowTransport> moneyflowTransports = new ArrayList<>();
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow15().build());
		expected.setMoneyflowTransports(moneyflowTransports);

		final List<ReportTurnoverCapitalsourceTransport> reportTurnoverCapitalsourceTransports = new ArrayList<>();
		reportTurnoverCapitalsourceTransports
				.add(new ReportTurnoverCapitalsourceTransportBuilder().forReport_2010_02_Capitalsource1().build());
		reportTurnoverCapitalsourceTransports
				.add(new ReportTurnoverCapitalsourceTransportBuilder().forReport_2010_02_Capitalsource2().build());
		reportTurnoverCapitalsourceTransports
				.add(new ReportTurnoverCapitalsourceTransportBuilder().forReport_2010_02_Capitalsource4().build());
		expected.setReportTurnoverCapitalsourceTransports(reportTurnoverCapitalsourceTransports);

		expected.setTurnoverEndOfYearCalculated(new BigDecimal("0.00"));
		expected.setAmountBeginOfYear(new BigDecimal("118.90"));

		expected.setPreviousMonth((short) 1);
		expected.setPreviousYear((short) 2010);
		expected.setNextMonth((short) 3);
		expected.setNextYear((short) 2010);

		expected.setNextMonthHasMoneyflows((short) 1);
		expected.setPreviousMonthHasMoneyflows((short) 1);

		final ListReportsResponse actual = super.callUsecaseWithoutContent("/2010/02", this.method, false,
				ListReportsResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_MarchSettledAndAlsoPreviousMonthSettledButOneSourceNotSettled_completeResponse() throws Exception {
		final ListReportsResponse expected = new ListReportsResponse();
		expected.setYear((short) 2010);
		expected.setAllYears(ALL_YEARS);
		expected.setAllMonth(Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4, (short) 5));
		expected.setMonth((short) 3);

		final List<MoneyflowTransport> moneyflowTransports = new ArrayList<>();
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow16().build());
		expected.setMoneyflowTransports(moneyflowTransports);

		final List<ReportTurnoverCapitalsourceTransport> reportTurnoverCapitalsourceTransports = new ArrayList<>();
		reportTurnoverCapitalsourceTransports
				.add(new ReportTurnoverCapitalsourceTransportBuilder().forReport_2010_03_Capitalsource1().build());
		reportTurnoverCapitalsourceTransports
				.add(new ReportTurnoverCapitalsourceTransportBuilder().forReport_2010_03_Capitalsource2().build());
		reportTurnoverCapitalsourceTransports
				.add(new ReportTurnoverCapitalsourceTransportBuilder().forReport_2010_03_Capitalsource4().build());
		expected.setReportTurnoverCapitalsourceTransports(reportTurnoverCapitalsourceTransports);

		expected.setTurnoverEndOfYearCalculated(new BigDecimal("-10.00"));
		expected.setAmountBeginOfYear(new BigDecimal("118.90"));

		expected.setPreviousMonth((short) 2);
		expected.setPreviousYear((short) 2010);
		expected.setNextMonth((short) 4);
		expected.setNextYear((short) 2010);

		expected.setNextMonthHasMoneyflows((short) 1);
		expected.setPreviousMonthHasMoneyflows((short) 1);

		final ListReportsResponse actual = super.callUsecaseWithoutContent("/2010/03", this.method, false,
				ListReportsResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void test_MayNotSettledAndAlsoPreviousMonthSettled_completeResponse() throws Exception {
		final ListReportsResponse expected = new ListReportsResponse();
		expected.setYear((short) 2010);
		expected.setAllYears(ALL_YEARS);
		expected.setAllMonth(Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4, (short) 5));
		expected.setMonth((short) 5);

		final List<MoneyflowTransport> moneyflowTransports = new ArrayList<>();
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow18().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow19().build());
		expected.setMoneyflowTransports(moneyflowTransports);

		final List<ReportTurnoverCapitalsourceTransport> reportTurnoverCapitalsourceTransports = new ArrayList<>();

		final ReportTurnoverCapitalsourceTransport transport1 = new ReportTurnoverCapitalsourceTransportBuilder()
				.forReport_2010_05_Capitalsource1().build();
		transport1.setAmountCurrent(new BigDecimal("111.00"));
		transport1.setAmountCurrentState(new Timestamp(109, 11, 1, 20, 20, 20, 0));

		reportTurnoverCapitalsourceTransports.add(transport1);
		reportTurnoverCapitalsourceTransports
				.add(new ReportTurnoverCapitalsourceTransportBuilder().forReport_2010_05_Capitalsource2().build());
		reportTurnoverCapitalsourceTransports
				.add(new ReportTurnoverCapitalsourceTransportBuilder().forReport_2010_05_Capitalsource4().build());
		expected.setReportTurnoverCapitalsourceTransports(reportTurnoverCapitalsourceTransports);

		expected.setTurnoverEndOfYearCalculated(new BigDecimal("-10.00"));
		expected.setAmountBeginOfYear(new BigDecimal("118.90"));

		expected.setPreviousMonth((short) 4);
		expected.setPreviousYear((short) 2010);

		expected.setPreviousMonthHasMoneyflows((short) 1);

		final Capitalsource capitalsource = new Capitalsource(
				new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID));

		final ImportedBalance importedBalance = new ImportedBalance();
		importedBalance.setBalance(new BigDecimal("111.00"));
		importedBalance.setCapitalsource(capitalsource);
		importedBalance.setDate(new Timestamp(109, 11, 1, 20, 20, 20, 0).toLocalDateTime());
		this.importedBalanceService.upsertImportedBalance(importedBalance);

		final ListReportsResponse actual = super.callUsecaseWithoutContent("/2010/5", this.method, false,
				ListReportsResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_ValidityPeriodOfCapitalsourceEndedLastMonth_capitalsourceNotIncludedInTurnover() throws Exception {

		final CapitalsourceID capitalsourceId = new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE4_ID);
		final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
		final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
				capitalsourceId);
		capitalsource.setValidTil(LocalDate.of(2010, Month.APRIL, 30));
		this.capitalsourceService.updateCapitalsource(capitalsource);

		final ListReportsResponse expected = new ListReportsResponse();
		expected.setYear((short) 2010);
		expected.setAllYears(ALL_YEARS);
		expected.setAllMonth(Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4, (short) 5));
		expected.setMonth((short) 5);

		final List<MoneyflowTransport> moneyflowTransports = new ArrayList<>();
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow18().build());
		moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow19().build());
		expected.setMoneyflowTransports(moneyflowTransports);

		final List<ReportTurnoverCapitalsourceTransport> reportTurnoverCapitalsourceTransports = new ArrayList<>();
		reportTurnoverCapitalsourceTransports
				.add(new ReportTurnoverCapitalsourceTransportBuilder().forReport_2010_05_Capitalsource1().build());
		reportTurnoverCapitalsourceTransports
				.add(new ReportTurnoverCapitalsourceTransportBuilder().forReport_2010_05_Capitalsource2().build());
		expected.setReportTurnoverCapitalsourceTransports(reportTurnoverCapitalsourceTransports);

		expected.setTurnoverEndOfYearCalculated(new BigDecimal("-10.00"));
		expected.setAmountBeginOfYear(new BigDecimal("118.90"));

		expected.setPreviousMonth((short) 4);
		expected.setPreviousYear((short) 2010);

		expected.setPreviousMonthHasMoneyflows((short) 1);

		final ListReportsResponse actual = super.callUsecaseWithoutContent("/2010/5", this.method, false,
				ListReportsResponse.class);

		Assert.assertEquals(expected, actual);

	}

	@Test
	public void test_AuthorizationRequired_01_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("", this.method, false, ErrorResponse.class);
		Assert.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

	@Test
	public void test_AuthorizationRequired_02_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("/2010", this.method, false, ErrorResponse.class);
		Assert.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

	@Test
	public void test_AuthorizationRequired_03_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("/2010/1", this.method, false,
				ErrorResponse.class);
		Assert.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	public void test_emptyDatabase_noException() throws Exception {
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
		super.callUsecaseWithoutContent("", this.method, false, ListReportsResponse.class);
	}
}