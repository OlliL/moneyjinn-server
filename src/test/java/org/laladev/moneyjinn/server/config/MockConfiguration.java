package org.laladev.moneyjinn.server.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Configuration
public class MockConfiguration {

	@Inject
	private WebApplicationContext applicationContext;

	@Bean
	public MockMvc mockMvc() {
		// TODO: this will only work if we get the RequestFilter to work to set the Auth
		// headers.....
		// return MockMvcBuilders.webAppContextSetup(this.applicationContext).addFilter(new
		// BufferFilter(), "/moneyflow/*")
		// .build();
		return MockMvcBuilders.webAppContextSetup(this.applicationContext).build();
	}

}
