package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowID;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public class MoneyflowIdMapper extends AbstractEntityIdMapper<MoneyflowID, Long> {
}
