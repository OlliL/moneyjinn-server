
package org.laladev.moneyjinn.server.controller.etf;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.EtfEffectiveFlowTransportBuilder;
import org.laladev.moneyjinn.server.builder.EtfTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.model.EtfEffectiveFlowTransport;
import org.laladev.moneyjinn.server.model.EtfSummaryTransport;
import org.laladev.moneyjinn.server.model.EtfTransport;
import org.laladev.moneyjinn.server.model.ListEtfOverviewResponse;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class ListEtfOverviewTest extends AbstractControllerTest {
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
  public void test_standardRequest_FullResponseObject() throws Exception {
    final EtfTransport etf = new EtfTransportBuilder().forEtf1().build();
    final EtfEffectiveFlowTransport flow1 = new EtfEffectiveFlowTransportBuilder().forFlow1()
        .build();
    final EtfEffectiveFlowTransport flow3 = new EtfEffectiveFlowTransportBuilder().forFlow3()
        .build();

    final ListEtfOverviewResponse expected = new ListEtfOverviewResponse();

    final EtfSummaryTransport transport = new EtfSummaryTransport();
    final BigDecimal amount = flow1.getAmount().add(flow3.getAmount());
    final BigDecimal spentValue = flow1.getAmount().multiply(flow1.getPrice())
        .add(flow3.getAmount().multiply(flow3.getPrice()));
    transport.setIsin(etf.getIsin());
    transport.setName(etf.getName());
    transport.setChartUrl(etf.getChartUrl());
    transport.setAmount(amount);
    transport.setSpentValue(spentValue);
    // latest etfvalues table entry
    transport.setBuyPrice(new BigDecimal("666.000"));
    transport.setSellPrice(new BigDecimal("666.543"));
    transport.setPricesTimestamp(OffsetDateTime.of(2008, 12, 16, 22, 5, 2, 0, ZoneOffset.UTC));

    expected.setEtfSummaryTransports(Collections.singletonList(transport));

    final ListEtfOverviewResponse actual = super.callUsecaseWithoutContent("/2008/12", this.method,
        false, ListEtfOverviewResponse.class);

    Assertions.assertEquals(expected, actual);

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

    super.callUsecaseWithoutContent("/2008/12", this.method, false, ListEtfOverviewResponse.class);

  }
}