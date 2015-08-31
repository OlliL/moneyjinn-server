package org.laladev.moneyjinn.server.controller.mapper;

import java.sql.Date;
import java.time.LocalDate;

import org.laladev.moneyjinn.api.IMapper;
import org.laladev.moneyjinn.businesslogic.model.access.AccessID;
import org.laladev.moneyjinn.businesslogic.model.access.AccessRelation;
import org.laladev.moneyjinn.core.rest.model.transport.AccessRelationTransport;

public class AccessRelationTransportMapper implements IMapper<AccessRelation, AccessRelationTransport> {
	@Override
	public AccessRelation mapBToA(final AccessRelationTransport accessRelationTransport) {
		LocalDate validFrom = null;
		if (accessRelationTransport.getValidfrom() != null) {
			validFrom = accessRelationTransport.getValidfrom().toLocalDate();
		}
		LocalDate validTil = null;
		if (accessRelationTransport.getValidtil() != null) {
			validTil = accessRelationTransport.getValidtil().toLocalDate();
		}
		final AccessRelation accessRelation = new AccessRelation(new AccessID(accessRelationTransport.getId()), null,
				validFrom, validTil);

		if (accessRelationTransport.getRefId() != null) {
			accessRelation
					.setParentAccessRelation(new AccessRelation(new AccessID(accessRelationTransport.getRefId())));
		}
		return accessRelation;
	}

	@Override
	public AccessRelationTransport mapAToB(final AccessRelation accessRelation) {
		final Date validFrom = Date.valueOf(accessRelation.getValidFrom());
		final Date validTil = Date.valueOf(accessRelation.getValidTil());
		final AccessRelationTransport accessRelationTransport = new AccessRelationTransport();
		accessRelationTransport.setId(accessRelation.getId().getId());
		if (accessRelation.getParentAccessRelation() != null) {
			accessRelationTransport.setRefId(accessRelation.getParentAccessRelation().getId().getId());
		}
		accessRelationTransport.setValidfrom(validFrom);
		accessRelationTransport.setValidtil(validTil);

		return accessRelationTransport;
	}
}
