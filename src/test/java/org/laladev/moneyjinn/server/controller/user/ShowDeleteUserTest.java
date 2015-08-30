package org.laladev.moneyjinn.server.controller.user;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.laladev.moneyjinn.core.rest.model.transport.AccessRelationTransport;
import org.laladev.moneyjinn.core.rest.model.transport.GroupTransport;
import org.laladev.moneyjinn.core.rest.model.user.ShowDeleteUserResponse;
import org.laladev.moneyjinn.server.builder.AccessRelationTransportBuilder;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;
import org.laladev.moneyjinn.server.controller.AbstractControllerTest;
import org.springframework.http.HttpMethod;

public class ShowDeleteUserTest extends AbstractControllerTest {

	private final HttpMethod method = HttpMethod.GET;

	@Override
	protected String getUsecase() {
		return super.getUsecaseFromTestClassName("user", this.getClass());
	}

	@Test
	public void test_unknownUser_emptyResponseObject() throws Exception {
		final ShowDeleteUserResponse expected = new ShowDeleteUserResponse();
		final ShowDeleteUserResponse actual = super.callUsecaseWithoutContent(
				"/" + UserTransportBuilder.NON_EXISTING_ID, this.method, false, ShowDeleteUserResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_User1_completeResponseObject() throws Exception {
		final ShowDeleteUserResponse expected = new ShowDeleteUserResponse();
		expected.setUserTransport(new UserTransportBuilder().forUser1().build());
		final List<GroupTransport> groupTransports = new ArrayList<>();
		groupTransports.add(new GroupTransportBuilder().forAdminGroup().build());
		groupTransports.add(new GroupTransportBuilder().forGroup1().build());
		groupTransports.add(new GroupTransportBuilder().forGroup2().build());
		expected.setGroupTransports(groupTransports);
		final List<AccessRelationTransport> accessRelationTransports = new ArrayList<>();
		accessRelationTransports.add(new AccessRelationTransportBuilder().forUser1_2000_01_01().build());
		accessRelationTransports.add(new AccessRelationTransportBuilder().forUser1_2600_01_01().build());
		accessRelationTransports.add(new AccessRelationTransportBuilder().forUser1_2700_01_01().build());
		accessRelationTransports.add(new AccessRelationTransportBuilder().forUser1_2800_01_01().build());
		expected.setAccessRelationTransports(accessRelationTransports);

		final ShowDeleteUserResponse actual = super.callUsecaseWithoutContent("/" + UserTransportBuilder.USER1_ID,
				this.method, false, ShowDeleteUserResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_User2_completeResponseObject() throws Exception {
		final ShowDeleteUserResponse expected = new ShowDeleteUserResponse();
		expected.setUserTransport(new UserTransportBuilder().forUser2().build());
		final List<GroupTransport> groupTransports = new ArrayList<>();
		groupTransports.add(new GroupTransportBuilder().forAdminGroup().build());
		groupTransports.add(new GroupTransportBuilder().forGroup1().build());
		groupTransports.add(new GroupTransportBuilder().forGroup2().build());
		expected.setGroupTransports(groupTransports);
		final List<AccessRelationTransport> accessRelationTransports = new ArrayList<>();
		accessRelationTransports.add(new AccessRelationTransportBuilder().forUser2_2000_01_01().build());
		expected.setAccessRelationTransports(accessRelationTransports);

		final ShowDeleteUserResponse actual = super.callUsecaseWithoutContent("/" + UserTransportBuilder.USER2_ID,
				this.method, false, ShowDeleteUserResponse.class);

		Assert.assertEquals(expected, actual);
	}

}