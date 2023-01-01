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

package org.laladev.moneyjinn.core.rest.model.etf.transport;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

public class EtfSummaryTransport {
  private String isin;
  private String name;
  private String chartUrl;
  private BigDecimal amount;
  private BigDecimal spentValue;
  private BigDecimal buyPrice;
  private BigDecimal sellPrice;
  private Timestamp pricesTimestamp;

  public final String getIsin() {
    return this.isin;
  }

  public final void setIsin(final String isin) {
    this.isin = isin;
  }

  public final String getName() {
    return this.name;
  }

  public final void setName(final String name) {
    this.name = name;
  }

  public final String getChartUrl() {
    return this.chartUrl;
  }

  public final void setChartUrl(final String chartUrl) {
    this.chartUrl = chartUrl;
  }

  public final BigDecimal getAmount() {
    return this.amount;
  }

  public final void setAmount(final BigDecimal amount) {
    this.amount = amount;
  }

  public final BigDecimal getSpentValue() {
    return this.spentValue;
  }

  public final void setSpentValue(final BigDecimal spentValue) {
    this.spentValue = spentValue;
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

  public final Timestamp getPricesTimestamp() {
    return this.pricesTimestamp;
  }

  public final void setPricesTimestamp(final Timestamp pricesTimestamp) {
    this.pricesTimestamp = pricesTimestamp;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.amount, this.buyPrice, this.chartUrl, this.isin, this.name,
        this.pricesTimestamp, this.sellPrice, this.spentValue);
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
    final EtfSummaryTransport other = (EtfSummaryTransport) obj;
    return Objects.equals(this.amount, other.amount)
        && Objects.equals(this.buyPrice, other.buyPrice)
        && Objects.equals(this.chartUrl, other.chartUrl) && Objects.equals(this.isin, other.isin)
        && Objects.equals(this.name, other.name)
        && Objects.equals(this.pricesTimestamp, other.pricesTimestamp)
        && Objects.equals(this.sellPrice, other.sellPrice)
        && Objects.equals(this.spentValue, other.spentValue);
  }

  @Override
  public String toString() {
    return "EtfSummaryTransport [isin=" + this.isin + ", name=" + this.name + ", chartUrl="
        + this.chartUrl + ", amount=" + this.amount + ", spentValue=" + this.spentValue
        + ", buyPrice=" + this.buyPrice + ", sellPrice=" + this.sellPrice + ", pricesTimestamp="
        + this.pricesTimestamp + "]";
  }

}
