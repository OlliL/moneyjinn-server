package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.laladev.moneyjinn.model.access.UserID;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface UserIdMapper extends EntityIdMapper<UserID, Long> {
}
