//Copyright (c) 2015-2016 Oliver Lehmann <oliver@laladev.org>
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.laladev.moneyjinn.businesslogic.model.Contractpartner;
import org.laladev.moneyjinn.businesslogic.model.PostingAccount;
import org.laladev.moneyjinn.businesslogic.model.PreDefMoneyflow;
import org.laladev.moneyjinn.businesslogic.model.PreDefMoneyflowID;
import org.laladev.moneyjinn.businesslogic.model.access.Group;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.businesslogic.model.moneyflow.Moneyflow;
import org.laladev.moneyjinn.businesslogic.model.moneyflow.MoneyflowID;
import org.laladev.moneyjinn.businesslogic.model.moneyflow.search.MoneyflowSearchParams;
import org.laladev.moneyjinn.businesslogic.model.moneyflow.search.MoneyflowSearchResult;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientNumFreeMoneyflowsSetting;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResult;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.businesslogic.service.api.IAccessRelationService;
import org.laladev.moneyjinn.businesslogic.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.businesslogic.service.api.IContractpartnerService;
import org.laladev.moneyjinn.businesslogic.service.api.IMoneyflowService;
import org.laladev.moneyjinn.businesslogic.service.api.IPostingAccountService;
import org.laladev.moneyjinn.businesslogic.service.api.IPreDefMoneyflowService;
import org.laladev.moneyjinn.businesslogic.service.api.ISettingService;
import org.laladev.moneyjinn.businesslogic.service.api.IUserService;
import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.moneyflow.AbstractAddMoneyflowResponse;
import org.laladev.moneyjinn.core.rest.model.moneyflow.AbstractEditMoneyflowResponse;
import org.laladev.moneyjinn.core.rest.model.moneyflow.CreateMoneyflowsRequest;
import org.laladev.moneyjinn.core.rest.model.moneyflow.CreateMoneyflowsResponse;
import org.laladev.moneyjinn.core.rest.model.moneyflow.SearchMoneyflowsRequest;
import org.laladev.moneyjinn.core.rest.model.moneyflow.SearchMoneyflowsResponse;
import org.laladev.moneyjinn.core.rest.model.moneyflow.ShowAddMoneyflowsResponse;
import org.laladev.moneyjinn.core.rest.model.moneyflow.ShowDeleteMoneyflowResponse;
import org.laladev.moneyjinn.core.rest.model.moneyflow.ShowEditMoneyflowResponse;
import org.laladev.moneyjinn.core.rest.model.moneyflow.ShowSearchMoneyflowFormResponse;
import org.laladev.moneyjinn.core.rest.model.moneyflow.UpdateMoneyflowRequest;
import org.laladev.moneyjinn.core.rest.model.moneyflow.UpdateMoneyflowResponse;
import org.laladev.moneyjinn.core.rest.model.moneyflow.transport.MoneyflowSearchResultTransport;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ContractpartnerTransport;
import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowTransport;
import org.laladev.moneyjinn.core.rest.model.transport.PostingAccountTransport;
import org.laladev.moneyjinn.core.rest.model.transport.PreDefMoneyflowTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.server.annotation.RequiresAuthorization;
import org.laladev.moneyjinn.server.controller.mapper.CapitalsourceTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.ContractpartnerTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.MoneyflowSearchParamsTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.MoneyflowSearchResultTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.MoneyflowTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.PostingAccountTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.PreDefMoneyflowTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.ValidationItemTransportMapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequestMapping("/moneyflow/server/moneyflow/")
public class MoneyflowController extends AbstractController {
	@Inject
	private IUserService userService;
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
	@Inject
	private IMoneyflowService moneyflowService;

	@Override
	protected void addBeanMapper() {
		this.registerBeanMapper(new CapitalsourceTransportMapper());
		this.registerBeanMapper(new ContractpartnerTransportMapper());
		this.registerBeanMapper(new PostingAccountTransportMapper());
		this.registerBeanMapper(new PreDefMoneyflowTransportMapper());
		this.registerBeanMapper(new MoneyflowTransportMapper());
		this.registerBeanMapper(new ValidationItemTransportMapper());
		this.registerBeanMapper(new MoneyflowSearchParamsTransportMapper());
		this.registerBeanMapper(new MoneyflowSearchResultTransportMapper());
	}

	private void fillAbstractAddMoneyflowResponse(final UserID userId, final AbstractAddMoneyflowResponse response) {
		final LocalDate today = LocalDate.now();

		final List<Capitalsource> capitalsources = this.capitalsourceService
				.getGroupBookableCapitalsourcesByDateRange(userId, today, today);
		if (capitalsources != null && !capitalsources.isEmpty()) {
			response.setCapitalsourceTransports(super.mapList(capitalsources, CapitalsourceTransport.class));
		}

		final List<Contractpartner> contractpartner = this.contractpartnerService
				.getAllContractpartnersByDateRange(userId, today, today);
		if (contractpartner != null && !contractpartner.isEmpty()) {
			response.setContractpartnerTransports(super.mapList(contractpartner, ContractpartnerTransport.class));
		}

		final List<PostingAccount> postingAccounts = this.postingAccountService.getAllPostingAccounts();
		if (postingAccounts != null && !postingAccounts.isEmpty()) {
			response.setPostingAccountTransports(super.mapList(postingAccounts, PostingAccountTransport.class));
		}

		List<PreDefMoneyflow> preDefMoneyflows = this.preDefMoneyflowService.getAllPreDefMoneyflows(userId);
		if (preDefMoneyflows != null && !preDefMoneyflows.isEmpty()) {
			preDefMoneyflows = preDefMoneyflows.stream().filter(pdm -> !this.isOnceAMonthAndAlreadyUsed(today, pdm))
					.collect(Collectors.toCollection(ArrayList::new));
			response.setPreDefMoneyflowTransports(super.mapList(preDefMoneyflows, PreDefMoneyflowTransport.class));
		}

		final ClientNumFreeMoneyflowsSetting clientNumFreeMoneyflowsSetting = this.settingService
				.getClientNumFreeMoneyflowsSetting(userId);
		response.setSettingNumberOfFreeMoneyflows(clientNumFreeMoneyflowsSetting.getSetting());
	}

	private boolean isOnceAMonthAndAlreadyUsed(final LocalDate today, final PreDefMoneyflow preDefMoneyflow) {
		final LocalDate lastUsedDate = preDefMoneyflow.getLastUsedDate();

		return preDefMoneyflow.isOnceAMonth() //
				&& lastUsedDate != null && lastUsedDate.getMonth().equals(today.getMonth()) //
				&& lastUsedDate.getYear() == today.getYear();
	}

	private void fillAbstractEditMoneyflowResponse(final Moneyflow moneyflow,
			final AbstractEditMoneyflowResponse response) {
		Assert.notNull(moneyflow.getUser());

		final UserID userId = moneyflow.getUser().getId();

		final List<Capitalsource> capitalsources = this.capitalsourceService.getGroupBookableCapitalsources(userId);
		response.setCapitalsourceTransports(super.mapList(capitalsources, CapitalsourceTransport.class));

		final List<Contractpartner> contractpartner = this.contractpartnerService.getAllContractpartners(userId);
		response.setContractpartnerTransports(super.mapList(contractpartner, ContractpartnerTransport.class));

		final List<PostingAccount> postingAccounts = this.postingAccountService.getAllPostingAccounts();
		response.setPostingAccountTransports(super.mapList(postingAccounts, PostingAccountTransport.class));
	}

	@RequestMapping(value = "showAddMoneyflows", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowAddMoneyflowsResponse showAddMoneyflows() {
		final UserID userId = super.getUserId();
		final ShowAddMoneyflowsResponse response = new ShowAddMoneyflowsResponse();

		this.fillAbstractAddMoneyflowResponse(userId, response);

		return response;
	}

	@RequestMapping(value = "showEditMoneyflow/{id}", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowEditMoneyflowResponse showEditMoneyflow(@PathVariable(value = "id") final Long id) {

		final UserID userId = super.getUserId();
		final ShowEditMoneyflowResponse response = new ShowEditMoneyflowResponse();

		final Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId, new MoneyflowID(id));

		if (moneyflow != null && moneyflow.getUser().getId().equals(userId)) {
			response.setMoneyflowTransport(super.map(moneyflow, MoneyflowTransport.class));
			this.fillAbstractEditMoneyflowResponse(moneyflow, response);
		}

		return response;
	}

	@RequestMapping(value = "showDeleteMoneyflow/{id}", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowDeleteMoneyflowResponse showDeleteMoneyflow(@PathVariable(value = "id") final Long id) {
		final UserID userId = super.getUserId();
		final MoneyflowID moneyflowId = new MoneyflowID(id);
		final ShowDeleteMoneyflowResponse response = new ShowDeleteMoneyflowResponse();

		final Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);
		if (moneyflow != null && moneyflow.getUser().getId().equals(userId)) {
			response.setMoneyflowTransport(super.map(moneyflow, MoneyflowTransport.class));
		}

		return response;
	}

	@RequestMapping(value = "showSearchMoneyflowForm", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowSearchMoneyflowFormResponse showSearchMoneyflowForm() {
		final UserID userId = super.getUserId();
		final ShowSearchMoneyflowFormResponse response = new ShowSearchMoneyflowFormResponse();

		final List<Contractpartner> contractpartner = this.contractpartnerService.getAllContractpartners(userId);
		if (contractpartner != null && !contractpartner.isEmpty()) {
			final List<ContractpartnerTransport> contractpartnerTransports = super.mapList(contractpartner,
					ContractpartnerTransport.class);
			response.setContractpartnerTransports(contractpartnerTransports);
		}

		final List<PostingAccount> postingAccounts = this.postingAccountService.getAllPostingAccounts();
		if (postingAccounts != null && !postingAccounts.isEmpty()) {
			final List<PostingAccountTransport> postingAccountTransports = super.mapList(postingAccounts,
					PostingAccountTransport.class);
			response.setPostingAccountTransports(postingAccountTransports);
		}

		return response;
	}

	@RequestMapping(value = "searchMoneyflows", method = { RequestMethod.PUT })
	@RequiresAuthorization
	public SearchMoneyflowsResponse searchMoneyflows(@RequestBody final SearchMoneyflowsRequest request) {
		final UserID userId = super.getUserId();

		final SearchMoneyflowsResponse response = new SearchMoneyflowsResponse();

		final MoneyflowSearchParams moneyflowSearchParams = super.map(request.getMoneyflowSearchParamsTransport(),
				MoneyflowSearchParams.class);

		final ValidationResult validationResult = new ValidationResult();

		if (moneyflowSearchParams == null || (moneyflowSearchParams.getContractpartnerId() == null
				&& moneyflowSearchParams.getPostingAccountId() == null
				&& moneyflowSearchParams.getSearchString() == null)) {
			validationResult
					.addValidationResultItem(new ValidationResultItem(null, ErrorCode.NO_SEARCH_CRITERIA_ENTERED));
		}
		if (moneyflowSearchParams == null
				|| (moneyflowSearchParams.getGroupBy1() == null && moneyflowSearchParams.getGroupBy2() == null)) {
			validationResult
					.addValidationResultItem(new ValidationResultItem(null, ErrorCode.NO_GROUPING_CRITERIA_GIVEN));
		}

		if (!validationResult.isValid()) {
			response.setResult(false);
			response.setValidationItemTransports(
					super.mapList(validationResult.getValidationResultItems(), ValidationItemTransport.class));
		} else {
			final List<MoneyflowSearchResult> moneyflowSearchResults = this.moneyflowService.searchMoneyflows(userId,
					moneyflowSearchParams);
			if (moneyflowSearchResults != null && !moneyflowSearchResults.isEmpty()) {

				final List<MoneyflowSearchResultTransport> moneyflowSearchResultTransports = super.mapList(
						moneyflowSearchResults, MoneyflowSearchResultTransport.class);
				response.setMoneyflowSearchResultTransports(moneyflowSearchResultTransports);
			}
		}

		final List<Contractpartner> contractpartner = this.contractpartnerService.getAllContractpartners(userId);
		if (contractpartner != null && !contractpartner.isEmpty()) {
			final List<ContractpartnerTransport> contractpartnerTransports = super.mapList(contractpartner,
					ContractpartnerTransport.class);
			response.setContractpartnerTransports(contractpartnerTransports);
		}

		final List<PostingAccount> postingAccounts = this.postingAccountService.getAllPostingAccounts();
		if (postingAccounts != null && !postingAccounts.isEmpty()) {
			final List<PostingAccountTransport> postingAccountTransports = super.mapList(postingAccounts,
					PostingAccountTransport.class);
			response.setPostingAccountTransports(postingAccountTransports);
		}

		return response;
	}

	@RequestMapping(value = "createMoneyflows", method = { RequestMethod.POST })
	@RequiresAuthorization
	public CreateMoneyflowsResponse createMoneyflows(@RequestBody final CreateMoneyflowsRequest request) {
		final UserID userId = super.getUserId();

		final List<Moneyflow> moneyflows = super.mapList(request.getMoneyflowTransports(), Moneyflow.class);
		final List<Long> preDefMoneyflowIds = request.getUsedPreDefMoneyflowIds();

		final User user = this.userService.getUserById(userId);
		final Group group = this.accessRelationService.getAccessor(userId);

		final ValidationResult validationResult = new ValidationResult();
		moneyflows.stream().forEach(mf -> {
			mf.setUser(user);
			mf.setGroup(group);
			validationResult.mergeValidationResult(this.moneyflowService.validateMoneyflow(mf));
		});

		final CreateMoneyflowsResponse response = new CreateMoneyflowsResponse();

		if (validationResult.isValid()) {
			this.moneyflowService.createMoneyflows(moneyflows);
			if (preDefMoneyflowIds != null) {
				preDefMoneyflowIds.stream()
						.forEach(id -> this.preDefMoneyflowService.setLastUsedDate(userId, new PreDefMoneyflowID(id)));
			}
			response.setResult(true);
		} else {
			response.setResult(false);
			response.setValidationItemTransports(
					super.mapList(validationResult.getValidationResultItems(), ValidationItemTransport.class));
		}

		// important to do it here and not before as first, LastUsed of any used PreDefMoneyflow
		// must have been updated
		this.fillAbstractAddMoneyflowResponse(userId, response);

		return response;
	}

	@RequestMapping(value = "updateMoneyflow", method = { RequestMethod.PUT })
	@RequiresAuthorization
	public UpdateMoneyflowResponse updateMoneyflow(@RequestBody final UpdateMoneyflowRequest request) {
		final UserID userId = super.getUserId();
		final User user = this.userService.getUserById(userId);
		final Group group = this.accessRelationService.getAccessor(userId);

		final Moneyflow moneyflow = super.map(request.getMoneyflowTransport(), Moneyflow.class);
		moneyflow.setUser(user);
		moneyflow.setGroup(group);

		final ValidationResult validationResult = this.moneyflowService.validateMoneyflow(moneyflow);

		if (validationResult.isValid()) {
			this.moneyflowService.updateMoneyflow(moneyflow);
		} else {
			final UpdateMoneyflowResponse response = new UpdateMoneyflowResponse();
			this.fillAbstractEditMoneyflowResponse(moneyflow, response);
			response.setResult(validationResult.isValid());
			response.setValidationItemTransports(
					super.mapList(validationResult.getValidationResultItems(), ValidationItemTransport.class));
			return response;
		}

		return null;
	}

	@RequestMapping(value = "deleteMoneyflowById/{id}", method = { RequestMethod.DELETE })
	@RequiresAuthorization
	public void deleteMoneyflowById(@PathVariable(value = "id") final Long id) {
		final UserID userId = super.getUserId();
		final MoneyflowID moneyflowId = new MoneyflowID(id);
		this.moneyflowService.deleteMoneyflow(userId, moneyflowId);
	}
}