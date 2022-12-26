
package org.laladev.moneyjinn.server.builder;

import java.util.List;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;

public class ValidationItemTransportBuilder extends ValidationItemTransport {
  public ValidationItemTransportBuilder withKey(final Object key) {
    super.setKey(key);
    return this;
  }

  public ValidationItemTransportBuilder withError(final Integer error) {
    super.setError(error);
    return this;
  }

  public ValidationItemTransportBuilder withVariableArray(final List<String> variableArray) {
    super.setVariableArray(variableArray);
    return this;
  }

  public ValidationItemTransport build() {
    final ValidationItemTransport transport = new ValidationItemTransport();
    transport.setKey(super.getKey());
    transport.setError(super.getError());
    transport.setVariableArray(super.getVariableArray());
    return transport;
  }
}
