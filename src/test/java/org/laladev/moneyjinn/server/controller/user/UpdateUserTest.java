package org.laladev.moneyjinn.server.controller.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.laladev.moneyjinn.businesslogic.model.ErrorCode;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserAttribute;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.access.UserPermission;
import org.laladev.moneyjinn.businesslogic.service.api.IUserService;
import org.laladev.moneyjinn.core.rest.model.transport.AccessRelationTransport;
import org.laladev.moneyjinn.core.rest.model.transport.GroupTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.core.rest.model.user.UpdateUserRequest;
import org.laladev.moneyjinn.core.rest.model.user.UpdateUserResponse;
import org.laladev.moneyjinn.server.builder.AccessRelationTransportBuilder;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;

public class UpdateUserTest extends AbstractControllerTest {

	@Inject
	IUserService userService;

	@Override
	protected String getUsecase() {
		return super.getUsecaseFromTestClassName("user", this.getClass());
	}

	private void testError(final UserTransportBuilder transport, final ErrorCode errorCode) throws Exception {
		final UpdateUserRequest request = new UpdateUserRequest();

		request.setUserTransport(transport);

		final UpdateUserResponse expected = new UpdateUserResponse();
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

		final List<ValidationItemTransport> validationItems = new ArrayList<>();
		validationItems
				.add(new ValidationItemTransportBuilder().withKey(3).withError(errorCode.getErrorCode()).build());

		expected.setValidationItemTransports(validationItems);
		expected.setResult(Boolean.FALSE);

		final UpdateUserResponse actual = super.callUsecaseWithPUT("", request, false, UpdateUserResponse.class);

		Assert.assertEquals(expected, actual);

	}

	@Test
	public void test_UsernameAlreadyExisting_Error() throws Exception {

		final UserTransportBuilder transport = new UserTransportBuilder().forUser1();
		transport.setUserName(UserTransportBuilder.USER2_NAME);

		this.testError(transport, ErrorCode.USER_WITH_SAME_NAME_ALREADY_EXISTS);
	}

	@Test
	public void test_EmptyUsername_Error() throws Exception {
		final UserTransportBuilder transport = new UserTransportBuilder().forUser1();
		transport.setUserName("");

		this.testError(transport, ErrorCode.NAME_MUST_NOT_BE_EMPTY);
	}

	@Test
	public void test_AccessRelationAndPasswordEmpty_SuccessfullNoContent() throws Exception {
		final UpdateUserRequest request = new UpdateUserRequest();

		final UserTransportBuilder transport = new UserTransportBuilder().forUser1();
		transport.setUserIsNew(Short.valueOf((short) 0));
		request.setUserTransport(transport);

		final UpdateUserResponse actual = super.callUsecaseWithPUT("", request, true, UpdateUserResponse.class);

		Assert.assertNull(actual);

		final User user = this.userService.getUserById(new UserID(UserTransportBuilder.USER1_ID));

		Assert.assertEquals(user.getPassword(), UserTransportBuilder.USER1_PASSWORD);
		Assert.assertEquals(user.getAttributes(), Arrays.asList(UserAttribute.NONE));
	}

	@Test
	public void test_AccessRelationEmpty_SuccessfullNoContent() throws Exception {
		final UpdateUserRequest request = new UpdateUserRequest();

		final UserTransportBuilder transport = new UserTransportBuilder().forUser1();
		transport.setUserPassword("123");
		transport.setUserName("hugo");
		transport.setUserCanLogin(Short.valueOf((short) 0));
		transport.setUserIsAdmin(Short.valueOf((short) 0));

		/*
		 * this must be ignored by the server as the password is changed (which is done by the admin
		 * here so the user MUST reset the password afterwards)
		 */
		transport.setUserIsNew(Short.valueOf((short) 0));

		request.setUserTransport(transport);

		final UpdateUserResponse actual = super.callUsecaseWithPUT("", request, true, UpdateUserResponse.class);

		Assert.assertNull(actual);

		final User user = this.userService.getUserById(new UserID(UserTransportBuilder.USER1_ID));
		// sha1 of 123 --------------------------vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
		Assert.assertEquals(user.getPassword(), "40bd001563085fc35165329ea1ff5c5ecbdbbeef");
		Assert.assertEquals(user.getName(), "hugo");
		// instead of NONE ---------------------------------------------------vvvvvv
		Assert.assertEquals(user.getAttributes(), Arrays.asList(UserAttribute.IS_NEW));
		Assert.assertEquals(user.getPermissions(), Arrays.asList(UserPermission.NONE));
	}

}