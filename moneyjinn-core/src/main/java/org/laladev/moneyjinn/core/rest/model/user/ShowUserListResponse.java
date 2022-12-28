
package org.laladev.moneyjinn.core.rest.model.user;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;
import org.laladev.moneyjinn.core.rest.model.AbstractResponse;
import org.laladev.moneyjinn.core.rest.model.transport.GroupTransport;
import org.laladev.moneyjinn.core.rest.model.transport.UserTransport;
import org.laladev.moneyjinn.core.rest.model.user.transport.AccessRelationTransport;

@XmlRootElement(name = "showUserListResponse")
public class ShowUserListResponse extends AbstractResponse {
  @XmlElement(name = "userTransport")
  private List<UserTransport> userTransports;
  @XmlElement(name = "groupTransport")
  private List<GroupTransport> groupTransports;
  @XmlElement(name = "accessRelationTransport")
  private List<AccessRelationTransport> accessRelationTransports;

  public List<UserTransport> getUserTransports() {
    return this.userTransports;
  }

  public void setUserTransports(final List<UserTransport> userTransports) {
    this.userTransports = userTransports;
  }

  public List<GroupTransport> getGroupTransports() {
    return this.groupTransports;
  }

  public void setGroupTransports(final List<GroupTransport> groupTransports) {
    this.groupTransports = groupTransports;
  }

  public List<AccessRelationTransport> getAccessRelationTransports() {
    return this.accessRelationTransports;
  }

  public void setAccessRelationTransports(
      final List<AccessRelationTransport> accessRelationTransports) {
    this.accessRelationTransports = accessRelationTransports;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result
        + Objects.hash(this.accessRelationTransports, this.groupTransports, this.userTransports);
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
    final ShowUserListResponse other = (ShowUserListResponse) obj;
    return Objects.equals(this.accessRelationTransports, other.accessRelationTransports)
        && Objects.equals(this.groupTransports, other.groupTransports)
        && Objects.equals(this.userTransports, other.userTransports);
  }

  @Override
  public String toString() {
    return "ShowUserListResponse [userTransports=" + this.userTransports + ", groupTransports="
        + this.groupTransports + ", accessRelationTransports=" + this.accessRelationTransports
        + "]";
  }
}
