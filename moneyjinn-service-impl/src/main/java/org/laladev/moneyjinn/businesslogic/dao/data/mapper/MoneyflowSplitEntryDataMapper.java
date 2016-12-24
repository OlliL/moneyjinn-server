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

import org.laladev.moneyjinn.businesslogic.dao.data.MoneyflowSplitEntryData;
import org.laladev.moneyjinn.core.mapper.IMapper;
import org.laladev.moneyjinn.model.PostingAccount;
import org.laladev.moneyjinn.model.PostingAccountID;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowID;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowSplitEntry;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowSplitEntryID;

public class MoneyflowSplitEntryDataMapper implements IMapper<MoneyflowSplitEntry, MoneyflowSplitEntryData> {

	@Override
	public MoneyflowSplitEntry mapBToA(final MoneyflowSplitEntryData moneyflowSplitEntryData) {
		final MoneyflowSplitEntry moneyflowSplitEntry = new MoneyflowSplitEntry();
		moneyflowSplitEntry.setId(new MoneyflowSplitEntryID(moneyflowSplitEntryData.getId()));
		moneyflowSplitEntry.setMoneyflowId(new MoneyflowID(moneyflowSplitEntryData.getMmfMoneyflowId()));
		moneyflowSplitEntry.setAmount(moneyflowSplitEntryData.getAmount());

		moneyflowSplitEntry.setComment(moneyflowSplitEntryData.getComment());
		moneyflowSplitEntry.setPostingAccount(
				new PostingAccount(new PostingAccountID(moneyflowSplitEntryData.getMpaPostingAccountId())));

		return moneyflowSplitEntry;
	}

	@Override
	public MoneyflowSplitEntryData mapAToB(final MoneyflowSplitEntry moneyflowSplitEntry) {
		final MoneyflowSplitEntryData moneyflowSplitEntryData = new MoneyflowSplitEntryData();
		// might be null for new MoneyflowSplitEntrys
		if (moneyflowSplitEntry.getId() != null) {
			moneyflowSplitEntryData.setId(moneyflowSplitEntry.getId().getId());
		}
		moneyflowSplitEntryData.setMmfMoneyflowId(moneyflowSplitEntry.getMoneyflowId().getId());
		moneyflowSplitEntryData.setAmount(moneyflowSplitEntry.getAmount());
		moneyflowSplitEntryData.setComment(moneyflowSplitEntry.getComment());
		moneyflowSplitEntryData.setMpaPostingAccountId(moneyflowSplitEntry.getPostingAccount().getId().getId());

		return moneyflowSplitEntryData;
	}
}
