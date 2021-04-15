package org.laladev.moneyjinn.service.dao.data;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EtfFlowData {
	private Long etfflowid;
	private LocalDate flowdate;
	private String isin;
	private BigDecimal amount;
	private BigDecimal price;

	public final Long getEtfflowid() {
		return this.etfflowid;
	}

	public final void setEtfflowid(final Long etfflowid) {
		this.etfflowid = etfflowid;
	}

	public final LocalDate getFlowdate() {
		return this.flowdate;
	}

	public final void setFlowdate(final LocalDate flowdate) {
		this.flowdate = flowdate;
	}

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

	public final BigDecimal getPrice() {
		return this.price;
	}

	public final void setPrice(final BigDecimal price) {
		this.price = price;
	}

}
