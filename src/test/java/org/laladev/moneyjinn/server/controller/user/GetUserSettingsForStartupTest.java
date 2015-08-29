package org.laladev.moneyjinn.server.controller.user;

import org.junit.Assert;
import org.junit.Test;
import org.laladev.moneyjinn.core.rest.model.user.GetUserSettingsForStartupResponse;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;

public class GetUserSettingsForStartupTest extends AbstractControllerTest {

	@Override
	protected String getUsecase() {
		return super.getUsecaseFromTestClassName("user", this.getClass());
	}

	@Test
	public void test_unknownUser_emptyResponseObject() throws Exception {
		final GetUserSettingsForStartupResponse expected = new GetUserSettingsForStartupResponse();
		final GetUserSettingsForStartupResponse actual = super.callUsecaseWithGET("/xxx",
				GetUserSettingsForStartupResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_User1_completeResponseObject() throws Exception {
		final GetUserSettingsForStartupResponse expected = new GetUserSettingsForStartupResponse();
		expected.setAttributeNew(Boolean.TRUE);
		expected.setPermissionAdmin(Boolean.TRUE);
		expected.setSettingDateFormat("YYYY-MM-DD");
		expected.setSettingDisplayedLanguage(1);
		expected.setUserId(3l);

		final GetUserSettingsForStartupResponse actual = super.callUsecaseWithGET("/" + UserTransportBuilder.USER1_NAME,
				GetUserSettingsForStartupResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_User2_completeResponseObject() throws Exception {
		final GetUserSettingsForStartupResponse expected = new GetUserSettingsForStartupResponse();
		expected.setAttributeNew(Boolean.FALSE);
		expected.setPermissionAdmin(Boolean.FALSE);
		expected.setSettingDateFormat("YYYY-MM-DD");
		expected.setSettingDisplayedLanguage(1);
		expected.setUserId(4l);

		final GetUserSettingsForStartupResponse actual = super.callUsecaseWithGET("/" + UserTransportBuilder.USER2_NAME,
				GetUserSettingsForStartupResponse.class);

		Assert.assertEquals(expected, actual);
	}
}