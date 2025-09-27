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
import org.laladev.moneyjinn.model.ContractpartnerAccount;
import org.laladev.moneyjinn.model.ContractpartnerAccountID;
import org.laladev.moneyjinn.model.ContractpartnerID;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.server.controller.api.CrudContractpartnerAccountControllerApi;
import org.laladev.moneyjinn.server.controller.impl.AbstractController;
import org.laladev.moneyjinn.server.controller.mapper.ContractpartnerAccountTransportMapper;
import org.laladev.moneyjinn.server.model.ContractpartnerAccountTransport;
import org.laladev.moneyjinn.service.api.IContractpartnerAccountService;
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
public class CrudContractpartnerAccountController extends AbstractController
        implements CrudContractpartnerAccountControllerApi {
    private final IContractpartnerAccountService contractpartnerAccountService;
    private final ContractpartnerAccountTransportMapper contractpartnerAccountTransportMapper;

    @Override
    public ResponseEntity<List<ContractpartnerAccountTransport>> readAll(final Long id) {
        final UserID userId = super.getUserId();
        final ContractpartnerID contractpartnerId = new ContractpartnerID(id);
        final List<ContractpartnerAccount> contractpartnerAccounts = this.contractpartnerAccountService
                .getContractpartnerAccounts(userId, contractpartnerId);

        return ResponseEntity.ok(this.contractpartnerAccountTransportMapper.mapAToB(contractpartnerAccounts));
    }

    @Override
    public ResponseEntity<ContractpartnerAccountTransport> create(
            @RequestBody final ContractpartnerAccountTransport contractpartnerAccountTransport,
            @RequestHeader(value = HEADER_PREFER, required = false) final List<String> prefer) {
        final UserID userId = super.getUserId();
        final ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountTransportMapper
                .mapBToA(contractpartnerAccountTransport);
        contractpartnerAccount.setId(null);
        final ValidationResult validationResult = this.contractpartnerAccountService
                .validateContractpartnerAccount(userId, contractpartnerAccount);

        this.throwValidationExceptionIfInvalid(validationResult);

        final ContractpartnerAccountID contractpartnerAccountId = this.contractpartnerAccountService
                .createContractpartnerAccount(userId, contractpartnerAccount);

        contractpartnerAccount.setId(contractpartnerAccountId);

        return this.preferredReturn(prefer,
                () -> this.contractpartnerAccountTransportMapper.mapAToB(contractpartnerAccount));

    }

    @Override
    public ResponseEntity<ContractpartnerAccountTransport> update(
            @RequestBody final ContractpartnerAccountTransport contractpartnerAccountTransport,
            @RequestHeader(value = HEADER_PREFER, required = false) final List<String> prefer) {
        final UserID userId = super.getUserId();
        final ContractpartnerAccount contractpartnerAccount = this.contractpartnerAccountTransportMapper
                .mapBToA(contractpartnerAccountTransport);
        final ValidationResult validationResult = this.contractpartnerAccountService
                .validateContractpartnerAccount(userId, contractpartnerAccount);

        this.throwValidationExceptionIfInvalid(validationResult);

        this.contractpartnerAccountService.updateContractpartnerAccount(userId, contractpartnerAccount);

        return this.preferredReturn(prefer,
                () -> this.contractpartnerAccountTransportMapper.mapAToB(contractpartnerAccount));
    }

    @Override
    public ResponseEntity<Void> delete(final Long id) {
        final UserID userId = super.getUserId();
        final ContractpartnerAccountID contractpartnerAccountId = new ContractpartnerAccountID(id);

        this.contractpartnerAccountService.deleteContractpartnerAccountById(userId, contractpartnerAccountId);

        return ResponseEntity.noContent().build();
    }
}
