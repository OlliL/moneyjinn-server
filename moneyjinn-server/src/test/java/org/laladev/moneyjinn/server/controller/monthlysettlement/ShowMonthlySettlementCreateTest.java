
package org.laladev.moneyjinn.server.controller.monthlysettlement;

import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.MonthlySettlementTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.controller.mapper.CapitalsourceTransportMapper;
import org.laladev.moneyjinn.server.model.CapitalsourceTransport;
import org.laladev.moneyjinn.server.model.MonthlySettlementTransport;
import org.laladev.moneyjinn.server.model.ShowMonthlySettlementCreateResponse;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class ShowMonthlySettlementCreateTest extends AbstractControllerTest {
  private final HttpMethod method = HttpMethod.GET;
  private String userName;
  private String userPassword;
  @Inject
  private ICapitalsourceService capitalsourceService;
  @Inject
  private CapitalsourceTransportMapper capitalsourceTransportMapper;

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
  public void test_notAlreadySettledMonth_FullContentWithAmountAll0() throws Exception {
    final ShowMonthlySettlementCreateResponse expected = new ShowMonthlySettlementCreateResponse();
    final List<MonthlySettlementTransport> monthlySettlementTransports = new ArrayList<>();
    monthlySettlementTransports.add(new MonthlySettlementTransportBuilder().forMonthlySettlement1()
        .withId(null).withYear(2000).withMonth(1).withAmount(BigDecimal.ZERO).build());
    monthlySettlementTransports.add(new MonthlySettlementTransportBuilder().forMonthlySettlement2()
        .withId(null).withYear(2000).withMonth(1).withAmount(BigDecimal.ZERO).build());
    expected.setMonthlySettlementTransports(monthlySettlementTransports);
    expected.setYear(2000);
    expected.setMonth(1);
    expected.setEditMode(0);
    final ShowMonthlySettlementCreateResponse actual = super.callUsecaseExpect200("/2000/1",
        this.method, ShowMonthlySettlementCreateResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_alreadySettledMonth_FullContent() throws Exception {
    final ShowMonthlySettlementCreateResponse expected = new ShowMonthlySettlementCreateResponse();
    final List<MonthlySettlementTransport> monthlySettlementTransports = new ArrayList<>();
    monthlySettlementTransports
        .add(new MonthlySettlementTransportBuilder().forMonthlySettlement1().build());
    monthlySettlementTransports
        .add(new MonthlySettlementTransportBuilder().forMonthlySettlement2().build());
    expected.setMonthlySettlementTransports(monthlySettlementTransports);
    expected.setYear(2008);
    expected.setMonth(12);
    expected.setEditMode(1);
    ShowMonthlySettlementCreateResponse actual = super.callUsecaseExpect200("/2008/12", this.method,
        ShowMonthlySettlementCreateResponse.class);
    Assertions.assertEquals(expected, actual);
    actual = super.callUsecaseExpect200("/2008", this.method,
        ShowMonthlySettlementCreateResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_alreadySettledMonthWithNewCapitalsourceCreatedAfterwardsAndValid_FullContent()
      throws Exception {
    final ShowMonthlySettlementCreateResponse expected = new ShowMonthlySettlementCreateResponse();
    final List<MonthlySettlementTransport> monthlySettlementTransports = new ArrayList<>();
    monthlySettlementTransports
        .add(new MonthlySettlementTransportBuilder().forMonthlySettlement1().build());
    monthlySettlementTransports
        .add(new MonthlySettlementTransportBuilder().forMonthlySettlement2().build());
    final CapitalsourceTransport newCapitalsourceTransport = new CapitalsourceTransportBuilder()
        .forNewCapitalsource().withUserId(UserTransportBuilder.USER1_ID)
        .withId(CapitalsourceTransportBuilder.NEXT_ID).build();
    final Capitalsource newCapitalsource = this.capitalsourceTransportMapper
        .mapBToA(newCapitalsourceTransport);
    newCapitalsource.setAccess(new Group(new GroupID(GroupTransportBuilder.GROUP1_ID)));
    this.capitalsourceService.createCapitalsource(newCapitalsource);
    final MonthlySettlementTransport monthlySettlementTransport = new MonthlySettlementTransportBuilder()
        .withCapitalsource(newCapitalsourceTransport).withAmount(BigDecimal.ZERO).withMonth(12)
        .withYear(2008).withUserId(UserTransportBuilder.USER1_ID).build();
    monthlySettlementTransports.add(monthlySettlementTransport);
    expected.setMonthlySettlementTransports(monthlySettlementTransports);
    expected.setYear(2008);
    expected.setMonth(12);
    expected.setEditMode(1);
    ShowMonthlySettlementCreateResponse actual = super.callUsecaseExpect200("/2008/12", this.method,
        ShowMonthlySettlementCreateResponse.class);
    Assertions.assertEquals(expected, actual);
    actual = super.callUsecaseExpect200("/2008", this.method,
        ShowMonthlySettlementCreateResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_nextUnsettledMonthExplicitlyAndByDefault_FullContentWithCalculatedAmount()
      throws Exception {
    final ShowMonthlySettlementCreateResponse expected = new ShowMonthlySettlementCreateResponse();
    final List<MonthlySettlementTransport> monthlySettlementTransports = new ArrayList<>();
    monthlySettlementTransports.add(new MonthlySettlementTransportBuilder().forMonthlySettlement1()
        .withId(null).withYear(2010).withMonth(5).withAmount(BigDecimal.ZERO).build());
    monthlySettlementTransports.add(new MonthlySettlementTransportBuilder().forMonthlySettlement2()
        .withId(null).withYear(2010).withMonth(5).build());
    expected.setMonthlySettlementTransports(monthlySettlementTransports);
    expected.setYear(2010);
    expected.setMonth(5);
    expected.setEditMode(0);
    ShowMonthlySettlementCreateResponse actual = super.callUsecaseExpect200(this.method,
        ShowMonthlySettlementCreateResponse.class);
    Assertions.assertEquals(expected, actual);
    actual = super.callUsecaseExpect200("/2010/5", this.method,
        ShowMonthlySettlementCreateResponse.class);
    Assertions.assertEquals(expected, actual);
    actual = super.callUsecaseExpect200("/2010", this.method,
        ShowMonthlySettlementCreateResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_nextUnsettledMonthWithImportedData_FullContentWithCalculatedAmount()
      throws Exception {
    this.userName = UserTransportBuilder.USER3_NAME;
    this.userPassword = UserTransportBuilder.USER3_PASSWORD;
    final ShowMonthlySettlementCreateResponse expected = new ShowMonthlySettlementCreateResponse();
    final List<MonthlySettlementTransport> importedMonthlySettlementTransports = new ArrayList<>();
    importedMonthlySettlementTransports
        .add(new MonthlySettlementTransportBuilder().forMonthlySettlement3().withId(null)
            .withYear(2010).withMonth(5).withAmount(new BigDecimal("9.00")).build());
    expected.setImportedMonthlySettlementTransports(importedMonthlySettlementTransports);
    final List<MonthlySettlementTransport> monthlySettlementTransports = new ArrayList<>();
    monthlySettlementTransports
        .add(new MonthlySettlementTransportBuilder().forCapitalsource6().build());
    expected.setMonthlySettlementTransports(monthlySettlementTransports);
    expected.setYear(2010);
    expected.setMonth(5);
    expected.setEditMode(0);
    ShowMonthlySettlementCreateResponse actual = super.callUsecaseExpect200(this.method,
        ShowMonthlySettlementCreateResponse.class);
    Assertions.assertEquals(expected, actual);

    actual = super.callUsecaseExpect200("/2010/5", this.method,
        ShowMonthlySettlementCreateResponse.class);

    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;

    super.callUsecaseExpect403("/2012/08", this.method);
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  public void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    final ShowMonthlySettlementCreateResponse expected = new ShowMonthlySettlementCreateResponse();
    final LocalDate now = LocalDate.now();
    expected.setYear(now.getYear());
    expected.setMonth(now.getMonthValue());
    expected.setEditMode(0);

    final ShowMonthlySettlementCreateResponse actual = super.callUsecaseExpect200(this.method,
        ShowMonthlySettlementCreateResponse.class);

    Assertions.assertEquals(expected, actual);
  }
}