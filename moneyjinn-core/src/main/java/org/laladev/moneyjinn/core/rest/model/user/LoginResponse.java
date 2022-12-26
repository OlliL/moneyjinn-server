
package org.laladev.moneyjinn.core.rest.model.user;

import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import org.laladev.moneyjinn.core.rest.model.transport.UserTransport;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "loginResponse")
public class LoginResponse extends ValidationResponse {
  public UserTransport userTransport;
  public String token;
  public String refreshToken;

  public final UserTransport getUserTransport() {
    return this.userTransport;
  }

  public final void setUserTransport(final UserTransport userTransport) {
    this.userTransport = userTransport;
  }

  public final String getToken() {
    return this.token;
  }

  public final void setToken(final String token) {
    this.token = token;
  }

  public final String getRefreshToken() {
    return this.refreshToken;
  }

  public final void setRefreshToken(final String refreshToken) {
    this.refreshToken = refreshToken;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((this.refreshToken == null) ? 0 : this.refreshToken.hashCode());
    result = prime * result + ((this.token == null) ? 0 : this.token.hashCode());
    result = prime * result + ((this.userTransport == null) ? 0 : this.userTransport.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    final LoginResponse other = (LoginResponse) obj;
    if (this.refreshToken == null) {
      if (other.refreshToken != null) {
        return false;
      }
    } else if (!this.refreshToken.equals(other.refreshToken)) {
      return false;
    }
    if (this.token == null) {
      if (other.token != null) {
        return false;
      }
    } else if (!this.token.equals(other.token)) {
      return false;
    }
    if (this.userTransport == null) {
      if (other.userTransport != null) {
        return false;
      }
    } else if (!this.userTransport.equals(other.userTransport)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append("LoginResponse [userTransport=");
    builder.append(this.userTransport);
    builder.append(", token=");
    builder.append(this.token);
    builder.append(", refreshToken=");
    builder.append(this.refreshToken);
    builder.append("]");
    return builder.toString();
  }
}
