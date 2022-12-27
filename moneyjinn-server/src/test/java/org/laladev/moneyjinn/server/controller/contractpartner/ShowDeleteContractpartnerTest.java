
package org.laladev.moneyjinn.server.controller.contractpartner;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.contractpartner.ShowDeleteContractpartnerResponse;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class ShowDeleteContractpartnerTest extends AbstractControllerTest {
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
  public void test_unknownContractpartner_emptyResponseObject() throws Exception {
    final ShowDeleteContractpartnerResponse expected = new ShowDeleteContractpartnerResponse();
    final ShowDeleteContractpartnerResponse actual = super.callUsecaseWithoutContent(
        "/" + ContractpartnerTransportBuilder.NON_EXISTING_ID, this.method, false,
        ShowDeleteContractpartnerResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_Contractpartner1_completeResponseObject() throws Exception {
    final ShowDeleteContractpartnerResponse expected = new ShowDeleteContractpartnerResponse();
    expected.setContractpartnerTransport(
        new ContractpartnerTransportBuilder().forContractpartner1().build());
    final ShowDeleteContractpartnerResponse actual = super.callUsecaseWithoutContent(
        "/" + ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID, this.method, false,
        ShowDeleteContractpartnerResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_Contractpartner1AsDifferingUserSameGroup_completeResponseObject()
      throws Exception {
    this.userName = UserTransportBuilder.USER3_NAME;
    this.userPassword = UserTransportBuilder.USER3_PASSWORD;
    final ShowDeleteContractpartnerResponse expected = new ShowDeleteContractpartnerResponse();
    expected.setContractpartnerTransport(
        new ContractpartnerTransportBuilder().forContractpartner1().build());
    final ShowDeleteContractpartnerResponse actual = super.callUsecaseWithoutContent(
        "/" + ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID, this.method, false,
        ShowDeleteContractpartnerResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_Contractpartner1AsDifferingUserOtherGroup_emptyResponseObject()
      throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    final ShowDeleteContractpartnerResponse expected = new ShowDeleteContractpartnerResponse();
    final ShowDeleteContractpartnerResponse actual = super.callUsecaseWithoutContent(
        "/" + ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID, this.method, false,
        ShowDeleteContractpartnerResponse.class);
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
    final ShowDeleteContractpartnerResponse expected = new ShowDeleteContractpartnerResponse();
    final ShowDeleteContractpartnerResponse actual = super.callUsecaseWithoutContent(
        "/" + ContractpartnerTransportBuilder.NON_EXISTING_ID, this.method, false,
        ShowDeleteContractpartnerResponse.class);
    Assertions.assertEquals(expected, actual);
  }
}