package org.laladev.moneyjinn.converter.javatypes;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface LocalDateTimeToOffsetDateTimeMapper {
	public default LocalDateTime mapBToA(final OffsetDateTime b) {
		return b == null ? null : b.toLocalDateTime();
	}

	public default OffsetDateTime mapAToB(final LocalDateTime a) {
		return a == null ? null : a.atOffset(ZoneOffset.UTC);
	}
}
