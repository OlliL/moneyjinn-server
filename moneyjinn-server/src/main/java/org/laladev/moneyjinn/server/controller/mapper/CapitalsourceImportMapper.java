//
// Copyright (c) 2015-2025 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.server.controller.mapper;

import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceImport;
import org.laladev.moneyjinn.model.exception.TechnicalException;

public class CapitalsourceImportMapper {
    private static final int NOT_ALLOWED_INT = 0;
    private static final int ALL_ALLOWED_INT = 1;
    private static final int BALANCE_ALLOWED_INT = 2;

    private CapitalsourceImportMapper() {
    }

    public static CapitalsourceImport map(final Integer capitalsourceImport) {
        if (capitalsourceImport != null) {
            return switch (capitalsourceImport) {
                case NOT_ALLOWED_INT -> CapitalsourceImport.NOT_ALLOWED;
                case ALL_ALLOWED_INT -> CapitalsourceImport.ALL_ALLOWED;
                case BALANCE_ALLOWED_INT -> CapitalsourceImport.BALANCE_ALLOWED;
                default -> throw new TechnicalException("Import " + capitalsourceImport + " not defined!",
                        ErrorCode.UNKNOWN);
            };
        }
        return CapitalsourceImport.NOT_ALLOWED;
    }

    public static Integer map(final CapitalsourceImport capitalsourceImport) {
        if (capitalsourceImport != null) {
            return switch (capitalsourceImport) {
                case NOT_ALLOWED -> null;
                case ALL_ALLOWED -> ALL_ALLOWED_INT;
                case BALANCE_ALLOWED -> BALANCE_ALLOWED_INT;
            };
        }
        return null;
    }
}