
package org.laladev.moneyjinn.server.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import jakarta.inject.Inject;

@Configuration
public class SecurityConfig {
  @Inject
  JwtTokenProvider jwtTokenProvider;

  @Bean
  public AuthenticationManager authenticationManager(
      final AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
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
                .requestMatchers("/moneyflow/server/importedbalance/createImportedBalance").permitAll()
                .requestMatchers("/moneyflow/server/importedmoneyflow/createImportedMoneyflow").permitAll()
                .requestMatchers("/moneyflow/server/importedmonthlysettlement/createImportedMonthlySettlement").permitAll()
                .requestMatchers("/moneyflow/server/**").hasAuthority("LOGIN")
                .requestMatchers("/**").denyAll()
                .anyRequest().authenticated()
            );
        //@formatter:on
    return http.build();
  }
}