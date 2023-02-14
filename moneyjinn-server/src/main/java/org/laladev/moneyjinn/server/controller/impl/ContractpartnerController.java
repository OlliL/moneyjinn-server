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
import org.laladev.moneyjinn.model.Contractpartner;
import org.laladev.moneyjinn.model.ContractpartnerID;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.server.controller.api.ContractpartnerControllerApi;
import org.laladev.moneyjinn.server.controller.mapper.ContractpartnerTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.ValidationItemTransportMapper;
import org.laladev.moneyjinn.server.model.ContractpartnerTransport;
import org.laladev.moneyjinn.server.model.CreateContractpartnerRequest;
import org.laladev.moneyjinn.server.model.CreateContractpartnerResponse;
import org.laladev.moneyjinn.server.model.ErrorResponse;
import org.laladev.moneyjinn.server.model.ShowContractpartnerListResponse;
import org.laladev.moneyjinn.server.model.UpdateContractpartnerRequest;
import org.laladev.moneyjinn.server.model.ValidationItemTransport;
import org.laladev.moneyjinn.server.model.ValidationResponse;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.IContractpartnerAccountService;
import org.laladev.moneyjinn.service.api.IContractpartnerService;
import org.laladev.moneyjinn.service.api.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ContractpartnerController extends AbstractController
    implements ContractpartnerControllerApi {
  private final IAccessRelationService accessRelationService;
  private final IContractpartnerService contractpartnerService;
  private final IContractpartnerAccountService contractpartnerAccountService;
  private final IUserService userService;
  private final ContractpartnerTransportMapper contractpartnerTransportMapper;
  private final ValidationItemTransportMapper validationItemTransportMapper;

  @Override
  @PostConstruct
  protected void addBeanMapper() {
    this.registerBeanMapper(this.contractpartnerTransportMapper);
    this.registerBeanMapper(this.validationItemTransportMapper);
  }

  @Override
  public ResponseEntity<ShowContractpartnerListResponse> showContractpartnerList() {
    final UserID userId = super.getUserId();

    final List<Contractpartner> contractpartners = this.contractpartnerService
        .getAllContractpartners(userId);
    final ShowContractpartnerListResponse response = new ShowContractpartnerListResponse();
    if (contractpartners != null && !contractpartners.isEmpty()) {
      response.setContractpartnerTransports(
          super.mapList(contractpartners, ContractpartnerTransport.class));
    }

    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<CreateContractpartnerResponse> createContractpartner(
      @RequestBody final CreateContractpartnerRequest request) {
    final UserID userId = super.getUserId();
    final Contractpartner contractpartner = super.map(request.getContractpartnerTransport(),
        Contractpartner.class);
    final User user = this.userService.getUserById(userId);
    final Group accessor = this.accessRelationService.getAccessor(userId);
    contractpartner.setId(null);
    contractpartner.setUser(user);
    contractpartner.setAccess(accessor);
    final ValidationResult validationResult = this.contractpartnerService
        .validateContractpartner(contractpartner);
    final CreateContractpartnerResponse response = new CreateContractpartnerResponse();
    response.setResult(validationResult.isValid());
    if (!validationResult.isValid()) {
      response.setValidationItemTransports(super.mapList(
          validationResult.getValidationResultItems(), ValidationItemTransport.class));
      return ResponseEntity.ok(response);
    }
    final ContractpartnerID contractpartnerId = this.contractpartnerService
        .createContractpartner(contractpartner);
    response.setContractpartnerId(contractpartnerId.getId());
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<ValidationResponse> updateContractpartner(
      @RequestBody final UpdateContractpartnerRequest request) {
    final UserID userId = super.getUserId();
    final Contractpartner contractpartner = super.map(request.getContractpartnerTransport(),
        Contractpartner.class);
    final User user = this.userService.getUserById(userId);
    final Group accessor = this.accessRelationService.getAccessor(userId);
    contractpartner.setUser(user);
    contractpartner.setAccess(accessor);
    final ValidationResult validationResult = this.contractpartnerService
        .validateContractpartner(contractpartner);
    final ValidationResponse response = new ValidationResponse();
    response.setResult(validationResult.isValid());
    if (!validationResult.isValid()) {
      response.setValidationItemTransports(super.mapList(
          validationResult.getValidationResultItems(), ValidationItemTransport.class));
    } else {
      this.contractpartnerService.updateContractpartner(contractpartner);
    }
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<ErrorResponse> deleteContractpartner(
      @PathVariable(value = "id") final Long id) {
    final UserID userId = super.getUserId();
    final Group accessor = this.accessRelationService.getAccessor(userId);
    final ContractpartnerID contractpartnerId = new ContractpartnerID(id);
    this.contractpartnerAccountService.deleteContractpartnerAccounts(userId, contractpartnerId);
    this.contractpartnerService.deleteContractpartner(userId, accessor.getId(), contractpartnerId);
    return ResponseEntity.noContent().build();
  }
}
