package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.access.AccessID;
import org.laladev.moneyjinn.model.access.AccessRelation;
import org.laladev.moneyjinn.service.dao.data.AccessRelationData;
import org.laladev.moneyjinn.service.dao.data.mapper.AccessRelationDataMapper;

public class AccessRelationDataMapperTest {
	@Test
	public void testNoParentAccessRelation() {
		final AccessRelation accessRelation = new AccessRelation(new AccessID(0l));

		final AccessRelationDataMapper mapper = new AccessRelationDataMapper();

		final AccessRelationData accessRelationData = mapper.mapAToB(accessRelation);

		Assertions.assertNull(accessRelationData.getRefId());
	}

}
