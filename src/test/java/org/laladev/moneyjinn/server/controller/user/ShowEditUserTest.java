package org.laladev.moneyjinn.server.controller.user;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.businesslogic.model.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.transport.AccessRelationTransport;
import org.laladev.moneyjinn.core.rest.model.transport.GroupTransport;
import org.laladev.moneyjinn.core.rest.model.user.ShowEditUserResponse;
import org.laladev.moneyjinn.server.builder.AccessRelationTransportBuilder;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;

public class ShowEditUserTest extends AbstractControllerTest {

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
		return super.getUsecaseFromTestClassName("user", this.getClass());
	}

	@Test
	public void test_unknownUser_emptyResponseObject() throws Exception {
		final ShowEditUserResponse expected = new ShowEditUserResponse();
		final ShowEditUserResponse actual = super.callUsecaseWithoutContent("/" + UserTransportBuilder.NON_EXISTING_ID,
				this.method, false, ShowEditUserResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_User1_completeResponseObject() throws Exception {
		final ShowEditUserResponse expected = new ShowEditUserResponse();
		expected.setUserTransport(new UserTransportBuilder().forUser1().build());
		final List<GroupTransport> groupTransports = new ArrayList<>();
		groupTransports.add(new GroupTransportBuilder().forAdminGroup().build());
		groupTransports.add(new GroupTransportBuilder().forGroup1().build());
		groupTransports.add(new GroupTransportBuilder().forGroup2().build());
		expected.setGroupTransports(groupTransports);
		final List<AccessRelationTransport> accessRelationTransports = new ArrayList<>();
		accessRelationTransports.add(new AccessRelationTransportBuilder().forUser1_2000_01_01().build());
		accessRelationTransports.add(new AccessRelationTransportBuilder().forUser1_2600_01_01().build());
		accessRelationTransports.add(new AccessRelationTransportBuilder().forUser1_2700_01_01().build());
		accessRelationTransports.add(new AccessRelationTransportBuilder().forUser1_2800_01_01().build());
		expected.setAccessRelationTransports(accessRelationTransports);

		final ShowEditUserResponse actual = super.callUsecaseWithoutContent("/" + UserTransportBuilder.USER1_ID,
				this.method, false, ShowEditUserResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_User2_completeResponseObject() throws Exception {
		final ShowEditUserResponse expected = new ShowEditUserResponse();
		expected.setUserTransport(new UserTransportBuilder().forUser2().build());
		final List<GroupTransport> groupTransports = new ArrayList<>();
		groupTransports.add(new GroupTransportBuilder().forAdminGroup().build());
		groupTransports.add(new GroupTransportBuilder().forGroup1().build());
		groupTransports.add(new GroupTransportBuilder().forGroup2().build());
		expected.setGroupTransports(groupTransports);
		final List<AccessRelationTransport> accessRelationTransports = new ArrayList<>();
		accessRelationTransports.add(new AccessRelationTransportBuilder().forUser2_2000_01_01().build());
		expected.setAccessRelationTransports(accessRelationTransports);

		final ShowEditUserResponse actual = super.callUsecaseWithoutContent("/" + UserTransportBuilder.USER2_ID,
				this.method, false, ShowEditUserResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_OnlyAdminAllowed_ErrorResponse() throws Exception {
		this.userName = UserTransportBuilder.USER1_NAME;
		this.userPassword = UserTransportBuilder.USER1_PASSWORD;

		final ErrorResponse actual = super.callUsecaseWithoutContent("/" + UserTransportBuilder.USER2_ID, this.method,
				false, ErrorResponse.class);

		Assert.assertEquals(new Integer(ErrorCode.USER_IS_NO_ADMIN.getErrorCode()), actual.getCode());

	}

}