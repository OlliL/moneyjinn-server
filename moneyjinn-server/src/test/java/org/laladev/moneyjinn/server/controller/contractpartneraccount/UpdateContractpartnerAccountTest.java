
package org.laladev.moneyjinn.server.controller.contractpartneraccount;

import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.server.model.ValidationResponse;
import org.laladev.moneyjinn.server.model.UpdateContractpartnerAccountRequest;
import org.laladev.moneyjinn.server.model.ContractpartnerAccountTransport;
import org.laladev.moneyjinn.server.model.ValidationItemTransport;
import org.laladev.moneyjinn.model.ContractpartnerAccount;
import org.laladev.moneyjinn.model.ContractpartnerAccountID;
import org.laladev.moneyjinn.model.ContractpartnerID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.server.builder.ContractpartnerAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.IContractpartnerAccountService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class UpdateContractpartnerAccountTest extends AbstractControllerTest {
  @Inject
  private IContractpartnerAccountService contractpartnerAccountService;
  private final HttpMethod method = HttpMethod.PUT;
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

  private void testError(final ContractpartnerAccountTransport transport,
      final String contractpartnerName, final ErrorCode... errorCodes) throws Exception {
    final UpdateContractpartnerAccountRequest request = new UpdateContractpartnerAccountRequest();
    request.setContractpartnerAccountTransport(transport);
    final List<ValidationItemTransport> validationItems = new ArrayList<>();
    for (final ErrorCode errorCode : errorCodes) {
      final ValidationItemTransportBuilder builder = new ValidationItemTransportBuilder()
          .withKey(transport.getId().toString()).withError(errorCode.getErrorCode());
      if (contractpartnerName != null) {
        builder.withVariableArray(Arrays.asList(contractpartnerName));
      }
      validationItems.add(builder.build());
    }
    final ValidationResponse expected = new ValidationResponse();
    expected.setValidationItemTransports(validationItems);
    expected.setResult(Boolean.FALSE);
    final ValidationResponse actual = super.callUsecaseWithContent("", this.method, request, false,
        ValidationResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_ContractpartnerAccountNullBankaccount_Error() throws Exception {
    final ContractpartnerAccountTransport transport = new ContractpartnerAccountTransportBuilder()
        .forContractpartnerAccount2().build();
    transport.setAccountNumber(null);
    transport.setBankCode(null);
    this.testError(transport, null, ErrorCode.BANK_CODE_CONTAINS_ILLEGAL_CHARS_OR_IS_EMPTY,
        ErrorCode.ACCOUNT_NUMBER_CONTAINS_ILLEGAL_CHARS_OR_IS_EMPTY);
  }

  @Test
  public void test_ContractpartnerAccountAlreadyExisting_Error() throws Exception {
    final ContractpartnerAccountTransport transport1 = new ContractpartnerAccountTransportBuilder()
        .forContractpartnerAccount1().build();
    final ContractpartnerAccountTransport transport2 = new ContractpartnerAccountTransportBuilder()
        .forContractpartnerAccount2().build();
    transport2.setAccountNumber(transport1.getAccountNumber());
    transport2.setBankCode(transport1.getBankCode());
    this.testError(transport2, ContractpartnerTransportBuilder.CONTRACTPARTNER1_NAME,
        ErrorCode.ACCOUNT_ALREADY_ASSIGNED_TO_OTHER_PARTNER);
  }

  @Test
  public void test_AccountNumberAlreadyUsedButNotBankCode_Successfull() throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(
        ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT2_ID);
    final ContractpartnerAccountTransport transport1 = new ContractpartnerAccountTransportBuilder()
        .forContractpartnerAccount1().build();
    final ContractpartnerAccountTransport transport2 = new ContractpartnerAccountTransportBuilder()
        .forContractpartnerAccount2().build();
    transport2.setAccountNumber(transport1.getAccountNumber());
    final UpdateContractpartnerAccountRequest request = new UpdateContractpartnerAccountRequest();
    request.setContractpartnerAccountTransport(transport2);
    final ValidationResponse actual = super.callUsecaseWithContent("", this.method, request, false,
        ValidationResponse.class);
    Assertions.assertTrue(actual.getResult());
    final ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
        .getContractpartnerAccountById(userId, contractpartnerAccountId);
    Assertions.assertEquals(ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT2_ID,
        contractpartnerAccount.getId().getId());
    Assertions.assertEquals(transport2.getAccountNumber(),
        contractpartnerAccount.getBankAccount().getAccountNumber());
    Assertions.assertEquals(transport2.getBankCode(),
        contractpartnerAccount.getBankAccount().getBankCode());
  }

  @Test
  public void test_BankCodeAlreadyUsedButNotAccountNumber_Successfull() throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(
        ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT2_ID);
    final ContractpartnerAccountTransport transport1 = new ContractpartnerAccountTransportBuilder()
        .forContractpartnerAccount1().build();
    final ContractpartnerAccountTransport transport2 = new ContractpartnerAccountTransportBuilder()
        .forContractpartnerAccount2().build();
    transport2.setBankCode(transport1.getBankCode());
    final UpdateContractpartnerAccountRequest request = new UpdateContractpartnerAccountRequest();
    request.setContractpartnerAccountTransport(transport2);
    final ValidationResponse actual = super.callUsecaseWithContent("", this.method, request, false,
        ValidationResponse.class);
    Assertions.assertTrue(actual.getResult());
    final ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
        .getContractpartnerAccountById(userId, contractpartnerAccountId);
    Assertions.assertEquals(ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT2_ID,
        contractpartnerAccount.getId().getId());
    Assertions.assertEquals(transport2.getAccountNumber(),
        contractpartnerAccount.getBankAccount().getAccountNumber());
    Assertions.assertEquals(transport2.getBankCode(),
        contractpartnerAccount.getBankAccount().getBankCode());
  }

  @Test
  public void test_standardRequest_Successfull() throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(
        ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID);
    final UpdateContractpartnerAccountRequest request = new UpdateContractpartnerAccountRequest();
    final ContractpartnerAccountTransport transport = new ContractpartnerAccountTransportBuilder()
        .forContractpartnerAccount1().build();
    transport.setAccountNumber("1");
    transport.setBankCode("2");
    request.setContractpartnerAccountTransport(transport);
    final ValidationResponse actual = super.callUsecaseWithContent("", this.method, request, false,
        ValidationResponse.class);
    Assertions.assertTrue(actual.getResult());
    final ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
        .getContractpartnerAccountById(userId, contractpartnerAccountId);
    Assertions.assertEquals(ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID,
        contractpartnerAccount.getId().getId());
    Assertions.assertEquals("1", contractpartnerAccount.getBankAccount().getAccountNumber());
    Assertions.assertEquals("2", contractpartnerAccount.getBankAccount().getBankCode());
  }

  @Test
  public void test_Bic8Digits_fillesUpTo11Digits() throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(
        ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID);
    final UpdateContractpartnerAccountRequest request = new UpdateContractpartnerAccountRequest();
    final ContractpartnerAccountTransport transport = new ContractpartnerAccountTransportBuilder()
        .forContractpartnerAccount1().build();
    transport.setAccountNumber("1");
    transport.setBankCode("ABCDEFGH");
    request.setContractpartnerAccountTransport(transport);
    final ValidationResponse actual = super.callUsecaseWithContent("", this.method, request, false,
        ValidationResponse.class);
    Assertions.assertTrue(actual.getResult());
    final ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
        .getContractpartnerAccountById(userId, contractpartnerAccountId);
    Assertions.assertEquals(ContractpartnerAccountTransportBuilder.CONTRACTPARTNER_ACCOUNT1_ID,
        contractpartnerAccount.getId().getId());
    Assertions.assertEquals("1", contractpartnerAccount.getBankAccount().getAccountNumber());
    Assertions.assertEquals(transport.getBankCode() + "XXX",
        contractpartnerAccount.getBankAccount().getBankCode());
  }

  @Test
  public void test_standardRequestChangingContractpartner_SuccessfullNoContent() throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final ContractpartnerID contractpartner1Id = new ContractpartnerID(
        ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);
    final ContractpartnerID contractpartner2Id = new ContractpartnerID(
        ContractpartnerTransportBuilder.CONTRACTPARTNER2_ID);
    final UpdateContractpartnerAccountRequest request = new UpdateContractpartnerAccountRequest();
    final ContractpartnerAccountTransport transport = new ContractpartnerAccountTransportBuilder()
        .forContractpartnerAccount1().build();
    transport.setContractpartnerid(ContractpartnerTransportBuilder.CONTRACTPARTNER2_ID);
    request.setContractpartnerAccountTransport(transport);
    List<ContractpartnerAccount> contractpartner1Accounts = this.contractpartnerAccountService
        .getContractpartnerAccounts(userId, contractpartner1Id);
    List<ContractpartnerAccount> contractpartner2Accounts = this.contractpartnerAccountService
        .getContractpartnerAccounts(userId, contractpartner2Id);
    Assertions.assertEquals(2, contractpartner1Accounts.size());
    Assertions.assertTrue(contractpartner2Accounts.isEmpty());
    final ValidationResponse actual = super.callUsecaseWithContent("", this.method, request, false,
        ValidationResponse.class);
    Assertions.assertTrue(actual.getResult());
    contractpartner1Accounts = this.contractpartnerAccountService.getContractpartnerAccounts(userId,
        contractpartner1Id);
    contractpartner2Accounts = this.contractpartnerAccountService.getContractpartnerAccounts(userId,
        contractpartner2Id);
    Assertions.assertEquals(1, contractpartner1Accounts.size());
    Assertions.assertEquals(1, contractpartner2Accounts.size());
  }

  @Test
  public void test_editContractpartnerAccountOwnedBySomeoneElse_notSuccessfull() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    final ContractpartnerAccountTransport transport = new ContractpartnerAccountTransportBuilder()
        .forContractpartnerAccount1().build();
    transport.setAccountNumber("1");
    transport.setBankCode("2");
    this.testError(transport, null, ErrorCode.CONTRACTPARTNER_DOES_NOT_EXIST);
  }

  @Test
  public void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;
    super.callUsecaseExpect403("", this.method);
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  public void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    final ContractpartnerAccountTransport transport = new ContractpartnerAccountTransportBuilder()
        .forNewContractpartnerAccount().build();
    this.testError(transport, null, ErrorCode.CONTRACTPARTNER_DOES_NOT_EXIST);
  }
}