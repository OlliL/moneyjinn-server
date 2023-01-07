package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.model.comparedata.CompareDataFormatID;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public class CompareDataFormatIdMapper extends AbstractEntityIdMapper<CompareDataFormatID, Long> {
}
