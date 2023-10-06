
package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceType;
import org.laladev.moneyjinn.model.exception.TechnicalException;
import org.laladev.moneyjinn.service.dao.data.mapper.CapitalsourceTypeMapper;

class CapitalsourceTypeMapperTest {
	@Test
	void testNullInteger() {
		Assertions.assertNull(CapitalsourceTypeMapper.map((Integer) null));
	}

	@Test
	void testNullEnum() {
		Assertions.assertNull(CapitalsourceTypeMapper.map((CapitalsourceType) null));
	}

	@Test
	void test_unknownCapitalsourceType_exception() {
		final Integer type = Integer.valueOf("66");
		Assertions.assertThrows(TechnicalException.class, () -> {
			CapitalsourceTypeMapper.map(type);
		});
	}
}
