package org.laladev.moneyjinn.server.controller.user;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.transport.GroupTransport;
import org.laladev.moneyjinn.core.rest.model.transport.UserTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.core.rest.model.user.CreateUserRequest;
import org.laladev.moneyjinn.core.rest.model.user.CreateUserResponse;
import org.laladev.moneyjinn.core.rest.model.user.transport.AccessRelationTransport;
import org.laladev.moneyjinn.model.access.AccessID;
import org.laladev.moneyjinn.model.access.AccessRelation;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserAttribute;
import org.laladev.moneyjinn.model.access.UserPermission;
import org.laladev.moneyjinn.server.builder.AccessRelationTransportBuilder;
import org.laladev.moneyjinn.server.builder.DateUtil;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
//import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.IUserService;
import org.springframework.http.HttpMethod;

public class CreateUserTest extends AbstractControllerTest {

	@Inject
	private IUserService userService;
	@Inject
	private IAccessRelationService accessRelationService;

	private final HttpMethod method = HttpMethod.POST;
	private String userName;
	private String userPassword;

	@BeforeEach
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

	private void testError(final UserTransport transport, final AccessRelationTransport accessRelationTransport,
			final ErrorCode errorCode) throws Exception {
		final CreateUserRequest request = new CreateUserRequest();

		request.setUserTransport(transport);
		request.setAccessRelationTransport(accessRelationTransport);

		final CreateUserResponse expected = new CreateUserResponse();
		final List<GroupTransport> groupTransports = new ArrayList<>();
		groupTransports.add(new GroupTransportBuilder().forAdminGroup().build());
		groupTransports.add(new GroupTransportBuilder().forGroup1().build());
		groupTransports.add(new GroupTransportBuilder().forGroup2().build());
		groupTransports.add(new GroupTransportBuilder().forGroup3().build());
		expected.setGroupTransports(groupTransports);

		final List<ValidationItemTransport> validationItems = new ArrayList<>();
		validationItems
				.add(new ValidationItemTransportBuilder().withKey(null).withError(errorCode.getErrorCode()).build());

		expected.setValidationItemTransports(validationItems);
		expected.setResult(Boolean.FALSE);

		final CreateUserResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				CreateUserResponse.class);

		Assertions.assertEquals(expected, actual);

	}

	@Test
	public void test_UsernameAlreadyExisting_Error() throws Exception {

		final UserTransport transport = new UserTransportBuilder().forNewUser().build();
		transport.setUserName(UserTransportBuilder.USER2_NAME);

		this.testError(transport, null, ErrorCode.USER_WITH_SAME_NAME_ALREADY_EXISTS);
	}

	@Test
	public void test_emptyUsername_Error() throws Exception {
		final UserTransport transport = new UserTransportBuilder().forNewUser().build();
		transport.setUserName("");

		this.testError(transport, null, ErrorCode.NAME_MUST_NOT_BE_EMPTY);
	}

	@Test
	public void test_nullUsername_Error() throws Exception {
		final UserTransport transport = new UserTransportBuilder().forNewUser().build();
		transport.setUserName(null);

		this.testError(transport, null, ErrorCode.NAME_MUST_NOT_BE_EMPTY);
	}

	@Test
	public void test_AccessRelationAndPasswordEmpty_SuccessfullNoContent() throws Exception {
		final CreateUserRequest request = new CreateUserRequest();

		final UserTransport transport = new UserTransportBuilder().forNewUser().build();

		/*
		 * this must be ignored by the server as the attribute is always set to 1 on creation
		 */
		transport.setUserIsNew(Short.valueOf((short) 0));

		request.setUserTransport(transport);

		final CreateUserResponse actual = super.callUsecaseWithContent("", this.method, request, true,
				CreateUserResponse.class);

		Assertions.assertNull(actual);

		final User user = this.userService.getUserByName(UserTransportBuilder.NEWUSER_NAME);

		Assertions.assertEquals(UserTransportBuilder.NEXT_ID, user.getId().getId());
		Assertions.assertEquals(null, user.getPassword());
		// instead of NONE -----------------------------vvvvvv
		Assertions.assertEquals(Arrays.asList(UserAttribute.IS_NEW), user.getAttributes());
	}

	@Test
	public void test_AccessRelationEmpty_SuccessfullNoContent() throws Exception {
		final CreateUserRequest request = new CreateUserRequest();

		final UserTransport transport = new UserTransportBuilder().forNewUser().build();
		transport.setUserPassword("123");

		/*
		 * this must be ignored by the server as the attribute is always set to 1 on creation
		 */
		transport.setUserIsNew(Short.valueOf((short) 0));

		request.setUserTransport(transport);

		final CreateUserResponse actual = super.callUsecaseWithContent("", this.method, request, true,
				CreateUserResponse.class);

		Assertions.assertNull(actual);

		final User user = this.userService.getUserByName(UserTransportBuilder.NEWUSER_NAME);

		Assertions.assertEquals(UserTransportBuilder.NEXT_ID, user.getId().getId());
		// sha1 of 123 ------vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
		Assertions.assertEquals("40bd001563085fc35165329ea1ff5c5ecbdbbeef", user.getPassword());
		Assertions.assertEquals(UserTransportBuilder.NEWUSER_NAME, user.getName());
		// instead of NONE -----------------------------vvvvvv
		Assertions.assertEquals(Arrays.asList(UserAttribute.IS_NEW), user.getAttributes());
		Assertions.assertEquals(Arrays.asList(UserPermission.ADMIN, UserPermission.LOGIN), user.getPermissions());
	}

	@Test
	public void test_AccessRelationEmptyValidTil_SuccessfullNoContent() throws Exception {
		final CreateUserRequest request = new CreateUserRequest();

		final UserTransport transport = new UserTransportBuilder().forNewUser().build();
		request.setUserTransport(transport);

		final AccessRelationTransport accessRelationTransport = new AccessRelationTransportBuilder()
				.forNewUser_2000_01_01().build();
		request.setAccessRelationTransport(accessRelationTransport);

		final CreateUserResponse actual = super.callUsecaseWithContent("", this.method, request, true,
				CreateUserResponse.class);

		Assertions.assertNull(actual);

		final AccessRelation accessRelation = this.accessRelationService
				.getAccessRelationById(new AccessID(UserTransportBuilder.NEXT_ID));

		Assertions.assertNotNull(accessRelation);
		Assertions.assertEquals(accessRelationTransport.getRefId(),
				accessRelation.getParentAccessRelation().getId().getId());
		Assertions.assertEquals(accessRelationTransport.getValidfrom().getTime(),
				accessRelation.getValidFrom().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());
		// default if validTil is empty
		Assertions.assertEquals(DateUtil.getGmtDate("2999-12-31").getTime(),
				accessRelation.getValidTil().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());
	}

	@Test
	public void test_AccessRelationWithValidTil_SuccessfullNoContent() throws Exception {
		final CreateUserRequest request = new CreateUserRequest();

		final UserTransport transport = new UserTransportBuilder().forNewUser().build();
		request.setUserTransport(transport);

		final AccessRelationTransport accessRelationTransport = new AccessRelationTransportBuilder()
				.forNewUser_2000_01_01().build();
		accessRelationTransport.setValidtil(DateUtil.getGmtDate("2900-12-31"));
		request.setAccessRelationTransport(accessRelationTransport);

		final CreateUserResponse actual = super.callUsecaseWithContent("", this.method, request, true,
				CreateUserResponse.class);

		Assertions.assertNull(actual);

		final AccessRelation accessRelation = this.accessRelationService
				.getAccessRelationById(new AccessID(UserTransportBuilder.NEXT_ID));

		Assertions.assertNotNull(accessRelation);
		// default did not overwrite
		Assertions.assertEquals(accessRelationTransport.getValidtil().getTime(),
				accessRelation.getValidTil().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());
	}

	@Test
	public void test_NoValidFromForAccessRelation_Error() throws Exception {
		final UserTransport transport = new UserTransportBuilder().forNewUser().build();
		final AccessRelationTransport accessRelationTransport = new AccessRelationTransportBuilder()
				.forNewUser_2000_01_01().build();
		accessRelationTransport.setValidfrom(null);

		this.testError(transport, accessRelationTransport, ErrorCode.VALIDFROM_NOT_DEFINED);
	}

	@Test
	public void test_NoGroupForAccessRelation_Error() throws Exception {
		final UserTransport transport = new UserTransportBuilder().forNewUser().build();
		final AccessRelationTransport accessRelationTransport = new AccessRelationTransportBuilder()
				.forNewUser_2000_01_01().build();
		accessRelationTransport.setRefId(null);

		this.testError(transport, accessRelationTransport, ErrorCode.GROUP_MUST_BE_SPECIFIED);
	}

	@Test
	public void test_OnlyAdminAllowed_ErrorResponse() throws Exception {
		this.userName = UserTransportBuilder.USER1_NAME;
		this.userPassword = UserTransportBuilder.USER1_PASSWORD;

		final CreateUserRequest request = new CreateUserRequest();
		final ErrorResponse actual = super.callUsecaseWithContent("", this.method, request, false, ErrorResponse.class);

		Assertions.assertEquals(Integer.valueOf(ErrorCode.USER_IS_NO_ADMIN.getErrorCode()), actual.getCode());

	}

	@Test
	public void test_AuthorizationRequired_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;
		final ErrorResponse actual = super.callUsecaseWithoutContent("", this.method, false, ErrorResponse.class);
		Assertions.assertEquals(super.accessDeniedErrorResponse(), actual);
	}

}