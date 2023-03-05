
package org.laladev.moneyjinn.server.controller.predefmoneyflow;

import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
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
import org.laladev.moneyjinn.server.controller.api.PreDefMoneyflowControllerApi;
import org.laladev.moneyjinn.server.model.CreatePreDefMoneyflowRequest;
import org.laladev.moneyjinn.server.model.CreatePreDefMoneyflowResponse;
import org.laladev.moneyjinn.server.model.PreDefMoneyflowTransport;
import org.laladev.moneyjinn.server.model.ValidationItemTransport;
import org.laladev.moneyjinn.server.model.ValidationResponse;
import org.laladev.moneyjinn.service.api.IPreDefMoneyflowService;
import org.springframework.test.context.jdbc.Sql;

class CreatePreDefMoneyflowTest extends AbstractControllerTest {
  @Inject
  private IPreDefMoneyflowService preDefMoneyflowService;

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
  protected void loadMethod() {
    super.getMock(PreDefMoneyflowControllerApi.class).createPreDefMoneyflow(null);
  }

  private void testError(final PreDefMoneyflowTransport transport, final ErrorCode errorCode)
      throws Exception {
    final CreatePreDefMoneyflowRequest request = new CreatePreDefMoneyflowRequest();
    request.setPreDefMoneyflowTransport(transport);
    final List<ValidationItemTransport> validationItems = new ArrayList<>();
    validationItems.add(new ValidationItemTransportBuilder().withKey(null)
        .withError(errorCode.getErrorCode()).build());
    final ValidationResponse expected = new ValidationResponse();
    expected.setValidationItemTransports(validationItems);
    expected.setResult(Boolean.FALSE);
    final ValidationResponse actual = super.callUsecaseExpect422(request, ValidationResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
   void test_emptyComment_Error() throws Exception {
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forNewPreDefMoneyflow().build();
    transport.setComment("");
    this.testError(transport, ErrorCode.COMMENT_IS_NOT_SET);
  }

  @Test
   void test_nullComment_Error() throws Exception {
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forNewPreDefMoneyflow().build();
    transport.setComment(null);
    this.testError(transport, ErrorCode.COMMENT_IS_NOT_SET);
  }

  @Test
   void test_nullCapitalsource_Error() throws Exception {
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forNewPreDefMoneyflow().build();
    transport.setCapitalsourceid(null);
    this.testError(transport, ErrorCode.CAPITALSOURCE_IS_NOT_SET);
  }

  @Test
   void test_notExistingCapitalsource_Error() throws Exception {
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forNewPreDefMoneyflow().build();
    transport.setCapitalsourceid(CapitalsourceTransportBuilder.NON_EXISTING_ID);
    this.testError(transport, ErrorCode.CAPITALSOURCE_DOES_NOT_EXIST);
  }

  @Test
   void test_creditCapitalsource_Error() throws Exception {
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forNewPreDefMoneyflow().build();
    transport.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE5_ID);
    this.testError(transport, ErrorCode.CAPITALSOURCE_INVALID);
  }

  @Test
   void test_AmountToBig_Error() throws Exception {
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forNewPreDefMoneyflow().build();
    transport.setAmount(new BigDecimal(9999999));
    this.testError(transport, ErrorCode.AMOUNT_TO_BIG);
  }

  @Test
   void test_noLongerValidCapitalsource_Error() throws Exception {
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forNewPreDefMoneyflow().build();
    transport.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE3_ID);
    this.testError(transport, ErrorCode.CAPITALSOURCE_USE_OUT_OF_VALIDITY);
  }

  @Test
   void test_nullContractpartner_Error() throws Exception {
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forNewPreDefMoneyflow().build();
    transport.setContractpartnerid(null);
    this.testError(transport, ErrorCode.CONTRACTPARTNER_IS_NOT_SET);
  }

  @Test
   void test_notExistingContractpartner_Error() throws Exception {
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forNewPreDefMoneyflow().build();
    transport.setContractpartnerid(ContractpartnerTransportBuilder.NON_EXISTING_ID);
    this.testError(transport, ErrorCode.CONTRACTPARTNER_DOES_NOT_EXIST);
  }

  @Test
   void test_noLongerValidContractpartner_Error() throws Exception {
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forNewPreDefMoneyflow().build();
    transport.setContractpartnerid(ContractpartnerTransportBuilder.CONTRACTPARTNER3_ID);
    this.testError(transport, ErrorCode.CONTRACTPARTNER_NO_LONGER_VALID);
  }

  @Test
   void test_nullAmount_Error() throws Exception {
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forNewPreDefMoneyflow().build();
    transport.setAmount(null);
    this.testError(transport, ErrorCode.AMOUNT_IS_ZERO);
  }

  @Test
   void test_zeroAmount_Error() throws Exception {
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forNewPreDefMoneyflow().build();
    transport.setAmount(BigDecimal.ZERO);
    this.testError(transport, ErrorCode.AMOUNT_IS_ZERO);
  }

  // make sure it 0 is compared with compareTo not with equals
  @Test
   void test_0_00Amount_Error() throws Exception {
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forNewPreDefMoneyflow().build();
    transport.setAmount(new BigDecimal("0.00000"));
    this.testError(transport, ErrorCode.AMOUNT_IS_ZERO);
  }

  @Test
   void test_nullPostingAccount_Error() throws Exception {
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forNewPreDefMoneyflow().build();
    transport.setPostingaccountid(null);
    this.testError(transport, ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED);
  }

  @Test
   void test_notExistingPostingAccount_Error() throws Exception {
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forNewPreDefMoneyflow().build();
    transport.setPostingaccountid(PostingAccountTransportBuilder.NON_EXISTING_ID);
    this.testError(transport, ErrorCode.POSTING_ACCOUNT_NOT_SPECIFIED);
  }

  @Test
   void test_standardRequest_SuccessfullNoContent() throws Exception {
    final CreatePreDefMoneyflowRequest request = new CreatePreDefMoneyflowRequest();
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forNewPreDefMoneyflow().build();
    request.setPreDefMoneyflowTransport(transport);
    final CreatePreDefMoneyflowResponse response = super.callUsecaseExpect200(request,
        CreatePreDefMoneyflowResponse.class);
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final PreDefMoneyflowID preDefMoneyflowId = new PreDefMoneyflowID(
        PreDefMoneyflowTransportBuilder.NEXT_ID);
    final PreDefMoneyflow preDefMoneyflow = this.preDefMoneyflowService
        .getPreDefMoneyflowById(userId, preDefMoneyflowId);
    Assertions.assertEquals(PreDefMoneyflowTransportBuilder.NEXT_ID,
        preDefMoneyflow.getId().getId());
    Assertions.assertEquals(PreDefMoneyflowTransportBuilder.NEWPRE_DEF_MONEYFLOW_COMMENT,
        preDefMoneyflow.getComment());
    Assertions.assertEquals(PreDefMoneyflowTransportBuilder.NEXT_ID,
        response.getPreDefMoneyflowId());
  }

  @Test
   void test_differentUserIdSet_ButIgnoredAndAlwaysCreatedWithOwnUserId() throws Exception {
    final CreatePreDefMoneyflowRequest request = new CreatePreDefMoneyflowRequest();
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forNewPreDefMoneyflow().build();
    transport.setUserid(UserTransportBuilder.ADMIN_ID);
    request.setPreDefMoneyflowTransport(transport);
    final CreatePreDefMoneyflowResponse response = super.callUsecaseExpect200(request,
        CreatePreDefMoneyflowResponse.class);
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final PreDefMoneyflowID preDefMoneyflowId = new PreDefMoneyflowID(
        PreDefMoneyflowTransportBuilder.NEXT_ID);
    final PreDefMoneyflow preDefMoneyflow = this.preDefMoneyflowService
        .getPreDefMoneyflowById(userId, preDefMoneyflowId);
    Assertions.assertEquals(PreDefMoneyflowTransportBuilder.NEXT_ID,
        preDefMoneyflow.getId().getId());
    Assertions.assertEquals(PreDefMoneyflowTransportBuilder.NEWPRE_DEF_MONEYFLOW_COMMENT,
        preDefMoneyflow.getComment());
    Assertions.assertEquals(PreDefMoneyflowTransportBuilder.NEXT_ID,
        response.getPreDefMoneyflowId());
  }

  @Test
   void test_NotGroupUseableCapitalsourceUsed_Error() throws Exception {
    this.userName = UserTransportBuilder.USER3_NAME;
    this.userPassword = UserTransportBuilder.USER3_PASSWORD;
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forNewPreDefMoneyflow().build();
    transport.setCapitalsourceid(CapitalsourceTransportBuilder.CAPITALSOURCE1_ID);
    transport.setContractpartnerid(ContractpartnerTransportBuilder.CONTRACTPARTNER1_ID);
    this.testError(transport, ErrorCode.CAPITALSOURCE_DOES_NOT_EXIST);
  }

  @Test
   void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;

    super.callUsecaseExpect403(new PreDefMoneyflowTransportBuilder());
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    final CreatePreDefMoneyflowRequest request = new CreatePreDefMoneyflowRequest();
    final PreDefMoneyflowTransport transport = new PreDefMoneyflowTransportBuilder()
        .forNewPreDefMoneyflow().build();
    request.setPreDefMoneyflowTransport(transport);
    super.callUsecaseExpect422(request, ValidationResponse.class);
  }
}