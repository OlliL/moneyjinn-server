//
//Copyright (c) 2015-2017 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.core.rest.model.report;

import java.math.BigDecimal;
import java.util.List;

import org.laladev.moneyjinn.core.rest.model.report.transport.ReportTurnoverCapitalsourceTransport;
import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowSplitEntryTransport;
import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowTransport;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "listReportsResponse")
public class ListReportsResponse {
	@XmlElement(name = "moneyflowTransport")
	private List<MoneyflowTransport> moneyflowTransports;
	@XmlElement(name = "moneyflowSplitEntryTransport")
	private List<MoneyflowSplitEntryTransport> moneyflowSplitEntryTransports;
	private Short year;
	private Short month;
	private List<Short> allYears;
	private List<Short> allMonth;
	@XmlElement(name = "reportTurnoverCapitalsourceTransport")
	private List<ReportTurnoverCapitalsourceTransport> reportTurnoverCapitalsourceTransports;
	private BigDecimal turnoverEndOfYearCalculated;
	private BigDecimal amountBeginOfYear;
	private Short nextMonthHasMoneyflows;
	private Short previousMonthHasMoneyflows;
	private Short previousMonth;
	private Short previousYear;
	private Short nextMonth;
	private Short nextYear;
	private List<Long> moneyflowsWithReceipt;

	public final List<MoneyflowTransport> getMoneyflowTransports() {
		return this.moneyflowTransports;
	}

	public final void setMoneyflowTransports(final List<MoneyflowTransport> moneyflowTransports) {
		this.moneyflowTransports = moneyflowTransports;
	}

	public final List<MoneyflowSplitEntryTransport> getMoneyflowSplitEntryTransports() {
		return this.moneyflowSplitEntryTransports;
	}

	public final void setMoneyflowSplitEntryTransports(
			final List<MoneyflowSplitEntryTransport> moneyflowSplitEntryTransports) {
		this.moneyflowSplitEntryTransports = moneyflowSplitEntryTransports;
	}

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

	public final List<ReportTurnoverCapitalsourceTransport> getReportTurnoverCapitalsourceTransports() {
		return this.reportTurnoverCapitalsourceTransports;
	}

	public final void setReportTurnoverCapitalsourceTransports(
			final List<ReportTurnoverCapitalsourceTransport> reportTurnoverCapitalsourceTransports) {
		this.reportTurnoverCapitalsourceTransports = reportTurnoverCapitalsourceTransports;
	}

	public final BigDecimal getTurnoverEndOfYearCalculated() {
		return this.turnoverEndOfYearCalculated;
	}

	public final void setTurnoverEndOfYearCalculated(final BigDecimal turnoverEndOfYearCalculated) {
		this.turnoverEndOfYearCalculated = turnoverEndOfYearCalculated;
	}

	public final BigDecimal getAmountBeginOfYear() {
		return this.amountBeginOfYear;
	}

	public final void setAmountBeginOfYear(final BigDecimal amountBeginOfYear) {
		this.amountBeginOfYear = amountBeginOfYear;
	}

	public final Short getNextMonthHasMoneyflows() {
		return this.nextMonthHasMoneyflows;
	}

	public final void setNextMonthHasMoneyflows(final Short nextMonthHasMoneyflows) {
		this.nextMonthHasMoneyflows = nextMonthHasMoneyflows;
	}

	public final Short getPreviousMonthHasMoneyflows() {
		return this.previousMonthHasMoneyflows;
	}

	public final void setPreviousMonthHasMoneyflows(final Short previousMonthHasMoneyflows) {
		this.previousMonthHasMoneyflows = previousMonthHasMoneyflows;
	}

	public final Short getPreviousMonth() {
		return this.previousMonth;
	}

	public final void setPreviousMonth(final Short previousMonth) {
		this.previousMonth = previousMonth;
	}

	public final Short getPreviousYear() {
		return this.previousYear;
	}

	public final void setPreviousYear(final Short previousYear) {
		this.previousYear = previousYear;
	}

	public final Short getNextMonth() {
		return this.nextMonth;
	}

	public final void setNextMonth(final Short nextMonth) {
		this.nextMonth = nextMonth;
	}

	public final Short getNextYear() {
		return this.nextYear;
	}

	public final void setNextYear(final Short nextYear) {
		this.nextYear = nextYear;
	}

	public final List<Long> getMoneyflowsWithReceipt() {
		return this.moneyflowsWithReceipt;
	}

	public final void setMoneyflowsWithReceipt(final List<Long> moneyflowsWithReceipt) {
		this.moneyflowsWithReceipt = moneyflowsWithReceipt;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.allMonth == null) ? 0 : this.allMonth.hashCode());
		result = prime * result + ((this.allYears == null) ? 0 : this.allYears.hashCode());
		result = prime * result + ((this.amountBeginOfYear == null) ? 0 : this.amountBeginOfYear.hashCode());
		result = prime * result
				+ ((this.moneyflowSplitEntryTransports == null) ? 0 : this.moneyflowSplitEntryTransports.hashCode());
		result = prime * result + ((this.moneyflowTransports == null) ? 0 : this.moneyflowTransports.hashCode());
		result = prime * result + ((this.moneyflowsWithReceipt == null) ? 0 : this.moneyflowsWithReceipt.hashCode());
		result = prime * result + ((this.month == null) ? 0 : this.month.hashCode());
		result = prime * result + ((this.nextMonth == null) ? 0 : this.nextMonth.hashCode());
		result = prime * result + ((this.nextMonthHasMoneyflows == null) ? 0 : this.nextMonthHasMoneyflows.hashCode());
		result = prime * result + ((this.nextYear == null) ? 0 : this.nextYear.hashCode());
		result = prime * result + ((this.previousMonth == null) ? 0 : this.previousMonth.hashCode());
		result = prime * result
				+ ((this.previousMonthHasMoneyflows == null) ? 0 : this.previousMonthHasMoneyflows.hashCode());
		result = prime * result + ((this.previousYear == null) ? 0 : this.previousYear.hashCode());
		result = prime * result + ((this.reportTurnoverCapitalsourceTransports == null) ? 0
				: this.reportTurnoverCapitalsourceTransports.hashCode());
		result = prime * result
				+ ((this.turnoverEndOfYearCalculated == null) ? 0 : this.turnoverEndOfYearCalculated.hashCode());
		result = prime * result + ((this.year == null) ? 0 : this.year.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final ListReportsResponse other = (ListReportsResponse) obj;
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
		if (this.amountBeginOfYear == null) {
			if (other.amountBeginOfYear != null) {
				return false;
			}
		} else if (!this.amountBeginOfYear.equals(other.amountBeginOfYear)) {
			return false;
		}
		if (this.moneyflowSplitEntryTransports == null) {
			if (other.moneyflowSplitEntryTransports != null) {
				return false;
			}
		} else if (!this.moneyflowSplitEntryTransports.equals(other.moneyflowSplitEntryTransports)) {
			return false;
		}
		if (this.moneyflowTransports == null) {
			if (other.moneyflowTransports != null) {
				return false;
			}
		} else if (!this.moneyflowTransports.equals(other.moneyflowTransports)) {
			return false;
		}
		if (this.moneyflowsWithReceipt == null) {
			if (other.moneyflowsWithReceipt != null) {
				return false;
			}
		} else if (!this.moneyflowsWithReceipt.equals(other.moneyflowsWithReceipt)) {
			return false;
		}
		if (this.month == null) {
			if (other.month != null) {
				return false;
			}
		} else if (!this.month.equals(other.month)) {
			return false;
		}
		if (this.nextMonth == null) {
			if (other.nextMonth != null) {
				return false;
			}
		} else if (!this.nextMonth.equals(other.nextMonth)) {
			return false;
		}
		if (this.nextMonthHasMoneyflows == null) {
			if (other.nextMonthHasMoneyflows != null) {
				return false;
			}
		} else if (!this.nextMonthHasMoneyflows.equals(other.nextMonthHasMoneyflows)) {
			return false;
		}
		if (this.nextYear == null) {
			if (other.nextYear != null) {
				return false;
			}
		} else if (!this.nextYear.equals(other.nextYear)) {
			return false;
		}
		if (this.previousMonth == null) {
			if (other.previousMonth != null) {
				return false;
			}
		} else if (!this.previousMonth.equals(other.previousMonth)) {
			return false;
		}
		if (this.previousMonthHasMoneyflows == null) {
			if (other.previousMonthHasMoneyflows != null) {
				return false;
			}
		} else if (!this.previousMonthHasMoneyflows.equals(other.previousMonthHasMoneyflows)) {
			return false;
		}
		if (this.previousYear == null) {
			if (other.previousYear != null) {
				return false;
			}
		} else if (!this.previousYear.equals(other.previousYear)) {
			return false;
		}
		if (this.reportTurnoverCapitalsourceTransports == null) {
			if (other.reportTurnoverCapitalsourceTransports != null) {
				return false;
			}
		} else if (!this.reportTurnoverCapitalsourceTransports.equals(other.reportTurnoverCapitalsourceTransports)) {
			return false;
		}
		if (this.turnoverEndOfYearCalculated == null) {
			if (other.turnoverEndOfYearCalculated != null) {
				return false;
			}
		} else if (!this.turnoverEndOfYearCalculated.equals(other.turnoverEndOfYearCalculated)) {
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
		builder.append("ListReportsResponse [moneyflowTransports=");
		builder.append(this.moneyflowTransports);
		builder.append(", moneyflowSplitEntryTransports=");
		builder.append(this.moneyflowSplitEntryTransports);
		builder.append(", year=");
		builder.append(this.year);
		builder.append(", month=");
		builder.append(this.month);
		builder.append(", allYears=");
		builder.append(this.allYears);
		builder.append(", allMonth=");
		builder.append(this.allMonth);
		builder.append(", reportTurnoverCapitalsourceTransports=");
		builder.append(this.reportTurnoverCapitalsourceTransports);
		builder.append(", turnoverEndOfYearCalculated=");
		builder.append(this.turnoverEndOfYearCalculated);
		builder.append(", amountBeginOfYear=");
		builder.append(this.amountBeginOfYear);
		builder.append(", nextMonthHasMoneyflows=");
		builder.append(this.nextMonthHasMoneyflows);
		builder.append(", previousMonthHasMoneyflows=");
		builder.append(this.previousMonthHasMoneyflows);
		builder.append(", previousMonth=");
		builder.append(this.previousMonth);
		builder.append(", previousYear=");
		builder.append(this.previousYear);
		builder.append(", nextMonth=");
		builder.append(this.nextMonth);
		builder.append(", nextYear=");
		builder.append(this.nextYear);
		builder.append(", moneyflowsWithReceipt=");
		builder.append(this.moneyflowsWithReceipt);
		builder.append("]");
		return builder.toString();
	}

}
