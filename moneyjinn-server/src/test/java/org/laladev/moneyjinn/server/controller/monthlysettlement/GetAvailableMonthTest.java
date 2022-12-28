
package org.laladev.moneyjinn.server.controller.monthlysettlement;

import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.rest.model.monthlysettlement.GetAvailableMonthResponse;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
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

  private GetAvailableMonthResponse getDefaultResponse() {
    final GetAvailableMonthResponse expected = new GetAvailableMonthResponse();
    expected.setAllMonth(Arrays.asList((short) 1, (short) 2, (short) 3, (short) 4));
    expected.setAllYears(Arrays.asList((short) 2008, (short) 2009, (short) 2010));
    expected.setYear((short) 2010);
    return expected;
  }

  @Test
  public void test_default_FullResponseObject() throws Exception {
    final GetAvailableMonthResponse expected = this.getDefaultResponse();
    final GetAvailableMonthResponse actual = super.callUsecaseWithoutContent("", this.method, false,
        GetAvailableMonthResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_withYear_FullResponseObject() throws Exception {
    final GetAvailableMonthResponse expected = this.getDefaultResponse();
    final GetAvailableMonthResponse actual = super.callUsecaseWithoutContent("/2010", this.method,
        false, GetAvailableMonthResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_withInvalidYear_FullResponseObject() throws Exception {
    final GetAvailableMonthResponse expected = this.getDefaultResponse();
    final GetAvailableMonthResponse actual = super.callUsecaseWithoutContent("/1972", this.method,
        false, GetAvailableMonthResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_withYearAndInvalidMonth_FullResponseObject() throws Exception {
    final GetAvailableMonthResponse expected = this.getDefaultResponse();
    final GetAvailableMonthResponse actual = super.callUsecaseWithoutContent("/2010/10",
        this.method, false, GetAvailableMonthResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_withInvalidYearAndInvalidMonth13_FullResponseObject() throws Exception {
    final GetAvailableMonthResponse expected = this.getDefaultResponse();
    final GetAvailableMonthResponse actual = super.callUsecaseWithoutContent("/1/13", this.method,
        false, GetAvailableMonthResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_withInvalidYearAndInvalidMonth0_FullResponseObject() throws Exception {
    final GetAvailableMonthResponse expected = this.getDefaultResponse();
    final GetAvailableMonthResponse actual = super.callUsecaseWithoutContent("/1/0", this.method,
        false, GetAvailableMonthResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_withYearAndMonth_FullResponseObject() throws Exception {
    final GetAvailableMonthResponse expected = new GetAvailableMonthResponse();
    expected.setAllMonth(Arrays.asList((short) 11, (short) 12));
    expected.setAllYears(Arrays.asList((short) 2008, (short) 2009, (short) 2010));
    expected.setYear((short) 2008);
    expected.setMonth((short) 12);
    final GetAvailableMonthResponse actual = super.callUsecaseWithoutContent("/2008/12",
        this.method, false, GetAvailableMonthResponse.class);
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
    final GetAvailableMonthResponse expected = new GetAvailableMonthResponse();
    final GetAvailableMonthResponse actual = super.callUsecaseWithoutContent("", this.method, false,
        GetAvailableMonthResponse.class);
    Assertions.assertEquals(expected, actual);
  }
}
