package org.laladev.moneyjinn.businesslogic.dao.data;

import java.math.BigDecimal;
import java.sql.Date;

public class ImportedMoneyflowData {
	private Long id;
	private String externalId;
	private Long mcsCapitalsourceId;
	private Date bookingdate;
	private Date invoicedate;
	private String name;
	private String accountNumber;
	private String bankCode;
	private String comment;
	private BigDecimal amount;

	public final Long getId() {
		return this.id;
	}

	public final void setId(final Long id) {
		this.id = id;
	}

	public final String getExternalId() {
		return this.externalId;
	}

	public final void setExternalId(final String externalId) {
		this.externalId = externalId;
	}

	public final Long getMcsCapitalsourceId() {
		return this.mcsCapitalsourceId;
	}

	public final void setMcsCapitalsourceId(final Long mcsCapitalsourceId) {
		this.mcsCapitalsourceId = mcsCapitalsourceId;
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

	public final String getName() {
		return this.name;
	}

	public final void setName(final String name) {
		this.name = name;
	}

	public final String getAccountNumber() {
		return this.accountNumber;
	}

	public final void setAccountNumber(final String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public final String getBankCode() {
		return this.bankCode;
	}

	public final void setBankCode(final String bankCode) {
		this.bankCode = bankCode;
	}

	public final String getComment() {
		return this.comment;
	}

	public final void setComment(final String comment) {
		this.comment = comment;
	}

	public final BigDecimal getAmount() {
		return this.amount;
	}

	public final void setAmount(final BigDecimal amount) {
		this.amount = amount;
	}

}
