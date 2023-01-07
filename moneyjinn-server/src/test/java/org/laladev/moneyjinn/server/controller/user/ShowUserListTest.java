
package org.laladev.moneyjinn.server.controller.user;

import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.rest.model.transport.GroupTransport;
import org.laladev.moneyjinn.core.rest.model.transport.UserTransport;
import org.laladev.moneyjinn.core.rest.model.user.ShowUserListResponse;
import org.laladev.moneyjinn.core.rest.model.user.transport.AccessRelationTransport;
import org.laladev.moneyjinn.server.builder.AccessRelationTransportBuilder;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.IUserService;
import org.springframework.http.HttpMethod;

public class ShowUserListTest extends AbstractControllerTest {
  @Inject
  private IUserService userService;
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

  private ShowUserListResponse getCompleteResponse() {
    final ShowUserListResponse expected = new ShowUserListResponse();
    final List<UserTransport> userTransports = new ArrayList<>();
    userTransports.add(new UserTransportBuilder().forAdmin().build());
    userTransports.add(new UserTransportBuilder().forUser1().build());
    userTransports.add(new UserTransportBuilder().forUser2().build());
    userTransports.add(new UserTransportBuilder().forUser3().build());
    expected.setUserTransports(userTransports);
    final List<GroupTransport> groupTransports = new ArrayList<>();
    groupTransports.add(new GroupTransportBuilder().forGroup1().build());
    groupTransports.add(new GroupTransportBuilder().forAdminGroup().build());
    expected.setGroupTransports(groupTransports);
    final List<AccessRelationTransport> accessRelationTransports = new ArrayList<>();
    accessRelationTransports.add(new AccessRelationTransportBuilder().forAdminUser().build());
    accessRelationTransports
        .add(new AccessRelationTransportBuilder().forUser1_2000_01_01().build());
    accessRelationTransports
        .add(new AccessRelationTransportBuilder().forUser2_2000_01_01().build());
    accessRelationTransports
        .add(new AccessRelationTransportBuilder().forUser3_2000_01_01().build());
    expected.setAccessRelationTransports(accessRelationTransports);
    return expected;
  }

  @Test
  public void test_default_FullResponseObject() throws Exception {
    final ShowUserListResponse expected = this.getCompleteResponse();
    final ShowUserListResponse actual = super.callUsecaseWithoutContent("", this.method, false,
        ShowUserListResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_OnlyAdminAllowed_ErrorResponse() throws Exception {
    this.userName = UserTransportBuilder.USER1_NAME;
    this.userPassword = UserTransportBuilder.USER1_PASSWORD;
    super.callUsecaseExpect403("", this.method);
  }

  @Test
  public void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;
    super.callUsecaseExpect403("", this.method);
  }

}
