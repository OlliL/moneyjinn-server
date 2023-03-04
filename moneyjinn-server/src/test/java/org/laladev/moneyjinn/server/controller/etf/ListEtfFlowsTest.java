
package org.laladev.moneyjinn.server.controller.etf;

import jakarta.inject.Inject;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSaleAskPrice;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSaleBidPrice;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSaleIsin;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSalePieces;
import org.laladev.moneyjinn.model.setting.ClientCalcEtfSaleTransactionCosts;
import org.laladev.moneyjinn.server.builder.EtfEffectiveFlowTransportBuilder;
import org.laladev.moneyjinn.server.builder.EtfFlowTransportBuilder;
import org.laladev.moneyjinn.server.builder.EtfTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.controller.api.EtfControllerApi;
import org.laladev.moneyjinn.server.model.EtfEffectiveFlowTransport;
import org.laladev.moneyjinn.server.model.EtfFlowTransport;
import org.laladev.moneyjinn.server.model.EtfTransport;
import org.laladev.moneyjinn.server.model.ListEtfFlowsResponse;
import org.laladev.moneyjinn.service.api.ISettingService;
import org.springframework.test.context.jdbc.Sql;

public class ListEtfFlowsTest extends AbstractControllerTest {
  @Inject
  ISettingService settingService;

  private String userName;
  private String userPassword;

  private final static BigDecimal SETTING_SALE_ASK_PRICE = new BigDecimal("800.000");
  private final static BigDecimal SETTING_SALE_BID_PRICE = new BigDecimal("879.500");
  private final static String SETTING_ISIN = EtfTransportBuilder.ISIN;
  private final static BigDecimal SETTING_SALE_PIECES = new BigDecimal("10");
  private final static BigDecimal SETTING_SALE_TRANSACTION_COSTS = new BigDecimal("0.99");

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
    return super.getMethodFromTestClassName(EtfControllerApi.class, this.getClass());
  }

  private UserID getUserId() {
    return new UserID(UserTransportBuilder.USER1_ID);
  }

  private void initEtfSettings() {
    this.settingService.setClientCalcEtfSaleAskPrice(this.getUserId(),
        new ClientCalcEtfSaleAskPrice(SETTING_SALE_ASK_PRICE));
    this.settingService.setClientCalcEtfSaleBidPrice(this.getUserId(),
        new ClientCalcEtfSaleBidPrice(SETTING_SALE_BID_PRICE));
    this.settingService.setClientCalcEtfSaleIsin(this.getUserId(),
        new ClientCalcEtfSaleIsin(SETTING_ISIN));
    this.settingService.setClientCalcEtfSalePieces(this.getUserId(),
        new ClientCalcEtfSalePieces(SETTING_SALE_PIECES));
    this.settingService.setClientCalcEtfSaleTransactionCosts(this.getUserId(),
        new ClientCalcEtfSaleTransactionCosts(SETTING_SALE_TRANSACTION_COSTS));
  }

  private ListEtfFlowsResponse fillDefaultResponse() {
    final ListEtfFlowsResponse expected = new ListEtfFlowsResponse();

    final List<EtfTransport> etfs = new ArrayList<>();
    etfs.add(new EtfTransportBuilder().forEtf1().build());
    expected.setEtfTransports(etfs);

    final List<EtfFlowTransport> allTransports = new ArrayList<>();
    allTransports.add(new EtfFlowTransportBuilder().forFlow3().build());
    allTransports.add(new EtfFlowTransportBuilder().forFlow2().build());
    allTransports.add(new EtfFlowTransportBuilder().forFlow1().build());
    expected.setEtfFlowTransports(allTransports);

    final List<EtfEffectiveFlowTransport> effectiveTransports = new ArrayList<>();
    effectiveTransports.add(new EtfEffectiveFlowTransportBuilder().forFlow3().build());
    effectiveTransports.add(new EtfEffectiveFlowTransportBuilder().forFlow1().build());
    expected.setEtfEffectiveFlowTransports(effectiveTransports);
    return expected;
  }

  @Test
  public void test_standardRequestWithoutSettings_FullResponseObject() throws Exception {
    final ListEtfFlowsResponse expected = this.fillDefaultResponse();

    final ListEtfFlowsResponse actual = super.callUsecaseExpect200(ListEtfFlowsResponse.class);

    Assertions.assertEquals(expected, actual);

  }

  @Test
  public void test_standardRequestWithSettings_FullResponseObject() throws Exception {
    final ListEtfFlowsResponse expected = this.fillDefaultResponse();

    this.initEtfSettings();

    expected.setCalcEtfAskPrice(SETTING_SALE_ASK_PRICE);
    expected.setCalcEtfBidPrice(SETTING_SALE_BID_PRICE);
    expected.setCalcEtfSaleIsin(SETTING_ISIN);
    expected.setCalcEtfSalePieces(SETTING_SALE_PIECES);
    expected.setCalcEtfTransactionCosts(SETTING_SALE_TRANSACTION_COSTS);

    final ListEtfFlowsResponse actual = super.callUsecaseExpect200(ListEtfFlowsResponse.class);

    Assertions.assertEquals(expected, actual);

  }

  @Test
  public void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;

    super.callUsecaseExpect403();
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  public void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;

    final ListEtfFlowsResponse expected = new ListEtfFlowsResponse();
    final ListEtfFlowsResponse actual = super.callUsecaseExpect200(ListEtfFlowsResponse.class);
    Assertions.assertEquals(expected, actual);

  }
}