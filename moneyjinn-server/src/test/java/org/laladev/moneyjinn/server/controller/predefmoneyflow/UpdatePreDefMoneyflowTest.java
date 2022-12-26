
package org.laladev.moneyjinn.server.controller.predefmoneyflow;

import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import org.laladev.moneyjinn.core.rest.model.predefmoneyflow.UpdatePreDefMoneyflowRequest;
import org.laladev.moneyjinn.core.rest.model.transport.PreDefMoneyflowTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.model.PreDefMoneyflow;
import org.laladev.moneyjinn.model.PreDefMoneyflowID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.ContractpartnerTransportBuilder;
import org.laladev.moneyjinn.server.builder.PostingAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.PreDefMoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.IPreDefMoneyflowService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class UpdatePreDefMoneyflowTest extends AbstractControllerTest {
  @Inject
  private IPreDefMoneyflowService preDefMoneyflowService;
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

  private void testError(final PreDefMoneyflowTransport transport, final ErrorCode errorCode)
      throws Exception {
    final UpdatePreDefMoneyflowRequest request = new UpdatePreDefMoneyflowRequest();
    request.setPreDefMoneyflowTransport(transport);
    final List<ValidationItemTransport> validationItems = new ArrayList<>();
    validationItems.add(new ValidationItemTransportBuilder().withKey(transport.getId().intValue())
        .withError(errorCode.getErrorCode()).build());
    final ValidationResponse expected = new ValidationResponse();
    expected.setValidationItemTransports(validationItems);
    expected.setResult(Boolean.FALSE);
    final ValidationResponse actual = super.callUsecaseWithContent("", this.method, request, false,
        ValidationResponse.class);
    Assertions.assertEquals(expected.getErrorResponse(), actual.getErrorResponse());
    Assertions.assertEquals(expected.getResult(), actual.getResult());
    Assertions.assertEquals(expected.getValidationItemTransports(),
        actual.getValidationItemTransports());
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_emptyComment_Error() throws Exception {
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forPreDefMoneyflow1().build();
    transport.setComment("");
    this.testError(transport, ErrorCode.COMMENT_IS_NOT_SET);
  }

  @Test
  public void test_nullComment_Error() throws Exception {
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forPreDefMoneyflow1().build();
    transport.setComment(null);
    this.testError(transport, ErrorCode.COMMENT_IS_NOT_SET);
  }

  @Test
  public void test_nullCapitalsource_Error() throws Exception {
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forPreDefMoneyflow1().build();
    transport.setCapitalsourceid(null);
    this.testError(transport, ErrorCode.CAPITALSOURCE_IS_NOT_SET);
  }

  @Test
  public void test_notExistingCapitalsource_Error() throws Exception {
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forPreDefMoneyflow1().build();
    transport.setCapitalsourceid(CapitalsourceTransportBuilder.NON_EXISTING_ID);
    this.testError(transport, ErrorCode.CAPITALSOURCE_DOES_NOT_EXIST);
  }

  @Test
  public void test_creditCapitalsource_Error() throws Exception {
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forPreDefMoneyflow1().build();
    transport.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE5_ID);
    this.testError(transport, ErrorCode.CAPITALSOURCE_INVALID);
  }

  @Test
  public void test_AmountToBig_Error() throws Exception {
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forPreDefMoneyflow1().build();
    transport.setAmount(new BigDecimal(9999999));
    this.testError(transport, ErrorCode.AMOUNT_TO_BIG);
  }

  @Test
  public void test_noLongerValidCapitalsource_Error() throws Exception {
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forPreDefMoneyflow1().build();
    transport.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE3_ID);
    this.testError(transport, ErrorCode.CAPITALSOURCE_USE_OUT_OF_VALIDITY);
  }

  @Test
  public void test_nullContractpartner_Error() throws Exception {
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forPreDefMoneyflow1().build();
    transport.setContractpartnerid(null);
    this.testError(transport, ErrorCode.CONTRACTPARTNER_IS_NOT_SET);
  }

  @Test
  public void test_notExistingContractpartner_Error() throws Exception {
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forPreDefMoneyflow1().build();
    transport.setContractpartnerid(ContractpartnerTransportBuilder.NON_EXISTING_ID);
    this.testError(transport, ErrorCode.CONTRACTPARTNER_DOES_NOT_EXIST);
  }

  @Test
  public void test_noLongerValidContractpartner_Error() throws Exception {
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forPreDefMoneyflow1().build();
    transport.setContractpartnerid(ContractpartnerTransportBuilder.CONTRACTPARTNER3_ID);
    this.testError(transport, ErrorCode.CONTRACTPARTNER_NO_LONGER_VALID);
  }

  @Test
  public void test_nullAmount_Error() throws Exception {
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forPreDefMoneyflow1().build();
    transport.setAmount(null);
    this.testError(transport, ErrorCode.AMOUNT_IS_ZERO);
  }

  @Test
  public void test_zeroAmount_Error() throws Exception {
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forPreDefMoneyflow1().build();
    transport.setAmount(BigDecimal.ZERO);
    this.testError(transport, ErrorCode.AMOUNT_IS_ZERO);
  }

  // make sure it 0 is compared with compareTo not with equals
  @Test
  public void test_0_00Amount_Error() throws Exception {
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forPreDefMoneyflow1().build();
    transport.setAmount(new BigDecimal("0.00000"));
    this.testError(transport, ErrorCode.AMOUNT_IS_ZERO);
  }

  @Test
  public void test_nullPostingAccount_Error() throws Exception {
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forPreDefMoneyflow1().build();
    transport.setPostingaccountid(null);
    this.testError(transport, ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED);
  }

  @Test
  public void test_notExistingPostingAccount_Error() throws Exception {
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forPreDefMoneyflow1().build();
    transport.setPostingaccountid(PostingAccountTransportBuilder.NON_EXISTING_ID);
    this.testError(transport, ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED);
  }

  @Test
  public void test_notExisting_NothingHappend() throws Exception {
    final UpdatePreDefMoneyflowRequest request = new UpdatePreDefMoneyflowRequest();
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forPreDefMoneyflow1().build();
    request.setPreDefMoneyflowTransport(transport);
    super.callUsecaseWithContent("", this.method, request, true, Object.class);
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final PreDefMoneyflowID preDefMoneyflowId = new PreDefMoneyflowID(
        PreDefMoneyflowTransportBuilder.NEXT_ID);
    final PreDefMoneyflow preDefMoneyflow = this.preDefMoneyflowService
        .getPreDefMoneyflowById(userId, preDefMoneyflowId);
    Assertions.assertNull(preDefMoneyflow);
  }

  @Test
  public void test_existing_UpdateDone() throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final PreDefMoneyflowID preDefMoneyflowId = new PreDefMoneyflowID(
        PreDefMoneyflowTransportBuilder.PRE_DEF_MONEYFLOW1_ID);
    final UpdatePreDefMoneyflowRequest request = new UpdatePreDefMoneyflowRequest();
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forPreDefMoneyflow1().build();
    PreDefMoneyflow preDefMoneyflow = this.preDefMoneyflowService.getPreDefMoneyflowById(userId,
        preDefMoneyflowId);
    Assertions.assertNotNull(preDefMoneyflow);
    Assertions.assertEquals(transport.getAmount(), preDefMoneyflow.getAmount());
    Assertions.assertEquals(transport.getCapitalsourceid(),
        preDefMoneyflow.getCapitalsource().getId().getId());
    Assertions.assertEquals(transport.getComment(), preDefMoneyflow.getComment());
    Assertions.assertEquals(transport.getContractpartnerid(),
        preDefMoneyflow.getContractpartner().getId().getId());
    Assertions.assertEquals(Short.valueOf("1").equals(transport.getOnceAMonth()),
        preDefMoneyflow.isOnceAMonth());
    Assertions.assertEquals(transport.getPostingaccountid(),
        preDefMoneyflow.getPostingAccount().getId().getId());
    transport.setAmount(BigDecimal.valueOf(1020, 2));
    transport.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE2_ID);
    transport.setComment("hugo");
    transport.setContractpartnerid(ContractpartnerTransportBuilder.CONTRACTPARTNER2_ID);
    transport.setOnceAMonth((short) 0);
    transport.setPostingaccountid(PostingAccountTransportBuilder.POSTING_ACCOUNT2_ID);
    request.setPreDefMoneyflowTransport(transport);
    super.callUsecaseWithContent("", this.method, request, true, Object.class);
    preDefMoneyflow = this.preDefMoneyflowService.getPreDefMoneyflowById(userId, preDefMoneyflowId);
    Assertions.assertNotNull(preDefMoneyflow);
    Assertions.assertEquals(transport.getAmount(), preDefMoneyflow.getAmount());
    Assertions.assertEquals(transport.getCapitalsourceid(),
        preDefMoneyflow.getCapitalsource().getId().getId());
    Assertions.assertEquals(transport.getComment(), preDefMoneyflow.getComment());
    Assertions.assertEquals(transport.getContractpartnerid(),
        preDefMoneyflow.getContractpartner().getId().getId());
    Assertions.assertEquals(Short.valueOf("1").equals(transport.getOnceAMonth()),
        preDefMoneyflow.isOnceAMonth());
    Assertions.assertEquals(transport.getPostingaccountid(),
        preDefMoneyflow.getPostingAccount().getId().getId());
  }

  @Test
  public void test_NotGroupUseableCapitalsourceUsed_Error() throws Exception {
    this.userName = UserTransportBuilder.USER3_NAME;
    this.userPassword = UserTransportBuilder.USER3_PASSWORD;
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forPreDefMoneyflow2().build();
    transport.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID);
    transport.setContractpartnerid(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);
    this.testError(transport, ErrorCode.CAPITALSOURCE_DOES_NOT_EXIST);
  }

  @Test
  public void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;
    final ErrorResponse actual = super.callUsecaseWithoutContent("", this.method, false,
        ErrorResponse.class);
    Assertions.assertEquals(super.accessDeniedErrorResponse(), actual);
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  public void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    final UpdatePreDefMoneyflowRequest request = new UpdatePreDefMoneyflowRequest();
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forPreDefMoneyflow1().build();
    request.setPreDefMoneyflowTransport(transport);
    super.callUsecaseWithContent("", this.method, request, false, ValidationResponse.class);
  }
}