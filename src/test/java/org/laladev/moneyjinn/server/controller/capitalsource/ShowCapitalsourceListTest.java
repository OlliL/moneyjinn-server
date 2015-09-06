package org.laladev.moneyjinn.server.controller.capitalsource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.businesslogic.model.access.AccessID;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientMaxRowsSetting;
import org.laladev.moneyjinn.businesslogic.service.impl.SettingService;
import org.laladev.moneyjinn.core.rest.model.capitalsource.ShowCapitalsourceListResponse;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;
import org.laladev.moneyjinn.server.builder.CapitalsourceTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;

public class ShowCapitalsourceListTest extends AbstractControllerTest {

	@Inject
	private SettingService settingService;

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
		return super.getUsecaseFromTestClassName("capitalsource", this.getClass());
	}

	private ShowCapitalsourceListResponse getCompleteResponse() {
		final ShowCapitalsourceListResponse expected = new ShowCapitalsourceListResponse();
		expected.setInitials(Arrays.asList('S'));

		final List<CapitalsourceTransport> capitalsourceTransports = new ArrayList<>();
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource1().build());
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource2().build());
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource3().build());
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource4().build());
		expected.setCapitalsourceTransports(capitalsourceTransports);

		return expected;
	}

	@Test
	public void test_default_FullResponseObject() throws Exception {
		final ShowCapitalsourceListResponse expected = this.getCompleteResponse();

		final ClientMaxRowsSetting setting = new ClientMaxRowsSetting(10);
		this.settingService.setClientMaxRowsSetting(new AccessID(UserTransportBuilder.USER1_ID), setting);

		final ShowCapitalsourceListResponse actual = super.callUsecaseWithoutContent("/currentlyValid/0", this.method,
				false, ShowCapitalsourceListResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_MaxRowSettingReached_OnlyInitials() throws Exception {
		final ShowCapitalsourceListResponse expected = new ShowCapitalsourceListResponse();
		expected.setInitials(Arrays.asList('S'));

		final ClientMaxRowsSetting setting = new ClientMaxRowsSetting(1);
		this.settingService.setClientMaxRowsSetting(new AccessID(UserTransportBuilder.USER1_ID), setting);

		final ShowCapitalsourceListResponse actual = super.callUsecaseWithoutContent("/currentlyValid/0", this.method,
				false, ShowCapitalsourceListResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_explicitAll_FullResponseObject() throws Exception {
		final ShowCapitalsourceListResponse expected = this.getCompleteResponse();

		final ClientMaxRowsSetting setting = new ClientMaxRowsSetting(1);
		this.settingService.setClientMaxRowsSetting(new AccessID(UserTransportBuilder.USER1_ID), setting);

		final ShowCapitalsourceListResponse actual = super.callUsecaseWithoutContent("/all/currentlyValid/0",
				this.method, false, ShowCapitalsourceListResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_initialS_AResponseObject() throws Exception {
		final ShowCapitalsourceListResponse expected = new ShowCapitalsourceListResponse();
		expected.setInitials(Arrays.asList('S'));

		final List<CapitalsourceTransport> capitalsourceTransports = new ArrayList<>();
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource1().build());
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource2().build());
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource3().build());
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource4().build());
		expected.setCapitalsourceTransports(capitalsourceTransports);

		final ShowCapitalsourceListResponse actual = super.callUsecaseWithoutContent("/S/currentlyValid/0", this.method,
				false, ShowCapitalsourceListResponse.class);

		Assert.assertEquals(expected, actual);
	}

}
