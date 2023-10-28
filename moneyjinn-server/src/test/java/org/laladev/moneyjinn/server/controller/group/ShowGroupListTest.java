
package org.laladev.moneyjinn.server.controller.group;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractAdminUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.GroupControllerApi;
import org.laladev.moneyjinn.server.model.GroupTransport;
import org.laladev.moneyjinn.server.model.ShowGroupListResponse;
import org.laladev.moneyjinn.service.api.IGroupService;

import jakarta.inject.Inject;

class ShowGroupListTest extends AbstractAdminUserControllerTest {
	@Inject
	private IGroupService groupService;

	@Override
	protected void loadMethod() {
		super.getMock(GroupControllerApi.class).showGroupList();
	}

	private ShowGroupListResponse getCompleteResponse() {
		final ShowGroupListResponse expected = new ShowGroupListResponse();
		final List<GroupTransport> groupTransports = new ArrayList<>();
		groupTransports.add(new GroupTransportBuilder().forAdminGroup().build());
		groupTransports.add(new GroupTransportBuilder().forGroup1().build());
		groupTransports.add(new GroupTransportBuilder().forGroup2().build());
		groupTransports.add(new GroupTransportBuilder().forGroup3().build());
		expected.setGroupTransports(groupTransports);
		return expected;
	}

	@Test
	void test_default_FullResponseObject() throws Exception {
		final ShowGroupListResponse expected = this.getCompleteResponse();

		final ShowGroupListResponse actual = super.callUsecaseExpect200(ShowGroupListResponse.class);

		Assertions.assertEquals(expected, actual);
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403();
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		// Groups are always there.
	}
}
