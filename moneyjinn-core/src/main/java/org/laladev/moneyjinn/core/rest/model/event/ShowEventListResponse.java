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

package org.laladev.moneyjinn.core.rest.model.event;

import org.laladev.moneyjinn.core.rest.model.AbstractResponse;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("showEventListResponse")
public class ShowEventListResponse extends AbstractResponse {
	private boolean monthlySettlementMissing;
	private Short monthlySettlementMonth;
	private Short monthlySettlementYear;
	private Integer monthlySettlementNumberOfAddableSettlements;
	private Integer numberOfImportedMoneyflows;

	public final boolean getMonthlySettlementMissing() {
		return this.monthlySettlementMissing;
	}

	public final void setMonthlySettlementMissing(final boolean monthlySettlementMissing) {
		this.monthlySettlementMissing = monthlySettlementMissing;
	}

	public final Short getMonthlySettlementMonth() {
		return this.monthlySettlementMonth;
	}

	public final void setMonthlySettlementMonth(final Short monthlySettlementMonth) {
		this.monthlySettlementMonth = monthlySettlementMonth;
	}

	public final Short getMonthlySettlementYear() {
		return this.monthlySettlementYear;
	}

	public final void setMonthlySettlementYear(final Short monthlySettlementYear) {
		this.monthlySettlementYear = monthlySettlementYear;
	}

	public final Integer getMonthlySettlementNumberOfAddableSettlements() {
		return this.monthlySettlementNumberOfAddableSettlements;
	}

	public final void setMonthlySettlementNumberOfAddableSettlements(
			final Integer monthlySettlementNumberOfAddableSettlements) {
		this.monthlySettlementNumberOfAddableSettlements = monthlySettlementNumberOfAddableSettlements;
	}

	public final Integer getNumberOfImportedMoneyflows() {
		return this.numberOfImportedMoneyflows;
	}

	public final void setNumberOfImportedMoneyflows(final Integer numberOfImportedMoneyflows) {
		this.numberOfImportedMoneyflows = numberOfImportedMoneyflows;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (this.monthlySettlementMissing ? 1231 : 1237);
		result = prime * result + ((this.monthlySettlementMonth == null) ? 0 : this.monthlySettlementMonth.hashCode());
		result = prime * result + ((this.monthlySettlementNumberOfAddableSettlements == null) ? 0
				: this.monthlySettlementNumberOfAddableSettlements.hashCode());
		result = prime * result + ((this.monthlySettlementYear == null) ? 0 : this.monthlySettlementYear.hashCode());
		result = prime * result
				+ ((this.numberOfImportedMoneyflows == null) ? 0 : this.numberOfImportedMoneyflows.hashCode());
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
		final ShowEventListResponse other = (ShowEventListResponse) obj;
		if (this.monthlySettlementMissing != other.monthlySettlementMissing) {
			return false;
		}
		if (this.monthlySettlementMonth == null) {
			if (other.monthlySettlementMonth != null) {
				return false;
			}
		} else if (!this.monthlySettlementMonth.equals(other.monthlySettlementMonth)) {
			return false;
		}
		if (this.monthlySettlementNumberOfAddableSettlements == null) {
			if (other.monthlySettlementNumberOfAddableSettlements != null) {
				return false;
			}
		} else if (!this.monthlySettlementNumberOfAddableSettlements
				.equals(other.monthlySettlementNumberOfAddableSettlements)) {
			return false;
		}
		if (this.monthlySettlementYear == null) {
			if (other.monthlySettlementYear != null) {
				return false;
			}
		} else if (!this.monthlySettlementYear.equals(other.monthlySettlementYear)) {
			return false;
		}
		if (this.numberOfImportedMoneyflows == null) {
			if (other.numberOfImportedMoneyflows != null) {
				return false;
			}
		} else if (!this.numberOfImportedMoneyflows.equals(other.numberOfImportedMoneyflows)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ShowEventListResponse [monthlySettlementMissing=");
		builder.append(this.monthlySettlementMissing);
		builder.append(", monthlySettlementMonth=");
		builder.append(this.monthlySettlementMonth);
		builder.append(", monthlySettlementYear=");
		builder.append(this.monthlySettlementYear);
		builder.append(", monthlySettlementNumberOfAddableSettlements=");
		builder.append(this.monthlySettlementNumberOfAddableSettlements);
		builder.append(", numberOfImportedMoneyflows=");
		builder.append(this.numberOfImportedMoneyflows);
		builder.append("]");
		return builder.toString();
	}

}