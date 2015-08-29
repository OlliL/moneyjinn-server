package org.laladev.moneyjinn.businesslogic.model.exception;

import org.laladev.moneyjinn.businesslogic.model.ErrorCode;

public class BusinessException extends MoneyflowException {

	private static final long serialVersionUID = 1L;

	public BusinessException(final String errorMessage, final ErrorCode errorCode) {
		super(errorMessage, errorCode);
	}

}
