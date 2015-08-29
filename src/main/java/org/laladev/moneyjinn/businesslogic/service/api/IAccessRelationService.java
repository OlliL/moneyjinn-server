package org.laladev.moneyjinn.businesslogic.service.api;

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
 * AccessRelationService is the Domain Service handling everything around an {@link AccessRelation}.
 * </p>
 *
 * <p>
 * AccessRelationService is the Domain Service handling operations around an {@link AccessRelation}
 * like getting, creating, updating, deleting. Before ab {@link AccessRelation} is created or
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
