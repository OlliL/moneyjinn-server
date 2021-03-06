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

package org.laladev.moneyjinn.service.dao;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.laladev.moneyjinn.service.dao.data.PreDefMoneyflowData;
import org.laladev.moneyjinn.service.dao.mapper.IPreDefMoneyflowDaoMapper;

@Named
public class PreDefMoneyflowDao {

	@Inject
	IPreDefMoneyflowDaoMapper mapper;

	public List<PreDefMoneyflowData> getAllPreDefMoneyflows(final Long userId) {
		return this.mapper.getAllPreDefMoneyflows(userId);
	}

	public PreDefMoneyflowData getPreDefMoneyflowById(final Long userId, final Long id) {
		return this.mapper.getPreDefMoneyflowById(userId, id);
	}

	public Integer countAllPreDefMoneyflows(final Long userId) {
		return this.mapper.countAllPreDefMoneyflows(userId);
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

	public List<Long> getAllContractpartnerIds(final Long userId) {
		return this.mapper.getAllContractpartnerIds(userId);
	}

	public void setLastUsed(final Long userId, final Long id) {
		this.mapper.setLastUsed(userId, id);
	}

}
