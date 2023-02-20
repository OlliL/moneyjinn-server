
package org.laladev.moneyjinn.server.controller.group;

import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.model.GroupTransport;
import org.laladev.moneyjinn.server.model.UpdateGroupRequest;
import org.laladev.moneyjinn.server.model.ValidationItemTransport;
import org.laladev.moneyjinn.server.model.ValidationResponse;
import org.laladev.moneyjinn.service.api.IGroupService;
import org.springframework.http.HttpMethod;

public class UpdateGroupTest extends AbstractControllerTest {
  @Inject
  private IGroupService groupService;
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

  private void testError(final GroupTransport transport, final ErrorCode errorCode)
      throws Exception {
    final UpdateGroupRequest request = new UpdateGroupRequest();
    request.setGroupTransport(transport);
    final List<ValidationItemTransport> validationItems = new ArrayList<>();
    validationItems.add(new ValidationItemTransportBuilder().withKey(transport.getId().toString())
        .withError(errorCode.getErrorCode()).build());
    final ValidationResponse expected = new ValidationResponse();
    expected.setValidationItemTransports(validationItems);
    expected.setResult(Boolean.FALSE);

    final ValidationResponse actual = super.callUsecaseExpect422(this.method, request,
        ValidationResponse.class);

    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_GroupnameAlreadyExisting_Error() throws Exception {
    final GroupTransport transport = new GroupTransportBuilder().forGroup2().build();
    transport.setName(GroupTransportBuilder.GROUP1_NAME);
    this.testError(transport, ErrorCode.GROUP_WITH_SAME_NAME_ALREADY_EXISTS);
  }

  @Test
  public void test_EmptyGroupname_Error() throws Exception {
    final GroupTransport transport = new GroupTransportBuilder().forGroup2().build();
    transport.setName("");
    this.testError(transport, ErrorCode.NAME_MUST_NOT_BE_EMPTY);
  }

  @Test
  public void test_standardRequest_Successfull() throws Exception {
    final UpdateGroupRequest request = new UpdateGroupRequest();
    final GroupTransport transport = new GroupTransportBuilder().forGroup1().build();
    transport.setName("hugo");
    request.setGroupTransport(transport);

    super.callUsecaseExpect204(this.method, request);

    final Group group = this.groupService
        .getGroupById(new GroupID(GroupTransportBuilder.GROUP1_ID));
    Assertions.assertEquals(GroupTransportBuilder.GROUP1_ID, group.getId().getId());
    Assertions.assertEquals("hugo", group.getName());
  }

  @Test
  public void test_OnlyAdminAllowed_ErrorResponse() throws Exception {
    this.userName = UserTransportBuilder.USER1_NAME;
    this.userPassword = UserTransportBuilder.USER1_PASSWORD;
    super.callUsecaseExpect403("", this.method, new UpdateGroupRequest());
  }

  @Test
  public void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;
    super.callUsecaseExpect403("", this.method);
  }
}