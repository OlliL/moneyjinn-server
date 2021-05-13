//
// Copyright (c) 2015-2016 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.server.controller.mapper;

import java.time.LocalDate;

import org.laladev.moneyjinn.core.mapper.IMapper;
import org.laladev.moneyjinn.core.rest.model.moneyflow.transport.MoneyflowSearchParamsTransport;
import org.laladev.moneyjinn.model.ContractpartnerID;
import org.laladev.moneyjinn.model.PostingAccountID;
import org.laladev.moneyjinn.model.moneyflow.search.MoneyflowSearchParams;

public class MoneyflowSearchParamsTransportMapper
		implements IMapper<MoneyflowSearchParams, MoneyflowSearchParamsTransport> {

	private static final Short TRUE = (short) 1;

	@Override
	public MoneyflowSearchParams mapBToA(final MoneyflowSearchParamsTransport moneyflowSearchParamsTransport) {
		final MoneyflowSearchParams moneyflowSearchParams = new MoneyflowSearchParams();

		if (moneyflowSearchParamsTransport.getStartDate() != null) {
			final LocalDate startDate = moneyflowSearchParamsTransport.getStartDate().toLocalDate();
			moneyflowSearchParams.setStartDate(startDate);
		}
		if (moneyflowSearchParamsTransport.getEndDate() != null) {
			final LocalDate endDate = moneyflowSearchParamsTransport.getEndDate().toLocalDate();
			moneyflowSearchParams.setEndDate(endDate);
		}

		moneyflowSearchParams
				.setFeatureEqual(TRUE.equals(moneyflowSearchParamsTransport.getFeatureEqual()) ? true : false);
		moneyflowSearchParams.setFeatureCaseSensitive(
				TRUE.equals(moneyflowSearchParamsTransport.getFeatureCaseSensitive()) ? true : false);
		moneyflowSearchParams.setFeatureOnlyMinusAmounts(
				TRUE.equals(moneyflowSearchParamsTransport.getFeatureOnlyMinusAmounts()) ? true : false);
		moneyflowSearchParams
				.setFeatureRegexp(TRUE.equals(moneyflowSearchParamsTransport.getFeatureRegexp()) ? true : false);

		if (moneyflowSearchParamsTransport.getSearchString() != null
				&& !moneyflowSearchParamsTransport.getSearchString().isEmpty()) {
			moneyflowSearchParams.setSearchString(moneyflowSearchParamsTransport.getSearchString());
		}

		if (moneyflowSearchParamsTransport.getContractpartnerId() != null) {
			moneyflowSearchParams
					.setContractpartnerId(new ContractpartnerID(moneyflowSearchParamsTransport.getContractpartnerId()));
		}
		if (moneyflowSearchParamsTransport.getPostingAccountId() != null) {
			moneyflowSearchParams
					.setPostingAccountId(new PostingAccountID(moneyflowSearchParamsTransport.getPostingAccountId()));
		}

		return moneyflowSearchParams;
	}

	@Override
	public MoneyflowSearchParamsTransport mapAToB(final MoneyflowSearchParams moneyflowSearchParams) {
		throw new UnsupportedOperationException("Mapping not supported!");
	}
}
