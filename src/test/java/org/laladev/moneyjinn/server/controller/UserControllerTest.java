package org.laladev.moneyjinn.server.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Assert;
import org.junit.Test;
import org.laladev.moneyjinn.core.rest.model.user.ShowEditUserResponse;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class UserControllerTest extends AbstractControllerTest {

	@Test
	public void showEditUser_unknownUser_emptyResponseObject() throws Exception {
		final MvcResult result = super.mvc().perform(
				MockMvcRequestBuilders.get("/moneyflow/user/showEditUser/100").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		final String content = result.getResponse().getContentAsString();

		Assert.assertNotNull(content);

		final ShowEditUserResponse excpected = new ShowEditUserResponse();
		final ShowEditUserResponse actual = super.map(content, ShowEditUserResponse.class);

		Assert.assertEquals(excpected, actual);
	}
}