package org.laladev.moneyjinn.businesslogic.service.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.laladev.moneyjinn.businesslogic.dao.UserDao;
import org.laladev.moneyjinn.businesslogic.dao.data.UserData;
import org.laladev.moneyjinn.businesslogic.dao.data.mapper.UserDataMapper;
import org.laladev.moneyjinn.businesslogic.model.ErrorCode;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.exception.BusinessException;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResult;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.businesslogic.service.CacheNames;
import org.laladev.moneyjinn.businesslogic.service.api.IUserService;
import org.laladev.moneyjinn.core.rest.util.BytesToHexConverter;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;

@Named
@EnableCaching
public class UserService extends AbstractService implements IUserService {
	@Inject
	private UserDao userDao;
	@Inject
	private CacheManager cacheManager;
	private MessageDigest sha1MD;

	public UserService() {
		try {
			this.sha1MD = MessageDigest.getInstance("SHA1");
		} catch (final NoSuchAlgorithmException e) {
		}

	}

	@Override
	protected void addBeanMapper() {
		this.registerBeanMapper(new UserDataMapper());
	}

	@Override
	public ValidationResult validateUser(final User user) {
		final ValidationResult validationResult = new ValidationResult();

		if (user.getName() == null || user.getName().trim().isEmpty()) {
			validationResult
					.addValidationResultItem(new ValidationResultItem(user.getId(), ErrorCode.NAME_MUST_NOT_BE_EMPTY));
		} else {
			final User checkUser = this.getUserByName(user.getName());
			// Update OR Create
			if ((user.getId() == null && checkUser != null)
					|| (checkUser != null && !checkUser.getId().equals(user.getId()))) {
				validationResult.addValidationResultItem(
						new ValidationResultItem(user.getId(), ErrorCode.USER_WITH_SAME_NAME_ALREADY_EXISTS));
			}
		}

		return validationResult;
	}

	@Override
	@Cacheable(CacheNames.USER_BY_ID)
	public User getUserById(final UserID userId) {
		final UserData userData = this.userDao.getUserById(userId.getId());
		final User user = super.map(userData, User.class);
		return user;
	}

	@Override
	public List<Character> getAllUserInitials() {
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
		final List<UserData> userDataList = this.userDao.getAllUsersByInitial(initial);
		return super.mapList(userDataList, User.class);
	}

	@Override
	@Cacheable(CacheNames.USER_BY_NAME)
	public User getUserByName(final String name) {
		final UserData userData = this.userDao.getUserByName(name);
		final User user = super.map(userData, User.class);
		return user;
	}

	@Override
	public UserID createUser(final User user) {
		final ValidationResult validationResult = this.validateUser(user);

		this.cryptPassword(user);

		if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
			final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().get(0);
			throw new BusinessException("User creation failed!", validationResultItem.getError());
		}

		final UserData userData = super.map(user, UserData.class);
		final Long userIDLong = this.userDao.createUser(userData);
		return new UserID(userIDLong);
	}

	@Override
	public ValidationResult updateUser(final User user) {
		final ValidationResult validationResult = this.validateUser(user);

		if (validationResult.isValid()) {
			final User oldUser = this.getUserById(user.getId());
			this.evictUserCache(oldUser);
			this.evictUserCache(user);

			final UserData userData = super.map(user, UserData.class);
			this.userDao.updateUser(userData);
		}
		return validationResult;
	}

	@Override
	public void setPassword(final UserID userId, final String password) {
		final User user = this.getUserById(userId);
		this.evictUserCache(user);
		this.cryptPassword(user);
		this.userDao.setPassword(userId.getId(), password);
	}

	@Override
	public void resetPassword(final UserID userId, final String password) {
		final User user = this.getUserById(userId);
		this.evictUserCache(user);
		this.cryptPassword(user);
		this.userDao.resetPassword(userId.getId(), password);
	}

	@Override
	public void deleteUser(final UserID userId) {
		try {
			final User user = this.getUserById(userId);
			this.evictUserCache(user);

			this.userDao.deleteUser(userId.getId());
		} catch (final Exception e) {
			throw new BusinessException("This user has already entered data and may therefore not be deleted!",
					ErrorCode.USER_HAS_DATA);
		}
	}

	private void cryptPassword(final User user) {
		this.sha1MD.reset();

		String userPassword = user.getPassword();
		userPassword = BytesToHexConverter.convert(this.sha1MD.digest(userPassword.getBytes()));
		user.setPassword(userPassword);
	}

	private void evictUserCache(final User user) {
		final Cache userByNameCache = this.cacheManager.getCache(CacheNames.USER_BY_NAME);
		final Cache userByIdCache = this.cacheManager.getCache(CacheNames.USER_BY_ID);
		userByNameCache.evict(user.getName());
		userByIdCache.evict(user.getId());
	}

}
