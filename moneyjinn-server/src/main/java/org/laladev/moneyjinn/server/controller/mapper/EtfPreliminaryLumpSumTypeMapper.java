//
// Copyright (c) 2024-2025 Oliver Lehmann <lehmann@ans-netz.de>
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
import org.laladev.moneyjinn.model.etf.EtfPreliminaryLumpSumType;
import org.laladev.moneyjinn.model.exception.TechnicalException;

public class EtfPreliminaryLumpSumTypeMapper {
	private static final int AMOUNT_PER_MONTH = 1;
	private static final int AMOUNT_PER_PIECE = 2;

	private EtfPreliminaryLumpSumTypeMapper() {
	}

	public static EtfPreliminaryLumpSumType map(final Integer type) {
		if (type != null) {
			return switch (type) {
			case AMOUNT_PER_MONTH -> EtfPreliminaryLumpSumType.AMOUNT_PER_MONTH;
			case AMOUNT_PER_PIECE -> EtfPreliminaryLumpSumType.AMOUNT_PER_PIECE;
			default -> throw new TechnicalException("Type " + type + " not defined!", ErrorCode.UNKNOWN);
			};
		}
		return null;
	}

	public static Integer map(final EtfPreliminaryLumpSumType type) {
		if (type != null) {
			return switch (type) {
			case AMOUNT_PER_MONTH -> AMOUNT_PER_MONTH;
			case AMOUNT_PER_PIECE -> AMOUNT_PER_PIECE;
			default -> throw new TechnicalException("Type " + type + " not defined!", ErrorCode.UNKNOWN);
			};
		}
		return null;
	}
}