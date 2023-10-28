
package org.laladev.moneyjinn.server.controller.group;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractAdminUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.GroupControllerApi;
import org.laladev.moneyjinn.server.model.ErrorResponse;
import org.laladev.moneyjinn.service.api.IGroupService;

import jakarta.inject.Inject;

class DeleteGroupByIdTest extends AbstractAdminUserControllerTest {
	@Inject
	private IGroupService groupService;

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

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403WithUriVariables(GroupTransportBuilder.GROUP1_ID);
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		// Groups are always there.
	}
}