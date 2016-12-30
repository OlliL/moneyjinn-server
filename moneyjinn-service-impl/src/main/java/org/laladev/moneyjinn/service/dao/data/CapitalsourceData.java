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

package org.laladev.moneyjinn.service.dao.data;

import java.time.LocalDate;

public class CapitalsourceData {
	private Long id;
	private Long macIdCreator;
	private Long macIdAccessor;
	private Short type;
	private Short state;
	private String accountNumber;
	private String bankCode;
	private String comment;
	private LocalDate validTil;
	private LocalDate validFrom;
	private boolean attGroupUse;
	private Short importAllowed;

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

	public final Short getType() {
		return this.type;
	}

	public final void setType(final Short type) {
		this.type = type;
	}

	public final Short getState() {
		return this.state;
	}

	public final void setState(final Short state) {
		this.state = state;
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

	public final boolean isAttGroupUse() {
		return this.attGroupUse;
	}

	public final void setAttGroupUse(final boolean attGroupUse) {
		this.attGroupUse = attGroupUse;
	}

	public final Short getImportAllowed() {
		return this.importAllowed;
	}

	public final void setImportAllowed(final Short importAllowed) {
		this.importAllowed = importAllowed;
	}

}
