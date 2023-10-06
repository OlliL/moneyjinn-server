//
// Copyright (c) 2015-2023 Oliver Lehmann <lehmann@ans-netz.de>
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

import org.laladev.moneyjinn.model.Contractpartner;
import org.laladev.moneyjinn.model.ContractpartnerID;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.model.validation.ValidationResult;

/**
 * <p>
 * ContractpartnerService is the Core Service handling everything around an
 * {@link Contractpartner}.
 * </p>
 *
 * <p>
 * ContractpartnerService is the Core Service handling operations around an
 * {@link Contractpartner} like getting, creating, updating, deleting. Before a
 * {@link Contractpartner} is created or updated, the {@link Contractpartner} is
 * validated for correctness.
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
	 * <li>check that the specified name is unique and not already used by a
	 * different <code>Contractpartner</code></li>
	 * <li>check that when there are no recorded <code>Moneyflow</code>s outside the
	 * new validty period changing an existing <code>Contractpartner</code></li>
	 * </ul>
	 *
	 * @param contractpartner the {@link Contractpartner} to validate
	 * @return ValidationResult
	 */
	ValidationResult validateContractpartner(Contractpartner contractpartner);

	/**
	 * This service returns the {@link Contractpartner} specified by its Id.
	 *
	 * @param userId            {@link UserID}
	 * @param contractpartnerId {@link ContractpartnerID}
	 * @return {@link Contractpartner}
	 */
	Contractpartner getContractpartnerById(UserID userId, ContractpartnerID contractpartnerId);

	/**
	 * This service returns all {@link Contractpartner}s where the given user has
	 * reading permissions.
	 *
	 * @param userId {@link UserID}
	 * @return a list of {@link Contractpartner}s
	 */
	List<Contractpartner> getAllContractpartners(UserID userId);

	/**
	 * This service looks the {@link Contractpartner} up which is valid on the given
	 * date and has the specified comment.
	 *
	 * @param userId {@link UserID}
	 * @param name
	 * @return {@link Contractpartner}
	 */
	Contractpartner getContractpartnerByName(UserID userId, String name);

	/**
	 * This method persists (updates) the given {@link Contractpartner}.
	 *
	 * @param contractpartner {@link Contractpartner}
	 * @throws BusinessException
	 */
	void updateContractpartner(Contractpartner contractpartner);

	/**
	 * This method persists (creates) the given {@link Contractpartner}.
	 *
	 * @param contractpartner {@link Contractpartner}
	 * @return The {@link ContractpartnerID} of the created {@link Contractpartner}
	 * @throws BusinessException
	 */
	ContractpartnerID createContractpartner(Contractpartner contractpartner);

	/**
	 * This method deletes the {@link Contractpartner} specified by its Id.
	 *
	 * @param userId            {@link UserID}
	 * @param groupId           {@link GroupID}
	 * @param contractpartnerId {@link ContractpartnerID}
	 * @throws BusinessException
	 */
	void deleteContractpartner(UserID userId, GroupID groupId, ContractpartnerID contractpartnerId);
}