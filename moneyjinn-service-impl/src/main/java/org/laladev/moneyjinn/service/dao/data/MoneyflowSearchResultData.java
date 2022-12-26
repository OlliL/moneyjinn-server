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

package org.laladev.moneyjinn.service.dao.data;

import java.math.BigDecimal;

public class MoneyflowSearchResultData {
  private Short year;
  private Short month;
  private BigDecimal amount;
  private Long contractpartnerid;
  private String comment;

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

  public final BigDecimal getAmount() {
    return this.amount;
  }

  public final void setAmount(final BigDecimal amount) {
    this.amount = amount;
  }

  public final Long getContractpartnerid() {
    return this.contractpartnerid;
  }

  public final void setContractpartnerid(final Long contractpartnerid) {
    this.contractpartnerid = contractpartnerid;
  }

  public final String getComment() {
    return this.comment;
  }

  public final void setComment(final String comment) {
    this.comment = comment;
  }
}
