
package org.laladev.moneyjinn.core.rest.model.capitalsource;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.laladev.moneyjinn.core.rest.model.AbstractRequest;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractCapitalsourceRequest extends AbstractRequest {
  private CapitalsourceTransport capitalsourceTransport;
}
