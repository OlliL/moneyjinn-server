package org.laladev.moneyjinn.core.rest.model.monthlysettlement;

import java.util.List;

import org.laladev.moneyjinn.core.rest.model.AbstractResponse;
import org.laladev.moneyjinn.core.rest.model.transport.ImportedMonthlySettlementTransport;
import org.laladev.moneyjinn.core.rest.model.transport.MonthlySettlementTransport;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("showMonthlySettlementCreateResponse")
public class ShowMonthlySettlementCreateResponse extends AbstractResponse {
	private Short year;
	private Short month;
	private Short editMode;
	@JsonProperty("monthlySettlementTransport")
	private List<MonthlySettlementTransport> monthlySettlementTransports;
	@JsonProperty("importedMonthlySettlementTransport")
	private List<ImportedMonthlySettlementTransport> importedMonthlySettlementTransports;

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

	public final Short getEditMode() {
		return this.editMode;
	}

	public final void setEditMode(final Short editMode) {
		this.editMode = editMode;
	}

	public final List<MonthlySettlementTransport> getMonthlySettlementTransports() {
		return this.monthlySettlementTransports;
	}

	public final void setMonthlySettlementTransports(
			final List<MonthlySettlementTransport> monthlySettlementTransports) {
		this.monthlySettlementTransports = monthlySettlementTransports;
	}

	public final List<ImportedMonthlySettlementTransport> getImportedMonthlySettlementTransports() {
		return this.importedMonthlySettlementTransports;
	}

	public final void setImportedMonthlySettlementTransports(
			final List<ImportedMonthlySettlementTransport> importedMonthlySettlementTransports) {
		this.importedMonthlySettlementTransports = importedMonthlySettlementTransports;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.editMode == null) ? 0 : this.editMode.hashCode());
		result = prime * result + ((this.importedMonthlySettlementTransports == null) ? 0
				: this.importedMonthlySettlementTransports.hashCode());
		result = prime * result + ((this.month == null) ? 0 : this.month.hashCode());
		result = prime * result
				+ ((this.monthlySettlementTransports == null) ? 0 : this.monthlySettlementTransports.hashCode());
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
		final ShowMonthlySettlementCreateResponse other = (ShowMonthlySettlementCreateResponse) obj;
		if (this.editMode == null) {
			if (other.editMode != null) {
				return false;
			}
		} else if (!this.editMode.equals(other.editMode)) {
			return false;
		}
		if (this.importedMonthlySettlementTransports == null) {
			if (other.importedMonthlySettlementTransports != null) {
				return false;
			}
		} else if (!this.importedMonthlySettlementTransports.equals(other.importedMonthlySettlementTransports)) {
			return false;
		}
		if (this.month == null) {
			if (other.month != null) {
				return false;
			}
		} else if (!this.month.equals(other.month)) {
			return false;
		}
		if (this.monthlySettlementTransports == null) {
			if (other.monthlySettlementTransports != null) {
				return false;
			}
		} else if (!this.monthlySettlementTransports.equals(other.monthlySettlementTransports)) {
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
		builder.append("ShowMonthlySettlementCreateResponse [year=");
		builder.append(this.year);
		builder.append(", month=");
		builder.append(this.month);
		builder.append(", editMode=");
		builder.append(this.editMode);
		builder.append(", monthlySettlementTransports=");
		builder.append(this.monthlySettlementTransports);
		builder.append(", importedMonthlySettlementTransports=");
		builder.append(this.importedMonthlySettlementTransports);
		builder.append("]");
		return builder.toString();
	}

}
