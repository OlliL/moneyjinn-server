
package org.laladev.moneyjinn.server.builder;

import java.math.BigDecimal;
import org.laladev.moneyjinn.server.model.TrendsTransport;

public class TrendsTransportBuilder extends TrendsTransport {
  public TrendsTransportBuilder withYear(final Integer year) {
    super.setYear(year);
    return this;
  }

  public TrendsTransportBuilder withMonth(final Integer month) {
    super.setMonth(month);
    return this;
  }

  public TrendsTransportBuilder withAmount(final String amount) {
    super.setAmount(new BigDecimal(amount));
    return this;
  }

  public TrendsTransport build() {
    final TrendsTransport transport = new TrendsTransport();
    transport.setYear(super.getYear());
    transport.setMonth(super.getMonth());
    transport.setAmount(super.getAmount());
    return transport;
  }
}
