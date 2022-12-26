
package org.laladev.moneyjinn.core.rest.model.user;

import org.laladev.moneyjinn.core.rest.model.AbstractRequest;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "loginRequest")
public class LoginRequest extends AbstractRequest {
  private String userName;
  private String userPassword;

  public final String getUserName() {
    return this.userName;
  }

  public final void setUserName(final String userName) {
    this.userName = userName;
  }

  public final String getUserPassword() {
    return this.userPassword;
  }

  public final void setUserPassword(final String userPassword) {
    this.userPassword = userPassword;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.userName == null) ? 0 : this.userName.hashCode());
    result = prime * result + ((this.userPassword == null) ? 0 : this.userPassword.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    final LoginRequest other = (LoginRequest) obj;
    if (this.userName == null) {
      if (other.userName != null) {
        return false;
      }
    } else if (!this.userName.equals(other.userName)) {
      return false;
    }
    if (this.userPassword == null) {
      if (other.userPassword != null) {
        return false;
      }
    } else if (!this.userPassword.equals(other.userPassword)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append("LoginRequest [userName=");
    builder.append(this.userName);
    builder.append(", userPassword=");
    builder.append(this.userPassword);
    builder.append("]");
    return builder.toString();
  }
}
