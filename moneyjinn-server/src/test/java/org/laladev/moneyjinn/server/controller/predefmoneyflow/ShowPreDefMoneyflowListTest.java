package org.laladev.moneyjinn.server.controller.predefmoneyflow;

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
import org.laladev.moneyjinn.core.rest.model.predefmoneyflow.ShowPreDefMoneyflowListResponse;
import org.laladev.moneyjinn.core.rest.model.transport.PreDefMoneyflowTransport;
import org.laladev.moneyjinn.server.builder.PreDefMoneyflowTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;

public class ShowPreDefMoneyflowListTest extends AbstractControllerTest {

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
		return super.getUsecaseFromTestClassName(this.getClass());
	}

	private ShowPreDefMoneyflowListResponse getCompleteResponse() {
		final ShowPreDefMoneyflowListResponse expected = new ShowPreDefMoneyflowListResponse();
		expected.setInitials(new HashSet<Character>(Arrays.asList('P', 'Q')));

		final List<PreDefMoneyflowTransport> preDefMoneyflowTransports = new ArrayList<>();
		preDefMoneyflowTransports.add(new PreDefMoneyflowTransportBuilder().forPreDefMoneyflow1().build());
		preDefMoneyflowTransports.add(new PreDefMoneyflowTransportBuilder().forPreDefMoneyflow3().build());
		expected.setPreDefMoneyflowTransports(preDefMoneyflowTransports);

		return expected;
	}

	@Test
	public void test_default_FullResponseObject() throws Exception {
		final ClientMaxRowsSetting setting = new ClientMaxRowsSetting(10);
		this.settingService.setClientMaxRowsSetting(new AccessID(UserTransportBuilder.USER1_ID), setting);

		final ShowPreDefMoneyflowListResponse expected = this.getCompleteResponse();

		final ShowPreDefMoneyflowListResponse actual = super.callUsecaseWithoutContent("/", this.method, false,
				ShowPreDefMoneyflowListResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_MaxRowSettingReached_OnlyInitials() throws Exception {
		final ShowPreDefMoneyflowListResponse expected = new ShowPreDefMoneyflowListResponse();
		expected.setInitials(new HashSet<Character>(Arrays.asList('P', 'Q')));

		final ClientMaxRowsSetting setting = new ClientMaxRowsSetting(1);
		this.settingService.setClientMaxRowsSetting(new AccessID(UserTransportBuilder.USER1_ID), setting);

		final ShowPreDefMoneyflowListResponse actual = super.callUsecaseWithoutContent("/", this.method, false,
				ShowPreDefMoneyflowListResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_explicitAll_FullResponseObject() throws Exception {
		final ShowPreDefMoneyflowListResponse expected = this.getCompleteResponse();

		final ClientMaxRowsSetting setting = new ClientMaxRowsSetting(1);
		this.settingService.setClientMaxRowsSetting(new AccessID(UserTransportBuilder.USER1_ID), setting);

		final ShowPreDefMoneyflowListResponse actual = super.callUsecaseWithoutContent("/all", this.method, false,
				ShowPreDefMoneyflowListResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_initialP_responseObjectcontainingP() throws Exception {
		final ShowPreDefMoneyflowListResponse expected = new ShowPreDefMoneyflowListResponse();
		expected.setInitials(new HashSet<Character>(Arrays.asList('P', 'Q')));

		final List<PreDefMoneyflowTransport> preDefMoneyflowTransports = new ArrayList<>();
		preDefMoneyflowTransports.add(new PreDefMoneyflowTransportBuilder().forPreDefMoneyflow1().build());
		expected.setPreDefMoneyflowTransports(preDefMoneyflowTransports);

		final ShowPreDefMoneyflowListResponse actual = super.callUsecaseWithoutContent("/P", this.method, false,
				ShowPreDefMoneyflowListResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_initialX_responseObjectContainingNoPreDefMoneyflow() throws Exception {
		final ShowPreDefMoneyflowListResponse expected = new ShowPreDefMoneyflowListResponse();
		expected.setInitials(new HashSet<Character>(Arrays.asList('P', 'Q')));

		final ShowPreDefMoneyflowListResponse actual = super.callUsecaseWithoutContent("/X", this.method, false,
				ShowPreDefMoneyflowListResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_AuthorizationRequired1_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("", this.method, false, ErrorResponse.class);
		Assert.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

	@Test
	public void test_AuthorizationRequired2_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("/all", this.method, false, ErrorResponse.class);
		Assert.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	public void test_emptyDatabase_noException() throws Exception {
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;

		final ShowPreDefMoneyflowListResponse expected = new ShowPreDefMoneyflowListResponse();
		final ShowPreDefMoneyflowListResponse actual = super.callUsecaseWithoutContent("/all", this.method, false,
				ShowPreDefMoneyflowListResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	@Sql("classpath:h2defaults.sql")
	public void test_emptyDatabaseExplicitLetter_noException() throws Exception {
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.userPassword = UserTransportBuilder.ADMIN_PASSWORD;

		final ShowPreDefMoneyflowListResponse expected = new ShowPreDefMoneyflowListResponse();
		final ShowPreDefMoneyflowListResponse actual = super.callUsecaseWithoutContent("/A", this.method, false,
				ShowPreDefMoneyflowListResponse.class);

		Assert.assertEquals(expected, actual);
	}

}
