//
// Copyright (c) 2015 Oliver Lehmann <oliver@laladev.org>
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

import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceState;
import org.laladev.moneyjinn.businesslogic.model.exception.TechnicalException;
import org.laladev.moneyjinn.core.error.ErrorCode;

public class CapitalsourceStateMapper {
	private static final Short NON_CACHE_SHORT = Short.valueOf((short) 1);
	private static final Short CACHE_SHORT = Short.valueOf((short) 2);

	private CapitalsourceStateMapper() {

	}

	public static CapitalsourceState map(final Short state) {
		if (state != null) {
			switch (state) {
			case 1:
				return CapitalsourceState.NON_CACHE;
			case 2:
				return CapitalsourceState.CACHE;
			default:
				throw new TechnicalException("State " + state + " not defined!", ErrorCode.UNKNOWN);
			}
		}
		return null;
	}

	public static Short map(final CapitalsourceState state) {
		if (state != null) {
			switch (state) {
			case NON_CACHE:
				return NON_CACHE_SHORT;
			case CACHE:
				return CACHE_SHORT;
			default:
				throw new TechnicalException("State " + state + " not defined!", ErrorCode.UNKNOWN);
			}
		}
		return null;
	}
}