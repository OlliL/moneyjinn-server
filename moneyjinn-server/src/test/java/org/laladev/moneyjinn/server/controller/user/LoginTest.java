
package org.laladev.moneyjinn.server.controller.user;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.server.model.ErrorResponse;
import org.laladev.moneyjinn.server.model.LoginRequest;
import org.laladev.moneyjinn.server.model.LoginResponse;
import  org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.config.jwt.JwtTokenProvider;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class LoginTest extends AbstractControllerTest {
  @Inject
  JwtTokenProvider jwtTokenProvider;

  private final HttpMethod method = HttpMethod.POST;
  private String userName;
  private String userPassword;

  @BeforeEach
  public void setUp() {
    this.userName = null;
    this.userPassword = null;
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
  public void test_RegularUser_Successfull() throws Exception {
    final String username = UserTransportBuilder.USER1_NAME;
    final String password = UserTransportBuilder.USER1_PASSWORD;

    final LoginRequest request = new LoginRequest();
    request.setUserName(username);
    request.setUserPassword(password);

    final LoginResponse response = super.callUsecaseWithContent("", this.method, request, false,
        LoginResponse.class);

    Assertions.assertEquals(new UserTransportBuilder().forUser1().build(),
        response.getUserTransport());

    Assertions.assertTrue(this.jwtTokenProvider.validateToken(response.getToken()));
    Assertions.assertFalse(this.jwtTokenProvider.isRefreshToken(response.getToken()));

    Assertions.assertTrue(this.jwtTokenProvider.validateToken(response.getRefreshToken()));
    Assertions.assertTrue(this.jwtTokenProvider.isRefreshToken(response.getRefreshToken()));
  }

  @Test
  public void test_LockedUser_ErrorResponse() throws Exception {
    final String username = UserTransportBuilder.USER2_NAME;
    final String password = UserTransportBuilder.USER2_PASSWORD;

    final LoginRequest request = new LoginRequest();
    request.setUserName(username);
    request.setUserPassword(password);

    final ErrorResponse response = super.callUsecaseExpect403("", this.method, request,
        ErrorResponse.class);

    Assertions.assertEquals(response.getCode(), ErrorCode.ACCOUNT_IS_LOCKED.getErrorCode());
  }

  @Test
  public void test_WrongPassword_ErrorResponse() throws Exception {
    final String username = UserTransportBuilder.USER2_NAME;
    final String password = "wrong password";

    final LoginRequest request = new LoginRequest();
    request.setUserName(username);
    request.setUserPassword(password);

    super.callUsecaseExpect403("", this.method, request);

  }

  @Test
  @Sql("classpath:h2defaults.sql")
  public void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;

    final LoginRequest request = new LoginRequest();
    request.setUserName(this.userName);
    request.setUserPassword(this.userPassword);
    super.callUsecaseWithContent("", this.method, request, false, LoginResponse.class);
  }
}