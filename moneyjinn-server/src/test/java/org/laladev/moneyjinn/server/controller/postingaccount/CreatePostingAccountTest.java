
package org.laladev.moneyjinn.server.controller.postingaccount;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.postingaccount.CreatePostingAccountRequest;
import org.laladev.moneyjinn.core.rest.model.postingaccount.CreatePostingAccountResponse;
import org.laladev.moneyjinn.core.rest.model.transport.PostingAccountTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.model.PostingAccount;
import org.laladev.moneyjinn.server.builder.PostingAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.IPostingAccountService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;
import jakarta.inject.Inject;

public class CreatePostingAccountTest extends AbstractControllerTest {
  @Inject
  private IPostingAccountService postingAccountService;
  private final HttpMethod method = HttpMethod.POST;
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

  private void testError(final PostingAccountTransport transport, final ErrorCode errorCode)
      throws Exception {
    final CreatePostingAccountRequest request = new CreatePostingAccountRequest();
    request.setPostingAccountTransport(transport);
    final List<ValidationItemTransport> validationItems = new ArrayList<>();
    validationItems.add(new ValidationItemTransportBuilder().withKey(null)
        .withError(errorCode.getErrorCode()).build());
    final CreatePostingAccountResponse expected = new CreatePostingAccountResponse();
    expected.setValidationItemTransports(validationItems);
    expected.setResult(Boolean.FALSE);
    final CreatePostingAccountResponse actual = super.callUsecaseWithContent("", this.method,
        request, false, CreatePostingAccountResponse.class);
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
    super.callUsecaseWithContent("", this.method, request, false,
        CreatePostingAccountResponse.class);
    final PostingAccount postingAccount = this.postingAccountService
        .getPostingAccountByName(PostingAccountTransportBuilder.NEWPOSTING_ACCOUNT_NAME);
    Assertions.assertEquals(PostingAccountTransportBuilder.NEXT_ID, postingAccount.getId().getId());
    Assertions.assertEquals(PostingAccountTransportBuilder.NEWPOSTING_ACCOUNT_NAME,
        postingAccount.getName());
  }

  @Test
  public void test_OnlyAdminAllowed_ErrorResponse() throws Exception {
    this.userName = UserTransportBuilder.USER1_NAME;
    this.userPassword = UserTransportBuilder.USER1_PASSWORD;
    super.callUsecaseExpect403("", this.method);
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
    final CreatePostingAccountRequest request = new CreatePostingAccountRequest();
    final PostingAccountTransport transport = new PostingAccountTransportBuilder()
        .forNewPostingAccount().build();
    request.setPostingAccountTransport(transport);
    final CreatePostingAccountResponse expected = new CreatePostingAccountResponse();
    expected.setPostingAccountId(1L);
    expected.setResult(true);
    final CreatePostingAccountResponse actual = super.callUsecaseWithContent("", this.method,
        request, false, CreatePostingAccountResponse.class);
    Assertions.assertEquals(expected, actual);
  }
}