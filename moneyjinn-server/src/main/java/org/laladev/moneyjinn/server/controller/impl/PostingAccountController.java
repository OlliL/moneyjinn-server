//Copyright (c) 2015-2025 Oliver Lehmann <lehmann@ans-netz.de>
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
import lombok.RequiredArgsConstructor;
import org.laladev.moneyjinn.model.PostingAccount;
import org.laladev.moneyjinn.model.PostingAccountID;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.server.controller.api.PostingAccountControllerApi;
import org.laladev.moneyjinn.server.controller.mapper.PostingAccountTransportMapper;
import org.laladev.moneyjinn.server.model.CreatePostingAccountRequest;
import org.laladev.moneyjinn.server.model.CreatePostingAccountResponse;
import org.laladev.moneyjinn.server.model.ShowPostingAccountListResponse;
import org.laladev.moneyjinn.server.model.UpdatePostingAccountRequest;
import org.laladev.moneyjinn.service.api.IPostingAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class PostingAccountController extends AbstractController implements PostingAccountControllerApi {
    private final IPostingAccountService postingAccountService;
    private final PostingAccountTransportMapper postingAccountTransportMapper;

    @Override
    public ResponseEntity<ShowPostingAccountListResponse> showPostingAccountList() {
        final List<PostingAccount> postingAccounts = this.postingAccountService.getAllPostingAccounts();
        final ShowPostingAccountListResponse response = new ShowPostingAccountListResponse();
        if (postingAccounts != null && !postingAccounts.isEmpty()) {
            response.setPostingAccountTransports(this.postingAccountTransportMapper.mapAToB(postingAccounts));
        }
        return ResponseEntity.ok(response);
    }

    @Override
    @PreAuthorize(HAS_AUTHORITY_ADMIN)
    public ResponseEntity<CreatePostingAccountResponse> createPostingAccount(
            @RequestBody final CreatePostingAccountRequest request) {
        final PostingAccount postingAccount = this.postingAccountTransportMapper
                .mapBToA(request.getPostingAccountTransport());
        postingAccount.setId(null);
        final ValidationResult validationResult = this.postingAccountService.validatePostingAccount(postingAccount);

        this.throwValidationExceptionIfInvalid(validationResult);

        final PostingAccountID postingAccountId = this.postingAccountService.createPostingAccount(postingAccount);

        final CreatePostingAccountResponse response = new CreatePostingAccountResponse();
        response.setPostingAccountId(postingAccountId.getId());

        return ResponseEntity.ok(response);
    }

    @Override
    @PreAuthorize(HAS_AUTHORITY_ADMIN)
    public ResponseEntity<Void> updatePostingAccount(@RequestBody final UpdatePostingAccountRequest request) {
        final PostingAccount postingAccount = this.postingAccountTransportMapper
                .mapBToA(request.getPostingAccountTransport());
        final ValidationResult validationResult = this.postingAccountService.validatePostingAccount(postingAccount);

        this.throwValidationExceptionIfInvalid(validationResult);

        this.postingAccountService.updatePostingAccount(postingAccount);

        return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize(HAS_AUTHORITY_ADMIN)
    public ResponseEntity<Void> deletePostingAccountById(final Long id) {
        final PostingAccountID postingAccountId = new PostingAccountID(id);
        this.postingAccountService.deletePostingAccount(postingAccountId);

        return ResponseEntity.noContent().build();
    }
}
