package org.laladev.moneyjinn.model.etf;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.laladev.moneyjinn.model.AbstractEntity;

public class EtfFlow extends AbstractEntity<EtfFlowID> {
	private static final long serialVersionUID = 1L;
	private String isin;
	private String wkn;
	private LocalDate date;
	private BigDecimal amount;
	private BigDecimal price;

	public final String getIsin() {
		return this.isin;
	}

	public final void setIsin(final String isin) {
		this.isin = isin;
	}

	public final String getWkn() {
		return this.wkn;
	}

	public final void setWkn(final String wkn) {
		this.wkn = wkn;
	}

	public final LocalDate getDate() {
		return this.date;
	}

	public final void setDate(final LocalDate date) {
		this.date = date;
	}

	public final BigDecimal getAmount() {
		return this.amount;
	}

	public final void setAmount(final BigDecimal amount) {
		this.amount = amount;
	}

	public final BigDecimal getPrice() {
		return this.price;
	}

	public final void setPrice(final BigDecimal price) {
		this.price = price;
	}

}
