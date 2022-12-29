
package org.laladev.moneyjinn.server.jwt;

import jakarta.inject.Inject;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
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
    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
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
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .apply(new JwtConfigurer(this.jwtTokenProvider))
        .and()
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/moneyflow/server/user/login").permitAll()
            .requestMatchers("/moneyflow/server/user/refreshToken").permitAll()
            .requestMatchers("/moneyflow/server/importedbalance/createImportedBalance").permitAll()
            .requestMatchers("/moneyflow/server/importedmoneyflow/createImportedMoneyflow").permitAll()
            .requestMatchers("/moneyflow/server/importedmonthlysettlement/createImportedMonthlySettlement").permitAll()
            // Admin: Group
            .requestMatchers("/moneyflow/server/group/createGroup").hasAuthority("ADMIN")
            .requestMatchers("/moneyflow/server/group/updateGroup").hasAuthority("ADMIN")
            .requestMatchers("/moneyflow/server/group/deleteGroupById/**").hasAuthority("ADMIN")
            .requestMatchers("/moneyflow/server/group/showDeleteGroup/**").hasAuthority("ADMIN")
            .requestMatchers("/moneyflow/server/group/showEditGroup/**").hasAuthority("ADMIN")
            .requestMatchers("/moneyflow/server/group/showGroupList").hasAuthority("ADMIN")
            // Admin: PostingAccount
            .requestMatchers("/moneyflow/server/postingaccount/createPostingAccount").hasAuthority("ADMIN")
            .requestMatchers("/moneyflow/server/postingaccount/updatePostingAccount").hasAuthority("ADMIN")
            .requestMatchers("/moneyflow/server/postingaccount/deletePostingAccountById/**").hasAuthority("ADMIN")
            .requestMatchers("/moneyflow/server/postingaccount/showDeletePostingAccount/**").hasAuthority("ADMIN")
            .requestMatchers("/moneyflow/server/postingaccount/showEditPostingAccount/**").hasAuthority("ADMIN")
            // Admin: User
            .requestMatchers("/moneyflow/server/user/createUser").hasAuthority("ADMIN")
            .requestMatchers("/moneyflow/server/user/updateUser").hasAuthority("ADMIN")
            .requestMatchers("/moneyflow/server/user/deleteUserById/**").hasAuthority("ADMIN")
            .requestMatchers("/moneyflow/server/user/showCreateUser").hasAuthority("ADMIN")
            .requestMatchers("/moneyflow/server/user/showDeleteUser/**").hasAuthority("ADMIN")
            .requestMatchers("/moneyflow/server/user/showEditUser/**").hasAuthority("ADMIN")
            .requestMatchers("/moneyflow/server/user/showUserList/**").hasAuthority("ADMIN")
            // Regular User: everything else
            .requestMatchers("/moneyflow/server/**").hasAuthority("LOGIN")
            // Whatever else you trying: deny
            .requestMatchers("/**").denyAll()
            .anyRequest().authenticated()
        );
    //@formatter:on
    return http.build();
  }
}