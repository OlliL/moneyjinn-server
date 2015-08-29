package org.laladev.moneyjinn.server.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.laladev.moneyjinn.api.MoneyjinnProfiles;
import org.laladev.moneyjinn.server.config.MoneyjinnConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { MoneyjinnConfiguration.class })
@WebAppConfiguration
@EnableAutoConfiguration
@ActiveProfiles(MoneyjinnProfiles.TEST)
@SqlGroup({ @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = { "classpath:h2defaults.sql",
		"classpath:testdata.sql" }) })
public abstract class AbstractControllerTest {
	@Inject
	private ObjectMapper objectMapper;
	@Inject
	private MockMvc mvc;

	private MockMvc mvc() {
		return this.mvc;
	}

	private <T> T map(final String string, final Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		return this.objectMapper.readValue(string, clazz);
	}

	protected abstract String getUsecaseRoot();

	protected <T> T callUsecaseStatusOkWithResponse(final String uri, final Class<T> clazz) throws Exception {
		final MvcResult result = this.mvc().perform(MockMvcRequestBuilders
				.get("/moneyflow/" + this.getUsecaseRoot() + uri).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		final String content = result.getResponse().getContentAsString();

		Assert.assertNotNull(content);

		final T actual = this.map(content, clazz);

		return actual;

	}
}
