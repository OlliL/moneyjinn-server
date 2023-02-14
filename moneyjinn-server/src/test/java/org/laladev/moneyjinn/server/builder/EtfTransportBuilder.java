
package org.laladev.moneyjinn.server.builder;

import org.laladev.moneyjinn.server.model.EtfTransport;

public class EtfTransportBuilder extends EtfTransport {
  public static final String ISIN = "ISIN123";

  public EtfTransportBuilder forEtf1() {
    super.setIsin(ISIN);
    super.setName("name456");
    super.setWkn("WKN789");
    super.setTicker("TKR0");
    super.setChartUrl("https://www.lipsum.com/");
    return this;
  }

  public EtfTransport build() {
    final EtfTransport transport = new EtfTransport();
    transport.setIsin(super.getIsin());
    transport.setName(super.getName());
    transport.setWkn(super.getWkn());
    transport.setTicker(super.getTicker());
    transport.setChartUrl(super.getChartUrl());
    return transport;
  }
}
