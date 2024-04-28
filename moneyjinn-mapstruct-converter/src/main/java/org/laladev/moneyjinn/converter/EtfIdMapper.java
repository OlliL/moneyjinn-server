package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.laladev.moneyjinn.model.etf.EtfID;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface EtfIdMapper extends EntityIdMapper<EtfID, Long> {
}
