package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.model.monthlysettlement.MonthlySettlementID;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public class MonthlySettlementIdMapper extends AbstractEntityIdMapper<MonthlySettlementID, Long> {
}
