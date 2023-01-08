package org.laladev.moneyjinn.converter.javatypes;

import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public abstract class BooleanToShortMapper {
  private static Short TRUE = Short.valueOf((short) 1);

  public boolean mapBToA(final Short b) {
    if (b == null) {
      return false;
    }
    return TRUE.equals(b);
  }

  public Short mapAToB(final boolean a) {
    return a ? TRUE : null;
  }
}
