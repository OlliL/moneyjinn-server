package org.laladev.moneyjinn.converter.javatypes;

import java.time.Month;

import org.laladev.moneyjinn.converter.config.MapStructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface MonthToIntegerMapper {
	public default Month mapBToA(final Integer b) {
		return b == null ? null : Month.of(b);
	}

	public default Integer mapAToB(final Month a) {
		return a == null ? null : a.getValue();
	}
}
