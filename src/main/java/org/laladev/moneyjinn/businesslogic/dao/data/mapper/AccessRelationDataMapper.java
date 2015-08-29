package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import java.sql.Date;
import java.time.LocalDate;

import org.laladev.moneyjinn.api.IMapper;
import org.laladev.moneyjinn.businesslogic.dao.data.AccessRelationData;
import org.laladev.moneyjinn.businesslogic.model.access.AccessID;
import org.laladev.moneyjinn.businesslogic.model.access.AccessRelation;

public class AccessRelationDataMapper implements IMapper<AccessRelation, AccessRelationData> {

	@Override
	public AccessRelation mapBToA(final AccessRelationData accessRelationData) {
		if (accessRelationData == null) {
			return null;
		}

		final LocalDate validFrom = accessRelationData.getValidFrom().toLocalDate();
		final LocalDate validTil = accessRelationData.getValidTil().toLocalDate();

		// Only one level is supported right now, and the second level is always "0"
		final AccessRelation parentAccessRelation = new AccessRelation(new AccessID(accessRelationData.getRefId()));
		parentAccessRelation.setParentAccessRelation(new AccessRelation(new AccessID(0l)));

		final AccessRelation accessRelation = new AccessRelation(new AccessID(accessRelationData.getId()),
				parentAccessRelation, validFrom, validTil);
		return accessRelation;
	}

	@Override
	public AccessRelationData mapAToB(final AccessRelation accessRelation) {
		if (accessRelation == null) {
			return null;
		}

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
