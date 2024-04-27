package org.laladev.moneyjinn.converter.javatypes;

import java.time.Year;

import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface YearToIntegerMapper {
	public default Year mapBToA(final Integer b) {
		return b == null ? null : Year.of(b);
	}

	public default Integer mapAToB(final Year a) {
		return a == null ? null : a.getValue();
	}
}
