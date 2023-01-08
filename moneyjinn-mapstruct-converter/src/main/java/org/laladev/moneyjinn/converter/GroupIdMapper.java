package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.laladev.moneyjinn.model.access.GroupID;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public class GroupIdMapper extends AbstractEntityIdMapper<GroupID, Long> {
}
