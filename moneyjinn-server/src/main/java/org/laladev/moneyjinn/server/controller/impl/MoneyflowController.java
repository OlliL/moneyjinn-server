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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.moneyflow.AbstractAddMoneyflowResponse;
import org.laladev.moneyjinn.core.rest.model.moneyflow.AbstractEditMoneyflowResponse;
import org.laladev.moneyjinn.core.rest.model.moneyflow.CreateMoneyflowRequest;
import org.laladev.moneyjinn.core.rest.model.moneyflow.CreateMoneyflowResponse;
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
import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowSplitEntryTransport;
import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowTransport;
import org.laladev.moneyjinn.core.rest.model.transport.PostingAccountTransport;
import org.laladev.moneyjinn.core.rest.model.transport.PreDefMoneyflowTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.model.Contractpartner;
import org.laladev.moneyjinn.model.PostingAccount;
import org.laladev.moneyjinn.model.PreDefMoneyflow;
import org.laladev.moneyjinn.model.PreDefMoneyflowID;
import org.laladev.moneyjinn.model.access.AccessRelation;
import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.moneyflow.Moneyflow;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowID;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowSplitEntry;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowSplitEntryID;
import org.laladev.moneyjinn.model.moneyflow.search.MoneyflowSearchParams;
import org.laladev.moneyjinn.model.moneyflow.search.MoneyflowSearchResult;
import org.laladev.moneyjinn.model.setting.ClientNumFreeMoneyflowsSetting;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.server.annotation.RequiresAuthorization;
import org.laladev.moneyjinn.server.controller.mapper.CapitalsourceTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.ContractpartnerTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.MoneyflowSearchParamsTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.MoneyflowSearchResultTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.MoneyflowSplitEntryTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.MoneyflowTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.PostingAccountTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.PreDefMoneyflowTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.ValidationItemTransportMapper;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.service.api.IContractpartnerService;
import org.laladev.moneyjinn.service.api.IMoneyflowReceiptService;
import org.laladev.moneyjinn.service.api.IMoneyflowService;
import org.laladev.moneyjinn.service.api.IMoneyflowSplitEntryService;
import org.laladev.moneyjinn.service.api.IPostingAccountService;
import org.laladev.moneyjinn.service.api.IPreDefMoneyflowService;
import org.laladev.moneyjinn.service.api.ISettingService;
import org.laladev.moneyjinn.service.api.IUserService;
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
	private static final Short SHORT_1 = new Short("1");
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
	@Inject
	private IMoneyflowSplitEntryService moneyflowSplitEntryService;
	@Inject
	private IMoneyflowReceiptService moneyflowReceiptService;

	@Override
	protected void addBeanMapper() {
		super.registerBeanMapper(new CapitalsourceTransportMapper());
		super.registerBeanMapper(new ContractpartnerTransportMapper());
		super.registerBeanMapper(new PostingAccountTransportMapper());
		super.registerBeanMapper(new PreDefMoneyflowTransportMapper());
		super.registerBeanMapper(new MoneyflowTransportMapper());
		super.registerBeanMapper(new ValidationItemTransportMapper());
		super.registerBeanMapper(new MoneyflowSearchParamsTransportMapper());
		super.registerBeanMapper(new MoneyflowSearchResultTransportMapper());
		super.registerBeanMapper(new MoneyflowSplitEntryTransportMapper());
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
		Assert.notNull(moneyflow.getUser(), "User must not be null!");

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

			final List<MoneyflowSplitEntry> moneyflowSplitEntries = this.moneyflowSplitEntryService
					.getMoneyflowSplitEntries(userId, moneyflow.getId());
			if (!moneyflowSplitEntries.isEmpty()) {
				response.setMoneyflowSplitEntryTransports(
						super.mapList(moneyflowSplitEntries, MoneyflowSplitEntryTransport.class));
			}

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

	@RequestMapping(value = "createMoneyflow", method = { RequestMethod.POST })
	@RequiresAuthorization
	public CreateMoneyflowResponse createMoneyflows(@RequestBody final CreateMoneyflowRequest request) {
		final UserID userId = super.getUserId();

		final Moneyflow moneyflow = super.map(request.getMoneyflowTransport(), Moneyflow.class);
		final Long preDefMoneyflowIdLong = request.getUsedPreDefMoneyflowId();

		boolean saveAsPreDefMoneyflow = false;
		final Short saveAsPreDefMoneyflowShort = request.getSaveAsPreDefMoneyflow();
		if (saveAsPreDefMoneyflowShort != null && saveAsPreDefMoneyflowShort.equals(SHORT_1)) {
			saveAsPreDefMoneyflow = true;
		}

		final User user = this.userService.getUserById(userId);
		final Group group = this.accessRelationService.getAccessor(userId);

		moneyflow.setUser(user);
		moneyflow.setGroup(group);

		final LocalDate bookingDate = moneyflow.getBookingDate();

		if (bookingDate != null) {

			final AccessRelation accessRelation = this.accessRelationService
					.getAccessRelationById(moneyflow.getUser().getId(), LocalDate.now());
			// Only modify Capitalsources or Contractpartner if the Bookingdate is within the
			// current group assignment validity period
			if (!bookingDate.isBefore(accessRelation.getValidFrom())
					&& !bookingDate.isAfter(accessRelation.getValidTil())) {
				// Check if used Capitalsource is valid at bookingDate - if not, change its
				// validity so it fits.
				if (moneyflow.getCapitalsource() != null) {
					final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId,
							group.getId(), moneyflow.getCapitalsource().getId());

					if (capitalsource != null) {
						final boolean userMayUseCapitalsource = capitalsource.getUser().getId()
								.equals(moneyflow.getUser().getId()) || capitalsource.isGroupUse();

						if (userMayUseCapitalsource) {
							final boolean bookingDateIsBeforeValidity = bookingDate
									.isBefore(capitalsource.getValidFrom());
							final boolean bookingDateIsAfterValidity = bookingDate.isAfter(capitalsource.getValidTil());

							if (bookingDateIsBeforeValidity) {
								capitalsource.setValidFrom(bookingDate);
							}
							if (bookingDateIsAfterValidity) {
								capitalsource.setValidTil(bookingDate);
							}
							if (bookingDateIsAfterValidity || bookingDateIsBeforeValidity) {
								this.capitalsourceService.updateCapitalsource(capitalsource);

							}

						}
					}
				}

				// Check if used Contractpartner is valid at bookingDate - if not, change its
				// validity so it fits.
				if (moneyflow.getContractpartner() != null) {
					final Contractpartner contractpartner = this.contractpartnerService.getContractpartnerById(userId,
							moneyflow.getContractpartner().getId());

					if (contractpartner != null) {
						final boolean bookingDateIsBeforeValidity = bookingDate
								.isBefore(contractpartner.getValidFrom());
						final boolean bookingDateIsAfterValidity = bookingDate.isAfter(contractpartner.getValidTil());

						if (bookingDateIsBeforeValidity) {
							contractpartner.setValidFrom(bookingDate);
						}
						if (bookingDateIsAfterValidity) {
							contractpartner.setValidTil(bookingDate);
						}
						if (bookingDateIsAfterValidity || bookingDateIsBeforeValidity) {
							this.contractpartnerService.updateContractpartner(contractpartner);

						}
					}
				}
			}
		}

		final CreateMoneyflowResponse response = new CreateMoneyflowResponse();
		final ValidationResult validationResult = this.moneyflowService.validateMoneyflow(moneyflow);

		if (validationResult.isValid()) {
			this.moneyflowService.createMoneyflows(Arrays.asList(moneyflow));

			PreDefMoneyflowID preDefMoneyflowId = null;
			if (preDefMoneyflowIdLong != null) {
				preDefMoneyflowId = new PreDefMoneyflowID(preDefMoneyflowIdLong);
			}

			if (saveAsPreDefMoneyflow) {
				final PreDefMoneyflow preDefMoneyflow = new PreDefMoneyflow();
				preDefMoneyflow.setAmount(moneyflow.getAmount());
				preDefMoneyflow.setCapitalsource(moneyflow.getCapitalsource());
				preDefMoneyflow.setComment(moneyflow.getComment());
				preDefMoneyflow.setContractpartner(moneyflow.getContractpartner());
				preDefMoneyflow.setPostingAccount(moneyflow.getPostingAccount());
				preDefMoneyflow.setUser(user);

				if (preDefMoneyflowId != null) {
					final PreDefMoneyflow preDefMoneyflowOrig = this.preDefMoneyflowService
							.getPreDefMoneyflowById(userId, preDefMoneyflowId);
					if (preDefMoneyflowOrig != null) {
						preDefMoneyflow.setId(preDefMoneyflowId);
						preDefMoneyflow.setOnceAMonth(preDefMoneyflowOrig.isOnceAMonth());

						this.preDefMoneyflowService.updatePreDefMoneyflow(preDefMoneyflow);
					}
				} else {
					preDefMoneyflow.setOnceAMonth(false);
					preDefMoneyflowId = this.preDefMoneyflowService.createPreDefMoneyflow(preDefMoneyflow);
				}
			}

			if (preDefMoneyflowId != null) {
				this.preDefMoneyflowService.setLastUsedDate(userId, preDefMoneyflowId);
			}
			response.setResult(true);
		} else {
			response.setResult(false);
			response.setValidationItemTransports(
					super.mapList(validationResult.getValidationResultItems(), ValidationItemTransport.class));
		}

		// It is important that the "last used" of any predefmoneyflows is set before selecting any
		// relevant predefmoneyflows because of "once a month" they might not be relevant. Thats why
		// the response data is created at last
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

		final MoneyflowID moneyflowId = moneyflow.getId();
		final Moneyflow moneyflowById = this.moneyflowService.getMoneyflowById(userId, moneyflowId);

		final ValidationResult validationResult = new ValidationResult();

		// only the creator of a moneyflow may edit it!
		if (moneyflowById != null && moneyflowById.getUser().equals(user)) {

			validationResult.mergeValidationResult(this.moneyflowService.validateMoneyflow(moneyflow));

			if (validationResult.isValid()) {
				List<MoneyflowSplitEntry> updateMoneyflowSplitEntries = null;
				List<MoneyflowSplitEntry> insertMoneyflowSplitEntries = null;
				List<MoneyflowSplitEntryID> deleteMoneyflowSplitEntryIds = null;

				if (request.getUpdateMoneyflowSplitEntryTransports() != null) {
					updateMoneyflowSplitEntries = super.mapList(request.getUpdateMoneyflowSplitEntryTransports(),
							MoneyflowSplitEntry.class);
					updateMoneyflowSplitEntries.stream().forEach(mse -> {
						mse.setMoneyflowId(moneyflowId);
						validationResult.mergeValidationResult(
								this.moneyflowSplitEntryService.validateMoneyflowSplitEntry(mse));
					});
				}

				if (request.getInsertMoneyflowSplitEntryTransports() != null) {
					insertMoneyflowSplitEntries = super.mapList(request.getInsertMoneyflowSplitEntryTransports(),
							MoneyflowSplitEntry.class);
					insertMoneyflowSplitEntries.stream().forEach(mse -> {
						mse.setMoneyflowId(moneyflowId);
						validationResult.mergeValidationResult(
								this.moneyflowSplitEntryService.validateMoneyflowSplitEntry(mse));
					});
				}

				if (validationResult.isValid()) {
					if (request.getDeleteMoneyflowSplitEntryIds() != null) {
						deleteMoneyflowSplitEntryIds = request.getDeleteMoneyflowSplitEntryIds().stream()
								.map(id -> new MoneyflowSplitEntryID(id))
								.collect(Collectors.toCollection(ArrayList::new));
					}

					if (insertMoneyflowSplitEntries != null || updateMoneyflowSplitEntries != null
							|| deleteMoneyflowSplitEntryIds != null) {
						final List<MoneyflowSplitEntry> moneyflowSplitEntries = this.moneyflowSplitEntryService
								.getMoneyflowSplitEntries(userId, moneyflow.getId());
						final ListIterator<MoneyflowSplitEntry> entryIterator = moneyflowSplitEntries.listIterator();
						while (entryIterator.hasNext()) {
							final MoneyflowSplitEntry entry = entryIterator.next();
							if (deleteMoneyflowSplitEntryIds != null
									&& deleteMoneyflowSplitEntryIds.contains(entry.getId())) {
								entryIterator.remove();
							} else {
								if (updateMoneyflowSplitEntries != null) {
									final MoneyflowSplitEntry matchingUpdateEntry = updateMoneyflowSplitEntries.stream()
											.filter(mse -> mse.getId().equals(entry.getId())).findAny().orElse(null);
									if (matchingUpdateEntry != null) {
										entryIterator.set(matchingUpdateEntry);
									}
								}
							}
						}

						if (insertMoneyflowSplitEntries != null) {
							moneyflowSplitEntries.addAll(insertMoneyflowSplitEntries);
						}

						if (!moneyflowSplitEntries.isEmpty()) {
							final BigDecimal sumOfSplitEntriesAmount = moneyflowSplitEntries.stream()
									.map(MoneyflowSplitEntry::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

							if (sumOfSplitEntriesAmount.compareTo(moneyflow.getAmount()) != 0) {
								validationResult.addValidationResultItem(new ValidationResultItem(moneyflowId,
										ErrorCode.SPLIT_ENTRIES_AMOUNT_IS_NOT_EQUALS_MONEYFLOW_AMOUNT));
							}
						}

						if (validationResult.isValid()) {
							if (deleteMoneyflowSplitEntryIds != null) {
								deleteMoneyflowSplitEntryIds.forEach(mseId -> this.moneyflowSplitEntryService
										.deleteMoneyflowSplitEntry(userId, moneyflowId, mseId));
							}

							if (updateMoneyflowSplitEntries != null) {
								updateMoneyflowSplitEntries.stream().forEach(
										mse -> this.moneyflowSplitEntryService.updateMoneyflowSplitEntry(userId, mse));
							}

							if (insertMoneyflowSplitEntries != null) {
								this.moneyflowSplitEntryService.createMoneyflowSplitEntries(userId,
										insertMoneyflowSplitEntries);
							}

						}
					}

					if (validationResult.isValid()) {
						this.moneyflowService.updateMoneyflow(moneyflow);
					}
				}
			}

			if (!validationResult.isValid()) {
				final UpdateMoneyflowResponse response = new UpdateMoneyflowResponse();
				this.fillAbstractEditMoneyflowResponse(moneyflow, response);
				response.setResult(validationResult.isValid());
				response.setValidationItemTransports(
						super.mapList(validationResult.getValidationResultItems(), ValidationItemTransport.class));
				return response;
			}
		}

		return null;
	}

	@RequestMapping(value = "deleteMoneyflowById/{id}", method = { RequestMethod.DELETE })
	@RequiresAuthorization
	public void deleteMoneyflowById(@PathVariable(value = "id") final Long id) {
		final UserID userId = super.getUserId();
		final MoneyflowID moneyflowId = new MoneyflowID(id);
		this.moneyflowSplitEntryService.deleteMoneyflowSplitEntries(userId, moneyflowId);
		this.moneyflowReceiptService.deleteMoneyflowReceipt(userId, moneyflowId);
		this.moneyflowService.deleteMoneyflow(userId, moneyflowId);
	}
}
