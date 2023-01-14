
package org.laladev.moneyjinn.server.controller.report;

import jakarta.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.rest.model.report.ListReportsResponse;
import org.laladev.moneyjinn.core.rest.model.report.transport.ReportTurnoverCapitalsourceTransport;
import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowSplitEntryTransport;
import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowTransport;
import org.laladev.moneyjinn.model.ImportedBalance;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowID;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.MoneyflowSplitEntryTransportBuilder;
import org.laladev.moneyjinn.server.builder.MoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.ReportTurnoverCapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.IImportedBalanceService;
import org.laladev.moneyjinn.service.api.IMoneyflowService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class ListReportsV2Test extends AbstractControllerTest {
  @Inject
  private IImportedBalanceService importedBalanceService;
  @Inject
  private ICapitalsourceService capitalsourceService;
  @Inject
  private IMoneyflowService moneyflowService;
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

  private void assertEquals(final ListReportsResponse expected, final ListReportsResponse actual) {
    if (expected.getMoneyflowTransports() != null) {
      Collections.sort(expected.getMoneyflowTransports(), new MoneyflowTransportComparator());
    }
    if (actual.getMoneyflowTransports() != null) {
      Collections.sort(actual.getMoneyflowTransports(), new MoneyflowTransportComparator());
    }
    Assertions.assertEquals(expected, actual);
  }

  private static class MoneyflowTransportComparator
      implements Comparator<MoneyflowTransport>, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public int compare(final MoneyflowTransport o1, final MoneyflowTransport o2) {
      if (o1 == null) {
        if (o2 == null) {
          return 0;
        }
        return -1;
      } else {
        if (o2 == null) {
          return 1;
        }
        int result = 0;
        if (o1.getBookingdate() == null) {
          if (o2.getBookingdate() != null) {
            return -1;
          }
        } else {
          result = o1.getBookingdate().compareTo(o2.getBookingdate());
        }
        if (result != 0) {
          return result;
        }
        if (o1.getInvoicedate() == null) {
          if (o2.getInvoicedate() != null) {
            return -1;
          }
        } else {
          result = o1.getInvoicedate().compareTo(o2.getInvoicedate());
        }
        return result;
      }
    }
  }

  @Test
  public void test_invalidMonth_defaultsResponse() throws Exception {
    final ListReportsResponse expected = new ListReportsResponse();
    expected.setYear((short) 2010);
    ListReportsResponse actual = super.callUsecaseWithoutContent("/2010/13", this.method, false,
        ListReportsResponse.class);
    this.assertEquals(expected, actual);
    actual = super.callUsecaseWithoutContent("/2010/0", this.method, false,
        ListReportsResponse.class);
    this.assertEquals(expected, actual);
    expected.setMonth(Short.valueOf("11"));
    actual = super.callUsecaseWithoutContent("/2010/11", this.method, false,
        ListReportsResponse.class);
    this.assertEquals(expected, actual);
  }

  @Test
  public void test_DecemberFirstMonthAtAllSettledAndAlsoPreviousMonthSettled_completeResponse()
      throws Exception {
    final ListReportsResponse expected = new ListReportsResponse();
    expected.setYear((short) 2008);
    expected.setMonth((short) 12);
    final List<MoneyflowTransport> moneyflowTransports = new ArrayList<>();
    moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow2().build());
    expected.setMoneyflowTransports(moneyflowTransports);
    final List<ReportTurnoverCapitalsourceTransport> reportTurnoverCapitalsourceTransports = new ArrayList<>();
    reportTurnoverCapitalsourceTransports.add(new ReportTurnoverCapitalsourceTransportBuilder()
        .forReport_2008_12_Capitalsource1().build());
    reportTurnoverCapitalsourceTransports.add(new ReportTurnoverCapitalsourceTransportBuilder()
        .forReport_2008_12_Capitalsource2().build());
    reportTurnoverCapitalsourceTransports.add(new ReportTurnoverCapitalsourceTransportBuilder()
        .forReport_2008_12_Capitalsource4().build());
    reportTurnoverCapitalsourceTransports
        .add(new ReportTurnoverCapitalsourceTransportBuilder().forReport_Capitalsource6().build());
    expected.setReportTurnoverCapitalsourceTransports(reportTurnoverCapitalsourceTransports);
    expected.setTurnoverEndOfYearCalculated(new BigDecimal("10.10"));
    expected.setAmountBeginOfYear(new BigDecimal("99.90"));
    expected.setMoneyflowsWithReceipt(Arrays.asList(MoneyflowTransportBuilder.MONEYFLOW2_ID));
    final ListReportsResponse actual = super.callUsecaseWithoutContent("/2008/12", this.method,
        false, ListReportsResponse.class);
    this.assertEquals(expected, actual);
  }

  @Test
  public void test_JanuarySettledAndAlsoPreviousMonthSettled_completeResponse() throws Exception {
    final ListReportsResponse expected = new ListReportsResponse();
    expected.setYear((short) 2009);
    expected.setMonth((short) 1);
    final List<MoneyflowTransport> moneyflowTransports = new ArrayList<>();
    moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow1().build());
    expected.setMoneyflowTransports(moneyflowTransports);
    final List<MoneyflowSplitEntryTransport> moneyflowSplitEntryTransports = new ArrayList<>();
    moneyflowSplitEntryTransports
        .add(new MoneyflowSplitEntryTransportBuilder().forMoneyflowSplitEntry1().build());
    moneyflowSplitEntryTransports
        .add(new MoneyflowSplitEntryTransportBuilder().forMoneyflowSplitEntry2().build());
    expected.setMoneyflowSplitEntryTransports(moneyflowSplitEntryTransports);
    final List<ReportTurnoverCapitalsourceTransport> reportTurnoverCapitalsourceTransports = new ArrayList<>();
    reportTurnoverCapitalsourceTransports.add(new ReportTurnoverCapitalsourceTransportBuilder()
        .forReport_2009_01_Capitalsource1().build());
    reportTurnoverCapitalsourceTransports.add(new ReportTurnoverCapitalsourceTransportBuilder()
        .forReport_2009_01_Capitalsource2().build());
    reportTurnoverCapitalsourceTransports.add(new ReportTurnoverCapitalsourceTransportBuilder()
        .forReport_2009_01_Capitalsource4().build());
    reportTurnoverCapitalsourceTransports
        .add(new ReportTurnoverCapitalsourceTransportBuilder().forReport_Capitalsource6().build());
    expected.setReportTurnoverCapitalsourceTransports(reportTurnoverCapitalsourceTransports);
    expected.setTurnoverEndOfYearCalculated(new BigDecimal("-1.10"));
    expected.setAmountBeginOfYear(new BigDecimal("110.00"));
    expected.setMoneyflowsWithReceipt(Arrays.asList(MoneyflowTransportBuilder.MONEYFLOW1_ID));
    final ListReportsResponse actual = super.callUsecaseWithoutContent("/2009/1", this.method,
        false, ListReportsResponse.class);
    this.assertEquals(expected, actual);
  }

  @Test
  public void test_DecemberSettledAndAlsoPreviousMonthSettled_completeResponse() throws Exception {
    final ListReportsResponse expected = new ListReportsResponse();
    expected.setYear((short) 2009);
    expected.setMonth((short) 12);
    final List<MoneyflowTransport> moneyflowTransports = new ArrayList<>();
    moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow13().build());
    expected.setMoneyflowTransports(moneyflowTransports);
    final List<ReportTurnoverCapitalsourceTransport> reportTurnoverCapitalsourceTransports = new ArrayList<>();
    reportTurnoverCapitalsourceTransports.add(new ReportTurnoverCapitalsourceTransportBuilder()
        .forReport_2009_12_Capitalsource1().build());
    reportTurnoverCapitalsourceTransports.add(new ReportTurnoverCapitalsourceTransportBuilder()
        .forReport_2009_12_Capitalsource2().build());
    reportTurnoverCapitalsourceTransports.add(new ReportTurnoverCapitalsourceTransportBuilder()
        .forReport_2009_12_Capitalsource4().build());
    reportTurnoverCapitalsourceTransports
        .add(new ReportTurnoverCapitalsourceTransportBuilder().forReport_Capitalsource6().build());
    expected.setReportTurnoverCapitalsourceTransports(reportTurnoverCapitalsourceTransports);
    expected.setTurnoverEndOfYearCalculated(new BigDecimal("8.90"));
    expected.setAmountBeginOfYear(new BigDecimal("110.00"));
    final ListReportsResponse actual = super.callUsecaseWithoutContent("/2009/12", this.method,
        false, ListReportsResponse.class);
    this.assertEquals(expected, actual);
  }

  @Test
  public void test_DecemberAndNoMoneyflowsNextMonth_completeResponse() throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW14_ID);
    this.moneyflowService.deleteMoneyflow(userId, moneyflowId);
    final ListReportsResponse expected = new ListReportsResponse();
    expected.setYear((short) 2009);
    expected.setMonth((short) 12);
    final List<MoneyflowTransport> moneyflowTransports = new ArrayList<>();
    moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow13().build());
    expected.setMoneyflowTransports(moneyflowTransports);
    final List<ReportTurnoverCapitalsourceTransport> reportTurnoverCapitalsourceTransports = new ArrayList<>();
    reportTurnoverCapitalsourceTransports.add(new ReportTurnoverCapitalsourceTransportBuilder()
        .forReport_2009_12_Capitalsource1().build());
    reportTurnoverCapitalsourceTransports.add(new ReportTurnoverCapitalsourceTransportBuilder()
        .forReport_2009_12_Capitalsource2().build());
    reportTurnoverCapitalsourceTransports.add(new ReportTurnoverCapitalsourceTransportBuilder()
        .forReport_2009_12_Capitalsource4().build());
    reportTurnoverCapitalsourceTransports
        .add(new ReportTurnoverCapitalsourceTransportBuilder().forReport_Capitalsource6().build());
    expected.setReportTurnoverCapitalsourceTransports(reportTurnoverCapitalsourceTransports);
    expected.setTurnoverEndOfYearCalculated(new BigDecimal("8.90"));
    expected.setAmountBeginOfYear(new BigDecimal("110.00"));
    final ListReportsResponse actual = super.callUsecaseWithoutContent("/2009/12", this.method,
        false, ListReportsResponse.class);
    this.assertEquals(expected, actual);
  }

  @Test
  public void test_JanuarySettledButOneSourceNotSettledAndAlsoPreviousMonthSettled_completeResponse()
      throws Exception {
    final ListReportsResponse expected = new ListReportsResponse();
    expected.setYear((short) 2010);
    expected.setMonth((short) 1);
    final List<MoneyflowTransport> moneyflowTransports = new ArrayList<>();
    moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow14().build());
    expected.setMoneyflowTransports(moneyflowTransports);
    final List<ReportTurnoverCapitalsourceTransport> reportTurnoverCapitalsourceTransports = new ArrayList<>();
    reportTurnoverCapitalsourceTransports.add(new ReportTurnoverCapitalsourceTransportBuilder()
        .forReport_2010_01_Capitalsource1().build());
    reportTurnoverCapitalsourceTransports.add(new ReportTurnoverCapitalsourceTransportBuilder()
        .forReport_2010_01_Capitalsource2().build());
    reportTurnoverCapitalsourceTransports.add(new ReportTurnoverCapitalsourceTransportBuilder()
        .forReport_2010_01_Capitalsource4().build());
    reportTurnoverCapitalsourceTransports
        .add(new ReportTurnoverCapitalsourceTransportBuilder().forReport_Capitalsource6().build());
    expected.setReportTurnoverCapitalsourceTransports(reportTurnoverCapitalsourceTransports);
    expected.setTurnoverEndOfYearCalculated(new BigDecimal("-10.00"));
    expected.setAmountBeginOfYear(new BigDecimal("118.90"));
    final ListReportsResponse actual = super.callUsecaseWithoutContent("/2010/01", this.method,
        false, ListReportsResponse.class);
    this.assertEquals(expected, actual);
  }

  @Test
  public void test_MoneyflowOfDifferentGroupMember_shownTurnoverSortingCorrect() throws Exception {
    this.userName = UserTransportBuilder.USER3_NAME;
    this.userPassword = UserTransportBuilder.USER3_PASSWORD;
    final ListReportsResponse expected = new ListReportsResponse();
    expected.setYear((short) 2010);
    expected.setMonth((short) 1);
    final List<MoneyflowTransport> moneyflowTransports = new ArrayList<>();
    moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow14().build());
    expected.setMoneyflowTransports(moneyflowTransports);
    final List<ReportTurnoverCapitalsourceTransport> reportTurnoverCapitalsourceTransports = new ArrayList<>();
    reportTurnoverCapitalsourceTransports.add(new ReportTurnoverCapitalsourceTransportBuilder()
        .forReport_2010_01_Capitalsource4().build());
    reportTurnoverCapitalsourceTransports
        .add(new ReportTurnoverCapitalsourceTransportBuilder().forReport_Capitalsource6().build());
    reportTurnoverCapitalsourceTransports.add(new ReportTurnoverCapitalsourceTransportBuilder()
        .forReport_2010_01_Capitalsource1().build());
    reportTurnoverCapitalsourceTransports.add(new ReportTurnoverCapitalsourceTransportBuilder()
        .forReport_2010_01_Capitalsource2().build());
    expected.setReportTurnoverCapitalsourceTransports(reportTurnoverCapitalsourceTransports);
    expected.setTurnoverEndOfYearCalculated(new BigDecimal("-10.00"));
    expected.setAmountBeginOfYear(new BigDecimal("118.90"));
    final ListReportsResponse actual = super.callUsecaseWithoutContent("/2010/01", this.method,
        false, ListReportsResponse.class);
    this.assertEquals(expected, actual);
  }

  @Test
  public void test_FebruarySettledButOneSourceNotSettledAndAlsoPreviousMonthSettledButOneSourceNotSettled_completeResponse()
      throws Exception {
    final ListReportsResponse expected = new ListReportsResponse();
    expected.setYear((short) 2010);
    expected.setMonth((short) 2);
    final List<MoneyflowTransport> moneyflowTransports = new ArrayList<>();
    moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow15().build());
    expected.setMoneyflowTransports(moneyflowTransports);
    final List<ReportTurnoverCapitalsourceTransport> reportTurnoverCapitalsourceTransports = new ArrayList<>();
    reportTurnoverCapitalsourceTransports.add(new ReportTurnoverCapitalsourceTransportBuilder()
        .forReport_2010_02_Capitalsource1().build());
    reportTurnoverCapitalsourceTransports.add(new ReportTurnoverCapitalsourceTransportBuilder()
        .forReport_2010_02_Capitalsource2().build());
    reportTurnoverCapitalsourceTransports.add(new ReportTurnoverCapitalsourceTransportBuilder()
        .forReport_2010_02_Capitalsource4().build());
    reportTurnoverCapitalsourceTransports
        .add(new ReportTurnoverCapitalsourceTransportBuilder().forReport_Capitalsource6().build());
    expected.setReportTurnoverCapitalsourceTransports(reportTurnoverCapitalsourceTransports);
    expected.setTurnoverEndOfYearCalculated(new BigDecimal("0.00"));
    expected.setAmountBeginOfYear(new BigDecimal("118.90"));
    final ListReportsResponse actual = super.callUsecaseWithoutContent("/2010/02", this.method,
        false, ListReportsResponse.class);
    this.assertEquals(expected, actual);
  }

  @Test
  public void test_MarchSettledAndAlsoPreviousMonthSettledButOneSourceNotSettled_completeResponse()
      throws Exception {
    final ListReportsResponse expected = new ListReportsResponse();
    expected.setYear((short) 2010);
    expected.setMonth((short) 3);
    final List<MoneyflowTransport> moneyflowTransports = new ArrayList<>();
    moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow16().build());
    expected.setMoneyflowTransports(moneyflowTransports);
    final List<ReportTurnoverCapitalsourceTransport> reportTurnoverCapitalsourceTransports = new ArrayList<>();
    reportTurnoverCapitalsourceTransports.add(new ReportTurnoverCapitalsourceTransportBuilder()
        .forReport_2010_03_Capitalsource1().build());
    reportTurnoverCapitalsourceTransports.add(new ReportTurnoverCapitalsourceTransportBuilder()
        .forReport_2010_03_Capitalsource2().build());
    reportTurnoverCapitalsourceTransports.add(new ReportTurnoverCapitalsourceTransportBuilder()
        .forReport_2010_03_Capitalsource4().build());
    reportTurnoverCapitalsourceTransports
        .add(new ReportTurnoverCapitalsourceTransportBuilder().forReport_Capitalsource6().build());
    expected.setReportTurnoverCapitalsourceTransports(reportTurnoverCapitalsourceTransports);
    expected.setTurnoverEndOfYearCalculated(new BigDecimal("-10.00"));
    expected.setAmountBeginOfYear(new BigDecimal("118.90"));
    final ListReportsResponse actual = super.callUsecaseWithoutContent("/2010/03", this.method,
        false, ListReportsResponse.class);
    this.assertEquals(expected, actual);
  }

  @Test
  public void test_MayNotSettledAndAlsoPreviousMonthSettled_completeResponse() throws Exception {
    final ListReportsResponse expected = new ListReportsResponse();
    expected.setYear((short) 2010);
    expected.setMonth((short) 5);
    final List<MoneyflowTransport> moneyflowTransports = new ArrayList<>();
    moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow19().build());
    moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow18().build());
    expected.setMoneyflowTransports(moneyflowTransports);
    final List<ReportTurnoverCapitalsourceTransport> reportTurnoverCapitalsourceTransports = new ArrayList<>();
    final ReportTurnoverCapitalsourceTransport transport1 = new ReportTurnoverCapitalsourceTransportBuilder()
        .forReport_2010_05_Capitalsource1().build();
    transport1.setAmountCurrent(new BigDecimal("111.00"));
    transport1.setAmountCurrentState(LocalDateTime.of(2009, 12, 1, 20, 20, 20, 0));
    reportTurnoverCapitalsourceTransports.add(transport1);
    reportTurnoverCapitalsourceTransports.add(new ReportTurnoverCapitalsourceTransportBuilder()
        .forReport_2010_05_Capitalsource2().build());
    reportTurnoverCapitalsourceTransports.add(new ReportTurnoverCapitalsourceTransportBuilder()
        .forReport_2010_05_Capitalsource4().build());
    reportTurnoverCapitalsourceTransports.add(new ReportTurnoverCapitalsourceTransportBuilder()
        .forReport_Capitalsource6().withAmountCurrentZero().build());
    expected.setReportTurnoverCapitalsourceTransports(reportTurnoverCapitalsourceTransports);
    expected.setTurnoverEndOfYearCalculated(new BigDecimal("-10.00"));
    expected.setAmountBeginOfYear(new BigDecimal("118.90"));
    final Capitalsource capitalsource = new Capitalsource(
        new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID));
    final ImportedBalance importedBalance = new ImportedBalance();
    importedBalance.setBalance(new BigDecimal("111.00"));
    importedBalance.setCapitalsource(capitalsource);
    importedBalance.setDate(LocalDateTime.of(2009, 12, 1, 20, 20, 20, 0));
    this.importedBalanceService.upsertImportedBalance(importedBalance);
    final ListReportsResponse actual = super.callUsecaseWithoutContent("/2010/5", this.method,
        false, ListReportsResponse.class);
    this.assertEquals(expected, actual);
  }

  @Test
  public void test_MaywithPrivateMoneyflows_privateMoneyflowNotShown() throws Exception {
    this.userName = UserTransportBuilder.USER3_NAME;
    this.userPassword = UserTransportBuilder.USER3_PASSWORD;
    final ListReportsResponse expected = new ListReportsResponse();
    expected.setYear((short) 2010);
    expected.setMonth((short) 5);
    final List<MoneyflowTransport> moneyflowTransports = new ArrayList<>();
    moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow19().build());
    expected.setMoneyflowTransports(moneyflowTransports);
    final List<ReportTurnoverCapitalsourceTransport> reportTurnoverCapitalsourceTransports = new ArrayList<>();
    reportTurnoverCapitalsourceTransports.add(new ReportTurnoverCapitalsourceTransportBuilder()
        .forReport_2010_05_Capitalsource4().build());
    reportTurnoverCapitalsourceTransports.add(new ReportTurnoverCapitalsourceTransportBuilder()
        .forReport_Capitalsource6().withAmountCurrentZero().build());
    final ReportTurnoverCapitalsourceTransport transport1 = new ReportTurnoverCapitalsourceTransportBuilder()
        .forReport_2010_05_Capitalsource1().build();
    transport1.setAmountCurrent(new BigDecimal("111.00"));
    transport1.setAmountCurrentState(LocalDateTime.of(2009, 12, 1, 20, 20, 20, 0));
    reportTurnoverCapitalsourceTransports.add(transport1);
    final ReportTurnoverCapitalsourceTransport transport2 = new ReportTurnoverCapitalsourceTransportBuilder()
        .forReport_2010_05_Capitalsource2().build();
    reportTurnoverCapitalsourceTransports.add(transport2);
    expected.setReportTurnoverCapitalsourceTransports(reportTurnoverCapitalsourceTransports);
    expected.setTurnoverEndOfYearCalculated(new BigDecimal("-10.00"));
    expected.setAmountBeginOfYear(new BigDecimal("118.90"));
    final Capitalsource capitalsource = new Capitalsource(
        new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID));
    final ImportedBalance importedBalance = new ImportedBalance();
    importedBalance.setBalance(new BigDecimal("111.00"));
    importedBalance.setCapitalsource(capitalsource);
    importedBalance.setDate(LocalDateTime.of(2009, 12, 1, 20, 20, 20, 0));
    this.importedBalanceService.upsertImportedBalance(importedBalance);
    final ListReportsResponse actual = super.callUsecaseWithoutContent("/2010/5", this.method,
        false, ListReportsResponse.class);
    this.assertEquals(expected, actual);
  }

  @Test
  public void test_ValidityPeriodOfCapitalsourceEndedLastMonth_capitalsourceNotIncludedInTurnover()
      throws Exception {
    final CapitalsourceID capitalsourceId = new CapitalsourceID(
        CapitalsourceTransportBuilder.CAPITALSOURCE4_ID);
    final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
    final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
    final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId,
        groupId, capitalsourceId);
    capitalsource.setValidTil(LocalDate.of(2010, Month.APRIL, 30));
    this.capitalsourceService.updateCapitalsource(capitalsource);
    final ListReportsResponse expected = new ListReportsResponse();
    expected.setYear((short) 2010);
    expected.setMonth((short) 5);
    final List<MoneyflowTransport> moneyflowTransports = new ArrayList<>();
    moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow19().build());
    moneyflowTransports.add(new MoneyflowTransportBuilder().forMoneyflow18().build());
    expected.setMoneyflowTransports(moneyflowTransports);
    final List<ReportTurnoverCapitalsourceTransport> reportTurnoverCapitalsourceTransports = new ArrayList<>();
    reportTurnoverCapitalsourceTransports.add(new ReportTurnoverCapitalsourceTransportBuilder()
        .forReport_2010_05_Capitalsource1().build());
    reportTurnoverCapitalsourceTransports.add(new ReportTurnoverCapitalsourceTransportBuilder()
        .forReport_2010_05_Capitalsource2().build());
    reportTurnoverCapitalsourceTransports.add(new ReportTurnoverCapitalsourceTransportBuilder()
        .forReport_Capitalsource6().withAmountCurrentZero().build());
    expected.setReportTurnoverCapitalsourceTransports(reportTurnoverCapitalsourceTransports);
    expected.setTurnoverEndOfYearCalculated(new BigDecimal("-10.00"));
    expected.setAmountBeginOfYear(new BigDecimal("118.90"));
    final ListReportsResponse actual = super.callUsecaseWithoutContent("/2010/5", this.method,
        false, ListReportsResponse.class);
    this.assertEquals(expected, actual);
  }

  @Test
  public void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;
    super.callUsecaseExpect403("/2010/1", this.method);
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  public void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    super.callUsecaseWithoutContent("/1234/10", this.method, false, ListReportsResponse.class);
  }
}