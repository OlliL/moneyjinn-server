//
// Copyright (c) 2015 Oliver Lehmann <lehmann@ans-netz.de>
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

import org.laladev.moneyjinn.model.BankAccount;
import org.laladev.moneyjinn.model.ContractpartnerAccount;
import org.laladev.moneyjinn.model.ContractpartnerAccountID;
import org.laladev.moneyjinn.model.ContractpartnerID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.validation.ValidationResult;

/**
 * <p>
 * ContractpartnerAccountService is the Core Service handling everything around
 * an {@link ContractpartnerAccount}.
 * </p>
 *
 * <p>
 * ContractpartnerAccountService is the Domain Service handling operations
 * around an {@link ContractpartnerAccount} like getting, creating, updating,
 * deleting. Before a {@link ContractpartnerAccount} is created or updated, the
 * {@link ContractpartnerAccount} is validated for correctness.
 * </p>
 * <p>
 * The main datasource is the Table <code>contractpartneraccounts</code>.
 * </p>
 *
 * @author Oliver Lehmann
 * @since 0.0.1
 *
 */
public interface IContractpartnerAccountService {
	/**
	 * This method validates a given {@link ContractpartnerAccount} for correctness.
	 *
	 * @param userId                 {@link UserID}
	 * @param contractpartnerAccount {@link ContractpartnerAccount}
	 * @return {@link ValidationResult}
	 */
	ValidationResult validateContractpartnerAccount(UserID userId, ContractpartnerAccount contractpartnerAccount);

	/**
	 * This method returns the {@link ContractpartnerAccount} specified by its Id
	 *
	 * @param userId                   {@link UserID}
	 * @param contractpartnerAccountId {@link ContractpartnerAccountID}
	 * @return {@link ContractpartnerAccount}
	 */
	ContractpartnerAccount getContractpartnerAccountById(UserID userId,
			ContractpartnerAccountID contractpartnerAccountId);

	/**
	 * This method returns the {@link ContractpartnerAccount}s assigned to a given
	 * {@link ContractpartnerID}
	 *
	 * @param userId            {@link UserID}
	 * @param contractpartnerId {@link ContractpartnerID}
	 * @return list of {@link ContractpartnerAccount}s
	 */
	List<ContractpartnerAccount> getContractpartnerAccounts(UserID userId, ContractpartnerID contractpartnerId);

	/**
	 * This method persists (creates) the given {@link ContractpartnerAccount}.
	 *
	 * @param userId                 {@link UserID}
	 * @param contractpartnerAccount {@link ContractpartnerAccount}
	 */
	ContractpartnerAccountID createContractpartnerAccount(UserID userId, ContractpartnerAccount contractpartnerAccount);

	/**
	 * This method persists (updates) the given {@link ContractpartnerAccount}.
	 *
	 * @param userId                 {@link UserID}
	 * @param contractpartnerAccount {@link ContractpartnerAccount}
	 */
	void updateContractpartnerAccount(UserID userId, ContractpartnerAccount contractpartnerAccount);

	/**
	 * This method deletes the given {@link ContractpartnerAccountID}.
	 *
	 * @param userId                   {@link UserID}
	 * @param contractpartnerAccountId {@link ContractpartnerAccountID}
	 */
	void deleteContractpartnerAccountById(UserID userId, ContractpartnerAccountID contractpartnerAccountId);

	/**
	 * This method deletes the {@link ContractpartnerAccount}s assigned to a given
	 * {@link ContractpartnerID}.
	 *
	 * @param userId            {@link UserID}
	 * @param contractpartnerId {@link ContractpartnerID}
	 */
	void deleteContractpartnerAccounts(UserID userId, ContractpartnerID contractpartnerId);

	/**
	 * This method selects all {@link ContractpartnerAccount}s for the given
	 * {@link BankAccount}s
	 *
	 * @param userId       {@link UserID}
	 * @param bankAccounts List of {@link BankAccount}
	 * @return List of all found {@link ContractpartnerAccount}
	 */
	List<ContractpartnerAccount> getAllContractpartnerByAccounts(UserID userId, List<BankAccount> bankAccounts);
}
