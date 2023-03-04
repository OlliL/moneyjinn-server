
package org.laladev.moneyjinn.server.controller.contractpartneraccount;

import jakarta.inject.Inject;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.ContractpartnerAccount;
import org.laladev.moneyjinn.model.ContractpartnerAccountID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.server.builder.ContractpartnerAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.controller.api.ContractpartnerAccountControllerApi;
import org.laladev.moneyjinn.service.api.IContractpartnerAccountService;
import org.springframework.test.context.jdbc.Sql;

public class DeleteContractpartnerAccountTest extends AbstractControllerTest {
  @Inject
  private IContractpartnerAccountService contractpartnerAccountService;

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
    return super.getMethodFromTestClassName(ContractpartnerAccountControllerApi.class,
        this.getClass());
  }

  @Test
  public void test_regularContractpartnerAccountNoData_SuccessfullNoContent() throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(
        ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT2_ID);
    ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
        .getContractpartnerAccountById(userId, contractpartnerAccountId);
    Assertions.assertNotNull(contractpartnerAccount);

    super.callUsecaseExpect204WithUriVariables(
        ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT2_ID);

    contractpartnerAccount = this.contractpartnerAccountService
        .getContractpartnerAccountById(userId, contractpartnerAccountId);
    Assertions.assertNull(contractpartnerAccount);
  }

  @Test
  public void test_nonExistingContractpartnerAccount_SuccessfullNoContent() throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(
        ContractpartnerAccountTransportBuilder.NON_EXISTING_ID);
    ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
        .getContractpartnerAccountById(userId, contractpartnerAccountId);
    Assertions.assertNull(contractpartnerAccount);

    super.callUsecaseExpect204WithUriVariables(
        ContractpartnerAccountTransportBuilder.NON_EXISTING_ID);

    contractpartnerAccount = this.contractpartnerAccountService
        .getContractpartnerAccountById(userId, contractpartnerAccountId);
    Assertions.assertNull(contractpartnerAccount);
  }

  @Test
  public void test_ContractpartnerAccountFromSameGroupButNotMe_SuccessfullNoContent()
      throws Exception {
    this.userName = UserTransportBuilder.USER3_NAME;
    this.userPassword = UserTransportBuilder.USER3_PASSWORD;
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(
        ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID);
    // Uncommented because of Cache eviction does not work right now for differing users
    // ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
    // .getContractpartnerAccountById(userId, contractpartnerAccountId);
    //
    // Assertions.assertNotNull(contractpartnerAccount);

    super.callUsecaseExpect204WithUriVariables(
        ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID);

    final ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
        .getContractpartnerAccountById(userId, contractpartnerAccountId);
    Assertions.assertNull(contractpartnerAccount);
  }

  @Test
  public void test_ContractpartnerAccountFromDifferentGroup_notSuccessfull() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(
        ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID);
    // Uncommented because of Cache eviction does not work right now for differing users
    // ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
    // .getContractpartnerAccountById(userId, contractpartnerAccountId);
    //
    // Assertions.assertNotNull(contractpartnerAccount);

    super.callUsecaseExpect204WithUriVariables(
        ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID);

    final ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
        .getContractpartnerAccountById(userId, contractpartnerAccountId);
    Assertions.assertNotNull(contractpartnerAccount);
  }

  @Test
  public void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;

    super.callUsecaseExpect403WithUriVariables(
        ContractpartnerAccountTransportBuilder.NON_EXISTING_ID);
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  public void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;

    super.callUsecaseExpect204WithUriVariables(
        ContractpartnerAccountTransportBuilder.NON_EXISTING_ID);
  }
}