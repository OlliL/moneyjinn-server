//Copyright (c) 2015-2025 Oliver Lehmann <lehmann@ans-netz.de>
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

import java.util.List;

import org.laladev.moneyjinn.model.PreDefMoneyflow;
import org.laladev.moneyjinn.model.PreDefMoneyflowID;
import org.laladev.moneyjinn.model.access.User;
import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.validation.ValidationResult;
import org.laladev.moneyjinn.server.controller.api.PreDefMoneyflowControllerApi;
import org.laladev.moneyjinn.server.controller.mapper.PreDefMoneyflowTransportMapper;
import org.laladev.moneyjinn.server.model.CreatePreDefMoneyflowRequest;
import org.laladev.moneyjinn.server.model.CreatePreDefMoneyflowResponse;
import org.laladev.moneyjinn.server.model.ShowPreDefMoneyflowListResponse;
import org.laladev.moneyjinn.server.model.UpdatePreDefMoneyflowRequest;
import org.laladev.moneyjinn.service.api.IPreDefMoneyflowService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class PreDefMoneyflowController extends AbstractController implements PreDefMoneyflowControllerApi {
	private final IPreDefMoneyflowService preDefMoneyflowService;
	private final PreDefMoneyflowTransportMapper preDefMoneyflowTransportMapper;

	@Override
	public ResponseEntity<ShowPreDefMoneyflowListResponse> showPreDefMoneyflowList() {
		final UserID userId = super.getUserId();
		final List<PreDefMoneyflow> preDefMoneyflows = this.preDefMoneyflowService.getAllPreDefMoneyflows(userId);
		final ShowPreDefMoneyflowListResponse response = new ShowPreDefMoneyflowListResponse();
		if (preDefMoneyflows != null && !preDefMoneyflows.isEmpty()) {
			response.setPreDefMoneyflowTransports(this.preDefMoneyflowTransportMapper.mapAToB(preDefMoneyflows));
		}
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<CreatePreDefMoneyflowResponse> createPreDefMoneyflow(
			@RequestBody final CreatePreDefMoneyflowRequest request) {
		final PreDefMoneyflow preDefMoneyflow = this.preDefMoneyflowTransportMapper
				.mapBToA(request.getPreDefMoneyflowTransport());
		final UserID userId = super.getUserId();
		preDefMoneyflow.setId(null);
		preDefMoneyflow.setUser(new User(userId));
		final ValidationResult validationResult = this.preDefMoneyflowService.validatePreDefMoneyflow(preDefMoneyflow);
		final CreatePreDefMoneyflowResponse response = new CreatePreDefMoneyflowResponse();

		this.throwValidationExceptionIfInvalid(validationResult);

		final PreDefMoneyflowID preDefMoneyflowId = this.preDefMoneyflowService.createPreDefMoneyflow(preDefMoneyflow);
		response.setPreDefMoneyflowId(preDefMoneyflowId.getId());
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<Void> updatePreDefMoneyflow(@RequestBody final UpdatePreDefMoneyflowRequest request) {
		final PreDefMoneyflow preDefMoneyflow = this.preDefMoneyflowTransportMapper
				.mapBToA(request.getPreDefMoneyflowTransport());
		final UserID userId = super.getUserId();
		preDefMoneyflow.setUser(new User(userId));
		final ValidationResult validationResult = this.preDefMoneyflowService.validatePreDefMoneyflow(preDefMoneyflow);

		this.throwValidationExceptionIfInvalid(validationResult);

		this.preDefMoneyflowService.updatePreDefMoneyflow(preDefMoneyflow);

		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<Void> deletePreDefMoneyflowById(final Long id) {
		final UserID userId = super.getUserId();
		final PreDefMoneyflowID preDefMoneyflowId = new PreDefMoneyflowID(id);

		this.preDefMoneyflowService.deletePreDefMoneyflow(userId, preDefMoneyflowId);

		return ResponseEntity.noContent().build();
	}

}
