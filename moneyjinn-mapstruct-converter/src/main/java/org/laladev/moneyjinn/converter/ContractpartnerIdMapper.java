package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.model.ContractpartnerID;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public class ContractpartnerIdMapper extends AbstractEntityIdMapper<ContractpartnerID, Long> {
}
