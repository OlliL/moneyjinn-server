package org.laladev.moneyjinn.converter.config;

import org.laladev.moneyjinn.converter.fixes.FixEmptyStringToNull;
import org.mapstruct.MapperConfig;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.control.NoComplexMapping;

@MapperConfig(componentModel = ComponentModel.JAKARTA, unmappedTargetPolicy = ReportingPolicy.ERROR,
        uses = FixEmptyStringToNull.class, mappingControl = NoComplexMapping.class)
public interface MapStructConfig {

}
