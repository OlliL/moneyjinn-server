//
// Copyright (c) 2014-2015 Oliver Lehmann <oliver@laladev.org>
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
// $Id: MoneyflowTransport.java,v 1.5 2015/08/24 17:24:29 olivleh1 Exp $
//
package org.laladev.moneyjinn.core.rest.model.transport;

import java.math.BigDecimal;
import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MoneyflowTransport {
	private Long id;
	private Long userid;
	private Date bookingdate;
	private Date invoicedate;
	private BigDecimal amount;
	private Long capitalsourceid;
	private String capitalsourcecomment;
	private Long contractpartnerid;
	private String contractpartnername;
	private String comment;
	@JsonProperty("private")
	private Short privat;
	private Long postingaccountid;
	private String postingaccountname;

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

	public final Date getBookingdate() {
		return this.bookingdate;
	}

	public final void setBookingdate(final Date bookingdate) {
		this.bookingdate = bookingdate;
	}

	public final Date getInvoicedate() {
		return this.invoicedate;
	}

	public final void setInvoicedate(final Date invoicedate) {
		this.invoicedate = invoicedate;
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

	public final Long getContractpartnerid() {
		return this.contractpartnerid;
	}

	public final void setContractpartnerid(final Long contractpartnerid) {
		this.contractpartnerid = contractpartnerid;
	}

	public final String getContractpartnername() {
		return this.contractpartnername;
	}

	public final void setContractpartnername(final String contractpartnername) {
		this.contractpartnername = contractpartnername;
	}

	public final String getComment() {
		return this.comment;
	}

	public final void setComment(final String comment) {
		this.comment = comment;
	}

	public final Short getPrivat() {
		return this.privat;
	}

	public final void setPrivat(final Short privat) {
		this.privat = privat;
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
		result = prime * result + ((this.bookingdate == null) ? 0 : this.bookingdate.hashCode());
		result = prime * result + ((this.capitalsourcecomment == null) ? 0 : this.capitalsourcecomment.hashCode());
		result = prime * result + ((this.capitalsourceid == null) ? 0 : this.capitalsourceid.hashCode());
		result = prime * result + ((this.comment == null) ? 0 : this.comment.hashCode());
		result = prime * result + ((this.contractpartnerid == null) ? 0 : this.contractpartnerid.hashCode());
		result = prime * result + ((this.contractpartnername == null) ? 0 : this.contractpartnername.hashCode());
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result + ((this.invoicedate == null) ? 0 : this.invoicedate.hashCode());
		result = prime * result + ((this.postingaccountid == null) ? 0 : this.postingaccountid.hashCode());
		result = prime * result + ((this.postingaccountname == null) ? 0 : this.postingaccountname.hashCode());
		result = prime * result + ((this.privat == null) ? 0 : this.privat.hashCode());
		result = prime * result + ((this.userid == null) ? 0 : this.userid.hashCode());
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
		final MoneyflowTransport other = (MoneyflowTransport) obj;
		if (this.amount == null) {
			if (other.amount != null) {
				return false;
			}
		} else if (!this.amount.equals(other.amount)) {
			return false;
		}
		if (this.bookingdate == null) {
			if (other.bookingdate != null) {
				return false;
			}
		} else if (!this.bookingdate.equals(other.bookingdate)) {
			return false;
		}
		if (this.capitalsourcecomment == null) {
			if (other.capitalsourcecomment != null) {
				return false;
			}
		} else if (!this.capitalsourcecomment.equals(other.capitalsourcecomment)) {
			return false;
		}
		if (this.capitalsourceid == null) {
			if (other.capitalsourceid != null) {
				return false;
			}
		} else if (!this.capitalsourceid.equals(other.capitalsourceid)) {
			return false;
		}
		if (this.comment == null) {
			if (other.comment != null) {
				return false;
			}
		} else if (!this.comment.equals(other.comment)) {
			return false;
		}
		if (this.contractpartnerid == null) {
			if (other.contractpartnerid != null) {
				return false;
			}
		} else if (!this.contractpartnerid.equals(other.contractpartnerid)) {
			return false;
		}
		if (this.contractpartnername == null) {
			if (other.contractpartnername != null) {
				return false;
			}
		} else if (!this.contractpartnername.equals(other.contractpartnername)) {
			return false;
		}
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		if (this.invoicedate == null) {
			if (other.invoicedate != null) {
				return false;
			}
		} else if (!this.invoicedate.equals(other.invoicedate)) {
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
		if (this.privat == null) {
			if (other.privat != null) {
				return false;
			}
		} else if (!this.privat.equals(other.privat)) {
			return false;
		}
		if (this.userid == null) {
			if (other.userid != null) {
				return false;
			}
		} else if (!this.userid.equals(other.userid)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("MoneyflowTransport [id=");
		builder.append(this.id);
		builder.append(", userid=");
		builder.append(this.userid);
		builder.append(", bookingdate=");
		builder.append(this.bookingdate);
		builder.append(", invoicedate=");
		builder.append(this.invoicedate);
		builder.append(", amount=");
		builder.append(this.amount);
		builder.append(", capitalsourceid=");
		builder.append(this.capitalsourceid);
		builder.append(", capitalsourcecomment=");
		builder.append(this.capitalsourcecomment);
		builder.append(", contractpartnerid=");
		builder.append(this.contractpartnerid);
		builder.append(", contractpartnername=");
		builder.append(this.contractpartnername);
		builder.append(", comment=");
		builder.append(this.comment);
		builder.append(", privat=");
		builder.append(this.privat);
		builder.append(", postingaccountid=");
		builder.append(this.postingaccountid);
		builder.append(", postingaccountname=");
		builder.append(this.postingaccountname);
		builder.append("]");
		return builder.toString();
	}

}
