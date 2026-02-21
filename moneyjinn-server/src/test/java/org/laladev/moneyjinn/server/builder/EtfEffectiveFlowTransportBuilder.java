package org.laladev.moneyjinn.server.builder;

import org.laladev.moneyjinn.server.model.EtfEffectiveFlowTransport;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;

public class EtfEffectiveFlowTransportBuilder extends EtfEffectiveFlowTransport {
    public static final Long ETF_FLOW_6ID = 6L;
    public static final Long ETF_FLOW_8ID = 8L;
    public static final Long ETF_FLOW_9ID = 9L;
    public static final Long ETF_FLOW_11ID = 11L;
    public static final Long ETF_ID_1 = EtfTransportBuilder.ETF_ID_1;

    public EtfEffectiveFlowTransportBuilder forFlow6() {
        super.setEtfflowid(ETF_FLOW_6ID);
        super.setEtfId(ETF_ID_1);
        super.setAmount(new BigDecimal("2.234000"));
        super.setNanoseconds(999000000);
        super.setPrice(new BigDecimal("889.123000"));
        super.setAccumulatedPreliminaryLumpSum(new BigDecimal("6.910"));
        super.setPreliminaryLumpSumPerYear(Map.of("2009", new BigDecimal("3.169"), "2010", new BigDecimal("3.741")));
        super.setTimestamp(OffsetDateTime.of(2009, 2, 20, 23, 59, 59, 999000000, ZoneOffset.UTC));
        return this;
    }

    public EtfEffectiveFlowTransportBuilder forFlow8() {
        super.setEtfflowid(ETF_FLOW_8ID);
        super.setEtfId(ETF_ID_1);
        super.setAmount(new BigDecimal("81.000000"));
        super.setNanoseconds(320000000);
        super.setPrice(new BigDecimal("777.000000"));
        super.setAccumulatedPreliminaryLumpSum(new BigDecimal("135.626"));
        super.setPreliminaryLumpSumPerYear(Map.of("2010", new BigDecimal("135.626")));
        super.setTimestamp(OffsetDateTime.of(2010, 1, 1, 15, 16, 20, 320000000, ZoneOffset.UTC));
        return this;
    }

    public EtfEffectiveFlowTransportBuilder forFlow9() {
        super.setEtfflowid(ETF_FLOW_9ID);
        super.setEtfId(ETF_ID_1);
        super.setAmount(new BigDecimal("80.000000"));
        super.setNanoseconds(320000000);
        super.setPrice(new BigDecimal("777.000000"));
        super.setAccumulatedPreliminaryLumpSum(new BigDecimal("122.789"));
        super.setPreliminaryLumpSumPerYear(Map.of("2010", new BigDecimal("122.789")));
        super.setTimestamp(OffsetDateTime.of(2010, 2, 2, 15, 16, 20, 320000000, ZoneOffset.UTC));
        return this;
    }

    public EtfEffectiveFlowTransportBuilder forFlow11() {
        super.setEtfflowid(ETF_FLOW_11ID);
        super.setEtfId(ETF_ID_1);
        super.setAmount(new BigDecimal("30.000000"));
        super.setNanoseconds(320000000);
        super.setPrice(new BigDecimal("750.000000"));
        super.setAccumulatedPreliminaryLumpSum(new BigDecimal("46.046"));
        super.setPreliminaryLumpSumPerYear(Map.of("2010", new BigDecimal("46.046")));
        super.setTimestamp(OffsetDateTime.of(2010, 2, 4, 15, 16, 20, 320000000, ZoneOffset.UTC));
        return this;
    }

    public EtfEffectiveFlowTransport build() {
        final EtfEffectiveFlowTransport transport = new EtfEffectiveFlowTransport();
        transport.setEtfflowid(super.getEtfflowid());
        transport.setEtfId(super.getEtfId());
        transport.setAmount(super.getAmount());
        transport.setNanoseconds(super.getNanoseconds());
        transport.setPrice(super.getPrice());
        transport.setAccumulatedPreliminaryLumpSum(super.getAccumulatedPreliminaryLumpSum());
        transport.setPreliminaryLumpSumPerYear(super.getPreliminaryLumpSumPerYear());
        transport.setTimestamp(super.getTimestamp());
        return transport;
    }
}
