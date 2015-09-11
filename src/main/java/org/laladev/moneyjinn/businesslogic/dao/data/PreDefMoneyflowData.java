package org.laladev.moneyjinn.businesslogic.dao.data;

import java.math.BigDecimal;
import java.sql.Date;

public class PreDefMoneyflowData {
	private Long id;
	private Long macId;
	private BigDecimal amount;
	private Long mcsCapitalsourceId;
	private Long mcpContractpartnerId;
	private String comment;
	private Date createdate;
	private boolean onceAMonth;
	private Date lastUsed;
	private Long mpaPostingAccountId;

	public final Long getId() {
		return this.id;
	}

	public final void setId(final Long id) {
		this.id = id;
	}

	public final Long getMacId() {
		return this.macId;
	}

	public final void setMacId(final Long macId) {
		this.macId = macId;
	}

	public final BigDecimal getAmount() {
		return this.amount;
	}

	public final void setAmount(final BigDecimal amount) {
		this.amount = amount;
	}

	public final Long getMcsCapitalsourceId() {
		return this.mcsCapitalsourceId;
	}

	public final void setMcsCapitalsourceId(final Long mcsCapitalsourceId) {
		this.mcsCapitalsourceId = mcsCapitalsourceId;
	}

	public final Long getMcpContractpartnerId() {
		return this.mcpContractpartnerId;
	}

	public final void setMcpContractpartnerId(final Long mcpContractpartnerId) {
		this.mcpContractpartnerId = mcpContractpartnerId;
	}

	public final String getComment() {
		return this.comment;
	}

	public final void setComment(final String comment) {
		this.comment = comment;
	}

	public final Date getCreatedate() {
		return this.createdate;
	}

	public final void setCreatedate(final Date createdate) {
		this.createdate = createdate;
	}

	public final boolean isOnceAMonth() {
		return this.onceAMonth;
	}

	public final void setOnceAMonth(final boolean onceAMonth) {
		this.onceAMonth = onceAMonth;
	}

	public final Date getLastUsed() {
		return this.lastUsed;
	}

	public final void setLastUsed(final Date lastUsed) {
		this.lastUsed = lastUsed;
	}

	public final Long getMpaPostingAccountId() {
		return this.mpaPostingAccountId;
	}

	public final void setMpaPostingAccountId(final Long mpaPostingAccountId) {
		this.mpaPostingAccountId = mpaPostingAccountId;
	}

}
