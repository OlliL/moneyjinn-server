
package org.laladev.moneyjinn.server.controller.monthlysettlement;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.MonthlySettlementTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.model.MonthlySettlementTransport;
import org.laladev.moneyjinn.server.model.ShowMonthlySettlementListResponse;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class ShowMonthlySettlementListV2Test extends AbstractControllerTest {
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

  @Test
  public void test_withYearAndInvalidMonth_EmptyResponseObject() throws Exception {
    final ShowMonthlySettlementListResponse expected = new ShowMonthlySettlementListResponse();
    expected.setMonthlySettlementTransports(new ArrayList<>());
    final ShowMonthlySettlementListResponse actual = super.callUsecaseExpect200("/2010/10",
        this.method, ShowMonthlySettlementListResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_withInvalidYearAndInvalidMonth13_EmptyResponseObject() throws Exception {
    final ShowMonthlySettlementListResponse expected = new ShowMonthlySettlementListResponse();
    final ShowMonthlySettlementListResponse actual = super.callUsecaseExpect200("/1/13",
        this.method, ShowMonthlySettlementListResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_withInvalidYearAndInvalidMonth0_EmptyResponseObject() throws Exception {
    final ShowMonthlySettlementListResponse expected = new ShowMonthlySettlementListResponse();
    final ShowMonthlySettlementListResponse actual = super.callUsecaseExpect200("/1/0", this.method,
        ShowMonthlySettlementListResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_withYearAndMonth_FullResponseObject() throws Exception {
    final List<MonthlySettlementTransport> monthlySettlementTransports = new ArrayList<>();
    monthlySettlementTransports
        .add(new MonthlySettlementTransportBuilder().forMonthlySettlement1().build());
    monthlySettlementTransports
        .add(new MonthlySettlementTransportBuilder().forMonthlySettlement2().build());
    monthlySettlementTransports
        .add(new MonthlySettlementTransportBuilder().forMonthlySettlement3().build());
    final ShowMonthlySettlementListResponse expected = new ShowMonthlySettlementListResponse();
    expected.setMonthlySettlementTransports(monthlySettlementTransports);
    final ShowMonthlySettlementListResponse actual = super.callUsecaseExpect200("/2008/12",
        this.method, ShowMonthlySettlementListResponse.class);
    Assertions.assertEquals(expected, actual);
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
    final ShowMonthlySettlementListResponse expected = new ShowMonthlySettlementListResponse();
    expected.setMonthlySettlementTransports(new ArrayList<>());
    final ShowMonthlySettlementListResponse actual = super.callUsecaseExpect200("/2020/10",
        this.method, ShowMonthlySettlementListResponse.class);
    Assertions.assertEquals(expected, actual);
  }
}
