package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowReceiptID;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public class MoneyflowReceiptIdMapper extends AbstractEntityIdMapper<MoneyflowReceiptID, Long> {
}
