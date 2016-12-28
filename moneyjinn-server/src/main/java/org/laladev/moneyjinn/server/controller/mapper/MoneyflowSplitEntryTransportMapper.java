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

package org.laladev.moneyjinn.server.controller.mapper;

import org.laladev.moneyjinn.core.mapper.IMapper;
import org.laladev.moneyjinn.core.rest.model.transport.MoneyflowSplitEntryTransport;
import org.laladev.moneyjinn.model.PostingAccount;
import org.laladev.moneyjinn.model.PostingAccountID;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowID;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowSplitEntry;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowSplitEntryID;

public class MoneyflowSplitEntryTransportMapper implements IMapper<MoneyflowSplitEntry, MoneyflowSplitEntryTransport> {

	@Override
	public MoneyflowSplitEntry mapBToA(final MoneyflowSplitEntryTransport moneyflowSplitEntryTransport) {
		final MoneyflowSplitEntry moneyflowSplitEntry = new MoneyflowSplitEntry();
		if (moneyflowSplitEntryTransport.getId() != null) {
			moneyflowSplitEntry.setId(new MoneyflowSplitEntryID(moneyflowSplitEntryTransport.getId()));
		}
		moneyflowSplitEntry.setMoneyflowId(new MoneyflowID(moneyflowSplitEntryTransport.getMoneyflowid()));
		moneyflowSplitEntry.setAmount(moneyflowSplitEntryTransport.getAmount());
		moneyflowSplitEntry.setComment(moneyflowSplitEntryTransport.getComment());

		if (moneyflowSplitEntryTransport.getPostingaccountid() != null) {
			final PostingAccount postingAccount = new PostingAccount(new PostingAccountID(moneyflowSplitEntryTransport.getPostingaccountid()));
			moneyflowSplitEntry.setPostingAccount(postingAccount);
		}

		return moneyflowSplitEntry;
	}

	@Override
	public MoneyflowSplitEntryTransport mapAToB(final MoneyflowSplitEntry moneyflowSplitEntry) {
		final MoneyflowSplitEntryTransport moneyflowSplitEntryTransport = new MoneyflowSplitEntryTransport();
		moneyflowSplitEntryTransport.setId(moneyflowSplitEntry.getId().getId());

		moneyflowSplitEntryTransport.setMoneyflowid(moneyflowSplitEntry.getMoneyflowId().getId());
		moneyflowSplitEntryTransport.setAmount(moneyflowSplitEntry.getAmount());
		moneyflowSplitEntryTransport.setComment(moneyflowSplitEntry.getComment());

		final PostingAccount postingAccount = moneyflowSplitEntry.getPostingAccount();
		moneyflowSplitEntryTransport.setPostingaccountid(postingAccount.getId().getId());
		moneyflowSplitEntryTransport.setPostingaccountname(postingAccount.getName());

		return moneyflowSplitEntryTransport;
	}
}
