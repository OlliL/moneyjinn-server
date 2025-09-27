package org.laladev.moneyjinn.converter.javatypes;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.AbstractTest;

class LocalDateTimeToOffsetDateTimeMapperTest extends AbstractTest {
    @Inject
    LocalDateTimeToOffsetDateTimeMapper localDateTimeToOffsetDateTimeMapper;

    @Test
    void testNullOffsetDateTime() {
        Assertions.assertNull(this.localDateTimeToOffsetDateTimeMapper.mapAToB(null));
    }

    @Test
    void testNullLocalDateTime() {
        Assertions.assertNull(this.localDateTimeToOffsetDateTimeMapper.mapBToA(null));
    }
}
