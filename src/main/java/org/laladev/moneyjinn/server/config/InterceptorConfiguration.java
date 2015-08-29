package org.laladev.moneyjinn.server.config;

import org.laladev.moneyjinn.server.main.MoneyJinnHandlerInterceptor;
import org.laladev.moneyjinn.server.main.SessionEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class InterceptorConfiguration extends WebMvcConfigurerAdapter {

	@Bean
	public MoneyJinnHandlerInterceptor moneyjinnHandlerInterceptor() {
		return new MoneyJinnHandlerInterceptor();
	}

	@Bean
	@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
	public SessionEnvironment sessionEnvironment() {
		return new SessionEnvironment();
	}

	@Override
	public void addInterceptors(final InterceptorRegistry registry) {
		registry.addInterceptor(this.moneyjinnHandlerInterceptor());
	}

}
