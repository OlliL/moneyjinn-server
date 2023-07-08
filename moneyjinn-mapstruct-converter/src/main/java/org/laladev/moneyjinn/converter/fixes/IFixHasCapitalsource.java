package org.laladev.moneyjinn.converter.fixes;

import org.laladev.moneyjinn.model.IHasCapitalsource;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;

public interface IFixHasCapitalsource {
  // work around https://github.com/mapstruct/mapstruct/issues/1166
  @AfterMapping
  default void fixHasCapitalsource(@MappingTarget final IHasCapitalsource entity) {
    if (entity.getCapitalsource() != null && entity.getCapitalsource().getId() == null) {
      entity.setCapitalsource(null);
    }
  }
}
