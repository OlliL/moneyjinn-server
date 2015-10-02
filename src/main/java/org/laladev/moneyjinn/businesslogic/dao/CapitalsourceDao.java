//Copyright (c) 2015 Oliver Lehmann <oliver@laladev.org>
//All rights reserved.
//
//Redistribution and use in source and binary forms, with or without
//modification, are permitted provided that the following conditions
//are met:
//1. Redistributions of source code must retain the above copyright
//notice, this list of conditions and the following disclaimer
//2. Redistributions in binary form must reproduce the above copyright
//notice, this list of conditions and the following disclaimer in the
//documentation and/or other materials provided with the distribution.
//
//THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
//ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
//IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
//ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
//FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
//DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
//OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
//HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
//LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
//OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
//SUCH DAMAGE.

package org.laladev.moneyjinn.businesslogic.dao;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.laladev.moneyjinn.businesslogic.dao.data.CapitalsourceData;
import org.laladev.moneyjinn.businesslogic.dao.mapper.ICapitalsourceDaoMapper;

@Named
public class CapitalsourceDao {

	@Inject
	ICapitalsourceDaoMapper mapper;

	public List<CapitalsourceData> getAllCapitalsources(final Long userId) {
		return this.mapper.getAllCapitalsources(userId);
	}

	public List<CapitalsourceData> getAllCapitalsourcesByDateRange(final Long userId, final Date validFrom,
			final Date validTil) {
		return this.mapper.getAllCapitalsourcesByDateRange(userId, validFrom, validTil);
	}

	public CapitalsourceData getCapitalsourceById(final Long userId, final Long accessorId, final Long id) {
		return this.mapper.getCapitalsourceById(userId, accessorId, id);
	}

	public Integer countAllCapitalsources(final Long userId) {
		return this.mapper.countAllCapitalsources(userId);
	}

	public Integer countAllCapitalsourcesByDateRange(final Long userId, final Date validFrom, final Date validTil) {
		return this.mapper.countAllCapitalsourcesByDateRange(userId, validFrom, validTil);
	}

	public Set<Character> getAllCapitalsourceInitials(final Long userId) {
		return this.mapper.getAllCapitalsourceInitials(userId);
	}

	public Set<Character> getAllCapitalsourceInitialsByDateRange(final Long userId, final Date validFrom,
			final Date validTil) {
		return this.mapper.getAllCapitalsourceInitialsByDateRange(userId, validFrom, validTil);
	}

	public List<CapitalsourceData> getAllCapitalsourcesByInitial(final Long userId, final Character initial) {
		return this.mapper.getAllCapitalsourcesByInitial(userId, initial);
	}

	public List<CapitalsourceData> getAllCapitalsourcesByInitialAndDateRange(final Long userId, final Character initial,
			final Date validFrom, final Date validTil) {
		return this.mapper.getAllCapitalsourcesByInitialAndDateRange(userId, initial, validFrom, validTil);
	}

	public CapitalsourceData getCapitalsourceByComment(final Long userId, final String comment, final Date date) {
		return this.mapper.getCapitalsourceByComment(userId, comment, date);
	}

	public Long createCapitalsource(final CapitalsourceData capitalsourceData) {
		this.mapper.createCapitalsource(capitalsourceData);
		return capitalsourceData.getId();
	}

	public void updateCapitalsource(final CapitalsourceData capitalsourceData) {
		this.mapper.updateCapitalsource(capitalsourceData);
	}

	public void deleteCapitalsource(final Long userId, final Long accessorId, final Long id) {
		this.mapper.deleteCapitalsource(userId, accessorId, id);
	}

	public boolean checkCapitalsourceInUseOutOfDate(final Long userId, final Long id, final Date validFrom,
			final Date validTil) {
		final Boolean result = this.mapper.checkCapitalsourceInUseOutOfDate(userId, id, validFrom, validTil);
		if (result == null) {
			return false;
		}
		return result;
	}

	public List<CapitalsourceData> getGroupCapitalsources(final Long userId) {
		return this.mapper.getGroupCapitalsources(userId);
	}

	public List<CapitalsourceData> getGroupCapitalsourcesByDateRange(final Long userId, final Date validFrom,
			final Date validTil) {
		return this.mapper.getGroupCapitalsourcesByDateRange(userId, validFrom, validTil);
	}

}
