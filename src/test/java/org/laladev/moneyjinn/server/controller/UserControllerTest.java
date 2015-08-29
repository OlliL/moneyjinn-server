package org.laladev.moneyjinn.server.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.laladev.moneyjinn.core.rest.model.transport.AccessRelationTransport;
import org.laladev.moneyjinn.core.rest.model.transport.GroupTransport;
import org.laladev.moneyjinn.core.rest.model.user.ShowEditUserResponse;
import org.laladev.moneyjinn.server.builder.AccessRelationTransportBuilder;
import org.laladev.moneyjinn.server.builder.GroupTransportBuilder;
import org.laladev.moneyjinn.server.builder.UserTransportBuilder;

public class UserControllerTest extends AbstractControllerTest {

	@Override
	protected String getUsecaseRoot() {
		return "user";
	}

	@Test
	public void showEditUser_unknownUser_emptyResponseObject() throws Exception {
		final ShowEditUserResponse expected = new ShowEditUserResponse();
		final ShowEditUserResponse actual = super.callUsecaseStatusOkWithResponse("/showEditUser/666",
				ShowEditUserResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void showEditUser_User1_completeResponseObject() throws Exception {
		final ShowEditUserResponse expected = new ShowEditUserResponse();
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

		final ShowEditUserResponse actual = super.callUsecaseStatusOkWithResponse("/showEditUser/3",
				ShowEditUserResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void showEditUser_User2_completeResponseObject() throws Exception {
		final ShowEditUserResponse expected = new ShowEditUserResponse();
		expected.setUserTransport(new UserTransportBuilder().forUser2().build());
		final List<GroupTransport> groupTransports = new ArrayList<>();
		groupTransports.add(new GroupTransportBuilder().forAdminGroup().build());
		groupTransports.add(new GroupTransportBuilder().forGroup1().build());
		groupTransports.add(new GroupTransportBuilder().forGroup2().build());
		expected.setGroupTransports(groupTransports);
		final List<AccessRelationTransport> accessRelationTransports = new ArrayList<>();
		accessRelationTransports.add(new AccessRelationTransportBuilder().forUser2_2000_01_01().build());
		expected.setAccessRelationTransports(accessRelationTransports);

		final ShowEditUserResponse actual = super.callUsecaseStatusOkWithResponse("/showEditUser/4",
				ShowEditUserResponse.class);

		Assert.assertEquals(expected, actual);
	}

}