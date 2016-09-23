package org.laladev.moneyjinn.model.setting;

import org.laladev.moneyjinn.model.access.User;

/**
 * <p>
 * This Setting describes the maximum number of entities the user wants to see in the "default view"
 * when listing Entites ({@link User}, {@link Capitalsource}, ...) before the default switches to
 * "show nothing".
 * </p>
 * <p>
 * This helps to prevent showing a massive amount of data when navigating into the entity for just
 * looking up a specific entity
 * </p>
 *
 * @author olivleh1
 *
 */
public class ClientDateFormatSetting extends AbstractSetting<String> {

	public ClientDateFormatSetting(final String setting) {
		super.setSetting(setting);
	}

	@Override
	public SettingType getType() {
		return SettingType.CLIENT_DATE_FORMAT;
	}

}
