//
//Copyright (c) 2022 Oliver Lehmann <lehmann@ans-netz.de>
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

import java.util.List;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "getAvailableMonthResponse")
public class GetAvailableMonthResponse {
  private Short year;
  private Short month;
  private List<Short> allYears;
  private List<Short> allMonth;
  private Short nextMonthHasMoneyflows;
  private Short previousMonthHasMoneyflows;
  private Short previousMonth;
  private Short previousYear;
  private Short nextMonth;
  private Short nextYear;

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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.allMonth == null) ? 0 : this.allMonth.hashCode());
    result = prime * result + ((this.allYears == null) ? 0 : this.allYears.hashCode());
    result = prime * result + ((this.month == null) ? 0 : this.month.hashCode());
    result = prime * result + ((this.nextMonth == null) ? 0 : this.nextMonth.hashCode());
    result = prime * result
        + ((this.nextMonthHasMoneyflows == null) ? 0 : this.nextMonthHasMoneyflows.hashCode());
    result = prime * result + ((this.nextYear == null) ? 0 : this.nextYear.hashCode());
    result = prime * result + ((this.previousMonth == null) ? 0 : this.previousMonth.hashCode());
    result = prime * result + ((this.previousMonthHasMoneyflows == null) ? 0
        : this.previousMonthHasMoneyflows.hashCode());
    result = prime * result + ((this.previousYear == null) ? 0 : this.previousYear.hashCode());
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
    final GetAvailableMonthResponse other = (GetAvailableMonthResponse) obj;
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
    builder.append("GetAvailableMonthResponse [year=");
    builder.append(this.year);
    builder.append(", month=");
    builder.append(this.month);
    builder.append(", allYears=");
    builder.append(this.allYears);
    builder.append(", allMonth=");
    builder.append(this.allMonth);
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
    builder.append("]");
    return builder.toString();
  }
}
