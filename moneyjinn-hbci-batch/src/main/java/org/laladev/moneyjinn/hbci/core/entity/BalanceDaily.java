//
// Copyright (c) 2015-2025 Oliver Lehmann <lehmann@ans-netz.de>
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

public class BalanceDaily extends AbstractAccountEntitiy implements Serializable {

	private static final long serialVersionUID = 1L;
	private LocalDate balanceDate;
	private LocalDateTime lastTransactionDate;
	private BigDecimal balanceAvailableValue;
	private BigDecimal lineOfCreditValue;
	private String balanceCurrency;
	private LocalDateTime lastBalanceUpdate;

	public LocalDate getBalanceDate() {
		return this.balanceDate;
	}

	public void setBalanceDate(final LocalDate balanceDate) {
		this.balanceDate = balanceDate;
	}

	public LocalDateTime getLastTransactionDate() {
		return this.lastTransactionDate;
	}

	public void setLastTransactionDate(final LocalDateTime lastTransactionDate) {
		this.lastTransactionDate = lastTransactionDate;
	}

	public BigDecimal getBalanceAvailableValue() {
		return this.balanceAvailableValue;
	}

	public void setBalanceAvailableValue(final BigDecimal balanceAvailableValue) {
		this.balanceAvailableValue = balanceAvailableValue;
	}

	public BigDecimal getLineOfCreditValue() {
		return this.lineOfCreditValue;
	}

	public void setLineOfCreditValue(final BigDecimal lineOfCreditValue) {
		this.lineOfCreditValue = lineOfCreditValue;
	}

	public String getBalanceCurrency() {
		return this.balanceCurrency;
	}

	public void setBalanceCurrency(final String balanceCurrency) {
		this.balanceCurrency = balanceCurrency;
	}

	public LocalDateTime getLastBalanceUpdate() {
		return this.lastBalanceUpdate;
	}

	public void setLastBalanceUpdate(final LocalDateTime lastBalanceUpdate) {
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
		result = 31 * result + this.balanceDate.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "BalanceDaily{" + "id=" + super.getId() + ", myIban='" + super.getMyIban() + '\'' + ", myBic='"
				+ super.getMyBic() + '\'' + ", myAccountnumber=" + super.getMyAccountnumber() + ", myBankcode="
				+ super.getMyBankcode() + ", balanceDate=" + this.balanceDate + ", lastTransactionDate="
				+ this.lastTransactionDate + ", balanceAvailableValue=" + this.balanceAvailableValue
				+ ", lineOfCreditValue=" + this.lineOfCreditValue + ", balanceCurrency='" + this.balanceCurrency + '\''
				+ ", lastBalanceUpdate=" + this.lastBalanceUpdate + '}';
	}
}
