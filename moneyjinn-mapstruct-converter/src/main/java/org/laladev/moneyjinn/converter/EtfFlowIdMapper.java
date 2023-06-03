package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.laladev.moneyjinn.model.etf.EtfFlowID;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface EtfFlowIdMapper extends EntityIdMapper<EtfFlowID, Long> {
}
