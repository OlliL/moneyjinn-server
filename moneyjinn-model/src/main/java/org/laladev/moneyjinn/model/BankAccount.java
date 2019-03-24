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

package org.laladev.moneyjinn.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.laladev.moneyjinn.core.error.ErrorCode;

public class BankAccount implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final Short ACCOUNT_NUMBER_MAX_LENGTH = 34;
	private static final Short BANK_CODE_MAX_LENGTH = 11;
	private String accountNumber;
	private String bankCode;
	private final Pattern p = Pattern.compile("[^a-zA-Z0-9]");

	public BankAccount(final String accountNumber, final String bankCode) {
		super();
		this.accountNumber = accountNumber;
		this.setBankCode(bankCode);
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
		// Always fill 8 digits BIC to 11 digits BIC!
		if (bankCode != null && bankCode.length() == 8 && !bankCode.matches("^\\d+$")) {
			this.bankCode = bankCode + "XXX";
		} else {
			this.bankCode = bankCode;
		}
	}

	public List<ErrorCode> checkValidity() {
		final List<ErrorCode> errorCodes = new ArrayList<>();
		if (this.accountNumber != null) {
			if (this.accountNumber.length() > BankAccount.ACCOUNT_NUMBER_MAX_LENGTH) {
				errorCodes.add(ErrorCode.ACCOUNT_NUMBER_TO_LONG);
			}
			if (this.p.matcher(this.accountNumber).find()) {
				errorCodes.add(ErrorCode.ACCOUNT_NUMBER_CONTAINS_ILLEGAL_CHARS);
			}
		}
		if (this.bankCode != null) {
			if (this.bankCode.length() > BankAccount.BANK_CODE_MAX_LENGTH) {
				errorCodes.add(ErrorCode.BANK_CODE_TO_LONG);
			}
			if (this.p.matcher(this.bankCode).find()) {
				errorCodes.add(ErrorCode.BANK_CODE_CONTAINS_ILLEGAL_CHARS);
			}
		}
		return errorCodes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.accountNumber == null) ? 0 : this.accountNumber.hashCode());
		result = prime * result + ((this.bankCode == null) ? 0 : this.bankCode.hashCode());
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
		final BankAccount other = (BankAccount) obj;
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
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("BankAccount [accountNumber=");
		builder.append(this.accountNumber);
		builder.append(", bankCode=");
		builder.append(this.bankCode);
		builder.append("]");
		return builder.toString();
	}

}
