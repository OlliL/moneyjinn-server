//Copyright (c) 2015 Oliver Lehmann <oliver@laladev.org>
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

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.laladev.moneyjinn.businesslogic.model.PostingAccount;
import org.laladev.moneyjinn.businesslogic.model.PostingAccountID;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientMaxRowsSetting;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResult;
import org.laladev.moneyjinn.businesslogic.service.api.IPostingAccountService;
import org.laladev.moneyjinn.businesslogic.service.api.ISettingService;
import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import org.laladev.moneyjinn.core.rest.model.postingaccount.AbstractPostingAccountResponse;
import org.laladev.moneyjinn.core.rest.model.postingaccount.CreatePostingAccountRequest;
import org.laladev.moneyjinn.core.rest.model.postingaccount.ShowDeletePostingAccountResponse;
import org.laladev.moneyjinn.core.rest.model.postingaccount.ShowEditPostingAccountResponse;
import org.laladev.moneyjinn.core.rest.model.postingaccount.ShowPostingAccountListResponse;
import org.laladev.moneyjinn.core.rest.model.postingaccount.UpdatePostingAccountRequest;
import org.laladev.moneyjinn.core.rest.model.transport.PostingAccountTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.server.annotation.RequiresAuthorization;
import org.laladev.moneyjinn.server.annotation.RequiresPermissionAdmin;
import org.laladev.moneyjinn.server.controller.mapper.PostingAccountTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.ValidationItemTransportMapper;
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
	@Inject
	private IPostingAccountService postingAccountService;
	@Inject
	private ISettingService settingService;

	@Override
	protected void addBeanMapper() {
		this.registerBeanMapper(new PostingAccountTransportMapper());
		this.registerBeanMapper(new ValidationItemTransportMapper());
	}

	@RequestMapping(value = "showPostingAccountList", method = { RequestMethod.GET })
	@RequiresAuthorization
	@RequiresPermissionAdmin
	public ShowPostingAccountListResponse showPostingAccountList() {
		return this.showPostingAccountList(null);
	}

	@RequestMapping(value = "showPostingAccountList/{restriction}", method = { RequestMethod.GET })
	@RequiresAuthorization
	@RequiresPermissionAdmin
	public ShowPostingAccountListResponse showPostingAccountList(
			@PathVariable(value = "restriction") final String restriction) {
		final UserID userID = super.getUserId();
		final ClientMaxRowsSetting clientMaxRowsSetting = this.settingService.getClientMaxRowsSetting(userID);
		final Set<Character> initials = this.postingAccountService.getAllPostingAccountInitials();
		final Integer count = this.postingAccountService.countAllPostingAccounts();

		List<PostingAccount> postingAccounts = null;

		if ((restriction != null && restriction.equals(String.valueOf("all")))
				|| (restriction == null && clientMaxRowsSetting.getSetting().compareTo(count) >= 0)) {
			postingAccounts = this.postingAccountService.getAllPostingAccounts();
		} else if (restriction != null && restriction.length() == 1) {
			postingAccounts = this.postingAccountService.getAllPostingAccountsByInitial(restriction.toCharArray()[0]);
		}

		final ShowPostingAccountListResponse response = new ShowPostingAccountListResponse();
		if (postingAccounts != null && !postingAccounts.isEmpty()) {
			response.setPostingAccountTransports(super.mapList(postingAccounts, PostingAccountTransport.class));
		}

		if (initials != null && initials.size() > 0) {
			response.setInitials(initials);
		}

		return response;
	}

	@RequestMapping(value = "createPostingAccount", method = { RequestMethod.POST })
	@RequiresAuthorization
	@RequiresPermissionAdmin
	public ValidationResponse createPostingAccount(@RequestBody final CreatePostingAccountRequest request) {
		final PostingAccount postingAccount = super.map(request.getPostingAccountTransport(), PostingAccount.class);
		postingAccount.setId(null);

		final ValidationResult validationResult = this.postingAccountService.validatePostingAccount(postingAccount);

		if (!validationResult.isValid()) {
			final ValidationResponse response = new ValidationResponse();
			response.setResult(validationResult.isValid());
			response.setValidationItemTransports(
					super.mapList(validationResult.getValidationResultItems(), ValidationItemTransport.class));
			return response;
		}
		this.postingAccountService.createPostingAccount(postingAccount);
		return null;
	}

	@RequestMapping(value = "updatePostingAccount", method = { RequestMethod.PUT })
	@RequiresAuthorization
	@RequiresPermissionAdmin
	public ValidationResponse updatePostingAccount(@RequestBody final UpdatePostingAccountRequest request) {
		final PostingAccount postingAccount = super.map(request.getPostingAccountTransport(), PostingAccount.class);

		final ValidationResult validationResult = this.postingAccountService.validatePostingAccount(postingAccount);

		if (validationResult.isValid()) {
			this.postingAccountService.updatePostingAccount(postingAccount);
		} else {
			final ValidationResponse response = new ValidationResponse();
			response.setResult(validationResult.isValid());
			response.setValidationItemTransports(
					super.mapList(validationResult.getValidationResultItems(), ValidationItemTransport.class));
			return response;
		}

		return null;
	}

	@RequestMapping(value = "deletePostingAccountById/{id}", method = { RequestMethod.DELETE })
	@RequiresAuthorization
	@RequiresPermissionAdmin
	public void deletePostingAccountById(@PathVariable(value = "id") final Long id) {
		final PostingAccountID postingAccountId = new PostingAccountID(id);
		this.postingAccountService.deletePostingAccount(postingAccountId);
	}

	@RequestMapping(value = "showEditPostingAccount/{id}", method = { RequestMethod.GET })
	@RequiresAuthorization
	@RequiresPermissionAdmin
	public ShowEditPostingAccountResponse showEditPostingAccount(
			@PathVariable(value = "id") final Long postingAccountId) {
		final ShowEditPostingAccountResponse response = new ShowEditPostingAccountResponse();
		this.fillAbstractPostingAccountResponse(postingAccountId, response);
		return response;
	}

	@RequestMapping(value = "showDeletePostingAccount/{id}", method = { RequestMethod.GET })
	@RequiresAuthorization
	@RequiresPermissionAdmin
	public ShowDeletePostingAccountResponse showDeletePostingAccount(
			@PathVariable(value = "id") final Long postingAccountId) {
		final ShowDeletePostingAccountResponse response = new ShowDeletePostingAccountResponse();
		this.fillAbstractPostingAccountResponse(postingAccountId, response);
		return response;
	}

	private void fillAbstractPostingAccountResponse(final Long postingAccountId,
			final AbstractPostingAccountResponse response) {
		final PostingAccount postingAccount = this.postingAccountService
				.getPostingAccountById(new PostingAccountID(postingAccountId));
		response.setPostingAccountTransport(super.map(postingAccount, PostingAccountTransport.class));
	}
}
