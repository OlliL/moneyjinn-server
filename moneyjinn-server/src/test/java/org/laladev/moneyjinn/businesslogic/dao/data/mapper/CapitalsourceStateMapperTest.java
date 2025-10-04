package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceState;
import org.laladev.moneyjinn.model.exception.TechnicalException;
import org.laladev.moneyjinn.service.dao.data.mapper.CapitalsourceStateMapper;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CapitalsourceStateMapperTest {
    @Test
    void testNullInteger() {
        assertNull(CapitalsourceStateMapper.map((Integer) null));
    }

    @Test
    void testNullEnum() {
        assertNull(CapitalsourceStateMapper.map((CapitalsourceState) null));
    }

    @Test
    void test_unknownCapitalsourceState_exception() {
        assertThrows(TechnicalException.class, () -> CapitalsourceStateMapper.map(66));
    }
}
