
package org.laladev.moneyjinn.config;

import jakarta.inject.Inject;
import org.laladev.moneyjinn.server.main.BufferFilter;
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
    return MockMvcBuilders.webAppContextSetup(this.applicationContext)
        .addFilter(new BufferFilter(), "/moneyflow/*").build();
  }
}
