package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.model.access.UserID;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public class UserIdMapper extends AbstractEntityIdMapper<UserID, Long> {
}
