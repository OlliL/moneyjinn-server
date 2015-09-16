package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import org.laladev.moneyjinn.businesslogic.model.ErrorCode;
import org.laladev.moneyjinn.businesslogic.model.capitalsource.CapitalsourceType;
import org.laladev.moneyjinn.businesslogic.model.exception.TechnicalException;

public class CapitalsourceTypeMapper {
	private static final Short CURRENT_ASSET_SHORT = Short.valueOf((short) 1);
	private static final Short LONG_TERM_ASSET_SHORT = Short.valueOf((short) 2);
	private static final Short RESERVE_ASSET_SHORT = Short.valueOf((short) 3);
	private static final Short PROVISION_ASSET_SHORT = Short.valueOf((short) 4);

	private CapitalsourceTypeMapper() {

	}

	public static CapitalsourceType map(final Short type) {
		if (type != null) {
			switch (type) {
			case 1:
				return CapitalsourceType.CURRENT_ASSET;
			case 2:
				return CapitalsourceType.LONG_TERM_ASSET;
			case 3:
				return CapitalsourceType.RESERVE_ASSET;
			case 4:
				return CapitalsourceType.PROVISION_ASSET;
			default:
				throw new TechnicalException("Type " + type + " not defined!", ErrorCode.UNKNOWN);
			}
		}
		return null;
	}

	public static Short map(final CapitalsourceType type) {
		if (type != null) {
			switch (type) {
			case CURRENT_ASSET:
				return CURRENT_ASSET_SHORT;
			case LONG_TERM_ASSET:
				return LONG_TERM_ASSET_SHORT;
			case RESERVE_ASSET:
				return RESERVE_ASSET_SHORT;
			case PROVISION_ASSET:
				return PROVISION_ASSET_SHORT;
			default:
				throw new TechnicalException("Type " + type + " not defined!", ErrorCode.UNKNOWN);
			}
		}
		return null;
	}

}
