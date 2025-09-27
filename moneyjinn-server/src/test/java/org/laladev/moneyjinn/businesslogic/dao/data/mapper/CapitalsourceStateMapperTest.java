package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceState;
import org.laladev.moneyjinn.model.exception.TechnicalException;
import org.laladev.moneyjinn.service.dao.data.mapper.CapitalsourceStateMapper;

class CapitalsourceStateMapperTest {
    @Test
    void testNullInteger() {
        Assertions.assertNull(CapitalsourceStateMapper.map((Integer) null));
    }

    @Test
    void testNullEnum() {
        Assertions.assertNull(CapitalsourceStateMapper.map((CapitalsourceState) null));
    }

    @Test
    void test_unknownCapitalsourceState_exception() {
        final Integer state = Integer.valueOf("66");
        Assertions.assertThrows(TechnicalException.class, () -> CapitalsourceStateMapper.map(state));
    }
}
