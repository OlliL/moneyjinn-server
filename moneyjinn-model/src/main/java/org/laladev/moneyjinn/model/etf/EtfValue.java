package org.laladev.moneyjinn.model.etf;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class EtfValue {
	private String isin;
	private LocalDate date;
	private BigDecimal buyPrice;
	private BigDecimal sellPrice;
	private LocalDateTime changeDate;

	public final String getIsin() {
		return this.isin;
	}

	public final void setIsin(final String isin) {
		this.isin = isin;
	}

	public final LocalDate getDate() {
		return this.date;
	}

	public final void setDate(final LocalDate date) {
		this.date = date;
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

	public final LocalDateTime getChangeDate() {
		return this.changeDate;
	}

	public final void setChangeDate(final LocalDateTime changeDate) {
		this.changeDate = changeDate;
	}

}
