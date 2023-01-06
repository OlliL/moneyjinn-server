
package org.laladev.moneyjinn.server.config;

import jakarta.inject.Inject;
import java.util.Arrays;
import java.util.List;
import org.laladev.moneyjinn.server.config.jwt.JwtConfigurer;
import org.laladev.moneyjinn.server.config.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
  @Inject
  JwtTokenProvider jwtTokenProvider;

  @Value("#{'${org.laladev.moneyjinn.server.cors.allowed-origins}'.split(',')}")
  private List<String> allowedOrigins;

  @Bean
  public AuthenticationManager authenticationManager(
      final AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    final CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(this.allowedOrigins);
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
    configuration.setAllowCredentials(true);
    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "x-csrf-token"));
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/moneyflow/server/**", configuration);
    return source;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
    //@formatter:off
    http
        .httpBasic().disable()
        .cors().and()
        .csrf(configurer -> {
           // FIX for https://github.com/spring-projects/spring-security/issues/12378
           configurer.csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler());
           configurer.ignoringRequestMatchers("/moneyflow/server/user/login", "/moneyflow/server/user/refreshToken");
        })
        .apply(new JwtConfigurer(this.jwtTokenProvider))
        .and()
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/websocket").permitAll()
            .requestMatchers("/moneyflow/server/csrf/csrf").permitAll()
            .requestMatchers("/moneyflow/server/user/login").permitAll()
            .requestMatchers("/moneyflow/server/user/refreshToken").permitAll()
            .requestMatchers("/moneyflow/server/importedbalance/createImportedBalance").permitAll()
            .requestMatchers("/moneyflow/server/importedmoneyflow/createImportedMoneyflow").permitAll()
            .requestMatchers("/moneyflow/server/importedmonthlysettlement/createImportedMonthlySettlement").permitAll()
            .requestMatchers("/moneyflow/server/**").hasAuthority("LOGIN")
            // Whatever else you trying: deny
            .requestMatchers("/**").denyAll()
            .anyRequest().authenticated()
        );
    //@formatter:on
    return http.build();
  }
}