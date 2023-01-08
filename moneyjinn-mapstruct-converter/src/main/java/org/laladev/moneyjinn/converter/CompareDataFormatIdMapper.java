package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.laladev.moneyjinn.model.comparedata.CompareDataFormatID;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public class CompareDataFormatIdMapper extends AbstractEntityIdMapper<CompareDataFormatID, Long> {
}
