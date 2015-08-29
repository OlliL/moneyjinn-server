package org.laladev.moneyjinn.businesslogic.dao.data.mapper;

import org.laladev.moneyjinn.businesslogic.model.setting.SettingType;

public class SettingTypeConverter {
	public static String getSettingNameByType(final SettingType type) {
		switch (type) {
		case CLIENT_MAX_ROWS:
			return "max_rows";
		case CLIENT_TREND_CAPITALSOURCEIDS:
			return "trend_capitalsourceid";
		case CLIENT_DATE_FORMAT:
			return "date_format";
		case CLIENT_DISPLAYED_LANGUAGE:
			return "displayed_language";
		case CLIENT_NUM_FREE_MONEYFLOWS:
			return "num_free_moneyflows";
		default:
			throw new UnsupportedOperationException("SettingType " + type + " unsupported!");
		}
	}

	public static SettingType getSettingTypeByName(final String string) {
		switch (string) {
		case "max_rows":
			return SettingType.CLIENT_MAX_ROWS;
		case "trend_capitalsourceid":
			return SettingType.CLIENT_TREND_CAPITALSOURCEIDS;
		case "date_format":
			return SettingType.CLIENT_DATE_FORMAT;
		case "displayed_language":
			return SettingType.CLIENT_DISPLAYED_LANGUAGE;
		case "num_free_moneyflows":
			return SettingType.CLIENT_NUM_FREE_MONEYFLOWS;
		default:
			throw new UnsupportedOperationException("SettingType " + string + " unsupported!");
		}
	}
}
