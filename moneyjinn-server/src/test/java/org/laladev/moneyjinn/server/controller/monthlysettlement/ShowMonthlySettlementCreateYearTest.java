
package org.laladev.moneyjinn.server.controller.monthlysettlement;

import jakarta.inject.Inject;
import java.math.BigDecimal;
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
import org.laladev.moneyjinn.server.controller.api.MonthlySettlementControllerApi;
import org.laladev.moneyjinn.server.controller.mapper.CapitalsourceTransportMapper;
import org.laladev.moneyjinn.server.model.CapitalsourceTransport;
import org.laladev.moneyjinn.server.model.MonthlySettlementTransport;
import org.laladev.moneyjinn.server.model.ShowMonthlySettlementCreateResponse;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.springframework.test.context.jdbc.Sql;

class ShowMonthlySettlementCreateYearTest extends AbstractControllerTest {
  @Inject
  private ICapitalsourceService capitalsourceService;
  @Inject
  private CapitalsourceTransportMapper capitalsourceTransportMapper;

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
    super.getMock(MonthlySettlementControllerApi.class).showMonthlySettlementCreateYear(null);
  }

  @Test
   void test_alreadySettledMonth_FullContent() throws Exception {
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

    final ShowMonthlySettlementCreateResponse actual = super.callUsecaseExpect200(
        ShowMonthlySettlementCreateResponse.class, 2008);

    Assertions.assertEquals(expected, actual);
  }

  @Test
   void test_alreadySettledMonthWithNewCapitalsourceCreatedAfterwardsAndValid_FullContent()
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

    final ShowMonthlySettlementCreateResponse actual = super.callUsecaseExpect200(
        ShowMonthlySettlementCreateResponse.class, 2008);

    Assertions.assertEquals(expected, actual);
  }

  @Test
   void test_nextUnsettledMonthExplicitlyAndByDefault_FullContentWithCalculatedAmount()
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

    final ShowMonthlySettlementCreateResponse actual = super.callUsecaseExpect200(
        ShowMonthlySettlementCreateResponse.class, 2010);

    Assertions.assertEquals(expected, actual);
  }

  @Test
   void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;

    super.callUsecaseExpect403WithUriVariables(2012);
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    final ShowMonthlySettlementCreateResponse expected = new ShowMonthlySettlementCreateResponse();
    expected.setYear(2012);
    expected.setMonth(12);
    expected.setEditMode(0);

    final ShowMonthlySettlementCreateResponse actual = super.callUsecaseExpect200(
        ShowMonthlySettlementCreateResponse.class, 2012);

    Assertions.assertEquals(expected, actual);
  }
}