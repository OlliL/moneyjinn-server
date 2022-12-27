
package org.laladev.moneyjinn.server.controller.moneyflow;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.moneyflow.ShowEditMoneyflowResponse;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ContractpartnerTransport;
import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowSplitEntryTransport;
import org.laladev.moneyjinn.core.rest.model.transport.PostingAccountTransport;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.builder.MoneyflowSplitEntryTransportBuilder;
import org.laladev.moneyjinn.server.builder.MoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.PostingAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class ShowEditMoneyflowTest extends AbstractControllerTest {
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
  public void test_unknownMoneyflow_emptyResponseObject() throws Exception {
    final ShowEditMoneyflowResponse expected = new ShowEditMoneyflowResponse();
    final ShowEditMoneyflowResponse actual = super.callUsecaseWithoutContent(
        "/" + MoneyflowTransportBuilder.NON_EXISTING_ID, this.method, false,
        ShowEditMoneyflowResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_MoneyflowOwnedBySomeoneElse_emptyResponseObject() throws Exception {
    this.userName = UserTransportBuilder.USER3_NAME;
    this.userPassword = UserTransportBuilder.USER3_PASSWORD;
    final ShowEditMoneyflowResponse expected = new ShowEditMoneyflowResponse();
    final ShowEditMoneyflowResponse actual = super.callUsecaseWithoutContent(
        "/" + MoneyflowTransportBuilder.MONEYFLOW1_ID, this.method, false,
        ShowEditMoneyflowResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_Moneyflow1_completeResponseObject() throws Exception {
    final ShowEditMoneyflowResponse expected = new ShowEditMoneyflowResponse();
    expected.setMoneyflowTransport(new MoneyflowTransportBuilder().forMoneyflow1().build());
    final List<MoneyflowSplitEntryTransport> moneyflowSplitEntryTransports = new ArrayList<>();
    moneyflowSplitEntryTransports
        .add(new MoneyflowSplitEntryTransportBuilder().forMoneyflowSplitEntry1().build());
    moneyflowSplitEntryTransports
        .add(new MoneyflowSplitEntryTransportBuilder().forMoneyflowSplitEntry2().build());
    expected.setMoneyflowSplitEntryTransports(moneyflowSplitEntryTransports);
    final List<PostingAccountTransport> postingAccountTransports = new ArrayList<>();
    postingAccountTransports.add(new PostingAccountTransportBuilder().forPostingAccount1().build());
    postingAccountTransports.add(new PostingAccountTransportBuilder().forPostingAccount2().build());
    postingAccountTransports.add(new PostingAccountTransportBuilder().forPostingAccount3().build());
    expected.setPostingAccountTransports(postingAccountTransports);
    final List<ContractpartnerTransport> contractpartnerTransports = new ArrayList<>();
    contractpartnerTransports
        .add(new ContractpartnerTransportBuilder().forContractpartner1().build());
    contractpartnerTransports
        .add(new ContractpartnerTransportBuilder().forContractpartner2().build());
    contractpartnerTransports
        .add(new ContractpartnerTransportBuilder().forContractpartner3().build());
    contractpartnerTransports
        .add(new ContractpartnerTransportBuilder().forContractpartner4().build());
    expected.setContractpartnerTransports(contractpartnerTransports);
    final List<CapitalsourceTransport> capitalsourceTransports = new ArrayList<>();
    capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource1().build());
    capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource2().build());
    capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource3().build());
    capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource4().build());
    expected.setCapitalsourceTransports(capitalsourceTransports);
    expected.setHasReceipt(true);
    final ShowEditMoneyflowResponse actual = super.callUsecaseWithoutContent(
        "/" + MoneyflowTransportBuilder.MONEYFLOW1_ID, this.method, false,
        ShowEditMoneyflowResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;
    final ErrorResponse actual = super.callUsecaseWithoutContent("/1", this.method, false,
        ErrorResponse.class);

  }

  @Test
  @Sql("classpath:h2defaults.sql")
  public void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    final ShowEditMoneyflowResponse expected = new ShowEditMoneyflowResponse();
    final ShowEditMoneyflowResponse actual = super.callUsecaseWithoutContent(
        "/" + MoneyflowTransportBuilder.MONEYFLOW1_ID, this.method, false,
        ShowEditMoneyflowResponse.class);
    Assertions.assertEquals(expected, actual);
  }
}