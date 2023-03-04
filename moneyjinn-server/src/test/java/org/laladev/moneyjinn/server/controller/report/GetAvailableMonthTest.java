
package org.laladev.moneyjinn.server.controller.report;

import jakarta.inject.Inject;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.controller.api.ReportControllerApi;
import org.laladev.moneyjinn.server.model.GetAvailableReportMonthResponse;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.IMoneyflowService;
import org.springframework.test.context.jdbc.Sql;

public class GetAvailableMonthTest extends AbstractControllerTest {
  @Inject
  private ICapitalsourceService capitalsourceService;
  @Inject
  private IMoneyflowService moneyflowService;

  private static final List<Integer> ALL_YEARS = Arrays.asList(2008, 2009, 2010);
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
  protected void loadMethod() {
    super.getMock(ReportControllerApi.class).getAvailableMonth();
  }

  private void assertEquals(final GetAvailableReportMonthResponse expected,
      final GetAvailableReportMonthResponse actual) {
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_noArgumentOrOnlyYear_defaultsResponse() throws Exception {
    final GetAvailableReportMonthResponse expected = new GetAvailableReportMonthResponse();
    expected.setYear(2010);
    expected.setAllYears(ALL_YEARS);
    expected.setAllMonth(Arrays.asList(1, 2, 3, 4, 5));

    final GetAvailableReportMonthResponse actual = super.callUsecaseExpect200(
        GetAvailableReportMonthResponse.class);

    this.assertEquals(expected, actual);
  }

  @Test
  public void test_AuthorizationRequired_01_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;

    super.callUsecaseExpect403();
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  public void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;

    super.callUsecaseExpect200(GetAvailableReportMonthResponse.class);
  }
}