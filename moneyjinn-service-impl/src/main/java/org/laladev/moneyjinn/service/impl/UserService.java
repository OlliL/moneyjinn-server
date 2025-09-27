//
// Copyright (c) 2015-2025 Oliver Lehmann <lehmann@ans-netz.de>
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

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
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

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;

import static org.springframework.util.Assert.isTrue;

@Named
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Log
public class UserService extends AbstractService implements IUserService {
    private static final String STILL_REFERENCED =
            "This user has already entered data and may therefore not be deleted!";
    private final UserDao userDao;
    private final UserDataMapper userDataMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ValidationResult validateUser(@NonNull final User user) {
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
    public User getUserById(@NonNull final UserID userId) {
        final Supplier<User> supplier = () -> this.userDataMapper.mapBToA(this.userDao.getUserById(userId.getId()));

        return super.getFromCacheOrExecute(CacheNames.USER_BY_ID, userId, supplier, User.class);
    }

    @Override
    public List<User> getAllUsers() {
        final List<UserData> userDataList = this.userDao.getAllUsers();
        return this.userDataMapper.mapBToA(userDataList);
    }

    @Override
    public User getUserByName(@NonNull final String name) {
        final Supplier<User> supplier = () -> this.userDataMapper.mapBToA(
                this.userDao.getUserByName(name));

        return super.getFromCacheOrExecute(CacheNames.USER_BY_NAME, name, supplier, User.class);
    }

    @Override
    public UserID createUser(@NonNull final User user) {
        user.setId(null);
        final ValidationResult validationResult = this.validateUser(user);
        if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
            final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().getFirst();
            throw new BusinessException("User creation failed!", validationResultItem.getError());
        }
        final String cryptedPassword = this.cryptPassword(user.getPassword());
        user.setPassword(cryptedPassword);
        final UserData userData = this.userDataMapper.mapAToB(user);
        final Long userIdLong = this.userDao.createUser(userData);
        return new UserID(userIdLong);
    }

    @Override
    public void updateUser(@NonNull final User user) {
        final ValidationResult validationResult = this.validateUser(user);
        if (!validationResult.isValid() && !validationResult.getValidationResultItems().isEmpty()) {
            final ValidationResultItem validationResultItem = validationResult.getValidationResultItems().getFirst();
            throw new BusinessException("User update failed!", validationResultItem.getError());
        }
        final User oldUser = this.getUserById(user.getId());
        this.evictUserCache(oldUser);
        this.evictUserCache(user);
        final UserData userData = this.userDataMapper.mapAToB(user);
        this.userDao.updateUser(userData);
    }

    @Override
    public void setPassword(@NonNull final UserID userId, @NonNull final String password,
                            @NonNull final String oldPassword) {
        isTrue(!password.isBlank(), "Password must not be empty!");
        isTrue(!oldPassword.isBlank(), "Old password must not be empty!");

        final User user = this.getUserById(userId);
        if (!this.passwordMatches(oldPassword, user.getPassword())) {
            throw new BusinessException("Wrong password!", ErrorCode.PASSWORD_NOT_MATCHING);
        }

        this.evictUserCache(user);

        final String cryptedPassword = this.cryptPassword(password);
        this.userDao.setPassword(userId.getId(), cryptedPassword);
    }

    @Override
    public void resetPassword(@NonNull final UserID userId, @NonNull final String password) {
        final User user = this.getUserById(userId);
        this.evictUserCache(user);
        final String cryptedPassword = this.cryptPassword(password);
        this.userDao.resetPassword(userId.getId(), cryptedPassword);
    }

    @Override
    public void deleteUser(@NonNull final UserID userId) {
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
