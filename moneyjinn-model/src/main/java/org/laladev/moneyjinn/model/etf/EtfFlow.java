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
import java.time.LocalDateTime;
import org.laladev.moneyjinn.model.AbstractEntity;

public class EtfFlow extends AbstractEntity<EtfFlowID> {
  private static final long serialVersionUID = 1L;
  private EtfIsin isin;
  private LocalDateTime time;
  private BigDecimal amount;
  private BigDecimal price;

  public final EtfIsin getIsin() {
    return this.isin;
  }

  public final void setIsin(final EtfIsin isin) {
    this.isin = isin;
  }

  public final LocalDateTime getTime() {
    return this.time;
  }

  public final void setTime(final LocalDateTime time) {
    this.time = time;
  }

  public final BigDecimal getAmount() {
    return this.amount;
  }

  public final void setAmount(final BigDecimal amount) {
    this.amount = amount;
  }

  public final BigDecimal getPrice() {
    return this.price;
  }

  public final void setPrice(final BigDecimal price) {
    this.price = price;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((this.amount == null) ? 0 : this.amount.hashCode());
    result = prime * result + ((this.isin == null) ? 0 : this.isin.hashCode());
    result = prime * result + ((this.price == null) ? 0 : this.price.hashCode());
    result = prime * result + ((this.time == null) ? 0 : this.time.hashCode());
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
    final EtfFlow other = (EtfFlow) obj;
    if (this.amount == null) {
      if (other.amount != null) {
        return false;
      }
    } else if (!this.amount.equals(other.amount)) {
      return false;
    }
    if (this.isin == null) {
      if (other.isin != null) {
        return false;
      }
    } else if (!this.isin.equals(other.isin)) {
      return false;
    }
    if (this.price == null) {
      if (other.price != null) {
        return false;
      }
    } else if (!this.price.equals(other.price)) {
      return false;
    }
    if (this.time == null) {
      if (other.time != null) {
        return false;
      }
    } else if (!this.time.equals(other.time)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append("EtfFlow [isin=");
    builder.append(this.isin);
    builder.append(", time=");
    builder.append(this.time);
    builder.append(", amount=");
    builder.append(this.amount);
    builder.append(", price=");
    builder.append(this.price);
    builder.append("]");
    return builder.toString();
  }
}
