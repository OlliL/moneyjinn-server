
package org.laladev.moneyjinn.server.controller.user;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.user.ChangePasswordRequest;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.IUserService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class ChangePasswordTest extends AbstractControllerTest {
  @Inject
  private IUserService userService;
  @Inject
  private IAccessRelationService accessRelationService;
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

  @Test
  public void test_OldPasswordMatchingNewPasswordProvided_Successfull() throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final String newPassword = this.userPassword + "new";

    final ChangePasswordRequest request = new ChangePasswordRequest();
    request.setOldPassword(this.userPassword);
    request.setPassword(newPassword);

    User user = this.userService.getUserById(userId);
    String cryptedPassword = this.userService.cryptPassword(this.userPassword);
    Assertions.assertEquals(user.getPassword(), cryptedPassword);

    super.callUsecaseWithContent("", this.method, request, true, Object.class);

    user = this.userService.getUserById(userId);
    cryptedPassword = this.userService.cryptPassword(newPassword);
    Assertions.assertEquals(user.getPassword(), cryptedPassword);

  }

  @Test
  public void test_OldPasswordMatchingNewPasswordEmptyButUserNew_errorRaised() throws Exception {
    final ChangePasswordRequest request = new ChangePasswordRequest();
    request.setOldPassword(this.userPassword);
    request.setPassword("");

    final ErrorResponse errorResponse = super.callUsecaseWithContent("", this.method, request,
        false, ErrorResponse.class);
    Assertions.assertNotNull(errorResponse);
    Assertions.assertEquals(errorResponse.getCode(),
        ErrorCode.PASSWORD_MUST_BE_CHANGED.getErrorCode());

  }

  @Test
  public void test_OldPasswordMatchingNewPasswordEmpty_passwordGotNotChanged() throws Exception {
    this.userName = UserTransportBuilder.USER3_NAME;
    this.userPassword = UserTransportBuilder.USER3_PASSWORD;

    final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
    final String newPassword = "";

    final ChangePasswordRequest request = new ChangePasswordRequest();
    request.setOldPassword(this.userPassword);
    request.setPassword(newPassword);

    final String cryptedPassword = this.userService.cryptPassword(this.userPassword);

    User user = this.userService.getUserById(userId);
    Assertions.assertEquals(user.getPassword(), cryptedPassword);

    super.callUsecaseWithContent("", this.method, request, true, Object.class);

    user = this.userService.getUserById(userId);
    Assertions.assertEquals(user.getPassword(), cryptedPassword);
  }

  @Test
  public void test_OldPasswordNotMatching_errorRaised() throws Exception {
    final ChangePasswordRequest request = new ChangePasswordRequest();
    request.setOldPassword("wrongPassword");

    final ErrorResponse errorResponse = super.callUsecaseWithContent("", this.method, request,
        false, ErrorResponse.class);
    Assertions.assertNotNull(errorResponse);
    Assertions.assertEquals(errorResponse.getCode(),
        ErrorCode.PASSWORD_NOT_MATCHING.getErrorCode());

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

    final ChangePasswordRequest request = new ChangePasswordRequest();
    request.setOldPassword(this.userPassword);
    request.setPassword(this.userPassword + "new");
    super.callUsecaseWithContent("", this.method, request, true, Object.class);
  }
}