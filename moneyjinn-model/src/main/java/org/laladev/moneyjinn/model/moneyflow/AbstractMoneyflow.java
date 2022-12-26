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

package org.laladev.moneyjinn.model.moneyflow;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.laladev.moneyjinn.model.AbstractEntity;
import org.laladev.moneyjinn.model.AbstractEntityID;
import org.laladev.moneyjinn.model.Contractpartner;
import org.laladev.moneyjinn.model.PostingAccount;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;

public class AbstractMoneyflow<I extends AbstractEntityID<?>> extends AbstractEntity<I> {
  private static final long serialVersionUID = 1L;
  private User user;
  private Group group;
  private LocalDate bookingDate;
  private LocalDate invoiceDate;
  private BigDecimal amount;
  private Capitalsource capitalsource;
  private Contractpartner contractpartner;
  private String comment;
  private boolean privat;
  private PostingAccount postingAccount;

  public final User getUser() {
    return this.user;
  }

  public final void setUser(final User user) {
    this.user = user;
  }

  public final Group getGroup() {
    return this.group;
  }

  public final void setGroup(final Group group) {
    this.group = group;
  }

  public final LocalDate getBookingDate() {
    return this.bookingDate;
  }

  public final void setBookingDate(final LocalDate bookingDate) {
    this.bookingDate = bookingDate;
  }

  public final LocalDate getInvoiceDate() {
    return this.invoiceDate;
  }

  public final void setInvoiceDate(final LocalDate invoiceDate) {
    this.invoiceDate = invoiceDate;
  }

  public final BigDecimal getAmount() {
    return this.amount;
  }

  public final void setAmount(final BigDecimal amount) {
    this.amount = amount;
  }

  public final Capitalsource getCapitalsource() {
    return this.capitalsource;
  }

  public final void setCapitalsource(final Capitalsource capitalsource) {
    this.capitalsource = capitalsource;
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

  public final boolean isPrivat() {
    return this.privat;
  }

  public final void setPrivat(final boolean privat) {
    this.privat = privat;
  }

  public final PostingAccount getPostingAccount() {
    return this.postingAccount;
  }

  public final void setPostingAccount(final PostingAccount postingAccount) {
    this.postingAccount = postingAccount;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((this.amount == null) ? 0 : this.amount.hashCode());
    result = prime * result + ((this.bookingDate == null) ? 0 : this.bookingDate.hashCode());
    result = prime * result + ((this.capitalsource == null) ? 0 : this.capitalsource.hashCode());
    result = prime * result + ((this.comment == null) ? 0 : this.comment.hashCode());
    result = prime * result
        + ((this.contractpartner == null) ? 0 : this.contractpartner.hashCode());
    result = prime * result + ((this.group == null) ? 0 : this.group.hashCode());
    result = prime * result + ((this.invoiceDate == null) ? 0 : this.invoiceDate.hashCode());
    result = prime * result + ((this.postingAccount == null) ? 0 : this.postingAccount.hashCode());
    result = prime * result + (this.privat ? 1231 : 1237);
    result = prime * result + ((this.user == null) ? 0 : this.user.hashCode());
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
    @SuppressWarnings("rawtypes")
    final AbstractMoneyflow other = (AbstractMoneyflow) obj;
    if (this.amount == null) {
      if (other.amount != null) {
        return false;
      }
    } else if (!this.amount.equals(other.amount)) {
      return false;
    }
    if (this.bookingDate == null) {
      if (other.bookingDate != null) {
        return false;
      }
    } else if (!this.bookingDate.equals(other.bookingDate)) {
      return false;
    }
    if (this.capitalsource == null) {
      if (other.capitalsource != null) {
        return false;
      }
    } else if (!this.capitalsource.equals(other.capitalsource)) {
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
    if (this.group == null) {
      if (other.group != null) {
        return false;
      }
    } else if (!this.group.equals(other.group)) {
      return false;
    }
    if (this.invoiceDate == null) {
      if (other.invoiceDate != null) {
        return false;
      }
    } else if (!this.invoiceDate.equals(other.invoiceDate)) {
      return false;
    }
    if (this.postingAccount == null) {
      if (other.postingAccount != null) {
        return false;
      }
    } else if (!this.postingAccount.equals(other.postingAccount)) {
      return false;
    }
    if (this.privat != other.privat) {
      return false;
    }
    if (this.user == null) {
      if (other.user != null) {
        return false;
      }
    } else if (!this.user.equals(other.user)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append("Moneyflow [user=");
    builder.append(this.user);
    builder.append(", group=");
    builder.append(this.group);
    builder.append(", bookingDate=");
    builder.append(this.bookingDate);
    builder.append(", invoiceDate=");
    builder.append(this.invoiceDate);
    builder.append(", amount=");
    builder.append(this.amount);
    builder.append(", capitalsource=");
    builder.append(this.capitalsource);
    builder.append(", contractpartner=");
    builder.append(this.contractpartner);
    builder.append(", comment=");
    builder.append(this.comment);
    builder.append(", privat=");
    builder.append(this.privat);
    builder.append(", postingAccount=");
    builder.append(this.postingAccount);
    builder.append(", getId()=");
    builder.append(this.getId());
    builder.append("]");
    return builder.toString();
  }
}
