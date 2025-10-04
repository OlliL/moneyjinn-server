package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceType;
import org.laladev.moneyjinn.model.exception.TechnicalException;
import org.laladev.moneyjinn.service.dao.data.mapper.CapitalsourceTypeMapper;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CapitalsourceTypeMapperTest {
    @Test
    void testNullInteger() {
        assertNull(CapitalsourceTypeMapper.map((Integer) null));
    }

    @Test
    void testNullEnum() {
        assertNull(CapitalsourceTypeMapper.map((CapitalsourceType) null));
    }

    @Test
    void test_unknownCapitalsourceType_exception() {
        assertThrows(TechnicalException.class, () -> CapitalsourceTypeMapper.map(66));
    }
}
