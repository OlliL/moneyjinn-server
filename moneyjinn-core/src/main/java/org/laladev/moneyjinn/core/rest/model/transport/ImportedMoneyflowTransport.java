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

	public final String getExternalid() {
		return this.externalid;
	}

	public final void setExternalid(final String externalid) {
		this.externalid = externalid;
	}

	public final String getName() {
		return this.name;
	}

	public final void setName(final String name) {
		this.name = name;
	}

	public final String getUsage() {
		return this.usage;
	}

	public final void setUsage(final String usage) {
		this.usage = usage;
	}

	public final String getAccountNumberCapitalsource() {
		return this.accountNumberCapitalsource;
	}

	public final void setAccountNumberCapitalsource(final String accountNumberCapitalsource) {
		this.accountNumberCapitalsource = accountNumberCapitalsource;
	}

	public final String getBankCodeCapitalsource() {
		return this.bankCodeCapitalsource;
	}

	public final void setBankCodeCapitalsource(final String bankCodeCapitalsource) {
		this.bankCodeCapitalsource = bankCodeCapitalsource;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((this.accountNumber == null) ? 0 : this.accountNumber.hashCode());
		result = prime * result
				+ ((this.accountNumberCapitalsource == null) ? 0 : this.accountNumberCapitalsource.hashCode());
		result = prime * result + ((this.bankCode == null) ? 0 : this.bankCode.hashCode());
		result = prime * result + ((this.bankCodeCapitalsource == null) ? 0 : this.bankCodeCapitalsource.hashCode());
		result = prime * result + ((this.externalid == null) ? 0 : this.externalid.hashCode());
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
		final ImportedMoneyflowTransport other = (ImportedMoneyflowTransport) obj;
		if (this.accountNumber == null) {
			if (other.accountNumber != null) {
				return false;
			}
		} else if (!this.accountNumber.equals(other.accountNumber)) {
			return false;
		}
		if (this.accountNumberCapitalsource == null) {
			if (other.accountNumberCapitalsource != null) {
				return false;
			}
		} else if (!this.accountNumberCapitalsource.equals(other.accountNumberCapitalsource)) {
			return false;
		}
		if (this.bankCode == null) {
			if (other.bankCode != null) {
				return false;
			}
		} else if (!this.bankCode.equals(other.bankCode)) {
			return false;
		}
		if (this.bankCodeCapitalsource == null) {
			if (other.bankCodeCapitalsource != null) {
				return false;
			}
		} else if (!this.bankCodeCapitalsource.equals(other.bankCodeCapitalsource)) {
			return false;
		}
		if (this.externalid == null) {
			if (other.externalid != null) {
				return false;
			}
		} else if (!this.externalid.equals(other.externalid)) {
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
		builder.append("ImportedMoneyflowTransport [accountNumber=");
		builder.append(this.accountNumber);
		builder.append(", bankCode=");
		builder.append(this.bankCode);
		builder.append(", externalid=");
		builder.append(this.externalid);
		builder.append(", name=");
		builder.append(this.name);
		builder.append(", usage=");
		builder.append(this.usage);
		builder.append(", accountNumberCapitalsource=");
		builder.append(this.accountNumberCapitalsource);
		builder.append(", bankCodeCapitalsource=");
		builder.append(this.bankCodeCapitalsource);
		builder.append(", toString()=");
		builder.append(super.toString());
		builder.append("]");
		return builder.toString();
	}

}
