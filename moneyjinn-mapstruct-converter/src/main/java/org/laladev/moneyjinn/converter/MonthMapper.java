package org.laladev.moneyjinn.converter;

import java.time.Month;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public class MonthMapper {
  public Month mapBToA(final Short b) {
    return b == null ? null : Month.of(b.intValue());
  }

  public Short mapAToB(final Month a) {
    return a == null ? null : (short) a.getValue();
  }
}
