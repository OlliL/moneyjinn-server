package org.laladev.moneyjinn.core.rest.model.wsevent;

import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.Objects;
import org.laladev.moneyjinn.core.rest.model.transport.PostingAccountTransport;

@XmlRootElement(name = "postingAccountChangedEventTransport")
public class PostingAccountChangedEventTransport {
  private String eventType;
  private PostingAccountTransport postingAccountTransport;

  public String getEventType() {
    return this.eventType;
  }

  public void setEventType(final String eventType) {
    this.eventType = eventType;
  }

  public PostingAccountTransport getPostingAccountTransport() {
    return this.postingAccountTransport;
  }

  public void setPostingAccountTransport(final PostingAccountTransport postingAccountTransport) {
    this.postingAccountTransport = postingAccountTransport;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.postingAccountTransport, this.eventType);
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
    final PostingAccountChangedEventTransport other = (PostingAccountChangedEventTransport) obj;
    return Objects.equals(this.postingAccountTransport, other.postingAccountTransport)
        && Objects.equals(this.eventType, other.eventType);
  }

  @Override
  public String toString() {
    return "PostingAccountChangedEventTransport [eventType=" + this.eventType
        + ", postingAccountTransport=" + this.postingAccountTransport + "]";
  }
}
