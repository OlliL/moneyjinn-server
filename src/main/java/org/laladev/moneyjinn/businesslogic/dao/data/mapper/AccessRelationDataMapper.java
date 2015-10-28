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

import java.sql.Date;
import java.time.LocalDate;

import org.laladev.moneyjinn.businesslogic.dao.data.AccessRelationData;
import org.laladev.moneyjinn.businesslogic.model.access.AccessID;
import org.laladev.moneyjinn.businesslogic.model.access.AccessRelation;
import org.laladev.moneyjinn.core.mapper.IMapper;

public class AccessRelationDataMapper implements IMapper<AccessRelation, AccessRelationData> {

	@Override
	public AccessRelation mapBToA(final AccessRelationData accessRelationData) {
		final LocalDate validFrom = accessRelationData.getValidFrom().toLocalDate();
		final LocalDate validTil = accessRelationData.getValidTil().toLocalDate();

		// Only one level is supported right now, and the second level is always "0"
		final AccessRelation parentAccessRelation = new AccessRelation(new AccessID(accessRelationData.getRefId()));
		parentAccessRelation.setParentAccessRelation(new AccessRelation(new AccessID(0L)));

		return new AccessRelation(new AccessID(accessRelationData.getId()), parentAccessRelation, validFrom, validTil);
	}

	@Override
	public AccessRelationData mapAToB(final AccessRelation accessRelation) {
		final Date validFrom = Date.valueOf(accessRelation.getValidFrom());
		final Date validTil = Date.valueOf(accessRelation.getValidTil());

		final AccessRelationData accessRelationData = new AccessRelationData();
		accessRelationData.setId(accessRelation.getId().getId());
		accessRelationData.setValidFrom(validFrom);
		accessRelationData.setValidTil(validTil);
		if (accessRelation.getParentAccessRelation() != null) {
			accessRelationData.setRefId(accessRelation.getParentAccessRelation().getId().getId());
		}
		return accessRelationData;
	}
}
