
package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.access.AccessID;
import org.laladev.moneyjinn.model.access.AccessRelation;
import org.laladev.moneyjinn.service.dao.data.AccessFlattenedData;
import org.laladev.moneyjinn.service.dao.data.mapper.AccessFlattenedDataMapper;

class AccessFlattenedDataMapperTest {
	@Test
	void testAccessRelationToData() {
		final LocalDate validFrom = LocalDate.parse("2015-01-01");
		final LocalDate validTil = LocalDate.parse("2015-12-01");
		final AccessRelation accessRelationMain = new AccessRelation(new AccessID(0l));
		final AccessRelation accessRelation1 = new AccessRelation(new AccessID(1l));
		final AccessRelation accessRelation2 = new AccessRelation(new AccessID(2l));
		final AccessRelation accessRelation3 = new AccessRelation(new AccessID(3l));
		final AccessRelation accessRelation4 = new AccessRelation(new AccessID(4l));
		final AccessRelation accessRelation5 = new AccessRelation(new AccessID(5l));
		accessRelation4.setParentAccessRelation(accessRelation5);
		accessRelation3.setParentAccessRelation(accessRelation4);
		accessRelation2.setParentAccessRelation(accessRelation3);
		accessRelation1.setParentAccessRelation(accessRelation2);
		accessRelationMain.setParentAccessRelation(accessRelation1);
		accessRelationMain.setValidFrom(validFrom);
		accessRelationMain.setValidTil(validTil);
		final AccessFlattenedDataMapper mapper = new AccessFlattenedDataMapper();
		final AccessFlattenedData accessFlattenedData = mapper.mapAToB(accessRelationMain);
		Assertions.assertEquals(Long.valueOf(0l), accessFlattenedData.getId());
		Assertions.assertEquals(Long.valueOf(0l), accessFlattenedData.getIdLevel1());
		Assertions.assertEquals(Long.valueOf(1l), accessFlattenedData.getIdLevel2());
		Assertions.assertEquals(Long.valueOf(2l), accessFlattenedData.getIdLevel3());
		Assertions.assertEquals(Long.valueOf(3l), accessFlattenedData.getIdLevel4());
		Assertions.assertEquals(Long.valueOf(4l), accessFlattenedData.getIdLevel5());
		Assertions.assertEquals(validFrom, accessFlattenedData.getValidFrom());
		Assertions.assertEquals(validTil, accessFlattenedData.getValidTil());
	}

	@Test
	void testAccessFlattenedDataToModel() {
		final LocalDate validFrom = LocalDate.parse("2015-01-01");
		final LocalDate validTil = LocalDate.parse("2015-12-01");
		final AccessFlattenedData accessFlattenedData = new AccessFlattenedData();
		accessFlattenedData.setId(0l);
		accessFlattenedData.setIdLevel1(0l);
		accessFlattenedData.setIdLevel2(1l);
		accessFlattenedData.setIdLevel3(2l);
		accessFlattenedData.setIdLevel4(3l);
		accessFlattenedData.setIdLevel5(4l);
		accessFlattenedData.setValidFrom(validFrom);
		accessFlattenedData.setValidTil(validTil);
		final AccessFlattenedDataMapper mapper = new AccessFlattenedDataMapper();
		final AccessRelation accessRelation = mapper.mapBToA(accessFlattenedData);
		Assertions.assertEquals(Long.valueOf(0l), accessRelation.getId().getId());
		Assertions.assertEquals(Long.valueOf(1l), accessRelation.getParentAccessRelation().getId().getId());
		Assertions.assertEquals(Long.valueOf(2l),
				accessRelation.getParentAccessRelation().getParentAccessRelation().getId().getId());
		Assertions.assertEquals(Long.valueOf(3l), accessRelation.getParentAccessRelation().getParentAccessRelation()
				.getParentAccessRelation().getId().getId());
		Assertions.assertEquals(Long.valueOf(4l), accessRelation.getParentAccessRelation().getParentAccessRelation()
				.getParentAccessRelation().getParentAccessRelation().getId().getId());
		Assertions.assertEquals(accessRelation.getValidFrom(), validFrom);
		Assertions.assertEquals(accessRelation.getValidTil(), validTil);
	}

	@Test
	void testEmptyAccessRelationToData() {
		final LocalDate validFrom = LocalDate.parse("2015-01-01");
		final LocalDate validTil = LocalDate.parse("2015-12-01");
		final AccessRelation accessRelationMain = new AccessRelation(new AccessID(0l));
		accessRelationMain.setValidFrom(validFrom);
		accessRelationMain.setValidTil(validTil);
		final AccessFlattenedDataMapper mapper = new AccessFlattenedDataMapper();
		final AccessFlattenedData accessFlattenedData = mapper.mapAToB(accessRelationMain);
		Assertions.assertEquals(Long.valueOf(0l), accessFlattenedData.getId());
		Assertions.assertEquals(Long.valueOf(0l), accessFlattenedData.getIdLevel1());
		Assertions.assertNull(accessFlattenedData.getIdLevel2());
		Assertions.assertNull(accessFlattenedData.getIdLevel3());
		Assertions.assertNull(accessFlattenedData.getIdLevel4());
		Assertions.assertNull(accessFlattenedData.getIdLevel5());
		Assertions.assertEquals(validFrom, accessFlattenedData.getValidFrom());
		Assertions.assertEquals(validTil, accessFlattenedData.getValidTil());
	}

	@Test
	void testEmptyAccessFlattenedDataToModel() {
		final LocalDate validFrom = LocalDate.parse("2015-01-01");
		final LocalDate validTil = LocalDate.parse("2015-12-01");
		final AccessFlattenedData accessFlattenedData = new AccessFlattenedData();
		accessFlattenedData.setId(0l);
		accessFlattenedData.setIdLevel1(0l);
		accessFlattenedData.setValidFrom(validFrom);
		accessFlattenedData.setValidTil(validTil);
		final AccessFlattenedDataMapper mapper = new AccessFlattenedDataMapper();
		final AccessRelation accessRelation = mapper.mapBToA(accessFlattenedData);
		Assertions.assertEquals(Long.valueOf(0l), accessRelation.getId().getId());
		Assertions.assertNull(accessRelation.getParentAccessRelation());
		Assertions.assertEquals(accessRelation.getValidFrom(), validFrom);
		Assertions.assertEquals(accessRelation.getValidTil(), validTil);
	}
}
