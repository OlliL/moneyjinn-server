package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.model.etf.EtfFlowID;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public class EtfFlowIdMapper extends AbstractEntityIdMapper<EtfFlowID, Long> {
}
