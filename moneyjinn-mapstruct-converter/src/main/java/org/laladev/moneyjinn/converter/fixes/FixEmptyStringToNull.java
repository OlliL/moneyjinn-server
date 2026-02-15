package org.laladev.moneyjinn.converter.fixes;

import org.mapstruct.Condition;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface FixEmptyStringToNull {
    @Condition
    default boolean isNotEmpty(final String value) {
        return value != null && !value.isEmpty();
    }
}
