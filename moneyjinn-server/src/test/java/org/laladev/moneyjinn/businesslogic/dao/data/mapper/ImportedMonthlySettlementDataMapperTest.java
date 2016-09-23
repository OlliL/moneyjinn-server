package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import java.time.Month;

import org.junit.Assert;
import org.junit.Test;
import org.laladev.moneyjinn.businesslogic.dao.data.ImportedMonthlySettlementData;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.businesslogic.model.monthlysettlement.ImportedMonthlySettlement;

public class ImportedMonthlySettlementDataMapperTest {
	@Test
	public void testWithNullID() {
		final ImportedMonthlySettlement importedMonthlySettlement = new ImportedMonthlySettlement();
		importedMonthlySettlement.setMonth(Month.JANUARY);
		importedMonthlySettlement.setCapitalsource(new Capitalsource(new CapitalsourceID(1L)));

		final ImportedMonthlySettlementDataMapper importedMonthlySettlementDataMapper = new ImportedMonthlySettlementDataMapper();
		final ImportedMonthlySettlementData importedMonthlySettlementData = importedMonthlySettlementDataMapper
				.mapAToB(importedMonthlySettlement);

		Assert.assertNull(importedMonthlySettlementData.getId());
	}
}