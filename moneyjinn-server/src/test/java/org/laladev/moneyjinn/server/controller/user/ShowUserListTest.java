package org.laladev.moneyjinn.server.controller.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.businesslogic.service.impl.SettingService;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.transport.GroupTransport;
import org.laladev.moneyjinn.core.rest.model.transport.UserTransport;
import org.laladev.moneyjinn.core.rest.model.user.ShowUserListResponse;
import org.laladev.moneyjinn.core.rest.model.user.transport.AccessRelationTransport;
import org.laladev.moneyjinn.model.access.AccessID;
import org.laladev.moneyjinn.model.setting.ClientMaxRowsSetting;
import org.laladev.moneyjinn.server.builder.AccessRelationTransportBuilder;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;

public class ShowUserListTest extends AbstractControllerTest {

	@Inject
	private SettingService settingService;

	private final HttpMethod method = HttpMethod.GET;
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
		return super.getUsecaseFromTestClassName(this.getClass());
	}

	private ShowUserListResponse getCompleteResponse() {
		final ShowUserListResponse expected = new ShowUserListResponse();
		expected.setInitials(new HashSet<Character>(Arrays.asList('A', 'U')));

		final List<UserTransport> userTransports = new ArrayList<>();
		userTransports.add(new UserTransportBuilder().forAdmin().build());
		userTransports.add(new UserTransportBuilder().forUser1().build());
		userTransports.add(new UserTransportBuilder().forUser2().build());
		userTransports.add(new UserTransportBuilder().forUser3().build());
		expected.setUserTransports(userTransports);

		final List<GroupTransport> groupTransports = new ArrayList<>();
		groupTransports.add(new GroupTransportBuilder().forAdminGroup().build());
		groupTransports.add(new GroupTransportBuilder().forGroup1().build());
		expected.setGroupTransports(groupTransports);

		final List<AccessRelationTransport> accessRelationTransports = new ArrayList<>();
		accessRelationTransports.add(new AccessRelationTransportBuilder().forAdminUser().build());
		accessRelationTransports.add(new AccessRelationTransportBuilder().forUser1_2000_01_01().build());
		accessRelationTransports.add(new AccessRelationTransportBuilder().forUser2_2000_01_01().build());
		accessRelationTransports.add(new AccessRelationTransportBuilder().forUser3_2000_01_01().build());
		expected.setAccessRelationTransports(accessRelationTransports);
		return expected;
	}

	@Test
	public void test_default_FullResponseObject() throws Exception {
		final ShowUserListResponse expected = this.getCompleteResponse();

		final ShowUserListResponse actual = super.callUsecaseWithoutContent("/", this.method, false,
				ShowUserListResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_MaxRowSettingReached_OnlyInitials() throws Exception {
		final ShowUserListResponse expected = new ShowUserListResponse();
		expected.setInitials(new HashSet<Character>(Arrays.asList('A', 'U')));

		final ClientMaxRowsSetting setting = new ClientMaxRowsSetting(1);
		this.settingService.setClientMaxRowsSetting(new AccessID(UserTransportBuilder.ADMIN_ID), setting);

		final ShowUserListResponse actual = super.callUsecaseWithoutContent("/", this.method, false,
				ShowUserListResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_explicitAll_FullResponseObject() throws Exception {
		final ShowUserListResponse expected = this.getCompleteResponse();

		final ClientMaxRowsSetting setting = new ClientMaxRowsSetting(1);
		this.settingService.setClientMaxRowsSetting(new AccessID(UserTransportBuilder.ADMIN_ID), setting);

		final ShowUserListResponse actual = super.callUsecaseWithoutContent("/all", this.method, false,
				ShowUserListResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_initialA_AResponseObject() throws Exception {
		final ShowUserListResponse expected = new ShowUserListResponse();
		expected.setInitials(new HashSet<Character>(Arrays.asList('A', 'U')));

		final List<UserTransport> userTransports = new ArrayList<>();
		userTransports.add(new UserTransportBuilder().forAdmin().build());
		expected.setUserTransports(userTransports);

		final List<GroupTransport> groupTransports = new ArrayList<>();
		groupTransports.add(new GroupTransportBuilder().forAdminGroup().build());
		expected.setGroupTransports(groupTransports);

		final List<AccessRelationTransport> accessRelationTransports = new ArrayList<>();
		accessRelationTransports.add(new AccessRelationTransportBuilder().forAdminUser().build());
		expected.setAccessRelationTransports(accessRelationTransports);

		final ShowUserListResponse actual = super.callUsecaseWithoutContent("/A", this.method, false,
				ShowUserListResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_OnlyAdminAllowed_ErrorResponse() throws Exception {
		this.userName = UserTransportBuilder.USER1_NAME;
		this.userPassword = UserTransportBuilder.USER1_PASSWORD;

		final ErrorResponse actual = super.callUsecaseWithoutContent("/", this.method, false, ErrorResponse.class);

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
