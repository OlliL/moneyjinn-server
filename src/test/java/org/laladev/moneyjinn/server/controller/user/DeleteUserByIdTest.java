package org.laladev.moneyjinn.server.controller.user;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.service.api.IAccessRelationService;
import org.laladev.moneyjinn.businesslogic.service.api.IUserService;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;

public class DeleteUserByIdTest extends AbstractControllerTest {

	@Inject
	IUserService userService;

	@Inject
	IAccessRelationService accessRelationService;

	private final HttpMethod method = HttpMethod.DELETE;

	@Override
	protected String getUsecase() {
		return super.getUsecaseFromTestClassName("user", this.getClass());
	}

	@Test
	public void test_regularUserNoData_SuccessfullNoContent() throws Exception {
		User user = this.userService.getUserById(new UserID(UserTransportBuilder.USER1_ID));

		Assert.assertNotNull(user);

		super.callUsecaseWithoutContent("/" + UserTransportBuilder.USER1_ID, this.method, true, Object.class);

		user = this.userService.getUserById(new UserID(UserTransportBuilder.USER1_ID));

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

}