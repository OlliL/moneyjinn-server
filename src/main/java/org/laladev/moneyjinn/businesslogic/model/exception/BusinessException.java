package org.laladev.moneyjinn.businesslogic.model.exception;

import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.error.MoneyjinnException;

public class BusinessException extends MoneyjinnException {

	private static final long serialVersionUID = 1L;

	public BusinessException(final String errorMessage, final ErrorCode errorCode) {
		super(errorMessage, errorCode);
	}

}
