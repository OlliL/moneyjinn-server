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
// $Id: CapitalsourceTransport.java,v 1.5 2015/08/24 17:24:29 olivleh1 Exp $
//
package org.laladev.moneyjinn.core.rest.model.transport;

import java.sql.Date;

public class CapitalsourceTransport {
	private Integer id;
	private Integer userid;
	private Integer type;
	private Integer state;
	private String accountNumber;
	private String bankCode;
	private String comment;
	private Date validTil;
	private Date validFrom;
	private Boolean groupUse;
	private Boolean importAllowed;

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

	public final Integer getType() {
		return type;
	}

	public final void setType(final Integer type) {
		this.type = type;
	}

	public final Integer getState() {
		return state;
	}

	public final void setState(final Integer state) {
		this.state = state;
	}

	public final String getAccountNumber() {
		return accountNumber;
	}

	public final void setAccountNumber(final String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public final String getBankCode() {
		return bankCode;
	}

	public final void setBankCode(final String bankCode) {
		this.bankCode = bankCode;
	}

	public final String getComment() {
		return comment;
	}

	public final void setComment(final String comment) {
		this.comment = comment;
	}

	public final Date getValidTil() {
		return validTil;
	}

	public final void setValidTil(final Date validTil) {
		this.validTil = validTil;
	}

	public final Date getValidFrom() {
		return validFrom;
	}

	public final void setValidFrom(final Date validFrom) {
		this.validFrom = validFrom;
	}

	public final Boolean getGroupUse() {
		return groupUse;
	}

	public final void setGroupUse(final Boolean groupUse) {
		this.groupUse = groupUse;
	}

	public final Boolean getImportAllowed() {
		return importAllowed;
	}

	public final void setImportAllowed(final Boolean importAllowed) {
		this.importAllowed = importAllowed;
	}

}
