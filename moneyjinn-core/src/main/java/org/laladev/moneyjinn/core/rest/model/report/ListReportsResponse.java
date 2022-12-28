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

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import org.laladev.moneyjinn.core.rest.model.report.transport.ReportTurnoverCapitalsourceTransport;
import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowSplitEntryTransport;
import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowTransport;

@XmlRootElement(name = "listReportsResponse")
public class ListReportsResponse {
  @XmlElement(name = "moneyflowTransport")
  private List<MoneyflowTransport> moneyflowTransports;
  @XmlElement(name = "moneyflowSplitEntryTransport")
  private List<MoneyflowSplitEntryTransport> moneyflowSplitEntryTransports;
  private Short year;
  private Short month;
  @XmlElement(name = "reportTurnoverCapitalsourceTransport")
  private List<ReportTurnoverCapitalsourceTransport> reportTurnoverCapitalsourceTransports;
  private BigDecimal turnoverEndOfYearCalculated;
  private BigDecimal amountBeginOfYear;
  private List<Long> moneyflowsWithReceipt;

  public List<MoneyflowTransport> getMoneyflowTransports() {
    return this.moneyflowTransports;
  }

  public void setMoneyflowTransports(final List<MoneyflowTransport> moneyflowTransports) {
    this.moneyflowTransports = moneyflowTransports;
  }

  public List<MoneyflowSplitEntryTransport> getMoneyflowSplitEntryTransports() {
    return this.moneyflowSplitEntryTransports;
  }

  public void setMoneyflowSplitEntryTransports(
      final List<MoneyflowSplitEntryTransport> moneyflowSplitEntryTransports) {
    this.moneyflowSplitEntryTransports = moneyflowSplitEntryTransports;
  }

  public Short getYear() {
    return this.year;
  }

  public void setYear(final Short year) {
    this.year = year;
  }

  public Short getMonth() {
    return this.month;
  }

  public void setMonth(final Short month) {
    this.month = month;
  }

  public List<ReportTurnoverCapitalsourceTransport> getReportTurnoverCapitalsourceTransports() {
    return this.reportTurnoverCapitalsourceTransports;
  }

  public void setReportTurnoverCapitalsourceTransports(
      final List<ReportTurnoverCapitalsourceTransport> reportTurnoverCapitalsourceTransports) {
    this.reportTurnoverCapitalsourceTransports = reportTurnoverCapitalsourceTransports;
  }

  public BigDecimal getTurnoverEndOfYearCalculated() {
    return this.turnoverEndOfYearCalculated;
  }

  public void setTurnoverEndOfYearCalculated(final BigDecimal turnoverEndOfYearCalculated) {
    this.turnoverEndOfYearCalculated = turnoverEndOfYearCalculated;
  }

  public BigDecimal getAmountBeginOfYear() {
    return this.amountBeginOfYear;
  }

  public void setAmountBeginOfYear(final BigDecimal amountBeginOfYear) {
    this.amountBeginOfYear = amountBeginOfYear;
  }

  public List<Long> getMoneyflowsWithReceipt() {
    return this.moneyflowsWithReceipt;
  }

  public void setMoneyflowsWithReceipt(final List<Long> moneyflowsWithReceipt) {
    this.moneyflowsWithReceipt = moneyflowsWithReceipt;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.amountBeginOfYear, this.moneyflowSplitEntryTransports,
        this.moneyflowTransports, this.moneyflowsWithReceipt, this.month,
        this.reportTurnoverCapitalsourceTransports, this.turnoverEndOfYearCalculated, this.year);
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
    return Objects.equals(this.amountBeginOfYear, other.amountBeginOfYear)
        && Objects.equals(this.moneyflowSplitEntryTransports, other.moneyflowSplitEntryTransports)
        && Objects.equals(this.moneyflowTransports, other.moneyflowTransports)
        && Objects.equals(this.moneyflowsWithReceipt, other.moneyflowsWithReceipt)
        && Objects.equals(this.month, other.month)
        && Objects.equals(this.reportTurnoverCapitalsourceTransports,
            other.reportTurnoverCapitalsourceTransports)
        && Objects.equals(this.turnoverEndOfYearCalculated, other.turnoverEndOfYearCalculated)
        && Objects.equals(this.year, other.year);
  }

  @Override
  public String toString() {
    return "ListReportsResponse [moneyflowTransports=" + this.moneyflowTransports
        + ", moneyflowSplitEntryTransports=" + this.moneyflowSplitEntryTransports + ", year="
        + this.year + ", month=" + this.month + ", reportTurnoverCapitalsourceTransports="
        + this.reportTurnoverCapitalsourceTransports + ", turnoverEndOfYearCalculated="
        + this.turnoverEndOfYearCalculated + ", amountBeginOfYear=" + this.amountBeginOfYear
        + ", moneyflowsWithReceipt=" + this.moneyflowsWithReceipt + "]";
  }

}
