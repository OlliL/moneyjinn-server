package org.laladev.moneyjinn.server.controller.user;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.businesslogic.model.ErrorCode;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.service.api.IAccessRelationService;
import org.laladev.moneyjinn.businesslogic.service.api.IUserService;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;

public class DeleteUserByIdTest extends AbstractControllerTest {

	@Inject
	IUserService userService;

	@Inject
	IAccessRelationService accessRelationService;

	private final HttpMethod method = HttpMethod.DELETE;
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

	@Test
	public void test_regularUserNoData_SuccessfullNoContent() throws Exception {
		User user = this.userService.getUserById(new UserID(UserTransportBuilder.USER2_ID));

		Assert.assertNotNull(user);

		super.callUsecaseWithoutContent("/" + UserTransportBuilder.USER2_ID, this.method, true, Object.class);

		user = this.userService.getUserById(new UserID(UserTransportBuilder.USER2_ID));

		Assert.assertNull(user);
	}

	@Test
	public void test_nonExistingUser_SuccessfullNoContent() throws Exception {
		User user = this.userService.getUserById(new UserID(UserTransportBuilder.NON_EXISTING_ID));

		Assert.assertNull(user);

		super.callUsecaseWithoutContent("/" + UserTransportBuilder.NON_EXISTING_ID, this.method, true, Object.class);

		user = this.userService.getUserById(new UserID(UserTransportBuilder.NON_EXISTING_ID));

		Assert.assertNull(user);
	}

	@Test
	public void test_regularUserWithData_SuccessfullNoContent() throws Exception {
		final ErrorResponse expected = new ErrorResponse();
		expected.setCode(ErrorCode.USER_HAS_DATA.getErrorCode());
		expected.setMessage("This user has already entered data and may therefore not be deleted!");

		User user = this.userService.getUserById(new UserID(UserTransportBuilder.USER3_ID));

		Assert.assertNotNull(user);

		final ErrorResponse response = super.callUsecaseWithoutContent("/" + UserTransportBuilder.USER3_ID, this.method,
				false, ErrorResponse.class);

		user = this.userService.getUserById(new UserID(UserTransportBuilder.USER3_ID));

		Assert.assertNotNull(user);

		Assert.assertEquals(expected, response);
	}

	@Test
	public void test_OnlyAdminAllowed_ErrorResponse() throws Exception {
		this.userName = UserTransportBuilder.USER1_NAME;
		this.userPassword = UserTransportBuilder.USER1_PASSWORD;

		final ErrorResponse actual = super.callUsecaseWithoutContent("/" + UserTransportBuilder.USER2_ID, this.method,
				false, ErrorResponse.class);

		Assert.assertEquals(new Integer(ErrorCode.USER_IS_NO_ADMIN.getErrorCode()), actual.getCode());

	}

	@Test
	public void test_AuthorizationRequired_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("/1", this.method, false, ErrorResponse.class);
		Assert.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

}