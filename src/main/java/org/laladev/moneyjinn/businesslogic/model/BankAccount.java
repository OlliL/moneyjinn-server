package org.laladev.moneyjinn.businesslogic.model;

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
		this.bankCode = bankCode;
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
}
