
package org.laladev.moneyjinn.server.jwt;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

public class RefreshOnlyGrantedAuthority implements GrantedAuthority {
  private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
  public static final String ROLE = "refreshOnly";

  @Override
  public String getAuthority() {
    return ROLE;
  }
}