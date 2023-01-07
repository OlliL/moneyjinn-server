package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.model.etf.EtfIsin;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = ComponentModel.JAKARTA, unmappedTargetPolicy = ReportingPolicy.ERROR)
public class EtfIsinMapper extends AbstractEntityIdMapper<EtfIsin, String> {
}
