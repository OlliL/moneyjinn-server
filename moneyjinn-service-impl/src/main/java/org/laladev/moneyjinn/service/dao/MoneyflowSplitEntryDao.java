//
// Copyright (c) 2016-2024 Oliver Lehmann <lehmann@ans-netz.de>
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

package org.laladev.moneyjinn.service.dao;

import java.util.Collections;
import java.util.List;

import org.laladev.moneyjinn.service.dao.data.MoneyflowSplitEntryData;
import org.laladev.moneyjinn.service.dao.mapper.IMoneyflowSplitEntryDaoMapper;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;

@Named
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class MoneyflowSplitEntryDao {
	private final IMoneyflowSplitEntryDaoMapper mapper;

	public List<MoneyflowSplitEntryData> getMoneyflowSplitEntries(final List<Long> moneyflowIds) {
		if (moneyflowIds.isEmpty()) {
			return Collections.emptyList();
		}
		return this.mapper.getMoneyflowSplitEntries(moneyflowIds);
	}

	public Long createMoneyflowSplitEntry(final MoneyflowSplitEntryData moneyflowSplitEntryData) {
		this.mapper.createMoneyflowSplitEntry(moneyflowSplitEntryData);
		return moneyflowSplitEntryData.getId();
	}

	public void updateMoneyflowSplitEntry(final MoneyflowSplitEntryData moneyflowSplitEntryData) {
		this.mapper.updateMoneyflowSplitEntry(moneyflowSplitEntryData);
	}

	public void deleteMoneyflowSplitEntry(final Long moneyflowId, final Long moneyflowSplitEntryId) {
		this.mapper.deleteMoneyflowSplitEntry(moneyflowId, moneyflowSplitEntryId);
	}

	public void deleteMoneyflowSplitEntries(final Long moneyflowId) {
		this.mapper.deleteMoneyflowSplitEntries(moneyflowId);
	}
}
