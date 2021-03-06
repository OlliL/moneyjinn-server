//Copyright (c) 2015-2021 Oliver Lehmann <lehmann@ans-netz.de>
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.laladev.moneyjinn.core.error.ErrorCode;
import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import org.laladev.moneyjinn.core.rest.model.moneyflow.CreateMoneyflowRequest;
import org.laladev.moneyjinn.core.rest.model.moneyflow.SearchMoneyflowsByAmountResponse;
import org.laladev.moneyjinn.core.rest.model.moneyflow.SearchMoneyflowsRequest;
import org.laladev.moneyjinn.core.rest.model.moneyflow.SearchMoneyflowsResponse;
import org.laladev.moneyjinn.core.rest.model.moneyflow.ShowAddMoneyflowsResponse;
import org.laladev.moneyjinn.core.rest.model.moneyflow.ShowDeleteMoneyflowResponse;
import org.laladev.moneyjinn.core.rest.model.moneyflow.ShowEditMoneyflowResponse;
import org.laladev.moneyjinn.core.rest.model.moneyflow.ShowSearchMoneyflowFormResponse;
import org.laladev.moneyjinn.core.rest.model.moneyflow.UpdateMoneyflowRequest;
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
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.model.validation.ValidationResultItem;
import org.laladev.moneyjinn.server.annotation.RequiresAuthorization;
import org.laladev.moneyjinn.server.controller.mapper.CapitalsourceTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.ContractpartnerTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.MoneyflowSearchParamsTransportMapper;
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
	private IMoneyflowService moneyflowService;
	@Inject
	private IMoneyflowSplitEntryService moneyflowSplitEntryService;
	@Inject
	private IMoneyflowReceiptService moneyflowReceiptService;

	private static final DateTimeFormatter SEARCH_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

	@Override
	protected void addBeanMapper() {
		super.registerBeanMapper(new CapitalsourceTransportMapper());
		super.registerBeanMapper(new ContractpartnerTransportMapper());
		super.registerBeanMapper(new PostingAccountTransportMapper());
		super.registerBeanMapper(new PreDefMoneyflowTransportMapper());
		super.registerBeanMapper(new MoneyflowTransportMapper());
		super.registerBeanMapper(new ValidationItemTransportMapper());
		super.registerBeanMapper(new MoneyflowSearchParamsTransportMapper());
		super.registerBeanMapper(new MoneyflowSplitEntryTransportMapper());
	}

	private boolean isOnceAMonthAndAlreadyUsed(final LocalDate today, final PreDefMoneyflow preDefMoneyflow) {
		final LocalDate lastUsedDate = preDefMoneyflow.getLastUsedDate();

		return preDefMoneyflow.isOnceAMonth() //
				&& lastUsedDate != null && lastUsedDate.getMonth().equals(today.getMonth()) //
				&& lastUsedDate.getYear() == today.getYear();
	}

	@RequestMapping(value = "showAddMoneyflows", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowAddMoneyflowsResponse showAddMoneyflows() {
		final UserID userId = super.getUserId();
		final ShowAddMoneyflowsResponse response = new ShowAddMoneyflowsResponse();

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

			final List<Capitalsource> capitalsources = this.capitalsourceService.getGroupBookableCapitalsources(userId);
			response.setCapitalsourceTransports(super.mapList(capitalsources, CapitalsourceTransport.class));

			final List<Contractpartner> contractpartner = this.contractpartnerService.getAllContractpartners(userId);
			response.setContractpartnerTransports(super.mapList(contractpartner, ContractpartnerTransport.class));

			final List<PostingAccount> postingAccounts = this.postingAccountService.getAllPostingAccounts();
			response.setPostingAccountTransports(super.mapList(postingAccounts, PostingAccountTransport.class));

			final List<MoneyflowID> moneyflowIdsWithReceipts = this.moneyflowReceiptService
					.getMoneyflowIdsWithReceipt(userId, Arrays.asList(moneyflow.getId()));

			response.setHasReceipt(moneyflowIdsWithReceipts.size() == 1);

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

		if (!validationResult.isValid()) {
			response.setResult(false);
			response.setValidationItemTransports(
					super.mapList(validationResult.getValidationResultItems(), ValidationItemTransport.class));
		} else {
			final List<Moneyflow> moneyflows = this.moneyflowService.searchMoneyflows(userId, moneyflowSearchParams);
			if (moneyflows != null && !moneyflows.isEmpty()) {

				final List<MoneyflowTransport> moneyflowTransports = moneyflows.stream()
						.filter(mf -> !mf.isPrivat() || mf.getUser().getId().equals(userId))
						.map(mf -> super.map(mf, MoneyflowTransport.class))
						.collect(Collectors.toCollection(ArrayList::new));

				response.setMoneyflowTransports(moneyflowTransports);
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

	/**
	 * Creates a new moneyflow together with split entries if they where given.
	 *
	 * @param request
	 *            The request which contains the moneyflow
	 * @return ValidationResponse
	 */
	@RequestMapping(value = "createMoneyflow", method = { RequestMethod.POST })
	@RequiresAuthorization
	public ValidationResponse createMoneyflows(@RequestBody final CreateMoneyflowRequest request) {
		final UserID userId = super.getUserId();

		final Moneyflow moneyflow = super.map(request.getMoneyflowTransport(), Moneyflow.class);
		final List<MoneyflowSplitEntry> moneyflowSplitEntries = super.mapList(
				request.getInsertMoneyflowSplitEntryTransports(), MoneyflowSplitEntry.class);

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

		this.prepareForValidityCheck(moneyflow, moneyflowSplitEntries);

		final ValidationResponse response = new ValidationResponse();
		final ValidationResult validationResult = this.moneyflowService.validateMoneyflow(moneyflow);

		if (validationResult.isValid()) {
			if (!moneyflowSplitEntries.isEmpty()) {
				moneyflowSplitEntries.stream().forEach(mse -> {
					validationResult
							.mergeValidationResult(this.moneyflowSplitEntryService.validateMoneyflowSplitEntry(mse));
				});

				if (validationResult.isValid()) {
					validationResult.mergeValidationResult(this.checkIfAmountIsEqual(moneyflow, moneyflowSplitEntries));
				}
			}

			if (validationResult.isValid()) {
				final MoneyflowID moneyflowId = this.moneyflowService.createMoneyflow(moneyflow);
				if (!moneyflowSplitEntries.isEmpty()) {
					moneyflowSplitEntries.stream().forEach(mse -> mse.setMoneyflowId(moneyflowId));
					this.moneyflowSplitEntryService.createMoneyflowSplitEntries(userId, moneyflowSplitEntries);
				}

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

				return null;
			}
		}

		if (!validationResult.isValid()) {
			response.setResult(false);
			response.setValidationItemTransports(
					super.mapList(validationResult.getValidationResultItems(), ValidationItemTransport.class));
		}

		return response;
	}

	/**
	 * Checks if capitalsource and contractparter are valid on bookingdate - otherwise the validity
	 * is modified. Also fills comment and postingaccount if it is empty and MoneyflowSplitEntries
	 * where provided with data from the first MoneyflowSplitEntry.
	 *
	 * @param moneyflow
	 *            Moneyflow
	 * @param moneyflowSplitEntries
	 *            MoneyflowSplitEntries
	 */
	private void prepareForValidityCheck(final Moneyflow moneyflow,
			final List<MoneyflowSplitEntry> moneyflowSplitEntries) {
		final LocalDate bookingDate = moneyflow.getBookingDate();

		final UserID userId = moneyflow.getUser().getId();
		final Group group = moneyflow.getGroup();

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

		// use the comment and postingaccount of the 1st split booking for the main booking if
		// nothing is specified
		if (!moneyflowSplitEntries.isEmpty()) {
			final MoneyflowSplitEntry moneyflowSplitEntry = moneyflowSplitEntries.iterator().next();

			if (moneyflow.getComment() == null || moneyflow.getComment().trim().isEmpty()) {
				moneyflow.setComment(moneyflowSplitEntry.getComment());
			}

			if (moneyflow.getPostingAccount() == null) {
				moneyflow.setPostingAccount(moneyflowSplitEntry.getPostingAccount());
			}
		}
	}

	/**
	 * Updates the given Moneyflow
	 *
	 * @param request
	 *            The Request object which contains the Moneyflow.
	 * @return Validation Response
	 */
	@RequestMapping(value = "updateMoneyflow", method = { RequestMethod.PUT })
	@RequiresAuthorization
	public ValidationResponse updateMoneyflow(@RequestBody final UpdateMoneyflowRequest request) {
		final UserID userId = super.getUserId();
		final User user = this.userService.getUserById(userId);
		final Group group = this.accessRelationService.getAccessor(userId);

		final List<MoneyflowSplitEntry> updateMoneyflowSplitEntries = super.mapList(
				request.getUpdateMoneyflowSplitEntryTransports(), MoneyflowSplitEntry.class);
		final List<MoneyflowSplitEntry> insertMoneyflowSplitEntries = super.mapList(
				request.getInsertMoneyflowSplitEntryTransports(), MoneyflowSplitEntry.class);
		List<MoneyflowSplitEntryID> deleteMoneyflowSplitEntryIds = null;
		if (request.getDeleteMoneyflowSplitEntryIds() != null) {
			deleteMoneyflowSplitEntryIds = request.getDeleteMoneyflowSplitEntryIds().stream()
					.map(MoneyflowSplitEntryID::new).collect(Collectors.toCollection(ArrayList::new));
		}
		final Moneyflow moneyflow = super.map(request.getMoneyflowTransport(), Moneyflow.class);
		moneyflow.setUser(user);
		moneyflow.setGroup(group);

		// build a List of all MoneyflowSplitEntries which will be there after his update
		final List<MoneyflowSplitEntry> moneyflowSplitEntries = this.moneyflowSplitEntryService
				.getMoneyflowSplitEntries(userId, moneyflow.getId());
		final ListIterator<MoneyflowSplitEntry> entryIterator = moneyflowSplitEntries.listIterator();
		while (entryIterator.hasNext()) {
			final MoneyflowSplitEntry entry = entryIterator.next();
			if (deleteMoneyflowSplitEntryIds != null && deleteMoneyflowSplitEntryIds.contains(entry.getId())) {
				entryIterator.remove();
			} else {
				final MoneyflowSplitEntry matchingUpdateEntry = updateMoneyflowSplitEntries.stream()
						.filter(mse -> mse.getId().equals(entry.getId())).findAny().orElse(null);
				if (matchingUpdateEntry != null) {
					entryIterator.set(matchingUpdateEntry);
				}
			}
		}

		moneyflowSplitEntries.addAll(insertMoneyflowSplitEntries);

		this.prepareForValidityCheck(moneyflow, moneyflowSplitEntries);

		final MoneyflowID moneyflowId = moneyflow.getId();
		final Moneyflow moneyflowById = this.moneyflowService.getMoneyflowById(userId, moneyflowId);

		// only the creator of a moneyflow may edit it!
		if (moneyflowById != null && moneyflowById.getUser().equals(user)) {

			final ValidationResult validationResult = this.moneyflowService.validateMoneyflow(moneyflow);

			if (validationResult.isValid()) {

				updateMoneyflowSplitEntries.forEach(mse -> {
					mse.setMoneyflowId(moneyflowId);
					validationResult
							.mergeValidationResult(this.moneyflowSplitEntryService.validateMoneyflowSplitEntry(mse));
				});

				insertMoneyflowSplitEntries.forEach(mse -> {
					mse.setMoneyflowId(moneyflowId);
					validationResult
							.mergeValidationResult(this.moneyflowSplitEntryService.validateMoneyflowSplitEntry(mse));
				});

				if (validationResult.isValid()) {

					validationResult.mergeValidationResult(this.checkIfAmountIsEqual(moneyflow, moneyflowSplitEntries));

					if (validationResult.isValid()) {
						if (deleteMoneyflowSplitEntryIds != null) {
							deleteMoneyflowSplitEntryIds.forEach(mseId -> this.moneyflowSplitEntryService
									.deleteMoneyflowSplitEntry(userId, moneyflowId, mseId));
						}

						updateMoneyflowSplitEntries
								.forEach(mse -> this.moneyflowSplitEntryService.updateMoneyflowSplitEntry(userId, mse));

						if (!insertMoneyflowSplitEntries.isEmpty()) {
							this.moneyflowSplitEntryService.createMoneyflowSplitEntries(userId,
									insertMoneyflowSplitEntries);
						}

						this.moneyflowService.updateMoneyflow(moneyflow);
					}
				}
			}

			if (!validationResult.isValid()) {
				return super.returnValidationResponse(validationResult);
			}
		}

		return null;
	}

	private ValidationResult checkIfAmountIsEqual(final Moneyflow moneyflow,
			final List<MoneyflowSplitEntry> moneyflowSplitEntries) {
		final ValidationResult validationResult = new ValidationResult();

		if (!moneyflowSplitEntries.isEmpty()) {
			final BigDecimal sumOfSplitEntriesAmount = moneyflowSplitEntries.stream()
					.map(MoneyflowSplitEntry::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

			if (sumOfSplitEntriesAmount.compareTo(moneyflow.getAmount()) != 0) {
				validationResult.addValidationResultItem(new ValidationResultItem(moneyflow.getId(),
						ErrorCode.SPLIT_ENTRIES_AMOUNT_IS_NOT_EQUALS_MONEYFLOW_AMOUNT));
			}
		}

		return validationResult;
	}

	/**
	 * Deletes the specified Moneyflow.
	 *
	 * @param id
	 *            The ID of the Moneyflow to delete
	 */
	@RequestMapping(value = "deleteMoneyflowById/{id}", method = { RequestMethod.DELETE })
	@RequiresAuthorization
	public void deleteMoneyflowById(@PathVariable(value = "id") final Long id) {
		final UserID userId = super.getUserId();
		final MoneyflowID moneyflowId = new MoneyflowID(id);
		this.moneyflowSplitEntryService.deleteMoneyflowSplitEntries(userId, moneyflowId);
		this.moneyflowReceiptService.deleteMoneyflowReceipt(userId, moneyflowId);
		this.moneyflowService.deleteMoneyflow(userId, moneyflowId);
	}

	/**
	 * Searches for Moneyflows given by an absolut amount and a date range.
	 *
	 * @param amount
	 *            ABS amount
	 * @param dateFromStr
	 *            date to start searching (format: YYYYMMDD)
	 * @param dateTilStr
	 *            date to end searching (format: YYYYMMDD)
	 * @return matching Moneyflows
	 */
	@RequestMapping(value = "searchMoneyflowsByAmount/{amount}/{dateFromStr}/{dateTilStr}", method = {
			RequestMethod.GET })
	@RequiresAuthorization
	public SearchMoneyflowsByAmountResponse searchMoneyflowsByAmount(
			@PathVariable(value = "amount") final BigDecimal amount,
			@PathVariable(value = "dateFromStr") final String dateFromStr,
			@PathVariable(value = "dateTilStr") final String dateTilStr) {
		final UserID userId = super.getUserId();
		final SearchMoneyflowsByAmountResponse response = new SearchMoneyflowsByAmountResponse();
		final LocalDate dateFrom = LocalDate.parse(dateFromStr, SEARCH_DATE_FORMATTER);
		final LocalDate dateTil = LocalDate.parse(dateTilStr, SEARCH_DATE_FORMATTER);

		final List<Moneyflow> moneyflows = this.moneyflowService.searchMoneyflowsByAbsoluteAmountDate(userId, amount,
				dateFrom, dateTil);
		if (moneyflows != null && !moneyflows.isEmpty()) {
			final List<Moneyflow> relevantMoneyflows = moneyflows.stream()
					.filter(mf -> !mf.isPrivat() || mf.getUser().getId().equals(userId))
					.collect(Collectors.toCollection(ArrayList::new));

			if (relevantMoneyflows != null && !relevantMoneyflows.isEmpty()) {
				final List<MoneyflowID> relevantMoneyflowIds = relevantMoneyflows.stream().map(Moneyflow::getId)
						.collect(Collectors.toCollection(ArrayList::new));

				final Map<MoneyflowID, List<MoneyflowSplitEntry>> moneyflowSplitEntries = this.moneyflowSplitEntryService
						.getMoneyflowSplitEntries(userId, relevantMoneyflowIds);

				final List<MoneyflowTransport> moneyflowTransports = relevantMoneyflows.stream()
						.map(mf -> super.map(mf, MoneyflowTransport.class))
						.collect(Collectors.toCollection(ArrayList::new));

				response.setMoneyflowTransports(moneyflowTransports);

				if (!moneyflowSplitEntries.isEmpty()) {
					final ArrayList<MoneyflowSplitEntry> moneyflowSplitEntryList = moneyflowSplitEntries.values()
							.stream().flatMap(List::stream).collect(Collectors.toCollection(ArrayList::new));

					response.setMoneyflowSplitEntryTransports(
							super.mapList(moneyflowSplitEntryList, MoneyflowSplitEntryTransport.class));
				}

				return response;
			}
		}

		return null;

	}
}
