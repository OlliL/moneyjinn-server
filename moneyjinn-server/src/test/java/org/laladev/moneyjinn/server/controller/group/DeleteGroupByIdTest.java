
package org.laladev.moneyjinn.server.controller.group;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.model.ErrorResponse;
import org.laladev.moneyjinn.service.api.IGroupService;
import org.springframework.http.HttpMethod;

public class DeleteGroupByIdTest extends AbstractControllerTest {
  @Inject
  private IGroupService groupService;
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
  public void test_regularGroupNoData_SuccessfullNoContent() throws Exception {
    Group group = this.groupService.getGroupById(new GroupID(GroupTransportBuilder.GROUP3_ID));
    Assertions.assertNotNull(group);

    super.callUsecaseExpect204("/" + GroupTransportBuilder.GROUP3_ID, this.method);

    group = this.groupService.getGroupById(new GroupID(GroupTransportBuilder.GROUP3_ID));
    Assertions.assertNull(group);
  }

  @Test
  public void test_nonExistingGroup_SuccessfullNoContent() throws Exception {
    Group group = this.groupService
        .getGroupById(new GroupID(GroupTransportBuilder.NON_EXISTING_ID));
    Assertions.assertNull(group);

    super.callUsecaseExpect204("/" + GroupTransportBuilder.NON_EXISTING_ID, this.method);

    group = this.groupService.getGroupById(new GroupID(GroupTransportBuilder.NON_EXISTING_ID));
    Assertions.assertNull(group);
  }

  @Test
  public void test_regularGroupWithData_ErrorResponse() throws Exception {
    final ErrorResponse expected = new ErrorResponse();
    expected.setCode(ErrorCode.GROUP_IN_USE.getErrorCode());
    expected.setMessage("You may not delete a group while there where/are users assigned to it!");
    Group group = this.groupService.getGroupById(new GroupID(GroupTransportBuilder.GROUP1_ID));
    Assertions.assertNotNull(group);

    final ErrorResponse actual = super.callUsecaseExpect400("/" + GroupTransportBuilder.GROUP1_ID,
        this.method, ErrorResponse.class);

    group = this.groupService.getGroupById(new GroupID(GroupTransportBuilder.GROUP1_ID));
    Assertions.assertNotNull(group);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_OnlyAdminAllowed_ErrorResponse() throws Exception {
    this.userName = UserTransportBuilder.USER1_NAME;
    this.userPassword = UserTransportBuilder.USER1_PASSWORD;

    super.callUsecaseExpect403("/" + GroupTransportBuilder.GROUP1_ID, this.method);
  }

  @Test
  public void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;

    super.callUsecaseExpect403("/" + GroupTransportBuilder.GROUP1_ID, this.method);
  }
}