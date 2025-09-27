package org.laladev.moneyjinn.server.builder;

import org.laladev.moneyjinn.server.model.EtfPreliminaryLumpSumTransport;

import java.math.BigDecimal;

public class EtfPreliminaryLumpSumTransportBuilder extends EtfPreliminaryLumpSumTransport {
    public static final Long ID_2009 = 0L;
    public static final Long ID_2010 = 1L;
    public static final Long NEXT_ID = 2L;
    public static final Long NON_EXISTING_ID = 3L;
    private final BigDecimal zero = new BigDecimal("0.00");

    public EtfPreliminaryLumpSumTransportBuilder for2009() {
        super.setId(ID_2009);
        super.setEtfId(EtfTransportBuilder.ETF_ID_1);
        super.setYear(2009);
        super.setType(1);
        super.setAmountPerPiece(null);
        super.setAmountJanuary(new BigDecimal("134.23"));
        super.setAmountFebruary(new BigDecimal("9.22"));
        super.setAmountMarch(this.zero);
        super.setAmountApril(this.zero);
        super.setAmountMay(this.zero);
        super.setAmountJune(this.zero);
        super.setAmountJuly(this.zero);
        super.setAmountAugust(this.zero);
        super.setAmountSeptember(this.zero);
        super.setAmountOctober(this.zero);
        super.setAmountNovember(this.zero);
        super.setAmountDecember(this.zero);
        return this;
    }

    public EtfPreliminaryLumpSumTransportBuilder for2010() {
        super.setId(ID_2010);
        super.setEtfId(EtfTransportBuilder.ETF_ID_1);
        super.setYear(2010);
        super.setType(2);
        super.setAmountPerPiece(new BigDecimal("1.67438917"));
        super.setAmountJanuary(null);
        super.setAmountFebruary(null);
        super.setAmountMarch(null);
        super.setAmountApril(null);
        super.setAmountMay(null);
        super.setAmountJune(null);
        super.setAmountJuly(null);
        super.setAmountAugust(null);
        super.setAmountSeptember(null);
        super.setAmountOctober(null);
        super.setAmountNovember(null);
        super.setAmountDecember(null);
        return this;
    }

    public EtfPreliminaryLumpSumTransportBuilder forNewYear() {
        super.setId(NEXT_ID);
        super.setEtfId(EtfTransportBuilder.ETF_ID_1);
        super.setYear(2011);
        super.setType(1);
        super.setAmountPerPiece(null);
        super.setAmountJanuary(new BigDecimal("1"));
        super.setAmountFebruary(new BigDecimal("2"));
        super.setAmountMarch(this.zero);
        super.setAmountApril(this.zero);
        super.setAmountMay(this.zero);
        super.setAmountJune(this.zero);
        super.setAmountJuly(this.zero);
        super.setAmountAugust(this.zero);
        super.setAmountSeptember(this.zero);
        super.setAmountOctober(this.zero);
        super.setAmountNovember(this.zero);
        super.setAmountDecember(new BigDecimal("12"));
        return this;
    }

    public EtfPreliminaryLumpSumTransport build() {
        final EtfPreliminaryLumpSumTransport transport = new EtfPreliminaryLumpSumTransport();
        transport.setId(this.getId());
        transport.setEtfId(this.getEtfId());
        transport.setYear(this.getYear());
        transport.setType(this.getType());
        transport.setAmountPerPiece(this.getAmountPerPiece());
        transport.setAmountJanuary(this.getAmountJanuary());
        transport.setAmountFebruary(this.getAmountFebruary());
        transport.setAmountMarch(this.getAmountMarch());
        transport.setAmountApril(this.getAmountApril());
        transport.setAmountMay(this.getAmountMay());
        transport.setAmountJune(this.getAmountJune());
        transport.setAmountJuly(this.getAmountJuly());
        transport.setAmountAugust(this.getAmountAugust());
        transport.setAmountSeptember(this.getAmountSeptember());
        transport.setAmountOctober(this.getAmountOctober());
        transport.setAmountNovember(this.getAmountNovember());
        transport.setAmountDecember(this.getAmountDecember());

        return transport;
    }
}
