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
import java.util.Set;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.model.validation.ValidationResult;

/**
 * <p>
 * UserService is the Core Service handling everything around an {@link User}.
 * </p>
 *
 * <p>
 * UserService is the Core Service handling operations around an {@link User} like getting,
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
   *          the {@link User}
   * @return {@link ValidationResult}
   */
  ValidationResult validateUser(final User user);

  /**
   * This Service returns the {@link User} for the given {@link UserID}.
   *
   * @param id
   *          the {@link UserID}
   * @return User
   */
  User getUserById(final UserID userId);

  /**
   * This Service returns the distinct initials of all usernames in the database in uppercase.
   *
   * @return all uppercased initials
   */
  Set<Character> getAllUserInitials();

  /**
   * This Service returns the number of all existing users
   *
   * @return number of {@link User}s
   */
  Integer countAllUsers();

  /**
   * This Service returns all existing {@link User}s
   *
   * @return list of all {@link User}s
   */
  List<User> getAllUsers();

  /**
   * This Service returns all existing {@link User}s which name start with the specified initial
   * case-insensitive
   *
   * @param initial
   *          the first letter of the {@link User}s name
   * @return a list of {@link User}s
   */
  List<User> getAllUsersByInitial(final Character initial);

  /**
   * This Service returns the {@link User} for the specified name
   *
   * @param name
   *          the User-Name
   * @return {@link User}
   */
  User getUserByName(final String name);

  /**
   * This service creates a {@link User}. Before the {@link User} is created it is validated for
   * correctness.
   *
   * @param user
   *          the {@link User} to be created
   * @throws BusinessException
   *           If the validation of the given {@link User} failed.
   * @return {@link UserID} of the created {@link User}
   */
  UserID createUser(final User user);

  /**
   * This service changes a {@link User}. Before the {@link User} is changed, the new values are
   * validated for correctness. Attention: If given, the password is ignored and _not_ updated by
   * this Service.
   *
   * @param user
   *          the new {@link User} attributes
   * @throws BusinessException
   *           If the validation of the given {@link User} failed.
   */
  void updateUser(final User user);

  /**
   * This service sets a new User-Password for the given User-Id. The password has to be given in
   * clear-text to this service! This service should be used when the user itself changes his
   * password as it also disables the "user has to change his password flag"
   *
   * @param userId
   *          The {@link UserID} for which the password has to be set
   * @param password
   *          The new User-Password
   */
  void setPassword(final UserID userId, final String password);

  /**
   * This service sets a new User-Password for the given User-Id. The password has to be given in
   * clear-text to this service! This service should be used when an admin changes the password as
   * it also sets the "user has to change his password flag"
   *
   * @param userId
   *          The {@link UserID} for which the password has to be set
   * @param password
   *          The new User-Password
   */
  void resetPassword(final UserID userId, final String password);

  /**
   * This service deletes a {@link User} from the system
   *
   * @param userId
   *          The {@link UserID} of the to-be-deleted {@link User}
   * @throws BusinessException
   *           If the deletion fails an error is throws. It is always assumed that it fails because
   *           of a Foreign Key Constraint Violation on the DB level
   */
  void deleteUser(final UserID userId);
}