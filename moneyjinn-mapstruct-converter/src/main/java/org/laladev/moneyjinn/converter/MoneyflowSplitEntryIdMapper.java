package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.model.moneyflow.MoneyflowSplitEntryID;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = ComponentModel.JAKARTA, unmappedTargetPolicy = ReportingPolicy.ERROR)
public class MoneyflowSplitEntryIdMapper
    extends AbstractEntityIdMapper<MoneyflowSplitEntryID, Long> {
}
