package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.laladev.moneyjinn.model.ContractpartnerID;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public class ContractpartnerIdMapper extends AbstractEntityIdMapper<ContractpartnerID, Long> {
}
