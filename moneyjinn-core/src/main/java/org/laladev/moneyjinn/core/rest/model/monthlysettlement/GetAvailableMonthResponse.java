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

package org.laladev.moneyjinn.core.rest.model.monthlysettlement;

import java.util.List;
import java.util.Objects;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "getAvailableMonthResponse")
public class GetAvailableMonthResponse {
  private Short year;
  private Short month;
  private List<Short> allYears;
  private List<Short> allMonth;

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

  public List<Short> getAllYears() {
    return this.allYears;
  }

  public void setAllYears(final List<Short> allYears) {
    this.allYears = allYears;
  }

  public List<Short> getAllMonth() {
    return this.allMonth;
  }

  public void setAllMonth(final List<Short> allMonth) {
    this.allMonth = allMonth;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.allMonth, this.allYears, this.month, this.year);
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
    return Objects.equals(this.allMonth, other.allMonth)
        && Objects.equals(this.allYears, other.allYears) && Objects.equals(this.month, other.month)
        && Objects.equals(this.year, other.year);
  }

  @Override
  public String toString() {
    return "GetAvailableMonthResponse [year=" + this.year + ", month=" + this.month + ", allYears="
        + this.allYears + ", allMonth=" + this.allMonth + "]";
  }
}
