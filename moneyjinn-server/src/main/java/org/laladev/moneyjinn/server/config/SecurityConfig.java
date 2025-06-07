//
// Copyright (c) 2022-2025 Oliver Lehmann <lehmann@ans-netz.de>
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
// ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
// OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
// OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE.
//

package org.laladev.moneyjinn.server.config;

import static org.springframework.security.config.Customizer.withDefaults;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.laladev.moneyjinn.model.access.UserRole;
import org.laladev.moneyjinn.server.config.jwt.JwtConfigurer;
import org.laladev.moneyjinn.server.config.jwt.JwtTokenProvider;
import org.laladev.moneyjinn.server.config.jwt.RefreshOnlyGrantedAuthority;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.inject.Inject;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class SecurityConfig {
	private final JwtTokenProvider jwtTokenProvider;

	@Value("#{'${org.laladev.moneyjinn.server.cors.allowed-origins}'.split(',')}")
	private List<String> allowedOrigins;

	private static final String API_ROOT = "/moneyflow/server";

	private static final String LOGIN_ENDPOINT = API_ROOT + "/user/login";

	@Bean
	public AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(this.allowedOrigins);
		configuration.setAllowedMethods(Arrays.asList(HttpMethod.GET.name(), HttpMethod.POST.name(),
				HttpMethod.PUT.name(), HttpMethod.DELETE.name()));
		configuration.setAllowCredentials(true);
		configuration.setAllowedHeaders(
				Arrays.asList(HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE, "x-csrf-token", "x-xsrf-token"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration(API_ROOT + "/**", configuration);
		return source;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
		final PathPatternRequestMatcher.Builder mvc = PathPatternRequestMatcher.withDefaults();
	//@formatter:off
    http.logout(logout -> {logout.logoutUrl(API_ROOT + "/logout"); logout.logoutSuccessHandler((new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT)));})
        .httpBasic(AbstractHttpConfigurer::disable)
        .cors(withDefaults())
        .csrf(configurer -> {
          configurer.csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler());
          configurer.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
          configurer.ignoringRequestMatchers(mvc.matcher(LOGIN_ENDPOINT));
        })
        .authorizeHttpRequests(auth -> auth
          .requestMatchers(mvc.matcher(LOGIN_ENDPOINT)).permitAll()
          .requestMatchers(mvc.matcher("/websocket")).permitAll()
          .requestMatchers(mvc.matcher("/actuator/**")).permitAll()
          .requestMatchers(mvc.matcher(API_ROOT + "/user/refreshToken")).hasAuthority(RefreshOnlyGrantedAuthority.ROLE)
          .requestMatchers(mvc.matcher(API_ROOT + "/importedbalance/createImportedBalance")).hasAnyAuthority(UserRole.IMPORT.name(), UserRole.ADMIN.name())
          .requestMatchers(mvc.matcher(API_ROOT + "/importedmoneyflow/createImportedMoneyflow")).hasAnyAuthority(UserRole.IMPORT.name(), UserRole.ADMIN.name())
          .requestMatchers(mvc.matcher(API_ROOT + "/importedmonthlysettlement/createImportedMonthlySettlement")).hasAnyAuthority(UserRole.IMPORT.name(), UserRole.ADMIN.name())
          .requestMatchers(mvc.matcher(API_ROOT + "/**")).hasAnyAuthority(UserRole.STANDARD.name(), UserRole.ADMIN.name())
          // Whatever else you trying: deny
          .requestMatchers(mvc.matcher("/**")).denyAll()
          .anyRequest().authenticated()
        )
        .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
        .with(new JwtConfigurer(this.jwtTokenProvider), withDefaults());
    //@formatter:on
		return http.build();
	}

	private static final class CsrfCookieFilter extends OncePerRequestFilter {

		@Override
		protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
				final FilterChain filterChain) throws ServletException, IOException {
			final CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
			// Render the token value to a cookie by causing the deferred token to be loaded
			csrfToken.getToken();

			filterChain.doFilter(request, response);
		}

	}
}