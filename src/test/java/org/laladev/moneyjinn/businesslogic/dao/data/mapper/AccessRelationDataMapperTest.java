package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import org.junit.Assert;
import org.junit.Test;
import org.laladev.moneyjinn.businesslogic.dao.data.AccessRelationData;
import org.laladev.moneyjinn.businesslogic.model.access.AccessID;
import org.laladev.moneyjinn.businesslogic.model.access.AccessRelation;

public class AccessRelationDataMapperTest {
	@Test
	public void testNoParentAccessRelation() {
		final AccessRelation accessRelation = new AccessRelation(new AccessID(0l));

		final AccessRelationDataMapper mapper = new AccessRelationDataMapper();

		final AccessRelationData accessRelationData = mapper.mapAToB(accessRelation);

		Assert.assertNull(accessRelationData.getRefId());
	}

}
