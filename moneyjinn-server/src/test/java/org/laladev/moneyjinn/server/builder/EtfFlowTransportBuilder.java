
package org.laladev.moneyjinn.server.builder;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.laladev.moneyjinn.core.rest.model.etf.transport.EtfFlowTransport;

public class EtfFlowTransportBuilder extends EtfFlowTransport {
  public static final Long ETF_FLOW_1ID = 1L;
  public static final Long ETF_FLOW_2ID = 2L;
  public static final Long ETF_FLOW_3ID = 3L;
  public static final Long NEXT_ID = 4L;
  public static final String ISIN = "ISIN123";

  public EtfFlowTransportBuilder forFlow1() {
    super.setEtfflowid(ETF_FLOW_1ID);
    super.setAmount(BigDecimal.valueOf(100));
    super.setIsin(ISIN);
    super.setNanoseconds(320000000);
    super.setPrice(BigDecimal.valueOf(777, 666));
    super.setTimestamp(Timestamp.valueOf(LocalDateTime.of(2008, 12, 14, 15, 16, 20)));
    return this;
  }

  public EtfFlowTransportBuilder forFlow2() {
    super.setEtfflowid(ETF_FLOW_2ID);
    super.setAmount(BigDecimal.valueOf(-50));
    super.setIsin(ISIN);
    super.setNanoseconds(320000000);
    super.setPrice(BigDecimal.valueOf(877));
    super.setTimestamp(Timestamp.valueOf(LocalDateTime.of(2008, 12, 15, 15, 16, 20)));
    return this;
  }

  public EtfFlowTransportBuilder forFlow3() {
    super.setEtfflowid(ETF_FLOW_3ID);
    super.setAmount(BigDecimal.valueOf(1, 234));
    super.setIsin(ISIN);
    super.setNanoseconds(320000000);
    super.setPrice(BigDecimal.valueOf(666, 123));
    super.setTimestamp(Timestamp.valueOf(LocalDateTime.of(2008, 12, 16, 15, 16, 20)));
    return this;
  }

  public EtfFlowTransportBuilder forNewFlow() {
    super.setEtfflowid(NEXT_ID);
    super.setAmount(BigDecimal.valueOf(100, 432));
    super.setIsin(ISIN);
    super.setNanoseconds(20000000);
    super.setPrice(BigDecimal.valueOf(667, 456));
    super.setTimestamp(Timestamp.valueOf(LocalDateTime.of(2008, 12, 17, 23, 59, 59)));
    return this;
  }

  public EtfFlowTransport build() {
    final EtfFlowTransport transport = new EtfFlowTransport();
    transport.setEtfflowid(super.getEtfflowid());
    transport.setAmount(super.getAmount());
    transport.setIsin(super.getIsin());
    transport.setNanoseconds(super.getNanoseconds());
    transport.setPrice(super.getPrice());
    transport.setTimestamp(super.getTimestamp());
    return transport;
  }
}
