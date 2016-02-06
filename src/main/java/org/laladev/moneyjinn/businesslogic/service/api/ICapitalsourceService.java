//
// Copyright (c) 2015-2016 Oliver Lehmann <oliver@laladev.org>
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
import java.util.Set;

import org.laladev.moneyjinn.businesslogic.model.BankAccount;
import org.laladev.moneyjinn.businesslogic.model.access.GroupID;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.businesslogic.model.exception.BusinessException;
import org.laladev.moneyjinn.businesslogic.model.moneyflow.Moneyflow;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResult;

/**
 * <p>
 * CapitalsourceService is the Core Service handling everything around an {@link Capitalsource}.
 * </p>
 *
 * <p>
 * CapitalsourceService is the Core Service handling operations around an {@link Capitalsource} like
 * getting, creating, updating, deleting. Before a {@link Capitalsource} is created or updated, the
 * {@link Capitalsource} is validated for correctness.
 * </p>
 * <p>
 * The main datasource is the Table <code>capitalsources</code>.
 * </p>
 *
 * @author Oliver Lehmann
 * @since 0.0.1
 *
 */
public interface ICapitalsourceService {

	/**
	 * This method validates a given {@link Capitalsource} for correctness.
	 * <ul>
	 * <li>check that the given valid-from is before the given valid-til</li>
	 * <li>check that the specified comment is unique and not already used by a different
	 * <code>Capitalsource</code></li>
	 * <li>check that when there are no recorded <code>Moneyflow</code>s outside the new validty
	 * period changing an existing <code>Capitalsource</code></li>
	 * <li>check that the given account number is nummeric</li>
	 * <li>check that the given bank code is nummeric</li>
	 * <li>check that the comment of the <code>Capitalsource</code> is not empty</li>
	 * </ul>
	 *
	 * @param capitalsource
	 *            the {@link Capitalsource} to validate
	 * @return ValidationResult
	 */
	public ValidationResult validateCapitalsource(Capitalsource capitalsource);

	/**
	 * This method returns the {@link Capitalsource} specified by its Id
	 *
	 * @param userId
	 *            {@link UserID}
	 * @param groupId
	 *            {@link GroupID}
	 * @param capitalsourceId
	 *            {@link CapitalsourceID}
	 * @return {@link Capitalsource}
	 */
	public Capitalsource getCapitalsourceById(UserID userId, GroupID groupId, CapitalsourceID capitalsourceId);

	/**
	 * This method returns all initials of all accessable {@link Capitalsource}s
	 *
	 * @param userId
	 *            {@link UserID}
	 * @return all uppercased initials
	 */
	public Set<Character> getAllCapitalsourceInitials(UserID userId);

	/**
	 * This method returns all initials of all {@link Capitalsource}s valid between the given Dates.
	 *
	 * @param userId
	 *            {@link UserID}
	 * @param validFrom
	 *            {@link LocalDate}
	 * @param validTil
	 *            {@link LocalDate}
	 * @return all uppercased initials
	 */
	public Set<Character> getAllCapitalsourceInitialsByDateRange(UserID userId, LocalDate validFrom,
			LocalDate validTil);

	/**
	 * This method counts all existing accessable {@link Capitalsource}s.
	 *
	 * @param userId
	 *            {@link UserID}
	 * @return number of {@link Capitalsource}s
	 */
	public Integer countAllCapitalsources(UserID userId);

	/**
	 * This method counts all {@link Capitalsource}s valid between the given Dates.
	 *
	 * @param userId
	 *            {@link UserID}
	 * @param validFrom
	 *            {@link LocalDate}
	 * @param validTil
	 *            {@link LocalDate}
	 * @return number of {@link Capitalsource}s
	 */
	public Integer countAllCapitalsourcesByDateRange(UserID userId, LocalDate validFrom, LocalDate validTil);

	/**
	 * This method returns all {@link Capitalsource}s where the given user has reading permissions.
	 *
	 * @param userId
	 *            {@link UserID}
	 * @return a list of {@link Capitalsource}s
	 */
	public List<Capitalsource> getAllCapitalsources(UserID userId);

	/**
	 * This method returns all {@link Capitalsource}s where the given user has reading permissions
	 * valid between the given Dates.
	 *
	 * @param userId
	 *            {@link UserID}
	 * @param validFrom
	 *            {@link LocalDate}
	 * @param validTil
	 *            {@link LocalDate}
	 * @return a list of {@link Capitalsource}s
	 */
	public List<Capitalsource> getAllCapitalsourcesByDateRange(UserID userId, LocalDate validFrom, LocalDate validTil);

	/**
	 * This method returns all {@link Capitalsource}s where the given <code>initial</code> is the
	 * first letter of the comment (case-insensitive).
	 *
	 * @param userId
	 *            {@link UserID}
	 * @param initial
	 *            the first letter of the {@link Capitalsource}s name
	 * @return a list of {@link Capitalsource}s
	 */
	public List<Capitalsource> getAllCapitalsourcesByInitial(UserID userId, Character initial);

	/**
	 * This method returns all {@link Capitalsource}s where the given <code>initial</code> is the
	 * first letter of the comment (case-insensitive) and which are valid between the given Dates.
	 *
	 * @param userId
	 *            {@link UserID}
	 * @param validFrom
	 *            {@link LocalDate}
	 * @param validTil
	 *            {@link LocalDate}
	 * @param initial
	 *            the first letter of the {@link Capitalsource}s name
	 * @return a list of {@link Capitalsource}s
	 */
	public List<Capitalsource> getAllCapitalsourcesByInitialAndDateRange(UserID userId, Character initial,
			LocalDate validFrom, LocalDate validTil);

	/**
	 * This method look the {@link Capitalsource} up which is valid on the given date and has the
	 * specified comment.
	 *
	 * @param userId
	 *            {@link UserID}
	 * @param comment
	 * @param date
	 * @return {@link Capitalsource}
	 */
	public Capitalsource getCapitalsourceByComment(UserID userId, String name, LocalDate date);

	/**
	 * This method persists (updates) the given {@link Capitalsource}.
	 *
	 * @param capitalsource
	 *            updateCapitalsource
	 * @throws BusinessException
	 */
	public void updateCapitalsource(Capitalsource capitalsource);

	/**
	 * This method persists (creates) the given {@link Capitalsource}.
	 *
	 * @param capitalsource
	 *            updateCapitalsource
	 * @throws BusinessException
	 */
	public void createCapitalsource(Capitalsource capitalsource);

	/**
	 * This method deletes the {@link Capitalsource} specified by its Id.
	 *
	 * @param userId
	 *            {@link UserID}
	 * @param groupId
	 *            {@link GroupID}
	 * @param capitalsourceId
	 *            {@link CapitalsourceID}
	 * @throws BusinessException
	 */
	public void deleteCapitalsource(UserID userId, GroupID groupId, CapitalsourceID capitalsourceId);

	/**
	 * This method returns all {@link Capitalsource}s where the given {@link User} has usage for
	 * creating a {@link Moneyflow}. {@link Capitalsource}s for which no {@link Moneyflow} must be
	 * created, are filtered out.
	 *
	 * @param userId
	 *            {@link UserID}
	 * @return a list of {@link Capitalsource}s
	 */
	public List<Capitalsource> getGroupBookableCapitalsources(UserID userId);

	/**
	 * This method returns all {@link Capitalsource}s where the {@link User} has usage permissions
	 * like creating a {@link Moneyflow} and which valid between the given Dates.
	 *
	 * @param userId
	 *            {@link UserID}
	 * @param validFrom
	 *            {@link LocalDate}
	 * @param validTil
	 *            {@link LocalDate}
	 * @return a list of {@link Capitalsource}s
	 */
	public List<Capitalsource> getGroupCapitalsourcesByDateRange(UserID userId, LocalDate validFrom,
			LocalDate validTil);

	/**
	 * Determines the {@link Capitalsource} by the given {@link BankAccound}
	 *
	 * @param bankAccount
	 * @param endOfMonth
	 * @return
	 */
	public Capitalsource getCapitalsourceByAccount(UserID userId, BankAccount bankAccount, LocalDate endOfMonth);

	/**
	 * This method returns all {@link Capitalsource}s where the {@link User} has usage permissions
	 * for creating a {@link Moneyflow} and which valid between the given Dates.
	 * {@link Capitalsource}s for which no {@link Moneyflow} must be created, are filtered out.
	 *
	 * @param userId
	 * @param validFrom
	 * @param validTil
	 * @return
	 */
	public List<Capitalsource> getGroupBookableCapitalsourcesByDateRange(UserID userId, LocalDate validFrom,
			LocalDate validTil);

}