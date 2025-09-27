//Copyright (c) 2015-2025 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.server.controller.impl;

import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.exception.TechnicalException;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.server.exception.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class AbstractController {
    protected static final String HEADER_PREFER = "Prefer";
    protected static final String HAS_AUTHORITY_ADMIN = "hasAuthority('ADMIN')";
    private static final String HEADER_PREFERENCE_APPLIED = "Preference-Applied";
    private static final String RETURN = "return=";
    private static final String RETURN_MINIMAL = RETURN + "minimal";
    private static final String RETURN_REPRESENTATION = RETURN + "representation";

    protected UserID getUserId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final Object authenticationDetails = authentication != null ? authentication.getDetails() : null;
        if (authenticationDetails instanceof final Long userId) {
            return new UserID(userId);
        }

        throw new TechnicalException("UserId must not be null!", ErrorCode.UNKNOWN);
    }

    protected void throwValidationExceptionIfInvalid(final ValidationResult validationResult) {
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult);
        }
    }

    protected <T> ResponseEntity<T> preferedReturn(final List<String> prefer, final Supplier<T> transportSupplier) {

        final String returnHeaderValue = Optional.ofNullable(prefer).orElse(Collections.emptyList()).stream()
                .map(String::toLowerCase).filter(p -> p.startsWith(RETURN)).findFirst().orElse("");

        return switch (returnHeaderValue) {
            case RETURN_REPRESENTATION -> ResponseEntity.ok().header(HEADER_PREFERENCE_APPLIED, RETURN_REPRESENTATION)
                    .body(transportSupplier.get());
            case RETURN_MINIMAL -> ResponseEntity.noContent().header(HEADER_PREFERENCE_APPLIED, RETURN_MINIMAL).build();
            default -> ResponseEntity.noContent().build();
        };
    }
}
