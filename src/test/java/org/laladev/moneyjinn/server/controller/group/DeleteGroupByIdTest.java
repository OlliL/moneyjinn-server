package org.laladev.moneyjinn.server.controller.group;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.laladev.moneyjinn.businesslogic.model.ErrorCode;
import org.laladev.moneyjinn.businesslogic.model.access.Group;
import org.laladev.moneyjinn.businesslogic.model.access.GroupID;
import org.laladev.moneyjinn.businesslogic.service.api.IGroupService;
import org.laladev.moneyjinn.core.rest.model.ErrorResponse;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;

public class DeleteGroupByIdTest extends AbstractControllerTest {

	@Inject
	IGroupService groupService;

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
		return super.getUsecaseFromTestClassName("group", this.getClass());
	}

	@Test
	public void test_regularGroupNoData_SuccessfullNoContent() throws Exception {
		Group group = this.groupService.getGroupById(new GroupID(GroupTransportBuilder.GROUP3_ID));

		Assert.assertNotNull(group);

		super.callUsecaseWithoutContent("/" + GroupTransportBuilder.GROUP3_ID, this.method, true, Object.class);

		group = this.groupService.getGroupById(new GroupID(GroupTransportBuilder.GROUP3_ID));

		Assert.assertNull(group);
	}

	@Test
	public void test_nonExistingGroup_SuccessfullNoContent() throws Exception {
		Group group = this.groupService.getGroupById(new GroupID(GroupTransportBuilder.NON_EXISTING_ID));

		Assert.assertNull(group);

		super.callUsecaseWithoutContent("/" + GroupTransportBuilder.NON_EXISTING_ID, this.method, true, Object.class);

		group = this.groupService.getGroupById(new GroupID(GroupTransportBuilder.NON_EXISTING_ID));

		Assert.assertNull(group);
	}

	@Test
	public void test_regularGroupWithData_SuccessfullNoContent() throws Exception {
		final ErrorResponse expected = new ErrorResponse();
		expected.setCode(ErrorCode.GROUP_IN_USE.getErrorCode());
		expected.setMessage("You may not delete a group while there where/are users assigned to it!");

		Group group = this.groupService.getGroupById(new GroupID(GroupTransportBuilder.GROUP1_ID));

		Assert.assertNotNull(group);

		final ErrorResponse response = super.callUsecaseWithoutContent("/" + GroupTransportBuilder.GROUP1_ID,
				this.method, false, ErrorResponse.class);

		group = this.groupService.getGroupById(new GroupID(GroupTransportBuilder.GROUP1_ID));

		Assert.assertNotNull(group);

		Assert.assertEquals(expected, response);
	}

	@Test
	public void test_OnlyAdminAllowed_ErrorResponse() throws Exception {
		this.userName = UserTransportBuilder.USER1_NAME;
		this.userPassword = UserTransportBuilder.USER1_PASSWORD;

		final ErrorResponse actual = super.callUsecaseWithoutContent("/" + GroupTransportBuilder.GROUP1_ID, this.method,
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