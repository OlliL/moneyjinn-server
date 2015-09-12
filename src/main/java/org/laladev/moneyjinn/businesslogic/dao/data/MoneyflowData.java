package org.laladev.moneyjinn.businesslogic.dao.data;

import java.math.BigDecimal;
import java.sql.Date;

public class MoneyflowData {
	private Long id;
	private Long macIdCreator;
	private Long macIdAccessor;
	private Date bookingdate;
	private Date invoicedate;
	private BigDecimal amount;
	private Long mcsCapitalsourceId;
	private Long mcpContractpartnerId;
	private String comment;
	private Long mpaPostingAccountId;
	private boolean privat;

	public final Long getId() {
		return this.id;
	}

	public final void setId(final Long id) {
		this.id = id;
	}

	public final Long getMacIdCreator() {
		return this.macIdCreator;
	}

	public final void setMacIdCreator(final Long macIdCreator) {
		this.macIdCreator = macIdCreator;
	}

	public final Long getMacIdAccessor() {
		return this.macIdAccessor;
	}

	public final void setMacIdAccessor(final Long macIdAccessor) {
		this.macIdAccessor = macIdAccessor;
	}

	public final Date getBookingdate() {
		return this.bookingdate;
	}

	public final void setBookingdate(final Date bookingdate) {
		this.bookingdate = bookingdate;
	}

	public final Date getInvoicedate() {
		return this.invoicedate;
	}

	public final void setInvoicedate(final Date invoicedate) {
		this.invoicedate = invoicedate;
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

	public final Long getMpaPostingAccountId() {
		return this.mpaPostingAccountId;
	}

	public final void setMpaPostingAccountId(final Long mpaPostingAccountId) {
		this.mpaPostingAccountId = mpaPostingAccountId;
	}

	public final boolean isPrivat() {
		return this.privat;
	}

	public final void setPrivat(final boolean privat) {
		this.privat = privat;
	}

}
