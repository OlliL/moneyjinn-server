//Copyright (c) 2023-2025 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.server.controller.impl.crud;

import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.server.controller.api.CrudCapitalsourceControllerApi;
import org.laladev.moneyjinn.server.controller.impl.AbstractController;
import org.laladev.moneyjinn.server.controller.mapper.CapitalsourceTransportMapper;
import org.laladev.moneyjinn.server.model.CapitalsourceTransport;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class CrudCapitalsourceController extends AbstractController implements CrudCapitalsourceControllerApi {
    private final IAccessRelationService accessRelationService;
    private final ICapitalsourceService capitalsourceService;
    private final IUserService userService;
    private final CapitalsourceTransportMapper capitalsourceTransportMapper;

    @Override
    public ResponseEntity<List<CapitalsourceTransport>> readAll() {
        final UserID userId = super.getUserId();
        final List<Capitalsource> capitalsources = this.capitalsourceService.getAllCapitalsources(userId);

        return ResponseEntity.ok(this.capitalsourceTransportMapper.mapAToB(capitalsources));
    }

    @Override
    public ResponseEntity<CapitalsourceTransport> readOne(final Long id) {
        final UserID userId = super.getUserId();
        final Group group = this.accessRelationService.getCurrentGroup(userId);
        final CapitalsourceID capitalsourceId = new CapitalsourceID(id);
        final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, group.getId(),
                capitalsourceId);

        if (capitalsource == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(this.capitalsourceTransportMapper.mapAToB(capitalsource));
    }

    @Override
    public ResponseEntity<CapitalsourceTransport> create(
            @RequestBody final CapitalsourceTransport capitalsourceTransport,
            @RequestHeader(value = HEADER_PREFER, required = false) final List<String> prefer) {
        final UserID userId = super.getUserId();
        final Capitalsource capitalsource = this.capitalsourceTransportMapper.mapBToA(capitalsourceTransport);
        final User user = this.userService.getUserById(userId);
        final Group group = this.accessRelationService.getCurrentGroup(userId);
        capitalsource.setId(null);
        capitalsource.setUser(user);
        capitalsource.setGroup(group);
        final ValidationResult validationResult = this.capitalsourceService.validateCapitalsource(capitalsource);

        this.throwValidationExceptionIfInvalid(validationResult);

        final CapitalsourceID capitalsourceId = this.capitalsourceService.createCapitalsource(capitalsource);

        capitalsource.setId(capitalsourceId);
        return this.preferredReturn(prefer, () -> this.capitalsourceTransportMapper.mapAToB(capitalsource));

    }

    @Override
    public ResponseEntity<CapitalsourceTransport> update(
            @RequestBody final CapitalsourceTransport capitalsourceTransport,
            @RequestHeader(value = HEADER_PREFER, required = false) final List<String> prefer) {
        final UserID userId = super.getUserId();
        final Capitalsource capitalsource = this.capitalsourceTransportMapper.mapBToA(capitalsourceTransport);
        final User user = this.userService.getUserById(userId);
        final Group group = this.accessRelationService.getCurrentGroup(userId);
        capitalsource.setUser(user);
        capitalsource.setGroup(group);
        final ValidationResult validationResult = this.capitalsourceService.validateCapitalsource(capitalsource);

        this.throwValidationExceptionIfInvalid(validationResult);

        this.capitalsourceService.updateCapitalsource(capitalsource);

        return this.preferredReturn(prefer, () -> this.capitalsourceTransportMapper.mapAToB(capitalsource));
    }

    @Override
    public ResponseEntity<Void> delete(final Long id) {
        final UserID userId = super.getUserId();
        final Group group = this.accessRelationService.getCurrentGroup(userId);
        final CapitalsourceID capitalsourceId = new CapitalsourceID(id);

        this.capitalsourceService.deleteCapitalsource(userId, group.getId(), capitalsourceId);

        return ResponseEntity.noContent().build();
    }
}
