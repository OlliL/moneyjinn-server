package org.laladev.moneyjinn.converter.javatypes;

import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.mapstruct.Mapper;

import java.time.Year;

@Mapper(config = MapStructConfig.class)
public interface YearToIntegerMapper {
    default Year mapBToA(final Integer b) {
        return b == null ? null : Year.of(b);
    }

    default Integer mapAToB(final Year a) {
        return a == null ? null : a.getValue();
    }
}
