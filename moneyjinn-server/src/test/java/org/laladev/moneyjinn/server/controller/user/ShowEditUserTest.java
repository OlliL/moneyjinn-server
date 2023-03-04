
package org.laladev.moneyjinn.server.controller.user;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.AccessRelationTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.controller.api.UserControllerApi;
import org.laladev.moneyjinn.server.model.AccessRelationTransport;
import org.laladev.moneyjinn.server.model.ShowEditUserResponse;

public class ShowEditUserTest extends AbstractControllerTest {
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
    super.getMock(UserControllerApi.class).showEditUser(null);
  }

  @Test
  public void test_unknownUser_emptyResponseObject() throws Exception {
    final ShowEditUserResponse expected = new ShowEditUserResponse();

    final ShowEditUserResponse actual = super.callUsecaseExpect200(ShowEditUserResponse.class,
        UserTransportBuilder.NON_EXISTING_ID);

    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_User1_completeResponseObject() throws Exception {
    final ShowEditUserResponse expected = new ShowEditUserResponse();
    final List<AccessRelationTransport> accessRelationTransports = new ArrayList<>();
    accessRelationTransports
        .add(new AccessRelationTransportBuilder().forUser1_2000_01_01().build());
    accessRelationTransports
        .add(new AccessRelationTransportBuilder().forUser1_2600_01_01().build());
    accessRelationTransports
        .add(new AccessRelationTransportBuilder().forUser1_2700_01_01().build());
    accessRelationTransports
        .add(new AccessRelationTransportBuilder().forUser1_2800_01_01().build());
    expected.setAccessRelationTransports(accessRelationTransports);

    final ShowEditUserResponse actual = super.callUsecaseExpect200(ShowEditUserResponse.class,
        UserTransportBuilder.USER1_ID);

    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_User2_completeResponseObject() throws Exception {
    final ShowEditUserResponse expected = new ShowEditUserResponse();
    final List<AccessRelationTransport> accessRelationTransports = new ArrayList<>();
    accessRelationTransports
        .add(new AccessRelationTransportBuilder().forUser2_2000_01_01().build());
    expected.setAccessRelationTransports(accessRelationTransports);

    final ShowEditUserResponse actual = super.callUsecaseExpect200(ShowEditUserResponse.class,
        UserTransportBuilder.USER2_ID);

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