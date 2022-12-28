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
import java.util.List;
import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import org.laladev.moneyjinn.core.rest.model.predefmoneyflow.CreatePreDefMoneyflowRequest;
import org.laladev.moneyjinn.core.rest.model.predefmoneyflow.CreatePreDefMoneyflowResponse;
import org.laladev.moneyjinn.core.rest.model.predefmoneyflow.ShowPreDefMoneyflowListResponse;
import org.laladev.moneyjinn.core.rest.model.predefmoneyflow.UpdatePreDefMoneyflowRequest;
import org.laladev.moneyjinn.core.rest.model.transport.PreDefMoneyflowTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.model.PreDefMoneyflow;
import org.laladev.moneyjinn.model.PreDefMoneyflowID;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.server.controller.mapper.PreDefMoneyflowTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.ValidationItemTransportMapper;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.IContractpartnerService;
import org.laladev.moneyjinn.service.api.IPostingAccountService;
import org.laladev.moneyjinn.service.api.IPreDefMoneyflowService;
import org.laladev.moneyjinn.service.api.ISettingService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequestMapping("/moneyflow/server/predefmoneyflow/")
public class PreDefMoneyflowController extends AbstractController {
  private static final String RESTRICTION_ALL = "all";
  @Inject
  private IAccessRelationService accessRelationService;
  @Inject
  private IPreDefMoneyflowService preDefMoneyflowService;
  @Inject
  private ICapitalsourceService capitalsourceService;
  @Inject
  private IContractpartnerService contractpartnerService;
  @Inject
  private IPostingAccountService postingAccountService;
  @Inject
  private ISettingService settingService;

  @Override
  protected void addBeanMapper() {
    this.registerBeanMapper(new PreDefMoneyflowTransportMapper());
    this.registerBeanMapper(new ValidationItemTransportMapper());
  }

  @RequestMapping(value = "showPreDefMoneyflowList", method = { RequestMethod.GET })
  public ShowPreDefMoneyflowListResponse showPreDefMoneyflowList() {
    final UserID userId = super.getUserId();
    final List<PreDefMoneyflow> preDefMoneyflows = this.preDefMoneyflowService
        .getAllPreDefMoneyflows(userId);
    final ShowPreDefMoneyflowListResponse response = new ShowPreDefMoneyflowListResponse();
    if (preDefMoneyflows != null && !preDefMoneyflows.isEmpty()) {
      response.setPreDefMoneyflowTransports(
          super.mapList(preDefMoneyflows, PreDefMoneyflowTransport.class));
    }
    return response;
  }

  @RequestMapping(value = "createPreDefMoneyflow", method = { RequestMethod.POST })
  public CreatePreDefMoneyflowResponse createPreDefMoneyflow(
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
    return response;
  }

  @RequestMapping(value = "updatePreDefMoneyflow", method = { RequestMethod.PUT })
  public ValidationResponse updatePreDefMoneyflow(
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
      return response;
    }
    return null;
  }

  @RequestMapping(value = "deletePreDefMoneyflow/{id}", method = { RequestMethod.DELETE })
  public void deletePreDefMoneyflowById(@PathVariable(value = "id") final Long id) {
    final UserID userId = super.getUserId();
    final PreDefMoneyflowID preDefMoneyflowId = new PreDefMoneyflowID(id);
    this.preDefMoneyflowService.deletePreDefMoneyflow(userId, preDefMoneyflowId);
  }

}
