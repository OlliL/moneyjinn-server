//
// Copyright (c) 2015 Oliver Lehmann <lehmann@ans-netz.de>
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
// $Id: BalanceDailyObserver.java,v 1.6 2015/09/11 18:43:05 olivleh1 Exp $
//
package org.laladev.moneyjinn.hbci.batch.subscriber;

import java.util.Observable;
import java.util.Observer;

import org.laladev.moneyjinn.core.rest.model.ValidationResponse;
import org.laladev.moneyjinn.core.rest.model.importedbalance.CreateImportedBalanceRequest;
import org.laladev.moneyjinn.core.rest.model.transport.ImportedBalanceTransport;
import org.laladev.moneyjinn.hbci.batch.config.Configuration;
import org.laladev.moneyjinn.hbci.batch.config.MessageConverter;
import org.laladev.moneyjinn.hbci.core.entity.BalanceDaily;
import org.springframework.web.client.RestTemplate;

public class BalanceDailyObserver implements Observer {

	private final RestTemplate restTemplate;

	public BalanceDailyObserver() {
		this.restTemplate = new RestTemplate();

		this.restTemplate.getMessageConverters().clear();
		this.restTemplate.getMessageConverters().add(new MessageConverter());
	}

	@Override
	public void update(final Observable o, final Object arg) {
		if (arg instanceof BalanceDaily) {
			this.notify((BalanceDaily) arg);
		}

	}

	private void notify(final BalanceDaily balanceDaily) {
		final ImportedBalanceTransport transport = new ImportedBalanceTransport();
		transport.setAccountNumberCapitalsource(balanceDaily.getMyIban());
		transport.setBankCodeCapitalsource(balanceDaily.getMyBic());
		transport.setBalance(balanceDaily.getBalanceAvailableValue());

		final CreateImportedBalanceRequest request = new CreateImportedBalanceRequest();
		request.setImportedBalanceTransport(transport);

		final ValidationResponse response = this.restTemplate.postForObject(
				Configuration.ROOT_URL + "/importedbalance/createImportedBalance", request, ValidationResponse.class);

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
