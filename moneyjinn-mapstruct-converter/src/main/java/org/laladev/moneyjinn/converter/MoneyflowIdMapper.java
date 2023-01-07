package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.model.moneyflow.MoneyflowID;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public class MoneyflowIdMapper extends AbstractEntityIdMapper<MoneyflowID, Long> {
}
