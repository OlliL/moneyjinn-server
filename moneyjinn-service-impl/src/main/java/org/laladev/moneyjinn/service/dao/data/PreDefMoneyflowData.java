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

import java.math.BigDecimal;
import java.time.LocalDate;

public class PreDefMoneyflowData {
	private Long id;
	private Long macId;
	private BigDecimal amount;
	private Long mcsCapitalsourceId;
	private Long mcpContractpartnerId;
	private String comment;
	private LocalDate createdate;
	private boolean onceAMonth;
	private LocalDate lastUsed;
	private Long mpaPostingAccountId;

	public final Long getId() {
		return this.id;
	}

	public final void setId(final Long id) {
		this.id = id;
	}

	public final Long getMacId() {
		return this.macId;
	}

	public final void setMacId(final Long macId) {
		this.macId = macId;
	}

	public final BigDecimal getAmount() {
		return this.amount;
	}

	public final void setAmount(final BigDecimal amount) {
		this.amount = amount;
	}

	public final Long getMcsCapitalsourceId() {
		return this.mcsCapitalsourceId;
	}

	public final void setMcsCapitalsourceId(final Long mcsCapitalsourceId) {
		this.mcsCapitalsourceId = mcsCapitalsourceId;
	}

	public final Long getMcpContractpartnerId() {
		return this.mcpContractpartnerId;
	}

	public final void setMcpContractpartnerId(final Long mcpContractpartnerId) {
		this.mcpContractpartnerId = mcpContractpartnerId;
	}

	public final String getComment() {
		return this.comment;
	}

	public final void setComment(final String comment) {
		this.comment = comment;
	}

	public final LocalDate getCreatedate() {
		return this.createdate;
	}

	public final void setCreatedate(final LocalDate createdate) {
		this.createdate = createdate;
	}

	public final boolean isOnceAMonth() {
		return this.onceAMonth;
	}

	public final void setOnceAMonth(final boolean onceAMonth) {
		this.onceAMonth = onceAMonth;
	}

	public final LocalDate getLastUsed() {
		return this.lastUsed;
	}

	public final void setLastUsed(final LocalDate lastUsed) {
		this.lastUsed = lastUsed;
	}

	public final Long getMpaPostingAccountId() {
		return this.mpaPostingAccountId;
	}

	public final void setMpaPostingAccountId(final Long mpaPostingAccountId) {
		this.mpaPostingAccountId = mpaPostingAccountId;
	}

}
