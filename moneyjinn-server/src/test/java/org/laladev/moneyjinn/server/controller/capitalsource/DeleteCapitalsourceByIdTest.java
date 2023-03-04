
package org.laladev.moneyjinn.server.controller.capitalsource;

import jakarta.inject.Inject;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.controller.api.CapitalsourceControllerApi;
import org.laladev.moneyjinn.server.model.ErrorResponse;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.springframework.test.context.jdbc.Sql;

public class DeleteCapitalsourceByIdTest extends AbstractControllerTest {
  @Inject
  private ICapitalsourceService capitalsourceService;

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
    return super.getMethodFromTestClassName(CapitalsourceControllerApi.class, this.getClass());
  }

  @Test
  public void test_regularCapitalsourceNoData_SuccessfullNoContent() throws Exception {
    this.userName = UserTransportBuilder.USER3_NAME;
    this.userPassword = UserTransportBuilder.USER3_PASSWORD;
    final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
    final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
    final CapitalsourceID capitalsourceId = new CapitalsourceID(
        CapitalsourceTransportBuilder.CAPITALSOURCE3_ID);
    Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
        capitalsourceId);
    Assertions.assertNotNull(capitalsource);

    super.callUsecaseExpect204WithUriVariables(CapitalsourceTransportBuilder.CAPITALSOURCE3_ID);

    capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
        capitalsourceId);
    Assertions.assertNull(capitalsource);
  }

  @Test
  public void test_nonExistingCapitalsource_SuccessfullNoContent() throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
    final CapitalsourceID capitalsourceId = new CapitalsourceID(
        CapitalsourceTransportBuilder.NON_EXISTING_ID);
    Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
        capitalsourceId);
    Assertions.assertNull(capitalsource);

    super.callUsecaseExpect204WithUriVariables(CapitalsourceTransportBuilder.NON_EXISTING_ID);

    capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
        capitalsourceId);
    Assertions.assertNull(capitalsource);
  }

  @Test
  public void test_regularCapitalsourceWithData_ErrorResponse() throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
    final CapitalsourceID capitalsourceId = new CapitalsourceID(
        CapitalsourceTransportBuilder.CAPITALSOURCE1_ID);
    final ErrorResponse expected = new ErrorResponse();
    expected.setCode(ErrorCode.CAPITALSOURCE_STILL_REFERENCED.getErrorCode());
    expected.setMessage(
        "You may not delete a source of capital while it is referenced by a flow of money!");
    Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
        capitalsourceId);
    Assertions.assertNotNull(capitalsource);

    final ErrorResponse actual = super.callUsecaseExpect400(ErrorResponse.class,
        CapitalsourceTransportBuilder.CAPITALSOURCE1_ID);

    capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
        capitalsourceId);
    Assertions.assertNotNull(capitalsource);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_deleteCapitalsourceOwnedBySomeoneElse_notSuccessfull() throws Exception {
    final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
    final GroupID groupId = new GroupID(GroupTransportBuilder.GROUP1_ID);
    final CapitalsourceID capitalsourceId = new CapitalsourceID(
        CapitalsourceTransportBuilder.CAPITALSOURCE3_ID);
    Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
        capitalsourceId);
    Assertions.assertNotNull(capitalsource);

    super.callUsecaseExpect204WithUriVariables(CapitalsourceTransportBuilder.CAPITALSOURCE3_ID);
    capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
        capitalsourceId);

    Assertions.assertNotNull(capitalsource);
  }

  @Test
  public void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;

    super.callUsecaseExpect403WithUriVariables(CapitalsourceTransportBuilder.NON_EXISTING_ID);
  }

  @Test
  @Sql("classpath:h2defaults.sql")
  public void test_emptyDatabase_noException() throws Exception {
    this.userName = UserTransportBuilder.ADMIN_NAME;
    this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;

    super.callUsecaseExpect204WithUriVariables(CapitalsourceTransportBuilder.NON_EXISTING_ID);
  }
}