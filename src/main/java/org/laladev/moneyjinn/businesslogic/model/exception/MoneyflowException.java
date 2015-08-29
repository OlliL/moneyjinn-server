package org.laladev.moneyjinn.businesslogic.model.exception;

import org.laladev.moneyjinn.businesslogic.model.ErrorCode;

public class MoneyflowException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public String errorMessage;
	public ErrorCode errorCode;

	public MoneyflowException(final String errorMessage, final ErrorCode errorCode) {
		super();
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
	}

	public final String getErrorMessage() {
		return this.errorMessage;
	}

	public final ErrorCode getErrorCode() {
		return this.errorCode;
	}

	@Override
	public String toString() {
		return this.errorMessage;
	}

}
