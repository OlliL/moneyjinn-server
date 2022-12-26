
package org.laladev.moneyjinn.core.rest.model.group;

import java.util.Objects;
import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "createGroupResponse")
public class CreateGroupResponse extends ValidationResponse {
  private Long groupId;

  public Long getGroupId() {
    return this.groupId;
  }

  public void setGroupId(final Long groupId) {
    this.groupId = groupId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + Objects.hash(this.groupId);
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
    final CreateGroupResponse other = (CreateGroupResponse) obj;
    return Objects.equals(this.groupId, other.groupId);
  }

  @Override
  public String toString() {
    return "CreateGroupResponse [groupId=" + this.groupId + "]";
  }
}
