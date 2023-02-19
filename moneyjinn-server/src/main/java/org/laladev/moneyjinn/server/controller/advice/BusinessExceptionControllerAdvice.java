//
// Copyright (c) 2015-2023 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.server.controller.advice;

import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.server.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class BusinessExceptionControllerAdvice extends ResponseEntityExceptionHandler {

  private static Logger LOGGER = LoggerFactory.getLogger(BusinessExceptionControllerAdvice.class);

  @ExceptionHandler(BusinessException.class)
  @ResponseBody
  ResponseEntity<Object> handleControllerException(final BusinessException ex) {
    final ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setCode(ex.getErrorCode().getErrorCode());
    errorResponse.setMessage(ex.getErrorMessage());
    HttpStatus httpStatus;
    switch (ex.getErrorCode()) {
      case USERNAME_PASSWORD_WRONG:
      case ACCOUNT_IS_LOCKED:
        httpStatus = HttpStatus.FORBIDDEN;
        break;
      default:
        httpStatus = HttpStatus.BAD_REQUEST;
    }

    LOGGER.error(ex.getErrorMessage(), ex);
    return new ResponseEntity<>(errorResponse, httpStatus);
  }
}
