
package org.laladev.moneyjinn.server.controller.group;

import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.controller.api.GroupControllerApi;
import org.laladev.moneyjinn.server.model.GroupTransport;
import org.laladev.moneyjinn.server.model.ShowGroupListResponse;
import org.laladev.moneyjinn.service.api.IGroupService;

class ShowGroupListTest extends AbstractControllerTest {
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
    super.getMock(GroupControllerApi.class).showGroupList();
  }

  private ShowGroupListResponse getCompleteResponse() {
    final ShowGroupListResponse expected = new ShowGroupListResponse();
    final List<GroupTransport> groupTransports = new ArrayList<>();
    groupTransports.add(new GroupTransportBuilder().forAdminGroup().build());
    groupTransports.add(new GroupTransportBuilder().forGroup1().build());
    groupTransports.add(new GroupTransportBuilder().forGroup2().build());
    groupTransports.add(new GroupTransportBuilder().forGroup3().build());
    expected.setGroupTransports(groupTransports);
    return expected;
  }

  @Test
   void test_default_FullResponseObject() throws Exception {
    final ShowGroupListResponse expected = this.getCompleteResponse();

    final ShowGroupListResponse actual = super.callUsecaseExpect200(ShowGroupListResponse.class);

    Assertions.assertEquals(expected, actual);
  }

  @Test
   void test_OnlyAdminAllowed_ErrorResponse() throws Exception {
    this.userName = UserTransportBuilder.USER1_NAME;
    this.userPassword = UserTransportBuilder.USER1_PASSWORD;

    super.callUsecaseExpect403();
  }

  @Test
   void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;

    super.callUsecaseExpect403();
  }
}
