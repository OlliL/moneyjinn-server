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

package org.laladev.moneyjinn.service.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.util.BytesToHexConverter;
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
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.util.Assert;

@Named
@EnableCaching
public class UserService extends AbstractService implements IUserService {
	private static final Log LOG = LogFactory.getLog(UserService.class);

	@Inject
	private UserDao userDao;
	private MessageDigest sha1MD;

	public UserService() {
		try {
			this.sha1MD = MessageDigest.getInstance("SHA1");
		} catch (final NoSuchAlgorithmException e) {
			LOG.error(e);
		}

	}

	@Override
	protected void addBeanMapper() {
		this.registerBeanMapper(new UserDataMapper());
	}

	@Override
	public ValidationResult validateUser(final User user) {
		Assert.notNull(user);
		final ValidationResult validationResult = new ValidationResult();

		if (user.getName() == null || user.getName().trim().isEmpty()) {
			validationResult
					.addValidationResultItem(new ValidationResultItem(user.getId(), ErrorCode.NAME_MUST_NOT_BE_EMPTY));
		} else {
			final User checkUser = this.getUserByName(user.getName());
			if (checkUser != null && (user.getId() == null || !checkUser.getId().equals(user.getId()))) {
				// Update OR Create
				validationResult.addValidationResultItem(
						new ValidationResultItem(user.getId(), ErrorCode.USER_WITH_SAME_NAME_ALREADY_EXISTS));
			}
		}

		return validationResult;

	}

	@Override
	@Cacheable(CacheNames.USER_BY_ID)
	public User getUserById(final UserID userId) {
		Assert.notNull(userId);
		final UserData userData = this.userDao.getUserById(userId.getId());
		return super.map(userData, User.class);
	}

	@Override
	public Set<Character> getAllUserInitials() {
		return this.userDao.getAllUserInitials();
	}

	@Override
	public Integer countAllUsers() {
		return this.userDao.countAllUsers();
	}

	@Override
	public List<User> getAllUsers() {
		final List<UserData> userDataList = this.userDao.getAllUsers();
		return super.mapList(userDataList, User.class);
	}

	@Override
	public List<User> getAllUsersByInitial(final Character initial) {
		Assert.notNull(initial);
		final List<UserData> userDataList = this.userDao.getAllUsersByInitial(initial);
		return super.mapList(userDataList, User.class);
	}

	@Override
	@Cacheable(CacheNames.USER_BY_NAME)
	public User getUserByName(final String name) {
		Assert.notNull(name);
		final UserData userData = this.userDao.getUserByName(name);
		return super.map(userData, User.class);
	}

	@Override
	public UserID createUser(final User user) {
		Assert.notNull(user);
		user.setId(null);
		final ValidationResult validationResult = this.validateUser(user);

		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("User creation failed!", validationResultItem.getError());
		}

		final String cryptedPassword = this.cryptPassword(user.getPassword());
		user.setPassword(cryptedPassword);

		final UserData userData = super.map(user, UserData.class);
		final Long userIDLong = this.userDao.createUser(userData);
		return new UserID(userIDLong);
	}

	@Override
	public void updateUser(final User user) {
		Assert.notNull(user);
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
	public void setPassword(final UserID userId, final String password) {
		Assert.notNull(userId);
		Assert.notNull(password);
		final User user = this.getUserById(userId);
		this.evictUserCache(user);
		final String cryptedPassword = this.cryptPassword(password);
		this.userDao.setPassword(userId.getId(), cryptedPassword);
	}

	@Override
	public void resetPassword(final UserID userId, final String password) {
		Assert.notNull(userId);
		Assert.notNull(password);
		final User user = this.getUserById(userId);
		this.evictUserCache(user);
		final String cryptedPassword = this.cryptPassword(password);
		this.userDao.resetPassword(userId.getId(), cryptedPassword);
	}

	@Override
	public void deleteUser(final UserID userId) {
		Assert.notNull(userId);
		try {
			final User user = this.getUserById(userId);
			this.evictUserCache(user);

			this.userDao.deleteUser(userId.getId());
		} catch (final Exception e) {
			LOG.info(e);
			throw new BusinessException("This user has already entered data and may therefore not be deleted!",
					ErrorCode.USER_HAS_DATA);
		}
	}

	private String cryptPassword(final String password) {
		if (password != null) {
			this.sha1MD.reset();

			return BytesToHexConverter.convert(this.sha1MD.digest(password.getBytes()));
		}
		return null;
	}

	private void evictUserCache(final User user) {
		if (user != null) {
			final Cache userByNameCache = super.getCache(CacheNames.USER_BY_NAME);
			final Cache userByIdCache = super.getCache(CacheNames.USER_BY_ID);
			if (userByNameCache != null) {
				userByNameCache.evict(user.getName());
			}
			if (userByIdCache != null) {
				userByIdCache.evict(user.getId());
			}
		}
	}

}
