
package org.laladev.moneyjinn.service.dao.data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class EtfValueData {
  private String isin;
  private LocalDate date;
  private BigDecimal buyPrice;
  private BigDecimal sellPrice;
  private LocalDateTime changedate;

  public final String getIsin() {
    return this.isin;
  }

  public final void setIsin(final String isin) {
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

  public final LocalDateTime getChangedate() {
    return this.changedate;
  }

  public final void setChangedate(final LocalDateTime changedate) {
    this.changedate = changedate;
  }
}
