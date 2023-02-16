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
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.GroupID;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.server.controller.api.GroupControllerApi;
import org.laladev.moneyjinn.server.controller.mapper.GroupTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.ValidationItemTransportMapper;
import org.laladev.moneyjinn.server.model.CreateGroupRequest;
import org.laladev.moneyjinn.server.model.CreateGroupResponse;
import org.laladev.moneyjinn.server.model.ErrorResponse;
import org.laladev.moneyjinn.server.model.GroupTransport;
import org.laladev.moneyjinn.server.model.ShowGroupListResponse;
import org.laladev.moneyjinn.server.model.UpdateGroupRequest;
import org.laladev.moneyjinn.server.model.ValidationItemTransport;
import org.laladev.moneyjinn.server.model.ValidationResponse;
import org.laladev.moneyjinn.service.api.IGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class GroupController extends AbstractController implements GroupControllerApi {
  private final IGroupService groupService;
  private final GroupTransportMapper groupTransportMapper;
  private final ValidationItemTransportMapper validationItemTransportMapper;

  @Override
  @PostConstruct
  protected void addBeanMapper() {
    this.registerBeanMapper(this.groupTransportMapper);
    this.registerBeanMapper(this.validationItemTransportMapper);
  }

  @Override
  @PreAuthorize(HAS_AUTHORITY_ADMIN)
  public ResponseEntity<ShowGroupListResponse> showGroupList() {
    final List<Group> groups = this.groupService.getAllGroups();
    final ShowGroupListResponse response = new ShowGroupListResponse();
    if (groups != null && !groups.isEmpty()) {
      response.setGroupTransports(super.mapList(groups, GroupTransport.class));
    }
    return ResponseEntity.ok(response);
  }

  @Override
  @PreAuthorize(HAS_AUTHORITY_ADMIN)
  public ResponseEntity<CreateGroupResponse> createGroup(
      @RequestBody final CreateGroupRequest request) {
    final Group group = super.map(request.getGroupTransport(), Group.class);
    group.setId(null);
    final ValidationResult validationResult = this.groupService.validateGroup(group);
    final CreateGroupResponse response = new CreateGroupResponse();
    response.setResult(validationResult.isValid());
    if (validationResult.isValid()) {
      final GroupID groupId = this.groupService.createGroup(group);
      response.setGroupId(groupId.getId());
    } else {
      response.setValidationItemTransports(super.mapList(
          validationResult.getValidationResultItems(), ValidationItemTransport.class));
    }
    return ResponseEntity.ok(response);
  }

  @Override
  @PreAuthorize(HAS_AUTHORITY_ADMIN)
  public ResponseEntity<ValidationResponse> updateGroup(
      @RequestBody final UpdateGroupRequest request) {
    final Group group = super.map(request.getGroupTransport(), Group.class);
    final ValidationResult validationResult = this.groupService.validateGroup(group);
    final ValidationResponse response = new ValidationResponse();
    response.setResult(validationResult.isValid());
    if (validationResult.isValid()) {
      this.groupService.updateGroup(group);
    } else {
      response.setValidationItemTransports(super.mapList(
          validationResult.getValidationResultItems(), ValidationItemTransport.class));
    }
    return ResponseEntity.ok(response);
  }

  @Override
  @PreAuthorize(HAS_AUTHORITY_ADMIN)
  public ResponseEntity<ErrorResponse> deleteGroupById(@PathVariable(value = "id") final Long id) {
    final GroupID groupId = new GroupID(id);
    this.groupService.deleteGroup(groupId);
    return ResponseEntity.noContent().build();
  }

}
