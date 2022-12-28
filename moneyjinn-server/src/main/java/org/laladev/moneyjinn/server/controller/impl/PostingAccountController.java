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
import org.laladev.moneyjinn.core.rest.model.postingaccount.CreatePostingAccountRequest;
import org.laladev.moneyjinn.core.rest.model.postingaccount.CreatePostingAccountResponse;
import org.laladev.moneyjinn.core.rest.model.postingaccount.ShowPostingAccountListResponse;
import org.laladev.moneyjinn.core.rest.model.postingaccount.UpdatePostingAccountRequest;
import org.laladev.moneyjinn.core.rest.model.transport.PostingAccountTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.model.PostingAccount;
import org.laladev.moneyjinn.model.PostingAccountID;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.server.controller.mapper.PostingAccountTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.ValidationItemTransportMapper;
import org.laladev.moneyjinn.service.api.IPostingAccountService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequestMapping("/moneyflow/server/postingaccount/")
public class PostingAccountController extends AbstractController {
  private static final String RESTRICTION_ALL = "all";
  @Inject
  private IPostingAccountService postingAccountService;

  @Override
  protected void addBeanMapper() {
    this.registerBeanMapper(new PostingAccountTransportMapper());
    this.registerBeanMapper(new ValidationItemTransportMapper());
  }

  @RequestMapping(value = "showPostingAccountList", method = { RequestMethod.GET })
  public ShowPostingAccountListResponse showPostingAccountList() {
    final List<PostingAccount> postingAccounts = this.postingAccountService.getAllPostingAccounts();
    final ShowPostingAccountListResponse response = new ShowPostingAccountListResponse();
    if (postingAccounts != null && !postingAccounts.isEmpty()) {
      response.setPostingAccountTransports(
          super.mapList(postingAccounts, PostingAccountTransport.class));
    }
    return response;
  }

  @RequestMapping(value = "createPostingAccount", method = { RequestMethod.POST })
  public CreatePostingAccountResponse createPostingAccount(
      @RequestBody final CreatePostingAccountRequest request) {
    final PostingAccount postingAccount = super.map(request.getPostingAccountTransport(),
        PostingAccount.class);
    postingAccount.setId(null);
    final ValidationResult validationResult = this.postingAccountService
        .validatePostingAccount(postingAccount);
    final CreatePostingAccountResponse response = new CreatePostingAccountResponse();
    response.setResult(validationResult.isValid());
    if (!validationResult.isValid()) {
      response.setValidationItemTransports(super.mapList(
          validationResult.getValidationResultItems(), ValidationItemTransport.class));
      return response;
    }
    final PostingAccountID postingAccountId = this.postingAccountService
        .createPostingAccount(postingAccount);
    response.setPostingAccountId(postingAccountId.getId());
    return response;
  }

  @RequestMapping(value = "updatePostingAccount", method = { RequestMethod.PUT })
  public ValidationResponse updatePostingAccount(
      @RequestBody final UpdatePostingAccountRequest request) {
    final PostingAccount postingAccount = super.map(request.getPostingAccountTransport(),
        PostingAccount.class);
    final ValidationResult validationResult = this.postingAccountService
        .validatePostingAccount(postingAccount);
    final ValidationResponse response = new ValidationResponse();
    response.setResult(validationResult.isValid());
    if (validationResult.isValid()) {
      this.postingAccountService.updatePostingAccount(postingAccount);
    } else {
      response.setValidationItemTransports(super.mapList(
          validationResult.getValidationResultItems(), ValidationItemTransport.class));
    }
    return response;
  }

  @RequestMapping(value = "deletePostingAccountById/{id}", method = { RequestMethod.DELETE })
  public void deletePostingAccountById(@PathVariable(value = "id") final Long id) {
    final PostingAccountID postingAccountId = new PostingAccountID(id);
    this.postingAccountService.deletePostingAccount(postingAccountId);
  }
}
