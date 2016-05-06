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

import org.laladev.moneyjinn.businesslogic.dao.data.AccessFlattenedData;
import org.laladev.moneyjinn.businesslogic.model.access.AccessID;
import org.laladev.moneyjinn.businesslogic.model.access.AccessRelation;
import org.laladev.moneyjinn.core.mapper.IMapper;

public class AccessFlattenedDataMapper implements IMapper<AccessRelation, AccessFlattenedData> {

	@Override
	public AccessRelation mapBToA(final AccessFlattenedData accessFlattenedData) {
		final AccessID accessId = new AccessID(accessFlattenedData.getId());

		AccessRelation parentAccessRelation = null;
		AccessRelation parentAccessRelationTmp = null;

		if (accessFlattenedData.getIdLevel5() != null) {
			parentAccessRelation = new AccessRelation(new AccessID(accessFlattenedData.getIdLevel5()));
		}

		if (accessFlattenedData.getIdLevel4() != null) {
			parentAccessRelationTmp = new AccessRelation(new AccessID(accessFlattenedData.getIdLevel4()));
			if (parentAccessRelation != null) {
				parentAccessRelationTmp.setParentAccessRelation(parentAccessRelation);
			}
			parentAccessRelation = parentAccessRelationTmp;
		}
		if (accessFlattenedData.getIdLevel3() != null) {
			parentAccessRelationTmp = new AccessRelation(new AccessID(accessFlattenedData.getIdLevel3()));
			if (parentAccessRelation != null) {
				parentAccessRelationTmp.setParentAccessRelation(parentAccessRelation);
			}
			parentAccessRelation = parentAccessRelationTmp;
		}
		if (accessFlattenedData.getIdLevel2() != null) {
			parentAccessRelationTmp = new AccessRelation(new AccessID(accessFlattenedData.getIdLevel2()));
			if (parentAccessRelation != null) {
				parentAccessRelationTmp.setParentAccessRelation(parentAccessRelation);
			}
			parentAccessRelation = parentAccessRelationTmp;
		}

		final AccessRelation accessRelation = new AccessRelation(accessId, null, accessFlattenedData.getValidFrom(),
				accessFlattenedData.getValidTil());

		if (parentAccessRelation != null) {
			accessRelation.setParentAccessRelation(parentAccessRelation);
		}
		return accessRelation;
	}

	@Override
	public AccessFlattenedData mapAToB(final AccessRelation accessRelation) {
		final AccessFlattenedData accessFlattenedData = new AccessFlattenedData();
		accessFlattenedData.setId(accessRelation.getId().getId());
		accessFlattenedData.setValidFrom(accessRelation.getValidFrom());
		accessFlattenedData.setValidTil(accessRelation.getValidTil());

		AccessRelation parentAccessRelation = accessRelation;
		accessFlattenedData.setIdLevel1(parentAccessRelation.getId().getId());
		parentAccessRelation = parentAccessRelation.getParentAccessRelation();

		if (parentAccessRelation != null) {
			accessFlattenedData.setIdLevel2(parentAccessRelation.getId().getId());
			parentAccessRelation = parentAccessRelation.getParentAccessRelation();
		}
		if (parentAccessRelation != null) {
			accessFlattenedData.setIdLevel3(parentAccessRelation.getId().getId());
			parentAccessRelation = parentAccessRelation.getParentAccessRelation();
		}
		if (parentAccessRelation != null) {
			accessFlattenedData.setIdLevel4(parentAccessRelation.getId().getId());
			parentAccessRelation = parentAccessRelation.getParentAccessRelation();
		}
		if (parentAccessRelation != null) {
			accessFlattenedData.setIdLevel5(parentAccessRelation.getId().getId());
		}
		return accessFlattenedData;
	}
}
