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
//

package org.laladev.moneyjinn.core.rest.model.transport;

public class ContractpartnerAccountTransport {
	private Long id;
	private Long contractpartnerid;
	private String accountNumber;
	private String bankCode;

	public ContractpartnerAccountTransport() {

	}

	public ContractpartnerAccountTransport(final Long id, final Long contractpartnerid, final String accountNumber,
			final String bankCode) {
		super();
		this.id = id;
		this.contractpartnerid = contractpartnerid;
		this.accountNumber = accountNumber;
		this.bankCode = bankCode;
	}

	public final Long getId() {
		return this.id;
	}

	public final void setId(final Long id) {
		this.id = id;
	}

	public final Long getContractpartnerid() {
		return this.contractpartnerid;
	}

	public final void setContractpartnerid(final Long contractpartnerid) {
		this.contractpartnerid = contractpartnerid;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.accountNumber == null) ? 0 : this.accountNumber.hashCode());
		result = prime * result + ((this.bankCode == null) ? 0 : this.bankCode.hashCode());
		result = prime * result + ((this.contractpartnerid == null) ? 0 : this.contractpartnerid.hashCode());
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final ContractpartnerAccountTransport other = (ContractpartnerAccountTransport) obj;
		if (this.accountNumber == null) {
			if (other.accountNumber != null) {
				return false;
			}
		} else if (!this.accountNumber.equals(other.accountNumber)) {
			return false;
		}
		if (this.bankCode == null) {
			if (other.bankCode != null) {
				return false;
			}
		} else if (!this.bankCode.equals(other.bankCode)) {
			return false;
		}
		if (this.contractpartnerid == null) {
			if (other.contractpartnerid != null) {
				return false;
			}
		} else if (!this.contractpartnerid.equals(other.contractpartnerid)) {
			return false;
		}
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ContractpartnerAccountTransport [id=");
		builder.append(this.id);
		builder.append(", contractpartnerid=");
		builder.append(this.contractpartnerid);
		builder.append(", accountNumber=");
		builder.append(this.accountNumber);
		builder.append(", bankCode=");
		builder.append(this.bankCode);
		builder.append("]");
		return builder.toString();
	}

}
