package org.laladev.moneyjinn.model.setting;

/**
 * <p>
 * This Setting describes if the selected source the default is file or import.
 * </p>
 *
 * @author olivleh1
 *
 */
public class ClientCompareDataSelectedSourceIsFile extends AbstractSetting<Boolean> {

	public ClientCompareDataSelectedSourceIsFile(final Boolean setting) {
		super.setSetting(setting);
	}

	@Override
	public SettingType getType() {
		return SettingType.CLIENT_COMPARE_DATA_SELECTED_SOURCE_IS_FILE;
	}

}
