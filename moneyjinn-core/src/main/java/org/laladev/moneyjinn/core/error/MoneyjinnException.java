package org.laladev.moneyjinn.core.error;

public class MoneyjinnException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private final String errorMessage;
	private final ErrorCode errorCode;

	public MoneyjinnException(final String errorMessage, final ErrorCode errorCode) {
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
