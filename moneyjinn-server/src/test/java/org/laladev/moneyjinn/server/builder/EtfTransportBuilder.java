package org.laladev.moneyjinn.server.builder;

import org.laladev.moneyjinn.server.model.EtfTransport;

import java.math.BigDecimal;

public class EtfTransportBuilder extends EtfTransport {
    public static final String ISIN = "ISIN123";
    public static final Long ETF_ID_1 = 1L;
    public static final Long ETF_ID_2 = 2L;
    public static final Long ETF_ID_3 = 3L;
    public static final Long ETF_ID_4 = 4L;
    public static final Long NON_EXISTING_ID = 0L;
    public static final Long NEXT_ID = 5L;
    public static final Long FAVORITE_ETF_ID = 1L;
    public static final String NEW_ETFNAME = "NEWNAME";
    private static final String ISIN_2 = "ISIN456";
    private static final String ISIN_3 = "ISIN789";
    private static final String ISIN_4 = "ISIN012";

    public EtfTransportBuilder forEtf1() {
        super.setEtfId(ETF_ID_1);
        super.setUserid(UserTransportBuilder.USER1_ID);
        super.setIsin(ISIN);
        super.setName("name456");
        super.setWkn("WKN789");
        super.setTicker("TKR0");
        super.setChartUrl("https://www.lipsum.com/");
        super.setIsFavorite(1);
        super.setTransactionCostsAbsolute(new BigDecimal("0.99"));
        super.setTransactionCostsRelative(new BigDecimal("0.25"));
        super.setTransactionCostsMaximum(new BigDecimal("69.90"));
        super.setPartialTaxExemption(new BigDecimal("30.00"));
        return this;
    }

    public EtfTransportBuilder forEtf2() {
        super.setEtfId(ETF_ID_2);
        super.setUserid(UserTransportBuilder.ADMIN_ID);
        super.setIsin(ISIN_2);
        super.setName("name456");
        super.setWkn("WKN789");
        super.setTicker("TKR0");
        super.setChartUrl("https://www.lipsum.com/");
        return this;
    }

    public EtfTransportBuilder forEtf3() {
        super.setEtfId(ETF_ID_3);
        super.setUserid(UserTransportBuilder.USER1_ID);
        super.setIsin(ISIN_3);
        super.setName("name456");
        super.setWkn("WKN789");
        super.setTicker("TKR0");
        super.setChartUrl("https://www.lipsum.com/");
        return this;
    }

    public EtfTransportBuilder forEtf4() {
        super.setEtfId(ETF_ID_4);
        super.setUserid(UserTransportBuilder.USER3_ID);
        super.setIsin(ISIN_4);
        super.setName("name456");
        super.setWkn("WKN789");
        super.setTicker("TKR0");
        super.setChartUrl("https://www.lipsum.com/");
        return this;
    }

    public EtfTransportBuilder forNewEtf() {
        super.setEtfId(null);
        super.setUserid(UserTransportBuilder.USER1_ID);
        super.setIsin("HAHA");
        super.setName(NEW_ETFNAME);
        super.setWkn("987NKW");
        super.setTicker("0RKT");
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
        transport.setTransactionCostsAbsolute(this.getTransactionCostsAbsolute());
        transport.setTransactionCostsRelative(this.getTransactionCostsRelative());
        transport.setTransactionCostsMaximum(this.getTransactionCostsMaximum());
        transport.setPartialTaxExemption(this.getPartialTaxExemption());
        transport.setIsFavorite(super.getIsFavorite());
        return transport;
    }
}
