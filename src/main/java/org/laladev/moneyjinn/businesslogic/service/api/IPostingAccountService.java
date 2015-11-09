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

import java.util.List;
import java.util.Set;

import org.laladev.moneyjinn.businesslogic.model.PostingAccount;
import org.laladev.moneyjinn.businesslogic.model.PostingAccountID;
import org.laladev.moneyjinn.businesslogic.model.exception.BusinessException;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResult;

/**
 * <p>
 * PostingAccountService is the Core Service handling everything around an {@link PostingAccount}.
 * </p>
 *
 * <p>
 * PostingAccountService is the Core Service handling operations around an {@link PostingAccount}
 * like getting, creating, updating, deleting. Before a {@link PostingAccount} is created or
 * updated, the {@link PostingAccount} is validated for correctness.
 * </p>
 * <p>
 * The main datasource is the Table <code>postingaccounts</code>.
 * </p>
 *
 * @author Oliver Lehmann
 * @since 0.0.1
 *
 */
public interface IPostingAccountService {
	/**
	 * Checks the validity of the given {@link PostingAccount}
	 *
	 * @param postingAccount
	 *            the {@link PostingAccount}
	 * @return {@link ValidationResult}
	 */
	public ValidationResult validatePostingAccount(PostingAccount postingAccount);

	/**
	 * Returns the {@link PostingAccount} for the given {@link PostingAccountID}.
	 *
	 * @param postingAccountId
	 *            the {@link PostingAccountID}
	 * @return
	 */
	public PostingAccount getPostingAccountById(PostingAccountID postingAccountId);

	/**
	 * This Service returns the distinct initials of all postingAccountnames in the database in
	 * uppercase.
	 *
	 * @return all uppercased initials
	 */
	public Set<Character> getAllPostingAccountInitials();

	/**
	 * This Service returns the number of all existing postingAccounts
	 *
	 * @return number of {@link PostingAccount}s
	 */
	public Integer countAllPostingAccounts();

	/**
	 * This Service returns all existing {@link PostingAccount}s
	 *
	 * @return list of all {@link PostingAccount}s
	 */
	public List<PostingAccount> getAllPostingAccounts();

	/**
	 * This Service returns all existing {@link PostingAccount}s which name start with the specified
	 * initial case-insensitive
	 *
	 * @param initial
	 *            the first letter of the {@link PostingAccount}s name
	 * @return a list of {@link PostingAccount}s
	 */
	public List<PostingAccount> getAllPostingAccountsByInitial(Character initial);

	/**
	 * This Service returns the {@link PostingAccount} for the specified name
	 *
	 * @param name
	 *            the PostingAccount-Name
	 * @return {@link PostingAccount}
	 */
	public PostingAccount getPostingAccountByName(String name);

	/**
	 * This service creates a {@link PostingAccount}. Before the {@link PostingAccount} is created
	 * it is validated for correctness.
	 *
	 * @param postingAccount
	 *            the {@link PostingAccount} to be created
	 * @throws BusinessException
	 *             If the validation of the given {@link PostingAccount} failed.
	 */
	public void createPostingAccount(PostingAccount postingAccount);

	/**
	 * This service changes a {@link PostingAccount}. Before the {@link PostingAccount} is changed,
	 * the new values are validated for correctness.
	 *
	 * @param postingAccount
	 *            the new {@link PostingAccount} attributes
	 * @throws BusinessException
	 *             If the validation of the given {@link PostingAccount} failed.
	 */
	public void updatePostingAccount(PostingAccount postingAccount);

	/**
	 * This service deletes a {@link PostingAccount} from the system
	 *
	 * @param postingAccountId
	 *            The {@link PostingAccountID} of the to-be-deleted {@link PostingAccount}
	 * @throws BusinessException
	 *             If the deletion fails an error is throws. It is always assumed that it fails
	 *             because of a Foreign Key Constraint Violation on the DB level
	 */
	public void deletePostingAccount(PostingAccountID postingAccountId);

}
