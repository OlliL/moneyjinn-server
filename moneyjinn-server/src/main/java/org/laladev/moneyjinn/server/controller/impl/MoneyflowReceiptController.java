//Copyright (c) 2017-2025 Oliver Lehmann <lehmann@ans-netz.de>
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

import java.util.Base64;

import org.laladev.moneyjinn.model.access.UserID;
import org.laladev.moneyjinn.model.moneyflow.Moneyflow;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowID;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowReceipt;
import org.laladev.moneyjinn.server.controller.api.MoneyflowReceiptControllerApi;
import org.laladev.moneyjinn.server.controller.mapper.MoneyflowReceiptTypeMapper;
import org.laladev.moneyjinn.server.model.ShowMoneyflowReceiptResponse;
import org.laladev.moneyjinn.service.api.IMoneyflowReceiptService;
import org.laladev.moneyjinn.service.api.IMoneyflowService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

@RestController
@Transactional(propagation = Propagation.REQUIRES_NEW)

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class MoneyflowReceiptController extends AbstractController implements MoneyflowReceiptControllerApi {
	private final IMoneyflowService moneyflowService;
	private final IMoneyflowReceiptService moneyflowReceiptService;

	@Override
	public ResponseEntity<ShowMoneyflowReceiptResponse> showMoneyflowReceipt(final Long id) {
		final UserID userId = super.getUserId();
		final MoneyflowID moneyflowId = new MoneyflowID(id);
		final ShowMoneyflowReceiptResponse response = new ShowMoneyflowReceiptResponse();

		final Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);
		if (moneyflow != null) {
			final MoneyflowReceipt moneyflowReceipt = this.moneyflowReceiptService.getMoneyflowReceipt(userId,
					moneyflowId);
			if (moneyflowReceipt != null) {
				response.setReceipt(Base64.getEncoder().encodeToString(moneyflowReceipt.getReceipt()));
				response.setReceiptType(MoneyflowReceiptTypeMapper.map(moneyflowReceipt.getMoneyflowReceiptType()));
			}
		}

		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<Void> deleteMoneyflowReceipt(final Long id) {
		final UserID userId = super.getUserId();
		final MoneyflowID moneyflowId = new MoneyflowID(id);

		final Moneyflow moneyflow = this.moneyflowService.getMoneyflowById(userId, moneyflowId);
		if (moneyflow != null) {
			this.moneyflowReceiptService.deleteMoneyflowReceipt(userId, moneyflowId);
		}

		return ResponseEntity.noContent().build();
	}
}
