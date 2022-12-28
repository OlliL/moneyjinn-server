
package org.laladev.moneyjinn.core.rest.model.user;

import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.Objects;
import org.laladev.moneyjinn.core.rest.model.transport.UserTransport;

@XmlRootElement(name = "showEditUserResponse")
public class ShowEditUserResponse extends AbstractUpdateUserResponse {
  private UserTransport userTransport;

  public UserTransport getUserTransport() {
    return this.userTransport;
  }

  public void setUserTransport(final UserTransport userTransport) {
    this.userTransport = userTransport;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + Objects.hash(this.userTransport);
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
    final ShowEditUserResponse other = (ShowEditUserResponse) obj;
    return Objects.equals(this.userTransport, other.userTransport);
  }

  @Override
  public String toString() {
    return "ShowEditUserResponse [userTransport=" + this.userTransport
        + ", getAccessRelationTransports()=" + this.getAccessRelationTransports() + ", getUserId()="
        + this.getUserId() + ", getGroupTransports()=" + this.getGroupTransports()
        + ", getResult()=" + this.getResult() + ", getValidationItemTransports()="
        + this.getValidationItemTransports() + "]";
  }

}
