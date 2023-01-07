package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.model.moneyflow.MoneyflowSplitEntryID;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public class MoneyflowSplitEntryIdMapper
    extends AbstractEntityIdMapper<MoneyflowSplitEntryID, Long> {
}
