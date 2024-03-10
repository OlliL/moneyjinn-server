//
// Copyright (c) 2015-2024 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.service.impl;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;

import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.IHasUser;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.service.CacheNames;
import org.laladev.moneyjinn.service.api.IUserService;
import org.laladev.moneyjinn.service.dao.UserDao;
import org.laladev.moneyjinn.service.dao.data.UserData;
import org.laladev.moneyjinn.service.dao.data.mapper.UserDataMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Named
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Log
public class UserService extends AbstractService implements IUserService {
	private static final String STILL_REFERENCED = "This user has already entered data and may therefore not be deleted!";
	private static final String USER_MUST_NOT_BE_NULL = "user must not be null!";
	private static final String USER_ID_MUST_NOT_BE_NULL = "UserId must not be null!";
	private final UserDao userDao;
	private final UserDataMapper userDataMapper;
	private final PasswordEncoder passwordEncoder;

	@Override
	@PostConstruct
	protected void addBeanMapper() {
		this.registerBeanMapper(this.userDataMapper);
	}

	@Override
	public ValidationResult validateUser(final User user) {
		Assert.notNull(user, USER_MUST_NOT_BE_NULL);

		final ValidationResult validationResult = new ValidationResult();
		final Consumer<ErrorCode> addResult = (final ErrorCode errorCode) -> validationResult.addValidationResultItem(
				new ValidationResultItem(user.getId(), errorCode));

		if (user.getName() == null || user.getName().isBlank()) {
			addResult.accept(ErrorCode.NAME_MUST_NOT_BE_EMPTY);
		} else {
			final User checkUser = this.getUserByName(user.getName());
			if (checkUser != null && (user.getId() == null || !checkUser.getId().equals(user.getId()))) {
				// Update OR Create
				addResult.accept(ErrorCode.USER_WITH_SAME_NAME_ALREADY_EXISTS);
			}
		}

		return validationResult;
	}

	@Override
	public User getUserById(final UserID userId) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);

		final Supplier<User> supplier = () -> super.map(
				this.userDao.getUserById(userId.getId()), User.class);

		return super.getFromCacheOrExecute(CacheNames.USER_BY_ID, userId, supplier, User.class);
	}

	@Override
	public List<User> getAllUsers() {
		final List<UserData> userDataList = this.userDao.getAllUsers();
		return super.mapList(userDataList, User.class);
	}

	@Override
	public User getUserByName(final String name) {
		Assert.notNull(name, "name must not be null!");

		final Supplier<User> supplier = () -> super.map(
				this.userDao.getUserByName(name), User.class);

		return super.getFromCacheOrExecute(CacheNames.USER_BY_NAME, name, supplier, User.class);
	}

	@Override
	public UserID createUser(final User user) {
		Assert.notNull(user, USER_MUST_NOT_BE_NULL);
		user.setId(null);
		final ValidationResult validationResult = this.validateUser(user);
		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("User creation failed!", validationResultItem.getError());
		}
		final String cryptedPassword = this.cryptPassword(user.getPassword());
		user.setPassword(cryptedPassword);
		final UserData userData = super.map(user, UserData.class);
		final Long userIdLong = this.userDao.createUser(userData);
		return new UserID(userIdLong);
	}

	@Override
	public void updateUser(final User user) {
		Assert.notNull(user, USER_MUST_NOT_BE_NULL);
		final ValidationResult validationResult = this.validateUser(user);
		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("User update failed!", validationResultItem.getError());
		}
		final User oldUser = this.getUserById(user.getId());
		this.evictUserCache(oldUser);
		this.evictUserCache(user);
		final UserData userData = super.map(user, UserData.class);
		this.userDao.updateUser(userData);
	}

	@Override
	public void setPassword(final UserID userId, final String password, final String oldPassword) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(password, "Password must not be null!");
		Assert.notNull(oldPassword, "Old password must not be null!");
		Assert.isTrue(!password.isBlank(), "Password must not be empty!");
		Assert.isTrue(!oldPassword.isBlank(), "Old password must not be empty!");

		final User user = this.getUserById(userId);
		if (!this.passwordMatches(oldPassword, user.getPassword())) {
			throw new BusinessException("Wrong password!", ErrorCode.PASSWORD_NOT_MATCHING);
		}

		this.evictUserCache(user);

		final String cryptedPassword = this.cryptPassword(password);
		this.userDao.setPassword(userId.getId(), cryptedPassword);
	}

	@Override
	public void resetPassword(final UserID userId, final String password) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		Assert.notNull(password, "password must not be null!");
		final User user = this.getUserById(userId);
		this.evictUserCache(user);
		final String cryptedPassword = this.cryptPassword(password);
		this.userDao.resetPassword(userId.getId(), cryptedPassword);
	}

	@Override
	public void deleteUser(final UserID userId) {
		Assert.notNull(userId, USER_ID_MUST_NOT_BE_NULL);
		try {
			final User user = this.getUserById(userId);
			this.evictUserCache(user);
			this.userDao.deleteUser(userId.getId());
		} catch (final Exception e) {
			log.log(Level.INFO, STILL_REFERENCED, e);
			throw new BusinessException(STILL_REFERENCED, ErrorCode.USER_HAS_DATA);
		}
	}

	@Override
	public boolean passwordMatches(final CharSequence rawPassword, final String encodedPassword) {
		return this.passwordEncoder.matches(rawPassword, encodedPassword);
	}

	private String cryptPassword(final String password) {
		return password == null ? null : this.passwordEncoder.encode(password);
	}

	private void evictUserCache(final User user) {
		if (user != null) {
			super.evictFromCache(CacheNames.USER_BY_NAME, user.getName());
			super.evictFromCache(CacheNames.USER_BY_ID, user.getId());
		}
	}

	@Override
	public <T extends IHasUser> void enrichEntity(final T entity) {
		if (entity.getUser() != null) {
			final var fullMau = this.getUserById(entity.getUser().getId());
			entity.setUser(fullMau);
		}
	}
}
