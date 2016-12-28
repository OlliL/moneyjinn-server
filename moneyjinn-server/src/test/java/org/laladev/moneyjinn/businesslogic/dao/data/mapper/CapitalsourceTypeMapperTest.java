package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import org.junit.Assert;
import org.junit.Test;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceType;
import org.laladev.moneyjinn.model.exception.TechnicalException;

public class CapitalsourceTypeMapperTest {
	@Test
	public void testNullShort() {
		Assert.assertNull(CapitalsourceTypeMapper.map((Short) null));
	}

	@Test
	public void testNullEnum() {
		Assert.assertNull(CapitalsourceTypeMapper.map((CapitalsourceType) null));
	}

	@Test(expected = TechnicalException.class)
	public void test_unknownCapitalsourceType_exception() {
		Assert.assertNull(CapitalsourceTypeMapper.map(Short.valueOf("66")));

	}
}
