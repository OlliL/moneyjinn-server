package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.laladev.moneyjinn.model.PostingAccountID;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface PostingAccountIdMapper extends EntityIdMapper<PostingAccountID, Long> {
}
