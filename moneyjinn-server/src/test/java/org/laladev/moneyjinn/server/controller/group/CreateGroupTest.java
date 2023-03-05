
package org.laladev.moneyjinn.server.controller.group;

import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.controller.api.GroupControllerApi;
import org.laladev.moneyjinn.server.model.CreateGroupRequest;
import org.laladev.moneyjinn.server.model.CreateGroupResponse;
import org.laladev.moneyjinn.server.model.GroupTransport;
import org.laladev.moneyjinn.server.model.ValidationItemTransport;
import org.laladev.moneyjinn.server.model.ValidationResponse;
import org.laladev.moneyjinn.service.api.IGroupService;

class CreateGroupTest extends AbstractControllerTest {
  @Inject
  private IGroupService groupService;

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
  protected void loadMethod() {
    super.getMock(GroupControllerApi.class).createGroup(null);
  }

  private void testError(final GroupTransport transport, final ErrorCode errorCode)
      throws Exception {
    final CreateGroupRequest request = new CreateGroupRequest();
    request.setGroupTransport(transport);
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
   void test_GroupnameAlreadyExisting_Error() throws Exception {
    final GroupTransport transport = new GroupTransportBuilder().forNewGroup().build();
    transport.setName(GroupTransportBuilder.GROUP1_NAME);
    this.testError(transport, ErrorCode.GROUP_WITH_SAME_NAME_ALREADY_EXISTS);
  }

  @Test
   void test_emptyGroupname_Error() throws Exception {
    final GroupTransport transport = new GroupTransportBuilder().forNewGroup().build();
    transport.setName("");
    this.testError(transport, ErrorCode.NAME_MUST_NOT_BE_EMPTY);
  }

  @Test
   void test_nullGroupname_Error() throws Exception {
    final GroupTransport transport = new GroupTransportBuilder().forNewGroup().build();
    transport.setName(null);
    this.testError(transport, ErrorCode.NAME_MUST_NOT_BE_EMPTY);
  }

  @Test
   void test_standardRequest_Successfull() throws Exception {
    final CreateGroupRequest request = new CreateGroupRequest();
    final GroupTransport transport = new GroupTransportBuilder().forNewGroup().build();
    request.setGroupTransport(transport);

    final CreateGroupResponse actual = super.callUsecaseExpect200(request,
        CreateGroupResponse.class);

    final Group group = this.groupService.getGroupByName(GroupTransportBuilder.NEWGROUP_NAME);
    Assertions.assertEquals(GroupTransportBuilder.NEXT_ID, group.getId().getId());
    Assertions.assertEquals(GroupTransportBuilder.NEXT_ID, actual.getGroupId());
    Assertions.assertEquals(GroupTransportBuilder.NEWGROUP_NAME, group.getName());
  }

  @Test
   void test_OnlyAdminAllowed_ErrorResponse() throws Exception {
    this.userName = UserTransportBuilder.USER1_NAME;
    this.userPassword = UserTransportBuilder.USER1_PASSWORD;
    super.callUsecaseExpect403(new CreateGroupRequest());
  }

  @Test
   void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;

    super.callUsecaseExpect403(new CreateGroupRequest());
  }
}