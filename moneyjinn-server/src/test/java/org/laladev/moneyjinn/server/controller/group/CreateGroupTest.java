package org.laladev.moneyjinn.server.controller.group;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import org.laladev.moneyjinn.core.rest.model.group.CreateGroupRequest;
import org.laladev.moneyjinn.core.rest.model.transport.GroupTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.service.api.IGroupService;
import org.springframework.http.HttpMethod;

public class CreateGroupTest extends AbstractControllerTest {

	@Inject
	private IGroupService groupService;

	private final HttpMethod method = HttpMethod.POST;
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

	private void testError(final GroupTransport transport, final ErrorCode errorCode) throws Exception {
		final CreateGroupRequest request = new CreateGroupRequest();

		request.setGroupTransport(transport);

		final List<ValidationItemTransport> validationItems = new ArrayList<>();
		validationItems
				.add(new ValidationItemTransportBuilder().withKey(null).withError(errorCode.getErrorCode()).build());

		final ValidationResponse expected = new ValidationResponse();
		expected.setValidationItemTransports(validationItems);
		expected.setResult(Boolean.FALSE);

		final ValidationResponse actual = super.callUsecaseWithContent("", this.method, request, false,
				ValidationResponse.class);

		Assert.assertEquals(expected, actual);

	}

	@Test
	public void test_GroupnameAlreadyExisting_Error() throws Exception {

		final GroupTransport transport = new GroupTransportBuilder().forNewGroup().build();
		transport.setName(GroupTransportBuilder.GROUP1_NAME);

		this.testError(transport, ErrorCode.GROUP_WITH_SAME_NAME_ALREADY_EXISTS);
	}

	@Test
	public void test_emptyGroupname_Error() throws Exception {
		final GroupTransport transport = new GroupTransportBuilder().forNewGroup().build();
		transport.setName("");

		this.testError(transport, ErrorCode.NAME_MUST_NOT_BE_EMPTY);
	}

	@Test
	public void test_nullGroupname_Error() throws Exception {
		final GroupTransport transport = new GroupTransportBuilder().forNewGroup().build();
		transport.setName(null);

		this.testError(transport, ErrorCode.NAME_MUST_NOT_BE_EMPTY);
	}

	@Test
	public void test_standardRequest_SuccessfullNoContent() throws Exception {
		final CreateGroupRequest request = new CreateGroupRequest();

		final GroupTransport transport = new GroupTransportBuilder().forNewGroup().build();

		request.setGroupTransport(transport);

		super.callUsecaseWithContent("", this.method, request, true, Object.class);

		final Group group = this.groupService.getGroupByName(GroupTransportBuilder.NEWGROUP_NAME);

		Assert.assertEquals(GroupTransportBuilder.NEXT_ID, group.getId().getId());
		Assert.assertEquals(GroupTransportBuilder.NEWGROUP_NAME, group.getName());
	}

	@Test
	public void test_OnlyAdminAllowed_ErrorResponse() throws Exception {
		this.userName = UserTransportBuilder.USER1_NAME;
		this.userPassword = UserTransportBuilder.USER1_PASSWORD;

		final CreateGroupRequest request = new CreateGroupRequest();
		final ErrorResponse actual = super.callUsecaseWithContent("", this.method, request, false, ErrorResponse.class);

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