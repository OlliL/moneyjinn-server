
package org.laladev.moneyjinn.server.builder;

import java.math.BigDecimal;

import org.laladev.moneyjinn.server.model.EtfPreliminaryLumpSumTransport;

public class EtfPreliminaryLumpSumTransportBuilder extends EtfPreliminaryLumpSumTransport {
	private final BigDecimal zero = new BigDecimal("0.00");
	public static final Integer YEAR_2009 = 2009;
	public static final Integer YEAR_2010 = 2010;
	public static final Integer NEW_YEAR = 2011;
	public static final Integer NON_EXISTING_YEAR = 1970;

	public EtfPreliminaryLumpSumTransportBuilder for2009() {
		super.setIsin(EtfTransportBuilder.ISIN);
		super.setYear(YEAR_2009);
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
		super.setIsin(EtfTransportBuilder.ISIN);
		super.setYear(YEAR_2010);
		super.setAmountJanuary(new BigDecimal("156.11"));
		super.setAmountFebruary(new BigDecimal("168.83"));
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

	public EtfPreliminaryLumpSumTransportBuilder forNewYear() {
		super.setIsin(EtfTransportBuilder.ISIN);
		super.setYear(NEW_YEAR);
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
		transport.setIsin(super.getIsin());
		transport.setYear(this.getYear());
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
