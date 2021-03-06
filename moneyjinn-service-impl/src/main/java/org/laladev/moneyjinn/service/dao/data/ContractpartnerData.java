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

import java.time.LocalDate;

public class ContractpartnerData {
	private Long id;
	private Long macIdCreator;
	private Long macIdAccessor;
	private String name;
	private String street;
	private Integer postcode;
	private String town;
	private String country;
	private LocalDate validTil;
	private LocalDate validFrom;
	private String mmfComment;
	private Long mpaPostingAccountId;

	public final Long getId() {
		return this.id;
	}

	public final void setId(final Long id) {
		this.id = id;
	}

	public final Long getMacIdCreator() {
		return this.macIdCreator;
	}

	public final void setMacIdCreator(final Long macIdCreator) {
		this.macIdCreator = macIdCreator;
	}

	public final Long getMacIdAccessor() {
		return this.macIdAccessor;
	}

	public final void setMacIdAccessor(final Long macIdAccessor) {
		this.macIdAccessor = macIdAccessor;
	}

	public final String getName() {
		return this.name;
	}

	public final void setName(final String name) {
		this.name = name;
	}

	public final String getStreet() {
		return this.street;
	}

	public final void setStreet(final String street) {
		this.street = street;
	}

	public final Integer getPostcode() {
		return this.postcode;
	}

	public final void setPostcode(final Integer postcode) {
		this.postcode = postcode;
	}

	public final String getTown() {
		return this.town;
	}

	public final void setTown(final String town) {
		this.town = town;
	}

	public final String getCountry() {
		return this.country;
	}

	public final void setCountry(final String country) {
		this.country = country;
	}

	public final LocalDate getValidTil() {
		return this.validTil;
	}

	public final void setValidTil(final LocalDate validTil) {
		this.validTil = validTil;
	}

	public final LocalDate getValidFrom() {
		return this.validFrom;
	}

	public final void setValidFrom(final LocalDate validFrom) {
		this.validFrom = validFrom;
	}

	public final String getMmfComment() {
		return this.mmfComment;
	}

	public final void setMmfComment(final String mmfComment) {
		this.mmfComment = mmfComment;
	}

	public final Long getMpaPostingAccountId() {
		return this.mpaPostingAccountId;
	}

	public final void setMpaPostingAccountId(final Long mpaPostingAccountId) {
		this.mpaPostingAccountId = mpaPostingAccountId;
	}

}
