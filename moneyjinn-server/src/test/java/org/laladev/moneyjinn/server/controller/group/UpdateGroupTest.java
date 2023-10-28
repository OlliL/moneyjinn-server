
package org.laladev.moneyjinn.server.controller.group;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.ValidationItemTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractAdminUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.GroupControllerApi;
import org.laladev.moneyjinn.server.model.GroupTransport;
import org.laladev.moneyjinn.server.model.UpdateGroupRequest;
import org.laladev.moneyjinn.server.model.ValidationItemTransport;
import org.laladev.moneyjinn.server.model.ValidationResponse;
import org.laladev.moneyjinn.service.api.IGroupService;

import jakarta.inject.Inject;

class UpdateGroupTest extends AbstractAdminUserControllerTest {
	@Inject
	private IGroupService groupService;

	@Override
	protected void loadMethod() {
		super.getMock(GroupControllerApi.class).updateGroup(null);
	}

	private void testError(final GroupTransport transport, final ErrorCode errorCode) throws Exception {
		final UpdateGroupRequest request = new UpdateGroupRequest();
		request.setGroupTransport(transport);
		final List<ValidationItemTransport> validationItems = new ArrayList<>();
		validationItems.add(new ValidationItemTransportBuilder().withKey(transport.getId().toString())
				.withError(errorCode.getErrorCode()).build());
		final ValidationResponse expected = new ValidationResponse();
		expected.setValidationItemTransports(validationItems);
		expected.setResult(Boolean.FALSE);

		final ValidationResponse actual = super.callUsecaseExpect422(request, ValidationResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	void test_GroupnameAlreadyExisting_Error() throws Exception {
		final GroupTransport transport = new GroupTransportBuilder().forGroup2().build();
		transport.setName(GroupTransportBuilder.GROUP1_NAME);
		this.testError(transport, ErrorCode.GROUP_WITH_SAME_NAME_ALREADY_EXISTS);
	}

	@Test
	void test_EmptyGroupname_Error() throws Exception {
		final GroupTransport transport = new GroupTransportBuilder().forGroup2().build();
		transport.setName("");
		this.testError(transport, ErrorCode.NAME_MUST_NOT_BE_EMPTY);
	}

	@Test
	void test_standardRequest_Successfull() throws Exception {
		final UpdateGroupRequest request = new UpdateGroupRequest();
		final GroupTransport transport = new GroupTransportBuilder().forGroup1().build();
		transport.setName("hugo");
		request.setGroupTransport(transport);

		super.callUsecaseExpect204(request);

		final Group group = this.groupService.getGroupById(new GroupID(GroupTransportBuilder.GROUP1_ID));
		Assertions.assertEquals(GroupTransportBuilder.GROUP1_ID, group.getId().getId());
		Assertions.assertEquals("hugo", group.getName());
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403(new UpdateGroupRequest());
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		// Groups are always there.
	}
}