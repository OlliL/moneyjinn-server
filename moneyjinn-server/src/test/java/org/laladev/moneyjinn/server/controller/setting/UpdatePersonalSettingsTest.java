package org.laladev.moneyjinn.server.controller.setting;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.setting.UpdatePersonalSettingsRequest;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserAttribute;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.setting.ClientDateFormatSetting;
import org.laladev.moneyjinn.model.setting.ClientDisplayedLanguageSetting;
import org.laladev.moneyjinn.model.setting.ClientMaxRowsSetting;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.ISettingService;
import org.laladev.moneyjinn.service.api.IUserService;
import org.springframework.http.HttpMethod;

public class UpdatePersonalSettingsTest extends AbstractControllerTest {

	@Inject
	private ISettingService settingService;
	@Inject
	private IUserService userService;

	private final HttpMethod method = HttpMethod.PUT;
	private String userName;
	private String userPassword;

	@Before
	public void setUp() {
		this.userName = UserTransportBuilder.USER1_NAME;
		this.userPassword = UserTransportBuilder.USER1_PASSWORD;
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
	public void test_standardRequestNoPassword_regularResponse() throws Exception {
		final UpdatePersonalSettingsRequest request = new UpdatePersonalSettingsRequest();
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
		request.setDateFormat("YYYYMMDD");
		request.setLanguage(2);
		request.setMaxRows(10);

		// Set password so the "new user" flag is reset
		final UserID accessId = new UserID(UserTransportBuilder.ADMIN_ID);
		this.userService.setPassword(accessId, UserTransportBuilder.ADMIN_PASSWORD);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final ClientDisplayedLanguageSetting clientDisplayedLanguageSetting = this.settingService
				.getClientDisplayedLanguageSetting(accessId);
		final ClientDateFormatSetting clientDateFormatSetting = this.settingService
				.getClientDateFormatSetting(accessId);
		final ClientMaxRowsSetting clientMaxRowsSetting = this.settingService.getClientMaxRowsSetting(accessId);

		Assert.assertEquals(new Integer(2), clientDisplayedLanguageSetting.getSetting());
		Assert.assertEquals("YYYYMMDD", clientDateFormatSetting.getSetting());
		Assert.assertEquals(new Integer(10), clientMaxRowsSetting.getSetting());

		final User user = this.userService.getUserById(new UserID(accessId.getId()));
		Assert.assertEquals(UserTransportBuilder.ADMIN_PASSWORD_SHA1, user.getPassword());
	}

	@Test
	public void test_standardRequestEmptyPassword_regularResponse() throws Exception {
		final UpdatePersonalSettingsRequest request = new UpdatePersonalSettingsRequest();
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
		request.setDateFormat("YYYYMMDD");
		request.setLanguage(2);
		request.setMaxRows(10);
		request.setPassword("");

		// Set password so the "new user" flag is reset
		final UserID accessId = new UserID(UserTransportBuilder.ADMIN_ID);
		this.userService.setPassword(accessId, UserTransportBuilder.ADMIN_PASSWORD);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final ClientDisplayedLanguageSetting clientDisplayedLanguageSetting = this.settingService
				.getClientDisplayedLanguageSetting(accessId);
		final ClientDateFormatSetting clientDateFormatSetting = this.settingService
				.getClientDateFormatSetting(accessId);
		final ClientMaxRowsSetting clientMaxRowsSetting = this.settingService.getClientMaxRowsSetting(accessId);

		Assert.assertEquals(new Integer(2), clientDisplayedLanguageSetting.getSetting());
		Assert.assertEquals("YYYYMMDD", clientDateFormatSetting.getSetting());
		Assert.assertEquals(new Integer(10), clientMaxRowsSetting.getSetting());

		final User user = this.userService.getUserById(new UserID(accessId.getId()));
		Assert.assertEquals(UserTransportBuilder.ADMIN_PASSWORD_SHA1, user.getPassword());
	}

	@Test
	public void test_standardRequestWithPassword_regularResponse() throws Exception {
		final UpdatePersonalSettingsRequest request = new UpdatePersonalSettingsRequest();
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;
		request.setDateFormat("YYYYMMDD");
		request.setLanguage(2);
		request.setMaxRows(10);
		request.setPassword(UserTransportBuilder.USER2_PASSWORD);

		// Set password so the "new user" flag is reset
		final UserID accessId = new UserID(UserTransportBuilder.ADMIN_ID);
		this.userService.setPassword(accessId, UserTransportBuilder.ADMIN_PASSWORD);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final ClientDisplayedLanguageSetting clientDisplayedLanguageSetting = this.settingService
				.getClientDisplayedLanguageSetting(accessId);
		final ClientDateFormatSetting clientDateFormatSetting = this.settingService
				.getClientDateFormatSetting(accessId);
		final ClientMaxRowsSetting clientMaxRowsSetting = this.settingService.getClientMaxRowsSetting(accessId);

		Assert.assertEquals(new Integer(2), clientDisplayedLanguageSetting.getSetting());
		Assert.assertEquals("YYYYMMDD", clientDateFormatSetting.getSetting());
		Assert.assertEquals(new Integer(10), clientMaxRowsSetting.getSetting());

		final User user = this.userService.getUserById(new UserID(accessId.getId()));
		Assert.assertEquals(UserTransportBuilder.USER2_PASSWORD_SHA1, user.getPassword());
	}

	@Test
	public void test_newUserWithoutPassword_errorResponse() throws Exception {
		final UpdatePersonalSettingsRequest request = new UpdatePersonalSettingsRequest();
		final ErrorResponse response = super.callUsecaseWithContent("", this.method, request, false,
				ErrorResponse.class);

		Assert.assertEquals(new Integer(ErrorCode.PASSWORD_MUST_BE_CHANGED.getErrorCode()), response.getCode());
	}

	@Test
	public void test_newUserWithPassword_regularResponse() throws Exception {
		final UpdatePersonalSettingsRequest request = new UpdatePersonalSettingsRequest();
		request.setDateFormat("YYYYMMDD");
		request.setLanguage(2);
		request.setMaxRows(10);
		request.setPassword(UserTransportBuilder.USER2_PASSWORD);
		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final UserID accessId = new UserID(UserTransportBuilder.USER1_ID);

		final ClientDisplayedLanguageSetting clientDisplayedLanguageSetting = this.settingService
				.getClientDisplayedLanguageSetting(accessId);
		final ClientDateFormatSetting clientDateFormatSetting = this.settingService
				.getClientDateFormatSetting(accessId);
		final ClientMaxRowsSetting clientMaxRowsSetting = this.settingService.getClientMaxRowsSetting(accessId);

		Assert.assertEquals(new Integer(2), clientDisplayedLanguageSetting.getSetting());
		Assert.assertEquals("YYYYMMDD", clientDateFormatSetting.getSetting());
		Assert.assertEquals(new Integer(10), clientMaxRowsSetting.getSetting());

		final User user = this.userService.getUserById(accessId);
		Assert.assertEquals(UserTransportBuilder.USER2_PASSWORD_SHA1, user.getPassword());
		Assert.assertTrue(!user.getAttributes().contains(UserAttribute.IS_NEW));
	}

	@Test
	public void test_AuthorizationRequired_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("", this.method, false, ErrorResponse.class);
		Assert.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

}