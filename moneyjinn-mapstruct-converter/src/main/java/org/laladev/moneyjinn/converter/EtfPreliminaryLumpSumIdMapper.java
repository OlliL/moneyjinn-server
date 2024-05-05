package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.laladev.moneyjinn.model.etf.EtfPreliminaryLumpSumID;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface EtfPreliminaryLumpSumIdMapper extends EntityIdMapper<EtfPreliminaryLumpSumID, Long> {
}
