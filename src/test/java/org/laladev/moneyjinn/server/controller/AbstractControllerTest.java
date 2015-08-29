package org.laladev.moneyjinn.server.controller;

import java.io.IOException;

import javax.inject.Inject;

import org.junit.runner.RunWith;
import org.laladev.moneyjinn.api.MoneyjinnProfiles;
import org.laladev.moneyjinn.server.config.MoneyjinnConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { MoneyjinnConfiguration.class })
@WebAppConfiguration
@EnableAutoConfiguration
@ActiveProfiles(MoneyjinnProfiles.TEST)
public class AbstractControllerTest {
	@Inject
	private ObjectMapper objectMapper;
	@Inject
	private MockMvc mvc;

	public MockMvc mvc() {
		return this.mvc;
	}

	protected <T> T map(final String string, final Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		return this.objectMapper.readValue(string, clazz);
	}
}
