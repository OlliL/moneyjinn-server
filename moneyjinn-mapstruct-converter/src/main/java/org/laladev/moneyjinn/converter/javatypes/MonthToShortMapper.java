package org.laladev.moneyjinn.converter.javatypes;

import java.time.Month;
import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public abstract class MonthToShortMapper {
  public Month mapBToA(final Short b) {
    return b == null ? null : Month.of(b.intValue());
  }

  public Short mapAToB(final Month a) {
    return a == null ? null : (short) a.getValue();
  }
}
