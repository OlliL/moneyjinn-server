//
// Copyright (c) 2014-2015 Oliver Lehmann <oliver@laladev.org>
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
// ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
// OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
// OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE.
//
// $Id: Transaction.java,v 1.4 2015/03/29 00:51:00 olivleh1 Exp $
//
package org.laladev.hbci.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "transactions")
public final class Transaction implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String iban;
	private String bic;
	private Date valuta;
	private Date bDate;
	private Date invoiceDate;
	private BigDecimal value;
	private Integer gvCode;
	private Integer addKey;
	private String gvText;
	private String customerRef;
	private String otherNumber;
	private String otherBlz;
	private String otherName;
	private BigDecimal saldoValue;
	private Date saldoTimestamp;
	private String usageText;

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();

		builder.append("IBAN: " + iban + "\n");
		builder.append("BIC: " + bic + "\n");
		builder.append("Datum der Wertstellung: " + valuta + "\n");
		builder.append("Datum der Buchung: " + bDate + "\n");
		builder.append("Datum der Rechnung: " + invoiceDate + "\n");
		builder.append("Betrag: " + value.doubleValue() + "\n");
		builder.append("Geschaeftsvorfall: " + gvCode + "\n");
		builder.append("Art der Buchung: " + gvText + "\n");
		builder.append("Kundenreferenz: " + customerRef + "\n");
		if (otherNumber != null) {
			builder.append("Kontonummer / IBAN: " + otherNumber + "\n");
			builder.append("Bankleitzahl / BIC: " + otherBlz + "\n");
			builder.append("Kontoinhaber: " + otherName + "\n");
		}
		builder.append("Saldo Wert: " + saldoValue.doubleValue() + "\n");
		builder.append("Saldo Datum: " + saldoTimestamp + "\n");
		builder.append("--------------------------------------" + "\n");
		builder.append("           VERWENDUNGSZWECK           " + "\n");
		builder.append("--------------------------------------" + "\n");
		builder.append(usageText + "\n");
		builder.append("--------------------------------------" + "\n");

		return builder.toString();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	public final Integer getId() {
		return id;
	}

	public final void setId(final Integer id) {
		this.id = id;
	}

	@Column
	public final String getIban() {
		return iban;
	}

	public final void setIban(final String iban) {
		this.iban = iban;
	}

	@Column
	public final String getBic() {
		return bic;
	}

	public final void setBic(final String bic) {
		this.bic = bic;
	}

	@Column
	public final Date getValuta() {
		return valuta;
	}

	public final void setValuta(final Date valuta) {
		this.valuta = valuta;
	}

	@Column(name = "bdate")
	public final Date getbDate() {
		return bDate;
	}

	public final void setbDate(final Date bDate) {
		this.bDate = bDate;
	}

	@Column(name = "invoicedate")
	public final Date getInvoiceDate() {
		return invoiceDate;
	}

	public final void setInvoiceDate(final Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	@Column
	public final BigDecimal getValue() {
		return value;
	}

	public final void setValue(final BigDecimal value) {
		this.value = value;
	}

	@Column(name = "gvcode")
	public final Integer getGvCode() {
		return gvCode;
	}

	public final void setGvCode(final Integer gvCode) {
		this.gvCode = gvCode;
	}

	@Column(name = "addkey")
	public final Integer getAddKey() {
		return addKey;
	}

	public final void setAddKey(final Integer addKey) {
		this.addKey = addKey;
	}

	@Column(name = "gvtext")
	public final String getGvText() {
		return gvText;
	}

	public final void setGvText(final String gvText) {
		this.gvText = gvText;
	}

	@Column(name = "customerref")
	public final String getCustomerRef() {
		return customerRef;
	}

	public final void setCustomerRef(final String customerRef) {
		this.customerRef = customerRef;
	}

	@Column(name = "other_number")
	public final String getOtherNumber() {
		return otherNumber;
	}

	public final void setOtherNumber(final String otherNumber) {
		this.otherNumber = otherNumber;
	}

	@Column(name = "other_blz")
	public final String getOtherBlz() {
		return otherBlz;
	}

	public final void setOtherBlz(final String otherBlz) {
		this.otherBlz = otherBlz;
	}

	@Column(name = "other_name")
	public final String getOtherName() {
		return otherName;
	}

	public final void setOtherName(final String otherName) {
		this.otherName = otherName;
	}

	@Column(name = "saldo_value")
	public final BigDecimal getSaldoValue() {
		return saldoValue;
	}

	public final void setSaldoValue(final BigDecimal saldoValue) {
		this.saldoValue = saldoValue;
	}

	@Column(name = "saldo_timestamp")
	public final Date getSaldoTimestamp() {
		return saldoTimestamp;
	}

	public final void setSaldoTimestamp(final Date saldoTimestamp) {
		this.saldoTimestamp = saldoTimestamp;
	}

	@Column(name = "usagetext")
	public final String getUsageText() {
		return usageText;
	}

	public final void setUsageText(final String usageText) {
		this.usageText = usageText;
	}

}
