
package org.laladev.moneyjinn.server.controller.monthlysettlement;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.controller.api.MonthlySettlementControllerApi;
import org.laladev.moneyjinn.server.model.GetAvailableMonthlySettlementMonthResponse;
import org.springframework.test.context.jdbc.Sql;

public class GetAvailableMonthYearTest extends AbstractControllerTest {
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
    return super.getMethodFromTestClassName(MonthlySettlementControllerApi.class, this.getClass());
  }

  private GetAvailableMonthlySettlementMonthResponse getDefaultResponse() {
    final GetAvailableMonthlySettlementMonthResponse expected = new GetAvailableMonthlySettlementMonthResponse();
    expected.setAllMonth(Arrays.asList(1, 2, 3, 4));
    expected.setAllYears(Arrays.asList(2008, 2009, 2010));
    expected.setYear(2010);
    return expected;
  }

  @Test
  public void test_withYear_FullResponseObject() throws Exception {
    final GetAvailableMonthlySettlementMonthResponse expected = this.getDefaultResponse();
    final GetAvailableMonthlySettlementMonthResponse actual = super.callUsecaseExpect200(
        GetAvailableMonthlySettlementMonthResponse.class, 2010);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_withInvalidYear_FullResponseObject() throws Exception {
    final GetAvailableMonthlySettlementMonthResponse expected = this.getDefaultResponse();
    final GetAvailableMonthlySettlementMonthResponse actual = super.callUsecaseExpect200(
        GetAvailableMonthlySettlementMonthResponse.class, 1972);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_AuthorizationRequired_2_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;

    super.callUsecaseExpect403WithUriVariables(2008);
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  public void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    final GetAvailableMonthlySettlementMonthResponse expected = new GetAvailableMonthlySettlementMonthResponse();

    final GetAvailableMonthlySettlementMonthResponse actual = super.callUsecaseExpect200(
        GetAvailableMonthlySettlementMonthResponse.class, 2010);

    Assertions.assertEquals(expected, actual);
  }
}