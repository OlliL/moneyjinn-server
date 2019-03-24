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

package org.laladev.moneyjinn.service.dao.data.mapper;

import org.laladev.moneyjinn.core.mapper.IMapper;
import org.laladev.moneyjinn.model.access.AccessID;
import org.laladev.moneyjinn.model.access.AccessRelation;
import org.laladev.moneyjinn.service.dao.data.AccessRelationData;

public class AccessRelationDataMapper implements IMapper<AccessRelation, AccessRelationData> {

	@Override
	public AccessRelation mapBToA(final AccessRelationData accessRelationData) {

		// Only one level is supported right now, and the second level is always "0"
		final AccessRelation parentAccessRelation = new AccessRelation(new AccessID(accessRelationData.getRefId()));
		parentAccessRelation.setParentAccessRelation(new AccessRelation(new AccessID(0L)));

		return new AccessRelation(new AccessID(accessRelationData.getId()), parentAccessRelation,
				accessRelationData.getValidFrom(), accessRelationData.getValidTil());
	}

	@Override
	public AccessRelationData mapAToB(final AccessRelation accessRelation) {

		final AccessRelationData accessRelationData = new AccessRelationData();
		accessRelationData.setId(accessRelation.getId().getId());
		accessRelationData.setValidFrom(accessRelation.getValidFrom());
		accessRelationData.setValidTil(accessRelation.getValidTil());
		if (accessRelation.getParentAccessRelation() != null) {
			accessRelationData.setRefId(accessRelation.getParentAccessRelation().getId().getId());
		}
		return accessRelationData;
	}
}
