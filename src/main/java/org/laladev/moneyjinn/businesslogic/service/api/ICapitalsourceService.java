package org.laladev.moneyjinn.businesslogic.service.api;

import java.time.LocalDate;
import java.util.List;

import org.laladev.moneyjinn.businesslogic.model.access.Group;
import org.laladev.moneyjinn.businesslogic.model.access.GroupID;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.businesslogic.model.exception.BusinessException;
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
	public List<Character> getAllCapitalsourceInitials(UserID userId);

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
	public List<Character> getAllCapitalsourceInitialsByDateRange(UserID userId, LocalDate validFrom,
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
	 *            the first letter of the {@link Group}s name
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
	 *            the first letter of the {@link Group}s name
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

}