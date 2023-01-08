package org.laladev.moneyjinn.converter;

import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.laladev.moneyjinn.model.etf.EtfIsin;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public class EtfIsinMapper extends AbstractEntityIdMapper<EtfIsin, String> {
}
