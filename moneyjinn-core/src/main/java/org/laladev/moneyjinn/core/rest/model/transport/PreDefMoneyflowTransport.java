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
// $Id: PreDefMoneyflowTransport.java,v 1.4 2015/08/24 17:24:29 olivleh1 Exp $
//
package org.laladev.moneyjinn.core.rest.model.transport;

import java.math.BigDecimal;
import java.sql.Date;

public class PreDefMoneyflowTransport {
	private Integer id;
	private Integer userid;
	private BigDecimal amount;
	private Integer capitalsourceid;
	private String capitalsourcecomment;
	private Integer contractpartnerid;
	private String contractpartnername;
	private String comment;
	private Date createdate;
	private Boolean onceAMonth;
	private Date lastUsed;
	private Integer postingaccountid;
	private String postingaccountname;

	public final Integer getId() {
		return id;
	}

	public final void setId(final Integer id) {
		this.id = id;
	}

	public final Integer getUserid() {
		return userid;
	}

	public final void setUserid(final Integer userid) {
		this.userid = userid;
	}

	public final BigDecimal getAmount() {
		return amount;
	}

	public final void setAmount(final BigDecimal amount) {
		this.amount = amount;
	}

	public final Integer getCapitalsourceid() {
		return capitalsourceid;
	}

	public final void setCapitalsourceid(final Integer capitalsourceid) {
		this.capitalsourceid = capitalsourceid;
	}

	public final String getCapitalsourcecomment() {
		return capitalsourcecomment;
	}

	public final void setCapitalsourcecomment(final String capitalsourcecomment) {
		this.capitalsourcecomment = capitalsourcecomment;
	}

	public final Integer getContractpartnerid() {
		return contractpartnerid;
	}

	public final void setContractpartnerid(final Integer contractpartnerid) {
		this.contractpartnerid = contractpartnerid;
	}

	public final String getContractpartnername() {
		return contractpartnername;
	}

	public final void setContractpartnername(final String contractpartnername) {
		this.contractpartnername = contractpartnername;
	}

	public final String getComment() {
		return comment;
	}

	public final void setComment(final String comment) {
		this.comment = comment;
	}

	public final Date getCreatedate() {
		return createdate;
	}

	public final void setCreatedate(final Date createdate) {
		this.createdate = createdate;
	}

	public final Boolean getOnceAMonth() {
		return onceAMonth;
	}

	public final void setOnceAMonth(final Boolean onceAMonth) {
		this.onceAMonth = onceAMonth;
	}

	public final Date getLastUsed() {
		return lastUsed;
	}

	public final void setLastUsed(final Date lastUsed) {
		this.lastUsed = lastUsed;
	}

	public final Integer getPostingaccountid() {
		return postingaccountid;
	}

	public final void setPostingaccountid(final Integer postingaccountid) {
		this.postingaccountid = postingaccountid;
	}

	public final String getPostingaccountname() {
		return postingaccountname;
	}

	public final void setPostingaccountname(final String postingaccountname) {
		this.postingaccountname = postingaccountname;
	}

}
