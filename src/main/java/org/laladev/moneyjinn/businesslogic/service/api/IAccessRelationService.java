package org.laladev.moneyjinn.businesslogic.service.api;

//Copyright (c) 2015 Oliver Lehmann <oliver@laladev.org>
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without
//modification, are permitted provided that the following conditions
//are met:
//1. Redistributions of source code must retain the above copyright
//notice, this list of conditions and the following disclaimer
//2. Redistributions in binary form must reproduce the above copyright
//notice, this list of conditions and the following disclaimer in the
//documentation and/or other materials provided with the distribution.
//
//THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
//ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
//IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
//ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
//FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
//DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
//OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
//HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
//LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
//OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
//SUCH DAMAGE.

import java.time.LocalDate;
import java.util.List;

import org.laladev.moneyjinn.businesslogic.model.access.AccessID;
import org.laladev.moneyjinn.businesslogic.model.access.AccessRelation;
import org.laladev.moneyjinn.businesslogic.model.access.Group;
import org.laladev.moneyjinn.businesslogic.model.access.GroupID;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResult;

/**
 * <p>
 * AccessRelationService is the Core Service handling everything around an {@link AccessRelation}.
 * </p>
 *
 * <p>
 * AccessRelationService is the Core Service handling operations around an {@link AccessRelation}
 * like getting, creating, updating, deleting. Before an {@link AccessRelation} is created or
 * updated, the {@link AccessRelation} is validated for correctness.
 * </p>
 * <p>
 * The main datasource are the Tables <code>access_relation</code> and <code>access_flattened</code>
 * .
 * </p>
 *
 * @author Oliver Lehmann
 * @since 0.0.1
 *
 */
public interface IAccessRelationService {
	/**
	 * Checks the validity of the given {@link AccessRelation}
	 *
	 * @param accessRelation
	 *            the {@link AccessRelation}
	 * @return {@link ValidationResult}
	 */
	public ValidationResult validateAccessRelation(final AccessRelation accessRelation);

	/**
	 * Returns all {@link AccessRelation}s for the given user.
	 *
	 * @param userId
	 *            the {@link UserID}
	 * @return
	 */
	public List<AccessRelation> getAllAccessRelationsById(AccessID userId);

	/**
	 * Persists the given {@link AccessRelation} for an existing User.
	 *
	 * @param accessRelation
	 *            the {@link AccessRelation}.
	 * @return {@link ValidationResult} if the given Object was not valid
	 */
	public ValidationResult setAccessRelationForExistingUser(AccessRelation accessRelation);

	/**
	 * Persists the given {@link AccessRelation} for a new User.
	 *
	 * @param accessRelation
	 *            the {@link AccessRelation}.
	 * @return {@link ValidationResult} if the given Object was not valid
	 */
	public ValidationResult setAccessRelationForNewUser(AccessRelation accessRelation);

	/**
	 * Gets the currently valid {@link AccessRelation} for the given {@link UserID} or
	 * {@link GroupID}.
	 *
	 * @param accessRelationID
	 * @return
	 */
	public AccessRelation getAccessRelationById(final AccessID accessRelationID);

	/**
	 * The valid {@link AccessRelation} for the given {@link UserID} or {@link GroupID} at the given
	 * {@link LocalDate}.
	 *
	 * @param accessRelationID
	 * @param date
	 * @return
	 */
	public AccessRelation getAccessRelationById(final AccessID accessRelationID, final LocalDate date);

	/**
	 * Delets all relations to the given AccessID
	 *
	 * @param userId
	 */
	public void deleteAllAccessRelation(AccessID accessRelationID);

	/**
	 * gives the Group the {@link AccessID} is attached to.
	 *
	 * @param userId
	 * @param date
	 * @return
	 */
	public Group getAccessor(AccessID userId, LocalDate date);

	/**
	 * gives the Group the {@link AccessID} is attached to.
	 *
	 * @param userId
	 * @return
	 */
	public Group getAccessor(AccessID userId);

}
