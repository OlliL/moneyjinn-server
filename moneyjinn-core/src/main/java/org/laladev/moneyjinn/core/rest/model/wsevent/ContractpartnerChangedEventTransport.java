package org.laladev.moneyjinn.core.rest.model.wsevent;

import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.Objects;
import org.laladev.moneyjinn.core.rest.model.transport.ContractpartnerTransport;

@XmlRootElement(name = "contractpartnerChangedEventTransport")
public class ContractpartnerChangedEventTransport {
  private String eventType;
  private ContractpartnerTransport contractpartnerTransport;

  public String getEventType() {
    return this.eventType;
  }

  public void setEventType(final String eventType) {
    this.eventType = eventType;
  }

  public ContractpartnerTransport getContractpartnerTransport() {
    return this.contractpartnerTransport;
  }

  public void setContractpartnerTransport(final ContractpartnerTransport contractpartnerTransport) {
    this.contractpartnerTransport = contractpartnerTransport;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.contractpartnerTransport, this.eventType);
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
    final ContractpartnerChangedEventTransport other = (ContractpartnerChangedEventTransport) obj;
    return Objects.equals(this.contractpartnerTransport, other.contractpartnerTransport)
        && Objects.equals(this.eventType, other.eventType);
  }

  @Override
  public String toString() {
    return "ContractpartnerChangedEventTransport [eventType=" + this.eventType
        + ", contractpartnerTransport=" + this.contractpartnerTransport + "]";
  }
}
