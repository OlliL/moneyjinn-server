//
// Copyright (c) 2015 Oliver Lehmann <oliver@laladev.org>
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

package org.laladev.moneyjinn.core.rest.model.transport;

import java.math.BigDecimal;

public class MonthlySettlementTransport {
	private Long id;
	private Long userid;
	private Short year;
	private Short month;
	private BigDecimal amount;
	private Long capitalsourceid;
	private String capitalsourcecomment;
	private Short capitalsourcegroupuse;

	public final Long getId() {
		return this.id;
	}

	public final void setId(final Long id) {
		this.id = id;
	}

	public final Long getUserid() {
		return this.userid;
	}

	public final void setUserid(final Long userid) {
		this.userid = userid;
	}

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

	public final Long getCapitalsourceid() {
		return this.capitalsourceid;
	}

	public final void setCapitalsourceid(final Long capitalsourceid) {
		this.capitalsourceid = capitalsourceid;
	}

	public final String getCapitalsourcecomment() {
		return this.capitalsourcecomment;
	}

	public final void setCapitalsourcecomment(final String capitalsourcecomment) {
		this.capitalsourcecomment = capitalsourcecomment;
	}

	public final Short getCapitalsourcegroupuse() {
		return this.capitalsourcegroupuse;
	}

	public final void setCapitalsourcegroupuse(final Short capitalsourcegroupuse) {
		this.capitalsourcegroupuse = capitalsourcegroupuse;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.amount == null) ? 0 : this.amount.hashCode());
		result = prime * result + ((this.capitalsourcecomment == null) ? 0 : this.capitalsourcecomment.hashCode());
		result = prime * result + ((this.capitalsourcegroupuse == null) ? 0 : this.capitalsourcegroupuse.hashCode());
		result = prime * result + ((this.capitalsourceid == null) ? 0 : this.capitalsourceid.hashCode());
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result + ((this.month == null) ? 0 : this.month.hashCode());
		result = prime * result + ((this.userid == null) ? 0 : this.userid.hashCode());
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
		final MonthlySettlementTransport other = (MonthlySettlementTransport) obj;
		if (this.amount == null) {
			if (other.amount != null) {
				return false;
			}
		} else if (!this.amount.equals(other.amount)) {
			return false;
		}
		if (this.capitalsourcecomment == null) {
			if (other.capitalsourcecomment != null) {
				return false;
			}
		} else if (!this.capitalsourcecomment.equals(other.capitalsourcecomment)) {
			return false;
		}
		if (this.capitalsourcegroupuse == null) {
			if (other.capitalsourcegroupuse != null) {
				return false;
			}
		} else if (!this.capitalsourcegroupuse.equals(other.capitalsourcegroupuse)) {
			return false;
		}
		if (this.capitalsourceid == null) {
			if (other.capitalsourceid != null) {
				return false;
			}
		} else if (!this.capitalsourceid.equals(other.capitalsourceid)) {
			return false;
		}
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		if (this.month == null) {
			if (other.month != null) {
				return false;
			}
		} else if (!this.month.equals(other.month)) {
			return false;
		}
		if (this.userid == null) {
			if (other.userid != null) {
				return false;
			}
		} else if (!this.userid.equals(other.userid)) {
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
		builder.append("MonthlySettlementTransport [id=");
		builder.append(this.id);
		builder.append(", userid=");
		builder.append(this.userid);
		builder.append(", year=");
		builder.append(this.year);
		builder.append(", month=");
		builder.append(this.month);
		builder.append(", amount=");
		builder.append(this.amount);
		builder.append(", capitalsourceid=");
		builder.append(this.capitalsourceid);
		builder.append(", capitalsourcecomment=");
		builder.append(this.capitalsourcecomment);
		builder.append(", capitalsourcegroupuse=");
		builder.append(this.capitalsourcegroupuse);
		builder.append("]");
		return builder.toString();
	}

}
