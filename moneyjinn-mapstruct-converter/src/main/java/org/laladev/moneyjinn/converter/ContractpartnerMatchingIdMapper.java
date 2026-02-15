package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.laladev.moneyjinn.model.ContractpartnerMatchingID;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface ContractpartnerMatchingIdMapper extends EntityIdMapper<ContractpartnerMatchingID, Long> {
}
