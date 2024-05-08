//Copyright (c) 2023-2024 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.server.controller.impl.crud;

import java.util.List;
import java.util.Optional;

import org.laladev.moneyjinn.model.access.Group;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.etf.Etf;
import org.laladev.moneyjinn.model.etf.EtfID;
import org.laladev.moneyjinn.model.setting.ClientListEtfDepotDefaultEtfId;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.server.controller.api.CrudEtfControllerApi;
import org.laladev.moneyjinn.server.controller.impl.AbstractController;
import org.laladev.moneyjinn.server.controller.mapper.EtfTransportMapper;
import org.laladev.moneyjinn.server.model.EtfTransport;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.IEtfService;
import org.laladev.moneyjinn.service.api.ISettingService;
import org.laladev.moneyjinn.service.api.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class CrudEtfController extends AbstractController implements CrudEtfControllerApi {
	private final IAccessRelationService accessRelationService;
	private final IEtfService etfService;
	private final IUserService userService;
	private final ISettingService settingService;
	private final EtfTransportMapper etfTransportMapper;

	@Override
	@PostConstruct
	protected void addBeanMapper() {
		this.registerBeanMapper(this.etfTransportMapper);
	}

	@Override
	public ResponseEntity<List<EtfTransport>> readAll() {
		final UserID userId = super.getUserId();
		final List<Etf> etfs = this.etfService.getAllEtf(userId);

		final List<EtfTransport> transports = super.mapList(etfs, EtfTransport.class);

		final Optional<ClientListEtfDepotDefaultEtfId> setting = this.settingService
				.getClientListEtfDepotDefaultEtfId(this.getUserId());
		if (setting.isPresent()) {
			final var favoriteEtfId = setting.get().getSetting();
			transports.stream().filter(t -> t.getEtfId().equals(favoriteEtfId.getId())).findFirst()
					.ifPresent(e -> e.setIsFavorite(1));

		}
		return ResponseEntity.ok(transports);
	}

	@Override
	public ResponseEntity<EtfTransport> readOne(@PathVariable("id") final Long id) {
		final UserID userId = super.getUserId();
		final EtfID etfId = new EtfID(id);
		final Etf etf = this.etfService.getEtfById(userId, etfId);

		if (etf == null) {
			return ResponseEntity.notFound().build();
		}

		final EtfTransport transport = super.map(etf, EtfTransport.class);

		final Optional<ClientListEtfDepotDefaultEtfId> setting = this.settingService
				.getClientListEtfDepotDefaultEtfId(this.getUserId());
		if (setting.isPresent()) {
			final var favoriteEtfId = setting.get().getSetting();
			if (favoriteEtfId.getId().equals(id)) {
				transport.setIsFavorite(1);
			}
		}

		return ResponseEntity.ok(transport);
	}

	@Override
	public ResponseEntity<EtfTransport> create(@RequestBody final EtfTransport etfTransport,
			@RequestHeader(value = HEADER_PREFER, required = false) final List<String> prefer) {
		final UserID userId = super.getUserId();
		final Etf etf = super.map(etfTransport, Etf.class);
		final User user = this.userService.getUserById(userId);
		final Group group = this.accessRelationService.getCurrentGroup(userId);
		etf.setId(null);
		etf.setUser(user);
		etf.setGroup(group);
		final ValidationResult validationResult = this.etfService.validateEtf(etf);

		this.throwValidationExceptionIfInvalid(validationResult);

		final EtfID etfId = this.etfService.createEtf(etf);

		etf.setId(etfId);
		if (Integer.valueOf(1).equals(etfTransport.getIsFavorite()))
			this.settingService.setClientListEtfDepotDefaultEtfId(userId, new ClientListEtfDepotDefaultEtfId(etfId));

		return this.preferedReturn(prefer, etf, EtfTransport.class);

	}

	@Override
	public ResponseEntity<EtfTransport> update(@RequestBody final EtfTransport etfTransport,
			@RequestHeader(value = HEADER_PREFER, required = false) final List<String> prefer) {
		final UserID userId = super.getUserId();
		final Etf etf = super.map(etfTransport, Etf.class);
		final User user = this.userService.getUserById(userId);
		final Group group = this.accessRelationService.getCurrentGroup(userId);
		etf.setUser(user);
		etf.setGroup(group);
		final ValidationResult validationResult = this.etfService.validateEtf(etf);

		this.throwValidationExceptionIfInvalid(validationResult);

		this.etfService.updateEtf(etf);
		if (Integer.valueOf(1).equals(etfTransport.getIsFavorite()))
			this.settingService.setClientListEtfDepotDefaultEtfId(userId,
					new ClientListEtfDepotDefaultEtfId(etf.getId()));

		return this.preferedReturn(prefer, etf, EtfTransport.class);
	}

	@Override
	public ResponseEntity<Void> delete(@PathVariable("id") final Long id) {
		final UserID userId = super.getUserId();
		final EtfID etfId = new EtfID(id);
		final Group group = this.accessRelationService.getCurrentGroup(userId);

		this.etfService.deleteEtf(userId, group.getId(), etfId);

		final Optional<ClientListEtfDepotDefaultEtfId> setting = this.settingService
				.getClientListEtfDepotDefaultEtfId(this.getUserId());
		if (setting.isPresent()) {
			final var favoriteEtfId = setting.get().getSetting();
			if (favoriteEtfId.equals(etfId)) {
				this.settingService.deleteSetting(userId, setting.get());
			}
		}

		return ResponseEntity.noContent().build();
	}
}
