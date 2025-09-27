package org.laladev.moneyjinn.converter.javatypes;

import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(config = MapStructConfig.class)
public interface LocalDateTimeToOffsetDateTimeMapper {
    default LocalDateTime mapBToA(final OffsetDateTime b) {
        return b == null ? null : b.toLocalDateTime();
    }

    default OffsetDateTime mapAToB(final LocalDateTime a) {
        return a == null ? null : a.atOffset(ZoneOffset.UTC);
    }
}
