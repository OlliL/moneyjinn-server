package org.laladev.moneyjinn.server.controller.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.laladev.moneyjinn.businesslogic.model.Contractpartner;
import org.laladev.moneyjinn.businesslogic.model.Moneyflow;
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
import org.laladev.moneyjinn.core.rest.model.moneyflow.CreateMoneyflowRequest;
import org.laladev.moneyjinn.core.rest.model.moneyflow.CreateMoneyflowResponse;
import org.laladev.moneyjinn.core.rest.model.moneyflow.ShowAddMoneyflowsResponse;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ContractpartnerTransport;
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
	public void showEditMoneyflow(@PathVariable(value = "id") final Long id) {
		// TODO implementation
	}

	@RequestMapping(value = "showDeleteMoneyflow/{id}", method = { RequestMethod.GET })
	@RequiresAuthorization
	public void showDeleteMoneyflow(@PathVariable(value = "id") final Long id) {
		// TODO implementation
	}

	@RequestMapping(value = "showSearchMoneyflowForm", method = { RequestMethod.GET })
	@RequiresAuthorization
	public void showSearchMoneyflowForm() {
		// TODO implementation
	}

	@RequestMapping(value = "searchMoneyflows", method = { RequestMethod.PUT })
	@RequiresAuthorization
	public void searchMoneyflows() {
		// TODO implementation
	}

	@RequestMapping(value = "createMoneyflows", method = { RequestMethod.POST })
	@RequiresAuthorization
	public CreateMoneyflowResponse createMoneyflows(@RequestBody final CreateMoneyflowRequest request) {
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

		final CreateMoneyflowResponse response = new CreateMoneyflowResponse();
		this.fillAbstractAddMoneyflowResponse(userId, response);

		if (validationResult.isValid() == true) {
			this.moneyflowService.createMoneyflows(moneyflows);
			if (preDefMoneyflowIds != null) {
				preDefMoneyflowIds.stream()
						.forEach(id -> this.preDefMoneyflowService.setLastUsedDate(userId, new PreDefMoneyflowID(id)));
			}
		} else {
			response.setResult(false);
			response.setValidationItemTransports(
					super.mapList(validationResult.getValidationResultItems(), ValidationItemTransport.class));
		}

		return response;
	}

	@RequestMapping(value = "updateMoneyflow", method = { RequestMethod.PUT })
	@RequiresAuthorization
	public void updateMoneyflow() {
		// TODO implementation
	}

	@RequestMapping(value = "deleteMoneyflowById/{id}", method = { RequestMethod.DELETE })
	@RequiresAuthorization
	public void deleteMoneyflowById(@PathVariable(value = "id") final Long id) {
		// TODO implementation
	}
}
