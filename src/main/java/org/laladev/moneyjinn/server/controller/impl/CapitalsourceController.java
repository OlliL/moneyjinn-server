package org.laladev.moneyjinn.server.controller.impl;

import java.time.LocalDate;

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

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.laladev.moneyjinn.businesslogic.model.access.Group;
import org.laladev.moneyjinn.businesslogic.model.access.User;
import org.laladev.moneyjinn.businesslogic.model.access.UserID;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientCurrentlyValidCapitalsourcesSetting;
import org.laladev.moneyjinn.businesslogic.model.setting.ClientMaxRowsSetting;
import org.laladev.moneyjinn.businesslogic.model.validation.ValidationResult;
import org.laladev.moneyjinn.businesslogic.service.api.IAccessRelationService;
import org.laladev.moneyjinn.businesslogic.service.api.ICapitalsourceService;
import org.laladev.moneyjinn.businesslogic.service.api.ISettingService;
import org.laladev.moneyjinn.businesslogic.service.api.IUserService;
import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import org.laladev.moneyjinn.core.rest.model.capitalsource.AbstractCapitalsourceResponse;
import org.laladev.moneyjinn.core.rest.model.capitalsource.CreateCapitalsourceRequest;
import org.laladev.moneyjinn.core.rest.model.capitalsource.ShowCapitalsourceListResponse;
import org.laladev.moneyjinn.core.rest.model.capitalsource.ShowDeleteCapitalsourceResponse;
import org.laladev.moneyjinn.core.rest.model.capitalsource.ShowEditCapitalsourceResponse;
import org.laladev.moneyjinn.core.rest.model.capitalsource.UpdateCapitalsourceRequest;
import org.laladev.moneyjinn.core.rest.model.transport.CapitalsourceTransport;
import org.laladev.moneyjinn.core.rest.model.transport.ValidationItemTransport;
import org.laladev.moneyjinn.server.annotation.RequiresAuthorization;
import org.laladev.moneyjinn.server.controller.mapper.CapitalsourceTransportMapper;
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
@RequestMapping("/moneyflow/server/capitalsource/")
public class CapitalsourceController extends AbstractController {
	@Inject
	private IAccessRelationService accessRelationService;
	@Inject
	private ICapitalsourceService capitalsourceService;
	@Inject
	private ISettingService settingService;
	@Inject
	private IUserService userService;

	@Override
	protected void addBeanMapper() {
		this.registerBeanMapper(new CapitalsourceTransportMapper());
		this.registerBeanMapper(new ValidationItemTransportMapper());
	}

	@RequestMapping(value = "showCapitalsourceList//currentlyValid/", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowCapitalsourceListResponse showCapitalsourceList() {
		final UserID userId = super.getUserId();
		final ClientCurrentlyValidCapitalsourcesSetting setting = this.settingService
				.getClientCurrentlyValidCapitalsourcesSetting(userId);
		return this.doShowCapitalsourceList(userId, null, setting.getSetting());

	}

	@RequestMapping(value = "showCapitalsourceList/{restriction}/currentlyValid/", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowCapitalsourceListResponse showCapitalsourceList(
			@PathVariable(value = "restriction") final String restriction) {
		final UserID userId = super.getUserId();
		final ClientCurrentlyValidCapitalsourcesSetting setting = this.settingService
				.getClientCurrentlyValidCapitalsourcesSetting(userId);
		return this.doShowCapitalsourceList(userId, restriction, setting.getSetting());

	}

	@RequestMapping(value = "showCapitalsourceList//currentlyValid/{currentlyValid}", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowCapitalsourceListResponse showCapitalsourceList(
			@PathVariable(value = "currentlyValid") final boolean currentlyValid) {
		final UserID userId = super.getUserId();
		final ShowCapitalsourceListResponse response = this.doShowCapitalsourceList(userId, null, currentlyValid);
		final ClientCurrentlyValidCapitalsourcesSetting setting = new ClientCurrentlyValidCapitalsourcesSetting(
				currentlyValid);
		this.settingService.setClientCurrentlyValidCapitalsourcesSetting(userId, setting);

		return response;
	}

	@RequestMapping(value = "showCapitalsourceList/{restriction}/currentlyValid/{currentlyValid}", method = {
			RequestMethod.GET })
	@RequiresAuthorization
	public ShowCapitalsourceListResponse showCapitalsourceList(
			@PathVariable(value = "restriction") final String restriction,
			@PathVariable(value = "currentlyValid") final boolean currentlyValid) {
		final UserID userId = super.getUserId();
		final ShowCapitalsourceListResponse response = this.doShowCapitalsourceList(userId, restriction,
				currentlyValid);
		final ClientCurrentlyValidCapitalsourcesSetting setting = new ClientCurrentlyValidCapitalsourcesSetting(
				currentlyValid);
		this.settingService.setClientCurrentlyValidCapitalsourcesSetting(userId, setting);

		return response;
	}

	private ShowCapitalsourceListResponse doShowCapitalsourceList(final UserID userId, final String restriction,
			final boolean currentlyValid) {
		final ClientMaxRowsSetting clientMaxRowsSetting = this.settingService.getClientMaxRowsSetting(userId);
		final LocalDate now = LocalDate.now();
		Set<Character> initials = null;
		Integer count = null;
		if (currentlyValid) {
			initials = this.capitalsourceService.getAllCapitalsourceInitialsByDateRange(userId, now, now);
			count = this.capitalsourceService.countAllCapitalsourcesByDateRange(userId, now, now);
		} else {
			initials = this.capitalsourceService.getAllCapitalsourceInitials(userId);
			count = this.capitalsourceService.countAllCapitalsources(userId);
		}

		List<Capitalsource> capitalsources = null;

		if ((restriction != null && restriction.equals(String.valueOf("all")))
				|| (restriction == null && clientMaxRowsSetting.getSetting().compareTo(count) >= 0)) {
			if (currentlyValid) {
				capitalsources = this.capitalsourceService.getAllCapitalsourcesByDateRange(userId, now, now);
			} else {
				capitalsources = this.capitalsourceService.getAllCapitalsources(userId);
			}
		} else if (restriction != null && restriction.length() == 1) {
			if (currentlyValid) {
				capitalsources = this.capitalsourceService.getAllCapitalsourcesByInitialAndDateRange(userId,
						restriction.toCharArray()[0], now, now);
			} else {
				capitalsources = this.capitalsourceService.getAllCapitalsourcesByInitial(userId,
						restriction.toCharArray()[0]);
			}
		}

		final ShowCapitalsourceListResponse response = new ShowCapitalsourceListResponse();
		if (capitalsources != null && !capitalsources.isEmpty()) {
			response.setCapitalsourceTransports(super.mapList(capitalsources, CapitalsourceTransport.class));
		}
		response.setInitials(initials);
		response.setCurrentlyValid(currentlyValid);

		return response;
	}

	@RequestMapping(value = "createCapitalsource", method = { RequestMethod.POST })
	@RequiresAuthorization
	public ValidationResponse createCapitalsource(@RequestBody final CreateCapitalsourceRequest request) {
		final UserID userId = super.getUserId();
		final Capitalsource capitalsource = super.map(request.getCapitalsourceTransport(), Capitalsource.class);

		final User user = this.userService.getUserById(userId);
		final Group accessor = this.accessRelationService.getAccessor(userId);

		capitalsource.setId(null);
		capitalsource.setUser(user);
		capitalsource.setAccess(accessor);

		final ValidationResult validationResult = this.capitalsourceService.validateCapitalsource(capitalsource);

		if (!validationResult.isValid()) {
			final ValidationResponse response = new ValidationResponse();
			response.setResult(validationResult.isValid());
			response.setValidationItemTransports(
					super.mapList(validationResult.getValidationResultItems(), ValidationItemTransport.class));
			return response;
		} else {
			this.capitalsourceService.createCapitalsource(capitalsource);
		}
		return null;
	}

	@RequestMapping(value = "updateCapitalsource", method = { RequestMethod.PUT })
	@RequiresAuthorization
	public ValidationResponse updateCapitalsource(@RequestBody final UpdateCapitalsourceRequest request) {
		final UserID userId = super.getUserId();
		final Capitalsource capitalsource = super.map(request.getCapitalsourceTransport(), Capitalsource.class);
		final User user = this.userService.getUserById(userId);
		final Group accessor = this.accessRelationService.getAccessor(userId);

		capitalsource.setUser(user);
		capitalsource.setAccess(accessor);

		final ValidationResult validationResult = this.capitalsourceService.validateCapitalsource(capitalsource);

		if (validationResult.isValid()) {
			this.capitalsourceService.updateCapitalsource(capitalsource);
		} else {
			final ValidationResponse response = new ValidationResponse();
			response.setResult(validationResult.isValid());
			response.setValidationItemTransports(
					super.mapList(validationResult.getValidationResultItems(), ValidationItemTransport.class));
			return response;
		}

		return null;
	}

	@RequestMapping(value = "deleteCapitalsourceById/{id}", method = { RequestMethod.DELETE })
	@RequiresAuthorization
	public void deleteCapitalsourceById(@PathVariable(value = "id") final Long id) {
		final UserID userId = super.getUserId();
		final Group accessor = this.accessRelationService.getAccessor(userId);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(id);
		this.capitalsourceService.deleteCapitalsource(userId, accessor.getId(), capitalsourceId);
	}

	@RequestMapping(value = "showEditCapitalsource/{id}", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowEditCapitalsourceResponse showEditCapitalsource(@PathVariable(value = "id") final Long capitalsourceId) {
		final ShowEditCapitalsourceResponse response = new ShowEditCapitalsourceResponse();
		this.fillAbstractCapitalsourceResponse(capitalsourceId, response);
		return response;
	}

	@RequestMapping(value = "showDeleteCapitalsource/{id}", method = { RequestMethod.GET })
	@RequiresAuthorization
	public ShowDeleteCapitalsourceResponse showDeleteCapitalsource(
			@PathVariable(value = "id") final Long capitalsourceId) {
		final ShowDeleteCapitalsourceResponse response = new ShowDeleteCapitalsourceResponse();
		this.fillAbstractCapitalsourceResponse(capitalsourceId, response);
		return response;
	}

	private void fillAbstractCapitalsourceResponse(final Long id, final AbstractCapitalsourceResponse response) {
		final UserID userId = super.getUserId();
		final Group accessor = this.accessRelationService.getAccessor(userId);
		final CapitalsourceID capitalsourceId = new CapitalsourceID(id);
		final Capitalsource capitalsource = this.capitalsourceService.getCapitalsourceById(userId, accessor.getId(),
				capitalsourceId);
		if (capitalsource != null && capitalsource.getUser().getId().equals(userId)) {
			// only the creator of a Capitalsource may edit or delete it
			response.setCapitalsourceTransport(super.map(capitalsource, CapitalsourceTransport.class));
		}
	}
}
