package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.model.PostingAccountID;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public class PostingAccountIdMapper extends AbstractEntityIdMapper<PostingAccountID, Long> {
}
