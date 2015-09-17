package org.laladev.moneyjinn.server.controller.mapper;

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

import java.sql.Date;
import java.time.LocalDate;

import org.laladev.moneyjinn.businesslogic.model.access.AccessID;
import org.laladev.moneyjinn.businesslogic.model.access.AccessRelation;
import org.laladev.moneyjinn.core.mapper.IMapper;
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
