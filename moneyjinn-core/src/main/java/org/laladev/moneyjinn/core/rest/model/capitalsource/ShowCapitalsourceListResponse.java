
package org.laladev.moneyjinn.core.rest.model.capitalsource;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Objects;
import org.laladev.moneyjinn.core.rest.model.AbstractResponse;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;

@XmlRootElement(name = "showCapitalsourceListResponse")
public class ShowCapitalsourceListResponse extends AbstractResponse {
  @XmlElement(name = "capitalsourceTransport")
  private List<CapitalsourceTransport> capitalsourceTransports;

  public List<CapitalsourceTransport> getCapitalsourceTransports() {
    return this.capitalsourceTransports;
  }

  public void setCapitalsourceTransports(
      final List<CapitalsourceTransport> capitalsourceTransports) {
    this.capitalsourceTransports = capitalsourceTransports;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + Objects.hash(this.capitalsourceTransports);
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
    final ShowCapitalsourceListResponse other = (ShowCapitalsourceListResponse) obj;
    return Objects.equals(this.capitalsourceTransports, other.capitalsourceTransports);
  }

  @Override
  public String toString() {
    return "ShowCapitalsourceListResponse [capitalsourceTransports=" + this.capitalsourceTransports
        + "]";
  }
}
