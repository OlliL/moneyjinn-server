
package org.laladev.moneyjinn.core.rest.model.user;

import java.util.Objects;
import org.laladev.moneyjinn.core.rest.model.AbstractRequest;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "changePasswordRequest")
public class ChangePasswordRequest extends AbstractRequest {
  private String oldPassword;
  private String password;

  public String getOldPassword() {
    return this.oldPassword;
  }

  public void setOldPassword(final String oldPassword) {
    this.oldPassword = oldPassword;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(final String password) {
    this.password = password;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.oldPassword, this.password);
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
    final ChangePasswordRequest other = (ChangePasswordRequest) obj;
    return Objects.equals(this.oldPassword, other.oldPassword)
        && Objects.equals(this.password, other.password);
  }

  @Override
  public String toString() {
    return "ChangePasswordRequest [oldPassword=" + this.oldPassword + ", password=" + this.password
        + "]";
  }
}
