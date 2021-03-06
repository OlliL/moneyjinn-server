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

import java.math.BigDecimal;
import java.time.LocalDate;

public class MoneyflowData {
	private Long id;
	private Long macIdCreator;
	private Long macIdAccessor;
	private LocalDate bookingdate;
	private LocalDate invoicedate;
	private BigDecimal amount;
	private Long mcsCapitalsourceId;
	private Long mcpContractpartnerId;
	private String comment;
	private Long mpaPostingAccountId;
	private boolean privat;

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

	public final LocalDate getBookingdate() {
		return this.bookingdate;
	}

	public final void setBookingdate(final LocalDate bookingdate) {
		this.bookingdate = bookingdate;
	}

	public final LocalDate getInvoicedate() {
		return this.invoicedate;
	}

	public final void setInvoicedate(final LocalDate invoicedate) {
		this.invoicedate = invoicedate;
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

	public final Long getMpaPostingAccountId() {
		return this.mpaPostingAccountId;
	}

	public final void setMpaPostingAccountId(final Long mpaPostingAccountId) {
		this.mpaPostingAccountId = mpaPostingAccountId;
	}

	public final boolean isPrivat() {
		return this.privat;
	}

	public final void setPrivat(final boolean privat) {
		this.privat = privat;
	}

}
