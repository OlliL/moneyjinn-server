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
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.server.controller.api.CapitalsourceControllerApi;
import org.laladev.moneyjinn.server.controller.mapper.CapitalsourceTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.ValidationItemTransportMapper;
import org.laladev.moneyjinn.server.model.CapitalsourceTransport;
import org.laladev.moneyjinn.server.model.CreateCapitalsourceRequest;
import org.laladev.moneyjinn.server.model.CreateCapitalsourceResponse;
import org.laladev.moneyjinn.server.model.ErrorResponse;
import org.laladev.moneyjinn.server.model.ShowCapitalsourceListResponse;
import org.laladev.moneyjinn.server.model.UpdateCapitalsourceRequest;
import org.laladev.moneyjinn.server.model.ValidationItemTransport;
import org.laladev.moneyjinn.server.model.ValidationResponse;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
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
public class CapitalsourceController extends AbstractController
    implements CapitalsourceControllerApi {
  private final IAccessRelationService accessRelationService;
  private final ICapitalsourceService capitalsourceService;
  private final IUserService userService;
  private final CapitalsourceTransportMapper capitalsourceTransportMapper;
  private final ValidationItemTransportMapper validationItemTransportMapper;

  @Override
  @PostConstruct
  protected void addBeanMapper() {
    this.registerBeanMapper(this.capitalsourceTransportMapper);
    this.registerBeanMapper(this.validationItemTransportMapper);
  }

  @Override
  public ResponseEntity<ShowCapitalsourceListResponse> showCapitalsourceList() {
    final UserID userId = super.getUserId();
    final List<Capitalsource> capitalsources = this.capitalsourceService
        .getAllCapitalsources(userId);
    final ShowCapitalsourceListResponse response = new ShowCapitalsourceListResponse();
    if (capitalsources != null && !capitalsources.isEmpty()) {
      response
          .setCapitalsourceTransports(super.mapList(capitalsources, CapitalsourceTransport.class));
    }
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<CreateCapitalsourceResponse> createCapitalsource(
      @RequestBody final CreateCapitalsourceRequest request) {
    final UserID userId = super.getUserId();
    final Capitalsource capitalsource = super.map(request.getCapitalsourceTransport(),
        Capitalsource.class);
    final User user = this.userService.getUserById(userId);
    final Group accessor = this.accessRelationService.getAccessor(userId);
    capitalsource.setId(null);
    capitalsource.setUser(user);
    capitalsource.setAccess(accessor);
    final ValidationResult validationResult = this.capitalsourceService
        .validateCapitalsource(capitalsource);

    this.throwValidationExceptionIfInvalid(validationResult);

    final CapitalsourceID capitalsourceId = this.capitalsourceService
        .createCapitalsource(capitalsource);

    final CreateCapitalsourceResponse response = new CreateCapitalsourceResponse();
    response.setCapitalsourceId(capitalsourceId.getId());
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<ValidationResponse> updateCapitalsource(
      @RequestBody final UpdateCapitalsourceRequest request) {
    final UserID userId = super.getUserId();
    final Capitalsource capitalsource = super.map(request.getCapitalsourceTransport(),
        Capitalsource.class);
    final User user = this.userService.getUserById(userId);
    final Group accessor = this.accessRelationService.getAccessor(userId);
    capitalsource.setUser(user);
    capitalsource.setAccess(accessor);
    final ValidationResult validationResult = this.capitalsourceService
        .validateCapitalsource(capitalsource);

    final ValidationResponse response = new ValidationResponse();
    response.setResult(validationResult.isValid());

    if (validationResult.isValid()) {
      this.capitalsourceService.updateCapitalsource(capitalsource);
    } else {
      response.setValidationItemTransports(super.mapList(
          validationResult.getValidationResultItems(), ValidationItemTransport.class));
    }

    return ResponseEntity.ok(response);

  }

  @Override
  public ResponseEntity<ErrorResponse> deleteCapitalsourceById(
      @PathVariable(value = "id") final Long id) {
    final UserID userId = super.getUserId();
    final Group accessor = this.accessRelationService.getAccessor(userId);
    final CapitalsourceID capitalsourceId = new CapitalsourceID(id);

    this.capitalsourceService.deleteCapitalsource(userId, accessor.getId(), capitalsourceId);

    return ResponseEntity.noContent().build();
  }
}
