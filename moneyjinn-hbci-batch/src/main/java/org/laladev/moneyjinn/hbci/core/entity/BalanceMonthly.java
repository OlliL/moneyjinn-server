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

public class BalanceMonthly extends AbstractAccountEntitiy implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer balanceYear;
	private Integer balanceMonth;
	private BigDecimal balanceValue;
	private String balanceCurrency;

	public final Integer getBalanceYear() {
		return this.balanceYear;
	}

	public final void setBalanceYear(final Integer balanceYear) {
		this.balanceYear = balanceYear;
	}

	public final Integer getBalanceMonth() {
		return this.balanceMonth;
	}

	public final void setBalanceMonth(final Integer balanceMonth) {
		this.balanceMonth = balanceMonth;
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
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.balanceCurrency == null) ? 0 : this.balanceCurrency.hashCode());
		result = prime * result + ((this.balanceMonth == null) ? 0 : this.balanceMonth.hashCode());
		result = prime * result + ((this.balanceValue == null) ? 0 : this.balanceValue.hashCode());
		result = prime * result + ((this.balanceYear == null) ? 0 : this.balanceYear.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final BalanceMonthly other = (BalanceMonthly) obj;
		if (this.balanceCurrency == null) {
			if (other.balanceCurrency != null) {
				return false;
			}
		} else if (!this.balanceCurrency.equals(other.balanceCurrency)) {
			return false;
		}
		if (this.balanceMonth == null) {
			if (other.balanceMonth != null) {
				return false;
			}
		} else if (!this.balanceMonth.equals(other.balanceMonth)) {
			return false;
		}
		if (this.balanceValue == null) {
			if (other.balanceValue != null) {
				return false;
			}
		} else if (!this.balanceValue.equals(other.balanceValue)) {
			return false;
		}
		if (this.balanceYear == null) {
			if (other.balanceYear != null) {
				return false;
			}
		} else if (!this.balanceYear.equals(other.balanceYear)) {
			return false;
		}
		return true;
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
