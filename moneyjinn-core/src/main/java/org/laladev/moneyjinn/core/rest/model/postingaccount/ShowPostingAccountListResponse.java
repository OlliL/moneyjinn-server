
package org.laladev.moneyjinn.core.rest.model.postingaccount;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;
import org.laladev.moneyjinn.core.rest.model.AbstractResponse;
import org.laladev.moneyjinn.core.rest.model.transport.PostingAccountTransport;

@XmlRootElement(name = "showPostingAccountListResponse")
public class ShowPostingAccountListResponse extends AbstractResponse {
  @XmlElement(name = "postingAccountTransport")
  private List<PostingAccountTransport> postingAccountTransports;

  public List<PostingAccountTransport> getPostingAccountTransports() {
    return this.postingAccountTransports;
  }

  public void setPostingAccountTransports(
      final List<PostingAccountTransport> postingAccountTransports) {
    this.postingAccountTransports = postingAccountTransports;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + Objects.hash(this.postingAccountTransports);
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
    final ShowPostingAccountListResponse other = (ShowPostingAccountListResponse) obj;
    return Objects.equals(this.postingAccountTransports, other.postingAccountTransports);
  }

  @Override
  public String toString() {
    return "ShowPostingAccountListResponse [postingAccountTransports="
        + this.postingAccountTransports + "]";
  }

}
