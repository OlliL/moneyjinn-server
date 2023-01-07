package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.model.PreDefMoneyflowID;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public class PreDefMoneyflowIdMapper extends AbstractEntityIdMapper<PreDefMoneyflowID, Long> {
}
