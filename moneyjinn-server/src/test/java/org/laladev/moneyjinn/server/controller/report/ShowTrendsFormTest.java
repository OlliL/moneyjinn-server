
package org.laladev.moneyjinn.server.controller.report;

import jakarta.inject.Inject;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.setting.ClientTrendCapitalsourceIDsSetting;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.controller.api.ReportControllerApi;
import org.laladev.moneyjinn.server.model.ShowTrendsFormResponse;
import org.laladev.moneyjinn.service.api.IMonthlySettlementService;
import org.laladev.moneyjinn.service.api.ISettingService;
import org.springframework.test.context.jdbc.Sql;

public class ShowTrendsFormTest extends AbstractControllerTest {
  @Inject
  private ISettingService settingService;
  @Inject
  private IMonthlySettlementService monthlySettlementService;

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
  protected Method getMethod() {
    return super.getMethodFromTestClassName(ReportControllerApi.class, this.getClass());
  }

  private ShowTrendsFormResponse getDefaultResponse() {
    final ShowTrendsFormResponse expected = new ShowTrendsFormResponse();

    expected.setMinDate(LocalDate.parse("2008-11-01"));
    expected.setMaxDate(LocalDate.parse("2010-05-03"));
    return expected;
  }

  @Test
  public void test_TestLastSettlementInDecemberButFlowsInNextYear_nextYearIncludedInAllYears()
      throws Exception {
    final UserID userId1 = new UserID(UserTransportBuilder.USER1_ID);
    final UserID userId3 = new UserID(UserTransportBuilder.USER3_ID);
    this.monthlySettlementService.deleteMonthlySettlement(userId1, 2010, Month.JANUARY);
    this.monthlySettlementService.deleteMonthlySettlement(userId1, 2010, Month.FEBRUARY);
    this.monthlySettlementService.deleteMonthlySettlement(userId1, 2010, Month.MARCH);
    this.monthlySettlementService.deleteMonthlySettlement(userId1, 2010, Month.APRIL);
    this.monthlySettlementService.deleteMonthlySettlement(userId3, 2010, Month.JANUARY);
    this.monthlySettlementService.deleteMonthlySettlement(userId3, 2010, Month.FEBRUARY);
    this.monthlySettlementService.deleteMonthlySettlement(userId3, 2010, Month.MARCH);
    this.monthlySettlementService.deleteMonthlySettlement(userId3, 2010, Month.APRIL);
    final ShowTrendsFormResponse expected = this.getDefaultResponse();
    final ShowTrendsFormResponse actual = super.callUsecaseExpect200(ShowTrendsFormResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_noSetting_defaultsResponse() throws Exception {
    final ShowTrendsFormResponse expected = this.getDefaultResponse();
    final ShowTrendsFormResponse actual = super.callUsecaseExpect200(ShowTrendsFormResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_witDefaultSelection_defaultsResponse() throws Exception {
    final ClientTrendCapitalsourceIDsSetting setting = new ClientTrendCapitalsourceIDsSetting(
        Arrays.asList(new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID),
            new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID),
            new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE5_ID),
            new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE6_ID)));
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    this.settingService.setClientTrendCapitalsourceIDsSetting(userId, setting);
    final ShowTrendsFormResponse expected = this.getDefaultResponse();
    expected.setSettingTrendCapitalsourceIds(
        Arrays.asList(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID,
            CapitalsourceTransportBuilder.CAPITALSOURCE2_ID,
            CapitalsourceTransportBuilder.CAPITALSOURCE5_ID,
            CapitalsourceTransportBuilder.CAPITALSOURCE6_ID));
    final ShowTrendsFormResponse actual = super.callUsecaseExpect200(ShowTrendsFormResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;

    super.callUsecaseExpect403();
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  public void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;

    super.callUsecaseExpect200(ShowTrendsFormResponse.class);
  }
}