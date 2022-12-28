//Copyright (c) 2015-2018 Oliver Lehmann <lehmann@ans-netz.de>
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

import jakarta.inject.Inject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.transport.GroupTransport;
import org.laladev.moneyjinn.core.rest.model.transport.UserTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.core.rest.model.user.AbstractCreateUserResponse;
import org.laladev.moneyjinn.core.rest.model.user.AbstractUpdateUserResponse;
import org.laladev.moneyjinn.core.rest.model.user.ChangePasswordRequest;
import org.laladev.moneyjinn.core.rest.model.user.CreateUserRequest;
import org.laladev.moneyjinn.core.rest.model.user.CreateUserResponse;
import org.laladev.moneyjinn.core.rest.model.user.LoginRequest;
import org.laladev.moneyjinn.core.rest.model.user.LoginResponse;
import org.laladev.moneyjinn.core.rest.model.user.ShowEditUserResponse;
import org.laladev.moneyjinn.core.rest.model.user.ShowUserListResponse;
import org.laladev.moneyjinn.core.rest.model.user.UpdateUserRequest;
import org.laladev.moneyjinn.core.rest.model.user.UpdateUserResponse;
import org.laladev.moneyjinn.core.rest.model.user.transport.AccessRelationTransport;
import org.laladev.moneyjinn.core.rest.util.BytesToHexConverter;
import org.laladev.moneyjinn.model.access.AccessRelation;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserAttribute;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.access.UserPermission;
import org.laladev.moneyjinn.model.exception.BusinessException;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.server.controller.mapper.AccessRelationTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.GroupTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.UserTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.ValidationItemTransportMapper;
import org.laladev.moneyjinn.server.jwt.JwtTokenProvider;
import org.laladev.moneyjinn.server.jwt.RefreshOnlyGrantedAuthority;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.IGroupService;
import org.laladev.moneyjinn.service.api.ISettingService;
import org.laladev.moneyjinn.service.api.IUserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequestMapping("/moneyflow/server/user/")
public class UserController extends AbstractController {
  private static final String RESTRICTION_ALL = "all";
  @Inject
  private IUserService userService;
  @Inject
  private IAccessRelationService accessRelationService;
  @Inject
  private IGroupService groupService;
  @Inject
  private ISettingService settingService;
  @Inject
  AuthenticationManager authenticationManager;
  @Inject
  JwtTokenProvider jwtTokenProvider;

  @Override
  protected void addBeanMapper() {
    this.registerBeanMapper(new UserTransportMapper());
    this.registerBeanMapper(new GroupTransportMapper());
    this.registerBeanMapper(new AccessRelationTransportMapper());
    this.registerBeanMapper(new ValidationItemTransportMapper());
  }

  @RequestMapping(value = "login", method = { RequestMethod.POST })
  public LoginResponse login(@RequestBody final LoginRequest request)
      throws NoSuchAlgorithmException {
    final String username = request.getUserName();
    final MessageDigest sha1Md = MessageDigest.getInstance("SHA1");
    final String password = BytesToHexConverter
        .convert(sha1Md.digest(request.getUserPassword().getBytes()));
    final LoginResponse response = new LoginResponse();
    this.authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(username, password));
    final User user = this.userService.getUserByName(username);
    return this.generateLoginResponse(response, user);
  }

  @RequestMapping(value = "refreshToken", method = { RequestMethod.GET })
  public LoginResponse refreshToken() throws NoSuchAlgorithmException {
    final LoginResponse response = new LoginResponse();
    final User user = this.userService.getUserById(super.getUserId());
    return this.generateLoginResponse(response, user);
  }

  private LoginResponse generateLoginResponse(final LoginResponse response, final User user) {
    if (user != null) {
      if (!user.getPermissions().contains(UserPermission.LOGIN)) {
        throw new BusinessException("Your account has been locked!", ErrorCode.ACCOUNT_IS_LOCKED);
      }
      final List<String> permissions = user.getPermissions().stream().map(perm -> perm.name())
          .collect(Collectors.toCollection(ArrayList::new));
      final String token = this.jwtTokenProvider.createToken(user.getName(), permissions);
      final String refreshToken = this.jwtTokenProvider.createRefreshToken(user.getName(),
          Arrays.asList(RefreshOnlyGrantedAuthority.ROLE));
      final UserTransport userTransport = super.map(user, UserTransport.class);
      response.setUserTransport(userTransport);
      response.setToken(token);
      response.setRefreshToken(refreshToken);
      return response;
    }
    throw new BusinessException("Wrong username or password!", ErrorCode.USERNAME_PASSWORD_WRONG);
  }

  @RequestMapping(value = "showEditUser/{id}", method = { RequestMethod.GET })
  public ShowEditUserResponse showEditUser(@PathVariable(value = "id") final Long userId) {
    final ShowEditUserResponse response = new ShowEditUserResponse();
    final User user = this.userService.getUserById(new UserID(userId));
    if (user != null) {
      final UserTransport userTransport = super.map(user, UserTransport.class);
      response.setUserTransport(userTransport);
      this.fillAbstractUpdateUserResponse(user.getId(), response);
    }

    return response;
  }

  @RequestMapping(value = "updateUser", method = { RequestMethod.PUT })
  public UpdateUserResponse updateUser(@RequestBody final UpdateUserRequest request) {
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
    if (validationResult.isValid()) {
      this.userService.updateUser(user);
      if (user.getPassword() != null) {
        this.userService.resetPassword(user.getId(), user.getPassword());
      }
      if (accessRelation != null) {
        validationResult.mergeValidationResult(
            this.accessRelationService.setAccessRelationForExistingUser(accessRelation));
      }
    }
    final UpdateUserResponse response = new UpdateUserResponse();
    response.setResult(validationResult.isValid());
    if (!validationResult.isValid()) {
      // TODO Rollback
      this.fillAbstractUpdateUserResponse(user.getId(), response);
      response.setValidationItemTransports(super.mapList(
          validationResult.getValidationResultItems(), ValidationItemTransport.class));
    }
    return response;
  }

  @RequestMapping(value = "showUserList", method = { RequestMethod.GET })
  public ShowUserListResponse showUserList() {
    final UserID userId = super.getUserId();
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
    return response;
  }

  @RequestMapping(value = "createUser", method = { RequestMethod.POST })
  public CreateUserResponse createUser(@RequestBody final CreateUserRequest request) {
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
    final CreateUserResponse response = new CreateUserResponse();
    response.setResult(validationResult.isValid());
    if (!validationResult.isValid()) {
      this.fillAbstractCreateUserResponse(response);
      response.setValidationItemTransports(super.mapList(
          validationResult.getValidationResultItems(), ValidationItemTransport.class));
    } else {
      final UserID newUserId = this.userService.createUser(user);
      if (newUserId != null) {
        response.setUserId(newUserId.getId());
        this.settingService.initSettings(newUserId);
        if (accessRelation != null) {
          accessRelation.setId(newUserId);
          this.accessRelationService.setAccessRelationForNewUser(accessRelation);
        }
      }
    }
    return response;
  }

  @RequestMapping(value = "deleteUserById/{id}", method = { RequestMethod.DELETE })
  public void deleteUserById(@PathVariable(value = "id") final Long id) {
    final UserID userId = new UserID(id);
    this.accessRelationService.deleteAllAccessRelation(userId);
    this.settingService.deleteSettings(userId);
    this.userService.deleteUser(userId);
  }

  @RequestMapping(value = "changePassword", method = { RequestMethod.PUT })
  public void changePassword(@RequestBody final ChangePasswordRequest request)
      throws NoSuchAlgorithmException {
    final UserID userId = super.getUserId();
    final User user = this.userService.getUserById(userId);
    final String password = request.getPassword();
    final MessageDigest sha1Md = MessageDigest.getInstance("SHA1");
    final String oldPassword = BytesToHexConverter
        .convert(sha1Md.digest(request.getOldPassword().getBytes()));
    if (!user.getPassword().equals(oldPassword)) {
      throw new BusinessException("Wrong password!", ErrorCode.PASSWORD_NOT_MATCHING);
    }
    if (password != null && !password.trim().isEmpty()) {
      this.userService.setPassword(userId, password);
    } else if (user.getAttributes().contains(UserAttribute.IS_NEW)) {
      throw new BusinessException("You have to change your password!",
          ErrorCode.PASSWORD_MUST_BE_CHANGED);
    }
  }

  private void fillAbstractCreateUserResponse(final AbstractCreateUserResponse response) {
    final List<Group> groups = this.groupService.getAllGroups();
    final List<GroupTransport> groupTransports = super.mapList(groups, GroupTransport.class);
    response.setGroupTransports(groupTransports);
  }

  private void fillAbstractUpdateUserResponse(final UserID userId,
      final AbstractUpdateUserResponse response) {
    final List<AccessRelation> accessRelations = this.accessRelationService
        .getAllAccessRelationsById(userId);
    final List<AccessRelationTransport> accessRelationTransports = super.mapList(accessRelations,
        AccessRelationTransport.class);
    response.setAccessRelationTransports(accessRelationTransports);
    this.fillAbstractCreateUserResponse(response);
  }

}
