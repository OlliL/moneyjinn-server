
package org.laladev.moneyjinn.server.controller.contractpartneraccount;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.ContractpartnerAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.model.ContractpartnerAccountTransport;
import org.laladev.moneyjinn.server.model.ShowContractpartnerAccountListResponse;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class ShowContractpartnerAccountListTest extends AbstractControllerTest {
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

  private ShowContractpartnerAccountListResponse getCompleteResponse() {
    final ShowContractpartnerAccountListResponse expected = new ShowContractpartnerAccountListResponse();
    final List<ContractpartnerAccountTransport> contractpartnerAccountTransports = new ArrayList<>();
    contractpartnerAccountTransports
        .add(new ContractpartnerAccountTransportBuilder().forContractpartnerAccount1().build());
    contractpartnerAccountTransports
        .add(new ContractpartnerAccountTransportBuilder().forContractpartnerAccount2().build());
    expected.setContractpartnerAccountTransports(contractpartnerAccountTransports);
    return expected;
  }

  @Test
  public void test_default_FullResponseObject() throws Exception {
    final ShowContractpartnerAccountListResponse expected = this.getCompleteResponse();
    final ShowContractpartnerAccountListResponse actual = super.callUsecaseExpect200(
        "/" + ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID, this.method,
        ShowContractpartnerAccountListResponse.class);

    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_contractpartnerWithNoAccounts_responseWithNoAccounts() throws Exception {
    final ShowContractpartnerAccountListResponse expected = new ShowContractpartnerAccountListResponse();
    final ShowContractpartnerAccountListResponse actual = super.callUsecaseExpect200(
        "/" + ContractpartnerTransportBuilder.CONTRACTPARTNER2_ID, this.method,
        ShowContractpartnerAccountListResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_notExistingContractpartner_emptyResponseObject() throws Exception {
    final ShowContractpartnerAccountListResponse expected = new ShowContractpartnerAccountListResponse();
    final ShowContractpartnerAccountListResponse actual = super.callUsecaseExpect200(
        "/" + ContractpartnerTransportBuilder.NON_EXISTING_ID, this.method,
        ShowContractpartnerAccountListResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;

    super.callUsecaseExpect403("/" + ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID,
        this.method);
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  public void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    final ShowContractpartnerAccountListResponse expected = new ShowContractpartnerAccountListResponse();

    final ShowContractpartnerAccountListResponse actual = super.callUsecaseExpect200(
        "/" + ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID, this.method,
        ShowContractpartnerAccountListResponse.class);

    Assertions.assertEquals(expected, actual);
  }
}
