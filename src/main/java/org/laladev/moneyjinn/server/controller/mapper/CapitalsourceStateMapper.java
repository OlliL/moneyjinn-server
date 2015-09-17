package org.laladev.moneyjinn.server.controller.mapper;

import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceState;
import org.laladev.moneyjinn.businesslogic.model.exception.TechnicalException;
import org.laladev.moneyjinn.core.error.ErrorCode;

public class CapitalsourceStateMapper {
	private static final Short NON_CACHE_SHORT = Short.valueOf((short) 1);
	private static final Short CACHE_SHORT = Short.valueOf((short) 2);

	private CapitalsourceStateMapper() {

	}

	public static CapitalsourceState map(final Short state) {
		if (state != null) {
			switch (state) {
			case 1:
				return CapitalsourceState.NON_CACHE;
			case 2:
				return CapitalsourceState.CACHE;
			default:
				throw new TechnicalException("State " + state + " not defined!", ErrorCode.UNKNOWN);
			}
		}
		return null;
	}

	public static Short map(final CapitalsourceState state) {
		if (state != null) {
			switch (state) {
			case NON_CACHE:
				return NON_CACHE_SHORT;
			case CACHE:
				return CACHE_SHORT;
			default:
				throw new TechnicalException("State " + state + " not defined!", ErrorCode.UNKNOWN);
			}
		}
		return null;
	}
}
