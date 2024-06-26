
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
	public static final Long ETF_FLOW_5ID = 5L;
	public static final Long ETF_FLOW_6ID = 6L;
	public static final Long ETF_FLOW_7ID = 7L;
	public static final Long ETF_FLOW_8ID = 8L;
	public static final Long ETF_FLOW_9ID = 9L;
	public static final Long ETF_FLOW_10ID = 10L;
	public static final Long ETF_FLOW_11ID = 11L;
	public static final Long NEXT_ID = 12L;
	private static final Long ETF_ID_1 = EtfTransportBuilder.ETF_ID_1;
	public static final Long NON_EXISTING_ID = 0L;

	public EtfFlowTransportBuilder forFlow1() {
		super.setEtfflowid(ETF_FLOW_1ID);
		super.setEtfId(ETF_ID_1);
		super.setAmount(new BigDecimal("30.00000"));
		super.setNanoseconds(320000000);
		super.setPrice(new BigDecimal("777.666"));
		super.setTimestamp(OffsetDateTime.of(2008, 12, 13, 15, 16, 20, 320000000, ZoneOffset.UTC));
		return this;
	}

	public EtfFlowTransportBuilder forFlow2() {
		super.setEtfflowid(ETF_FLOW_2ID);
		super.setEtfId(ETF_ID_1);
		super.setAmount(new BigDecimal("100.00000"));
		super.setNanoseconds(320000000);
		super.setPrice(new BigDecimal("777.666"));
		super.setTimestamp(OffsetDateTime.of(2008, 12, 14, 15, 16, 20, 320000000, ZoneOffset.UTC));
		return this;
	}

	public EtfFlowTransportBuilder forFlow3() {
		super.setEtfflowid(ETF_FLOW_3ID);
		super.setEtfId(ETF_ID_1);
		super.setAmount(new BigDecimal("-50.00000"));
		super.setNanoseconds(320000000);
		super.setPrice(new BigDecimal("877.000"));
		super.setTimestamp(OffsetDateTime.of(2008, 12, 15, 15, 16, 20, 320000000, ZoneOffset.UTC));
		return this;
	}

	public EtfFlowTransportBuilder forFlow4() {
		super.setEtfflowid(ETF_FLOW_4ID);
		super.setEtfId(ETF_ID_1);
		super.setAmount(new BigDecimal("1.23400"));
		super.setNanoseconds(320000000);
		super.setPrice(new BigDecimal("666.123"));
		super.setTimestamp(OffsetDateTime.of(2008, 12, 16, 15, 16, 20, 320000000, ZoneOffset.UTC));
		return this;
	}

	public EtfFlowTransportBuilder forFlow5() {
		super.setEtfflowid(ETF_FLOW_5ID);
		super.setEtfId(ETF_ID_1);
		super.setAmount(new BigDecimal("5.50000"));
		super.setNanoseconds(999000000);
		super.setPrice(new BigDecimal("789.123"));
		super.setTimestamp(OffsetDateTime.of(2009, 1, 31, 23, 59, 59, 999000000, ZoneOffset.UTC));
		return this;
	}

	public EtfFlowTransportBuilder forFlow6() {
		super.setEtfflowid(ETF_FLOW_6ID);
		super.setEtfId(ETF_ID_1);
		super.setAmount(new BigDecimal("6.50000"));
		super.setNanoseconds(999000000);
		super.setPrice(new BigDecimal("889.123"));
		super.setTimestamp(OffsetDateTime.of(2009, 2, 20, 23, 59, 59, 999000000, ZoneOffset.UTC));
		return this;
	}

	public EtfFlowTransportBuilder forFlow7() {
		super.setEtfflowid(ETF_FLOW_7ID);
		super.setEtfId(ETF_ID_1);
		super.setAmount(new BigDecimal("-81.00000"));
		super.setNanoseconds(320000000);
		super.setPrice(new BigDecimal("877.000"));
		super.setTimestamp(OffsetDateTime.of(2009, 12, 12, 15, 16, 20, 320000000, ZoneOffset.UTC));
		return this;
	}

	public EtfFlowTransportBuilder forFlow8() {
		super.setEtfflowid(ETF_FLOW_8ID);
		super.setEtfId(ETF_ID_1);
		super.setAmount(new BigDecimal("81.00000"));
		super.setNanoseconds(320000000);
		super.setPrice(new BigDecimal("777.000"));
		super.setTimestamp(OffsetDateTime.of(2010, 01, 01, 15, 16, 20, 320000000, ZoneOffset.UTC));
		return this;
	}

	public EtfFlowTransportBuilder forFlow9() {
		super.setEtfflowid(ETF_FLOW_9ID);
		super.setEtfId(ETF_ID_1);
		super.setAmount(new BigDecimal("80.00000"));
		super.setNanoseconds(320000000);
		super.setPrice(new BigDecimal("777.000"));
		super.setTimestamp(OffsetDateTime.of(2010, 02, 02, 15, 16, 20, 320000000, ZoneOffset.UTC));
		return this;
	}

	public EtfFlowTransportBuilder forFlow10() {
		super.setEtfflowid(ETF_FLOW_10ID);
		super.setEtfId(ETF_ID_1);
		super.setAmount(new BigDecimal("-10.00000"));
		super.setNanoseconds(320000000);
		super.setPrice(new BigDecimal("760.000"));
		super.setTimestamp(OffsetDateTime.of(2010, 02, 03, 15, 16, 20, 320000000, ZoneOffset.UTC));
		return this;
	}

	public EtfFlowTransportBuilder forFlow11() {
		super.setEtfflowid(ETF_FLOW_11ID);
		super.setEtfId(ETF_ID_1);
		super.setAmount(new BigDecimal("30.00000"));
		super.setNanoseconds(320000000);
		super.setPrice(new BigDecimal("750.000"));
		super.setTimestamp(OffsetDateTime.of(2010, 02, 04, 15, 16, 20, 320000000, ZoneOffset.UTC));
		return this;
	}

	public EtfFlowTransportBuilder forNewFlow() {
		super.setEtfflowid(NEXT_ID);
		super.setEtfId(ETF_ID_1);
		super.setAmount(new BigDecimal("100.432"));
		super.setNanoseconds(20000000);
		super.setPrice(new BigDecimal("667.456"));
		super.setTimestamp(OffsetDateTime.of(2008, 12, 17, 23, 59, 59, 20000000, ZoneOffset.UTC));
		return this;
	}

	public EtfFlowTransport build() {
		final EtfFlowTransport transport = new EtfFlowTransport();
		transport.setEtfflowid(super.getEtfflowid());
		transport.setEtfId(super.getEtfId());
		transport.setAmount(super.getAmount());
		transport.setNanoseconds(super.getNanoseconds());
		transport.setPrice(super.getPrice());
		transport.setTimestamp(super.getTimestamp());
		return transport;
	}
}
