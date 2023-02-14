
package org.laladev.moneyjinn.server.controller.monthlysettlement;

import jakarta.inject.Inject;
import java.time.Month;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.monthlysettlement.MonthlySettlement;
import org.laladev.moneyjinn.server.builder.MonthlySettlementTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.IMonthlySettlementService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class DeleteMonthlySettlementTest extends AbstractControllerTest {
  @Inject
  private IMonthlySettlementService monthlySettlementService;
  private final HttpMethod method = HttpMethod.DELETE;
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
  public void test_regularMonthlySettlement_SuccessfullNoContent() throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    List<MonthlySettlement> monthlySettlements = this.monthlySettlementService
        .getAllMonthlySettlementsByYearMonth(userId,  2008, Month.DECEMBER);
    Assertions.assertNotNull(monthlySettlements);
    Assertions.assertEquals(3, monthlySettlements.size());
    super.callUsecaseWithoutContent("/2008/12", this.method, true, Object.class);
    monthlySettlements = this.monthlySettlementService.getAllMonthlySettlementsByYearMonth(userId,
         2008, Month.DECEMBER);
    Assertions.assertNotNull(monthlySettlements);
    Assertions.assertEquals(1, monthlySettlements.size());
    Assertions.assertEquals(MonthlySettlementTransportBuilder.MONTHLYSETTLEMENT3_ID,
        monthlySettlements.iterator().next().getId().getId());
  }

  @Test
  public void test_nonExistingMonthlySettlement_SuccessfullNoContent() throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    List<MonthlySettlement> monthlySettlements = this.monthlySettlementService
        .getAllMonthlySettlementsByYearMonth(userId,  1970, Month.OCTOBER);
    Assertions.assertNotNull(monthlySettlements);
    Assertions.assertTrue(monthlySettlements.isEmpty());
    super.callUsecaseWithoutContent("/1970/10", this.method, true, Object.class);
    monthlySettlements = this.monthlySettlementService.getAllMonthlySettlementsByYearMonth(userId,
         1970, Month.OCTOBER);
    Assertions.assertNotNull(monthlySettlements);
    Assertions.assertTrue(monthlySettlements.isEmpty());
  }

  @Test
  public void test_MonthlySettlementFromDifferentGroup_notSuccessfull() throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.ADMIN_ID);
    List<MonthlySettlement> monthlySettlements = this.monthlySettlementService
        .getAllMonthlySettlementsByYearMonth(userId,  2008, Month.DECEMBER);
    Assertions.assertNotNull(monthlySettlements);
    Assertions.assertTrue(monthlySettlements.isEmpty());
    super.callUsecaseWithoutContent("/2008/12", this.method, true, Object.class);
    monthlySettlements = this.monthlySettlementService.getAllMonthlySettlementsByYearMonth(userId,
         2008, Month.DECEMBER);
    Assertions.assertNotNull(monthlySettlements);
    Assertions.assertTrue(monthlySettlements.isEmpty());
  }

  @Test
  public void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;
    super.callUsecaseExpect403("/2008/12", this.method);
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  public void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    super.callUsecaseWithoutContent("/2008/12", this.method, true, Object.class);
  }
}