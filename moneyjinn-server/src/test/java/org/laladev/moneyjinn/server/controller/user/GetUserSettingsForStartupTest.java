
package org.laladev.moneyjinn.server.controller.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.user.GetUserSettingsForStartupResponse;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;

public class GetUserSettingsForStartupTest extends AbstractControllerTest {
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
    final GetUserSettingsForStartupResponse expected = new GetUserSettingsForStartupResponse();
    final GetUserSettingsForStartupResponse actual = super.callUsecaseWithoutContent("/xxx",
        this.method, false, GetUserSettingsForStartupResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_AdminUser_completeResponseObject() throws Exception {
    final GetUserSettingsForStartupResponse expected = new GetUserSettingsForStartupResponse();
    expected.setAttributeNew(Boolean.TRUE);
    expected.setPermissionAdmin(Boolean.TRUE);
    expected.setSettingDateFormat("YYYY-MM-DD");
    expected.setSettingDisplayedLanguage(1);
    expected.setUserId(UserTransportBuilder.ADMIN_ID);
    final GetUserSettingsForStartupResponse actual = super.callUsecaseWithoutContent(
        "/" + UserTransportBuilder.ADMIN_NAME, this.method, false,
        GetUserSettingsForStartupResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_User1_completeResponseObject() throws Exception {
    final GetUserSettingsForStartupResponse expected = new GetUserSettingsForStartupResponse();
    expected.setAttributeNew(Boolean.TRUE);
    expected.setPermissionAdmin(Boolean.FALSE);
    expected.setSettingDateFormat("YYYY-MM-DD");
    expected.setSettingDisplayedLanguage(1);
    expected.setUserId(UserTransportBuilder.USER1_ID);
    final GetUserSettingsForStartupResponse actual = super.callUsecaseWithoutContent(
        "/" + UserTransportBuilder.USER1_NAME, this.method, false,
        GetUserSettingsForStartupResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_User2_completeResponseObject() throws Exception {
    final GetUserSettingsForStartupResponse expected = new GetUserSettingsForStartupResponse();
    expected.setAttributeNew(Boolean.FALSE);
    expected.setPermissionAdmin(Boolean.FALSE);
    expected.setSettingDateFormat("YYYY-MM-DD");
    expected.setSettingDisplayedLanguage(1);
    expected.setUserId(UserTransportBuilder.USER2_ID);
    final GetUserSettingsForStartupResponse actual = super.callUsecaseWithoutContent(
        "/" + UserTransportBuilder.USER2_NAME, this.method, false,
        GetUserSettingsForStartupResponse.class);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void test_AuthorizationRequired_Error() throws Exception {
    this.userName = null;
    this.userPassword = null;
    super.callUsecaseExpect403("/1", this.method);
  }

  @Test
  public void test_Options_EmptyResponse() throws Exception {
    this.userName = null;
    this.userPassword = null;
    super.callUsecaseWithoutContent("/" + UserTransportBuilder.USER1_NAME, HttpMethod.OPTIONS, true,
        GetUserSettingsForStartupResponse.class);
  }
}