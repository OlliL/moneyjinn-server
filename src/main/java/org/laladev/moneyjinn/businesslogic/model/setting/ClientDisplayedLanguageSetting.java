package org.laladev.moneyjinn.businesslogic.model.setting;

/**
 * <p>
 * This Setting describes the client language the user has choosen.
 * </p>
 *
 * @author olivleh1
 *
 */
public class ClientDisplayedLanguageSetting extends AbstractSetting<Integer> {

	public ClientDisplayedLanguageSetting(final Integer setting) {
		super.setSetting(setting);
	}

	@Override
	public SettingType getType() {
		return SettingType.CLIENT_DISPLAYED_LANGUAGE;
	}

}
