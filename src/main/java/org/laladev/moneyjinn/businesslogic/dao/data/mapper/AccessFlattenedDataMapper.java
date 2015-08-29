package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import java.sql.Date;
import java.time.LocalDate;

import org.laladev.moneyjinn.api.IMapper;
import org.laladev.moneyjinn.businesslogic.dao.data.AccessFlattenedData;
import org.laladev.moneyjinn.businesslogic.model.access.AccessID;
import org.laladev.moneyjinn.businesslogic.model.access.AccessRelation;

public class AccessFlattenedDataMapper implements IMapper<AccessRelation, AccessFlattenedData> {

	@Override
	public AccessRelation mapBToA(final AccessFlattenedData accessFlattenedData) {
		if (accessFlattenedData == null) {
			return null;
		}

		final AccessID accessId = new AccessID(accessFlattenedData.getId());
		final LocalDate validFrom = accessFlattenedData.getValidFrom().toLocalDate();
		final LocalDate validTil = accessFlattenedData.getValidTil().toLocalDate();

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

		final AccessRelation accessRelation = new AccessRelation(accessId, null, validFrom, validTil);

		if (parentAccessRelation != null) {
			accessRelation.setParentAccessRelation(parentAccessRelation);
		}
		return accessRelation;
	}

	@Override
	public AccessFlattenedData mapAToB(final AccessRelation accessRelation) {
		if (accessRelation == null) {
			return null;
		}

		Date validFrom = null;
		if (accessRelation.getValidFrom() != null) {
			validFrom = Date.valueOf(accessRelation.getValidFrom());
		}
		Date validTil = null;
		if (accessRelation.getValidTil() != null) {
			validTil = Date.valueOf(accessRelation.getValidTil());
		}

		final AccessFlattenedData accessFlattenedData = new AccessFlattenedData();
		accessFlattenedData.setId(accessRelation.getId().getId());
		accessFlattenedData.setValidFrom(validFrom);
		accessFlattenedData.setValidTil(validTil);

		AccessRelation parentAccessRelation = accessRelation;
		if (parentAccessRelation != null) {
			accessFlattenedData.setIdLevel1(parentAccessRelation.getId().getId());
			parentAccessRelation = parentAccessRelation.getParentAccessRelation();
		}
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
