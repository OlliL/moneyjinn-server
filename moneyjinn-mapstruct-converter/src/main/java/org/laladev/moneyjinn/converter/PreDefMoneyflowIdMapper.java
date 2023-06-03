package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.laladev.moneyjinn.model.PreDefMoneyflowID;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface PreDefMoneyflowIdMapper extends EntityIdMapper<PreDefMoneyflowID, Long> {
}
