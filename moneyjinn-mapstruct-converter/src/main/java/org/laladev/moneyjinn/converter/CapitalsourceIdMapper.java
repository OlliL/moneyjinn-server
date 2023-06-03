package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.laladev.moneyjinn.model.capitalsource.CapitalsourceID;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface CapitalsourceIdMapper extends EntityIdMapper<CapitalsourceID, Long> {
}
