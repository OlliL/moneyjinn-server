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
import org.laladev.moneyjinn.model.ContractpartnerMatching;
import org.laladev.moneyjinn.model.ContractpartnerMatchingID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.server.controller.api.CrudContractpartnerMatchingControllerApi;
import org.laladev.moneyjinn.server.controller.impl.AbstractController;
import org.laladev.moneyjinn.server.controller.mapper.ContractpartnerMatchingTransportMapper;
import org.laladev.moneyjinn.server.model.ContractpartnerMatchingTransport;
import org.laladev.moneyjinn.service.api.IContractpartnerMatchingService;
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
public class CrudContractpartnerMatchingController extends AbstractController
        implements CrudContractpartnerMatchingControllerApi {
    private final IContractpartnerMatchingService contractpartnerMatchingService;
    private final ContractpartnerMatchingTransportMapper contractpartnerMatchingTransportMapper;

    @Override
    public ResponseEntity<List<ContractpartnerMatchingTransport>> readAll() {
        final UserID userId = super.getUserId();
        final List<ContractpartnerMatching> contractpartnerMatchings = this.contractpartnerMatchingService
                .getContractpartnerMatchings(userId);

        return ResponseEntity.ok(this.contractpartnerMatchingTransportMapper.mapAToB(contractpartnerMatchings));
    }

    @Override
    public ResponseEntity<ContractpartnerMatchingTransport> create(
            @RequestBody final ContractpartnerMatchingTransport contractpartnerMatchingTransport,
            @RequestHeader(value = HEADER_PREFER, required = false) final List<String> prefer) {
        final UserID userId = super.getUserId();
        final ContractpartnerMatching contractpartnerMatching = this.contractpartnerMatchingTransportMapper
                .mapBToA(contractpartnerMatchingTransport);
        contractpartnerMatching.setId(null);
        final ValidationResult validationResult = this.contractpartnerMatchingService
                .validateContractpartnerMatching(userId, contractpartnerMatching);

        this.throwValidationExceptionIfInvalid(validationResult);

        final ContractpartnerMatchingID contractpartnerMatchingId = this.contractpartnerMatchingService
                .createContractpartnerMatching(userId, contractpartnerMatching);

        contractpartnerMatching.setId(contractpartnerMatchingId);

        return this.preferredReturn(prefer,
                () -> this.contractpartnerMatchingTransportMapper.mapAToB(contractpartnerMatching));

    }

    @Override
    public ResponseEntity<ContractpartnerMatchingTransport> update(
            @RequestBody final ContractpartnerMatchingTransport contractpartnerMatchingTransport,
            @RequestHeader(value = HEADER_PREFER, required = false) final List<String> prefer) {
        final UserID userId = super.getUserId();
        final ContractpartnerMatching contractpartnerMatching = this.contractpartnerMatchingTransportMapper
                .mapBToA(contractpartnerMatchingTransport);
        final ValidationResult validationResult = this.contractpartnerMatchingService
                .validateContractpartnerMatching(userId, contractpartnerMatching);

        this.throwValidationExceptionIfInvalid(validationResult);

        this.contractpartnerMatchingService.updateContractpartnerMatching(userId, contractpartnerMatching);

        return this.preferredReturn(prefer,
                () -> this.contractpartnerMatchingTransportMapper.mapAToB(contractpartnerMatching));
    }

    @Override
    public ResponseEntity<Void> delete(final Long id) {
        final UserID userId = super.getUserId();
        final ContractpartnerMatchingID contractpartnerMatchingId = new ContractpartnerMatchingID(id);

        this.contractpartnerMatchingService.deleteContractpartnerMatchingById(userId, contractpartnerMatchingId);

        return ResponseEntity.noContent().build();
    }
}
