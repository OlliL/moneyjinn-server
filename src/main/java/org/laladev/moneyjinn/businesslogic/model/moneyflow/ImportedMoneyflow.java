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

package org.laladev.moneyjinn.businesslogic.model.moneyflow;

import org.laladev.moneyjinn.businesslogic.model.BankAccount;

public class ImportedMoneyflow extends AbstractMoneyflow<ImportedMoneyflowID> {
	private static final long serialVersionUID = 1L;
	private String externalId;
	private String name;
	private BankAccount bankAccount;
	private String usage;

	public final String getExternalId() {
		return this.externalId;
	}

	public final void setExternalId(final String externalId) {
		this.externalId = externalId;
	}

	public final String getName() {
		return this.name;
	}

	public final void setName(final String name) {
		this.name = name;
	}

	public final BankAccount getBankAccount() {
		return this.bankAccount;
	}

	public final void setBankAccount(final BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}

	public final String getUsage() {
		return this.usage;
	}

	public final void setUsage(final String usage) {
		this.usage = usage;
	}

	public Moneyflow getMoneyflow() {
		final Moneyflow moneyflow = new Moneyflow();
		moneyflow.setUser(super.getUser());
		moneyflow.setGroup(super.getGroup());
		moneyflow.setBookingDate(super.getBookingDate());
		moneyflow.setInvoiceDate(super.getInvoiceDate());
		moneyflow.setAmount(super.getAmount());
		moneyflow.setCapitalsource(super.getCapitalsource());
		moneyflow.setContractpartner(super.getContractpartner());
		moneyflow.setComment(super.getComment());
		moneyflow.setPrivat(super.isPrivat());
		moneyflow.setPostingAccount(super.getPostingAccount());
		return moneyflow;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.bankAccount == null) ? 0 : this.bankAccount.hashCode());
		result = prime * result + ((this.externalId == null) ? 0 : this.externalId.hashCode());
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result + ((this.usage == null) ? 0 : this.usage.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final ImportedMoneyflow other = (ImportedMoneyflow) obj;
		if (this.bankAccount == null) {
			if (other.bankAccount != null) {
				return false;
			}
		} else if (!this.bankAccount.equals(other.bankAccount)) {
			return false;
		}
		if (this.externalId == null) {
			if (other.externalId != null) {
				return false;
			}
		} else if (!this.externalId.equals(other.externalId)) {
			return false;
		}
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		if (this.usage == null) {
			if (other.usage != null) {
				return false;
			}
		} else if (!this.usage.equals(other.usage)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ImportedMoneyflow [externalId=");
		builder.append(this.externalId);
		builder.append(", name=");
		builder.append(this.name);
		builder.append(", bankAccount=");
		builder.append(this.bankAccount);
		builder.append(", usage=");
		builder.append(this.usage);
		builder.append(", toString()=");
		builder.append(super.toString());
		builder.append("]");
		return builder.toString();
	}

}
