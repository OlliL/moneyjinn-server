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
import org.laladev.moneyjinn.model.PreDefMoneyflow;
import org.laladev.moneyjinn.model.PreDefMoneyflowID;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.server.controller.api.PreDefMoneyflowControllerApi;
import org.laladev.moneyjinn.server.controller.mapper.PreDefMoneyflowTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.ValidationItemTransportMapper;
import org.laladev.moneyjinn.server.model.CreatePreDefMoneyflowRequest;
import org.laladev.moneyjinn.server.model.CreatePreDefMoneyflowResponse;
import org.laladev.moneyjinn.server.model.ErrorResponse;
import org.laladev.moneyjinn.server.model.PreDefMoneyflowTransport;
import org.laladev.moneyjinn.server.model.ShowPreDefMoneyflowListResponse;
import org.laladev.moneyjinn.server.model.UpdatePreDefMoneyflowRequest;
import org.laladev.moneyjinn.server.model.ValidationItemTransport;
import org.laladev.moneyjinn.server.model.ValidationResponse;
import org.laladev.moneyjinn.service.api.IPreDefMoneyflowService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class PreDefMoneyflowController extends AbstractController
    implements PreDefMoneyflowControllerApi {
  private final IPreDefMoneyflowService preDefMoneyflowService;
  private final PreDefMoneyflowTransportMapper preDefMoneyflowTransportMapper;
  private final ValidationItemTransportMapper validationItemTransportMapper;

  @Override
  @PostConstruct
  protected void addBeanMapper() {
    this.registerBeanMapper(this.preDefMoneyflowTransportMapper);
    this.registerBeanMapper(this.validationItemTransportMapper);
  }

  @Override
  public ResponseEntity<ShowPreDefMoneyflowListResponse> showPreDefMoneyflowList() {
    final UserID userId = super.getUserId();
    final List<PreDefMoneyflow> preDefMoneyflows = this.preDefMoneyflowService
        .getAllPreDefMoneyflows(userId);
    final ShowPreDefMoneyflowListResponse response = new ShowPreDefMoneyflowListResponse();
    if (preDefMoneyflows != null && !preDefMoneyflows.isEmpty()) {
      response.setPreDefMoneyflowTransports(
          super.mapList(preDefMoneyflows, PreDefMoneyflowTransport.class));
    }
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<CreatePreDefMoneyflowResponse> createPreDefMoneyflow(
      @RequestBody final CreatePreDefMoneyflowRequest request) {
    final PreDefMoneyflow preDefMoneyflow = super.map(request.getPreDefMoneyflowTransport(),
        PreDefMoneyflow.class);
    final UserID userId = super.getUserId();
    preDefMoneyflow.setId(null);
    preDefMoneyflow.setUser(new User(userId));
    final ValidationResult validationResult = this.preDefMoneyflowService
        .validatePreDefMoneyflow(preDefMoneyflow);
    final CreatePreDefMoneyflowResponse response = new CreatePreDefMoneyflowResponse();
    response.setResult(validationResult.isValid());
    if (!validationResult.isValid()) {
      response.setValidationItemTransports(super.mapList(
          validationResult.getValidationResultItems(), ValidationItemTransport.class));
    } else {
      final PreDefMoneyflowID preDefMoneyflowId = this.preDefMoneyflowService
          .createPreDefMoneyflow(preDefMoneyflow);
      response.setPreDefMoneyflowId(preDefMoneyflowId.getId());
    }
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<ValidationResponse> updatePreDefMoneyflow(
      @RequestBody final UpdatePreDefMoneyflowRequest request) {
    final PreDefMoneyflow preDefMoneyflow = super.map(request.getPreDefMoneyflowTransport(),
        PreDefMoneyflow.class);
    final UserID userId = super.getUserId();
    preDefMoneyflow.setUser(new User(userId));
    final ValidationResult validationResult = this.preDefMoneyflowService
        .validatePreDefMoneyflow(preDefMoneyflow);
    if (validationResult.isValid()) {
      this.preDefMoneyflowService.updatePreDefMoneyflow(preDefMoneyflow);
    } else {
      final ValidationResponse response = new ValidationResponse();
      response.setResult(validationResult.isValid());
      response.setValidationItemTransports(super.mapList(
          validationResult.getValidationResultItems(), ValidationItemTransport.class));
      return ResponseEntity.ok(response);
    }
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<ErrorResponse> deletePreDefMoneyflowById(
      @PathVariable(value = "id") final Long id) {
    final UserID userId = super.getUserId();
    final PreDefMoneyflowID preDefMoneyflowId = new PreDefMoneyflowID(id);
    this.preDefMoneyflowService.deletePreDefMoneyflow(userId, preDefMoneyflowId);
    return ResponseEntity.noContent().build();
  }

}
