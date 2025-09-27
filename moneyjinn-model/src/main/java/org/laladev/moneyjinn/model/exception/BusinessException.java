package org.laladev.moneyjinn.model.exception;

import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.error.MoneyjinnException;

import java.io.Serial;

public class BusinessException extends MoneyjinnException {
    @Serial
    private static final long serialVersionUID = 1L;

    public BusinessException(final String errorMessage, final ErrorCode errorCode) {
        super(errorMessage, errorCode);
    }
}
