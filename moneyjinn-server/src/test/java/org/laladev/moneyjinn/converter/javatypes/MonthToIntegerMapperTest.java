package org.laladev.moneyjinn.converter.javatypes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.AbstractTest;

import jakarta.inject.Inject;

class MonthToIntegerMapperTest extends AbstractTest {
	@Inject
	YearToIntegerMapper yearToIntegerMapper;

	@Test
	void testNullYear() {
		Assertions.assertNull(this.yearToIntegerMapper.mapAToB(null));
	}

	@Test
	void testNullInteger() {
		Assertions.assertNull(this.yearToIntegerMapper.mapBToA(null));
	}

}
