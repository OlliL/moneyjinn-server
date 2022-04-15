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

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import org.laladev.moneyjinn.core.rest.model.contractpartner.AbstractContractpartnerResponse;
import org.laladev.moneyjinn.core.rest.model.contractpartner.CreateContractpartnerRequest;
import org.laladev.moneyjinn.core.rest.model.contractpartner.CreateContractpartnerResponse;
import org.laladev.moneyjinn.core.rest.model.contractpartner.ShowContractpartnerListResponse;
import org.laladev.moneyjinn.core.rest.model.contractpartner.ShowCreateContractpartnerResponse;
import org.laladev.moneyjinn.core.rest.model.contractpartner.ShowDeleteContractpartnerResponse;
import org.laladev.moneyjinn.core.rest.model.contractpartner.ShowEditContractpartnerResponse;
import org.laladev.moneyjinn.core.rest.model.contractpartner.UpdateContractpartnerRequest;
import org.laladev.moneyjinn.core.rest.model.transport.ContractpartnerTransport;
import org.laladev.moneyjinn.core.rest.model.transport.PostingAccountTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.model.Contractpartner;
import org.laladev.moneyjinn.model.ContractpartnerID;
import org.laladev.moneyjinn.model.PostingAccount;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.setting.ClientCurrentlyValidContractpartnerSetting;
import org.laladev.moneyjinn.model.setting.ClientMaxRowsSetting;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.server.annotation.RequiresAuthorization;
import org.laladev.moneyjinn.server.controller.mapper.ContractpartnerTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.PostingAccountTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.ValidationItemTransportMapper;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.IContractpartnerAccountService;
import org.laladev.moneyjinn.service.api.IContractpartnerService;
import org.laladev.moneyjinn.service.api.IPostingAccountService;
import org.laladev.moneyjinn.service.api.ISettingService;
import org.laladev.moneyjinn.service.api.IUserService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequestMapping("/moneyflow/server/contractpartner/")
public class ContractpartnerController extends AbstractController {
	private static final String RESTRICTION_ALL = "all";
	@Inject
	private IAccessRelationService accessRelationService;
	@Inject
	private IContractpartnerService contractpartnerService;
	@Inject
	private IContractpartnerAccountService contractpartnerAccountService;
	@Inject
	private IPostingAccountService postingAccountService;
	@Inject
	private ISettingService settingService;
	@Inject
	private IUserService userService;

	@Override
	protected void addBeanMapper() {
		this.registerBeanMapper(new ContractpartnerTransportMapper());
		this.registerBeanMapper(new PostingAccountTransportMapper());
		this.registerBeanMapper(new ValidationItemTransportMapper());
	}

	@RequestMapping(value = "showContractpartnerList/currentlyValid", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowContractpartnerListResponse showContractpartnerList() {
		final UserID userId = super.getUserId();
		final ClientCurrentlyValidContractpartnerSetting setting = this.settingService
				.getClientCurrentlyValidContractpartnerSetting(userId);
		return this.doShowContractpartnerList(userId, null, setting.getSetting());

	}

	@RequestMapping(value = "showContractpartnerList/{restriction}/currentlyValid", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowContractpartnerListResponse showContractpartnerList(
			@PathVariable(value = "restriction") final String restriction) {
		final UserID userId = super.getUserId();
		final ClientCurrentlyValidContractpartnerSetting setting = this.settingService
				.getClientCurrentlyValidContractpartnerSetting(userId);
		return this.doShowContractpartnerList(userId, restriction, setting.getSetting());

	}

	@RequestMapping(value = "showContractpartnerList/currentlyValid/{currentlyValid}", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowContractpartnerListResponse showContractpartnerList(
			@PathVariable(value = "currentlyValid") final boolean currentlyValid) {
		final UserID userId = super.getUserId();
		final ShowContractpartnerListResponse response = this.doShowContractpartnerList(userId, null, currentlyValid);
		final ClientCurrentlyValidContractpartnerSetting setting = new ClientCurrentlyValidContractpartnerSetting(
				currentlyValid);
		this.settingService.setClientCurrentlyValidContractpartnerSetting(userId, setting);

		return response;
	}

	@RequestMapping(value = "showContractpartnerList/{restriction}/currentlyValid/{currentlyValid}", method = {
			RequestMethod.GET })
	@RequiresAuthorization
	public ShowContractpartnerListResponse showContractpartnerList(
			@PathVariable(value = "restriction") final String restriction,
			@PathVariable(value = "currentlyValid") final boolean currentlyValid) {
		final UserID userId = super.getUserId();
		final ShowContractpartnerListResponse response = this.doShowContractpartnerList(userId, restriction,
				currentlyValid);
		final ClientCurrentlyValidContractpartnerSetting setting = new ClientCurrentlyValidContractpartnerSetting(
				currentlyValid);
		this.settingService.setClientCurrentlyValidContractpartnerSetting(userId, setting);

		return response;
	}

	private ShowContractpartnerListResponse doShowContractpartnerList(final UserID userId, final String restriction,
			final boolean currentlyValid) {
		final LocalDate now = LocalDate.now();
		Set<Character> initials;

		if (currentlyValid) {
			initials = this.contractpartnerService.getAllContractpartnerInitialsByDateRange(userId, now, now);
		} else {
			initials = this.contractpartnerService.getAllContractpartnerInitials(userId);
		}

		List<Contractpartner> contractpartners = null;

		if (restriction != null) {
			if (restriction.equals(String.valueOf(RESTRICTION_ALL))) {
				if (currentlyValid) {
					contractpartners = this.contractpartnerService.getAllContractpartnersByDateRange(userId, now, now);
				} else {
					contractpartners = this.contractpartnerService.getAllContractpartners(userId);
				}
			} else if (restriction.length() == 1) {
				if (currentlyValid) {
					contractpartners = this.contractpartnerService.getAllContractpartnersByInitialAndDateRange(userId,
							restriction.toCharArray()[0], now, now);
				} else {
					contractpartners = this.contractpartnerService.getAllContractpartnersByInitial(userId,
							restriction.toCharArray()[0]);
				}
			}
		} else {
			final ClientMaxRowsSetting clientMaxRowsSetting = this.settingService.getClientMaxRowsSetting(userId);
			Integer count;
			if (currentlyValid) {
				count = this.contractpartnerService.countAllContractpartnersByDateRange(userId, now, now);
			} else {
				count = this.contractpartnerService.countAllContractpartners(userId);
			}

			if (clientMaxRowsSetting.getSetting().compareTo(count) >= 0) {
				if (currentlyValid) {
					contractpartners = this.contractpartnerService.getAllContractpartnersByDateRange(userId, now, now);
				} else {
					contractpartners = this.contractpartnerService.getAllContractpartners(userId);
				}
			}
		}

		final ShowContractpartnerListResponse response = new ShowContractpartnerListResponse();
		if (contractpartners != null && !contractpartners.isEmpty()) {
			response.setContractpartnerTransports(super.mapList(contractpartners, ContractpartnerTransport.class));
		}

		if (initials != null && !initials.isEmpty()) {
			response.setInitials(initials);
		}
		response.setCurrentlyValid(currentlyValid);

		return response;
	}

	@RequestMapping(value = "createContractpartner", method = { RequestMethod.POST })
	@RequiresAuthorization
	public CreateContractpartnerResponse createContractpartner(
			@RequestBody final CreateContractpartnerRequest request) {
		final UserID userId = super.getUserId();
		final Contractpartner contractpartner = super.map(request.getContractpartnerTransport(), Contractpartner.class);

		final User user = this.userService.getUserById(userId);
		final Group accessor = this.accessRelationService.getAccessor(userId);

		contractpartner.setId(null);
		contractpartner.setUser(user);
		contractpartner.setAccess(accessor);

		final ValidationResult validationResult = this.contractpartnerService.validateContractpartner(contractpartner);

		final CreateContractpartnerResponse response = new CreateContractpartnerResponse();
		if (!validationResult.isValid()) {
			response.setResult(validationResult.isValid());
			response.setValidationItemTransports(
					super.mapList(validationResult.getValidationResultItems(), ValidationItemTransport.class));
			return response;
		}
		final ContractpartnerID contractpartnerId = this.contractpartnerService.createContractpartner(contractpartner);
		response.setContractpartnerId(contractpartnerId.getId());
		return response;
	}

	@RequestMapping(value = "updateContractpartner", method = { RequestMethod.PUT })
	@RequiresAuthorization
	public ValidationResponse updateContractpartner(@RequestBody final UpdateContractpartnerRequest request) {
		final UserID userId = super.getUserId();
		final Contractpartner contractpartner = super.map(request.getContractpartnerTransport(), Contractpartner.class);
		final User user = this.userService.getUserById(userId);
		final Group accessor = this.accessRelationService.getAccessor(userId);

		contractpartner.setUser(user);
		contractpartner.setAccess(accessor);

		final ValidationResult validationResult = this.contractpartnerService.validateContractpartner(contractpartner);

		if (!validationResult.isValid()) {
			return super.returnValidationResponse(validationResult);
		}

		this.contractpartnerService.updateContractpartner(contractpartner);
		return null;
	}

	@RequestMapping(value = "deleteContractpartner/{id}", method = { RequestMethod.DELETE })
	@RequiresAuthorization
	public void deleteContractpartner(@PathVariable(value = "id") final Long id) {
		final UserID userId = super.getUserId();
		final Group accessor = this.accessRelationService.getAccessor(userId);

		final ContractpartnerID contractpartnerId = new ContractpartnerID(id);

		this.contractpartnerAccountService.deleteContractpartnerAccounts(userId, contractpartnerId);
		this.contractpartnerService.deleteContractpartner(userId, accessor.getId(), contractpartnerId);
	}

	@RequestMapping(value = "showCreateContractpartner", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowCreateContractpartnerResponse showCreateContractpartner() {
		final ShowCreateContractpartnerResponse response = new ShowCreateContractpartnerResponse();
		final List<PostingAccount> postingAccounts = this.postingAccountService.getAllPostingAccounts();
		if (postingAccounts != null && !postingAccounts.isEmpty()) {
			response.setPostingAccountTransports(super.mapList(postingAccounts, PostingAccountTransport.class));
		}
		return response;
	}

	@RequestMapping(value = "showEditContractpartner/{id}", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowEditContractpartnerResponse showEditContractpartner(
			@PathVariable(value = "id") final Long contractpartnerId) {
		final ShowEditContractpartnerResponse response = new ShowEditContractpartnerResponse();
		this.fillAbstractContractpartnerResponse(contractpartnerId, response);
		if (response.getContractpartnerTransport() != null) {
			final List<PostingAccount> postingAccounts = this.postingAccountService.getAllPostingAccounts();
			response.setPostingAccountTransports(super.mapList(postingAccounts, PostingAccountTransport.class));
		}
		return response;
	}

	@RequestMapping(value = "showDeleteContractpartner/{id}", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowDeleteContractpartnerResponse showDeleteContractpartner(
			@PathVariable(value = "id") final Long contractpartnerId) {
		final ShowDeleteContractpartnerResponse response = new ShowDeleteContractpartnerResponse();
		this.fillAbstractContractpartnerResponse(contractpartnerId, response);
		return response;
	}

	private void fillAbstractContractpartnerResponse(final Long id, final AbstractContractpartnerResponse response) {
		final UserID userId = super.getUserId();
		final ContractpartnerID contractpartnerId = new ContractpartnerID(id);
		final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
				contractpartnerId);
		response.setContractpartnerTransport(super.map(contractpartner, ContractpartnerTransport.class));
	}

}
