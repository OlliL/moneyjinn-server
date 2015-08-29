package org.laladev.moneyjinn.businesslogic.model.exception;

import org.laladev.moneyjinn.businesslogic.model.ErrorCode;

public class TechnicalException extends MoneyflowException {

	private static final long serialVersionUID = 1L;

	public TechnicalException(final String errorMessage, final ErrorCode errorCode) {
		super(errorMessage, errorCode);
	}

}
