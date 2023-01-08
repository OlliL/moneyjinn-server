package org.laladev.moneyjinn.converter.config;

import org.mapstruct.MapperConfig;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@MapperConfig(componentModel = ComponentModel.JAKARTA, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MapStructConfig {

}
