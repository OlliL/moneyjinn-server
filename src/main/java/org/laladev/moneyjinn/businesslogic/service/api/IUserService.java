package org.laladev.moneyjinn.businesslogic.service.api;

import java.util.List;

import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.exception.BusinessException;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResult;

/**
 * <p>
 * UserService is the Domain Service handling everything around an {@link User}.
 * </p>
 *
 * <p>
 * UserService is the Domain Service handling operations around an {@link User} like getting,
 * creating, updating, deleting. Before an {@link User} is created or updated, the {@link User} is
 * validated for correctness.
 * </p>
 * <p>
 * The main datasource is the Table <code>access</code>.
 * </p>
 *
 * @author Oliver Lehmann
 * @since 0.0.1
 *
 */
public interface IUserService {

	/**
	 * Checks the validity of the given {@link User}
	 *
	 * @param user
	 *            the {@link User}
	 * @return {@link ValidationResult}
	 */
	public ValidationResult validateUser(final User user);

	/**
	 * This Service returns the User for the specified id
	 *
	 * @param id
	 *            the {@link UserID}
	 * @return User
	 */
	public User getUserById(final UserID userId);

	/**
	 * This Service returns the distinct initials of all usernames in the database in uppercase.
	 *
	 * @return array all uppercased initials
	 */
	public List<Character> getAllUserInitials();

	/**
	 * This Service returns the number of all existing users
	 *
	 * @return int number of users
	 */
	public Integer countAllUsers();

	/**
	 * This Service returns all existing Users
	 *
	 * @return array a list of User objects
	 */
	public List<User> getAllUsers();

	/**
	 * This Service returns all existing Users which name start with the specified initial
	 * case-insensitive
	 *
	 * @param initial
	 *            the first letter of the Users name
	 * @return a list of User objects
	 */
	public List<User> getAllUsersByInitial(final Character initial);

	/**
	 * This Service returns the User for the specified name
	 *
	 * @param name
	 *            the User-Name
	 * @return User
	 */
	public User getUserByName(final String name);

	/**
	 * This service creates a User. Before the User is created it is validated for correctness.
	 *
	 * @param user
	 *            the User to be created
	 * @return {@link ValidationResult}
	 */
	public UserID createUser(final User user);

	/**
	 * This service changes a User. Before the User is changed, the new values are validated for
	 * correctness. Attention: The password is ignored _not_ updated by this Service.
	 *
	 * @param user
	 *            the new User attributes
	 * @return {@link ValidationResult}
	 */
	public ValidationResult updateUser(final User user);

	/**
	 * This service sets a new User-Password for the given User-Id. The password has to be given in
	 * clear-text to this service! This service should be used when the user itself changes his
	 * password as it also disables the "user has to change his password flag"
	 *
	 * @param userId
	 *            The {@link UserID} for which the password has to be set
	 * @param password
	 *            The new User-Password
	 */
	public void setPassword(final UserID userId, final String password);

	/**
	 * This service sets a new User-Password for the given User-Id. The password has to be given in
	 * clear-text to this service! This service should be used when an admin changes the password as
	 * it also sets the "user has to change his password flag"
	 *
	 * @param userId
	 *            The {@link UserID} for which the password has to be set
	 * @param password
	 *            The new User-Password
	 */
	public void resetPassword(final UserID userId, final String password);

	/**
	 * This service deletes a user from the system
	 *
	 * @param userId
	 *            The {@link UserID} of the to-be-deleted {@link User}
	 * @throws BusinessException
	 *             If the deletion fails an error is throws. It is always assumed that it fails
	 *             because of a Foreign Key Constraint Violation on the DB level
	 */
	public void deleteUser(final UserID userId);

}