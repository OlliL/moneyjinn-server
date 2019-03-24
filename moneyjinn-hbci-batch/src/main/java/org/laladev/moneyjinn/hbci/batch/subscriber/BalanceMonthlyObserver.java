//
// Copyright (c) 2014-2015 Oliver Lehmann <lehmann@ans-netz.de>
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
// ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
// OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
// OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE.
//
// $Id: BalanceMonthlyObserver.java,v 1.7 2015/09/30 20:12:44 olivleh1 Exp $
//
package org.laladev.moneyjinn.hbci.batch.subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.laladev.moneyjinn.client.core.config.Configuration;
import org.laladev.moneyjinn.client.core.rest.util.MessageConverter;
import org.laladev.moneyjinn.client.core.rest.util.RequestInterceptor;
import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import org.laladev.moneyjinn.core.rest.model.importedmonthlysettlement.CreateImportedMonthlySettlementRequest;
import org.laladev.moneyjinn.core.rest.model.transport.ImportedMonthlySettlementTransport;
import org.laladev.moneyjinn.hbci.core.entity.BalanceMonthly;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

public class BalanceMonthlyObserver implements Observer {

	private final RestTemplate restTemplate;

	public BalanceMonthlyObserver() {
		this.restTemplate = new RestTemplate();

		this.restTemplate.getMessageConverters().clear();
		this.restTemplate.getMessageConverters().add(new MessageConverter());

		final List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
		interceptors.add(new RequestInterceptor());
		this.restTemplate.setInterceptors(interceptors);
	}

	@Override
	public void update(final Observable o, final Object arg) {
		if (arg instanceof BalanceMonthly) {
			this.notify((BalanceMonthly) arg);
		}

	}

	private void notify(final BalanceMonthly balanceMonthly) {
		final ImportedMonthlySettlementTransport transport = new ImportedMonthlySettlementTransport();
		transport.setAccountNumberCapitalsource(balanceMonthly.getMyIban());
		transport.setBankCodeCapitalsource(balanceMonthly.getMyBic());
		transport.setExternalid(balanceMonthly.getId().toString());
		transport.setMonth(balanceMonthly.getBalanceMonth().shortValue());
		transport.setYear(balanceMonthly.getBalanceYear().shortValue());
		transport.setAmount(balanceMonthly.getBalanceValue());

		final CreateImportedMonthlySettlementRequest request = new CreateImportedMonthlySettlementRequest();
		request.setImportedMonthlySettlementTransport(transport);

		final ValidationResponse response = this.restTemplate.postForObject(
				Configuration.ROOT_URL + "/importedmonthlysettlement/createImportedMonthlySettlement", request,
				ValidationResponse.class);

		if (response != null) {
			if (response.getErrorResponse() != null) {
				throw (new RuntimeException("error: " + response.getErrorResponse().getMessage()));
			} else if (response.getResult().equals(Boolean.FALSE)) {
				throw (new RuntimeException(
						"error: " + response.getValidationItemTransports().get(0).getError().toString()));
			}

		}
	}

}
