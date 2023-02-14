
package org.laladev.moneyjinn.server.controller.contractpartneraccount;

import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.server.model.CreateContractpartnerAccountRequest;
import org.laladev.moneyjinn.server.model.CreateContractpartnerAccountResponse;
import org.laladev.moneyjinn.server.model.ContractpartnerAccountTransport;
import org.laladev.moneyjinn.server.model.ValidationItemTransport;
import org.laladev.moneyjinn.model.ContractpartnerAccount;
import org.laladev.moneyjinn.model.ContractpartnerAccountID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.server.builder.ContractpartnerAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.IContractpartnerAccountService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class CreateContractpartnerAccountTest extends AbstractControllerTest {
  @Inject
  private IContractpartnerAccountService contractpartnerAccountService;
  private final HttpMethod method = HttpMethod.POST;
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

  private void testError(final ContractpartnerAccountTransport transport, final ErrorCode errorCode)
      throws Exception {
    final CreateContractpartnerAccountRequest request = new CreateContractpartnerAccountRequest();
    request.setContractpartnerAccountTransport(transport);
    final List<ValidationItemTransport> validationItems = new ArrayList<>();
    validationItems.add(new ValidationItemTransportBuilder().withKey(null)
        .withError(errorCode.getErrorCode()).build());
    final CreateContractpartnerAccountResponse expected = new CreateContractpartnerAccountResponse();
    expected.setValidationItemTransports(validationItems);
    expected.setResult(Boolean.FALSE);
    final CreateContractpartnerAccountResponse actual = super.callUsecaseWithContent("",
        this.method, request, false, CreateContractpartnerAccountResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_ToLongAccountnumber_Error() throws Exception {
    final ContractpartnerAccountTransport transport = new ContractpartnerAccountTransportBuilder()
        .forNewContractpartnerAccount().build();
    transport.setAccountNumber("12345678901234567890123456789012345");
    this.testError(transport, ErrorCode.ACCOUNT_NUMBER_TO_LONG);
  }

  @Test
  public void test_ToLongBankcode_Error() throws Exception {
    final ContractpartnerAccountTransport transport = new ContractpartnerAccountTransportBuilder()
        .forNewContractpartnerAccount().build();
    transport.setBankCode("123456789012");
    this.testError(transport, ErrorCode.BANK_CODE_TO_LONG);
  }

  @Test
  public void test_AccountnumberInvalidChar_Error() throws Exception {
    final ContractpartnerAccountTransport transport = new ContractpartnerAccountTransportBuilder()
        .forNewContractpartnerAccount().build();
    transport.setAccountNumber("+");
    this.testError(transport, ErrorCode.ACCOUNT_NUMBER_CONTAINS_ILLEGAL_CHARS);
  }

  @Test
  public void test_BankcodeInvalidChar_Error() throws Exception {
    final ContractpartnerAccountTransport transport = new ContractpartnerAccountTransportBuilder()
        .forNewContractpartnerAccount().build();
    transport.setBankCode("+");
    this.testError(transport, ErrorCode.BANK_CODE_CONTAINS_ILLEGAL_CHARS);
  }

  @Test
  public void test_emptyContractpartner_Error() throws Exception {
    final ContractpartnerAccountTransport transport = new ContractpartnerAccountTransportBuilder()
        .forNewContractpartnerAccount().withContractpartnerid(null).build();
    this.testError(transport, ErrorCode.CONTRACTPARTNER_IS_NOT_SET);
  }

  @Test
  public void test_nonExistingContractpartner_Error() throws Exception {
    final ContractpartnerAccountTransport transport = new ContractpartnerAccountTransportBuilder()
        .forNewContractpartnerAccount()
        .withContractpartnerid(ContractpartnerTransportBuilder.NON_EXISTING_ID).build();
    this.testError(transport, ErrorCode.CONTRACTPARTNER_DOES_NOT_EXIST);
  }

  @Test
  public void test_standardRequest_SuccessfullNewIdReturned() throws Exception {
    final CreateContractpartnerAccountRequest request = new CreateContractpartnerAccountRequest();
    final ContractpartnerAccountTransport transport = new ContractpartnerAccountTransportBuilder()
        .forNewContractpartnerAccount().build();
    request.setContractpartnerAccountTransport(transport);
    final CreateContractpartnerAccountResponse actual = super.callUsecaseWithContent("",
        this.method, request, false, CreateContractpartnerAccountResponse.class);
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(
        ContractpartnerAccountTransportBuilder.NEXT_ID);
    final ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
        .getContractpartnerAccountById(userId, contractpartnerAccountId);
    Assertions.assertEquals(ContractpartnerAccountTransportBuilder.NEXT_ID,
        contractpartnerAccount.getId().getId());
    Assertions.assertEquals(ContractpartnerAccountTransportBuilder.NEXT_ID,
        actual.getContractpartnerAccountId());
  }

  @Test
  public void test_Bic8Digits_fillesUpTo11Digits() throws Exception {
    final CreateContractpartnerAccountRequest request = new CreateContractpartnerAccountRequest();
    final ContractpartnerAccountTransport transport = new ContractpartnerAccountTransportBuilder()
        .forNewContractpartnerAccount().build();
    transport.setBankCode("ABCDEFGH");
    request.setContractpartnerAccountTransport(transport);
    final CreateContractpartnerAccountResponse actual = super.callUsecaseWithContent("",
        this.method, request, false, CreateContractpartnerAccountResponse.class);
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(
        ContractpartnerAccountTransportBuilder.NEXT_ID);
    final ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountService
        .getContractpartnerAccountById(userId, contractpartnerAccountId);
    Assertions.assertEquals(ContractpartnerAccountTransportBuilder.NEXT_ID,
        contractpartnerAccount.getId().getId());
    Assertions.assertEquals(ContractpartnerAccountTransportBuilder.NEXT_ID,
        actual.getContractpartnerAccountId());
    Assertions.assertEquals(transport.getBankCode() + "XXX",
        contractpartnerAccount.getBankAccount().getBankCode());
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
    this.testError(transport, ErrorCode.CONTRACTPARTNER_DOES_NOT_EXIST);
  }
}