//
// Copyright (c) 2015-2024 Oliver Lehmann <lehmann@ans-netz.de>
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

import java.util.List;

import org.laladev.moneyjinn.service.dao.data.PreDefMoneyflowData;
import org.laladev.moneyjinn.service.dao.mapper.IPreDefMoneyflowDaoMapper;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;

@Named
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class PreDefMoneyflowDao {
	private final IPreDefMoneyflowDaoMapper mapper;

	public List<PreDefMoneyflowData> getAllPreDefMoneyflows(final Long userId) {
		return this.mapper.getAllPreDefMoneyflows(userId);
	}

	public PreDefMoneyflowData getPreDefMoneyflowById(final Long userId, final Long id) {
		return this.mapper.getPreDefMoneyflowById(userId, id);
	}

	public Long createPreDefMoneyflow(final PreDefMoneyflowData preDefMoneyflowData) {
		this.mapper.createPreDefMoneyflow(preDefMoneyflowData);
		return preDefMoneyflowData.getId();
	}

	public void updatePreDefMoneyflow(final PreDefMoneyflowData preDefMoneyflowData) {
		this.mapper.updatePreDefMoneyflow(preDefMoneyflowData);
	}

	public void deletePreDefMoneyflow(final Long userId, final Long id) {
		this.mapper.deletePreDefMoneyflow(userId, id);
	}

	public void setLastUsed(final Long userId, final Long id) {
		this.mapper.setLastUsed(userId, id);
	}
}
