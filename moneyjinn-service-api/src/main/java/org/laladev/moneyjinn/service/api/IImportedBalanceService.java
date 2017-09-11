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

package org.laladev.moneyjinn.service.api;

import org.laladev.moneyjinn.model.ImportedBalance;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.validation.ValidationResult;

import java.util.List;

/**
 * <p>
 * ImportedBalanceService is the Core Service handling everything around an {@link ImportedBalance}.
 * </p>
 *
 * <p>
 * ImportedBalanceService is the Domain Service handling operations around an
 * {@link ImportedBalance} like getting, creating, updating, deleting. Before a
 * {@link ImportedBalance} is created or updated, the {@link ImportedBalance} is validated for
 * correctness.
 * </p>
 * <p>
 * The main datasource is the Table <code>impbalance</code>.
 * </p>
 *
 * @author Oliver Lehmann
 * @since 0.0.1
 *
 */
public interface IImportedBalanceService {

	/**
	 * Validates the given {@link ImportedBalance} for correctness.
	 *
	 * @param importedBalance The {@link ImportedBalance}
	 * @return The {@link ValidationResult}
	 */
	ValidationResult validateImportedBalance(ImportedBalance importedBalance);

	/**
	 * Persists the given {@link ImportedBalance}.
	 *
	 * @param importedBalance The {@link ImportedBalance}
	 */
	void upsertImportedBalance(ImportedBalance importedBalance);

	/**
	 * Retrives all {@link ImportedBalance}s for the given {@link CapitalsourceID}s
	 *
	 * @param userId The {@link UserID}
	 * @param capitalsourceIds The {@link ImportedBalance}
	 * @return List of all matching {@link ImportedBalance}
	 */
	List<ImportedBalance> getAllImportedBalancesByCapitalsourceIds(UserID userId,
			List<CapitalsourceID> capitalsourceIds);

}
