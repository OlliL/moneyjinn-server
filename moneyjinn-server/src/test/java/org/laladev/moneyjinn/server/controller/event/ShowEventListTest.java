
package org.laladev.moneyjinn.server.controller.event;

import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowID;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowStatus;
import org.laladev.moneyjinn.model.monthlysettlement.MonthlySettlement;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.ImportedMoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.model.ShowEventListResponse;
import org.laladev.moneyjinn.service.api.IImportedMoneyflowService;
import org.laladev.moneyjinn.service.api.IMonthlySettlementService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class ShowEventListTest extends AbstractControllerTest {
  @Inject
  private IMonthlySettlementService monthlySettlementService;
  @Inject
  private IImportedMoneyflowService importedMoneyflowService;
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
  public void test_previousMonthIsNotSettled_completeResponseObject() throws Exception {
    final ShowEventListResponse expected = new ShowEventListResponse();
    final LocalDate lastMonth = LocalDate.now().minusMonths(1l);
    expected.setMonthlySettlementMissing(true);
    expected.setMonthlySettlementMonth((lastMonth.getMonthValue()));
    expected.setMonthlySettlementYear((lastMonth.getYear()));
    expected.setNumberOfImportedMoneyflows(2);

    final ShowEventListResponse actual = super.callUsecaseExpect200(this.method,
        ShowEventListResponse.class);

    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_previousMonthIsSettled_completeResponseObject() throws Exception {
    final ShowEventListResponse expected = new ShowEventListResponse();
    final LocalDate lastMonth = LocalDate.now().minusMonths(1l);
    final MonthlySettlement monthlySettlement = new MonthlySettlement();
    monthlySettlement.setYear(lastMonth.getYear());
    monthlySettlement.setMonth(lastMonth.getMonth());
    monthlySettlement.setCapitalsource(
        new Capitalsource(new CapitalsourceID(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID)));
    monthlySettlement.setAmount(BigDecimal.TEN);
    monthlySettlement.setUser(new User(new UserID(UserTransportBuilder.USER1_ID)));
    monthlySettlement.setGroup(new Group(new GroupID(GroupTransportBuilder.GROUP1_ID)));
    this.monthlySettlementService.upsertMonthlySettlements(Arrays.asList(monthlySettlement));
    expected.setMonthlySettlementMissing(false);
    expected.setMonthlySettlementMonth((lastMonth.getMonthValue()));
    expected.setMonthlySettlementYear((lastMonth.getYear()));
    expected.setNumberOfImportedMoneyflows(2);

    final ShowEventListResponse actual = super.callUsecaseExpect200(this.method,
        ShowEventListResponse.class);

    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_deletedImportedMoneyflow_isIgnored() throws Exception {

    ShowEventListResponse actual = super.callUsecaseExpect200(this.method,
        ShowEventListResponse.class);

    Assertions.assertEquals(Integer.valueOf(2), actual.getNumberOfImportedMoneyflows());
    this.importedMoneyflowService.deleteImportedMoneyflowById(
        new UserID(UserTransportBuilder.USER1_ID),
        new ImportedMoneyflowID(ImportedMoneyflowTransportBuilder.IMPORTED_MONEYFLOW1_ID));

    actual = super.callUsecaseExpect200(this.method, ShowEventListResponse.class);

    Assertions.assertEquals(Integer.valueOf(1), actual.getNumberOfImportedMoneyflows());
  }

  @Test
  public void test_importedImportedMoneyflow_isIgnored() throws Exception {

    ShowEventListResponse actual = super.callUsecaseExpect200(this.method,
        ShowEventListResponse.class);

    final int numberOfImportedMoneyflows = actual.getNumberOfImportedMoneyflows().intValue();
    this.importedMoneyflowService.updateImportedMoneyflowStatus(
        new UserID(UserTransportBuilder.USER1_ID),
        new ImportedMoneyflowID(ImportedMoneyflowTransportBuilder.IMPORTED_MONEYFLOW1_ID),
        ImportedMoneyflowStatus.PROCESSED);

    actual = super.callUsecaseExpect200(this.method, ShowEventListResponse.class);

    Assertions.assertEquals(Integer.valueOf(numberOfImportedMoneyflows - 1),
        actual.getNumberOfImportedMoneyflows());
  }

  @Test
  public void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;

    super.callUsecaseExpect403(this.method);
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  public void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;

    super.callUsecaseExpect200(this.method, ShowEventListResponse.class);

  }
}