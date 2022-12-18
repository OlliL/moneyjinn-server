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

import org.laladev.moneyjinn.service.dao.data.AccessFlattenedData;
import org.laladev.moneyjinn.service.dao.data.AccessRelationData;
import org.laladev.moneyjinn.service.dao.mapper.IAccessRelationDaoMapper;

@Named
public class AccessRelationDao {

	@Inject
	IAccessRelationDaoMapper mapper;

	public AccessRelationData getAccessRelationById(final Long id, final LocalDate date) {
		return this.mapper.getAccessRelationById(id, date);
	}

	public List<AccessRelationData> getAllAccessRelationsById(final Long id) {
		return this.mapper.getAllAccessRelationsById(id);
	}

	public List<AccessRelationData> getAllAccessRelationsByIdDate(final Long id, final LocalDate date) {
		return this.mapper.getAllAccessRelationsByIdDate(id, date);
	}

	public void deleteAllAccessRelation(final Long id) {
		this.mapper.deleteAllAccessRelation(id);
	}

	public void deleteAccessRelationByDate(final Long id, final LocalDate date) {
		this.mapper.deleteAccessRelationByDate(id, date);
	}

	public void updateAccessRelation(final Long id, final LocalDate date, final AccessRelationData accessRelationData) {
		this.mapper.updateAccessRelation(id, date, accessRelationData);
	}

	public void createAccessRelation(final AccessRelationData accessRelationData) {
		this.mapper.createAccessRelation(accessRelationData);
	}

	public void deleteAllAccessFlattened(final Long id) {
		this.mapper.deleteAllAccessFlattened(id);
	}

	public void deleteAccessFlattenedAfter(final Long id, final LocalDate date) {
		this.mapper.deleteAccessFlattenedAfter(id, date);
	}

	public void createAccessFlattened(final AccessFlattenedData accessFlattenedData) {
		this.mapper.createAccessFlattened(accessFlattenedData);
	}

	public Set<Long> getAllUserWithSameGroup(final Long id) {
		return this.mapper.getAllUserWithSameGroup(id);
	}

}
