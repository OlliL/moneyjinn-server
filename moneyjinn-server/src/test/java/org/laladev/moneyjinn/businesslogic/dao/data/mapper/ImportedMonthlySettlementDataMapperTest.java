
package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import java.time.Month;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.monthlysettlement.ImportedMonthlySettlement;
import org.laladev.moneyjinn.service.dao.data.ImportedMonthlySettlementData;
import org.laladev.moneyjinn.service.dao.data.mapper.ImportedMonthlySettlementDataMapper;

public class ImportedMonthlySettlementDataMapperTest {
  @Test
  public void testWithNullId() {
    final ImportedMonthlySettlement importedMonthlySettlement = new ImportedMonthlySettlement();
    importedMonthlySettlement.setMonth(Month.JANUARY);
    importedMonthlySettlement.setCapitalsource(new Capitalsource(new CapitalsourceID(1L)));
    final ImportedMonthlySettlementDataMapper importedMonthlySettlementDataMapper = new ImportedMonthlySettlementDataMapper();
    final ImportedMonthlySettlementData importedMonthlySettlementData = importedMonthlySettlementDataMapper
        .mapAToB(importedMonthlySettlement);
    Assertions.assertNull(importedMonthlySettlementData.getId());
  }
}
