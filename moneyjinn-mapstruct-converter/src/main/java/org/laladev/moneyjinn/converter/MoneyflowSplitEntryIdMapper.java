package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.laladev.moneyjinn.model.moneyflow.MoneyflowSplitEntryID;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public class MoneyflowSplitEntryIdMapper
    extends AbstractEntityIdMapper<MoneyflowSplitEntryID, Long> {
}
