
package org.laladev.moneyjinn.server.builder;

import org.laladev.moneyjinn.server.model.EtfTransport;

public class EtfTransportBuilder extends EtfTransport {
	public static final String ISIN = "ISIN123";
	public static final Long ETF_ID_1 = 1L;
	public static final Long NON_EXISTING_ETF_ID = 0L;

	public EtfTransportBuilder forEtf1() {
		super.setEtfId(ETF_ID_1);
		super.setUserid(UserTransportBuilder.USER1_ID);
		super.setIsin(ISIN);
		super.setName("name456");
		super.setWkn("WKN789");
		super.setTicker("TKR0");
		super.setChartUrl("https://www.lipsum.com/");
		return this;
	}

	public EtfTransport build() {
		final EtfTransport transport = new EtfTransport();
		transport.setEtfId(super.getEtfId());
		transport.setUserid(super.getUserid());
		transport.setIsin(super.getIsin());
		transport.setName(super.getName());
		transport.setWkn(super.getWkn());
		transport.setTicker(super.getTicker());
		transport.setChartUrl(super.getChartUrl());
		return transport;
	}
}
