package org.laladev.moneyjinn.core.rest.model.etf;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.laladev.moneyjinn.core.rest.model.etf.transport.EtfTransport;

@XmlRootElement(name = "listEtfOverviewResponse")
public class ListEtfOverviewResponse {
	@XmlElement(name = "etfTransport")
	private List<EtfTransport> etfTransports;

	public final List<EtfTransport> getEtfTransports() {
		return this.etfTransports;
	}

	public final void setEtfTransports(final List<EtfTransport> etfTransports) {
		this.etfTransports = etfTransports;
	}
}
