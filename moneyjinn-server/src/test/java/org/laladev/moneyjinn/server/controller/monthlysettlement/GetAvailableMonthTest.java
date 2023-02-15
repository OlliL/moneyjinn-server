
package org.laladev.moneyjinn.server.controller.monthlysettlement;

import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.model.GetAvailableMonthlySettlementMonthResponse;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class GetAvailableMonthTest extends AbstractControllerTest {
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

  private GetAvailableMonthlySettlementMonthResponse getDefaultResponse() {
    final GetAvailableMonthlySettlementMonthResponse expected = new GetAvailableMonthlySettlementMonthResponse();
    expected.setAllMonth(Arrays.asList(1, 2, 3, 4));
    expected.setAllYears(Arrays.asList(2008, 2009, 2010));
    expected.setYear(2010);
    return expected;
  }

  @Test
  public void test_default_FullResponseObject() throws Exception {
    final GetAvailableMonthlySettlementMonthResponse expected = this.getDefaultResponse();
    final GetAvailableMonthlySettlementMonthResponse actual = super.callUsecaseWithoutContent("",
        this.method, false, GetAvailableMonthlySettlementMonthResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_withYear_FullResponseObject() throws Exception {
    final GetAvailableMonthlySettlementMonthResponse expected = this.getDefaultResponse();
    final GetAvailableMonthlySettlementMonthResponse actual = super.callUsecaseWithoutContent(
        "/2010", this.method, false, GetAvailableMonthlySettlementMonthResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_withInvalidYear_FullResponseObject() throws Exception {
    final GetAvailableMonthlySettlementMonthResponse expected = this.getDefaultResponse();
    final GetAvailableMonthlySettlementMonthResponse actual = super.callUsecaseWithoutContent(
        "/1972", this.method, false, GetAvailableMonthlySettlementMonthResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_withYearAndInvalidMonth_FullResponseObject() throws Exception {
    final GetAvailableMonthlySettlementMonthResponse expected = this.getDefaultResponse();
    final GetAvailableMonthlySettlementMonthResponse actual = super.callUsecaseWithoutContent(
        "/2010/10", this.method, false, GetAvailableMonthlySettlementMonthResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_withInvalidYearAndInvalidMonth13_FullResponseObject() throws Exception {
    final GetAvailableMonthlySettlementMonthResponse expected = this.getDefaultResponse();
    final GetAvailableMonthlySettlementMonthResponse actual = super.callUsecaseWithoutContent(
        "/1/13", this.method, false, GetAvailableMonthlySettlementMonthResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_withInvalidYearAndInvalidMonth0_FullResponseObject() throws Exception {
    final GetAvailableMonthlySettlementMonthResponse expected = this.getDefaultResponse();
    final GetAvailableMonthlySettlementMonthResponse actual = super.callUsecaseWithoutContent(
        "/1/0", this.method, false, GetAvailableMonthlySettlementMonthResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_withYearAndMonth_FullResponseObject() throws Exception {
    final GetAvailableMonthlySettlementMonthResponse expected = new GetAvailableMonthlySettlementMonthResponse();
    expected.setAllMonth(Arrays.asList(11, 12));
    expected.setAllYears(Arrays.asList(2008, 2009, 2010));
    expected.setYear(2008);
    expected.setMonth(12);
    final GetAvailableMonthlySettlementMonthResponse actual = super.callUsecaseWithoutContent(
        "/2008/12", this.method, false, GetAvailableMonthlySettlementMonthResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_AuthorizationRequired_1_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;
    super.callUsecaseExpect403("", this.method);
  }

  @Test
  public void test_AuthorizationRequired_2_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;
    super.callUsecaseExpect403("/2008", this.method);
  }

  @Test
  public void test_AuthorizationRequired_3_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;
    super.callUsecaseExpect403("/2008/12", this.method);
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  public void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    final GetAvailableMonthlySettlementMonthResponse expected = new GetAvailableMonthlySettlementMonthResponse();
    final GetAvailableMonthlySettlementMonthResponse actual = super.callUsecaseWithoutContent("",
        this.method, false, GetAvailableMonthlySettlementMonthResponse.class);
    Assertions.assertEquals(expected, actual);
  }
}
