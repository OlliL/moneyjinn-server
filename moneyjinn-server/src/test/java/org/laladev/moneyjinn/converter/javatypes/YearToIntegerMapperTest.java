package org.laladev.moneyjinn.converter.javatypes;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.AbstractTest;

class YearToIntegerMapperTest extends AbstractTest {
    @Inject
    MonthToIntegerMapper monthToIntegerMapper;

    @Test
    void testNullMonth() {
        Assertions.assertNull(this.monthToIntegerMapper.mapAToB(null));
    }

    @Test
    void testNullInteger() {
        Assertions.assertNull(this.monthToIntegerMapper.mapBToA(null));
    }
}
