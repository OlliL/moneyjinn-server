package org.laladev.moneyjinn.server.config;

import org.laladev.moneyjinn.core.rest.util.RESTAuthorization;
import org.laladev.moneyjinn.server.main.BufferFilter;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class BeanProducer {

	@Bean
	public RESTAuthorization restAuthorization() {
		return new RESTAuthorization();
	}

	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {

		return new EmbeddedServletContainerCustomizer() {
			@Override
			public void customize(final ConfigurableEmbeddedServletContainer container) {

				final ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/401.html");
				final ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
				final ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500.html");

				container.addErrorPages(error401Page, error404Page, error500Page);
			}
		};
	}

	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(new BufferFilter());
		registrationBean.addUrlPatterns("/moneyflow/*");
		registrationBean.setOrder(1);
		return registrationBean;
	}
}
