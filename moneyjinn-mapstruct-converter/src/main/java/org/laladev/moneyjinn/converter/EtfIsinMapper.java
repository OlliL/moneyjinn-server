package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.model.etf.EtfIsin;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public class EtfIsinMapper extends AbstractEntityIdMapper<EtfIsin, String> {
}
