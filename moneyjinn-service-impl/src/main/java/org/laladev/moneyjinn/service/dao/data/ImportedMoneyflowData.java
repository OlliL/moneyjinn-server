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
import java.time.LocalDate;

public class ImportedMoneyflowData {
  private Long id;
  private String externalId;
  private Long mcsCapitalsourceId;
  private LocalDate bookingdate;
  private LocalDate invoicedate;
  private String name;
  private String accountNumber;
  private String bankCode;
  private String comment;
  private BigDecimal amount;
  private Short status;

  public final Long getId() {
    return this.id;
  }

  public final void setId(final Long id) {
    this.id = id;
  }

  public final String getExternalId() {
    return this.externalId;
  }

  public final void setExternalId(final String externalId) {
    this.externalId = externalId;
  }

  public final Long getMcsCapitalsourceId() {
    return this.mcsCapitalsourceId;
  }

  public final void setMcsCapitalsourceId(final Long mcsCapitalsourceId) {
    this.mcsCapitalsourceId = mcsCapitalsourceId;
  }

  public final LocalDate getBookingdate() {
    return this.bookingdate;
  }

  public final void setBookingdate(final LocalDate bookingdate) {
    this.bookingdate = bookingdate;
  }

  public final LocalDate getInvoicedate() {
    return this.invoicedate;
  }

  public final void setInvoicedate(final LocalDate invoicedate) {
    this.invoicedate = invoicedate;
  }

  public final String getName() {
    return this.name;
  }

  public final void setName(final String name) {
    this.name = name;
  }

  public final String getAccountNumber() {
    return this.accountNumber;
  }

  public final void setAccountNumber(final String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public final String getBankCode() {
    return this.bankCode;
  }

  public final void setBankCode(final String bankCode) {
    this.bankCode = bankCode;
  }

  public final String getComment() {
    return this.comment;
  }

  public final void setComment(final String comment) {
    this.comment = comment;
  }

  public final BigDecimal getAmount() {
    return this.amount;
  }

  public final void setAmount(final BigDecimal amount) {
    this.amount = amount;
  }

  public final Short getStatus() {
    return this.status;
  }

  public final void setStatus(final Short status) {
    this.status = status;
  }
}
