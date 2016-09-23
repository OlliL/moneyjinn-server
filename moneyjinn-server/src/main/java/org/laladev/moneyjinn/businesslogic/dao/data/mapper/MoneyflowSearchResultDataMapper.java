//
// Copyright (c) 2015 Oliver Lehmann <oliver@laladev.org>
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

package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import java.time.Month;

import org.laladev.moneyjinn.businesslogic.dao.data.MoneyflowSearchResultData;
import org.laladev.moneyjinn.core.mapper.IMapper;
import org.laladev.moneyjinn.model.Contractpartner;
import org.laladev.moneyjinn.model.ContractpartnerID;
import org.laladev.moneyjinn.model.moneyflow.search.MoneyflowSearchResult;

public class MoneyflowSearchResultDataMapper implements IMapper<MoneyflowSearchResult, MoneyflowSearchResultData> {

	@Override
	public MoneyflowSearchResult mapBToA(final MoneyflowSearchResultData moneyflowSearchResultData) {
		final MoneyflowSearchResult moneyflowSearchResult = new MoneyflowSearchResult();

		moneyflowSearchResult.setAmount(moneyflowSearchResultData.getAmount());
		moneyflowSearchResult.setComment(moneyflowSearchResultData.getComment());
		if (moneyflowSearchResultData.getMonth() != null) {
			moneyflowSearchResult.setMonth(Month.of(moneyflowSearchResultData.getMonth().intValue()));
		}
		moneyflowSearchResult.setYear(moneyflowSearchResultData.getYear());
		moneyflowSearchResult.setContractpartner(
				new Contractpartner(new ContractpartnerID(moneyflowSearchResultData.getContractpartnerid())));

		return moneyflowSearchResult;
	}

	@Override
	public MoneyflowSearchResultData mapAToB(final MoneyflowSearchResult moneyflowSearchResult) {
		throw new UnsupportedOperationException("Mapping not supported!");
	}

}
