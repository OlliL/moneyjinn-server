
package org.laladev.moneyjinn.server.controller.user;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.AccessRelationTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.model.AccessRelationTransport;
import org.laladev.moneyjinn.server.model.ShowEditUserResponse;
import org.springframework.http.HttpMethod;

public class ShowEditUserTest extends AbstractControllerTest {
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

  @Test
  public void test_unknownUser_emptyResponseObject() throws Exception {
    final ShowEditUserResponse expected = new ShowEditUserResponse();
    final ShowEditUserResponse actual = super.callUsecaseExpect200(
        "/" + UserTransportBuilder.NON_EXISTING_ID, this.method, ShowEditUserResponse.class);
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
    final ShowEditUserResponse actual = super.callUsecaseExpect200(
        "/" + UserTransportBuilder.USER1_ID, this.method, ShowEditUserResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_User2_completeResponseObject() throws Exception {
    final ShowEditUserResponse expected = new ShowEditUserResponse();
    final List<AccessRelationTransport> accessRelationTransports = new ArrayList<>();
    accessRelationTransports
        .add(new AccessRelationTransportBuilder().forUser2_2000_01_01().build());
    expected.setAccessRelationTransports(accessRelationTransports);
    final ShowEditUserResponse actual = super.callUsecaseExpect200(
        "/" + UserTransportBuilder.USER2_ID, this.method, ShowEditUserResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_OnlyAdminAllowed_ErrorResponse() throws Exception {
    this.userName = UserTransportBuilder.USER1_NAME;
    this.userPassword = UserTransportBuilder.USER1_PASSWORD;

    super.callUsecaseExpect403("/" + UserTransportBuilder.USER2_ID, this.method);
  }

  @Test
  public void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;

    super.callUsecaseExpect403("/" + UserTransportBuilder.USER2_ID, this.method);
  }
}