
package org.laladev.moneyjinn.core.rest.model.group;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;
import org.laladev.moneyjinn.core.rest.model.AbstractResponse;
import org.laladev.moneyjinn.core.rest.model.transport.GroupTransport;

@XmlRootElement(name = "showGroupListResponse")
public class ShowGroupListResponse extends AbstractResponse {
  @XmlElement(name = "groupTransport")
  private List<GroupTransport> groupTransports;

  public List<GroupTransport> getGroupTransports() {
    return this.groupTransports;
  }

  public void setGroupTransports(final List<GroupTransport> groupTransports) {
    this.groupTransports = groupTransports;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + Objects.hash(this.groupTransports);
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
    final ShowGroupListResponse other = (ShowGroupListResponse) obj;
    return Objects.equals(this.groupTransports, other.groupTransports);
  }

  @Override
  public String toString() {
    return "ShowGroupListResponse [groupTransports=" + this.groupTransports + "]";
  }

}
