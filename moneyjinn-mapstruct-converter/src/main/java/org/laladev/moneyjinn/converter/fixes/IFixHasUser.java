package org.laladev.moneyjinn.converter.fixes;

import org.laladev.moneyjinn.model.IHasUser;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;

public interface IFixHasUser {
    // work around https://github.com/mapstruct/mapstruct/issues/1166
    @AfterMapping
    default void fixHasUser(@MappingTarget final IHasUser entity) {
        if (entity.getUser() != null && entity.getUser().getId() == null) {
            entity.setUser(null);
        }
    }
}
