package org.laladev.moneyjinn.server.config;

import org.laladev.moneyjinn.core.rest.util.MyObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class ObjectMapperConfiguration {

	@Bean
	public ObjectMapper objectMapper() {
		final ObjectMapper objectMapper = new MyObjectMapper();
		return objectMapper;
	}
}
