package org.laladev.moneyjinn.businesslogic.model.setting;

/**
 * <p>
 * This Setting describes the client language the user has choosen.
 * </p>
 *
 * @deprecated Client Settings should be stored on client side!
 * @author olivleh1
 *
 */
@Deprecated
public class ClientDisplayedLanguageSetting extends AbstractSetting<Integer> {

	public ClientDisplayedLanguageSetting(final Integer setting) {
		super.setSetting(setting);
	}

	@Override
	public SettingType getType() {
		return SettingType.CLIENT_DISPLAYED_LANGUAGE;
	}

}
