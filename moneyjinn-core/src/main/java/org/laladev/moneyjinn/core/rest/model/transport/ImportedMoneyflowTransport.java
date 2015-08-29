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
// $Id: ImportedMoneyflowTransport.java,v 1.1 2015/02/08 00:26:04 olivleh1 Exp $
//
package org.laladev.moneyjinn.core.rest.model.transport;

public class ImportedMoneyflowTransport extends MoneyflowTransport {
	private String accountNumber;
	private String bankCode;
	private String externalid;
	private String name;
	private String usage;
	private String accountNumberCapitalsource;
	private String bankCodeCapitalsource;

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

	public final String getExternalid() {
		return externalid;
	}

	public final void setExternalid(final String externalid) {
		this.externalid = externalid;
	}

	public final String getName() {
		return name;
	}

	public final void setName(final String name) {
		this.name = name;
	}

	public final String getUsage() {
		return usage;
	}

	public final void setUsage(final String usage) {
		this.usage = usage;
	}

	public final String getAccountNumberCapitalsource() {
		return accountNumberCapitalsource;
	}

	public final void setAccountNumberCapitalsource(final String accountNumberCapitalsource) {
		this.accountNumberCapitalsource = accountNumberCapitalsource;
	}

	public final String getBankCodeCapitalsource() {
		return bankCodeCapitalsource;
	}

	public final void setBankCodeCapitalsource(final String bankCodeCapitalsource) {
		this.bankCodeCapitalsource = bankCodeCapitalsource;
	}

}
