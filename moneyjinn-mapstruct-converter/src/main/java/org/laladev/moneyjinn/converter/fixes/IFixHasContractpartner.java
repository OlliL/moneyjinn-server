package org.laladev.moneyjinn.converter.fixes;

import org.laladev.moneyjinn.model.IHasContractpartner;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;

public interface IFixHasContractpartner {
  // work around https://github.com/mapstruct/mapstruct/issues/1166
  @AfterMapping
  default void fixHasContractpartner(@MappingTarget final IHasContractpartner entity) {
    if (entity.getContractpartner() != null && entity.getContractpartner().getId() == null) {
      entity.setContractpartner(null);
    }
  }
}
