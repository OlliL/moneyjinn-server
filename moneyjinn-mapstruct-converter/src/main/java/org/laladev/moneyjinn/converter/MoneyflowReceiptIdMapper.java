package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.model.moneyflow.MoneyflowReceiptID;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public class MoneyflowReceiptIdMapper extends AbstractEntityIdMapper<MoneyflowReceiptID, Long> {
}
