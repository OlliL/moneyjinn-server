package org.laladev.moneyjinn.converter.javatypes;

import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public abstract class BooleanToIntegerMapper {
    private static final Integer TRUE = 1;

    public boolean mapBToA(final Integer b) {
        if (b == null) {
            return false;
        }
        return TRUE.equals(b);
    }

    public Integer mapAToB(final boolean a) {
        return a ? TRUE : null;
    }
}
