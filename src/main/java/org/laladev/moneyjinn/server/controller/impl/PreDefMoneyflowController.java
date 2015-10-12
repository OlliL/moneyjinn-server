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

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.laladev.moneyjinn.businesslogic.model.Contractpartner;
import org.laladev.moneyjinn.businesslogic.model.PostingAccount;
import org.laladev.moneyjinn.businesslogic.model.PreDefMoneyflow;
import org.laladev.moneyjinn.businesslogic.model.PreDefMoneyflowID;
import org.laladev.moneyjinn.businesslogic.model.access.GroupID;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientMaxRowsSetting;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResult;
import org.laladev.moneyjinn.businesslogic.service.api.IAccessRelationService;
import org.laladev.moneyjinn.businesslogic.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.businesslogic.service.api.IContractpartnerService;
import org.laladev.moneyjinn.businesslogic.service.api.IPostingAccountService;
import org.laladev.moneyjinn.businesslogic.service.api.IPreDefMoneyflowService;
import org.laladev.moneyjinn.businesslogic.service.api.ISettingService;
import org.laladev.moneyjinn.core.rest.model.predefmoneyflow.AbstractCreatePreDefMoneyflowResponse;
import org.laladev.moneyjinn.core.rest.model.predefmoneyflow.CreatePreDefMoneyflowRequest;
import org.laladev.moneyjinn.core.rest.model.predefmoneyflow.CreatePreDefMoneyflowResponse;
import org.laladev.moneyjinn.core.rest.model.predefmoneyflow.ShowCreatePreDefMoneyflowResponse;
import org.laladev.moneyjinn.core.rest.model.predefmoneyflow.ShowDeletePreDefMoneyflowResponse;
import org.laladev.moneyjinn.core.rest.model.predefmoneyflow.ShowEditPreDefMoneyflowResponse;
import org.laladev.moneyjinn.core.rest.model.predefmoneyflow.ShowPreDefMoneyflowListResponse;
import org.laladev.moneyjinn.core.rest.model.predefmoneyflow.UpdatePreDefMoneyflowRequest;
import org.laladev.moneyjinn.core.rest.model.predefmoneyflow.UpdatePreDefMoneyflowResponse;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ContractpartnerTransport;
import org.laladev.moneyjinn.core.rest.model.transport.PostingAccountTransport;
import org.laladev.moneyjinn.core.rest.model.transport.PreDefMoneyflowTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.server.annotation.RequiresAuthorization;
import org.laladev.moneyjinn.server.controller.mapper.CapitalsourceTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.ContractpartnerTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.PostingAccountTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.PreDefMoneyflowTransportMapper;
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
@RequestMapping("/moneyflow/server/predefmoneyflow/")
public class PreDefMoneyflowController extends AbstractController {
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
		this.registerBeanMapper(new CapitalsourceTransportMapper());
		this.registerBeanMapper(new ContractpartnerTransportMapper());
		this.registerBeanMapper(new PostingAccountTransportMapper());
		this.registerBeanMapper(new PreDefMoneyflowTransportMapper());
		this.registerBeanMapper(new ValidationItemTransportMapper());
	}

	@RequestMapping(value = "showPreDefMoneyflowList", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowPreDefMoneyflowListResponse showPreDefMoneyflowList() {
		return this.showPreDefMoneyflowList(null);
	}

	@RequestMapping(value = "showPreDefMoneyflowList/{restriction}", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowPreDefMoneyflowListResponse showPreDefMoneyflowList(
			@PathVariable(value = "restriction") final String restriction) {
		final UserID userId = super.getUserId();
		final ClientMaxRowsSetting clientMaxRowsSetting = this.settingService.getClientMaxRowsSetting(userId);
		final Set<Character> initials = this.preDefMoneyflowService.getAllPreDefMoneyflowInitials(userId);
		final Integer count = this.preDefMoneyflowService.countAllPreDefMoneyflows(userId);

		List<PreDefMoneyflow> preDefMoneyflows = null;

		if ((restriction != null && restriction.equals(String.valueOf("all")))
				|| (restriction == null && clientMaxRowsSetting.getSetting().compareTo(count) >= 0)) {
			preDefMoneyflows = this.preDefMoneyflowService.getAllPreDefMoneyflows(userId);
		} else if (restriction != null && restriction.length() == 1) {
			preDefMoneyflows = this.preDefMoneyflowService.getAllPreDefMoneyflowsByInitial(userId,
					restriction.toCharArray()[0]);
		}

		final ShowPreDefMoneyflowListResponse response = new ShowPreDefMoneyflowListResponse();
		if (preDefMoneyflows != null && !preDefMoneyflows.isEmpty()) {
			response.setPreDefMoneyflowTransports(super.mapList(preDefMoneyflows, PreDefMoneyflowTransport.class));
		}
		if (initials != null && initials.size() > 0) {
			response.setInitials(initials);
		}

		return response;
	}

	@RequestMapping(value = "createPreDefMoneyflow", method = { RequestMethod.POST })
	@RequiresAuthorization
	public CreatePreDefMoneyflowResponse createPreDefMoneyflow(
			@RequestBody final CreatePreDefMoneyflowRequest request) {
		final PreDefMoneyflow preDefMoneyflow = super.map(request.getPreDefMoneyflowTransport(), PreDefMoneyflow.class);
		final UserID userId = super.getUserId();

		preDefMoneyflow.setId(null);
		preDefMoneyflow.setUser(new User(userId));

		final ValidationResult validationResult = this.preDefMoneyflowService.validatePreDefMoneyflow(preDefMoneyflow);

		if (!validationResult.isValid()) {
			final CreatePreDefMoneyflowResponse response = new CreatePreDefMoneyflowResponse();
			this.fillAbstractCreatePreDefMoneyflowResponse(userId, response, preDefMoneyflow);
			response.setResult(validationResult.isValid());
			response.setValidationItemTransports(
					super.mapList(validationResult.getValidationResultItems(), ValidationItemTransport.class));
			return response;
		}
		this.preDefMoneyflowService.createPreDefMoneyflow(preDefMoneyflow);
		return null;
	}

	@RequestMapping(value = "updatePreDefMoneyflow", method = { RequestMethod.PUT })
	@RequiresAuthorization
	public UpdatePreDefMoneyflowResponse updatePreDefMoneyflow(
			@RequestBody final UpdatePreDefMoneyflowRequest request) {
		final PreDefMoneyflow preDefMoneyflow = super.map(request.getPreDefMoneyflowTransport(), PreDefMoneyflow.class);
		final UserID userId = super.getUserId();
		preDefMoneyflow.setUser(new User(userId));

		final ValidationResult validationResult = this.preDefMoneyflowService.validatePreDefMoneyflow(preDefMoneyflow);

		if (validationResult.isValid()) {
			this.preDefMoneyflowService.updatePreDefMoneyflow(preDefMoneyflow);
		} else {
			final UpdatePreDefMoneyflowResponse response = new UpdatePreDefMoneyflowResponse();
			this.fillAbstractCreatePreDefMoneyflowResponse(userId, response, preDefMoneyflow);
			response.setResult(validationResult.isValid());
			response.setValidationItemTransports(
					super.mapList(validationResult.getValidationResultItems(), ValidationItemTransport.class));
			return response;
		}

		return null;
	}

	@RequestMapping(value = "deletePreDefMoneyflow/{id}", method = { RequestMethod.DELETE })
	@RequiresAuthorization
	public void deletePreDefMoneyflowById(@PathVariable(value = "id") final Long id) {
		final UserID userId = super.getUserId();
		final PreDefMoneyflowID preDefMoneyflowId = new PreDefMoneyflowID(id);
		this.preDefMoneyflowService.deletePreDefMoneyflow(userId, preDefMoneyflowId);
	}

	@RequestMapping(value = "showCreatePreDefMoneyflow", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowCreatePreDefMoneyflowResponse showCreatePreDefMoneyflow() {
		final UserID userId = super.getUserId();
		final ShowCreatePreDefMoneyflowResponse response = new ShowCreatePreDefMoneyflowResponse();
		this.fillAbstractCreatePreDefMoneyflowResponse(userId, response, null);
		return response;
	}

	@RequestMapping(value = "showEditPreDefMoneyflow/{id}", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowEditPreDefMoneyflowResponse showEditPreDefMoneyflow(
			@PathVariable(value = "id") final Long preDefMoneyflowId) {
		final UserID userId = super.getUserId();
		final ShowEditPreDefMoneyflowResponse response = new ShowEditPreDefMoneyflowResponse();
		final PreDefMoneyflow preDefMoneyflow = this.preDefMoneyflowService.getPreDefMoneyflowById(userId,
				new PreDefMoneyflowID(preDefMoneyflowId));
		if (preDefMoneyflow != null) {
			response.setPreDefMoneyflowTransport(super.map(preDefMoneyflow, PreDefMoneyflowTransport.class));
			this.fillAbstractCreatePreDefMoneyflowResponse(userId, response, preDefMoneyflow);
		}
		return response;
	}

	@RequestMapping(value = "showDeletePreDefMoneyflow/{id}", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowDeletePreDefMoneyflowResponse showDeletePreDefMoneyflow(
			@PathVariable(value = "id") final Long preDefMoneyflowId) {
		final UserID userId = super.getUserId();
		final ShowDeletePreDefMoneyflowResponse response = new ShowDeletePreDefMoneyflowResponse();
		final PreDefMoneyflow preDefMoneyflow = this.preDefMoneyflowService.getPreDefMoneyflowById(userId,
				new PreDefMoneyflowID(preDefMoneyflowId));
		response.setPreDefMoneyflowTransport(super.map(preDefMoneyflow, PreDefMoneyflowTransport.class));
		return response;
	}

	private void fillAbstractCreatePreDefMoneyflowResponse(final UserID userId,
			final AbstractCreatePreDefMoneyflowResponse response, final PreDefMoneyflow preDefMoneyflow) {
		final LocalDate today = LocalDate.now();

		final List<Capitalsource> capitalsources = this.capitalsourceService.getGroupCapitalsourcesByDateRange(userId,
				today, today);
		// if the Capitalsource is no longer valid, it was not returned by the service call above
		// but must be in the list to make it selectable.
		if (preDefMoneyflow != null) {
			Capitalsource capitalsource = preDefMoneyflow.getCapitalsource();
			if (capitalsource != null) {
				if (preDefMoneyflow.getCapitalsource().getValidFrom() == null) {
					// lazy initialized via update-request.
					final GroupID groupId = this.accessRelationService.getAccessor(userId).getId();
					capitalsource = this.capitalsourceService.getCapitalsourceById(userId, groupId,
							capitalsource.getId());
				}

				if (capitalsource != null && (today.isBefore(capitalsource.getValidFrom())
						|| today.isAfter(capitalsource.getValidTil()))) {
					capitalsources.add(capitalsource);
				}
			}
		}
		if (capitalsources != null && capitalsources.size() > 0) {
			final List<CapitalsourceTransport> capitalsourceTransports = super.mapList(capitalsources,
					CapitalsourceTransport.class);
			response.setCapitalsourceTransports(capitalsourceTransports);
		}

		final List<Contractpartner> contractpartners = this.contractpartnerService
				.getAllContractpartnersByDateRange(userId, today, today);
		// if the Contractpartner is no longer valid, it was not returned by the service call above
		// but must be in the list to make it selectable.
		if (preDefMoneyflow != null) {
			Contractpartner contractpartner = preDefMoneyflow.getContractpartner();
			if (contractpartner != null) {
				if (preDefMoneyflow.getContractpartner().getValidFrom() == null) {
					// lazy initialized via update-request.
					contractpartner = this.contractpartnerService.getContractpartnerById(userId,
							contractpartner.getId());
				}

				if (contractpartner != null && (today.isBefore(contractpartner.getValidFrom())
						|| today.isAfter(contractpartner.getValidTil()))) {
					contractpartners.add(contractpartner);
				}
			}
		}

		if (contractpartners != null && contractpartners.size() > 0) {
			final List<ContractpartnerTransport> contractpartnerTransports = super.mapList(contractpartners,
					ContractpartnerTransport.class);
			response.setContractpartnerTransports(contractpartnerTransports);
		}

		final List<PostingAccount> postingAccounts = this.postingAccountService.getAllPostingAccounts();
		if (postingAccounts != null && postingAccounts.size() > 0) {
			response.setPostingAccountTransports(super.mapList(postingAccounts, PostingAccountTransport.class));
		}

	}
}
