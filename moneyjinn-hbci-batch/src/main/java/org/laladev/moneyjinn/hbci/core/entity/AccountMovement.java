//
// Copyright (c) 2015-2024 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.hbci.core.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AccountMovement extends AbstractAccountEntitiy implements Serializable {
	private static final long serialVersionUID = 1L;

	private LocalDateTime creationTime;
	private LocalDate bookingDate;
	private LocalDate valueDate;
	private LocalDateTime invoiceTimestamp;
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
	private LocalDate balanceDate;
	private BigDecimal balanceValue;
	private String balanceCurrency;

	public final LocalDateTime getCreationTime() {
		return this.creationTime;
	}

	public final void setCreationTime(final LocalDateTime creationTime) {
		this.creationTime = creationTime;
	}

	public final LocalDate getBookingDate() {
		return this.bookingDate;
	}

	public final void setBookingDate(final LocalDate bookingDate) {
		this.bookingDate = bookingDate;
	}

	public final LocalDate getValueDate() {
		return this.valueDate;
	}

	public final void setValueDate(final LocalDate valueDate) {
		this.valueDate = valueDate;
	}

	public final LocalDateTime getInvoiceTimestamp() {
		return this.invoiceTimestamp;
	}

	public final void setInvoiceTimestamp(final LocalDateTime invoiceTimestamp) {
		this.invoiceTimestamp = invoiceTimestamp;
	}

	public final String getOtherIban() {
		return this.otherIban;
	}

	public final void setOtherIban(final String otherIban) {
		this.otherIban = otherIban;
	}

	public final String getOtherBic() {
		return this.otherBic;
	}

	public final void setOtherBic(final String otherBic) {
		this.otherBic = otherBic;
	}

	public final Long getOtherAccountnumber() {
		return this.otherAccountnumber;
	}

	public final void setOtherAccountnumber(final Long otherAccountnumber) {
		this.otherAccountnumber = otherAccountnumber;
	}

	public final Integer getOtherBankcode() {
		return this.otherBankcode;
	}

	public final void setOtherBankcode(final Integer otherBankcode) {
		this.otherBankcode = otherBankcode;
	}

	public final String getOtherName() {
		return this.otherName;
	}

	public final void setOtherName(final String otherName) {
		this.otherName = otherName;
	}

	public final BigDecimal getChargeValue() {
		return this.chargeValue;
	}

	public final void setChargeValue(final BigDecimal chargeValue) {
		this.chargeValue = chargeValue;
	}

	public final String getChargeCurrency() {
		return this.chargeCurrency;
	}

	public final void setChargeCurrency(final String chargeCurrency) {
		this.chargeCurrency = chargeCurrency;
	}

	public final BigDecimal getOriginalValue() {
		return this.originalValue;
	}

	public final void setOriginalValue(final BigDecimal originalValue) {
		this.originalValue = originalValue;
	}

	public final String getOriginalCurrency() {
		return this.originalCurrency;
	}

	public final void setOriginalCurrency(final String originalCurrency) {
		this.originalCurrency = originalCurrency;
	}

	public final BigDecimal getMovementValue() {
		return this.movementValue;
	}

	public final void setMovementValue(final BigDecimal movementValue) {
		this.movementValue = movementValue;
	}

	public final String getMovementCurrency() {
		return this.movementCurrency;
	}

	public final void setMovementCurrency(final String movementCurrency) {
		this.movementCurrency = movementCurrency;
	}

	public final String getMovementReason() {
		return this.movementReason;
	}

	public final void setMovementReason(final String movementReason) {
		this.movementReason = movementReason;
	}

	public final Short getMovementTypeCode() {
		return this.movementTypeCode;
	}

	public final void setMovementTypeCode(final Short movementTypeCode) {
		this.movementTypeCode = movementTypeCode;
	}

	public final String getMovementTypeText() {
		return this.movementTypeText;
	}

	public final void setMovementTypeText(final String movementTypeText) {
		this.movementTypeText = movementTypeText;
	}

	public final String getCustomerReference() {
		return this.customerReference;
	}

	public final void setCustomerReference(final String customerReference) {
		this.customerReference = customerReference;
	}

	public final String getBankReference() {
		return this.bankReference;
	}

	public final void setBankReference(final String bankReference) {
		this.bankReference = bankReference;
	}

	public final Boolean getCancellation() {
		return this.cancellation;
	}

	public final void setCancellation(final Boolean cancellation) {
		this.cancellation = cancellation;
	}

	public final String getAdditionalInformation() {
		return this.additionalInformation;
	}

	public final void setAdditionalInformation(final String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}

	public final Short getAdditionalKey() {
		return this.additionalKey;
	}

	public final void setAdditionalKey(final Short additionalKey) {
		this.additionalKey = additionalKey;
	}

	public final String getPrimaNota() {
		return this.primaNota;
	}

	public final void setPrimaNota(final String primaNota) {
		this.primaNota = primaNota;
	}

	public final LocalDate getBalanceDate() {
		return this.balanceDate;
	}

	public final void setBalanceDate(final LocalDate blanceDate) {
		this.balanceDate = blanceDate;
	}

	public final BigDecimal getBalanceValue() {
		return this.balanceValue;
	}

	public final void setBalanceValue(final BigDecimal balanceValue) {
		this.balanceValue = balanceValue;
	}

	public final String getBalanceCurrency() {
		return this.balanceCurrency;
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
				.append(", bookingDate=").append(this.bookingDate).append(", valueDate=").append(this.valueDate)
				.append(", invoiceDate=").append(this.invoiceTimestamp).append(", otherIban=").append(this.otherIban)
				.append(", otherBic=").append(this.otherBic).append(", otherAccountnumber=")
				.append(this.otherAccountnumber).append(", otherBankcode=").append(this.otherBankcode)
				.append(", otherName=").append(this.otherName).append(", chargeValue=").append(this.chargeValue)
				.append(", chargeCurrency=").append(this.chargeCurrency).append(", originalValue=")
				.append(this.originalValue).append(", originalCurrency=").append(this.originalCurrency)
				.append(", movementValue=").append(this.movementValue).append(", movementCurrency=")
				.append(this.movementCurrency).append(", movementReason=").append(this.movementReason)
				.append(", movementTypeCode=").append(this.movementTypeCode).append(", movementTypeText=")
				.append(this.movementTypeText).append(", customerReference=").append(this.customerReference)
				.append(", bankReference=").append(this.bankReference).append(", cancellation=")
				.append(this.cancellation).append(", additionalInformation=").append(this.additionalInformation)
				.append(", additionalKey=").append(this.additionalKey).append(", primaNota=").append(this.primaNota)
				.append(", balanceDate=").append(this.balanceDate).append(", balanceValue=").append(this.balanceValue)
				.append(", balanceCurrency=").append(this.balanceCurrency).append("]");
		return builder.toString();
	}

}
