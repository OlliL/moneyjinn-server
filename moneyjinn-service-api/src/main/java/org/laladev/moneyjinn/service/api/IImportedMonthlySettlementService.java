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

package org.laladev.moneyjinn.service.api;

import java.time.Month;
import java.util.List;

import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.monthlysettlement.ImportedMonthlySettlement;
import org.laladev.moneyjinn.model.validation.ValidationResult;

/**
 * <p>
 * ImportedMonthlySettlementService is the Service handling everything around an
 * {@link ImportedMonthlySettlement}.
 * </p>
 *
 * <p>
 * ImportedMonthlySettlementService is the Domain Service handling operations
 * around an {@link ImportedMonthlySettlement} like getting, creating, updating,
 * deleting. Before a {@link ImportedMonthlySettlement} is created or updated,
 * the {@link ImportedMonthlySettlement} is validated for correctness.
 * </p>
 * <p>
 * The main datasource is the Table <code>impmonthlysettlements</code>.
 * </p>
 *
 * @author Oliver Lehmann
 * @since 0.0.1
 *
 */
public interface IImportedMonthlySettlementService {
	/**
	 * Retrieves all the imported monthly settlements for the given year and month.
	 *
	 * @param userId The {@link UserID}
	 * @param year   The year of the Settlement
	 * @param month  The month of the Settlement
	 * @return All matching {@link ImportedMonthlySettlement}
	 */
	List<ImportedMonthlySettlement> getImportedMonthlySettlementsByMonth(UserID userId, Integer year, Month month);

	/**
	 * Persists the given {@link ImportedMonthlySettlement}.
	 *
	 * @param importedMonthlySettlement The {@link ImportedMonthlySettlement}
	 */
	void upsertImportedMonthlySettlement(ImportedMonthlySettlement importedMonthlySettlement);

	/**
	 * Validates the given {@link ImportedMonthlySettlement} for correctness.
	 *
	 * @param importedMonthlySettlement The {@link ImportedMonthlySettlement}
	 * @return The {@link ValidationResult}
	 */
	ValidationResult validateImportedMonthlySettlement(ImportedMonthlySettlement importedMonthlySettlement);
}
