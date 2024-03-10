//
// Copyright (c) 2015-2024 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.service.dao.data.mapper;

import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.exception.TechnicalException;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowStatus;

public class ImportedMoneyflowStatusMapper {
	private static final int CREATED_INT = 0;
	private static final int PROCESSED_INT = 1;
	private static final int IGNORED_INT = 2;

	private ImportedMoneyflowStatusMapper() {
	}

	public static ImportedMoneyflowStatus map(final Integer type) {
		if (type != null) {
			return switch (type) {
			case CREATED_INT -> ImportedMoneyflowStatus.CREATED;
			case PROCESSED_INT -> ImportedMoneyflowStatus.PROCESSED;
			case IGNORED_INT -> ImportedMoneyflowStatus.IGNORED;
			default -> throw new TechnicalException("Type " + type + " not defined!", ErrorCode.UNKNOWN);
			};
		}
		return null;
	}

	public static Integer map(final ImportedMoneyflowStatus type) {
		if (type != null) {
			return switch (type) {
			case CREATED -> CREATED_INT;
			case PROCESSED -> PROCESSED_INT;
			case IGNORED -> IGNORED_INT;
			default -> throw new TechnicalException("Type " + type + " not defined!", ErrorCode.UNKNOWN);
			};
		}
		return null;
	}
}
