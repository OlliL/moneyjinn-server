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
package org.laladev.hbci.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "balance_monthly")
public class BalanceMonthly implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String myIban;
	private String myBic;
	private Long myAccountnumber;
	private Integer myBankcode;
	private Integer balanceYear;
	private Integer balanceMonth;
	private BigDecimal balanceValue;
	private String balanceCurrency;

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
	public final String getMyIban() {
		return myIban;
	}

	public final void setMyIban(final String myIban) {
		this.myIban = myIban;
	}

	@Column
	public final String getMyBic() {
		return myBic;
	}

	public final void setMyBic(final String myBic) {
		this.myBic = myBic;
	}

	@Column
	public final Long getMyAccountnumber() {
		return myAccountnumber;
	}

	public final void setMyAccountnumber(final Long myAccountnumber) {
		this.myAccountnumber = myAccountnumber;
	}

	@Column
	public final Integer getMyBankcode() {
		return myBankcode;
	}

	public final void setMyBankcode(final Integer myBankcode) {
		this.myBankcode = myBankcode;
	}

	@Column
	public final Integer getBalanceYear() {
		return balanceYear;
	}

	public final void setBalanceYear(final Integer balanceYear) {
		this.balanceYear = balanceYear;
	}

	@Column
	public final Integer getBalanceMonth() {
		return balanceMonth;
	}

	public final void setBalanceMonth(final Integer balanceMonth) {
		this.balanceMonth = balanceMonth;
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
		builder.append("BalanceMonthly [id=").append(id).append(", myIban=").append(myIban).append(", myBic=")
				.append(myBic).append(", myAccountnumber=").append(myAccountnumber).append(", myBankcode=")
				.append(myBankcode).append(", balanceYear=").append(balanceYear).append(", balanceMonth=")
				.append(balanceMonth).append(", balanceValue=").append(balanceValue).append(", balanceCurrency=")
				.append(balanceCurrency).append("]");
		return builder.toString();
	}

}
