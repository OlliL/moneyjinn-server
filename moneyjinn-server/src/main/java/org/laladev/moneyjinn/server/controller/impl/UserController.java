//Copyright (c) 2015-2023 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.server.controller.impl;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.model.access.AccessRelation;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserAttribute;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.access.UserPermission;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.server.config.jwt.JwtTokenProvider;
import org.laladev.moneyjinn.server.controller.api.UserControllerApi;
import org.laladev.moneyjinn.server.controller.mapper.AccessRelationTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.GroupTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.UserTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.ValidationItemTransportMapper;
import org.laladev.moneyjinn.server.model.AccessRelationTransport;
import org.laladev.moneyjinn.server.model.ChangePasswordRequest;
import org.laladev.moneyjinn.server.model.CreateUserRequest;
import org.laladev.moneyjinn.server.model.CreateUserResponse;
import org.laladev.moneyjinn.server.model.GroupTransport;
import org.laladev.moneyjinn.server.model.LoginRequest;
import org.laladev.moneyjinn.server.model.LoginResponse;
import org.laladev.moneyjinn.server.model.ShowEditUserResponse;
import org.laladev.moneyjinn.server.model.ShowUserListResponse;
import org.laladev.moneyjinn.server.model.UpdateUserRequest;
import org.laladev.moneyjinn.server.model.UserTransport;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.IGroupService;
import org.laladev.moneyjinn.service.api.ISettingService;
import org.laladev.moneyjinn.service.api.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class UserController extends AbstractController implements UserControllerApi {
  private final IUserService userService;
  private final IAccessRelationService accessRelationService;
  private final IGroupService groupService;
  private final ISettingService settingService;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;
  private final AccessRelationTransportMapper accessRelationTransportMapper;
  private final GroupTransportMapper groupTransportMapper;
  private final UserTransportMapper userTransportMapper;
  private final ValidationItemTransportMapper validationItemTransportMapper;

  @Override
  @PostConstruct
  protected void addBeanMapper() {
    this.registerBeanMapper(this.userTransportMapper);
    this.registerBeanMapper(this.groupTransportMapper);
    this.registerBeanMapper(this.accessRelationTransportMapper);
    this.registerBeanMapper(this.validationItemTransportMapper);
  }

  @Override
  public ResponseEntity<LoginResponse> login(@RequestBody final LoginRequest request) {
    final String username = request.getUserName();
    final String password = this.userService.cryptPassword(request.getUserPassword());

    this.authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(username, password));

    final User user = this.userService.getUserByName(username);

    return ResponseEntity.ok(this.generateLoginResponse(user));
  }

  @Override
  public ResponseEntity<LoginResponse> refreshToken() {
    final User user = this.userService.getUserById(super.getUserId());

    return ResponseEntity.ok(this.generateLoginResponse(user));

  }

  private LoginResponse generateLoginResponse(final User user) {
    final LoginResponse response = new LoginResponse();
    if (user != null) {
      if (!user.getPermissions().contains(UserPermission.LOGIN)) {
        throw new BusinessException("Your account has been locked!", ErrorCode.ACCOUNT_IS_LOCKED);
      }
      final List<String> permissions = user.getPermissions().stream().map(Enum::name)
          .collect(Collectors.toCollection(ArrayList::new));
      final String token = this.jwtTokenProvider.createToken(user.getName(), permissions,
          user.getId().getId());
      final String refreshToken = this.jwtTokenProvider.createRefreshToken(user.getName(),
          user.getId().getId());
      final UserTransport userTransport = super.map(user, UserTransport.class);
      response.setUserTransport(userTransport);
      response.setToken(token);
      response.setRefreshToken(refreshToken);
      return response;
    }
    throw new BusinessException("Wrong username or password!", ErrorCode.USERNAME_PASSWORD_WRONG);
  }

  @Override
  public ResponseEntity<Void> changePassword(@RequestBody final ChangePasswordRequest request) {
    final UserID userId = super.getUserId();
    final User user = this.userService.getUserById(userId);
    final String password = request.getPassword();
    final String oldPassword = this.userService.cryptPassword(request.getOldPassword());
    if (!user.getPassword().equals(oldPassword)) {
      throw new BusinessException("Wrong password!", ErrorCode.PASSWORD_NOT_MATCHING);
    }
    if (password != null && !password.trim().isEmpty()) {
      this.userService.setPassword(userId, password);
    } else if (user.getAttributes().contains(UserAttribute.IS_NEW)) {
      throw new BusinessException("You have to change your password!",
          ErrorCode.PASSWORD_MUST_BE_CHANGED);
    }
    return ResponseEntity.noContent().build();
  }

  @Override
  @PreAuthorize(HAS_AUTHORITY_ADMIN)
  public ResponseEntity<ShowEditUserResponse> showEditUser(
      @PathVariable(value = "id") final Long userId) {
    final ShowEditUserResponse response = new ShowEditUserResponse();
    final User user = this.userService.getUserById(new UserID(userId));
    if (user != null) {
      final List<AccessRelation> accessRelations = this.accessRelationService
          .getAllAccessRelationsById(user.getId());
      final List<AccessRelationTransport> accessRelationTransports = super.mapList(accessRelations,
          AccessRelationTransport.class);
      response.setAccessRelationTransports(accessRelationTransports);

    }

    return ResponseEntity.ok(response);
  }

  @Override
  @PreAuthorize(HAS_AUTHORITY_ADMIN)
  public ResponseEntity<Void> updateUser(@RequestBody final UpdateUserRequest request) {
    final User user = super.map(request.getUserTransport(), User.class);
    final ValidationResult validationResultUser = this.userService.validateUser(user);
    final ValidationResult validationResult = new ValidationResult();
    validationResult.mergeValidationResult(validationResultUser);
    final AccessRelation accessRelation = super.map(request.getAccessRelationTransport(),
        AccessRelation.class);
    if (accessRelation != null) {
      final ValidationResult validationResultAccess = this.accessRelationService
          .validateAccessRelation(accessRelation);
      validationResult.mergeValidationResult(validationResultAccess);
    }

    this.throwValidationExceptionIfInvalid(validationResult);

    this.userService.updateUser(user);
    if (user.getPassword() != null) {
      this.userService.resetPassword(user.getId(), user.getPassword());
    }
    if (accessRelation != null) {
      validationResult.mergeValidationResult(
          this.accessRelationService.setAccessRelationForExistingUser(accessRelation));

      this.throwValidationExceptionIfInvalid(validationResult);
    }

    return ResponseEntity.noContent().build();
  }

  @Override
  @PreAuthorize(HAS_AUTHORITY_ADMIN)
  public ResponseEntity<ShowUserListResponse> showUserList() {
    final List<User> users = this.userService.getAllUsers();

    final ShowUserListResponse response = new ShowUserListResponse();
    final Set<Group> groupSet = new HashSet<>();
    final List<AccessRelation> accessRelationList = new ArrayList<>();
    if (users != null && !users.isEmpty()) {
      for (final User user : users) {
        final AccessRelation accessRelation = this.accessRelationService
            .getAccessRelationById(user.getId());
        if (accessRelation != null) {
          accessRelationList.add(accessRelation);
          if (accessRelation.getParentAccessRelation() != null) {
            final Group group = this.groupService.getGroupById(
                new GroupID(accessRelation.getParentAccessRelation().getId().getId()));
            groupSet.add(group);
          }
        }
      }
      response.setUserTransports(super.mapList(users, UserTransport.class));
      response.setAccessRelationTransports(
          super.mapList(accessRelationList, AccessRelationTransport.class));
      response.setGroupTransports(super.mapList(new ArrayList<>(groupSet), GroupTransport.class));
    }
    return ResponseEntity.ok(response);
  }

  @Override
  @PreAuthorize(HAS_AUTHORITY_ADMIN)
  public ResponseEntity<CreateUserResponse> createUser(
      @RequestBody final CreateUserRequest request) {
    final User user = super.map(request.getUserTransport(), User.class);
    user.setId(null);
    final ValidationResult validationResultUser = this.userService.validateUser(user);
    final ValidationResult validationResult = new ValidationResult();
    validationResult.mergeValidationResult(validationResultUser);
    final AccessRelation accessRelation = super.map(request.getAccessRelationTransport(),
        AccessRelation.class);
    if (accessRelation != null) {
      accessRelation.setId(null);
      final ValidationResult validationResultAccess = this.accessRelationService
          .validateAccessRelation(accessRelation);
      validationResult.mergeValidationResult(validationResultAccess);
    }

    this.throwValidationExceptionIfInvalid(validationResult);

    final CreateUserResponse response = new CreateUserResponse();
    final UserID newUserId = this.userService.createUser(user);
    if (newUserId != null) {
      response.setUserId(newUserId.getId());
      if (accessRelation != null) {
        accessRelation.setId(newUserId);
        this.accessRelationService.setAccessRelationForNewUser(accessRelation);
      }
    }

    return ResponseEntity.ok(response);
  }

  @Override
  @PreAuthorize(HAS_AUTHORITY_ADMIN)
  public ResponseEntity<Void> deleteUserById(@PathVariable(value = "id") final Long id) {
    final UserID userId = new UserID(id);

    this.accessRelationService.deleteAllAccessRelation(userId);
    this.settingService.deleteSettings(userId);
    this.userService.deleteUser(userId);

    return ResponseEntity.noContent().build();
  }
}