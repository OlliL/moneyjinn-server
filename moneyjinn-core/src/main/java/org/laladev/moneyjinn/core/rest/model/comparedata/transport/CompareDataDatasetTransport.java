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

package org.laladev.moneyjinn.core.rest.model.comparedata.transport;

import java.math.BigDecimal;
import java.sql.Date;

public class CompareDataDatasetTransport {
	private Date bookingDate;
	private Date invoiceDate;
	private BigDecimal amount;
	private String partner;
	private String comment;

	public final Date getBookingDate() {
		return this.bookingDate;
	}

	public final void setBookingDate(final Date bookingDate) {
		this.bookingDate = bookingDate;
	}

	public final Date getInvoiceDate() {
		return this.invoiceDate;
	}

	public final void setInvoiceDate(final Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public final BigDecimal getAmount() {
		return this.amount;
	}

	public final void setAmount(final BigDecimal amount) {
		this.amount = amount;
	}

	public final String getPartner() {
		return this.partner;
	}

	public final void setPartner(final String partner) {
		this.partner = partner;
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
		result = prime * result + ((this.bookingDate == null) ? 0 : this.bookingDate.hashCode());
		result = prime * result + ((this.comment == null) ? 0 : this.comment.hashCode());
		result = prime * result + ((this.invoiceDate == null) ? 0 : this.invoiceDate.hashCode());
		result = prime * result + ((this.partner == null) ? 0 : this.partner.hashCode());
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
		final CompareDataDatasetTransport other = (CompareDataDatasetTransport) obj;
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
		if (this.comment == null) {
			if (other.comment != null) {
				return false;
			}
		} else if (!this.comment.equals(other.comment)) {
			return false;
		}
		if (this.invoiceDate == null) {
			if (other.invoiceDate != null) {
				return false;
			}
		} else if (!this.invoiceDate.equals(other.invoiceDate)) {
			return false;
		}
		if (this.partner == null) {
			if (other.partner != null) {
				return false;
			}
		} else if (!this.partner.equals(other.partner)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("CompareDataDatasetTransport [bookingDate=");
		builder.append(this.bookingDate);
		builder.append(", invoiceDate=");
		builder.append(this.invoiceDate);
		builder.append(", amount=");
		builder.append(this.amount);
		builder.append(", partner=");
		builder.append(this.partner);
		builder.append(", comment=");
		builder.append(this.comment);
		builder.append("]");
		return builder.toString();
	}

}
