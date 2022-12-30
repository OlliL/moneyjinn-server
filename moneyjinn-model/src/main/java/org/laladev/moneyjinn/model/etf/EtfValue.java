//
// Copyright (c) 2021 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.model.etf;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class EtfValue {
  private EtfIsin isin;
  private LocalDate date;
  private BigDecimal buyPrice;
  private BigDecimal sellPrice;
  private LocalDateTime changeDate;

  public final EtfIsin getIsin() {
    return this.isin;
  }

  public final void setIsin(final EtfIsin isin) {
    this.isin = isin;
  }

  public final LocalDate getDate() {
    return this.date;
  }

  public final void setDate(final LocalDate date) {
    this.date = date;
  }

  public final BigDecimal getBuyPrice() {
    return this.buyPrice;
  }

  public final void setBuyPrice(final BigDecimal buyPrice) {
    this.buyPrice = buyPrice;
  }

  public final BigDecimal getSellPrice() {
    return this.sellPrice;
  }

  public final void setSellPrice(final BigDecimal sellPrice) {
    this.sellPrice = sellPrice;
  }

  public final LocalDateTime getChangeDate() {
    return this.changeDate;
  }

  public final void setChangeDate(final LocalDateTime changeDate) {
    this.changeDate = changeDate;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.buyPrice, this.changeDate, this.date, this.isin, this.sellPrice);
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
    final EtfValue other = (EtfValue) obj;
    return Objects.equals(this.buyPrice, other.buyPrice)
        && Objects.equals(this.changeDate, other.changeDate)
        && Objects.equals(this.date, other.date) && Objects.equals(this.isin, other.isin)
        && Objects.equals(this.sellPrice, other.sellPrice);
  }

  @Override
  public String toString() {
    return "EtfValue [isin=" + this.isin + ", date=" + this.date + ", buyPrice=" + this.buyPrice
        + ", sellPrice=" + this.sellPrice + ", changeDate=" + this.changeDate + "]";
  }

}
