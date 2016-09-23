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

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.monthlysettlement.MonthlySettlement;
import org.laladev.moneyjinn.model.validation.ValidationResult;

/**
 * <p>
 * MonthlySettlementService is the Core Service handling everything around an
 * {@link MonthlySettlement}.
 * </p>
 *
 * <p>
 * MonthlySettlementService is the Domain Service handling operations around an
 * {@link MonthlySettlement} like getting, creating, updating, deleting. Before a
 * {@link MonthlySettlement} is created or updated, the {@link MonthlySettlement} is validated for
 * correctness.
 * </p>
 * <p>
 * The main datasource is the Table <code>monthlysettlements</code>.
 * </p>
 *
 * @author Oliver Lehmann
 * @since 0.0.1
 *
 */
public interface IMonthlySettlementService {

	/**
	 * Returns a list of years in which {@link MonthlySettlement}s where created.
	 *
	 * @param userId
	 *            The {@link UserID}
	 * @return list of years
	 */
	public List<Short> getAllYears(UserID userId);

	/**
	 * Returns a list of {@link Month}s where {@link MonthlySettlement}s where created for in the
	 * given year.
	 *
	 * @param userId
	 *            The {@link UserID}
	 * @param year
	 * @return list of {@link Month}s
	 */
	public List<Month> getAllMonth(UserID userId, Short year);

	/**
	 * Returns a list of {@link MonthlySettlement}s for the given year and {@link Month}.
	 *
	 * @param userId
	 *            The {@link UserID}
	 * @param year
	 * @param month
	 *            The {@link Month}
	 * @return list of {@link MonthlySettlement}s
	 */
	public List<MonthlySettlement> getAllMonthlySettlementsByYearMonth(UserID userId, Short year, Month month);

	/**
	 * Returns the last date a {@link MonthlySettlement} was created by the given {@link UserId}.
	 *
	 * @param userId
	 * @return
	 */
	public LocalDate getMaxSettlementDate(UserID userId);

	/**
	 * Checks if at the given year and month, the also given {@link UserId} has already created a
	 * {@link MonthlySettlement}.
	 *
	 * @param userId
	 * @param year
	 * @param month
	 * @return
	 */
	public boolean checkMonthlySettlementsExists(UserID userId, Short year, Month month);

	/**
	 * This method inserts or updates the given {@link MonthlySettlement}s.
	 *
	 * @param monthlySettlements
	 * @return
	 */
	public ValidationResult upsertMonthlySettlements(List<MonthlySettlement> monthlySettlements);

	/**
	 * This method deletes the {@link MonthlySettlement}s for the given year and {@link Month}
	 *
	 * @param userId
	 * @param year
	 * @param month
	 */
	public void deleteMonthlySettlement(UserID userId, Short year, Month month);

	/**
	 * Returns all {@link MonthlySettlement}s between the given dates for all given
	 * {@link CapitalsourceID}s.
	 *
	 * @param userId
	 * @param begin
	 * @param end
	 * @param capitalsourceIds
	 * @return
	 */
	public List<MonthlySettlement> getAllMonthlySettlementsByRangeAndCapitalsource(UserID userId, LocalDate begin,
			LocalDate end, List<CapitalsourceID> capitalsourceIds);
}
