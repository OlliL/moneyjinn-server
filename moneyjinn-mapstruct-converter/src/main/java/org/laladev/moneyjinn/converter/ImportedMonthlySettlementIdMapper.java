package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.model.monthlysettlement.ImportedMonthlySettlementID;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = ComponentModel.JAKARTA, unmappedTargetPolicy = ReportingPolicy.ERROR)
public class ImportedMonthlySettlementIdMapper
    extends AbstractEntityIdMapper<ImportedMonthlySettlementID, Long> {
}
