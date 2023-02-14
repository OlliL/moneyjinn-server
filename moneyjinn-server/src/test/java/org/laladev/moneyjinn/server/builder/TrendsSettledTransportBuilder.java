
package org.laladev.moneyjinn.server.builder;

import java.math.BigDecimal;
import org.laladev.moneyjinn.server.model.TrendsSettledTransport;

public class TrendsSettledTransportBuilder extends TrendsSettledTransport {
  public TrendsSettledTransportBuilder withYear(final Integer year) {
    super.setYear(year);
    return this;
  }

  public TrendsSettledTransportBuilder withMonth(final Integer month) {
    super.setMonth(month);
    return this;
  }

  public TrendsSettledTransportBuilder withAmount(final String amount) {
    super.setAmount(new BigDecimal(amount));
    return this;
  }

  public TrendsSettledTransport build() {
    final TrendsSettledTransport transport = new TrendsSettledTransport();
    transport.setYear(super.getYear());
    transport.setMonth(super.getMonth());
    transport.setAmount(super.getAmount());
    return transport;
  }
}
