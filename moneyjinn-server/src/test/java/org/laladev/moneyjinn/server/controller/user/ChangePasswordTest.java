
package org.laladev.moneyjinn.server.controller.user;

import jakarta.inject.Inject;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.controller.api.UserControllerApi;
import org.laladev.moneyjinn.server.model.ChangePasswordRequest;
import org.laladev.moneyjinn.server.model.ErrorResponse;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.IUserService;
import org.springframework.test.context.jdbc.Sql;

public class ChangePasswordTest extends AbstractControllerTest {
  @Inject
  private IUserService userService;
  @Inject
  private IAccessRelationService accessRelationService;

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
    return super.getMethodFromTestClassName(UserControllerApi.class, this.getClass());
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

    super.callUsecaseExpect204(request);

    user = this.userService.getUserById(userId);
    cryptedPassword = this.userService.cryptPassword(newPassword);
    Assertions.assertEquals(user.getPassword(), cryptedPassword);

  }

  @Test
  public void test_OldPasswordMatchingNewPasswordEmptyButUserNew_errorRaised() throws Exception {
    final ChangePasswordRequest request = new ChangePasswordRequest();
    request.setOldPassword(this.userPassword);
    request.setPassword("");

    final ErrorResponse actual = super.callUsecaseExpect400(request, ErrorResponse.class);
    Assertions.assertNotNull(actual);
    Assertions.assertEquals(actual.getCode(), ErrorCode.PASSWORD_MUST_BE_CHANGED.getErrorCode());

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

    super.callUsecaseExpect204(request);

    user = this.userService.getUserById(userId);
    Assertions.assertEquals(user.getPassword(), cryptedPassword);
  }

  @Test
  public void test_OldPasswordNotMatching_errorRaised() throws Exception {
    final ChangePasswordRequest request = new ChangePasswordRequest();
    request.setOldPassword("wrongPassword");

    final ErrorResponse actual = super.callUsecaseExpect400(request, ErrorResponse.class);
    Assertions.assertNotNull(actual);
    Assertions.assertEquals(actual.getCode(), ErrorCode.PASSWORD_NOT_MATCHING.getErrorCode());

  }

  @Test
  public void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;

    super.callUsecaseExpect403(new ChangePasswordRequest());
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  public void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;

    final ChangePasswordRequest request = new ChangePasswordRequest();
    request.setOldPassword(this.userPassword);
    request.setPassword(this.userPassword + "new");

    super.callUsecaseExpect204(request);
  }
}