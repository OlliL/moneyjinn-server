package org.laladev.moneyjinn.server.controller.setting;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.core.rest.model.setting.ShowPersonalSettingsResponse;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;

public class ShowPersonalSettingsTest extends AbstractControllerTest {

	private final HttpMethod method = HttpMethod.GET;
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
		return super.getUsecaseFromTestClassName("setting", this.getClass());
	}

	@Test
	public void test_standardRequest_regularResponse() throws Exception {
		final ShowPersonalSettingsResponse expected = new ShowPersonalSettingsResponse();
		expected.setDateFormat("YYYY-MM-DD");
		expected.setLanguage(1);
		expected.setMaxRows(1);
		expected.setNumFreeMoneyflows(1);
		final ShowPersonalSettingsResponse actual = super.callUsecaseWithoutContent("", this.method, false,
				ShowPersonalSettingsResponse.class);

		Assert.assertEquals(expected, actual);
	}

}