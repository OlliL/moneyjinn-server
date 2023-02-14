
package org.laladev.moneyjinn.server.controller.postingaccount;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.server.model.ErrorResponse;
import org.laladev.moneyjinn.model.PostingAccount;
import org.laladev.moneyjinn.model.PostingAccountID;
import org.laladev.moneyjinn.server.builder.PostingAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.IPostingAccountService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class DeletePostingAccountByIdTest extends AbstractControllerTest {
  @Inject
  private IPostingAccountService postingAccountService;
  private final HttpMethod method = HttpMethod.DELETE;
  private String userName;
  private String userPassword;

  @BeforeEach
  public void setUp() {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
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
  public void test_regularPostingAccountNoData_SuccessfullNoContent() throws Exception {
    PostingAccount postingAccount = this.postingAccountService.getPostingAccountById(
        new PostingAccountID(PostingAccountTransportBuilder.POSTING_ACCOUNT3_ID));
    Assertions.assertNotNull(postingAccount);
    super.callUsecaseWithoutContent("/" + PostingAccountTransportBuilder.POSTING_ACCOUNT3_ID,
        this.method, true, Object.class);
    postingAccount = this.postingAccountService.getPostingAccountById(
        new PostingAccountID(PostingAccountTransportBuilder.POSTING_ACCOUNT3_ID));
    Assertions.assertNull(postingAccount);
  }

  @Test
  public void test_nonExistingPostingAccount_SuccessfullNoContent() throws Exception {
    PostingAccount postingAccount = this.postingAccountService.getPostingAccountById(
        new PostingAccountID(PostingAccountTransportBuilder.NON_EXISTING_ID));
    Assertions.assertNull(postingAccount);
    super.callUsecaseWithoutContent("/" + PostingAccountTransportBuilder.NON_EXISTING_ID,
        this.method, true, Object.class);
    postingAccount = this.postingAccountService.getPostingAccountById(
        new PostingAccountID(PostingAccountTransportBuilder.NON_EXISTING_ID));
    Assertions.assertNull(postingAccount);
  }

  @Test
  public void test_regularPostingAccountWithData_SuccessfullNoContent() throws Exception {
    final ErrorResponse expected = new ErrorResponse();
    expected.setCode(ErrorCode.POSTINGACCOUNT_STILL_REFERENCED.getErrorCode());
    expected.setMessage(
        "The posting account cannot be deleted because it is still referenced by a flow of money or a predefined flow of money!");
    PostingAccount postingAccount = this.postingAccountService.getPostingAccountById(
        new PostingAccountID(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID));
    Assertions.assertNotNull(postingAccount);
    final ErrorResponse response = super.callUsecaseWithoutContent(
        "/" + PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID, this.method, false,
        ErrorResponse.class);
    postingAccount = this.postingAccountService.getPostingAccountById(
        new PostingAccountID(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID));
    Assertions.assertNotNull(postingAccount);
    Assertions.assertEquals(expected, response);
  }

  @Test
  public void test_OnlyAdminAllowed_ErrorResponse() throws Exception {
    this.userName = UserTransportBuilder.USER1_NAME;
    this.userPassword = UserTransportBuilder.USER1_PASSWORD;
    super.callUsecaseExpect403("/" + PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID,
        this.method);
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
    super.callUsecaseWithoutContent("/" + PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID,
        this.method, true, Object.class);
  }
}