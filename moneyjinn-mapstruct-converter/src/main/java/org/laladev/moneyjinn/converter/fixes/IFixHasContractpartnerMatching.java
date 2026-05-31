package org.laladev.moneyjinn.converter.fixes;

import org.laladev.moneyjinn.model.IHasContractpartnerMatching;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;

public interface IFixHasContractpartnerMatching {
    // work around https://github.com/mapstruct/mapstruct/issues/1166
    @AfterMapping
    default void fixHasContractpartnerMatching(@MappingTarget final IHasContractpartnerMatching entity) {
        if (entity.getContractpartnerMatching() != null && entity.getContractpartnerMatching().getId() == null) {
            entity.setContractpartnerMatching(null);
        }
    }
}
