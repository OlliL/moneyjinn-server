package org.laladev.moneyjinn.businesslogic.dao.data;

import java.math.BigDecimal;
import java.sql.Date;

public class PostingAccountAmountData {
	private Date date;
	private BigDecimal amount;
	private Long mpaPostingAccountId;

	public final Date getDate() {
		return this.date;
	}

	public final void setDate(final Date date) {
		this.date = date;
	}

	public final BigDecimal getAmount() {
		return this.amount;
	}

	public final void setAmount(final BigDecimal amount) {
		this.amount = amount;
	}

	public final Long getMpaPostingAccountId() {
		return this.mpaPostingAccountId;
	}

	public final void setMpaPostingAccountId(final Long mpaPostingAccountId) {
		this.mpaPostingAccountId = mpaPostingAccountId;
	}

}
