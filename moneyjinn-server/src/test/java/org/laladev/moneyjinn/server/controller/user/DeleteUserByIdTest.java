
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
import org.laladev.moneyjinn.server.model.ErrorResponse;
import org.laladev.moneyjinn.service.api.IUserService;

public class DeleteUserByIdTest extends AbstractControllerTest {
  @Inject
  private IUserService userService;

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
    return super.getMethodFromTestClassName(UserControllerApi.class, this.getClass());
  }

  @Test
  public void test_regularUserNoData_SuccessfullNoContent() throws Exception {
    User user = this.userService.getUserById(new UserID(UserTransportBuilder.USER2_ID));
    Assertions.assertNotNull(user);

    super.callUsecaseExpect204WithUriVariables(UserTransportBuilder.USER2_ID);

    user = this.userService.getUserById(new UserID(UserTransportBuilder.USER2_ID));
    Assertions.assertNull(user);
  }

  @Test
  public void test_nonExistingUser_SuccessfullNoContent() throws Exception {
    User user = this.userService.getUserById(new UserID(UserTransportBuilder.NON_EXISTING_ID));
    Assertions.assertNull(user);

    super.callUsecaseExpect204WithUriVariables(UserTransportBuilder.NON_EXISTING_ID);

    user = this.userService.getUserById(new UserID(UserTransportBuilder.NON_EXISTING_ID));
    Assertions.assertNull(user);
  }

  @Test
  public void test_regularUserWithData_SuccessfullNoContent() throws Exception {
    final ErrorResponse expected = new ErrorResponse();
    expected.setCode(ErrorCode.USER_HAS_DATA.getErrorCode());
    expected.setMessage("This user has already entered data and may therefore not be deleted!");
    User user = this.userService.getUserById(new UserID(UserTransportBuilder.USER3_ID));
    Assertions.assertNotNull(user);

    final ErrorResponse actual = super.callUsecaseExpect400(ErrorResponse.class,
        UserTransportBuilder.USER3_ID);

    user = this.userService.getUserById(new UserID(UserTransportBuilder.USER3_ID));
    Assertions.assertNotNull(user);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_OnlyAdminAllowed_ErrorResponse() throws Exception {
    this.userName = UserTransportBuilder.USER1_NAME;
    this.userPassword = UserTransportBuilder.USER1_PASSWORD;

    super.callUsecaseExpect403WithUriVariables(UserTransportBuilder.USER2_ID);
  }

  @Test
  public void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;

    super.callUsecaseExpect403WithUriVariables(UserTransportBuilder.USER2_ID);
  }
}