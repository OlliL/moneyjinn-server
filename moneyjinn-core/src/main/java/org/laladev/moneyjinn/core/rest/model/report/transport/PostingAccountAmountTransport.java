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
//

package org.laladev.moneyjinn.core.rest.model.report.transport;

import java.math.BigDecimal;
import java.sql.Date;

public class PostingAccountAmountTransport {
	private Date date;
	private BigDecimal amount;
	private Long postingaccountid;
	private String postingaccountname;

	public final Date getDate() {
		return this.date;
	}

	public final void setDate(final Date date) {
		this.date = date;
	}

	public final BigDecimal getAmount() {
		return this.amount;
	}

	public final void setAmount(final BigDecimal amount) {
		this.amount = amount;
	}

	public final Long getPostingaccountid() {
		return this.postingaccountid;
	}

	public final void setPostingaccountid(final Long postingaccountid) {
		this.postingaccountid = postingaccountid;
	}

	public final String getPostingaccountname() {
		return this.postingaccountname;
	}

	public final void setPostingaccountname(final String postingaccountname) {
		this.postingaccountname = postingaccountname;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.amount == null) ? 0 : this.amount.hashCode());
		result = prime * result + ((this.date == null) ? 0 : this.date.hashCode());
		result = prime * result + ((this.postingaccountid == null) ? 0 : this.postingaccountid.hashCode());
		result = prime * result + ((this.postingaccountname == null) ? 0 : this.postingaccountname.hashCode());
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
		final PostingAccountAmountTransport other = (PostingAccountAmountTransport) obj;
		if (this.amount == null) {
			if (other.amount != null) {
				return false;
			}
		} else if (!this.amount.equals(other.amount)) {
			return false;
		}
		if (this.date == null) {
			if (other.date != null) {
				return false;
			}
		} else if (!this.date.equals(other.date)) {
			return false;
		}
		if (this.postingaccountid == null) {
			if (other.postingaccountid != null) {
				return false;
			}
		} else if (!this.postingaccountid.equals(other.postingaccountid)) {
			return false;
		}
		if (this.postingaccountname == null) {
			if (other.postingaccountname != null) {
				return false;
			}
		} else if (!this.postingaccountname.equals(other.postingaccountname)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("PostingAccountAmountTransport [date=");
		builder.append(this.date);
		builder.append(", amount=");
		builder.append(this.amount);
		builder.append(", postingaccountid=");
		builder.append(this.postingaccountid);
		builder.append(", postingaccountname=");
		builder.append(this.postingaccountname);
		builder.append("]");
		return builder.toString();
	}
}
