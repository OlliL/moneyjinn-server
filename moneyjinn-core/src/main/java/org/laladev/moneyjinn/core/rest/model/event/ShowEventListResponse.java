package org.laladev.moneyjinn.core.rest.model.event;

import org.laladev.moneyjinn.core.rest.model.AbstractResponse;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("showEventListResponse")
public class ShowEventListResponse extends AbstractResponse {
	private Short monthlySettlementMissing;
	private Short monthlySettlementMonth;
	private Short monthlySettlementYear;
	private Integer monthlySettlementNumberOfAddableSettlements;
	private Integer numberOfImportedMoneyflows;

	public final Short getMonthlySettlementMissing() {
		return this.monthlySettlementMissing;
	}

	public final void setMonthlySettlementMissing(final Short monthlySettlementMissing) {
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
		result = prime * result
				+ ((this.monthlySettlementMissing == null) ? 0 : this.monthlySettlementMissing.hashCode());
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
		if (this.monthlySettlementMissing == null) {
			if (other.monthlySettlementMissing != null) {
				return false;
			}
		} else if (!this.monthlySettlementMissing.equals(other.monthlySettlementMissing)) {
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