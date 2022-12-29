
package org.laladev.moneyjinn.server.controller.postingaccount;

import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import org.laladev.moneyjinn.core.rest.model.postingaccount.UpdatePostingAccountRequest;
import org.laladev.moneyjinn.core.rest.model.transport.PostingAccountTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.model.PostingAccount;
import org.laladev.moneyjinn.model.PostingAccountID;
import org.laladev.moneyjinn.server.builder.PostingAccountTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.IPostingAccountService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class UpdatePostingAccountTest extends AbstractControllerTest {
  @Inject
  private IPostingAccountService postingAccountService;
  private final HttpMethod method = HttpMethod.PUT;
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
    final UpdatePostingAccountRequest request = new UpdatePostingAccountRequest();
    request.setPostingAccountTransport(transport);
    final List<ValidationItemTransport> validationItems = new ArrayList<>();
    validationItems.add(new ValidationItemTransportBuilder().withKey(transport.getId().intValue())
        .withError(errorCode.getErrorCode()).build());
    final ValidationResponse expected = new ValidationResponse();
    expected.setValidationItemTransports(validationItems);
    expected.setResult(Boolean.FALSE);
    final ValidationResponse actual = super.callUsecaseWithContent("", this.method, request, false,
        ValidationResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_PostingAccountnameAlreadyExisting_Error() throws Exception {
    final PostingAccountTransport transport = new PostingAccountTransportBuilder()
        .forPostingAccount2().build();
    transport.setName(PostingAccountTransportBuilder.POSTING_ACCOUNT1_NAME);
    this.testError(transport, ErrorCode.POSTINGACCOUNT_WITH_SAME_NAME_ALREADY_EXISTS);
  }

  @Test
  public void test_EmptyPostingAccountname_Error() throws Exception {
    final PostingAccountTransport transport = new PostingAccountTransportBuilder()
        .forPostingAccount2().build();
    transport.setName("");
    this.testError(transport, ErrorCode.NAME_MUST_NOT_BE_EMPTY);
  }

  @Test
  public void test_standardRequest_Successfull() throws Exception {
    final UpdatePostingAccountRequest request = new UpdatePostingAccountRequest();
    final PostingAccountTransport transport = new PostingAccountTransportBuilder()
        .forPostingAccount1().build();
    transport.setName("hugo");
    request.setPostingAccountTransport(transport);
    final ValidationResponse actual = super.callUsecaseWithContent("", this.method, request, false,
        ValidationResponse.class);
    Assertions.assertTrue(actual.getResult());
    final PostingAccount postingAccount = this.postingAccountService.getPostingAccountById(
        new PostingAccountID(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID));
    Assertions.assertEquals(PostingAccountTransportBuilder.POSTING_ACCOUNT1_ID,
        postingAccount.getId().getId());
    Assertions.assertEquals("hugo", postingAccount.getName());
  }

  @Test
  public void test_OnlyAdminAllowed_ErrorResponse() throws Exception {
    this.userName = UserTransportBuilder.USER1_NAME;
    this.userPassword = UserTransportBuilder.USER1_PASSWORD;
    super.callUsecaseExpect403("", this.method, new UpdatePostingAccountRequest());
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
    final UpdatePostingAccountRequest request = new UpdatePostingAccountRequest();
    final PostingAccountTransport transport = new PostingAccountTransportBuilder()
        .forPostingAccount1().build();
    request.setPostingAccountTransport(transport);
    final ValidationResponse actual = super.callUsecaseWithContent("", this.method, request, false,
        ValidationResponse.class);
    Assertions.assertTrue(actual.getResult());
  }
}