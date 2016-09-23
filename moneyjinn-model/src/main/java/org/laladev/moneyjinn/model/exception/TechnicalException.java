package org.laladev.moneyjinn.model.exception;

import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.error.MoneyjinnException;

public class TechnicalException extends MoneyjinnException {

	private static final long serialVersionUID = 1L;

	public TechnicalException(final String errorMessage, final ErrorCode errorCode) {
		super(errorMessage, errorCode);
	}

}
