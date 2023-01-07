package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.model.PreDefMoneyflowID;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = ComponentModel.JAKARTA, unmappedTargetPolicy = ReportingPolicy.ERROR)
public class PreDefMoneyflowIdMapper extends AbstractEntityIdMapper<PreDefMoneyflowID, Long> {
}
