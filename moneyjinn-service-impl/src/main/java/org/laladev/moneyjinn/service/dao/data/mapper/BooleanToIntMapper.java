package org.laladev.moneyjinn.service.dao.data.mapper;

import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public abstract class BooleanToIntMapper {
    private static final int TRUE = 1;
    private static final int FALSE = 0;

    public boolean mapBToA(final int b) {
        return TRUE == b;
    }

    public int mapAToB(final boolean a) {
        return a ? TRUE : FALSE;
    }
}
