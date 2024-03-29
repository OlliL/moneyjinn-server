package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.laladev.moneyjinn.model.access.AccessID;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface AccessIdMapper extends EntityIdMapper<AccessID, Long> {
}
