
package org.laladev.moneyjinn.server.builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.laladev.moneyjinn.server.model.EtfFlowTransport;

public class EtfFlowTransportBuilder extends EtfFlowTransport {
	public static final Long ETF_FLOW_1ID = 1L;
	public static final Long ETF_FLOW_2ID = 2L;
	public static final Long ETF_FLOW_3ID = 3L;
	public static final Long ETF_FLOW_4ID = 4L;
	public static final Long NEXT_ID = 5L;
	public static final String ISIN = "ISIN123";

	public EtfFlowTransportBuilder forFlow1() {
		super.setEtfflowid(ETF_FLOW_1ID);
		super.setAmount(new BigDecimal("30.000"));
		super.setIsin(ISIN);
		super.setNanoseconds(320000000);
		super.setPrice(new BigDecimal("777.666"));
		super.setTimestamp(OffsetDateTime.of(2008, 12, 13, 15, 16, 20, 320000000, ZoneOffset.UTC));
		return this;
	}

	public EtfFlowTransportBuilder forFlow2() {
		super.setEtfflowid(ETF_FLOW_2ID);
		super.setAmount(new BigDecimal("100.000"));
		super.setIsin(ISIN);
		super.setNanoseconds(320000000);
		super.setPrice(new BigDecimal("777.666"));
		super.setTimestamp(OffsetDateTime.of(2008, 12, 14, 15, 16, 20, 320000000, ZoneOffset.UTC));
		return this;
	}

	public EtfFlowTransportBuilder forFlow3() {
		super.setEtfflowid(ETF_FLOW_3ID);
		super.setAmount(new BigDecimal("-50.000"));
		super.setIsin(ISIN);
		super.setNanoseconds(320000000);
		super.setPrice(new BigDecimal("877.000"));
		super.setTimestamp(OffsetDateTime.of(2008, 12, 15, 15, 16, 20, 320000000, ZoneOffset.UTC));
		return this;
	}

	public EtfFlowTransportBuilder forFlow4() {
		super.setEtfflowid(ETF_FLOW_4ID);
		super.setAmount(new BigDecimal("1.234"));
		super.setIsin(ISIN);
		super.setNanoseconds(320000000);
		super.setPrice(new BigDecimal("666.123"));
		super.setTimestamp(OffsetDateTime.of(2008, 12, 16, 15, 16, 20, 320000000, ZoneOffset.UTC));
		return this;
	}

	public EtfFlowTransportBuilder forNewFlow() {
		super.setEtfflowid(NEXT_ID);
		super.setAmount(new BigDecimal("100.432"));
		super.setIsin(ISIN);
		super.setNanoseconds(20000000);
		super.setPrice(new BigDecimal("667.456"));
		super.setTimestamp(OffsetDateTime.of(2008, 12, 17, 23, 59, 59, 200000000, ZoneOffset.UTC));
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
