package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public class CapitalsourceIdMapper extends AbstractEntityIdMapper<CapitalsourceID, Long> {
}
