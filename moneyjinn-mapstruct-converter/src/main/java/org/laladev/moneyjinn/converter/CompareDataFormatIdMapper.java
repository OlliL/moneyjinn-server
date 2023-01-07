package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.model.comparedata.CompareDataFormatID;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = ComponentModel.JAKARTA, unmappedTargetPolicy = ReportingPolicy.ERROR)
public class CompareDataFormatIdMapper extends AbstractEntityIdMapper<CompareDataFormatID, Long> {
}
