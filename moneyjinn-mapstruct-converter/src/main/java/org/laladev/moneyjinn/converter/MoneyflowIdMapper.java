package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowID;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface MoneyflowIdMapper extends EntityIdMapper<MoneyflowID, Long> {
}
