package org.laladev.moneyjinn.server.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class UserControllerTest extends AbstractControllerTest {

	@Test
	public void showEditUser_unknownUser_emptyResponseObject() throws Exception {
		final MvcResult result = super.mvc().perform(
				MockMvcRequestBuilders.get("/moneyflow/user/showEditUser/666").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		final String content = result.getResponse().getContentAsString();

		Assert.assertNotNull(content);

		final ShowEditUserResponse expected = new ShowEditUserResponse();
		final ShowEditUserResponse actual = super.map(content, ShowEditUserResponse.class);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void showEditUser_User1_completeResponseObject() throws Exception {
		final MvcResult result = super.mvc()
				.perform(
						MockMvcRequestBuilders.get("/moneyflow/user/showEditUser/3").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		final String content = result.getResponse().getContentAsString();

		Assert.assertNotNull(content);

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

		final ShowEditUserResponse actual = super.map(content, ShowEditUserResponse.class);

		Assert.assertEquals(expected, actual);
	}
}