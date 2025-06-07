package org.laladev.moneyjinn.converter.javatypes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.AbstractTest;

import jakarta.inject.Inject;

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
