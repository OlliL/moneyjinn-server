//
// Copyright (c) 2015-2025 Oliver Lehmann <lehmann@ans-netz.de>
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

import java.time.LocalDate;
import java.util.List;

import org.laladev.moneyjinn.service.dao.data.ContractpartnerData;
import org.laladev.moneyjinn.service.dao.mapper.IContractpartnerDaoMapper;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;

@Named
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ContractpartnerDao {
	private final IContractpartnerDaoMapper mapper;

	public List<ContractpartnerData> getAllContractpartners(final Long userId) {
		return this.mapper.getAllContractpartners(userId);
	}

	public ContractpartnerData getContractpartnerById(final Long userId, final Long id) {
		return this.mapper.getContractpartnerById(userId, id);
	}

	public ContractpartnerData getContractpartnerByName(final Long userId, final String name) {
		return this.mapper.getContractpartnerByName(userId, name);
	}

	public Long createContractpartner(final ContractpartnerData contractpartnerData) {
		this.mapper.createContractpartner(contractpartnerData);
		return contractpartnerData.getId();
	}

	public void updateContractpartner(final ContractpartnerData contractpartnerData) {
		this.mapper.updateContractpartner(contractpartnerData);
	}

	public void deleteContractpartner(final Long groupId, final Long id) {
		this.mapper.deleteContractpartner(groupId, id);
	}

	public boolean checkContractpartnerInUseOutOfDate(final Long userId, final Long id, final LocalDate validFrom,
			final LocalDate validTil) {
		return Boolean.TRUE.equals(this.mapper.checkContractpartnerInUseOutOfDate(userId, id, validFrom, validTil));
	}
}
