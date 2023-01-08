package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.laladev.moneyjinn.model.moneyflow.ImportedMoneyflowReceiptID;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public class ImportedMoneyflowReceiptIdMapper
    extends AbstractEntityIdMapper<ImportedMoneyflowReceiptID, Long> {
}
