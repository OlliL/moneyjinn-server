
package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceType;
import org.laladev.moneyjinn.model.exception.TechnicalException;
import org.laladev.moneyjinn.service.dao.data.mapper.CapitalsourceTypeMapper;

public class CapitalsourceTypeMapperTest {
  @Test
  public void testNullInteger() {
    Assertions.assertNull(CapitalsourceTypeMapper.map((Integer) null));
  }

  @Test
  public void testNullEnum() {
    Assertions.assertNull(CapitalsourceTypeMapper.map((CapitalsourceType) null));
  }

  @Test
  public void test_unknownCapitalsourceType_exception() {
    Assertions.assertThrows(TechnicalException.class, () -> {
      Assertions.assertNull(CapitalsourceTypeMapper.map(Integer.valueOf("66")));
    });
  }
}
