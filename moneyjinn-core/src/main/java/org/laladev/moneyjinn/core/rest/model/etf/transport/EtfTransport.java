package org.laladev.moneyjinn.core.rest.model.etf.transport;

import java.math.BigDecimal;

public class EtfTransport {
	private String isin;
	private BigDecimal amount;
	private BigDecimal spentValue;
	private BigDecimal buyPrice;
	private BigDecimal sellPrice;

	public final String getIsin() {
		return this.isin;
	}

	public final void setIsin(final String isin) {
		this.isin = isin;
	}

	public final BigDecimal getAmount() {
		return this.amount;
	}

	public final void setAmount(final BigDecimal amount) {
		this.amount = amount;
	}

	public final BigDecimal getSpentValue() {
		return this.spentValue;
	}

	public final void setSpentValue(final BigDecimal spentValue) {
		this.spentValue = spentValue;
	}

	public final BigDecimal getBuyPrice() {
		return this.buyPrice;
	}

	public final void setBuyPrice(final BigDecimal buyPrice) {
		this.buyPrice = buyPrice;
	}

	public final BigDecimal getSellPrice() {
		return this.sellPrice;
	}

	public final void setSellPrice(final BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
	}

}
