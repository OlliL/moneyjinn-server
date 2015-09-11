package org.laladev.moneyjinn.server.controller.group;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.businesslogic.model.ErrorCode;
import org.laladev.moneyjinn.businesslogic.model.access.AccessID;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientMaxRowsSetting;
import org.laladev.moneyjinn.businesslogic.service.impl.SettingService;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.group.ShowGroupListResponse;
import org.laladev.moneyjinn.core.rest.model.transport.GroupTransport;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;

public class ShowGroupListTest extends AbstractControllerTest {

	@Inject
	private SettingService settingService;

	private final HttpMethod method = HttpMethod.GET;
	private String userName;
	private String groupPassword;

	@Before
	public void setUp() {
		this.userName = UserTransportBuilder.ADMIN_NAME;
		this.groupPassword = UserTransportBuilder.ADMIN_PASSWORD;
	}

	@Override
	protected String getUsername() {
		return this.userName;
	}

	@Override
	protected String getPassword() {
		return this.groupPassword;
	}

	@Override
	protected String getUsecase() {
		return super.getUsecaseFromTestClassName("group", this.getClass());
	}

	private ShowGroupListResponse getCompleteResponse() {
		final ShowGroupListResponse expected = new ShowGroupListResponse();
		expected.setInitials(new HashSet<Character>(Arrays.asList('A', 'G')));

		final List<GroupTransport> groupTransports = new ArrayList<>();
		groupTransports.add(new GroupTransportBuilder().forAdminGroup().build());
		groupTransports.add(new GroupTransportBuilder().forGroup1().build());
		groupTransports.add(new GroupTransportBuilder().forGroup2().build());
		groupTransports.add(new GroupTransportBuilder().forGroup3().build());
		expected.setGroupTransports(groupTransports);

		return expected;
	}

	@Test
	public void test_default_FullResponseObject() throws Exception {
		final ShowGroupListResponse expected = this.getCompleteResponse();

		final ShowGroupListResponse actual = super.callUsecaseWithoutContent("/", this.method, false,
				ShowGroupListResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_MaxRowSettingReached_OnlyInitials() throws Exception {
		final ShowGroupListResponse expected = new ShowGroupListResponse();
		expected.setInitials(new HashSet<Character>(Arrays.asList('A', 'G')));

		final ClientMaxRowsSetting setting = new ClientMaxRowsSetting(1);
		this.settingService.setClientMaxRowsSetting(new AccessID(UserTransportBuilder.ADMIN_ID), setting);

		final ShowGroupListResponse actual = super.callUsecaseWithoutContent("/", this.method, false,
				ShowGroupListResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_explicitAll_FullResponseObject() throws Exception {
		final ShowGroupListResponse expected = this.getCompleteResponse();

		final ClientMaxRowsSetting setting = new ClientMaxRowsSetting(1);
		this.settingService.setClientMaxRowsSetting(new AccessID(UserTransportBuilder.ADMIN_ID), setting);

		final ShowGroupListResponse actual = super.callUsecaseWithoutContent("/all", this.method, false,
				ShowGroupListResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_initialA_AResponseObject() throws Exception {
		final ShowGroupListResponse expected = new ShowGroupListResponse();
		expected.setInitials(new HashSet<Character>(Arrays.asList('A', 'G')));

		final List<GroupTransport> groupTransports = new ArrayList<>();
		groupTransports.add(new GroupTransportBuilder().forAdminGroup().build());
		expected.setGroupTransports(groupTransports);

		final ShowGroupListResponse actual = super.callUsecaseWithoutContent("/A", this.method, false,
				ShowGroupListResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_OnlyAdminAllowed_ErrorResponse() throws Exception {
		this.userName = UserTransportBuilder.USER1_NAME;
		this.groupPassword = UserTransportBuilder.USER1_PASSWORD;

		final ErrorResponse actual = super.callUsecaseWithoutContent("/", this.method, false, ErrorResponse.class);

		Assert.assertEquals(new Integer(ErrorCode.USER_IS_NO_ADMIN.getErrorCode()), actual.getCode());

	}

}
