package org.laladev.moneyjinn.converter.javatypes;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = ComponentModel.JAKARTA, unmappedTargetPolicy = ReportingPolicy.ERROR)
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
