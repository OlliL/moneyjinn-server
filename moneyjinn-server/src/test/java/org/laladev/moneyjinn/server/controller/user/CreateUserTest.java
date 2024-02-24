
package org.laladev.moneyjinn.server.controller.user;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.access.AccessRelation;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserAttribute;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.access.UserRole;
import org.laladev.moneyjinn.server.builder.AccessRelationTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractAdminUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.UserControllerApi;
import org.laladev.moneyjinn.server.model.AccessRelationTransport;
import org.laladev.moneyjinn.server.model.CreateUserRequest;
import org.laladev.moneyjinn.server.model.CreateUserResponse;
import org.laladev.moneyjinn.server.model.UserTransport;
import org.laladev.moneyjinn.server.model.UserTransport.RoleEnum;
import org.laladev.moneyjinn.server.model.ValidationItemTransport;
import org.laladev.moneyjinn.server.model.ValidationResponse;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.IUserService;

import jakarta.inject.Inject;

class CreateUserTest extends AbstractAdminUserControllerTest {
	@Inject
	private IUserService userService;
	@Inject
	private IAccessRelationService accessRelationService;

	@Override
	protected void loadMethod() {
		super.getMock(UserControllerApi.class).createUser(null);
	}

	private void testError(final UserTransport transport, final AccessRelationTransport accessRelationTransport,
			final ErrorCode errorCode) throws Exception {
		final CreateUserRequest request = new CreateUserRequest();
		request.setUserTransport(transport);
		request.setAccessRelationTransport(accessRelationTransport);
		final ValidationResponse expected = new ValidationResponse();
		final List<ValidationItemTransport> validationItems = new ArrayList<>();
		validationItems
				.add(new ValidationItemTransportBuilder().withKey(null).withError(errorCode.getErrorCode()).build());
		expected.setValidationItemTransports(validationItems);
		expected.setResult(Boolean.FALSE);
		final ValidationResponse actual = super.callUsecaseExpect422(request, ValidationResponse.class);
		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_UsernameAlreadyExisting_Error() throws Exception {
		final UserTransport transport = new UserTransportBuilder().forNewUser().build();
		transport.setUserName(UserTransportBuilder.USER2_NAME);
		this.testError(transport, null, ErrorCode.USER_WITH_SAME_NAME_ALREADY_EXISTS);
	}

	@Test
	void test_emptyUsername_Error() throws Exception {
		final UserTransport transport = new UserTransportBuilder().forNewUser().build();
		transport.setUserName("");
		this.testError(transport, null, ErrorCode.NAME_MUST_NOT_BE_EMPTY);
	}

	@Test
	void test_nullUsername_Error() throws Exception {
		final UserTransport transport = new UserTransportBuilder().forNewUser().build();
		transport.setUserName(null);
		this.testError(transport, null, ErrorCode.NAME_MUST_NOT_BE_EMPTY);
	}

	@Test
	void test_AccessRelationEmpty_Successfull() throws Exception {
		final CreateUserRequest request = new CreateUserRequest();
		final UserTransport transport = new UserTransportBuilder().forNewUser().build();
		/*
		 * this must be ignored by the server as the attribute is always set to 1 on
		 * creation
		 */
		transport.setUserIsNew(0);
		request.setUserTransport(transport);

		final CreateUserResponse actual = super.callUsecaseExpect200(request, CreateUserResponse.class);

		final User user = this.userService.getUserByName(UserTransportBuilder.NEWUSER_NAME);
		Assertions.assertEquals(UserTransportBuilder.NEXT_ID, user.getId().getId());
		Assertions.assertEquals(UserTransportBuilder.NEXT_ID, actual.getUserId());
		Assertions.assertEquals(UserTransportBuilder.IMPORTUSER_PASSWORD_SHA1, user.getPassword());
		// instead of NONE ---------------------------------vvvvvv
		Assertions.assertEquals(Arrays.asList(UserAttribute.IS_NEW), user.getAttributes());
	}

	@Test
	void test_createRegularUser_Successfull() throws Exception {
		final CreateUserRequest request = new CreateUserRequest();
		final UserTransport transport = new UserTransportBuilder().forNewUser().build();
		transport.setUserPassword("123");
		transport.setRole(RoleEnum.STANDARD);
		request.setUserTransport(transport);

		super.callUsecaseExpect200(request, CreateUserResponse.class);

		final User user = this.userService.getUserByName(UserTransportBuilder.NEWUSER_NAME);
		Assertions.assertEquals(UserRole.STANDARD, user.getRole());
	}

	@Test
	void test_createImportUser_Successfull() throws Exception {
		final CreateUserRequest request = new CreateUserRequest();
		final UserTransport transport = new UserTransportBuilder().forNewUser().build();
		transport.setUserPassword("123");
		transport.setRole(RoleEnum.IMPORT);
		request.setUserTransport(transport);

		super.callUsecaseExpect200(request, CreateUserResponse.class);

		final User user = this.userService.getUserByName(UserTransportBuilder.NEWUSER_NAME);
		Assertions.assertEquals(UserRole.IMPORT, user.getRole());
	}

	@Test
	void test_createLockedUser_Successfull() throws Exception {
		final CreateUserRequest request = new CreateUserRequest();
		final UserTransport transport = new UserTransportBuilder().forNewUser().build();
		transport.setUserPassword("123");
		transport.setRole(RoleEnum.INACTIVE);
		request.setUserTransport(transport);

		super.callUsecaseExpect200(request, CreateUserResponse.class);

		final User user = this.userService.getUserByName(UserTransportBuilder.NEWUSER_NAME);
		Assertions.assertEquals(UserRole.INACTIVE, user.getRole());
	}

	@Test
	void test_AccessRelationEmptyValidTil_Successfull() throws Exception {
		final CreateUserRequest request = new CreateUserRequest();
		final UserTransport transport = new UserTransportBuilder().forNewUser().build();
		request.setUserTransport(transport);
		final AccessRelationTransport accessRelationTransport = new AccessRelationTransportBuilder()
				.forNewUser_2000_01_01().build();
		request.setAccessRelationTransport(accessRelationTransport);

		final CreateUserResponse actual = super.callUsecaseExpect200(request, CreateUserResponse.class);

		Assertions.assertEquals(UserTransportBuilder.NEXT_ID, actual.getUserId());

		final AccessRelation accessRelation = this.accessRelationService
				.getCurrentAccessRelationById(new UserID(UserTransportBuilder.NEXT_ID));
		Assertions.assertNotNull(accessRelation);
		Assertions.assertEquals(accessRelationTransport.getRefId(), accessRelation.getGroupID().getId());
		Assertions.assertEquals(accessRelationTransport.getValidfrom(), accessRelation.getValidFrom());
		// default if validTil is empty
		Assertions.assertEquals(LocalDate.parse("2999-12-31"), accessRelation.getValidTil());
	}

	@Test
	void test_AccessRelationWithValidTil_Successfull() throws Exception {
		final CreateUserRequest request = new CreateUserRequest();
		final UserTransport transport = new UserTransportBuilder().forNewUser().build();
		request.setUserTransport(transport);
		final AccessRelationTransport accessRelationTransport = new AccessRelationTransportBuilder()
				.forNewUser_2000_01_01().build();
		accessRelationTransport.setValidtil(LocalDate.parse("2900-12-31"));
		request.setAccessRelationTransport(accessRelationTransport);

		final CreateUserResponse actual = super.callUsecaseExpect200(request, CreateUserResponse.class);

		Assertions.assertEquals(UserTransportBuilder.NEXT_ID, actual.getUserId());

		final AccessRelation accessRelation = this.accessRelationService
				.getCurrentAccessRelationById(new UserID(UserTransportBuilder.NEXT_ID));
		Assertions.assertNotNull(accessRelation);
		// default did not overwrite
		Assertions.assertEquals(accessRelationTransport.getValidtil(), accessRelation.getValidTil());
	}

	@Test
	void test_NoValidFromForAccessRelation_Error() throws Exception {
		final UserTransport transport = new UserTransportBuilder().forNewUser().build();
		final AccessRelationTransport accessRelationTransport = new AccessRelationTransportBuilder()
				.forNewUser_2000_01_01().build();
		accessRelationTransport.setValidfrom(null);
		this.testError(transport, accessRelationTransport, ErrorCode.VALIDFROM_NOT_DEFINED);
	}

	@Test
	void test_NoGroupForAccessRelation_Error() throws Exception {
		final UserTransport transport = new UserTransportBuilder().forNewUser().build();
		final AccessRelationTransport accessRelationTransport = new AccessRelationTransportBuilder()
				.forNewUser_2000_01_01().build();
		accessRelationTransport.setRefId(null);

		this.testError(transport, accessRelationTransport, ErrorCode.GROUP_MUST_BE_SPECIFIED);
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403(new CreateUserRequest());
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		// Users are always there.
	}
}