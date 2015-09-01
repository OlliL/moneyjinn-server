package org.laladev.moneyjinn.businesslogic.model.setting;

/**
 * <p>
 * This Setting describes the number of empty rows displayed for adding moneyflows
 * </p>
 *
 * @author olivleh1
 *
 */
public class ClientNumFreeMoneyflowsSetting extends AbstractSetting<Integer> {

	public ClientNumFreeMoneyflowsSetting(final Integer setting) {
		super.setSetting(setting);
	}

	@Override
	public SettingType getType() {
		return SettingType.CLIENT_NUM_FREE_MONEYFLOWS;
	}

}
