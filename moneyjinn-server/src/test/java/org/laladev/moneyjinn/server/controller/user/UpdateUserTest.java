
package org.laladev.moneyjinn.server.controller.user;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.access.AccessID;
import org.laladev.moneyjinn.model.access.AccessRelation;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserAttribute;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.access.UserRole;
import org.laladev.moneyjinn.server.builder.AccessRelationTransportBuilder;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractAdminUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.UserControllerApi;
import org.laladev.moneyjinn.server.model.AccessRelationTransport;
import org.laladev.moneyjinn.server.model.UpdateUserRequest;
import org.laladev.moneyjinn.server.model.UserTransport;
import org.laladev.moneyjinn.server.model.UserTransport.RoleEnum;
import org.laladev.moneyjinn.server.model.ValidationItemTransport;
import org.laladev.moneyjinn.server.model.ValidationResponse;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.IUserService;

import jakarta.inject.Inject;

class UpdateUserTest extends AbstractAdminUserControllerTest {
	@Inject
	private IUserService userService;
	@Inject
	private IAccessRelationService accessRelationService;
	private final AccessID accessIdUser1 = new AccessID(UserTransportBuilder.USER1_ID);
	private final AccessID accessIdUser2 = new AccessID(UserTransportBuilder.USER2_ID);
	private final AccessRelation accessRelationRoot = new AccessRelation(new AccessID(0l));
	private final AccessRelation accessRelationGroup1 = new AccessRelation(
			new AccessID(GroupTransportBuilder.GROUP1_ID), this.accessRelationRoot);
	private final AccessRelation accessRelationGroup2 = new AccessRelation(
			new AccessID(GroupTransportBuilder.GROUP2_ID), this.accessRelationRoot);
	private final AccessRelation accessRelationAdminGroup = new AccessRelation(
			new AccessID(GroupTransportBuilder.ADMINGROUP_ID), this.accessRelationRoot);
	private final AccessRelation accessRelationUser1Default1 = new AccessRelation(this.accessIdUser1,
			this.accessRelationGroup1, LocalDate.parse("2000-01-01"), LocalDate.parse("2599-12-31"));
	private final AccessRelation accessRelationUser1Default2 = new AccessRelation(this.accessIdUser1,
			this.accessRelationGroup2, LocalDate.parse("2600-01-01"), LocalDate.parse("2699-12-31"));
	private final AccessRelation accessRelationUser1Default3 = new AccessRelation(this.accessIdUser1,
			this.accessRelationGroup1, LocalDate.parse("2700-01-01"), LocalDate.parse("2799-12-31"));
	private final AccessRelation accessRelationUser1Default4 = new AccessRelation(this.accessIdUser1,
			this.accessRelationGroup2, LocalDate.parse("2800-01-01"), LocalDate.parse("2999-12-31"));

	@Override
	protected void loadMethod() {
		super.getMock(UserControllerApi.class).updateUser(null);
	}

	private void testError(final UserTransport transport, final AccessRelationTransport accessRelationTransport,
			final ErrorCode errorCode) throws Exception {
		final UpdateUserRequest request = new UpdateUserRequest();
		request.setUserTransport(transport);
		request.setAccessRelationTransport(accessRelationTransport);
		final ValidationResponse expected = new ValidationResponse();
		final List<ValidationItemTransport> validationItems = new ArrayList<>();
		validationItems.add(new ValidationItemTransportBuilder().withKey(transport.getId().toString())
				.withError(errorCode.getErrorCode()).build());
		expected.setValidationItemTransports(validationItems);
		expected.setResult(Boolean.FALSE);
		final ValidationResponse actual = super.callUsecaseExpect422(request, ValidationResponse.class);
		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_UsernameAlreadyExisting_Error() throws Exception {
		final UserTransport transport = new UserTransportBuilder().forUser1().build();
		transport.setUserName(UserTransportBuilder.USER2_NAME);
		this.testError(transport, null, ErrorCode.USER_WITH_SAME_NAME_ALREADY_EXISTS);
	}

	@Test
	void test_EmptyUsername_Error() throws Exception {
		final UserTransport transport = new UserTransportBuilder().forUser1().build();
		transport.setUserName("");
		this.testError(transport, null, ErrorCode.NAME_MUST_NOT_BE_EMPTY);
	}

	@Test
	void test_AccessRelationAndPasswordEmpty_SuccessfullPasswordNotChanged() throws Exception {
		final UpdateUserRequest request = new UpdateUserRequest();
		final UserTransport transport = new UserTransportBuilder().forUser1().build();
		transport.setUserIsNew(0);
		request.setUserTransport(transport);

		super.callUsecaseExpect204(request);

		final User user = this.userService.getUserById(new UserID(UserTransportBuilder.USER1_ID));
		Assertions.assertEquals(UserTransportBuilder.USER1_PASSWORD_SHA1, user.getPassword());
		Assertions.assertEquals(Arrays.asList(UserAttribute.NONE), user.getAttributes());
	}

	@Test
	void test_AccessRelationEmpty_Successfull() throws Exception {
		final UpdateUserRequest request = new UpdateUserRequest();
		final UserTransport transport = new UserTransportBuilder().forUser1().build();
		transport.setUserPassword("123");
		transport.setUserName("hugo");
		transport.setRole(RoleEnum.ADMIN);
		/*
		 * this must be ignored by the server as the password is changed (which is done
		 * by the admin here so the user MUST reset the password afterwards)
		 */
		transport.setUserIsNew(0);
		request.setUserTransport(transport);

		super.callUsecaseExpect204(request);

		final User user = this.userService.getUserById(new UserID(UserTransportBuilder.USER1_ID));
		// sha1 of 123 ----------vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
		Assertions.assertEquals("40bd001563085fc35165329ea1ff5c5ecbdbbeef", user.getPassword());
		Assertions.assertEquals("hugo", user.getName());
		// instead of NONE ---------------------------------vvvvvv
		Assertions.assertEquals(Arrays.asList(UserAttribute.IS_NEW), user.getAttributes());
		Assertions.assertEquals(UserRole.ADMIN, user.getRole());
	}

	@Test
	void test_makeAdminUserStandard_Successfull() throws Exception {
		final UpdateUserRequest request = new UpdateUserRequest();
		final UserTransport transport = new UserTransportBuilder().forAdmin().build();
		transport.setRole(RoleEnum.STANDARD);
		request.setUserTransport(transport);

		super.callUsecaseExpect204(request);

		final User user = this.userService.getUserById(new UserID(UserTransportBuilder.ADMIN_ID));
		Assertions.assertEquals(UserRole.STANDARD, user.getRole());
	}

	@Test
	void test_makeAdminUserImport_Successfull() throws Exception {
		final UpdateUserRequest request = new UpdateUserRequest();
		final UserTransport transport = new UserTransportBuilder().forAdmin().build();
		transport.setRole(RoleEnum.IMPORT);
		request.setUserTransport(transport);

		super.callUsecaseExpect204(request);

		final User user = this.userService.getUserById(new UserID(UserTransportBuilder.ADMIN_ID));
		Assertions.assertEquals(UserRole.IMPORT, user.getRole());
	}

	@Test
	void test_makeAdminUserInactive_Successfull() throws Exception {
		final UpdateUserRequest request = new UpdateUserRequest();
		final UserTransport transport = new UserTransportBuilder().forAdmin().build();
		transport.setRole(RoleEnum.INACTIVE);
		request.setUserTransport(transport);

		super.callUsecaseExpect204(request);

		final User user = this.userService.getUserById(new UserID(UserTransportBuilder.ADMIN_ID));
		Assertions.assertEquals(UserRole.INACTIVE, user.getRole());
	}

	@Test
	void test_makeStandardUserAdmin_Successfull() throws Exception {
		final UpdateUserRequest request = new UpdateUserRequest();
		final UserTransport transport = new UserTransportBuilder().forUser1().build();
		transport.setRole(RoleEnum.ADMIN);
		request.setUserTransport(transport);

		super.callUsecaseExpect204(request);

		final User user = this.userService.getUserById(new UserID(UserTransportBuilder.USER1_ID));
		Assertions.assertEquals(UserRole.ADMIN, user.getRole());
	}

	@Test
	void test_makeStandardUserImport_Successfull() throws Exception {
		final UpdateUserRequest request = new UpdateUserRequest();
		final UserTransport transport = new UserTransportBuilder().forUser1().build();
		transport.setRole(RoleEnum.IMPORT);
		request.setUserTransport(transport);

		super.callUsecaseExpect204(request);

		final User user = this.userService.getUserById(new UserID(UserTransportBuilder.USER1_ID));
		Assertions.assertEquals(UserRole.IMPORT, user.getRole());
	}

	@Test
	void test_makeStandardUserInactive_Successfull() throws Exception {
		final UpdateUserRequest request = new UpdateUserRequest();
		final UserTransport transport = new UserTransportBuilder().forUser1().build();
		transport.setRole(RoleEnum.INACTIVE);
		request.setUserTransport(transport);

		super.callUsecaseExpect204(request);

		final User user = this.userService.getUserById(new UserID(UserTransportBuilder.USER1_ID));
		Assertions.assertEquals(UserRole.INACTIVE, user.getRole());
	}

	@Test
	void test_makeImportUserAdmin_Successfull() throws Exception {
		final UpdateUserRequest request = new UpdateUserRequest();
		final UserTransport transport = new UserTransportBuilder().forImportUser().build();
		transport.setRole(RoleEnum.ADMIN);
		request.setUserTransport(transport);

		super.callUsecaseExpect204(request);

		final User user = this.userService.getUserById(new UserID(UserTransportBuilder.IMPORTUSER_ID));
		Assertions.assertEquals(UserRole.ADMIN, user.getRole());
	}

	@Test
	void test_makeImportUserStandard_Successfull() throws Exception {
		final UpdateUserRequest request = new UpdateUserRequest();
		final UserTransport transport = new UserTransportBuilder().forImportUser().build();
		transport.setRole(RoleEnum.STANDARD);
		request.setUserTransport(transport);

		super.callUsecaseExpect204(request);

		final User user = this.userService.getUserById(new UserID(UserTransportBuilder.IMPORTUSER_ID));
		Assertions.assertEquals(UserRole.STANDARD, user.getRole());
	}

	@Test
	void test_makeImportUserInactive_Successfull() throws Exception {
		final UpdateUserRequest request = new UpdateUserRequest();
		final UserTransport transport = new UserTransportBuilder().forImportUser().build();
		transport.setRole(RoleEnum.INACTIVE);
		request.setUserTransport(transport);

		super.callUsecaseExpect204(request);

		final User user = this.userService.getUserById(new UserID(UserTransportBuilder.IMPORTUSER_ID));
		Assertions.assertEquals(UserRole.INACTIVE, user.getRole());
	}

	@Test
	void test_NoValidFromForAccessRelation_Error() throws Exception {
		final UserTransport transport = new UserTransportBuilder().forUser1().build();
		final AccessRelationTransport accessRelationTransport = new AccessRelationTransportBuilder()
				.forUser1_2000_01_01().build();
		accessRelationTransport.setValidfrom(null);
		this.testError(transport, accessRelationTransport, ErrorCode.VALIDFROM_NOT_DEFINED);
	}

	@Test
	void test_ValidFromToEarlyForAccessRelation_Error() throws Exception {
		final UserTransport transport = new UserTransportBuilder().forUser1().build();
		final AccessRelationTransport accessRelationTransport = new AccessRelationTransportBuilder()
				.forUser1_2000_01_01().build();
		accessRelationTransport.setValidfrom(LocalDate.now().minusDays(1l));
		this.testError(transport, accessRelationTransport, ErrorCode.VALIDFROM_EARLIER_THAN_TOMORROW);
	}

	@Test
	void test_NoGroupForAccessRelation_Error() throws Exception {
		final UserTransport transport = new UserTransportBuilder().forUser1().build();
		final AccessRelationTransport accessRelationTransport = new AccessRelationTransportBuilder()
				.forUser1_2000_01_01().build();
		accessRelationTransport.setRefId(null);
		this.testError(transport, accessRelationTransport, ErrorCode.GROUP_MUST_BE_SPECIFIED);
	}

	private void help_AccessRelation_Testing(final UserTransport transport,
			final AccessRelationTransport accessRelationTransport, final List<AccessRelation> expectedAccessRelations)
			throws Exception {
		final UpdateUserRequest request = new UpdateUserRequest();
		request.setUserTransport(transport);
		request.setAccessRelationTransport(accessRelationTransport);

		super.callUsecaseExpect204(request);

		final List<AccessRelation> accessRelations = this.accessRelationService
				.getAllAccessRelationsById(new UserID(transport.getId()));
		Assertions.assertEquals(expectedAccessRelations, accessRelations);
	}

	/**
	 * see Javadoc in AccessRelationService.setAccessRelation() for the cases
	 */
	@Test
	void test_AR_caseA() throws Exception {
		final UserTransport transport = new UserTransportBuilder().forUser2().build();
		final AccessRelationTransport accessRelationTransport = new AccessRelationTransport();
		accessRelationTransport.setId(UserTransportBuilder.USER2_ID);
		accessRelationTransport.setRefId(GroupTransportBuilder.GROUP2_ID);
		accessRelationTransport.setValidfrom(LocalDate.parse("2100-12-31"));
		final List<AccessRelation> accessRelations = new ArrayList<>();
		accessRelations.add(new AccessRelation(this.accessIdUser2, this.accessRelationGroup1,
				LocalDate.parse("2000-01-01"), LocalDate.parse("2100-12-30")));
		accessRelations.add(new AccessRelation(this.accessIdUser2, this.accessRelationGroup2,
				LocalDate.parse("2100-12-31"), LocalDate.parse("2999-12-31")));
		this.help_AccessRelation_Testing(transport, accessRelationTransport, accessRelations);
	}

	/**
	 * see Javadoc in AccessRelationService.setAccessRelation() for the cases
	 */
	@Test
	void test_AR_caseB() throws Exception {
		final UserTransport transport = new UserTransportBuilder().forUser1().build();
		final AccessRelationTransport accessRelationTransport = new AccessRelationTransport();
		accessRelationTransport.setId(UserTransportBuilder.USER1_ID);
		accessRelationTransport.setRefId(GroupTransportBuilder.GROUP1_ID);
		accessRelationTransport.setValidfrom(LocalDate.parse("2900-01-01"));
		final List<AccessRelation> accessRelations = new ArrayList<>();
		accessRelations.add(this.accessRelationUser1Default1);
		accessRelations.add(this.accessRelationUser1Default2);
		accessRelations.add(this.accessRelationUser1Default3);
		accessRelations.add(new AccessRelation(this.accessIdUser1, this.accessRelationGroup2,
				LocalDate.parse("2800-01-01"), LocalDate.parse("2899-12-31")));
		accessRelations.add(new AccessRelation(this.accessIdUser1, this.accessRelationGroup1,
				LocalDate.parse("2900-01-01"), LocalDate.parse("2999-12-31")));
		this.help_AccessRelation_Testing(transport, accessRelationTransport, accessRelations);
	}

	/**
	 * see Javadoc in AccessRelationService.setAccessRelation() for the cases
	 */
	@Test
	void test_AR_caseC() throws Exception {
		final UserTransport transport = new UserTransportBuilder().forUser1().build();
		final AccessRelationTransport accessRelationTransport = new AccessRelationTransport();
		accessRelationTransport.setId(UserTransportBuilder.USER1_ID);
		accessRelationTransport.setRefId(GroupTransportBuilder.GROUP2_ID);
		accessRelationTransport.setValidfrom(LocalDate.parse("2900-01-01"));
		final List<AccessRelation> accessRelations = new ArrayList<>();
		accessRelations.add(this.accessRelationUser1Default1);
		accessRelations.add(this.accessRelationUser1Default2);
		accessRelations.add(this.accessRelationUser1Default3);
		accessRelations.add(this.accessRelationUser1Default4);
		this.help_AccessRelation_Testing(transport, accessRelationTransport, accessRelations);
	}

	/**
	 * see Javadoc in AccessRelationService.setAccessRelation() for the cases
	 */
	@Test
	void test_AR_caseD() throws Exception {
		final UserTransport transport = new UserTransportBuilder().forUser1().build();
		final AccessRelationTransport accessRelationTransport = new AccessRelationTransport();
		accessRelationTransport.setId(UserTransportBuilder.USER1_ID);
		accessRelationTransport.setRefId(GroupTransportBuilder.GROUP1_ID);
		accessRelationTransport.setValidfrom(LocalDate.parse("2800-01-01"));
		final List<AccessRelation> accessRelations = new ArrayList<>();
		accessRelations.add(this.accessRelationUser1Default1);
		accessRelations.add(this.accessRelationUser1Default2);
		accessRelations.add(new AccessRelation(this.accessIdUser1, this.accessRelationGroup1,
				LocalDate.parse("2700-01-01"), LocalDate.parse("2999-12-31")));
		this.help_AccessRelation_Testing(transport, accessRelationTransport, accessRelations);
	}

	/**
	 * see Javadoc in AccessRelationService.setAccessRelation() for the cases
	 */
	@Test
	void test_AR_caseE() throws Exception {
		final UserTransport transport = new UserTransportBuilder().forUser1().build();
		final AccessRelationTransport accessRelationTransport = new AccessRelationTransport();
		accessRelationTransport.setId(UserTransportBuilder.USER1_ID);
		accessRelationTransport.setRefId(GroupTransportBuilder.GROUP1_ID);
		accessRelationTransport.setValidfrom(LocalDate.parse("2700-01-01"));
		final List<AccessRelation> accessRelations = new ArrayList<>();
		accessRelations.add(this.accessRelationUser1Default1);
		accessRelations.add(this.accessRelationUser1Default2);
		accessRelations.add(this.accessRelationUser1Default3);
		accessRelations.add(this.accessRelationUser1Default4);
		this.help_AccessRelation_Testing(transport, accessRelationTransport, accessRelations);
	}

	/**
	 * see Javadoc in AccessRelationService.setAccessRelation() for the cases
	 */
	@Test
	void test_AR_caseF() throws Exception {
		final UserTransport transport = new UserTransportBuilder().forUser1().build();
		final AccessRelationTransport accessRelationTransport = new AccessRelationTransport();
		accessRelationTransport.setId(UserTransportBuilder.USER1_ID);
		accessRelationTransport.setRefId(GroupTransportBuilder.GROUP2_ID);
		accessRelationTransport.setValidfrom(LocalDate.parse("2780-01-01"));
		final List<AccessRelation> accessRelations = new ArrayList<>();
		accessRelations.add(this.accessRelationUser1Default1);
		accessRelations.add(this.accessRelationUser1Default2);
		accessRelations.add(new AccessRelation(this.accessIdUser1, this.accessRelationGroup1,
				LocalDate.parse("2700-01-01"), LocalDate.parse("2779-12-31")));
		accessRelations.add(new AccessRelation(this.accessIdUser1, this.accessRelationGroup2,
				LocalDate.parse("2780-01-01"), LocalDate.parse("2999-12-31")));
		this.help_AccessRelation_Testing(transport, accessRelationTransport, accessRelations);
	}

	/**
	 * see Javadoc in AccessRelationService.setAccessRelation() for the cases
	 */
	@Test
	void test_AR_caseG() throws Exception {
		final UserTransport transport = new UserTransportBuilder().forUser1().build();
		final AccessRelationTransport accessRelationTransport = new AccessRelationTransport();
		accessRelationTransport.setId(UserTransportBuilder.USER1_ID);
		accessRelationTransport.setRefId(GroupTransportBuilder.GROUP1_ID);
		accessRelationTransport.setValidfrom(LocalDate.parse("2650-01-01"));
		final List<AccessRelation> accessRelations = new ArrayList<>();
		accessRelations.add(this.accessRelationUser1Default1);
		accessRelations.add(new AccessRelation(this.accessIdUser1, this.accessRelationGroup2,
				LocalDate.parse("2600-01-01"), LocalDate.parse("2649-12-31")));
		accessRelations.add(new AccessRelation(this.accessIdUser1, this.accessRelationGroup1,
				LocalDate.parse("2650-01-01"), LocalDate.parse("2799-12-31")));
		accessRelations.add(this.accessRelationUser1Default4);
		this.help_AccessRelation_Testing(transport, accessRelationTransport, accessRelations);
	}

	/**
	 * see Javadoc in AccessRelationService.setAccessRelation() for the cases
	 */
	@Test
	void test_AR_caseH() throws Exception {
		final UserTransport transport = new UserTransportBuilder().forUser1().build();
		final AccessRelationTransport accessRelationTransport = new AccessRelationTransport();
		accessRelationTransport.setId(UserTransportBuilder.USER1_ID);
		accessRelationTransport.setRefId(GroupTransportBuilder.GROUP2_ID);
		accessRelationTransport.setValidfrom(LocalDate.parse("2700-01-01"));
		final List<AccessRelation> accessRelations = new ArrayList<>();
		accessRelations.add(this.accessRelationUser1Default1);
		accessRelations.add(new AccessRelation(this.accessIdUser1, this.accessRelationGroup2,
				LocalDate.parse("2600-01-01"), LocalDate.parse("2999-12-31")));
		this.help_AccessRelation_Testing(transport, accessRelationTransport, accessRelations);
	}

	/**
	 * see Javadoc in AccessRelationService.setAccessRelation() for the cases
	 */
	@Test
	void test_AR_caseI() throws Exception {
		final UserTransport transport = new UserTransportBuilder().forUser1().build();
		final AccessRelationTransport accessRelationTransport = new AccessRelationTransport();
		accessRelationTransport.setId(UserTransportBuilder.USER1_ID);
		accessRelationTransport.setRefId(GroupTransportBuilder.ADMINGROUP_ID);
		accessRelationTransport.setValidfrom(LocalDate.parse("2700-01-01"));
		final List<AccessRelation> accessRelations = new ArrayList<>();
		accessRelations.add(this.accessRelationUser1Default1);
		accessRelations.add(this.accessRelationUser1Default2);
		accessRelations.add(new AccessRelation(this.accessIdUser1, this.accessRelationAdminGroup,
				LocalDate.parse("2700-01-01"), LocalDate.parse("2799-12-31")));
		accessRelations.add(this.accessRelationUser1Default4);
		this.help_AccessRelation_Testing(transport, accessRelationTransport, accessRelations);
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403(new UpdateUserRequest());
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		// Users are always there.
	}
}