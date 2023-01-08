package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.laladev.moneyjinn.model.monthlysettlement.ImportedMonthlySettlementID;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public class ImportedMonthlySettlementIdMapper
    extends AbstractEntityIdMapper<ImportedMonthlySettlementID, Long> {
}
