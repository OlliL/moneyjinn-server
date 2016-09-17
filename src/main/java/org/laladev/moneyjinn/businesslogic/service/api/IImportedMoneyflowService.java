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

package org.laladev.moneyjinn.businesslogic.service.api;

import java.time.LocalDate;
import java.util.List;

import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.businesslogic.model.moneyflow.ImportedMoneyflow;
import org.laladev.moneyjinn.businesslogic.model.moneyflow.ImportedMoneyflowID;
import org.laladev.moneyjinn.businesslogic.model.moneyflow.ImportedMoneyflowStatus;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResult;

/**
 * <p>
 * ImportedMoneyflowService is the Core Service handling everything around an
 * {@link ImportedMoneyflow}.
 * </p>
 *
 * <p>
 * ImportedMoneyflowService is the Domain Service handling operations around an
 * {@link ImportedMoneyflow} like getting, creating, updating, deleting. Before a
 * {@link ImportedMoneyflow} is created or updated, the {@link ImportedMoneyflow} is validated for
 * correctness.
 * </p>
 * <p>
 * The main datasource is the Table <code>impmoneyflows</code>.
 * </p>
 *
 * @author Oliver Lehmann
 * @since 0.0.1
 *
 */
public interface IImportedMoneyflowService {
	/**
	 * Validates the given {@link ImportedMoneyflow} for correctness.
	 *
	 * @param importedMoneyflow
	 * @return
	 */
	ValidationResult validateImportedMoneyflow(ImportedMoneyflow importedMoneyflow);

	/**
	 * Counts how much {@link ImportedMoneyflow} are there to be processed.
	 *
	 * @param userId
	 * @param capitalsourceIds
	 * @return
	 */
	Integer countImportedMoneyflows(UserID userId, List<CapitalsourceID> capitalsourceIds);

	/**
	 * Retrives all {@link ImportedMoneyflow}s to be processed by the user for the given
	 * {@link CapitalsourceID}s.
	 *
	 * @param userId
	 * @param capitalsourceIds
	 * @param dateFrom
	 * @param dateTil
	 * @return
	 */
	List<ImportedMoneyflow> getAllImportedMoneyflowsByCapitalsourceIds(UserID userId,
			List<CapitalsourceID> capitalsourceIds, LocalDate dateFrom, LocalDate dateTil);

	/**
	 * Retrives all {@link ImportedMoneyflow}s to be processed by the user for the given
	 * {@link CapitalsourceID}s and {@link ImportedMoneyflowStatus}.
	 *
	 * @param userId
	 * @param capitalsourceIds
	 * @param status
	 * @return
	 */
	List<ImportedMoneyflow> getAllImportedMoneyflowsByCapitalsourceIds(UserID userId,
			List<CapitalsourceID> capitalsourceIds, ImportedMoneyflowStatus status);

	/**
	 * Persists the given {@link ImportedMoneyflow}.
	 *
	 * @param importedMoneyflow
	 */
	void createImportedMoneyflow(ImportedMoneyflow importedMoneyflow);

	/**
	 * Sets a new {@link ImportedMoneyflowStatus} for the {@link ImportedMoneyflow} specified by its
	 * {@link ImportedMoneyflowID}.
	 *
	 * @param userId
	 * @param importedMoneyflowId
	 * @param status
	 */
	void updateImportedMoneyflowStatus(UserID userId, ImportedMoneyflowID importedMoneyflowId,
			ImportedMoneyflowStatus status);

	/**
	 * Deletes the {@link ImportedMoneyflow} specified by its {@link ImportedMoneyflowID}.
	 *
	 * @param userId
	 * @param importedMoneyflowId
	 */
	void deleteImportedMoneyflowById(UserID userId, ImportedMoneyflowID importedMoneyflowId);

}
