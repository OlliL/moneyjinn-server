
package org.laladev.moneyjinn.server.controller.user;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.server.builder.AccessRelationTransportBuilder;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractAdminUserControllerTest;
import org.laladev.moneyjinn.server.controller.api.UserControllerApi;
import org.laladev.moneyjinn.server.model.AccessRelationTransport;
import org.laladev.moneyjinn.server.model.GroupTransport;
import org.laladev.moneyjinn.server.model.ShowUserListResponse;
import org.laladev.moneyjinn.server.model.UserTransport;
import org.laladev.moneyjinn.service.api.IUserService;

import jakarta.inject.Inject;

class ShowUserListTest extends AbstractAdminUserControllerTest {
	@Inject
	private IUserService userService;

	@Override
	protected void loadMethod() {
		super.getMock(UserControllerApi.class).showUserList();
	}

	private ShowUserListResponse getCompleteResponse() {
		final ShowUserListResponse expected = new ShowUserListResponse();
		final List<UserTransport> userTransports = new ArrayList<>();
		userTransports.add(new UserTransportBuilder().forAdmin().build());
		userTransports.add(new UserTransportBuilder().forUser1().build());
		userTransports.add(new UserTransportBuilder().forUser2().build());
		userTransports.add(new UserTransportBuilder().forUser3().build());
		userTransports.add(new UserTransportBuilder().forImportUser().build());
		expected.setUserTransports(userTransports);
		final List<GroupTransport> groupTransports = new ArrayList<>();
		groupTransports.add(new GroupTransportBuilder().forGroup1().build());
		groupTransports.add(new GroupTransportBuilder().forAdminGroup().build());
		expected.setGroupTransports(groupTransports);
		final List<AccessRelationTransport> accessRelationTransports = new ArrayList<>();
		accessRelationTransports.add(new AccessRelationTransportBuilder().forAdminUser().build());
		accessRelationTransports.add(new AccessRelationTransportBuilder().forUser1_2000_01_01().build());
		accessRelationTransports.add(new AccessRelationTransportBuilder().forUser2_2000_01_01().build());
		accessRelationTransports.add(new AccessRelationTransportBuilder().forUser3_2000_01_01().build());
		accessRelationTransports.add(new AccessRelationTransportBuilder().forImportUser_2000_01_01().build());
		expected.setAccessRelationTransports(accessRelationTransports);
		return expected;
	}

	@Test
	void test_default_FullResponseObject() throws Exception {
		final ShowUserListResponse expected = this.getCompleteResponse();
		final ShowUserListResponse actual = super.callUsecaseExpect200(ShowUserListResponse.class);
		Assertions.assertEquals(expected, actual);
	}

	@Override
	protected void callUsecaseExpect403ForThisUsecase() throws Exception {
		super.callUsecaseExpect403();
	}

	@Override
	protected void callUsecaseEmptyDatabase() throws Exception {
		// Users are always there
	}

}
