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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "balance_monthly")
public class BalanceMonthly extends AbstractAccountEntitiy implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer balanceYear;
	private Integer balanceMonth;
	private BigDecimal balanceValue;
	private String balanceCurrency;

	@Column(name = "balance_year")
	public final Integer getBalanceYear() {
		return this.balanceYear;
	}

	public final void setBalanceYear(final Integer balanceYear) {
		this.balanceYear = balanceYear;
	}

	@Column(name = "balance_month")
	public final Integer getBalanceMonth() {
		return this.balanceMonth;
	}

	public final void setBalanceMonth(final Integer balanceMonth) {
		this.balanceMonth = balanceMonth;
	}

	@Column(name = "balance_value")
	public final BigDecimal getBalanceValue() {
		return this.balanceValue;
	}

	public final void setBalanceValue(final BigDecimal balanceValue) {
		this.balanceValue = balanceValue;
	}

	@Column(name = "balance_currency")
	public final String getBalanceCurrency() {
		return this.balanceCurrency;
	}

	public final void setBalanceCurrency(final String balanceCurrency) {
		this.balanceCurrency = balanceCurrency;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("BalanceMonthly [id=").append(super.getId()).append(", myIban=").append(super.getMyIban())
				.append(", myBic=").append(super.getMyBic()).append(", myAccountnumber=")
				.append(super.getMyAccountnumber()).append(", myBankcode=").append(super.getMyBankcode())
				.append(", balanceYear=").append(this.balanceYear).append(", balanceMonth=").append(this.balanceMonth)
				.append(", balanceValue=").append(this.balanceValue).append(", balanceCurrency=")
				.append(this.balanceCurrency).append("]");
		return builder.toString();
	}

}
