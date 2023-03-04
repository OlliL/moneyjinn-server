
package org.laladev.moneyjinn.server.controller.monthlysettlement;

import jakarta.inject.Inject;
import java.lang.reflect.Method;
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
import org.laladev.moneyjinn.server.controller.api.MonthlySettlementControllerApi;
import org.laladev.moneyjinn.service.api.IMonthlySettlementService;
import org.springframework.test.context.jdbc.Sql;

public class DeleteMonthlySettlementTest extends AbstractControllerTest {
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
    return super.getMethodFromTestClassName(MonthlySettlementControllerApi.class, this.getClass());
  }

  @Test
  public void test_regularMonthlySettlement_SuccessfullNoContent() throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    List<MonthlySettlement> monthlySettlements = this.monthlySettlementService
        .getAllMonthlySettlementsByYearMonth(userId, 2008, Month.DECEMBER);
    Assertions.assertNotNull(monthlySettlements);
    Assertions.assertEquals(3, monthlySettlements.size());
    super.callUsecaseExpect204WithUriVariables(2008, 12);
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
        .getAllMonthlySettlementsByYearMonth(userId, 1970, Month.OCTOBER);
    Assertions.assertNotNull(monthlySettlements);
    Assertions.assertTrue(monthlySettlements.isEmpty());
    super.callUsecaseExpect204WithUriVariables(1970, 10);
    monthlySettlements = this.monthlySettlementService.getAllMonthlySettlementsByYearMonth(userId,
        1970, Month.OCTOBER);
    Assertions.assertNotNull(monthlySettlements);
    Assertions.assertTrue(monthlySettlements.isEmpty());
  }

  @Test
  public void test_MonthlySettlementFromDifferentGroup_notSuccessfull() throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.ADMIN_ID);
    List<MonthlySettlement> monthlySettlements = this.monthlySettlementService
        .getAllMonthlySettlementsByYearMonth(userId, 2008, Month.DECEMBER);
    Assertions.assertNotNull(monthlySettlements);
    Assertions.assertTrue(monthlySettlements.isEmpty());

    super.callUsecaseExpect204WithUriVariables(2008, 12);

    monthlySettlements = this.monthlySettlementService.getAllMonthlySettlementsByYearMonth(userId,
        2008, Month.DECEMBER);
    Assertions.assertNotNull(monthlySettlements);
    Assertions.assertTrue(monthlySettlements.isEmpty());
  }

  @Test
  public void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;

    super.callUsecaseExpect403WithUriVariables(2008, 12);
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  public void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;

    super.callUsecaseExpect204WithUriVariables(2008, 12);
  }
}