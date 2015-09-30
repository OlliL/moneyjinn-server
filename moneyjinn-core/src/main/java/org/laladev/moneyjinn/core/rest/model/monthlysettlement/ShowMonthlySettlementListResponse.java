package org.laladev.moneyjinn.core.rest.model.monthlysettlement;

import java.util.List;

import org.laladev.moneyjinn.core.rest.model.AbstractResponse;
import org.laladev.moneyjinn.core.rest.model.transport.MonthlySettlementTransport;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("showMonthlySettlementListResponse")
public class ShowMonthlySettlementListResponse extends AbstractResponse {
	private Short year;
	private Short month;
	private List<Short> allYears;
	private List<Short> allMonth;
	private List<MonthlySettlementTransport> monthlySettlementTransport;
	private Short numberOfEditableSettlements;
	private Short numberOfAddableSettlements;

	public final Short getYear() {
		return this.year;
	}

	public final void setYear(final Short year) {
		this.year = year;
	}

	public final Short getMonth() {
		return this.month;
	}

	public final void setMonth(final Short month) {
		this.month = month;
	}

	public final List<Short> getAllYears() {
		return this.allYears;
	}

	public final void setAllYears(final List<Short> allYears) {
		this.allYears = allYears;
	}

	public final List<Short> getAllMonth() {
		return this.allMonth;
	}

	public final void setAllMonth(final List<Short> allMonth) {
		this.allMonth = allMonth;
	}

	public final List<MonthlySettlementTransport> getMonthlySettlementTransport() {
		return this.monthlySettlementTransport;
	}

	public final void setMonthlySettlementTransport(final List<MonthlySettlementTransport> monthlySettlementTransport) {
		this.monthlySettlementTransport = monthlySettlementTransport;
	}

	public final Short getNumberOfEditableSettlements() {
		return this.numberOfEditableSettlements;
	}

	public final void setNumberOfEditableSettlements(final Short numberOfEditableSettlements) {
		this.numberOfEditableSettlements = numberOfEditableSettlements;
	}

	public final Short getNumberOfAddableSettlements() {
		return this.numberOfAddableSettlements;
	}

	public final void setNumberOfAddableSettlements(final Short numberOfAddableSettlements) {
		this.numberOfAddableSettlements = numberOfAddableSettlements;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.allMonth == null) ? 0 : this.allMonth.hashCode());
		result = prime * result + ((this.allYears == null) ? 0 : this.allYears.hashCode());
		result = prime * result + ((this.month == null) ? 0 : this.month.hashCode());
		result = prime * result
				+ ((this.monthlySettlementTransport == null) ? 0 : this.monthlySettlementTransport.hashCode());
		result = prime * result
				+ ((this.numberOfAddableSettlements == null) ? 0 : this.numberOfAddableSettlements.hashCode());
		result = prime * result
				+ ((this.numberOfEditableSettlements == null) ? 0 : this.numberOfEditableSettlements.hashCode());
		result = prime * result + ((this.year == null) ? 0 : this.year.hashCode());
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
		final ShowMonthlySettlementListResponse other = (ShowMonthlySettlementListResponse) obj;
		if (this.allMonth == null) {
			if (other.allMonth != null) {
				return false;
			}
		} else if (!this.allMonth.equals(other.allMonth)) {
			return false;
		}
		if (this.allYears == null) {
			if (other.allYears != null) {
				return false;
			}
		} else if (!this.allYears.equals(other.allYears)) {
			return false;
		}
		if (this.month == null) {
			if (other.month != null) {
				return false;
			}
		} else if (!this.month.equals(other.month)) {
			return false;
		}
		if (this.monthlySettlementTransport == null) {
			if (other.monthlySettlementTransport != null) {
				return false;
			}
		} else if (!this.monthlySettlementTransport.equals(other.monthlySettlementTransport)) {
			return false;
		}
		if (this.numberOfAddableSettlements == null) {
			if (other.numberOfAddableSettlements != null) {
				return false;
			}
		} else if (!this.numberOfAddableSettlements.equals(other.numberOfAddableSettlements)) {
			return false;
		}
		if (this.numberOfEditableSettlements == null) {
			if (other.numberOfEditableSettlements != null) {
				return false;
			}
		} else if (!this.numberOfEditableSettlements.equals(other.numberOfEditableSettlements)) {
			return false;
		}
		if (this.year == null) {
			if (other.year != null) {
				return false;
			}
		} else if (!this.year.equals(other.year)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ShowMonthlySettlementListResponse [year=");
		builder.append(this.year);
		builder.append(", month=");
		builder.append(this.month);
		builder.append(", allYears=");
		builder.append(this.allYears);
		builder.append(", allMonth=");
		builder.append(this.allMonth);
		builder.append(", monthlySettlementTransport=");
		builder.append(this.monthlySettlementTransport);
		builder.append(", numberOfEditableSettlements=");
		builder.append(this.numberOfEditableSettlements);
		builder.append(", numberOfAddableSettlements=");
		builder.append(this.numberOfAddableSettlements);
		builder.append("]");
		return builder.toString();
	}

}
