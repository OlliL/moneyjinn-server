package org.laladev.moneyjinn.businesslogic.model.setting;

import org.laladev.moneyjinn.businesslogic.model.access.User;

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
public class ClientMaxRowsSetting extends AbstractSetting<Integer> {

	public ClientMaxRowsSetting(final Integer setting) {
		super.setSetting(setting);
	}

	@Override
	public SettingType getType() {
		return SettingType.CLIENT_MAX_ROWS;
	}

}
