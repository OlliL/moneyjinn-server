package org.laladev.moneyjinn.converter.javatypes;

import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.mapstruct.Mapper;

import java.time.Month;

@Mapper(config = MapStructConfig.class)
public interface MonthToIntegerMapper {
    default Month mapBToA(final Integer b) {
        return b == null ? null : Month.of(b);
    }

    default Integer mapAToB(final Month a) {
        return a == null ? null : a.getValue();
    }
}
