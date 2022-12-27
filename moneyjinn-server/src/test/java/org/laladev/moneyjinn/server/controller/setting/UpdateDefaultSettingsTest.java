
package org.laladev.moneyjinn.server.controller.setting;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.setting.UpdateDefaultSettingsRequest;
import org.laladev.moneyjinn.model.access.AccessID;
import org.laladev.moneyjinn.model.setting.ClientDateFormatSetting;
import org.laladev.moneyjinn.model.setting.ClientDisplayedLanguageSetting;
import org.laladev.moneyjinn.model.setting.ClientMaxRowsSetting;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.ISettingService;
import org.springframework.http.HttpMethod;

public class UpdateDefaultSettingsTest extends AbstractControllerTest {
  @Inject
  private ISettingService settingService;
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

  @Test
  public void test_standardRequest_regularResponse() throws Exception {
    final UpdateDefaultSettingsRequest request = new UpdateDefaultSettingsRequest();
    request.setDateFormat("YYYYMMDD");
    request.setLanguage(2);
    request.setMaxRows(10);
    super.callUsecaseWithContent("", this.method, request, true, Object.class);
    final AccessID accessId = new AccessID(0l);
    final ClientDisplayedLanguageSetting clientDisplayedLanguageSetting = this.settingService
        .getClientDisplayedLanguageSetting(accessId);
    final ClientDateFormatSetting clientDateFormatSetting = this.settingService
        .getClientDateFormatSetting(accessId);
    final ClientMaxRowsSetting clientMaxRowsSetting = this.settingService
        .getClientMaxRowsSetting(accessId);
    Assertions.assertEquals(Integer.valueOf(2), clientDisplayedLanguageSetting.getSetting());
    Assertions.assertEquals("YYYYMMDD", clientDateFormatSetting.getSetting());
    Assertions.assertEquals(Integer.valueOf(10), clientMaxRowsSetting.getSetting());
  }

  @Test
  public void test_OnlyAdminAllowed_ErrorResponse() throws Exception {
    this.userName = UserTransportBuilder.USER1_NAME;
    this.userPassword = UserTransportBuilder.USER1_PASSWORD;
    final UpdateDefaultSettingsRequest request = new UpdateDefaultSettingsRequest();
    final ErrorResponse actual = super.callUsecaseWithContent("", this.method, request, false,
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

  }
}