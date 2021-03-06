package org.laladev.moneyjinn.server.controller.impl;

//Copyright (c) 2015-2017 Oliver Lehmann <lehmann@ans-netz.de>
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without
//modification, are permitted provided that the following conditions
//are met:
//1. Redistributions of source code must retain the above copyright
//notice, this list of conditions and the following disclaimer
//2. Redistributions in binary form must reproduce the above copyright
//notice, this list of conditions and the following disclaimer in the
//documentation and/or other materials provided with the distribution.
//
//THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
//ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
//IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
//ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
//FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
//DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
//OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
//HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
//LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
//OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
//SUCH DAMAGE.

import javax.inject.Inject;

import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.mapper.AbstractMapperSupport;
import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.exception.TechnicalException;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.server.main.SessionEnvironment;

public abstract class AbstractController extends AbstractMapperSupport {
	@Inject
	private SessionEnvironment sessionEnvironment;

	protected UserID getUserId() {
		if (this.sessionEnvironment.getUserId() == null) {
			throw new TechnicalException("UserId must not be null!", ErrorCode.UNKNOWN);
		}
		return this.sessionEnvironment.getUserId();
	}

	protected ValidationResponse returnValidationResponse(final ValidationResult validationResult) {
		final ValidationResponse response = new ValidationResponse();
		response.setResult(validationResult.isValid());
		response.setValidationItemTransports(
				super.mapList(validationResult.getValidationResultItems(), ValidationItemTransport.class));
		return response;
	}

}
