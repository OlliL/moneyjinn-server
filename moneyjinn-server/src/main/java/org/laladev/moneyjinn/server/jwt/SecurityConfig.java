package org.laladev.moneyjinn.server.jwt;

import jakarta.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	@Inject
	JwtTokenProvider jwtTokenProvider;

	@Bean
	public AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
		//@formatter:off
        http
            .httpBasic().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .apply(new JwtConfigurer(this.jwtTokenProvider))
            .and()
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS,"/moneyflow/server/**").permitAll()
                .requestMatchers("/moneyflow/server/user/login").permitAll()
                .requestMatchers("/moneyflow/server/user/test").hasAuthority("LOGIN")
                .requestMatchers("/moneyflow/server/user/refreshToken").hasAuthority(RefreshOnlyGrantedAuthority.ROLE)
                .requestMatchers("/moneyflow/server/report/getAvailableMonth").hasAuthority("LOGIN")
                .requestMatchers("/moneyflow/server/report/getAvailableMonth/*").hasAuthority("LOGIN")
                .requestMatchers("/moneyflow/server/report/getAvailableMonth/*/*").hasAuthority("LOGIN")
                .requestMatchers("/moneyflow/server/report/listReportsV2").hasAuthority("LOGIN")
                .requestMatchers("/moneyflow/server/report/listReportsV2/*").hasAuthority("LOGIN")
                .requestMatchers("/moneyflow/server/report/listReportsV2/*/*").hasAuthority("LOGIN")
                .requestMatchers("/**").permitAll()
                .anyRequest().authenticated()
            );
        //@formatter:on
		return http.build();
	}
}