
package org.laladev.moneyjinn.server.controller.postingaccount;

import jakarta.inject.Inject;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.PostingAccount;
import org.laladev.moneyjinn.server.builder.PostingAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.controller.api.PostingAccountControllerApi;
import org.laladev.moneyjinn.server.model.CreatePostingAccountRequest;
import org.laladev.moneyjinn.server.model.CreatePostingAccountResponse;
import org.laladev.moneyjinn.server.model.PostingAccountTransport;
import org.laladev.moneyjinn.server.model.ValidationItemTransport;
import org.laladev.moneyjinn.server.model.ValidationResponse;
import org.laladev.moneyjinn.service.api.IPostingAccountService;
import org.springframework.test.context.jdbc.Sql;

public class CreatePostingAccountTest extends AbstractControllerTest {
  @Inject
  private IPostingAccountService postingAccountService;

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
  protected Method getMethod() {
    return super.getMethodFromTestClassName(PostingAccountControllerApi.class, this.getClass());
  }

  private void testError(final PostingAccountTransport transport, final ErrorCode errorCode)
      throws Exception {
    final CreatePostingAccountRequest request = new CreatePostingAccountRequest();
    request.setPostingAccountTransport(transport);
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
  public void test_PostingAccountnameAlreadyExisting_Error() throws Exception {
    final PostingAccountTransport transport = new PostingAccountTransportBuilder()
        .forNewPostingAccount().build();
    transport.setName(PostingAccountTransportBuilder.POSTING_ACCOUNT1_NAME);
    this.testError(transport, ErrorCode.POSTINGACCOUNT_WITH_SAME_NAME_ALREADY_EXISTS);
  }

  @Test
  public void test_emptyPostingAccountname_Error() throws Exception {
    final PostingAccountTransport transport = new PostingAccountTransportBuilder()
        .forNewPostingAccount().build();
    transport.setName("");
    this.testError(transport, ErrorCode.NAME_MUST_NOT_BE_EMPTY);
  }

  @Test
  public void test_nullPostingAccountname_Error() throws Exception {
    final PostingAccountTransport transport = new PostingAccountTransportBuilder()
        .forNewPostingAccount().build();
    transport.setName(null);
    this.testError(transport, ErrorCode.NAME_MUST_NOT_BE_EMPTY);
  }

  @Test
  public void test_standardRequest_SuccessfullNoContent() throws Exception {
    final CreatePostingAccountRequest request = new CreatePostingAccountRequest();
    final PostingAccountTransport transport = new PostingAccountTransportBuilder()
        .forNewPostingAccount().build();
    request.setPostingAccountTransport(transport);
    final CreatePostingAccountResponse expected = new CreatePostingAccountResponse();
    expected.setPostingAccountId(PostingAccountTransportBuilder.NEXT_ID);

    final CreatePostingAccountResponse actual = super.callUsecaseExpect200(request,
        CreatePostingAccountResponse.class);

    Assertions.assertEquals(expected, actual);

    final PostingAccount postingAccount = this.postingAccountService
        .getPostingAccountByName(PostingAccountTransportBuilder.NEWPOSTING_ACCOUNT_NAME);
    Assertions.assertEquals(PostingAccountTransportBuilder.NEXT_ID, postingAccount.getId().getId());
    Assertions.assertEquals(PostingAccountTransportBuilder.NEXT_ID, actual.getPostingAccountId());
    Assertions.assertEquals(PostingAccountTransportBuilder.NEWPOSTING_ACCOUNT_NAME,
        postingAccount.getName());
  }

  @Test
  public void test_OnlyAdminAllowed_ErrorResponse() throws Exception {
    this.userName = UserTransportBuilder.USER1_NAME;
    this.userPassword = UserTransportBuilder.USER1_PASSWORD;

    super.callUsecaseExpect403(new CreatePostingAccountRequest());
  }

  @Test
  public void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;

    super.callUsecaseExpect403(new CreatePostingAccountRequest());
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  public void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
    final CreatePostingAccountRequest request = new CreatePostingAccountRequest();
    final PostingAccountTransport transport = new PostingAccountTransportBuilder()
        .forNewPostingAccount().build();
    request.setPostingAccountTransport(transport);
    final CreatePostingAccountResponse expected = new CreatePostingAccountResponse();
    expected.setPostingAccountId(1L);

    final CreatePostingAccountResponse actual = super.callUsecaseExpect200(request,
        CreatePostingAccountResponse.class);

    Assertions.assertEquals(expected, actual);
  }
}