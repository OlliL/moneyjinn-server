package org.laladev.moneyjinn.server.controller.capitalsource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.businesslogic.model.access.AccessID;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientMaxRowsSetting;
import org.laladev.moneyjinn.businesslogic.service.impl.SettingService;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
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
		expected.setInitials(new HashSet<Character>(Arrays.asList('A', 'S', 'X')));

		final List<CapitalsourceTransport> capitalsourceTransports = new ArrayList<>();
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource1().build());
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource2().build());
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource3().build());
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource4().build());
		expected.setCapitalsourceTransports(capitalsourceTransports);
		expected.setCurrentlyValid(false);

		return expected;
	}

	private ShowCapitalsourceListResponse getCurrentlyValidResponse() {
		final ShowCapitalsourceListResponse expected = new ShowCapitalsourceListResponse();
		expected.setInitials(new HashSet<Character>(Arrays.asList('A', 'S')));

		final List<CapitalsourceTransport> capitalsourceTransports = new ArrayList<>();
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource1().build());
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource2().build());
		expected.setCapitalsourceTransports(capitalsourceTransports);
		expected.setCurrentlyValid(true);

		return expected;
	}

	@Test
	public void test_default_FullResponseObject() throws Exception {
		final ClientMaxRowsSetting setting = new ClientMaxRowsSetting(10);
		this.settingService.setClientMaxRowsSetting(new AccessID(UserTransportBuilder.USER1_ID), setting);

		// set default to 0
		ShowCapitalsourceListResponse expected = this.getCompleteResponse();
		ShowCapitalsourceListResponse actual = super.callUsecaseWithoutContent("/currentlyValid/0", this.method, false,
				ShowCapitalsourceListResponse.class);
		Assert.assertEquals(expected, actual);

		// now the new default 0 must be taken
		actual = super.callUsecaseWithoutContent("/currentlyValid/", this.method, false,
				ShowCapitalsourceListResponse.class);
		Assert.assertEquals(expected, actual);

		// this must change the default-setting to 1
		expected = this.getCurrentlyValidResponse();
		actual = super.callUsecaseWithoutContent("/currentlyValid/1", this.method, false,
				ShowCapitalsourceListResponse.class);
		Assert.assertEquals(expected, actual);

		// now the default 1 must be taken
		actual = super.callUsecaseWithoutContent("/currentlyValid/", this.method, false,
				ShowCapitalsourceListResponse.class);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_MaxRowSettingReached_OnlyInitials() throws Exception {
		final ShowCapitalsourceListResponse expected = new ShowCapitalsourceListResponse();
		expected.setInitials(new HashSet<Character>(Arrays.asList('A', 'S', 'X')));

		final ClientMaxRowsSetting setting = new ClientMaxRowsSetting(1);
		this.settingService.setClientMaxRowsSetting(new AccessID(UserTransportBuilder.USER1_ID), setting);

		final ShowCapitalsourceListResponse actual = super.callUsecaseWithoutContent("/currentlyValid/0", this.method,
				false, ShowCapitalsourceListResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_explicitAll_FullResponseObject() throws Exception {
		final ClientMaxRowsSetting setting = new ClientMaxRowsSetting(1);
		this.settingService.setClientMaxRowsSetting(new AccessID(UserTransportBuilder.USER1_ID), setting);

		// set default to 0
		ShowCapitalsourceListResponse expected = this.getCompleteResponse();
		ShowCapitalsourceListResponse actual = super.callUsecaseWithoutContent("/all/currentlyValid/0", this.method,
				false, ShowCapitalsourceListResponse.class);
		Assert.assertEquals(expected, actual);

		// now the new default 0 must be taken
		actual = super.callUsecaseWithoutContent("/all/currentlyValid/", this.method, false,
				ShowCapitalsourceListResponse.class);
		Assert.assertEquals(expected, actual);

		// this must change the default-setting to 1
		expected = this.getCurrentlyValidResponse();
		actual = super.callUsecaseWithoutContent("/all/currentlyValid/1", this.method, false,
				ShowCapitalsourceListResponse.class);
		Assert.assertEquals(expected, actual);

		// now the default 1 must be taken
		actual = super.callUsecaseWithoutContent("/all/currentlyValid/", this.method, false,
				ShowCapitalsourceListResponse.class);
		Assert.assertEquals(expected, actual);

	}

	@Test
	public void test_initialS_AResponseObject() throws Exception {
		// set default to 0
		ShowCapitalsourceListResponse expected = new ShowCapitalsourceListResponse();
		expected.setInitials(new HashSet<Character>(Arrays.asList('A', 'S', 'X')));
		List<CapitalsourceTransport> capitalsourceTransports = new ArrayList<>();
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource2().build());
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource3().build());
		expected.setCapitalsourceTransports(capitalsourceTransports);
		expected.setCurrentlyValid(false);
		ShowCapitalsourceListResponse actual = super.callUsecaseWithoutContent("/S/currentlyValid/0", this.method,
				false, ShowCapitalsourceListResponse.class);
		Assert.assertEquals(expected, actual);

		// now the new default 0 must be taken
		actual = super.callUsecaseWithoutContent("/S/currentlyValid/", this.method, false,
				ShowCapitalsourceListResponse.class);
		Assert.assertEquals(expected, actual);

		// this must change the default-setting to 1
		expected = new ShowCapitalsourceListResponse();
		expected.setInitials(new HashSet<Character>(Arrays.asList('A', 'S')));
		capitalsourceTransports = new ArrayList<>();
		capitalsourceTransports.add(new CapitalsourceTransportBuilder().forCapitalsource2().build());
		expected.setCapitalsourceTransports(capitalsourceTransports);
		expected.setCurrentlyValid(true);
		actual = super.callUsecaseWithoutContent("/S/currentlyValid/1", this.method, false,
				ShowCapitalsourceListResponse.class);
		Assert.assertEquals(expected, actual);

		// now the default 1 must be taken
		actual = super.callUsecaseWithoutContent("/S/currentlyValid/", this.method, false,
				ShowCapitalsourceListResponse.class);
		Assert.assertEquals(expected, actual);

	}

	@Test
	public void test_AuthorizationRequired1_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("//currentlyValid/", this.method, false,
				ErrorResponse.class);
		Assert.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

	@Test
	public void test_AuthorizationRequired2_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("/all/currentlyValid/", this.method, false,
				ErrorResponse.class);
		Assert.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

	@Test
	public void test_AuthorizationRequired3_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("//currentlyValid/0", this.method, false,
				ErrorResponse.class);
		Assert.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

	@Test
	public void test_AuthorizationRequired4_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("/all/currentlyValid/0", this.method, false,
				ErrorResponse.class);
		Assert.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

}
