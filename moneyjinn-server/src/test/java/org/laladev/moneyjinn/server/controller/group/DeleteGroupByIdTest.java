
package org.laladev.moneyjinn.server.controller.group;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.laladev.moneyjinn.server.controller.api.GroupControllerApi;
import org.laladev.moneyjinn.server.model.ErrorResponse;
import org.laladev.moneyjinn.service.api.IGroupService;

import jakarta.inject.Inject;

class DeleteGroupByIdTest extends AbstractControllerTest {
	@Inject
	private IGroupService groupService;

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
	protected void loadMethod() {
		super.getMock(GroupControllerApi.class).deleteGroupById(null);
	}

	@Test
	void test_regularGroupNoData_SuccessfullNoContent() throws Exception {
		Group group = this.groupService.getGroupById(new GroupID(GroupTransportBuilder.GROUP3_ID));
		Assertions.assertNotNull(group);

		super.callUsecaseExpect204WithUriVariables(GroupTransportBuilder.GROUP3_ID);

		group = this.groupService.getGroupById(new GroupID(GroupTransportBuilder.GROUP3_ID));
		Assertions.assertNull(group);
	}

	@Test
	void test_nonExistingGroup_SuccessfullNoContent() throws Exception {
		Group group = this.groupService.getGroupById(new GroupID(GroupTransportBuilder.NON_EXISTING_ID));
		Assertions.assertNull(group);

		super.callUsecaseExpect204WithUriVariables(GroupTransportBuilder.NON_EXISTING_ID);

		group = this.groupService.getGroupById(new GroupID(GroupTransportBuilder.NON_EXISTING_ID));
		Assertions.assertNull(group);
	}

	@Test
	void test_regularGroupWithData_ErrorResponse() throws Exception {
		final ErrorResponse expected = new ErrorResponse();
		expected.setCode(ErrorCode.GROUP_IN_USE.getErrorCode());
		expected.setMessage("You may not delete a group while there where/are users assigned to it!");
		Group group = this.groupService.getGroupById(new GroupID(GroupTransportBuilder.GROUP1_ID));
		Assertions.assertNotNull(group);

		final ErrorResponse actual = super.callUsecaseExpect400(ErrorResponse.class, GroupTransportBuilder.GROUP1_ID);

		group = this.groupService.getGroupById(new GroupID(GroupTransportBuilder.GROUP1_ID));
		Assertions.assertNotNull(group);
		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_OnlyAdminAllowed_ErrorResponse() throws Exception {
		this.userName = UserTransportBuilder.USER1_NAME;
		this.userPassword = UserTransportBuilder.USER1_PASSWORD;

		super.callUsecaseExpect403WithUriVariables(GroupTransportBuilder.GROUP1_ID);
	}

	@Test
	void test_ImportRoleNotAllowed_ErrorResponse() throws Exception {
		this.userName = UserTransportBuilder.IMPORTUSER_NAME;
		this.userPassword = UserTransportBuilder.IMPORTUSER_PASSWORD;

		super.callUsecaseExpect403WithUriVariables(GroupTransportBuilder.GROUP1_ID);
	}

	@Test
	void test_AuthorizationRequired_Error() throws Exception {
		this.userName = null;
		this.userPassword = null;

		super.callUsecaseExpect403WithUriVariables(GroupTransportBuilder.GROUP1_ID);
	}
}