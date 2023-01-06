package org.laladev.moneyjinn.core.rest.model.wsevent;

import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.Objects;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;

@XmlRootElement(name = "capitalsourceChangedEventTransport")
public class CapitalsourceChangedEventTransport {
  private String eventType;
  private CapitalsourceTransport capitalsourceTransport;

  public String getEventType() {
    return this.eventType;
  }

  public void setEventType(final String eventType) {
    this.eventType = eventType;
  }

  public CapitalsourceTransport getCapitalsourceTransport() {
    return this.capitalsourceTransport;
  }

  public void setCapitalsourceTransport(final CapitalsourceTransport capitalsourceTransport) {
    this.capitalsourceTransport = capitalsourceTransport;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.capitalsourceTransport, this.eventType);
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
    final CapitalsourceChangedEventTransport other = (CapitalsourceChangedEventTransport) obj;
    return Objects.equals(this.capitalsourceTransport, other.capitalsourceTransport)
        && Objects.equals(this.eventType, other.eventType);
  }

  @Override
  public String toString() {
    return "CapitalsourceChangedEventTransport [eventType=" + this.eventType
        + ", capitalsourceTransport=" + this.capitalsourceTransport + "]";
  }
}
