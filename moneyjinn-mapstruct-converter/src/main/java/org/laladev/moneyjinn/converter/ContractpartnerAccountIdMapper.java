package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.model.ContractpartnerAccountID;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public class ContractpartnerAccountIdMapper
    extends AbstractEntityIdMapper<ContractpartnerAccountID, Long> {
}
