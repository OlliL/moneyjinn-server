
package org.laladev.moneyjinn.server.jwt;

import java.util.ArrayList;
import java.util.List;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.service.api.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {
  @Autowired
  private IUserService userService;
  @Autowired
  private PasswordEncoder encoder;

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    final User user = this.userService.getUserByName(username);
    if (user != null) {
      final String password = this.encoder.encode(user.getPassword());
      final List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
      user.getPermissions().stream()
          .forEach(p -> grantedAuthorities.add(new SimpleGrantedAuthority(p.name())));
      final org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
          user.getName(), password, grantedAuthorities);
      return userDetails;
    }
    return null;
  }
}