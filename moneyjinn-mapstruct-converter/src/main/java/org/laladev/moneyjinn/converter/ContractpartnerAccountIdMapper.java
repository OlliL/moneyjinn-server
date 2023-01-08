package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.laladev.moneyjinn.model.ContractpartnerAccountID;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public class ContractpartnerAccountIdMapper
    extends AbstractEntityIdMapper<ContractpartnerAccountID, Long> {
}
