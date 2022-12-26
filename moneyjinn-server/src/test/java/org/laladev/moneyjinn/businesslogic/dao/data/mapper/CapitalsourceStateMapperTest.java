
package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceState;
import org.laladev.moneyjinn.model.exception.TechnicalException;
import org.laladev.moneyjinn.service.dao.data.mapper.CapitalsourceStateMapper;

public class CapitalsourceStateMapperTest {
  @Test
  public void testNullShort() {
    Assertions.assertNull(CapitalsourceStateMapper.map((Short) null));
  }

  @Test
  public void testNullEnum() {
    Assertions.assertNull(CapitalsourceStateMapper.map((CapitalsourceState) null));
  }

  @Test
  public void test_unknownCapitalsourceState_exception() {
    Assertions.assertThrows(TechnicalException.class, () -> {
      Assertions.assertNull(CapitalsourceStateMapper.map(Short.valueOf("66")));
    });
  }
}
