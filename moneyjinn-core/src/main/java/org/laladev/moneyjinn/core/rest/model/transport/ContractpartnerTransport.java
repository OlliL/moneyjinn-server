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
// $Id: ContractpartnerTransport.java,v 1.5 2015/08/24 17:24:29 olivleh1 Exp $
//
package org.laladev.moneyjinn.core.rest.model.transport;

import java.sql.Date;

public class ContractpartnerTransport {
	private Integer id;
	private Integer userid;
	private String name;
	private String street;
	private Integer postcode;
	private String town;
	private Date validTil;
	private Date validFrom;
	private String country;
	private String moneyflowComment;
	private String postingAccountName;
	private Integer postingAccountId;

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

	public final String getName() {
		return name;
	}

	public final void setName(final String name) {
		this.name = name;
	}

	public final String getStreet() {
		return street;
	}

	public final void setStreet(final String street) {
		this.street = street;
	}

	public final Integer getPostcode() {
		return postcode;
	}

	public final void setPostcode(final Integer postcode) {
		this.postcode = postcode;
	}

	public final String getTown() {
		return town;
	}

	public final void setTown(final String town) {
		this.town = town;
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

	public final String getCountry() {
		return country;
	}

	public final void setCountry(final String country) {
		this.country = country;
	}

	public final String getMoneyflowComment() {
		return moneyflowComment;
	}

	public final void setMoneyflowComment(final String moneyflowComment) {
		this.moneyflowComment = moneyflowComment;
	}

	public final String getPostingAccountName() {
		return postingAccountName;
	}

	public final void setPostingAccountName(final String postingAccountName) {
		this.postingAccountName = postingAccountName;
	}

	public final Integer getPostingAccountId() {
		return postingAccountId;
	}

	public final void setPostingAccountId(final Integer postingAccountId) {
		this.postingAccountId = postingAccountId;
	}

}
