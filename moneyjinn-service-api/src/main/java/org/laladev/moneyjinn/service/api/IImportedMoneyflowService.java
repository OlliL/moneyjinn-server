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

import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflow;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowID;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowStatus;
import org.laladev.moneyjinn.model.validation.ValidationResult;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * ImportedMoneyflowService is the Core Service handling everything around an {@link ImportedMoneyflow}.
 * </p>
 *
 * <p>
 * ImportedMoneyflowService is the Domain Service handling operations around an {@link ImportedMoneyflow} like getting, creating, updating, deleting.
 * Before a {@link ImportedMoneyflow} is created or updated, the {@link ImportedMoneyflow} is validated for correctness.
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
	 * @param importedMoneyflow The {@link ImportedMoneyflow}
	 * @return The {@link ValidationResult}
	 */
	ValidationResult validateImportedMoneyflow(ImportedMoneyflow importedMoneyflow);

	/**
	 * Counts how much {@link ImportedMoneyflow} are there to be processed.
	 *
	 * @param userId The {@link UserID}
	 * @param capitalsourceIds List of all {@link CapitalsourceID} to be checked
	 * @param status {@link ImportedMoneyflowStatus} to be checked
	 * @return Number of matching {@link ImportedMoneyflow}
	 */
	Integer countImportedMoneyflows(UserID userId, List<CapitalsourceID> capitalsourceIds, ImportedMoneyflowStatus status);

	/**
	 * Retrives all {@link ImportedMoneyflow}s to be processed by the user for the given {@link CapitalsourceID}s.
	 *
	 * @param userId The {@link UserID}
	 * @param capitalsourceIds List of all {@link CapitalsourceID} to be checked
	 * @param dateFrom Begin of checking period
	 * @param dateTil End of checking period
	 * @return List of found {@link ImportedMoneyflow}
	 */
	List<ImportedMoneyflow> getAllImportedMoneyflowsByCapitalsourceIds(UserID userId, List<CapitalsourceID> capitalsourceIds, LocalDate dateFrom,
			LocalDate dateTil);

	/**
	 * Retrives all {@link ImportedMoneyflow}s to be processed by the user for the given {@link CapitalsourceID}s and {@link ImportedMoneyflowStatus}.
	 *
	 * @param userId The {@link UserID}
	 * @param capitalsourceIds List of all {@link CapitalsourceID} to be checked
	 * @param status {@link ImportedMoneyflowStatus} to be checked
	 * @return List of found {@link ImportedMoneyflow}
	 */
	List<ImportedMoneyflow> getAllImportedMoneyflowsByCapitalsourceIds(UserID userId, List<CapitalsourceID> capitalsourceIds, ImportedMoneyflowStatus status);

	/**
	 * Persists the given {@link ImportedMoneyflow}.
	 *
	 * @param importedMoneyflow The {@link ImportedMoneyflow}
	 */
	void createImportedMoneyflow(ImportedMoneyflow importedMoneyflow);

	/**
	 * Sets a new {@link ImportedMoneyflowStatus} for the {@link ImportedMoneyflow} specified by its {@link ImportedMoneyflowID}.
	 *
	 * @param userId The {@link UserID}
	 * @param importedMoneyflowId The {@link ImportedMoneyflow}
	 * @param status {@link ImportedMoneyflowStatus} to be checked
	 */
	void updateImportedMoneyflowStatus(UserID userId, ImportedMoneyflowID importedMoneyflowId, ImportedMoneyflowStatus status);

	/**
	 * Deletes the {@link ImportedMoneyflow} specified by its {@link ImportedMoneyflowID}.
	 *
	 * @param userId The {@link UserID}
	 * @param importedMoneyflowId The {@link ImportedMoneyflowID}
	 */
	void deleteImportedMoneyflowById(UserID userId, ImportedMoneyflowID importedMoneyflowId);

	/**
	 * checks if the externalId of a {@link ImportedMoneyflow} is already in the database.
	 * 
	 * @param externalId An ID from an external system referencing our {@link ImportedMoneyflow}
	 * @return true if externalid is already in the system, otherwise false
	 */
	boolean checkIfExternalIdAlreadyExists(String externalId);

}
