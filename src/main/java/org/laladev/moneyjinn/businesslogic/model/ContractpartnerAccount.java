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

package org.laladev.moneyjinn.businesslogic.model;

public class ContractpartnerAccount extends AbstractEntity<ContractpartnerAccountID> {
	private static final long serialVersionUID = 1L;
	private Contractpartner contractpartner;
	private BankAccount bankAccount;

	public ContractpartnerAccount() {
	}

	public ContractpartnerAccount(final ContractpartnerAccountID contractpartnerAccountId,
			final Contractpartner contractpartner, final BankAccount bankAccount) {
		super();
		super.setId(contractpartnerAccountId);
		this.contractpartner = contractpartner;
		this.bankAccount = bankAccount;
	}

	public final Contractpartner getContractpartner() {
		return this.contractpartner;
	}

	public final void setContractpartner(final Contractpartner contractpartner) {
		this.contractpartner = contractpartner;
	}

	public final BankAccount getBankAccount() {
		return this.bankAccount;
	}

	public final void setBankAccount(final BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.bankAccount == null) ? 0 : this.bankAccount.hashCode());
		result = prime * result + ((this.contractpartner == null) ? 0 : this.contractpartner.hashCode());
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
		final ContractpartnerAccount other = (ContractpartnerAccount) obj;
		if (this.bankAccount == null) {
			if (other.bankAccount != null) {
				return false;
			}
		} else if (!this.bankAccount.equals(other.bankAccount)) {
			return false;
		}
		if (this.contractpartner == null) {
			if (other.contractpartner != null) {
				return false;
			}
		} else if (!this.contractpartner.equals(other.contractpartner)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ContractpartnerAccount [contractpartner=");
		builder.append(this.contractpartner);
		builder.append(", bankAccount=");
		builder.append(this.bankAccount);
		builder.append(", getId()=");
		builder.append(this.getId());
		builder.append("]");
		return builder.toString();
	}

}
