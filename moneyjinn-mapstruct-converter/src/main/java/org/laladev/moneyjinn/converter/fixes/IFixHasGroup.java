package org.laladev.moneyjinn.converter.fixes;

import org.laladev.moneyjinn.model.IHasGroup;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;

public interface IFixHasGroup {
	// work around https://github.com/mapstruct/mapstruct/issues/1166
	@AfterMapping
	default void fixHasGroup(@MappingTarget final IHasGroup entity) {
		if (entity.getGroup() != null && entity.getGroup().getId() == null) {
			entity.setGroup(null);
		}
	}
}
