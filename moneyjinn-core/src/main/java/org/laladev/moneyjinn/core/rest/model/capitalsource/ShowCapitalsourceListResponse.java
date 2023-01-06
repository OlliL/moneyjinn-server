
package org.laladev.moneyjinn.core.rest.model.capitalsource;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.laladev.moneyjinn.core.rest.model.AbstractResponse;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@XmlRootElement(name = "showCapitalsourceListResponse")
public class ShowCapitalsourceListResponse extends AbstractResponse {
  @XmlElement(name = "capitalsourceTransport")
  private List<CapitalsourceTransport> capitalsourceTransports;
}
