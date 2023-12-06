
package org.laladev.moneyjinn.server.controller.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractWebUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.UserControllerApi;
import org.laladev.moneyjinn.server.model.ChangePasswordRequest;
import org.laladev.moneyjinn.server.model.ErrorResponse;
import org.laladev.moneyjinn.service.api.IUserService;

import jakarta.inject.Inject;

class ChangePasswordTest extends AbstractWebUserControllerTest {
	@Inject
	private IUserService userService;

	@Override
	protected void loadMethod() {
		super.getMock(UserControllerApi.class).changePassword(null);
	}

	@Test
	void test_OldPasswordMatchingNewPasswordProvided_Successfull() throws Exception {
		final UserID userId = new UserID(UserTransportBuilder.USER1_ID);
		final String newPassword = UserTransportBuilder.USER1_PASSWORD + "new";

		final ChangePasswordRequest request = new ChangePasswordRequest();
		request.setOldPassword(UserTransportBuilder.USER1_PASSWORD);
		request.setPassword(newPassword);

		User user = this.userService.getUserById(userId);
		String cryptedPassword = this.userService.cryptPassword(UserTransportBuilder.USER1_PASSWORD);
		Assertions.assertEquals(user.getPassword(), cryptedPassword);

		super.callUsecaseExpect204(request);

		user = this.userService.getUserById(userId);
		cryptedPassword = this.userService.cryptPassword(newPassword);
		Assertions.assertEquals(user.getPassword(), cryptedPassword);

	}

	@Test
	void test_OldPasswordMatchingNewPasswordEmptyButUserNew_errorRaised() throws Exception {
		final ChangePasswordRequest request = new ChangePasswordRequest();
		request.setOldPassword(UserTransportBuilder.USER1_PASSWORD);
		request.setPassword("");

		final ErrorResponse actual = super.callUsecaseExpect400(request, ErrorResponse.class);
		Assertions.assertNotNull(actual);
		Assertions.assertEquals(actual.getCode(), ErrorCode.PASSWORD_MUST_BE_CHANGED.getErrorCode());

	}

	@Test
	void test_OldPasswordMatchingNewPasswordEmpty_passwordGotNotChanged() throws Exception {
		super.setUsername(UserTransportBuilder.USER3_NAME);
		super.setPassword(UserTransportBuilder.USER3_PASSWORD);

		final UserID userId = new UserID(UserTransportBuilder.USER3_ID);
		final String newPassword = "";

		final ChangePasswordRequest request = new ChangePasswordRequest();
		request.setOldPassword(UserTransportBuilder.USER3_PASSWORD);
		request.setPassword(newPassword);

		final String cryptedPassword = this.userService.cryptPassword(UserTransportBuilder.USER3_PASSWORD);

		User user = this.userService.getUserById(userId);
		Assertions.assertEquals(user.getPassword(), cryptedPassword);

		super.callUsecaseExpect204(request);

		user = this.userService.getUserById(userId);
		Assertions.assertEquals(user.getPassword(), cryptedPassword);
	}

	@Test
	void test_OldPasswordNotMatching_errorRaised() throws Exception {
		final ChangePasswordRequest request = new ChangePasswordRequest();
		request.setOldPassword("wrongPassword");

		final ErrorResponse actual = super.callUsecaseExpect400(request, ErrorResponse.class);
		Assertions.assertNotNull(actual);
		Assertions.assertEquals(actual.getCode(), ErrorCode.PASSWORD_NOT_MATCHING.getErrorCode());

	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403(new ChangePasswordRequest());
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		final ChangePasswordRequest request = new ChangePasswordRequest();
		request.setOldPassword(UserTransportBuilder.ADMIN_PASSWORD);
		request.setPassword(UserTransportBuilder.ADMIN_PASSWORD + "new");

		super.callUsecaseExpect204(request);
	}
}