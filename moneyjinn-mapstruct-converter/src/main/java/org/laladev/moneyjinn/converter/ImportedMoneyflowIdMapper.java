package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowID;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public class ImportedMoneyflowIdMapper extends AbstractEntityIdMapper<ImportedMoneyflowID, Long> {
}
