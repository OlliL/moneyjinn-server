//
// Copyright (c) 2015 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.model.moneyflow.search;

import java.math.BigDecimal;
import java.time.Month;
import org.laladev.moneyjinn.model.Contractpartner;

public class MoneyflowSearchResult {
  private Short year;
  private Month month;
  private BigDecimal amount;
  private Contractpartner contractpartner;
  private String comment;

  public final Short getYear() {
    return this.year;
  }

  public final void setYear(final Short year) {
    this.year = year;
  }

  public final Month getMonth() {
    return this.month;
  }

  public final void setMonth(final Month month) {
    this.month = month;
  }

  public final BigDecimal getAmount() {
    return this.amount;
  }

  public final void setAmount(final BigDecimal amount) {
    this.amount = amount;
  }

  public final Contractpartner getContractpartner() {
    return this.contractpartner;
  }

  public final void setContractpartner(final Contractpartner contractpartner) {
    this.contractpartner = contractpartner;
  }

  public final String getComment() {
    return this.comment;
  }

  public final void setComment(final String comment) {
    this.comment = comment;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.amount == null) ? 0 : this.amount.hashCode());
    result = prime * result + ((this.comment == null) ? 0 : this.comment.hashCode());
    result = prime * result
        + ((this.contractpartner == null) ? 0 : this.contractpartner.hashCode());
    result = prime * result + ((this.month == null) ? 0 : this.month.hashCode());
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
    final MoneyflowSearchResult other = (MoneyflowSearchResult) obj;
    if (this.amount == null) {
      if (other.amount != null) {
        return false;
      }
    } else if (!this.amount.equals(other.amount)) {
      return false;
    }
    if (this.comment == null) {
      if (other.comment != null) {
        return false;
      }
    } else if (!this.comment.equals(other.comment)) {
      return false;
    }
    if (this.contractpartner == null) {
      if (other.contractpartner != null) {
        return false;
      }
    } else if (!this.contractpartner.equals(other.contractpartner)) {
      return false;
    }
    if (this.month != other.month) {
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
    builder.append("MoneyflowSearchResult [year=");
    builder.append(this.year);
    builder.append(", month=");
    builder.append(this.month);
    builder.append(", amount=");
    builder.append(this.amount);
    builder.append(", contractpartner=");
    builder.append(this.contractpartner);
    builder.append(", comment=");
    builder.append(this.comment);
    builder.append("]");
    return builder.toString();
  }
}
