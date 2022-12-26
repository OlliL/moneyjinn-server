
package org.laladev.moneyjinn.server.controller.group;

import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.group.ShowGroupListResponse;
import org.laladev.moneyjinn.core.rest.model.transport.GroupTransport;
import org.laladev.moneyjinn.model.access.AccessID;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.setting.ClientMaxRowsSetting;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.IGroupService;
import org.laladev.moneyjinn.service.impl.SettingService;
import org.springframework.http.HttpMethod;

public class ShowGroupListTest extends AbstractControllerTest {
  @Inject
  private SettingService settingService;
  @Inject
  private IGroupService groupService;
  private final HttpMethod method = HttpMethod.GET;
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

  private ShowGroupListResponse getCompleteResponse() {
    final ShowGroupListResponse expected = new ShowGroupListResponse();
    expected.setInitials(new HashSet<>(Arrays.asList('A', 'G')));
    final List<GroupTransport> groupTransports = new ArrayList<>();
    groupTransports.add(new GroupTransportBuilder().forAdminGroup().build());
    groupTransports.add(new GroupTransportBuilder().forGroup1().build());
    groupTransports.add(new GroupTransportBuilder().forGroup2().build());
    groupTransports.add(new GroupTransportBuilder().forGroup3().build());
    expected.setGroupTransports(groupTransports);
    return expected;
  }

  @Test
  public void test_default_FullResponseObject() throws Exception {
    final ShowGroupListResponse expected = this.getCompleteResponse();
    final ShowGroupListResponse actual = super.callUsecaseWithoutContent("", this.method, false,
        ShowGroupListResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_MaxRowSettingReached_OnlyInitials() throws Exception {
    final ShowGroupListResponse expected = new ShowGroupListResponse();
    expected.setInitials(new HashSet<>(Arrays.asList('A', 'G')));
    final ClientMaxRowsSetting setting = new ClientMaxRowsSetting(1);
    this.settingService.setClientMaxRowsSetting(new AccessID(UserTransportBuilder.ADMIN_ID),
        setting);
    final ShowGroupListResponse actual = super.callUsecaseWithoutContent("", this.method, false,
        ShowGroupListResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_explicitAll_FullResponseObject() throws Exception {
    final ShowGroupListResponse expected = this.getCompleteResponse();
    final ClientMaxRowsSetting setting = new ClientMaxRowsSetting(1);
    this.settingService.setClientMaxRowsSetting(new AccessID(UserTransportBuilder.ADMIN_ID),
        setting);
    final ShowGroupListResponse actual = super.callUsecaseWithoutContent("/all", this.method, false,
        ShowGroupListResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_initialA_AResponseObject() throws Exception {
    final ShowGroupListResponse expected = new ShowGroupListResponse();
    expected.setInitials(new HashSet<>(Arrays.asList('A', 'G')));
    final List<GroupTransport> groupTransports = new ArrayList<>();
    groupTransports.add(new GroupTransportBuilder().forAdminGroup().build());
    expected.setGroupTransports(groupTransports);
    final ShowGroupListResponse actual = super.callUsecaseWithoutContent("/A", this.method, false,
        ShowGroupListResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_initialUnderscore_AResponseObject() throws Exception {
    // make sure that requesting data starting with _ only returns matching data and _ is not
    // interpreted as LIKE SQL special char
    final Group group = new Group();
    group.setName("_1");
    this.groupService.createGroup(group);
    final ShowGroupListResponse expected = new ShowGroupListResponse();
    expected.setInitials(new HashSet<>(Arrays.asList('A', 'G', '_')));
    final List<GroupTransport> groupTransports = new ArrayList<>();
    final GroupTransport groupTransport = new GroupTransport();
    groupTransport.setId(GroupTransportBuilder.NEXT_ID);
    groupTransport.setName(group.getName());
    groupTransports.add(groupTransport);
    expected.setGroupTransports(groupTransports);
    final ShowGroupListResponse actual = super.callUsecaseWithoutContent("/_", this.method, false,
        ShowGroupListResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_initialPercent_AResponseObject() throws Exception {
    // make sure that requesting data starting with % only returns matching data and % is not
    // interpreted as LIKE SQL special char
    final Group group = new Group();
    group.setName("%1");
    this.groupService.createGroup(group);
    final ShowGroupListResponse expected = new ShowGroupListResponse();
    expected.setInitials(new HashSet<>(Arrays.asList('A', 'G', '%')));
    final List<GroupTransport> groupTransports = new ArrayList<>();
    final GroupTransport groupTransport = new GroupTransport();
    groupTransport.setId(GroupTransportBuilder.NEXT_ID);
    groupTransport.setName(group.getName());
    groupTransports.add(groupTransport);
    expected.setGroupTransports(groupTransports);
    final ShowGroupListResponse actual = super.callUsecaseWithoutContent("/%", this.method, false,
        ShowGroupListResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_OnlyAdminAllowed_ErrorResponse() throws Exception {
    this.userName = UserTransportBuilder.USER1_NAME;
    this.userPassword = UserTransportBuilder.USER1_PASSWORD;
    final ErrorResponse actual = super.callUsecaseWithoutContent("", this.method, false,
        ErrorResponse.class);
    Assertions.assertEquals(Integer.valueOf(ErrorCode.USER_IS_NO_ADMIN.getErrorCode()),
        actual.getCode());
  }

  @Test
  public void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;
    final ErrorResponse actual = super.callUsecaseWithoutContent("", this.method, false,
        ErrorResponse.class);
    Assertions.assertEquals(super.accessDeniedErrorResponse(), actual);
  }

  @Test
  public void test_OnlyAdminAllowed_filtered_ErrorResponse() throws Exception {
    this.userName = UserTransportBuilder.USER1_NAME;
    this.userPassword = UserTransportBuilder.USER1_PASSWORD;
    final ErrorResponse actual = super.callUsecaseWithoutContent("/A", this.method, false,
        ErrorResponse.class);
    Assertions.assertEquals(Integer.valueOf(ErrorCode.USER_IS_NO_ADMIN.getErrorCode()),
        actual.getCode());
  }

  @Test
  public void test_AuthorizationRequired_filtered_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;
    final ErrorResponse actual = super.callUsecaseWithoutContent("/A", this.method, false,
        ErrorResponse.class);
    Assertions.assertEquals(super.accessDeniedErrorResponse(), actual);
  }
}
