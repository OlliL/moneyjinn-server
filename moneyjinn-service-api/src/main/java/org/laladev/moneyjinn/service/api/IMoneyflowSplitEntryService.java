//
// Copyright (c) 2016-2017 Oliver Lehmann <oliver@laladev.org>
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

import java.util.List;
import java.util.Map;

import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowID;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowSplitEntry;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowSplitEntryID;
import org.laladev.moneyjinn.model.validation.ValidationResult;

/**
 * <p>
 * MoneyflowSplitEntryService is the Service handling everything around an
 * {@link MoneyflowSplitEntry}.
 * </p>
 *
 * <p>
 * MoneyflowSplitEntryService is the Service handling operations around an
 * {@link MoneyflowSplitEntry} like getting, creating, updating, deleting. Before a
 * {@link MoneyflowSplitEntry} is created or updated, the {@link MoneyflowSplitEntry} is validated
 * for correctness.
 * </p>
 * <p>
 * The main datasource is the Table <code>moneyflowsplitentries</code>.
 * </p>
 *
 * @author Oliver Lehmann
 * @since 0.0.1
 *
 */
public interface IMoneyflowSplitEntryService {
	/**
	 * Checks the validity of the given {@link MoneyflowSplitEntry}
	 *
	 * @param moneyflowSplitEntry
	 *            the {@link MoneyflowSplitEntry}
	 * @return {@link ValidationResult}
	 */
	public ValidationResult validateMoneyflowSplitEntry(MoneyflowSplitEntry moneyflowSplitEntry);

	/**
	 * This method persists (creates) the given {@link MoneyflowSplitEntry}s.
	 *
	 * @param moneyflowSplitEntrys
	 *            {@link MoneyflowSplitEntry}s
	 * @throws BusinessException
	 */

	public void createMoneyflowSplitEntries(UserID userId, List<MoneyflowSplitEntry> moneyflowSplitEntries);

	/**
	 * This service changes a {@link MoneyflowSplitEntry}. Before the {@link MoneyflowSplitEntry} is
	 * changed, the new values are validated for correctness.
	 *
	 * @param moneyflowSplitEntry
	 *            the new {@link MoneyflowSplitEntry} attributes
	 */
	public void updateMoneyflowSplitEntry(UserID userId, MoneyflowSplitEntry moneyflowSplitEntry);

	/**
	 * This service retrieves all {@link MoneyflowSplitEntry}s for a given {@link MoneyflowID} from
	 * the system
	 *
	 * @param userId
	 *            {@link UserID}
	 * @param moneyflowId
	 *            The {@link MoneyflowID} of the {@link MoneyflowSplitEntry}s
	 */
	public List<MoneyflowSplitEntry> getMoneyflowSplitEntries(UserID userId, MoneyflowID moneyflowId);

	/**
	 * This service retrieves all {@link MoneyflowSplitEntry}s for all given {@link MoneyflowID}s
	 * from the system
	 *
	 * @param userId
	 *            {@link UserID}
	 * @param moneyflowIds
	 *            The {@link MoneyflowID}s of the {@link MoneyflowSplitEntry}s
	 */
	public Map<MoneyflowID, List<MoneyflowSplitEntry>> getMoneyflowSplitEntries(UserID userId,
			List<MoneyflowID> moneyflowIds);

	/**
	 * This service deletes the {@link MoneyflowSplitEntry} for a given
	 * {@link MoneyflowSplitEntryID} from the system
	 *
	 * @param userId
	 *            {@link UserID}
	 * @param moneyflowId
	 *            The {@link MoneyflowID} of the to-be-deleted {@link MoneyflowSplitEntry}s
	 * @param moneyflowSplitEntryId
	 *            The {@link MoneyflowSplitEntryID} of the to-be-deleted
	 *            {@link MoneyflowSplitEntry}s
	 */
	public void deleteMoneyflowSplitEntry(UserID userId, MoneyflowID moneyflowId,
			MoneyflowSplitEntryID moneyflowSplitEntryId);

	/**
	 * This service deletes the {@link MoneyflowSplitEntry}s for a given {@link MoneyflowyID} from
	 * the system
	 *
	 * @param userId
	 *            {@link UserID}
	 * @param moneyflowId
	 *            The {@link MoneyflowID} of the to-be-deleted {@link MoneyflowSplitEntry}s
	 */
	public void deleteMoneyflowSplitEntries(UserID userId, MoneyflowID moneyflowId);
}
