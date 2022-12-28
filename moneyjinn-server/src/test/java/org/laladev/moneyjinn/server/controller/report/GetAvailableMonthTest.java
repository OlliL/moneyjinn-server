
package org.laladev.moneyjinn.server.controller.report;

import jakarta.inject.Inject;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.rest.model.report.GetAvailableMonthResponse;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowID;
import org.laladev.moneyjinn.server.builder.MoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.IMoneyflowService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class GetAvailableMonthTest extends AbstractControllerTest {
  @Inject
  private ICapitalsourceService capitalsourceService;
  @Inject
  private IMoneyflowService moneyflowService;
  private static final List<Short> ALL_YEARS = Arrays.asList((short) 2008, (short) 2009,
      (short) 2010);
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

  private void assertEquals(final GetAvailableMonthResponse expected,
      final GetAvailableMonthResponse actual) {
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_noArgumentOrOnlyYear_defaultsResponse() throws Exception {
    final GetAvailableMonthResponse expected = new GetAvailableMonthResponse();
    expected.setYear((short) 2010);
    expected.setAllYears(ALL_YEARS);
    expected.setAllMonth(Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4, (short) 5));
    GetAvailableMonthResponse actual = super.callUsecaseWithoutContent("", this.method, false,
        GetAvailableMonthResponse.class);
    this.assertEquals(expected, actual);
    actual = super.callUsecaseWithoutContent("/2010", this.method, false,
        GetAvailableMonthResponse.class);
    this.assertEquals(expected, actual);
  }

  @Test
  public void test_invalidMonth_defaultsResponse() throws Exception {
    final GetAvailableMonthResponse expected = new GetAvailableMonthResponse();
    expected.setYear((short) 2010);
    expected.setAllYears(ALL_YEARS);
    expected.setAllMonth(Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4, (short) 5));
    GetAvailableMonthResponse actual = super.callUsecaseWithoutContent("/2010/13", this.method,
        false, GetAvailableMonthResponse.class);
    this.assertEquals(expected, actual);
    actual = super.callUsecaseWithoutContent("/2010/0", this.method, false,
        GetAvailableMonthResponse.class);
    this.assertEquals(expected, actual);
    actual = super.callUsecaseWithoutContent("/2010/11", this.method, false,
        GetAvailableMonthResponse.class);
    this.assertEquals(expected, actual);
  }

  @Test
  public void test_DecemberFirstMonthAtAllSettledAndAlsoPreviousMonthSettled_completeResponse()
      throws Exception {
    final GetAvailableMonthResponse expected = new GetAvailableMonthResponse();
    expected.setYear((short) 2008);
    expected.setAllYears(ALL_YEARS);
    expected.setAllMonth(Arrays.asList((short) 12));
    expected.setMonth((short) 12);
    expected.setNextMonth((short) 1);
    expected.setNextYear((short) 2009);
    expected.setNextMonthHasMoneyflows((short) 1);
    final GetAvailableMonthResponse actual = super.callUsecaseWithoutContent("/2008/12",
        this.method, false, GetAvailableMonthResponse.class);
    this.assertEquals(expected, actual);
  }

  @Test
  public void test_JanuarySettledAndAlsoPreviousMonthSettled_completeResponse() throws Exception {
    final GetAvailableMonthResponse expected = new GetAvailableMonthResponse();
    expected.setYear((short) 2009);
    expected.setAllYears(ALL_YEARS);
    expected.setAllMonth(Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4, (short) 5,
        (short) 6, (short) 7, (short) 8, (short) 9, (short) 10, (short) 11, (short) 12));
    expected.setMonth((short) 1);
    expected.setPreviousMonth((short) 12);
    expected.setPreviousYear((short) 2008);
    expected.setNextMonth((short) 2);
    expected.setNextYear((short) 2009);
    expected.setNextMonthHasMoneyflows((short) 1);
    expected.setPreviousMonthHasMoneyflows((short) 1);
    final GetAvailableMonthResponse actual = super.callUsecaseWithoutContent("/2009/1", this.method,
        false, GetAvailableMonthResponse.class);
    this.assertEquals(expected, actual);
  }

  @Test
  public void test_DecemberSettledAndAlsoPreviousMonthSettled_completeResponse() throws Exception {
    final GetAvailableMonthResponse expected = new GetAvailableMonthResponse();
    expected.setYear((short) 2009);
    expected.setAllYears(ALL_YEARS);
    expected.setAllMonth(Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4, (short) 5,
        (short) 6, (short) 7, (short) 8, (short) 9, (short) 10, (short) 11, (short) 12));
    expected.setMonth((short) 12);
    expected.setPreviousMonth((short) 11);
    expected.setPreviousYear((short) 2009);
    expected.setNextMonth((short) 1);
    expected.setNextYear((short) 2010);
    expected.setNextMonthHasMoneyflows((short) 1);
    expected.setPreviousMonthHasMoneyflows((short) 1);
    final GetAvailableMonthResponse actual = super.callUsecaseWithoutContent("/2009/12",
        this.method, false, GetAvailableMonthResponse.class);
    this.assertEquals(expected, actual);
  }

  @Test
  public void test_DecemberAndNoMoneyflowsNextMonth_completeResponse() throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final MoneyflowID moneyflowId = new MoneyflowID(MoneyflowTransportBuilder.MONEYFLOW14_ID);
    this.moneyflowService.deleteMoneyflow(userId, moneyflowId);
    final GetAvailableMonthResponse expected = new GetAvailableMonthResponse();
    expected.setYear((short) 2009);
    expected.setAllYears(ALL_YEARS);
    expected.setAllMonth(Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4, (short) 5,
        (short) 6, (short) 7, (short) 8, (short) 9, (short) 10, (short) 11, (short) 12));
    expected.setMonth((short) 12);
    expected.setPreviousMonth((short) 11);
    expected.setPreviousYear((short) 2009);
    expected.setNextMonth((short) 2);
    expected.setNextYear((short) 2010);
    expected.setPreviousMonthHasMoneyflows((short) 1);
    expected.setNextMonthHasMoneyflows((short) 1);
    final GetAvailableMonthResponse actual = super.callUsecaseWithoutContent("/2009/12",
        this.method, false, GetAvailableMonthResponse.class);
    this.assertEquals(expected, actual);
  }

  @Test
  public void test_AuthorizationRequired_01_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;
    super.callUsecaseExpect403("", this.method);
  }

  @Test
  public void test_AuthorizationRequired_02_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;
    super.callUsecaseExpect403("/2010", this.method);
  }

  @Test
  public void test_AuthorizationRequired_03_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;
    super.callUsecaseExpect403("/2010/1", this.method);
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  public void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    super.callUsecaseWithoutContent("", this.method, false, GetAvailableMonthResponse.class);
  }
}