
package org.laladev.moneyjinn.server.controller.predefmoneyflow;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.rest.model.predefmoneyflow.ShowEditPreDefMoneyflowResponse;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ContractpartnerTransport;
import org.laladev.moneyjinn.core.rest.model.transport.PostingAccountTransport;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.builder.PostingAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.PreDefMoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class ShowEditPreDefMoneyflowTest extends AbstractControllerTest {
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
  public void test_unknownPreDefMoneyflow_emptyResponseObject() throws Exception {
    final ShowEditPreDefMoneyflowResponse expected = new ShowEditPreDefMoneyflowResponse();
    final ShowEditPreDefMoneyflowResponse actual = super.callUsecaseWithoutContent(
        "/" + PreDefMoneyflowTransportBuilder.NON_EXISTING_ID, this.method, false,
        ShowEditPreDefMoneyflowResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_PreDefMoneyflowOwnedBySomeoneElse_emptyResponseObject() throws Exception {
    this.userName = UserTransportBuilder.USER3_NAME;
    this.userPassword = UserTransportBuilder.USER3_PASSWORD;
    final ShowEditPreDefMoneyflowResponse expected = new ShowEditPreDefMoneyflowResponse();
    final ShowEditPreDefMoneyflowResponse actual = super.callUsecaseWithoutContent(
        "/" + PreDefMoneyflowTransportBuilder.PRE_DEF_MONEYFLOW1_ID, this.method, false,
        ShowEditPreDefMoneyflowResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_PreDefMoneyflow1_completeResponseObject() throws Exception {
    final ShowEditPreDefMoneyflowResponse expected = new ShowEditPreDefMoneyflowResponse();
    expected.setPreDefMoneyflowTransport(
        new PreDefMoneyflowTransportBuilder().forPreDefMoneyflow1().build());
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
    expected.setContractpartnerTransports(contractpartnerTransports);
    final List<CapitalsourceTransport> capitalsourceTransports = new ArrayList<>();
    capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource1().build());
    capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource2().build());
    expected.setCapitalsourceTransports(capitalsourceTransports);
    final ShowEditPreDefMoneyflowResponse actual = super.callUsecaseWithoutContent(
        "/" + PreDefMoneyflowTransportBuilder.PRE_DEF_MONEYFLOW1_ID, this.method, false,
        ShowEditPreDefMoneyflowResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;
    super.callUsecaseExpect403("/1", this.method);
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  public void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    final ShowEditPreDefMoneyflowResponse expected = new ShowEditPreDefMoneyflowResponse();
    final ShowEditPreDefMoneyflowResponse actual = super.callUsecaseWithoutContent(
        "/" + PreDefMoneyflowTransportBuilder.PRE_DEF_MONEYFLOW1_ID, this.method, false,
        ShowEditPreDefMoneyflowResponse.class);
    Assertions.assertEquals(expected, actual);
  }
}