package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.model.access.GroupID;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public class GroupIdMapper extends AbstractEntityIdMapper<GroupID, Long> {
}
