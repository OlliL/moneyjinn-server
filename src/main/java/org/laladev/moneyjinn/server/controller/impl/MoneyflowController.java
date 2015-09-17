package org.laladev.moneyjinn.server.controller.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.laladev.moneyjinn.businesslogic.model.Contractpartner;
import org.laladev.moneyjinn.businesslogic.model.Moneyflow;
import org.laladev.moneyjinn.businesslogic.model.MoneyflowID;
import org.laladev.moneyjinn.businesslogic.model.PostingAccount;
import org.laladev.moneyjinn.businesslogic.model.PreDefMoneyflow;
import org.laladev.moneyjinn.businesslogic.model.PreDefMoneyflowID;
import org.laladev.moneyjinn.businesslogic.model.access.Group;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientNumFreeMoneyflowsSetting;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResult;
import org.laladev.moneyjinn.businesslogic.service.api.IAccessRelationService;
import org.laladev.moneyjinn.businesslogic.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.businesslogic.service.api.IContractpartnerService;
import org.laladev.moneyjinn.businesslogic.service.api.IMoneyflowService;
import org.laladev.moneyjinn.businesslogic.service.api.IPostingAccountService;
import org.laladev.moneyjinn.businesslogic.service.api.IPreDefMoneyflowService;
import org.laladev.moneyjinn.businesslogic.service.api.ISettingService;
import org.laladev.moneyjinn.businesslogic.service.api.IUserService;
import org.laladev.moneyjinn.core.rest.model.moneyflow.AbstractAddMoneyflowResponse;
import org.laladev.moneyjinn.core.rest.model.moneyflow.AbstractEditMoneyflowResponse;
import org.laladev.moneyjinn.core.rest.model.moneyflow.CreateMoneyflowsRequest;
import org.laladev.moneyjinn.core.rest.model.moneyflow.CreateMoneyflowsResponse;
import org.laladev.moneyjinn.core.rest.model.moneyflow.ShowAddMoneyflowsResponse;
import org.laladev.moneyjinn.core.rest.model.moneyflow.ShowDeleteMoneyflowResponse;
import org.laladev.moneyjinn.core.rest.model.moneyflow.ShowEditMoneyflowResponse;
import org.laladev.moneyjinn.core.rest.model.moneyflow.ShowSearchMoneyflowFormResponse;
import org.laladev.moneyjinn.core.rest.model.moneyflow.UpdateMoneyflowRequest;
import org.laladev.moneyjinn.core.rest.model.moneyflow.UpdateMoneyflowResponse;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ContractpartnerTransport;
import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowTransport;
import org.laladev.moneyjinn.core.rest.model.transport.PostingAccountTransport;
import org.laladev.moneyjinn.core.rest.model.transport.PreDefMoneyflowTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.server.annotation.RequiresAuthorization;
import org.laladev.moneyjinn.server.controller.mapper.CapitalsourceTransportMapper;
import org.laladev.moneyjinn.server.controller.mapper.ContractpartnerTransportMapper;
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
	}

	private void fillAbstractAddMoneyflowResponse(final UserID userId, final AbstractAddMoneyflowResponse response) {
		final LocalDate today = LocalDate.now();

		final List<Capitalsource> capitalsources = this.capitalsourceService.getGroupCapitalsourcesByDateRange(userId,
				today, today);
		response.setCapitalsourceTransports(super.mapList(capitalsources, CapitalsourceTransport.class));

		final List<Contractpartner> contractpartner = this.contractpartnerService
				.getAllContractpartnersByDateRange(userId, today, today);
		response.setContractpartnerTransports(super.mapList(contractpartner, ContractpartnerTransport.class));

		final List<PostingAccount> postingAccounts = this.postingAccountService.getAllPostingAccounts();
		response.setPostingAccountTransports(super.mapList(postingAccounts, PostingAccountTransport.class));

		List<PreDefMoneyflow> preDefMoneyflows = this.preDefMoneyflowService.getAllPreDefMoneyflows(userId);
		if (preDefMoneyflows != null) {
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

		final List<Capitalsource> capitalsources = this.capitalsourceService.getGroupCapitalsources(userId);
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

		final List<Contractpartner> contractpartner = this.contractpartnerService.getAllContractpartners(userId);
		final List<ContractpartnerTransport> contractpartnerTransports = super.mapList(contractpartner,
				ContractpartnerTransport.class);

		final List<PostingAccount> postingAccounts = this.postingAccountService.getAllPostingAccounts();
		final List<PostingAccountTransport> postingAccountTransports = super.mapList(postingAccounts,
				PostingAccountTransport.class);

		final ShowSearchMoneyflowFormResponse response = new ShowSearchMoneyflowFormResponse();
		response.setContractpartnerTransports(contractpartnerTransports);
		response.setPostingAccountTransports(postingAccountTransports);

		return response;
	}

	@RequestMapping(value = "searchMoneyflows", method = { RequestMethod.PUT })
	@RequiresAuthorization
	public void searchMoneyflows() {
		// TODO implementation
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
