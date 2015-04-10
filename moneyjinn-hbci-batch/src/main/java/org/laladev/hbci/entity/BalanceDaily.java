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
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "findDailyBalance", query = "FROM BalanceDaily b WHERE b.myIban = :myIban AND b.myBic = :myBic AND b.myAccountnumber = :myAccountnumber"
				+ " AND  b.myBankcode = :myBankcode AND b.balanceDate = :balanceDate") })

@Entity
@Table(name = "balance_daily")
public class BalanceDaily extends AbstractAccountEntitiy implements Serializable {

	private static final long serialVersionUID = 1L;
	private Date balanceDate;
	private Timestamp lastTransactionDate;
	private BigDecimal balanceAvailableValue;
	private BigDecimal lineOfCreditValue;
	private String balanceCurrency;
	private Timestamp lastBalanceUpdate;

	@Column
	public Date getBalanceDate() {
		return balanceDate;
	}

	public void setBalanceDate(final Date balanceDate) {
		this.balanceDate = balanceDate;
	}

	@Column
	public Timestamp getLastTransactionDate() {
		return lastTransactionDate;
	}

	public void setLastTransactionDate(final Timestamp lastTransactionDate) {
		this.lastTransactionDate = lastTransactionDate;
	}

	@Column
	public BigDecimal getBalanceAvailableValue() {
		return balanceAvailableValue;
	}

	public void setBalanceAvailableValue(final BigDecimal balanceAvailableValue) {
		this.balanceAvailableValue = balanceAvailableValue;
	}

	@Column
	public BigDecimal getLineOfCreditValue() {
		return lineOfCreditValue;
	}

	public void setLineOfCreditValue(final BigDecimal lineOfCreditValue) {
		this.lineOfCreditValue = lineOfCreditValue;
	}

	@Column
	public String getBalanceCurrency() {
		return balanceCurrency;
	}

	public void setBalanceCurrency(final String balanceCurrency) {
		this.balanceCurrency = balanceCurrency;
	}

	@Column
	public Timestamp getLastBalanceUpdate() {
		return lastBalanceUpdate;
	}

	public void setLastBalanceUpdate(final Timestamp lastBalanceUpdate) {
		this.lastBalanceUpdate = lastBalanceUpdate;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof BalanceDaily)) {
			return false;
		}

		final BalanceDaily that = (BalanceDaily) o;

		return super.equals(that);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + balanceDate.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "BalanceDaily{" + "id=" + super.getId() + ", myIban='" + super.getMyIban() + '\'' + ", myBic='"
				+ super.getMyBic() + '\'' + ", myAccountnumber=" + super.getMyAccountnumber() + ", myBankcode="
				+ super.getMyBankcode() + ", balanceDate=" + balanceDate + ", lastTransactionDate="
				+ lastTransactionDate + ", balanceAvailableValue=" + balanceAvailableValue + ", lineOfCreditValue="
				+ lineOfCreditValue + ", balanceCurrency='" + balanceCurrency + '\'' + ", lastBalanceUpdate="
				+ lastBalanceUpdate + '}';
	}
}
