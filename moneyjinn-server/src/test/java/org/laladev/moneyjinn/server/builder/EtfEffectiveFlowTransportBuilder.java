
package org.laladev.moneyjinn.server.builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.laladev.moneyjinn.server.model.EtfEffectiveFlowTransport;

public class EtfEffectiveFlowTransportBuilder extends EtfEffectiveFlowTransport {
	public static final Long ETF_FLOW_2ID = 2L;
	public static final Long ETF_FLOW_4ID = 4L;
	public static final Long ETF_FLOW_5ID = 5L;
	public static final Long ETF_FLOW_6ID = 6L;
	public static final Long NEXT_ID = 4L;
	public static final String ISIN = "ISIN123";

	public EtfEffectiveFlowTransportBuilder forFlow2() {
		super.setEtfflowid(ETF_FLOW_2ID);
		super.setAmount(new BigDecimal("80.000"));
		super.setIsin(ISIN);
		super.setNanoseconds(320000000);
		super.setPrice(new BigDecimal("777.666"));
		super.setTimestamp(OffsetDateTime.of(2008, 12, 14, 15, 16, 20, 320000000, ZoneOffset.UTC));
		return this;
	}

	public EtfEffectiveFlowTransportBuilder forFlow4() {
		super.setEtfflowid(ETF_FLOW_4ID);
		super.setAmount(new BigDecimal("1.234"));
		super.setIsin(ISIN);
		super.setNanoseconds(320000000);
		super.setPrice(new BigDecimal("666.123"));
		super.setTimestamp(OffsetDateTime.of(2008, 12, 16, 15, 16, 20, 320000000, ZoneOffset.UTC));
		return this;
	}

	public EtfEffectiveFlowTransportBuilder forFlow5() {
		super.setEtfflowid(ETF_FLOW_5ID);
		super.setAmount(new BigDecimal("5.500"));
		super.setIsin(ISIN);
		super.setNanoseconds(999000000);
		super.setPrice(new BigDecimal("789.123"));
		super.setTimestamp(OffsetDateTime.of(2009, 1, 31, 23, 59, 59, 999000000, ZoneOffset.UTC));
		return this;
	}

	public EtfEffectiveFlowTransportBuilder forFlow6() {
		super.setEtfflowid(ETF_FLOW_6ID);
		super.setAmount(new BigDecimal("6.500"));
		super.setIsin(ISIN);
		super.setNanoseconds(999000000);
		super.setPrice(new BigDecimal("889.123"));
		super.setTimestamp(OffsetDateTime.of(2009, 2, 20, 23, 59, 59, 999000000, ZoneOffset.UTC));
		return this;
	}

	public EtfEffectiveFlowTransport build() {
		final EtfEffectiveFlowTransport transport = new EtfEffectiveFlowTransport();
		transport.setEtfflowid(super.getEtfflowid());
		transport.setAmount(super.getAmount());
		transport.setIsin(super.getIsin());
		transport.setNanoseconds(super.getNanoseconds());
		transport.setPrice(super.getPrice());
		transport.setTimestamp(super.getTimestamp());
		return transport;
	}
}
