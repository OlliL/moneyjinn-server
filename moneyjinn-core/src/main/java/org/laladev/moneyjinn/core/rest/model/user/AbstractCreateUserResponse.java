
package org.laladev.moneyjinn.core.rest.model.user;

import java.util.List;
import java.util.Objects;
import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import org.laladev.moneyjinn.core.rest.model.transport.GroupTransport;
import jakarta.xml.bind.annotation.XmlElement;

public abstract class AbstractCreateUserResponse extends ValidationResponse {
  @XmlElement(name = "groupTransport")
  private List<GroupTransport> groupTransports;
  private Long userId;

  public Long getUserId() {
    return this.userId;
  }

  public void setUserId(final Long userId) {
    this.userId = userId;
  }

  public final List<GroupTransport> getGroupTransports() {
    return this.groupTransports;
  }

  public final void setGroupTransports(final List<GroupTransport> groupTransports) {
    this.groupTransports = groupTransports;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + Objects.hash(this.groupTransports, this.userId);
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
    final AbstractCreateUserResponse other = (AbstractCreateUserResponse) obj;
    return Objects.equals(this.groupTransports, other.groupTransports)
        && Objects.equals(this.userId, other.userId);
  }
}
