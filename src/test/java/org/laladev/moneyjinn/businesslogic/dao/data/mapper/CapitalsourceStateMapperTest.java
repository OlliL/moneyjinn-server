package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import org.junit.Assert;
import org.junit.Test;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceState;
import org.laladev.moneyjinn.businesslogic.model.exception.TechnicalException;

public class CapitalsourceStateMapperTest {
	@Test
	public void testNullShort() {
		Assert.assertNull(CapitalsourceStateMapper.map((Short) null));
	}

	@Test
	public void testNullEnum() {
		Assert.assertNull(CapitalsourceStateMapper.map((CapitalsourceState) null));
	}

	@Test(expected = TechnicalException.class)
	public void testExceptionShort() {
		Assert.assertNull(CapitalsourceStateMapper.map(Short.valueOf("66")));

	}
}
