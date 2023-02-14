
package org.laladev.moneyjinn.server.builder;

import java.math.BigDecimal;
import org.laladev.moneyjinn.server.model.TrendsCalculatedTransport;

public class TrendsCalculatedTransportBuilder extends TrendsCalculatedTransport {
  public TrendsCalculatedTransportBuilder withYear(final Integer year) {
    super.setYear(year);
    return this;
  }

  public TrendsCalculatedTransportBuilder withMonth(final Integer month) {
    super.setMonth(month);
    return this;
  }

  public TrendsCalculatedTransportBuilder withAmount(final String amount) {
    super.setAmount(new BigDecimal(amount));
    return this;
  }

  public TrendsCalculatedTransport build() {
    final TrendsCalculatedTransport transport = new TrendsCalculatedTransport();
    transport.setYear(super.getYear());
    transport.setMonth(super.getMonth());
    transport.setAmount(super.getAmount());
    return transport;
  }
}
