package org.laladev.moneyjinn.server.controller.setting;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.businesslogic.model.ErrorCode;
import org.laladev.moneyjinn.businesslogic.model.access.AccessID;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientDateFormatSetting;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientDisplayedLanguageSetting;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientMaxRowsSetting;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientNumFreeMoneyflowsSetting;
import org.laladev.moneyjinn.businesslogic.service.api.ISettingService;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.setting.UpdateDefaultSettingsRequest;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;

public class UpdateDefaultSettingsTest extends AbstractControllerTest {

	@Inject
	private ISettingService settingService;
	private final HttpMethod method = HttpMethod.PUT;
	private String userName;
	private String userPassword;

	@Before
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
		return super.getUsecaseFromTestClassName("setting", this.getClass());
	}

	@Test
	public void test_standardRequest_regularResponse() throws Exception {
		final UpdateDefaultSettingsRequest request = new UpdateDefaultSettingsRequest();
		request.setDateFormat("YYYYMMDD");
		request.setLanguage(2);
		request.setMaxRows(10);
		request.setNumFreeMoneyflows(20);
		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final AccessID accessId = new AccessID(0l);

		final ClientDisplayedLanguageSetting clientDisplayedLanguageSetting = this.settingService
				.getClientDisplayedLanguageSetting(accessId);
		final ClientDateFormatSetting clientDateFormatSetting = this.settingService
				.getClientDateFormatSetting(accessId);
		final ClientMaxRowsSetting clientMaxRowsSetting = this.settingService.getClientMaxRowsSetting(accessId);
		final ClientNumFreeMoneyflowsSetting clientNumFreeMoneyflowsSetting = this.settingService
				.getClientNumFreeMoneyflowsSetting(accessId);

		Assert.assertEquals(new Integer(2), clientDisplayedLanguageSetting.getSetting());
		Assert.assertEquals("YYYYMMDD", clientDateFormatSetting.getSetting());
		Assert.assertEquals(new Integer(10), clientMaxRowsSetting.getSetting());
		Assert.assertEquals(new Integer(20), clientNumFreeMoneyflowsSetting.getSetting());
	}

	@Test
	public void test_OnlyAdminAllowed_ErrorResponse() throws Exception {
		this.userName = UserTransportBuilder.USER1_NAME;
		this.userPassword = UserTransportBuilder.USER1_PASSWORD;

		final UpdateDefaultSettingsRequest request = new UpdateDefaultSettingsRequest();
		final ErrorResponse actual = super.callUsecaseWithContent("", this.method, request, false, ErrorResponse.class);

		Assert.assertEquals(new Integer(ErrorCode.USER_IS_NO_ADMIN.getErrorCode()), actual.getCode());

	}

	@Test
	public void test_AuthorizationRequired_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("", this.method, false, ErrorResponse.class);
		Assert.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

}