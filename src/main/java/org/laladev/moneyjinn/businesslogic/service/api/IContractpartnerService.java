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
import java.util.Set;

import org.laladev.moneyjinn.businesslogic.model.Contractpartner;
import org.laladev.moneyjinn.businesslogic.model.ContractpartnerID;
import org.laladev.moneyjinn.businesslogic.model.access.GroupID;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.exception.BusinessException;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResult;

/**
 * <p>
 * ContractpartnerService is the Core Service handling everything around an {@link Contractpartner}.
 * </p>
 *
 * <p>
 * ContractpartnerService is the Core Service handling operations around an {@link Contractpartner}
 * like getting, creating, updating, deleting. Before a {@link Contractpartner} is created or
 * updated, the {@link Contractpartner} is validated for correctness.
 * </p>
 * <p>
 * The main datasource is the Table <code>contractpartners</code>.
 * </p>
 *
 * @author Oliver Lehmann
 * @since 0.0.1
 *
 */
public interface IContractpartnerService {

	/**
	 * This method validates a given {@link Contractpartner} for correctness.
	 * <ul>
	 * <li>check that the given valid-from is before the given valid-til</li>
	 * <li>check that the name of the <code>Contractpartner</code> is not empty</li>
	 * <li>check that the specified name is unique and not already used by a different
	 * <code>Contractpartner</code></li>
	 * <li>check that when there are no recorded <code>Moneyflow</code>s outside the new validty
	 * period changing an existing <code>Contractpartner</code></li>
	 * </ul>
	 *
	 * @param contractpartner
	 *            the {@link Contractpartner} to validate
	 * @return ValidationResult
	 */
	public ValidationResult validateContractpartner(Contractpartner contractpartner);

	/**
	 * This method returns the {@link Contractpartner} specified by its Id
	 *
	 * @param userId
	 *            {@link UserID}
	 * @param contractpartnerId
	 *            {@link ContractpartnerID}
	 * @return {@link Contractpartner}
	 */
	public Contractpartner getContractpartnerById(UserID userId, ContractpartnerID contractpartnerId);

	/**
	 * This method returns all initials of all accessable {@link Contractpartner}s
	 *
	 * @param userId
	 *            {@link UserID}
	 * @return all uppercased initials
	 */
	public Set<Character> getAllContractpartnerInitials(UserID userId);

	/**
	 * This method returns all initials of all {@link Contractpartner}s valid between the given
	 * Dates.
	 *
	 * @param userId
	 *            {@link UserID}
	 * @param validFrom
	 *            {@link LocalDate}
	 * @param validTil
	 *            {@link LocalDate}
	 * @return all uppercased initials
	 */
	public Set<Character> getAllContractpartnerInitialsByDateRange(UserID userId, LocalDate validFrom,
			LocalDate validTil);

	/**
	 * This method counts all existing accessable {@link Contractpartner}s.
	 *
	 * @param userId
	 *            {@link UserID}
	 * @return number of {@link Contractpartner}s
	 */
	public Integer countAllContractpartners(UserID userId);

	/**
	 * This method counts all {@link Contractpartner}s valid between the given Dates.
	 *
	 * @param userId
	 *            {@link UserID}
	 * @param validFrom
	 *            {@link LocalDate}
	 * @param validTil
	 *            {@link LocalDate}
	 * @return number of {@link Contractpartner}s
	 */
	public Integer countAllContractpartnersByDateRange(UserID userId, LocalDate validFrom, LocalDate validTil);

	/**
	 * This method returns all {@link Contractpartner}s where the given user has reading
	 * permissions.
	 *
	 * @param userId
	 *            {@link UserID}
	 * @return a list of {@link Contractpartner}s
	 */
	public List<Contractpartner> getAllContractpartners(UserID userId);

	/**
	 * This method returns all {@link Contractpartner}s where the given user has reading permissions
	 * valid between the given Dates.
	 *
	 * @param userId
	 *            {@link UserID}
	 * @param validFrom
	 *            {@link LocalDate}
	 * @param validTil
	 *            {@link LocalDate}
	 * @return a list of {@link Contractpartner}s
	 */
	public List<Contractpartner> getAllContractpartnersByDateRange(UserID userId, LocalDate validFrom,
			LocalDate validTil);

	/**
	 * This method returns all {@link Contractpartner}s where the given <code>initial</code> is the
	 * first letter of the comment (case-insensitive).
	 *
	 * @param userId
	 *            {@link UserID}
	 * @param initial
	 *            the first letter of the {@link Contractpartner}s name
	 * @return a list of {@link Contractpartner}s
	 */
	public List<Contractpartner> getAllContractpartnersByInitial(UserID userId, Character initial);

	/**
	 * This method returns all {@link Contractpartner}s where the given <code>initial</code> is the
	 * first letter of the comment (case-insensitive) and which are valid between the given Dates.
	 *
	 * @param userId
	 *            {@link UserID}
	 * @param validFrom
	 *            {@link LocalDate}
	 * @param validTil
	 *            {@link LocalDate}
	 * @param initial
	 *            the first letter of the {@link Contractpartner}s name
	 * @return a list of {@link Contractpartner}s
	 */
	public List<Contractpartner> getAllContractpartnersByInitialAndDateRange(UserID userId, Character initial,
			LocalDate validFrom, LocalDate validTil);

	/**
	 * This method look the {@link Contractpartner} up which is valid on the given date and has the
	 * specified comment.
	 *
	 * @param userId
	 *            {@link UserID}
	 * @param name
	 * @return {@link Contractpartner}
	 */
	public Contractpartner getContractpartnerByName(UserID userId, String name);

	/**
	 * This method persists (updates) the given {@link Contractpartner}.
	 *
	 * @param contractpartner
	 *            {@link Contractpartner}
	 * @throws BusinessException
	 */
	public void updateContractpartner(Contractpartner contractpartner);

	/**
	 * This method persists (creates) the given {@link Contractpartner}.
	 *
	 * @param contractpartner
	 *            {@link Contractpartner}
	 * @throws BusinessException
	 */
	public void createContractpartner(Contractpartner contractpartner);

	/**
	 * This method deletes the {@link Contractpartner} specified by its Id.
	 *
	 * @param userId
	 *            {@link UserID}
	 * @param groupId
	 *            {@link GroupID}
	 * @param contractpartnerId
	 *            {@link ContractpartnerID}
	 * @throws BusinessException
	 */
	public void deleteContractpartner(UserID userId, GroupID groupId, ContractpartnerID contractpartnerId);

}