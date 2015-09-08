package org.laladev.moneyjinn.server.dao.data.mapper;

import java.sql.Date;
import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;
import org.laladev.moneyjinn.businesslogic.dao.data.AccessFlattenedData;
import org.laladev.moneyjinn.businesslogic.dao.data.mapper.AccessFlattenedDataMapper;
import org.laladev.moneyjinn.businesslogic.model.access.AccessID;
import org.laladev.moneyjinn.businesslogic.model.access.AccessRelation;

public class AccessFlattenedDataMapperTest {
	@Test
	public void testAccessRelationToData() {
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

		Assert.assertEquals(Long.valueOf(0l), accessFlattenedData.getId());
		Assert.assertEquals(Long.valueOf(0l), accessFlattenedData.getIdLevel1());
		Assert.assertEquals(Long.valueOf(1l), accessFlattenedData.getIdLevel2());
		Assert.assertEquals(Long.valueOf(2l), accessFlattenedData.getIdLevel3());
		Assert.assertEquals(Long.valueOf(3l), accessFlattenedData.getIdLevel4());
		Assert.assertEquals(Long.valueOf(4l), accessFlattenedData.getIdLevel5());
		Assert.assertEquals(Date.valueOf(validFrom), accessFlattenedData.getValidFrom());
		Assert.assertEquals(Date.valueOf(validTil), accessFlattenedData.getValidTil());
	}

	@Test
	public void testAccessFlattenedDataToModel() {
		final LocalDate validFrom = LocalDate.parse("2015-01-01");
		final LocalDate validTil = LocalDate.parse("2015-12-01");

		final AccessFlattenedData accessFlattenedData = new AccessFlattenedData();
		accessFlattenedData.setId(0l);
		accessFlattenedData.setIdLevel1(0l);
		accessFlattenedData.setIdLevel2(1l);
		accessFlattenedData.setIdLevel3(2l);
		accessFlattenedData.setIdLevel4(3l);
		accessFlattenedData.setIdLevel5(4l);
		accessFlattenedData.setValidFrom(Date.valueOf(validFrom));
		accessFlattenedData.setValidTil(Date.valueOf(validTil));

		final AccessFlattenedDataMapper mapper = new AccessFlattenedDataMapper();

		final AccessRelation accessRelation = mapper.mapBToA(accessFlattenedData);

		Assert.assertEquals(Long.valueOf(0l), accessRelation.getId().getId());
		Assert.assertEquals(Long.valueOf(1l), accessRelation.getParentAccessRelation().getId().getId());
		Assert.assertEquals(Long.valueOf(2l),
				accessRelation.getParentAccessRelation().getParentAccessRelation().getId().getId());
		Assert.assertEquals(Long.valueOf(3l), accessRelation.getParentAccessRelation().getParentAccessRelation()
				.getParentAccessRelation().getId().getId());
		Assert.assertEquals(Long.valueOf(4l), accessRelation.getParentAccessRelation().getParentAccessRelation()
				.getParentAccessRelation().getParentAccessRelation().getId().getId());
		Assert.assertEquals(accessRelation.getValidFrom(), validFrom);
		Assert.assertEquals(accessRelation.getValidTil(), validTil);
	}

	@Test
	public void testEmptyAccessRelationToData() {
		final LocalDate validFrom = LocalDate.parse("2015-01-01");
		final LocalDate validTil = LocalDate.parse("2015-12-01");

		final AccessRelation accessRelationMain = new AccessRelation(new AccessID(0l));
		accessRelationMain.setValidFrom(validFrom);
		accessRelationMain.setValidTil(validTil);

		final AccessFlattenedDataMapper mapper = new AccessFlattenedDataMapper();

		final AccessFlattenedData accessFlattenedData = mapper.mapAToB(accessRelationMain);

		Assert.assertEquals(Long.valueOf(0l), accessFlattenedData.getId());
		Assert.assertEquals(Long.valueOf(0l), accessFlattenedData.getIdLevel1());
		Assert.assertNull(accessFlattenedData.getIdLevel2());
		Assert.assertNull(accessFlattenedData.getIdLevel3());
		Assert.assertNull(accessFlattenedData.getIdLevel4());
		Assert.assertNull(accessFlattenedData.getIdLevel5());
		Assert.assertEquals(Date.valueOf(validFrom), accessFlattenedData.getValidFrom());
		Assert.assertEquals(Date.valueOf(validTil), accessFlattenedData.getValidTil());
	}

	@Test
	public void testEmptyAccessFlattenedDataToModel() {
		final LocalDate validFrom = LocalDate.parse("2015-01-01");
		final LocalDate validTil = LocalDate.parse("2015-12-01");

		final AccessFlattenedData accessFlattenedData = new AccessFlattenedData();
		accessFlattenedData.setId(0l);
		accessFlattenedData.setIdLevel1(0l);
		accessFlattenedData.setValidFrom(Date.valueOf(validFrom));
		accessFlattenedData.setValidTil(Date.valueOf(validTil));

		final AccessFlattenedDataMapper mapper = new AccessFlattenedDataMapper();

		final AccessRelation accessRelation = mapper.mapBToA(accessFlattenedData);

		Assert.assertEquals(Long.valueOf(0l), accessRelation.getId().getId());
		Assert.assertNull(accessRelation.getParentAccessRelation());
		Assert.assertEquals(accessRelation.getValidFrom(), validFrom);
		Assert.assertEquals(accessRelation.getValidTil(), validTil);
	}
}
