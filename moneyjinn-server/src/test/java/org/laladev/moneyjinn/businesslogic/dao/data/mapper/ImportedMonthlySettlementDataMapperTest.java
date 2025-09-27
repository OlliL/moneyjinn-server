package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.AbstractTest;
import org.laladev.moneyjinn.model.capitalsource.Capitalsource;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.laladev.moneyjinn.model.monthlysettlement.ImportedMonthlySettlement;
import org.laladev.moneyjinn.service.dao.data.ImportedMonthlySettlementData;
import org.laladev.moneyjinn.service.dao.data.mapper.ImportedMonthlySettlementDataMapper;

import java.time.Month;

class ImportedMonthlySettlementDataMapperTest extends AbstractTest {
    @Inject
    private ImportedMonthlySettlementDataMapper importedMonthlySettlementDataMapper;

    @Test
    void testWithNullId() {
        final ImportedMonthlySettlement importedMonthlySettlement = new ImportedMonthlySettlement();
        importedMonthlySettlement.setMonth(Month.JANUARY);
        importedMonthlySettlement.setCapitalsource(new Capitalsource(new CapitalsourceID(1L)));
        final ImportedMonthlySettlementData importedMonthlySettlementData = this.importedMonthlySettlementDataMapper
                .mapAToB(importedMonthlySettlement);
        Assertions.assertNull(importedMonthlySettlementData.getId());
    }
}
