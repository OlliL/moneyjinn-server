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

import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.etf.EtfFlow;
import org.laladev.moneyjinn.model.etf.EtfFlowID;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.server.controller.api.CrudEtfFlowControllerApi;
import org.laladev.moneyjinn.server.controller.impl.AbstractController;
import org.laladev.moneyjinn.server.controller.mapper.EtfFlowTransportMapper;
import org.laladev.moneyjinn.server.model.EtfFlowTransport;
import org.laladev.moneyjinn.service.api.IAccessRelationService;
import org.laladev.moneyjinn.service.api.IEtfService;
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
public class CrudEtfFlowController extends AbstractController implements CrudEtfFlowControllerApi {
	private final IAccessRelationService accessRelationService;
	private final IEtfService etfService;
	private final EtfFlowTransportMapper etfFlowTransportMapper;

	@Override
	@PostConstruct
	protected void addBeanMapper() {
		this.registerBeanMapper(this.etfFlowTransportMapper);
	}

	@Override
	public ResponseEntity<EtfFlowTransport> create(@RequestBody final EtfFlowTransport etfFlowTransport,
			@RequestHeader(value = HEADER_PREFER, required = false) final List<String> prefer) {
		final UserID userId = super.getUserId();
		final EtfFlow etfFlow = super.map(etfFlowTransport, EtfFlow.class);
		etfFlow.setId(null);
		final ValidationResult validationResult = this.etfService.validateEtfFlow(userId, etfFlow);

		this.throwValidationExceptionIfInvalid(validationResult);

		final EtfFlowID etfId = this.etfService.createEtfFlow(userId, etfFlow);

		etfFlow.setId(etfId);

		return this.preferedReturn(prefer, etfFlow, EtfFlowTransport.class);

	}

	@Override
	public ResponseEntity<EtfFlowTransport> update(@RequestBody final EtfFlowTransport etfFlowTransport,
			@RequestHeader(value = HEADER_PREFER, required = false) final List<String> prefer) {
		final UserID userId = super.getUserId();
		final EtfFlow etfFlow = super.map(etfFlowTransport, EtfFlow.class);
		final ValidationResult validationResult = this.etfService.validateEtfFlow(userId, etfFlow);

		this.throwValidationExceptionIfInvalid(validationResult);

		this.etfService.updateEtfFlow(userId, etfFlow);

		return this.preferedReturn(prefer, etfFlow, EtfFlowTransport.class);
	}

	@Override
	public ResponseEntity<Void> delete(@PathVariable("id") final Long id) {
		final UserID userId = super.getUserId();
		final EtfFlowID etfFlowId = new EtfFlowID(id);

		this.etfService.deleteEtfFlow(userId, etfFlowId);

		return ResponseEntity.noContent().build();
	}
}
