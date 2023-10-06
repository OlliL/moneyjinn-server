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

package org.laladev.moneyjinn.server.controller.mapper;

import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceType;
import org.laladev.moneyjinn.model.exception.TechnicalException;

public class CapitalsourceTypeMapper {
	private static final int CURRENT_ASSET_INT = 1;
	private static final int LONG_TERM_ASSET_INT = 2;
	private static final int RESERVE_ASSET_INT = 3;
	private static final int PROVISION_ASSET_INT = 4;
	private static final int CREDIT_INT = 5;

	private CapitalsourceTypeMapper() {
	}

	public static CapitalsourceType map(final Integer type) {
		if (type != null) {
			return switch (type) {
			case CURRENT_ASSET_INT -> CapitalsourceType.CURRENT_ASSET;
			case LONG_TERM_ASSET_INT -> CapitalsourceType.LONG_TERM_ASSET;
			case RESERVE_ASSET_INT -> CapitalsourceType.RESERVE_ASSET;
			case PROVISION_ASSET_INT -> CapitalsourceType.PROVISION_ASSET;
			case CREDIT_INT -> CapitalsourceType.CREDIT;
			default -> throw new TechnicalException("Type " + type + " not defined!", ErrorCode.UNKNOWN);
			};
		}
		return null;
	}

	public static Integer map(final CapitalsourceType type) {
		if (type != null) {
			return switch (type) {
			case CURRENT_ASSET -> CURRENT_ASSET_INT;
			case LONG_TERM_ASSET -> LONG_TERM_ASSET_INT;
			case RESERVE_ASSET -> RESERVE_ASSET_INT;
			case PROVISION_ASSET -> PROVISION_ASSET_INT;
			case CREDIT -> CREDIT_INT;
			default -> throw new TechnicalException("Type " + type + " not defined!", ErrorCode.UNKNOWN);
			};
		}
		return null;
	}
}