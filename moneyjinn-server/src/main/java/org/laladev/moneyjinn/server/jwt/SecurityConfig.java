
package org.laladev.moneyjinn.server.jwt;

import jakarta.inject.Inject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

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
        .requestMatchers(HttpMethod.OPTIONS, "/moneyflow/server/**").permitAll()
        .requestMatchers("/moneyflow/server/user/login").permitAll()
        .requestMatchers("/moneyflow/server/user/refreshToken").permitAll()
        .requestMatchers("/moneyflow/server/importedbalance/createImportedBalance").permitAll()
        .requestMatchers("/moneyflow/server/importedmoneyflow/createImportedMoneyflow").permitAll()
        .requestMatchers("/moneyflow/server/importedmonthlysettlement/createImportedMonthlySettlement").permitAll()
        .requestMatchers("/moneyflow/server/postingaccount/showEditPostingAccount/**").hasAuthority("ADMIN")
        // GROUP
        .requestMatchers("/moneyflow/server/group/createGroup").hasAuthority("ADMIN")
        .requestMatchers("/moneyflow/server/group/updateGroup").hasAuthority("ADMIN")
        .requestMatchers("/moneyflow/server/group/deleteGroupById/**").hasAuthority("ADMIN")
        .requestMatchers("/moneyflow/server/group/showDeleteGroup/**").hasAuthority("ADMIN")
        .requestMatchers("/moneyflow/server/group/showEditGroup/**").hasAuthority("ADMIN")
        .requestMatchers("/moneyflow/server/group/showGroupList").hasAuthority("ADMIN")
        // POSTINGACCOUNT
        .requestMatchers("/moneyflow/server/postingaccount/createPostingAccount").hasAuthority("ADMIN")
        .requestMatchers("/moneyflow/server/postingaccount/updatePostingAccount").hasAuthority("ADMIN")
        .requestMatchers("/moneyflow/server/postingaccount/deletePostingAccountById/**").hasAuthority("ADMIN")
        .requestMatchers("/moneyflow/server/postingaccount/showDeletePostingAccount/**").hasAuthority("ADMIN")
        .requestMatchers("/moneyflow/server/postingaccount/showEditPostingAccount/**").hasAuthority("ADMIN")
         // USER
        .requestMatchers("/moneyflow/server/user/createUser").hasAuthority("ADMIN")
        .requestMatchers("/moneyflow/server/user/updateUser").hasAuthority("ADMIN")
        .requestMatchers("/moneyflow/server/user/deleteUserById/**").hasAuthority("ADMIN")
        .requestMatchers("/moneyflow/server/user/showCreateUser").hasAuthority("ADMIN")
        .requestMatchers("/moneyflow/server/user/showDeleteUser/**").hasAuthority("ADMIN")
        .requestMatchers("/moneyflow/server/user/showEditUser/**").hasAuthority("ADMIN")
        .requestMatchers("/moneyflow/server/user/showUserList/**").hasAuthority("ADMIN")
        // SETTINGS
        .requestMatchers("/moneyflow/server/setting/showDefaultSettings").hasAuthority("ADMIN")
        .requestMatchers("/moneyflow/server/setting/updateDefaultSettings").hasAuthority("ADMIN")
        // every other non-ADMIN request
        .requestMatchers("/moneyflow/server/**").hasAuthority("LOGIN")
        .requestMatchers("/**").denyAll()
        .anyRequest().authenticated()
    );
    //@formatter:on
    return http.build();
  }
}