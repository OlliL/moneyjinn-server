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

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import jakarta.inject.Inject;
import jakarta.inject.Named;

import org.laladev.moneyjinn.service.dao.data.ContractpartnerData;
import org.laladev.moneyjinn.service.dao.mapper.IContractpartnerDaoMapper;

@Named
public class ContractpartnerDao {

	@Inject
	IContractpartnerDaoMapper mapper;

	public List<ContractpartnerData> getAllContractpartners(final Long userId) {
		return this.mapper.getAllContractpartners(userId);
	}

	public List<ContractpartnerData> getAllContractpartnersByDateRange(final Long userId, final LocalDate validFrom,
			final LocalDate validTil) {
		return this.mapper.getAllContractpartnersByDateRange(userId, validFrom, validTil);
	}

	public ContractpartnerData getContractpartnerById(final Long userId, final Long id) {
		return this.mapper.getContractpartnerById(userId, id);
	}

	public Integer countAllContractpartners(final Long userId) {
		return this.mapper.countAllContractpartners(userId);
	}

	public Integer countAllContractpartnersByDateRange(final Long userId, final LocalDate validFrom,
			final LocalDate validTil) {
		return this.mapper.countAllContractpartnersByDateRange(userId, validFrom, validTil);
	}

	public Set<Character> getAllContractpartnerInitials(final Long userId) {
		return this.mapper.getAllContractpartnerInitials(userId);
	}

	public Set<Character> getAllContractpartnerInitialsByDateRange(final Long userId, final LocalDate validFrom,
			final LocalDate validTil) {
		return this.mapper.getAllContractpartnerInitialsByDateRange(userId, validFrom, validTil);
	}

	public List<ContractpartnerData> getAllContractpartnersByInitial(final Long userId, final Character initial) {
		final String initialString = String.valueOf(initial).replaceAll("([_%])", "\\\\$1");
		return this.mapper.getAllContractpartnersByInitial(userId, initialString);
	}

	public List<ContractpartnerData> getAllContractpartnersByInitialAndDateRange(final Long userId,
			final Character initial, final LocalDate validFrom, final LocalDate validTil) {
		final String initialString = String.valueOf(initial).replaceAll("([_%])", "\\\\$1");
		return this.mapper.getAllContractpartnersByInitialAndDateRange(userId, initialString, validFrom, validTil);
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
		final Boolean result = this.mapper.checkContractpartnerInUseOutOfDate(userId, id, validFrom, validTil);
		if (result == null) {
			return false;
		}
		return result;
	}

}
