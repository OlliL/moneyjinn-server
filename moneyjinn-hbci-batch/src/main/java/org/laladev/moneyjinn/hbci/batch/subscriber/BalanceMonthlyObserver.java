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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.laladev.moneyjinn.hbci.backend.ApiException;
import org.laladev.moneyjinn.hbci.backend.api.ImportedMonthlySettlementControllerApi;
import org.laladev.moneyjinn.hbci.backend.model.CreateImportedMonthlySettlementRequest;
import org.laladev.moneyjinn.hbci.backend.model.ImportedMonthlySettlementTransport;
import org.laladev.moneyjinn.hbci.batch.main.MoneyjinnApiClient;
import org.laladev.moneyjinn.hbci.core.entity.BalanceMonthly;

public class BalanceMonthlyObserver implements PropertyChangeListener {

	@Override
	public void propertyChange(final PropertyChangeEvent event) {
		if (event.getNewValue() instanceof final BalanceMonthly balanceMonthly) {
			this.notify(balanceMonthly);
		}

	}

	private void notify(final BalanceMonthly balanceMonthly) {
		final ImportedMonthlySettlementTransport transport = new ImportedMonthlySettlementTransport();
		transport.setAccountNumberCapitalsource(balanceMonthly.getMyIban());
		transport.setBankCodeCapitalsource(balanceMonthly.getMyBic());
		transport.setExternalid(balanceMonthly.getId().toString());
		transport.setMonth(balanceMonthly.getBalanceMonth());
		transport.setYear(balanceMonthly.getBalanceYear());
		transport.setAmount(balanceMonthly.getBalanceValue());

		final CreateImportedMonthlySettlementRequest request = new CreateImportedMonthlySettlementRequest();
		request.setImportedMonthlySettlementTransport(transport);

		try {
			new ImportedMonthlySettlementControllerApi(MoneyjinnApiClient.getApiClient())
					.createImportedMonthlySettlement(request);
		} catch (final ApiException e) {
			e.printStackTrace();
		}

	}

}
