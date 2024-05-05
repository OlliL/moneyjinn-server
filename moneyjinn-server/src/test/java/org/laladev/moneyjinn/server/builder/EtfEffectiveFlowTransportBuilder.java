
package org.laladev.moneyjinn.server.builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.laladev.moneyjinn.server.model.EtfEffectiveFlowTransport;

public class EtfEffectiveFlowTransportBuilder extends EtfEffectiveFlowTransport {
	public static final Long ETF_FLOW_6ID = 6L;
	public static final Long ETF_FLOW_8ID = 8L;
	public static final Long ETF_FLOW_9ID = 9L;
	public static final Long ETF_FLOW_11ID = 11L;
	public static final Long ETF_ID_1 = EtfTransportBuilder.ETF_ID_1;

	public EtfEffectiveFlowTransportBuilder forFlow6() {
		super.setEtfflowid(ETF_FLOW_6ID);
		super.setEtfId(ETF_ID_1);
		super.setAmount(new BigDecimal("2.23400"));
		super.setNanoseconds(999000000);
		super.setPrice(new BigDecimal("889.123"));
		super.setTimestamp(OffsetDateTime.of(2009, 2, 20, 23, 59, 59, 999000000, ZoneOffset.UTC));
		return this;
	}

	public EtfEffectiveFlowTransportBuilder forFlow8() {
		super.setEtfflowid(ETF_FLOW_8ID);
		super.setEtfId(ETF_ID_1);
		super.setAmount(new BigDecimal("81.00000"));
		super.setNanoseconds(320000000);
		super.setPrice(new BigDecimal("777.000"));
		super.setTimestamp(OffsetDateTime.of(2010, 01, 01, 15, 16, 20, 320000000, ZoneOffset.UTC));
		return this;
	}

	public EtfEffectiveFlowTransportBuilder forFlow9() {
		super.setEtfflowid(ETF_FLOW_9ID);
		super.setEtfId(ETF_ID_1);
		super.setAmount(new BigDecimal("80.00000"));
		super.setNanoseconds(320000000);
		super.setPrice(new BigDecimal("777.000"));
		super.setTimestamp(OffsetDateTime.of(2010, 02, 02, 15, 16, 20, 320000000, ZoneOffset.UTC));
		return this;
	}

	public EtfEffectiveFlowTransportBuilder forFlow11() {
		super.setEtfflowid(ETF_FLOW_11ID);
		super.setEtfId(ETF_ID_1);
		super.setAmount(new BigDecimal("30.00000"));
		super.setNanoseconds(320000000);
		super.setPrice(new BigDecimal("750.000"));
		super.setTimestamp(OffsetDateTime.of(2010, 02, 04, 15, 16, 20, 320000000, ZoneOffset.UTC));
		return this;
	}

	public EtfEffectiveFlowTransport build() {
		final EtfEffectiveFlowTransport transport = new EtfEffectiveFlowTransport();
		transport.setEtfflowid(super.getEtfflowid());
		transport.setEtfId(super.getEtfId());
		transport.setAmount(super.getAmount());
		transport.setNanoseconds(super.getNanoseconds());
		transport.setPrice(super.getPrice());
		transport.setTimestamp(super.getTimestamp());
		return transport;
	}
}
