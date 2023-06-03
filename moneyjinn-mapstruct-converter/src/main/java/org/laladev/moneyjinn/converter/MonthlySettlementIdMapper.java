package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.laladev.moneyjinn.model.monthlysettlement.MonthlySettlementID;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface MonthlySettlementIdMapper extends EntityIdMapper<MonthlySettlementID, Long> {
}
