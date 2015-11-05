//
//Copyright (c) 2015 Oliver Lehmann <oliver@laladev.org>
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without
//modification, are permitted provided that the following conditions
//are met:
//1. Redistributions of source code must retain the above copyright
//notice, this list of conditions and the following disclaimer
//2. Redistributions in binary form must reproduce the above copyright
//notice, this list of conditions and the following disclaimer in the
//documentation and/or other materials provided with the distribution.
//
//THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
//ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
//IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
//ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
//FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
//DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
//OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
//HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
//LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
//OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
//SUCH DAMAGE.
//
package org.laladev.moneyjinn.hbci.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "account_movements")
public class AccountMovement extends AbstractAccountEntitiy implements Serializable {
	private static final long serialVersionUID = 1L;

	private Timestamp creationTime;
	private Date bookingDate;
	private Date valueDate;
	private Timestamp invoiceTimestamp;
	private String otherIban;
	private String otherBic;
	private Long otherAccountnumber;
	private Integer otherBankcode;
	private String otherName;
	private BigDecimal chargeValue;
	private String chargeCurrency;
	private BigDecimal originalValue;
	private String originalCurrency;
	private BigDecimal movementValue;
	private String movementCurrency;
	private String movementReason;
	private Short movementTypeCode;
	private String movementTypeText;
	private String customerReference;
	private String bankReference;
	private Boolean cancellation;
	private String additionalInformation;
	private Short additionalKey;
	private String primaNota;
	private Date balanceDate;
	private BigDecimal balanceValue;
	private String balanceCurrency;

	@Column
	public final Timestamp getCreationTime() {
		return creationTime;
	}

	public final void setCreationTime(final Timestamp creationTime) {
		this.creationTime = creationTime;
	}

	@Column
	public final Date getBookingDate() {
		return bookingDate;
	}

	public final void setBookingDate(final Date bookingDate) {
		this.bookingDate = bookingDate;
	}

	@Column
	public final Date getValueDate() {
		return valueDate;
	}

	public final void setValueDate(final Date valueDate) {
		this.valueDate = valueDate;
	}

	@Column
	public final Timestamp getInvoiceTimestamp() {
		return invoiceTimestamp;
	}

	public final void setInvoiceTimestamp(final Timestamp invoiceTimestamp) {
		this.invoiceTimestamp = invoiceTimestamp;
	}

	@Column
	public final String getOtherIban() {
		return otherIban;
	}

	public final void setOtherIban(final String otherIban) {
		this.otherIban = otherIban;
	}

	@Column
	public final String getOtherBic() {
		return otherBic;
	}

	public final void setOtherBic(final String otherBic) {
		this.otherBic = otherBic;
	}

	@Column
	public final Long getOtherAccountnumber() {
		return otherAccountnumber;
	}

	public final void setOtherAccountnumber(final Long otherAccountnumber) {
		this.otherAccountnumber = otherAccountnumber;
	}

	@Column
	public final Integer getOtherBankcode() {
		return otherBankcode;
	}

	public final void setOtherBankcode(final Integer otherBankcode) {
		this.otherBankcode = otherBankcode;
	}

	@Column
	public final String getOtherName() {
		return otherName;
	}

	public final void setOtherName(final String otherName) {
		this.otherName = otherName;
	}

	@Column
	public final BigDecimal getChargeValue() {
		return chargeValue;
	}

	public final void setChargeValue(final BigDecimal chargeValue) {
		this.chargeValue = chargeValue;
	}

	@Column
	public final String getChargeCurrency() {
		return chargeCurrency;
	}

	public final void setChargeCurrency(final String chargeCurrency) {
		this.chargeCurrency = chargeCurrency;
	}

	@Column
	public final BigDecimal getOriginalValue() {
		return originalValue;
	}

	public final void setOriginalValue(final BigDecimal originalValue) {
		this.originalValue = originalValue;
	}

	@Column
	public final String getOriginalCurrency() {
		return originalCurrency;
	}

	public final void setOriginalCurrency(final String originalCurrency) {
		this.originalCurrency = originalCurrency;
	}

	@Column
	public final BigDecimal getMovementValue() {
		return movementValue;
	}

	public final void setMovementValue(final BigDecimal movementValue) {
		this.movementValue = movementValue;
	}

	@Column
	public final String getMovementCurrency() {
		return movementCurrency;
	}

	public final void setMovementCurrency(final String movementCurrency) {
		this.movementCurrency = movementCurrency;
	}

	@Column
	public final String getMovementReason() {
		return movementReason;
	}

	public final void setMovementReason(final String movementReason) {
		this.movementReason = movementReason;
	}

	@Column
	public final Short getMovementTypeCode() {
		return movementTypeCode;
	}

	public final void setMovementTypeCode(final Short movementTypeCode) {
		this.movementTypeCode = movementTypeCode;
	}

	@Column
	public final String getMovementTypeText() {
		return movementTypeText;
	}

	public final void setMovementTypeText(final String movementTypeText) {
		this.movementTypeText = movementTypeText;
	}

	@Column
	public final String getCustomerReference() {
		return customerReference;
	}

	public final void setCustomerReference(final String customerReference) {
		this.customerReference = customerReference;
	}

	@Column
	public final String getBankReference() {
		return bankReference;
	}

	public final void setBankReference(final String bankReference) {
		this.bankReference = bankReference;
	}

	@Column
	public final Boolean getCancellation() {
		return cancellation;
	}

	public final void setCancellation(final Boolean cancellation) {
		this.cancellation = cancellation;
	}

	@Column
	public final String getAdditionalInformation() {
		return additionalInformation;
	}

	public final void setAdditionalInformation(final String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}

	@Column
	public final Short getAdditionalKey() {
		return additionalKey;
	}

	public final void setAdditionalKey(final Short additionalKey) {
		this.additionalKey = additionalKey;
	}

	@Column
	public final String getPrimaNota() {
		return primaNota;
	}

	public final void setPrimaNota(final String primaNota) {
		this.primaNota = primaNota;
	}

	@Column
	public final Date getBalanceDate() {
		return balanceDate;
	}

	public final void setBalanceDate(final Date blanceDate) {
		this.balanceDate = blanceDate;
	}

	@Column
	public final BigDecimal getBalanceValue() {
		return balanceValue;
	}

	public final void setBalanceValue(final BigDecimal balanceValue) {
		this.balanceValue = balanceValue;
	}

	@Column
	public final String getBalanceCurrency() {
		return balanceCurrency;
	}

	public final void setBalanceCurrency(final String balanceCurrency) {
		this.balanceCurrency = balanceCurrency;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("AccountMovement [id=").append(super.getId()).append(", myIban=").append(super.getMyIban())
				.append(", myBic=").append(super.getMyBic()).append(", myAccountnumber=")
				.append(super.getMyAccountnumber()).append(", myBankcode=").append(super.getMyBankcode())
				.append(", bookingDate=").append(bookingDate).append(", valueDate=").append(valueDate)
				.append(", invoiceDate=").append(invoiceTimestamp).append(", otherIban=").append(otherIban)
				.append(", otherBic=").append(otherBic).append(", otherAccountnumber=").append(otherAccountnumber)
				.append(", otherBankcode=").append(otherBankcode).append(", otherName=").append(otherName)
				.append(", chargeValue=").append(chargeValue).append(", chargeCurrency=").append(chargeCurrency)
				.append(", originalValue=").append(originalValue).append(", originalCurrency=").append(originalCurrency)
				.append(", movementValue=").append(movementValue).append(", movementCurrency=").append(movementCurrency)
				.append(", movementReason=").append(movementReason).append(", movementTypeCode=")
				.append(movementTypeCode).append(", movementTypeText=").append(movementTypeText)
				.append(", customerReference=").append(customerReference).append(", bankReference=")
				.append(bankReference).append(", cancellation=").append(cancellation).append(", additionalInformation=")
				.append(additionalInformation).append(", additionalKey=").append(additionalKey).append(", primaNota=")
				.append(primaNota).append(", balanceDate=").append(balanceDate).append(", balanceValue=")
				.append(balanceValue).append(", balanceCurrency=").append(balanceCurrency).append("]");
		return builder.toString();
	}

}
